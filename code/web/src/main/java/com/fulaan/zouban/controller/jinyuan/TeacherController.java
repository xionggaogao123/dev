package com.fulaan.zouban.controller.jinyuan;

import com.db.zouban.TimeTableDao;
import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.fulaan.examresult.controller.IdNameDTO;
import com.fulaan.myschool.controller.GradeView;
import com.fulaan.school.service.SchoolService;
import com.fulaan.zouban.dto.SubjectTeacher;
import com.fulaan.zouban.dto.TimetableState;
import com.fulaan.zouban.service.CommonService;
import com.fulaan.zouban.service.TimeTableService;
import com.fulaan.zouban.service.ZoubanModeService;
import com.fulaan.zouban.service.ZoubanStateService;
import com.pojo.user.UserRole;
import com.pojo.zouban.TimeTableEntry;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by wangkaidong on 2016/7/7.
 */

@Controller
@RequestMapping("/zouban/teacher")
public class TeacherController extends BaseController {
    @Autowired
    private CommonService commonService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private ZoubanStateService zoubanStateService;
    @Autowired
    private TimeTableService timeTableService;
    @Autowired
    private ZoubanModeService zoubanModeService;
    private TimeTableDao timeTableDao = new TimeTableDao();
    @RequestMapping(method = RequestMethod.GET)
    public String indexPage(Model model){
        model.addAttribute("curweek", 1);
        return "zoubannew/teacher/kebiao";
    }
    /**
     * 获取发布年级列表
     * @return
     */
    @RequestMapping("/getPublishGradeList")
    @ResponseBody
    public Map<String, Object> getPublishGradeList(String term) {
        List<GradeView> gradeList = schoolService.findGradeList(getSchoolId().toString());
        Iterator<GradeView> gradeIte = gradeList.iterator();
        while(gradeIte.hasNext()){
            GradeView gradeView = gradeIte.next();
            List<TimeTableEntry> timeTableList = timeTableDao.findTimeTableByGrade(term, new ObjectId(gradeView.getId()), TimetableState.PUBLISHED.getState(), 1);
            if(timeTableList.size()==0){
                gradeIte.remove();
            }
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("gradeList", gradeList);
        return result;
    }
    /**
     * 获取年级列表
     * @return
     */
    @RequestMapping("/getGradeList")
    @ResponseBody
    public Map<String, Object> getGradeList() {
        List<GradeView> gradeList = schoolService.findGradeList(getSchoolId().toString());
        Iterator<GradeView> gradeIte = gradeList.iterator();
        while(gradeIte.hasNext()){
            GradeView gradeView = gradeIte.next();
            int mode = zoubanModeService.getGradeMode(getSchoolId().toString(), gradeView.getId());
            if(mode==0){
                gradeIte.remove();
            }
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("gradeList", gradeList);
        return result;
    }
    /**
     * 管理员-校长-老师获取全部
     * @return
     */
    @UserRoles(value = {UserRole.HEADMASTER, UserRole.ADMIN, UserRole.TEACHER})
    @RequestMapping("/getSubgjectTeacher")
    @ResponseBody
    public List<SubjectTeacher> getSubjectAndTeachers() {
        List<GradeView> gradeList = schoolService.findGradeList(getSchoolId().toString());
        List<SubjectTeacher> subjectTeacherList = new ArrayList<SubjectTeacher>();
        List<SubjectTeacher> result = new ArrayList<SubjectTeacher>();
        //把三个年级的合在一起
        for(GradeView grade : gradeList){
            List<SubjectTeacher> list = commonService.getSubjectTeacherList(getSchoolId().toString(), grade.getId());
            subjectTeacherList.addAll(list);
        }
        //把老师列表为空的学科移除
        Iterator<SubjectTeacher> stIter = subjectTeacherList.iterator();
        while(stIter.hasNext()){
            SubjectTeacher subjectTeacher = stIter.next();
            if(subjectTeacher.getTeacherList().size()<=0){
                stIter.remove();
            }
        }
        Map<String, List<IdNameDTO>> subjectTeacherMap = new HashMap<String, List<IdNameDTO>>();
        for(SubjectTeacher subjectTeacher : subjectTeacherList){
            String key = subjectTeacher.getSubjectId()+","+subjectTeacher.getSubjectName();
            if(subjectTeacherMap.containsKey(key)){
                List<IdNameDTO> oldList = subjectTeacherMap.get(key);
                oldList.removeAll(subjectTeacher.getTeacherList());
                oldList.addAll(subjectTeacher.getTeacherList());
            }else{
                subjectTeacherMap.put(key,subjectTeacher.getTeacherList());
            }
        }
        Set set = subjectTeacherMap.entrySet();
        Iterator i = set.iterator();
        while(i.hasNext()){
            Map.Entry<String, List<IdNameDTO>> entry1=(Map.Entry<String, List<IdNameDTO>>)i.next();
            String idName = entry1.getKey();
            List<IdNameDTO> teacherList = entry1.getValue();
            String [] args = idName.split(",");
            SubjectTeacher subjectTeacher = new SubjectTeacher();
            subjectTeacher.setSubjectId(args[0]);
            subjectTeacher.setSubjectName(args[1]);
            subjectTeacher.setTeacherList(teacherList);
            result.add(subjectTeacher);
        }

        return result;
    }

    /**
     * 导出老师课表（自己的）
     *
     * @param response
     */
    @RequestMapping("/exportTea")
    @ResponseBody
    public void exportTeacherExcel(String term, int week, HttpServletResponse response) {
        List<GradeView> gradeList = schoolService.findGradeList(getSchoolId().toString());
        String userId = getUserId().toString();
        String userName = getSessionValue().getUserName();
        String gradeName = gradeList.get(0).getName();
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

        timeTableService.exportTeacherExcel(userId, term, gradeList.get(0).getId(), userName, gradeName, week, response);
    }


    /**
     * 导出行政班课表
     *
     * @param response
     */
    @RequestMapping("/exportClass")
    @ResponseBody
    public void exportClassExcel(String term, String year,String gradeName, String gradeId, int week, HttpServletResponse response) {
        List<GradeView> gradeList = schoolService.findGradeList(getSchoolId().toString());
        if (term.equals(""))
            term = commonService.getCurrentTerm();
        else {
            try {
                term = java.net.URLDecoder.decode(term, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        timeTableService.exportClassExcel(gradeId, gradeName, term, year, getSchoolId().toString(), 0, week,  response);
    }

    /**
     * 导出行政班课表
     *
     * @param response
     */
    @RequestMapping("/exportCourseTeacher")
    @ResponseBody
    public void exportCourseTeacherExcel(String term, String year,String courseName, String courseId, int week, HttpServletResponse response) {
        List<GradeView> gradeList = schoolService.findGradeList(getSchoolId().toString());
        if (term.equals(""))
            term = commonService.getCurrentTerm();
        else {
            try {
                term = java.net.URLDecoder.decode(term, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        timeTableService.exportCourseTeacherExcel(courseId, courseName, term, year, getSchoolId().toString(), 0, week, response);
    }
}
