package com.fulaan.zouban.controller.jinyuan;

import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.fulaan.classroom.service.ClassroomService;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.myschool.controller.GradeView;
import com.fulaan.school.service.SchoolService;
import com.fulaan.utils.StringUtil;
import com.fulaan.zouban.dto.*;
import com.fulaan.zouban.service.*;
import com.pojo.app.SessionValue;
import com.pojo.classroom.ClassroomEntry;
import com.pojo.school.ClassEntry;
import com.pojo.user.UserRole;
import com.pojo.zouban.ZouBanCourseEntry;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fl on 2016/7/26.
 */
@Controller
@RequestMapping("/zouban/kebiao")
public class StepFiveController extends BaseController {
    @Autowired
    private TimeTableService timeTableService;
    @Autowired
    private ClassService classService;
    @Autowired
    private ClassroomService classroomService;
    @Autowired
    private ZoubanStateService zoubanStateService;
    @Autowired
    private TermService termService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private ZoubanModeService zoubanModeService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private BianbanService bianbanService;



    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping
    public String indexPage(String term, String gid, String gnm, Model model, HttpServletResponse response) {
        int curweek = termService.findWeekIndex(getSessionValue().getSchoolId(), term);
        model.addAttribute("year", term);
        model.addAttribute("gradeId", gid);
        model.addAttribute("gradeName", gnm);
        model.addAttribute("curweek", curweek);
        int mode = zoubanModeService.getGradeMode(getSchoolId().toString(), gid);
        if (mode == -1) {
            try {
                response.sendRedirect("/user/homepage.do");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            model.addAttribute("mode", mode);
        }

        return "zoubannew/admin/kebiao";
    }


    /**
     * 获取当前学期总周数
     *
     * @param term
     * @return
     */
    @RequestMapping("/getAllWeek")
    @ResponseBody
    public int getAllWeek(String term) {
        return termService.getAllWeekByTerm(term, getSchoolId());
    }


    /**
     * 管理员====查看课表====行政班课表
     * 获取行政班课表，同班主任获取课表-----finish
     *
     * @param classId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.LEADER_CLASS, UserRole.HEADMASTER, UserRole.TEACHER})
    @RequestMapping("/getClassTimeTable")
    @ResponseBody
    public Map<String, Object> getClassTimeTable(@RequestParam String year, String term, @RequestParam String gradeId, @RequestParam String classId,
                                                 @RequestParam int type, @RequestParam int week) {
        if (type == 0) {
            int state = zoubanStateService.getZoubanState(year, getSessionValue().getSchoolId(), gradeId);
            if (state < 6)
                return null;
        }
        if (StringUtils.isBlank(classId)) {
            ObjectId userId = getUserId();
            List<ClassEntry> classInfoDTOList = classService.findClassEntryByMasterId(userId);
            if (classInfoDTOList != null && !classInfoDTOList.isEmpty()) {
                for (ClassEntry classInfoDTO : classInfoDTOList) {
                    if (classInfoDTO.getMaster().equals(userId)) {
                        classId = classInfoDTO.getID().toString();
                        break;
                    }
                }
            }
        }
        if (term.equals("")) {
            term = year;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        List<StudentTimeTable> list = timeTableService.getClassTimeTable(term, classId, type, week);
        map.put("course", list);
        CourseConfDTO courseConfDTO = timeTableService.findCourseConfByGradeId(year, new ObjectId(gradeId));
        map.put("conf", courseConfDTO);
        return map;
    }

    /**
     * 管理员====查看课表====教学班课表
     * 根据选定的课程id获取教学班课表----finish
     *
     * @param courseId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/getCourseTimeTable")
    @ResponseBody
    public Map<String, Object> getCourseTimeTable(@RequestParam String term, String year, @RequestParam String courseId,
                                                  @RequestParam String gradeId, @RequestParam int week) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<PointJson> pointList = timeTableService.getCourseTimeTable(term, courseId, gradeId, 1, week);
        if (pointList == null || pointList.isEmpty()) {
            pointList = timeTableService.getCourseTimeTable(term, courseId, gradeId, 0, week);
        }
        map.put("point", pointList);
        CourseConfDTO courseConfDTO = timeTableService.findCourseConfByGradeId(year, new ObjectId(gradeId));
        map.put("conf", courseConfDTO);
        ZouBanCourseEntry zouBanCourseEntry = timeTableService.getSubjectConf(courseId);
        map.put("teacher", zouBanCourseEntry.getTeacherName());
        ClassroomEntry classroomEntry = classroomService.findEntryById(zouBanCourseEntry.getClassRoomId());
        if (classroomEntry != null)
            map.put("classroom", classroomEntry.getRoomName());
        else
            map.put("classroom", "");
        return map;
    }

    /**
     * 管理员====查看课表====教师课表
     * 任课教师====课表
     * 获取任课老师的课表,获取本人课表时teacherId传递""即可----finish
     *
     * @param teacherId
     * @return
     */
    @UserRoles(value = {UserRole.TEACHER, UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/getTeacherTimeTable")
    @ResponseBody
    public Map<String, Object> getTeacherTimeTable(@RequestParam String term, String year, @RequestParam(required = false, defaultValue = "") String teacherId,
                                                   @RequestParam String gradeId, @RequestParam int week) {
        if (teacherId.equals("")) {
            teacherId = getUserId().toString();
        }
        return timeTableService.getTeacherTimeTable(term, year, teacherId, gradeId, week, getUserId().toString(), getSessionValue().getSchoolId());
    }


    /**
     * 获取当前学年
     *
     * @return
     */
    @RequestMapping("/getCurrTerm")
    @ResponseBody
    public Map<String, Object> getCurrTerm() {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> map = termService.getCurrentTerm(getSessionValue().getSchoolId().toString());
        //获取当前日期是第几周
        int currWeek = termService.findWeekIndex(getSessionValue().getSchoolId().toString(), (String) map.get("year"));
        result.put("message", (String) map.get("term"));
        result.put("currWeek", currWeek);
        String term = (String) map.get("term");
//        if(term.contains("第一学期")){
//            result.put("flag",1);
//        }else{
//            result.put("flag",2);
//        }
        result.put("integers", termService.getAllWeekByTerm(term, getSchoolId()));
        return result;
    }

    /**
     * 获取当前学期学生的课表
     *
     * @param week
     * @return
     */
    @UserRoles(value = {UserRole.STUDENT, UserRole.PARENT, UserRole.LEADER_CLASS, UserRole.ADMIN, UserRole.HEADMASTER, UserRole.TEACHER})
    @RequestMapping("/getStudentCurrTimeTable")
    @ResponseBody
    public Map<String, Object> getStudentCurrTimeTable( @RequestParam int week) {
        ObjectId userId = getUserId();
        int role = getSessionValue().getUserRole();
        Map<String, Object> map = termService.getCurrentTerm(getSessionValue().getSchoolId().toString());
        String year = (String) map.get("year");
        String term = (String) map.get("term");
        Map<String, Object> result;
        result = timeTableService.getMyTimeTable(getUserId().toString(), week, userId.toString(), role, term, year);
        result.put("message", "true");
        List<StudentTimeTable> course = (List<StudentTimeTable>) result.get("course");
        if (course.size() == 0) {
            result.put("message", "false");
        } else {
            result.put("message", "true");
        }
        return result;
    }

    /**
     * 老师查看当前学年的课表
     *
     * @param teacherId
     * @param week
     * @return
     */
    @UserRoles(value = {UserRole.TEACHER, UserRole.ADMIN, UserRole.HEADMASTER, UserRole.TEACHER})
    @RequestMapping("/getTeacherCurrTimeTable")
    @ResponseBody
    public Map<String, Object> getTeacherCurrTimeTable(@RequestParam(required = false, defaultValue = "") String teacherId,
                                                       @RequestParam int week) {
        if (teacherId.equals("")) {
            teacherId = getUserId().toString();
        }
        Map<String, Object> map = termService.getCurrentTerm(getSessionValue().getSchoolId().toString());
        String year = (String) map.get("year");
        String term = (String) map.get("term");
        String gradeId = "";
        List<GradeView> gradeViewList = schoolService.findGradeList(getSessionValue().getSchoolId().toString());
        List<ObjectId> gradeIds = new ArrayList<ObjectId>();
        for (GradeView gradeView : gradeViewList) {
            gradeIds.add(new ObjectId(gradeView.getId()));
        }
        for (GradeView gradeView : gradeViewList) {
            int state = zoubanStateService.getZoubanState(year, getSessionValue().getSchoolId(), gradeView.getId());
            if (state < 6) {
            } else {
                gradeId = gradeView.getId();
                break;
            }
        }
        Map<String, Object> result = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(gradeId)) {
            result = timeTableService.getTeacherTimeTable(term, year, teacherId, gradeId, week, getUserId().toString(), getSessionValue().getSchoolId());
            //课表合并
            CourseConfDTO courseConfDTO = timeTableService.getCourseConf(term, gradeIds);
            result.put("conf", courseConfDTO);
            result.put("message", "true");
        } else {
            result.put("message", "false");
        }

        return result;
    }

    /**
     * 管理员====查看课表====教学班课表
     * 获取头部年级、学科、课程列表------finish
     *
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER, UserRole.TEACHER})
    @RequestMapping("/getGradeSubjectCourse")
    @ResponseBody
    public GradeSubjectCourse getGradeSubjectCourseList(@RequestParam String year, @RequestParam String gradeId) {
        return timeTableService.getGradeSubjectCourseList(year, gradeId, getSessionValue().getSchoolId());
    }

    /**
     * 管理员====查看课表====教师课表
     * 获取学科以及老师列表-----finish
     *
     * @return
     */
    @UserRoles(value = {UserRole.HEADMASTER, UserRole.ADMIN, UserRole.TEACHER})
    @RequestMapping("/getSubgjectTeacher")
    @ResponseBody
    public List<SubjectTeacher> getSubjectAndTeachers(String gradeId) {
        List<SubjectTeacher> subjectTeacherList = commonService.getSubjectTeacherList(getSchoolId().toString(), gradeId);
        return subjectTeacherList;
    }

    /**
     * 管理员====查看课表====行政班课表====明细
     * 行政班学生课表明细（头部）-----finish
     *
     * @param gradeId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER, UserRole.LEADER_CLASS, UserRole.TEACHER})
    @RequestMapping("/getDetailHead")
    @ResponseBody
    public CourseConfDTO getDetailHead(@RequestParam String term, @RequestParam String gradeId) {
        CourseConfDTO courseConfDTO = timeTableService.findCourseConfByGradeId(term, new ObjectId(gradeId));
        return courseConfDTO;
    }

    /**
     * 管理员====查看课表====行政班课表====明细
     * 行政班学生课表明细-----finish
     *
     * @param classId
     * @param xIndex
     * @param yIndex
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER, UserRole.LEADER_CLASS, UserRole.TEACHER})
    @RequestMapping("/getDetailList")
    @ResponseBody
    public List<ClassDetail> getDetailList(@RequestParam String term, @RequestParam String classId, @RequestParam int xIndex, @RequestParam int yIndex,
                                           @RequestParam int type, @RequestParam int week) {
        List<ClassDetail> classDetails = timeTableService.getDetailList(term, classId, xIndex, yIndex, type, week);
        if (classDetails == null || classDetails.isEmpty()) {
            classDetails = timeTableService.getDetailList(term, classId, xIndex, yIndex, 0, week);
        }
        List<ObjectId> classroomIds = new ArrayList<ObjectId>();
        for (ClassDetail c : classDetails) {
            if (c.getClassRoom() != null && !c.getClassRoom().equals("")) {
                classroomIds.add(new ObjectId(c.getClassRoom()));
            }
        }
        Map<ObjectId, ClassroomEntry> classroomEntryMap = classroomService.findClassRoomEntryMap(classroomIds);
        for (ClassDetail c : classDetails) {
            if (c.getClassRoom() != null && !c.getClassRoom().equals("")) {
                c.setClassRoom(classroomEntryMap.get(new ObjectId(c.getClassRoom())).getRoomName());
            }
        }
        return classDetails;
    }

    /**
     * 学生==========课表
     * 获取学生课表----finish
     *
     * @param studentId
     * @return
     */
    @UserRoles(value = {UserRole.STUDENT, UserRole.PARENT, UserRole.LEADER_CLASS, UserRole.ADMIN, UserRole.HEADMASTER, UserRole.TEACHER})
    @RequestMapping("/getStudentTimeTable")
    @ResponseBody
    public Map<String, Object> getMyTimeTable(@RequestParam String studentId, @RequestParam int week, String term, String year) {
        ObjectId userId = getUserId();
        int role = getSessionValue().getUserRole();
        return timeTableService.getMyTimeTable(studentId, week, userId.toString(), role, term, year);
    }


    /**
     * 导出现在老师的课表
     *
     * @param term
     * @param teacherId
     * @param teacherName
     * @param week
     * @param response
     */
    @RequestMapping("/exportCurrTeacherExcel")
    @ResponseBody
    public void exportCurrTeacherExcel(String term, String teacherId, String teacherName,
                                       int week, HttpServletResponse response) {
        String userId = teacherId;
        String userName = teacherName;
        if (teacherId == "") {
            userId = getUserId().toString();
            userName = getSessionValue().getUserName();
        }
        if (term.equals(""))
            term = commonService.getCurrentTerm();
        else {
            try {
                userName = java.net.URLDecoder.decode(userName, "UTF-8");
                term = java.net.URLDecoder.decode(term, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        List<GradeView> gradeViewList = schoolService.findGradeList(getSessionValue().getSchoolId().toString());
        List<ObjectId> gradeIds = new ArrayList<ObjectId>();
        for (GradeView gradeView : gradeViewList) {
            gradeIds.add(new ObjectId(gradeView.getId()));
        }
        timeTableService.exportCurrTeacherExcel(gradeIds, userId, term, userName, week, response);

    }

    /**
     * 导出老师课表
     *
     * @param response
     */
    @RequestMapping("/exportTea")
    @ResponseBody
    public void exportTeacherExcel(String term, String gradeId, String gradeName, String teacherId, String teacherName,
                                   int week, HttpServletResponse response) {
        String userId = teacherId;
        String userName = teacherName;
        if (teacherId == "") {
            userId = getUserId().toString();
            userName = getSessionValue().getUserName();
        }
        if (term.equals(""))
            term = commonService.getCurrentTerm();
        else {
            try {
                userName = java.net.URLDecoder.decode(userName, "UTF-8");
                term = java.net.URLDecoder.decode(term, "UTF-8");
                gradeName = java.net.URLDecoder.decode(gradeName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        timeTableService.exportTeacherExcel(userId, term, gradeId, userName, gradeName, week, response);
    }

    /**
     * 导出全部学生课表
     *
     * @param response
     */
    @RequestMapping("/exportAllStu")
    @ResponseBody
    public void exportAllStudentExcel(String term, String year, String gradeId, String classId, int type, int week,
                                      HttpServletResponse response) {
        if (term.equals(""))
            term = commonService.getCurrentTerm();
        else {
            try {
                term = java.net.URLDecoder.decode(term, "UTF-8");
                year = java.net.URLDecoder.decode(year, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        String schoolId = getSessionValue().getSchoolId();
        timeTableService.exportAllExcel(gradeId, term, year, schoolId, classId, type, week, response);
    }

    /**
     * 导出教学班课表
     *
     * @param response
     */
    @RequestMapping("/exportCourse")
    @ResponseBody
    public void exportCourseExcel(String term, String courseId, String gradeId, int week, HttpServletResponse response) throws Exception {
        String year = term.substring(0, 11);
        timeTableService.exportCourseExcel(term, year, courseId, gradeId, week, response);
    }

    /**
     * 导出学生课表
     *
     * @param response
     */
    @RequestMapping("/exportStu")
    @ResponseBody
    public void exportStudentExcel(String term, @RequestParam(required = false, defaultValue = "") String stuId,
                                   @RequestParam(required = false, defaultValue = "") String stuName, int week, HttpServletResponse response) throws Exception {
        String userId = stuId;
        String userName = stuName;
        if (term == null || term.equals("")) {
            Map<String, Object> map = termService.getCurrentTerm(getSessionValue().getSchoolId());
            term = map.get("term").toString();
        }
        if (stuId.equals("")) {
            userId = getUserId().toString();
            userName = getSessionValue().getUserName();
        }
        String year = term.substring(0, 11);
        timeTableService.exportExcel(userId, term, year, userName, week, response);
    }

    /**
     * 导出学生列表（按sheet区分班级）
     *
     * @param term
     * @param gradeId
     * @param groupId
     * @param level
     * @param response
     */
    @RequestMapping("/exportZBStuList")
    @ResponseBody
    public void exportStudentListExcel(String term, String gradeId, String groupId,
                                       int level, HttpServletResponse response) {
        if (term == null || term.equals("")) {
            Map<String, Object> map = termService.getCurrentTerm(getSessionValue().getSchoolId());
            term = map.get("term").toString();
        }
        try {
            int mode = zoubanModeService.getGradeMode(getSchoolId().toString(), gradeId);
            List<Map<String, Object>> zouBanCourseDTOList = bianbanService.getCourseList(term, gradeId, groupId, level, mode);
            timeTableService.exportStudentListExcel(zouBanCourseDTOList, getSchoolId(), response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取教学周
     *
     * @param year
     * @return
     */
    @RequestMapping("/findTermEntry")
    @ResponseBody
    public TermDTO findTermEntry(String year) {
        TermDTO termDTO = termService.findTermDTO(year, getSchoolId());
        return termDTO;
    }

}
