package com.fulaan.zouban.controller.jinyuan;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.fulaan.classroom.ClassRoomDTO;
import com.fulaan.classroom.service.ClassroomService;
import com.fulaan.examresult.controller.IdNameDTO;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.myschool.controller.SubjectView;
import com.fulaan.school.service.SchoolService;
import com.fulaan.user.service.UserService;
import com.fulaan.zouban.dto.*;
import com.fulaan.zouban.service.*;
import com.pojo.app.SessionValue;
import com.pojo.classroom.ClassroomEntry;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserRole;
import com.pojo.zouban.XuankeConfEntry;
import com.pojo.zouban.ZouBanCourseEntry;
import com.pojo.zouban.ZoubanType;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangkaidong on 2016/7/5.
 * <p/>
 * 走班课程设置Controller
 */

@Controller
@RequestMapping("/zouban/subjectConfig")
public class StepOneController extends BaseController {
    @Autowired
    private XuanKeService xuanKeService;
    @Autowired
    private ZoubanModeService zoubanModeService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private BianbanService bianbanService;
    @Autowired
    private ClassService classService;
    @Autowired
    private ClassroomService classroomService;
    @Autowired
    private SubjectConfService subjectConfService;
    @Autowired
    private UserService userService;
    @Autowired
    private PaikeService paikeService;




    /**
     * 学科设置页面
     *
     * @param term
     * @param gid
     * @param gnm
     * @param model
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping
    public String subjectConfig(String term, String gid, String gnm, Model model, HttpServletResponse response) {
        XuankeConfEntry xuankeConfEntry = xuanKeService.findXuanKeConfEntry(term, gid, getSchoolId().toString());
        model.addAttribute("term", term);
        model.addAttribute("gradeId", gid);
        model.addAttribute("gradeName", gnm);
        model.addAttribute("xuankeId", xuankeConfEntry.getID());
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
        return "zoubannew/admin/subjectConfig";
    }


    /**
     * 获取年级学科列表
     *
     * @param gradeId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/findSubjectList")
    @ResponseBody
    public RespObj findGradeSubjectList(String gradeId) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            List<SubjectView> subjectList = schoolService.findSubjectListBySchoolIdAndGradeId(getSchoolId().toString(), gradeId);
            respObj.setCode(RespObj.SUCCESS.code);
            respObj.setMessage(subjectList);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }


    //------------------------------------------------走班课&非走班课学科设置----------------------------------------------

    /**
     * 判断是否开始选课
     *
     * @param xuankeId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/isRelease")
    @ResponseBody
    public RespObj isRelease(@ObjectIdType ObjectId xuankeId) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);

        try {
            boolean isRelease = xuanKeService.isRelease(xuankeId);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(isRelease);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }

        return respObj;
    }

    /**
     * 选课设置列表
     *
     * @param gradeId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/findSubjectConfList")
    @ResponseBody
    public RespObj findSubjectConfList(String gradeId, int type, @ObjectIdType ObjectId xuankeId) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);

        try {
            List<SubjectConfDTO> subjectConfList = subjectConfService.findSubjectConfList(getSchoolId().toString(), gradeId, type, xuankeId);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(subjectConfList);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }

        return respObj;
    }

    /**
     * 获取学科配置
     *
     * @param subjectConfId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping(value = "/subjectConf", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> findSubjectConf(String subjectConfId) {
        SubjectConfDTO subjectConfDTO = subjectConfService.findSubjectConf(subjectConfId);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("subjectConf", subjectConfDTO);
        return result;
    }


    /**
     * 添加或更新学科配置
     *
     * @param subjectConfDTO
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping(value = "/subjectConf", method = RequestMethod.POST)
    @ResponseBody
    public RespObj addSubjectConf(SubjectConfDTO subjectConfDTO) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            subjectConfService.addOrUpdateSubConf(subjectConfDTO, getSchoolId());
            respObj.setCode(RespObj.SUCCESS.code);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 删除学科配置
     *
     * @param subjectConfId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping(value = "/deleteSubjectConf/{subjectConfId}/deletion", method = RequestMethod.POST)
    @ResponseBody
    public RespObj deleteSubjectConf(@PathVariable String subjectConfId) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            subjectConfService.deleteSubjectConf(subjectConfId);
            respObj.setCode(RespObj.SUCCESS.code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respObj;
    }

    //-------------------------------------------------单双周设置----------------------------------------------------------、


    /**
     * 新增单双周课设置
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping(value = "/addDSZK")
    @ResponseBody
    public RespObj addDSZK(String oddEvenId, String term, String gradeId,
                           String subjectId, String xuankeId) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            subjectConfService.addDSZK(oddEvenId, getSchoolId().toString(), gradeId, term, 0, subjectId, xuankeId);
            respObj.setCode(RespObj.SUCCESS.code);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e);
        }
        return respObj;
    }

    /**
     * 单双周老师配置
     *
     * @param zoubanEvenId
     * @param teacherId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping(value = "/editDSZKTeacher")
    @ResponseBody
    public RespObj editDSZKTeacher(String zoubanEvenId, String teacherId, String teacherName) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            subjectConfService.addDSZKTeacher(zoubanEvenId, teacherId, teacherName);
            respObj.setCode(RespObj.SUCCESS.code);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 删除单双周课配置
     *
     * @param oddEvenId
     * @param term
     * @param gradeId
     * @param subjectId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping(value = "/deleteDSZKSubject")
    @ResponseBody
    public RespObj deleteDSZKSubject(String oddEvenId, String term, String gradeId, String subjectId) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            subjectConfService.removeDSZK(oddEvenId, gradeId, term, subjectId);
            respObj.setCode(RespObj.SUCCESS.code);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     * 删除老师设置
     *
     * @param courseId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping(value = "/deleteDSZKTeacher")
    @ResponseBody
    public RespObj deleteDSZKTeacher(String courseId, String term, String gradeId) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            subjectConfService.removeDSZKTeacher(courseId, term, gradeId);
            respObj.setCode(RespObj.SUCCESS.code);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 单双周课科目设置列表
     *
     * @param term
     * @param gradeId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/findDSZKSubjectConfList")
    @ResponseBody
    public Map<String, Object> findDSZKSubjectConfList(String term, String gradeId, int type) {
        List<OddEvenDTO> oddEvenDTOList = subjectConfService.getDSZKSubjectList(term, gradeId, type, getSessionValue().getSchoolId());
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("oddEvenDTOList", oddEvenDTOList);
        return result;
    }

    /**
     * 单双周老师设置列表
     *
     * @param term
     * @param gradeId
     * @param type
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/findDSZKTeacherList")
    @ResponseBody
    public Map<String, Object> findDSZKTeacherList(String term, String gradeId, int type) {
        List<ZoubanOddEvenDTO> zouBanCourseDTOs = subjectConfService.getDSZKTeacherList(term, gradeId, type, getSchoolId().toString());
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("zouBanCourseDTOs", zouBanCourseDTOs);
        return result;
    }

    /**
     * 单双周老师列表（编辑）
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/findOddEvenTeacher")
    @ResponseBody
    public Map<String, Object> findOddEvenTeacher(String subjectId, String gradeId) {
        String[] str = subjectId.split(",");
        List<IdNameDTO> oddDTOs = commonService.findTeacherBySubject(gradeId, str[0]);
        List<IdNameDTO> evenDTOs = commonService.findTeacherBySubject(gradeId, str[1]);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("oddDTOs", oddDTOs);
        result.put("evenDTOs", evenDTOs);
        return result;
    }


    //-------------------------------------------------分组走班课设置----------------------------------------------------------

    /**
     * 新增分组走班课
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping(value = "/addFZZB")
    @ResponseBody
    public RespObj addFZZB(String subjectConfId, String term, String gradeId, int lessonCount, String classIds,
                           int classNumber, String subjectId, String subjectName, String xuankeId) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            subjectConfService.addFZZB(subjectConfId, getSchoolId().toString(), gradeId, term, classIds, lessonCount,
                    classNumber, subjectId, subjectName, xuankeId);
            respObj.setCode(RespObj.SUCCESS.code);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     * 删除分组走班的信息
     *
     * @param subjectConfigId
     * @param term
     * @param gradeId
     * @param subjectId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping(value = "/deleteFZZBSubject")
    @ResponseBody
    public RespObj deleteFZZBSubject(String subjectConfigId, String term, String gradeId, String subjectId) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            subjectConfService.removeFZZB(subjectConfigId, gradeId, term, subjectId);
            respObj.setCode(RespObj.SUCCESS.code);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 更新分组走班课
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping(value = "/editFZZBInfo", method = {RequestMethod.POST})
    @ResponseBody
    public RespObj editFZZBInfo(@RequestBody List<ZoubanGroupDTO> zoubanGroupDTOs) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            subjectConfService.updateFZZBConfig(zoubanGroupDTOs);
            respObj.setCode(RespObj.SUCCESS.code);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }
    /**
     * 删除老师设置
     *
     * @param courseId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping(value = "/deleteFZBKTeacher")
    @ResponseBody
    public RespObj deleteFZBKTeacher(String term, String courseId) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            subjectConfService.removeFZBKTeacher(term, courseId);
            respObj.setCode(RespObj.SUCCESS.code);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }
    /**
     * 查询老师
     *
     * @param group
     * @param term
     * @param gradeId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping(value = "/findEditFZZBClassRoom")
    @ResponseBody
    public RespObj findEditFZZBClassRoom(String group, String term, String gradeId) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        List<ZoubanGroupDTO> zoubanGroupDTOs = new ArrayList<ZoubanGroupDTO>();
        List<IdNameDTO> idNameDTOs = new ArrayList<IdNameDTO>();
        try {
            zoubanGroupDTOs.addAll(subjectConfService.getEditFZZBTeacherList(group, term, gradeId));
            //教室列表
            List<ZouBanCourseEntry> zouBanCourseEntries = subjectConfService.getZouBanCourseEntry(group, term, gradeId);
            List<ObjectId> classIds = zouBanCourseEntries.get(0).getClassId();
            for (ObjectId objectId : classIds) {
                IdNameDTO idNameDTO = new IdNameDTO();
                ClassroomEntry classRoomEntry = subjectConfService.getClassRoom(objectId);
                idNameDTO.setId(classRoomEntry.getID().toString());
                idNameDTO.setName(classRoomEntry.getRoomName());
                idNameDTOs.add(idNameDTO);
            }
            List<ClassRoomDTO> classRoomDTOList = classroomService.findClassroomList(new ObjectId(getSchoolId().toString()));
            for (ClassRoomDTO classRoomDTO : classRoomDTOList) {
                if (classRoomDTO.getClassId().equals("5404a60cf6f28b7261cfad53")) {
                    IdNameDTO idNameDTO = new IdNameDTO();
                    idNameDTO.setId(classRoomDTO.getId());
                    idNameDTO.setName(classRoomDTO.getRoomName());
                    idNameDTOs.add(idNameDTO);
                }
            }
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("zoubanGroupDTOs", zoubanGroupDTOs);
            result.put("idNameDTOs", idNameDTOs);

            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 老师列表
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping(value = "/findEditFZZBTeacher")
    @ResponseBody
    public Map<String, Object> findEditFZZBTeacher(String subjectId, String gradeId) {
        List<IdNameDTO> idNameDTOs = commonService.findTeacherBySubject(gradeId, subjectId);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("idNameDTOs", idNameDTOs);
        return result;
    }


    /**
     * 分组走班选课设置列表
     *
     * @param term
     * @param gradeId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/findFZZBSubjectConfList")
    @ResponseBody
    public Map<String, Object> findFZZBSubjectConfList(String term, String gradeId, int type) {
        List<SubjectConfDTO> subjectConfList = subjectConfService.getSubjectConfList(term, gradeId, type, getSessionValue().getSchoolId());
        SubjectConfDTO s = new SubjectConfDTO();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("subjectConfList", subjectConfList);
        return result;
    }

    /**
     * 分组走班老师设置列表
     *
     * @param term
     * @param gradeId
     * @param type
     * @param subjectId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/findFZZBTeacherList")
    @ResponseBody
    public Map<String, Object> findFZZBTeacherList(String term, String gradeId, int type, String subjectId) {
        List<ZouBanCourseDTO> zouBanCourseDTOs = subjectConfService.getFZZBTeacherList(term, gradeId, type, subjectId);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("zoubanCourseList", zouBanCourseDTOs);
        return result;
    }

    /**
     * 获取分配学生的行政班列表信息
     *
     * @param oddEvenId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/getAllocClassInfo")
    @ResponseBody
    public Map<String, Object> getAllocClassInfo(String oddEvenId) {
        ZouBanCourseEntry zouBanCourseEntry = subjectConfService.getZouBanCourseEntry(new ObjectId(oddEvenId));
        List<IdNameDTO> idNameDTOs = subjectConfService.getIdNameDTO(zouBanCourseEntry);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("idNameDTOs", idNameDTOs);
        return result;
    }

    /**
     * 班级学生信息
     *
     * @param classId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/getClassStudentInfo")
    @ResponseBody
    public Map<String, Object> getClassStudentInfo(String classId, String oddEvenId) {
        List<UserDetailInfoDTO> resultData = new ArrayList<UserDetailInfoDTO>();
        ZouBanCourseEntry zouBanCourseEntry = subjectConfService.getZouBanCourseEntry(new ObjectId(oddEvenId));
        List<ZouBanCourseEntry> zouBanCourseEntries = subjectConfService.getZouBanCourseEntry(zouBanCourseEntry.getGroup().toString(),
                zouBanCourseEntry.getTerm(), zouBanCourseEntry.getGradeId().toString());
        List<ObjectId> studentInfo = new ArrayList<ObjectId>();
        for (ZouBanCourseEntry zouBanCourseEntry1 : zouBanCourseEntries) {
            studentInfo.addAll(zouBanCourseEntry1.getStudentList());
        }
        List<UserDetailInfoDTO> userDetailInfoDTOs = classService.findStuByClassIdAndKeyword(classId, "");
        for (UserDetailInfoDTO userDetailInfoDTO : userDetailInfoDTOs) {
            if (studentInfo.size() > 0) {
                boolean flag = false;
                for (ObjectId objectId : studentInfo) {
                    if (userDetailInfoDTO.getId().equals(objectId.toString())) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    resultData.add(userDetailInfoDTO);
                }
            } else {
                resultData.add(userDetailInfoDTO);
            }
        }
        List<ObjectId> objectIds = new ArrayList<ObjectId>();
        for (UserDetailInfoDTO userDetailInfoDTO : resultData) {
            objectIds.add(new ObjectId(userDetailInfoDTO.getId()));
        }
        List<IdNameDTO> idNameDTOs = userService.findUserIdNameByIds(objectIds);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("userDetailInfoDTOs", idNameDTOs);
        return result;
    }

    /**
     * 教学班的学生信息
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/getTeachStudentInfo")
    @ResponseBody
    public Map<String, Object> getTeachStudentInfo(String oddEvenId) {
        ZouBanCourseEntry zouBanCourseEntry = subjectConfService.getZouBanCourseEntry(new ObjectId(oddEvenId));
        List<ObjectId> classIds = zouBanCourseEntry.getClassId();
        List<ObjectId> studentInfo = zouBanCourseEntry.getStudentList();
        List<UserDetailInfoDTO> teachInfo = new ArrayList<UserDetailInfoDTO>();
        List<UserDetailInfoDTO> userDetailInfoDTOList = new ArrayList<UserDetailInfoDTO>();
        for (ObjectId classId : classIds) {
            List<UserDetailInfoDTO> userDetailInfoDTOs = classService.findStuByClassIdAndKeyword(classId.toString(), "");
            userDetailInfoDTOList.addAll(userDetailInfoDTOs);
        }
        for (ObjectId item : studentInfo) {
            for (UserDetailInfoDTO userDetailInfoDTO : userDetailInfoDTOList) {
                if (item.toString().equals(userDetailInfoDTO.getId())) {
                    teachInfo.add(userDetailInfoDTO);
                    break;
                }
            }
        }
        List<ObjectId> objectIds = new ArrayList<ObjectId>();
        for (UserDetailInfoDTO userDetailInfoDTO : teachInfo) {
            objectIds.add(new ObjectId(userDetailInfoDTO.getId()));
        }
        List<IdNameDTO> idNameDTOs = userService.findUserIdNameByIds(objectIds);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("teachInfo", idNameDTOs);
        return result;
    }

    /**
     * 添加教学班学生
     *
     * @param studentId
     * @param oddEvenId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/addTeachStudent")
    @ResponseBody
    public RespObj addTeachStudent(String studentId, String oddEvenId) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            bianbanService.addTeachStudent(oddEvenId, studentId);
            respObj.setCode(RespObj.SUCCESS.code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respObj;
    }

    /**
     * 添加教学班学生
     *
     * @param studentId
     * @param oddEvenId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("removeTeachStudent")
    @ResponseBody
    public RespObj removeTeachStudent(String studentId, String oddEvenId) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            bianbanService.removeTeachStudent(oddEvenId, studentId);
            respObj.setCode(RespObj.SUCCESS.code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respObj;
    }


    //-------------------------------------------------体育课设置----------------------------------------------------------

    /**
     * 体育课设置获取老师
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/findPETeacher")
    @ResponseBody
    public Map<String, Object> findPETeacher(String gradeId) {
        List<SubjectView> subjectList = schoolService.findSubjectList(getSchoolId().toString());
        String subjectId = "";
        for (SubjectView subjectView : subjectList) {
            if (subjectView.getName().contains("体育")) {
                subjectId = subjectView.getId();
                break;
            }
        }
        List<IdNameDTO> teacherList = commonService.findTeacherBySubject(gradeId, subjectId);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("teacherList", teacherList);
        return result;
    }


    /**
     * 新增体育课
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/addPECourse")
    @ResponseBody
    public RespObj addPECourse(String term, String gradeId, String className, int lessonCount, String classIds,
                               String mTeacherId, String mTeacherName, String fTeacherId, String fTeacherName) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            subjectConfService.addPECourse(getSchoolId().toString(), gradeId, term, classIds, className,
                    lessonCount, mTeacherId, fTeacherId, mTeacherName, fTeacherName);
            respObj.setCode(RespObj.SUCCESS.code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respObj;
    }

    /**
     * 更新体育课
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/updatePECourse")
    @ResponseBody
    public RespObj updatePECourse(String term, String mClassId, String fClassId, String className, String classIds,
                                  String mTeacherId, String fTeacherId, String mTeacherName, String fTeacherName, int lessonCount) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            subjectConfService.updatePECourse(term, mClassId, fClassId, className, classIds, mTeacherId, fTeacherId, mTeacherName, fTeacherName, lessonCount);
            respObj.setCode(RespObj.SUCCESS.code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respObj;
    }


    /**
     * 获取体育课
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/getPECourseList")
    @ResponseBody
    public Map<String, Object> getPECourseList(String term, String gradeId) {
        List<PECourseDTO> peCourseDTOList = subjectConfService.getPECourseList(gradeId, term);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("courseList", peCourseDTOList);
        return result;
    }

    /**
     * 删除体育课
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/deletePECourse")
    @ResponseBody
    public RespObj deletePECourse(String term, String mClassId, String fClassId) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            subjectConfService.deletePECourse(term, mClassId, fClassId);
            respObj.setCode(RespObj.SUCCESS.code);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage("删除失败");
        }
        return respObj;
    }




    /**
     * 查询教学班
     *
     * @param term
     * @param gradeId
     * @param subjectId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/findBianBanList")
    @ResponseBody
    public Map findBianBanList(String term, String gradeId, String subjectId) {
        HashMap map = new HashMap();

        subjectId = subjectId.equals("") ? null : subjectId;

        List<ZouBanCourseDTO> zouBanCourseDTOList = bianbanService.findBianBanList(term, gradeId, null, subjectId, getSchoolId().toString(), 2);
        map.put("rows", zouBanCourseDTOList);
        return map;
    }

    /**
     * 获取老师和教室
     *
     * @param gradeId
     * @param subjectId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping(value = "/teachers", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getTeachers(String gradeId, String subjectId) {
        Map<String, Object> result = new HashMap<String, Object>();
        List<IdNameDTO> teacherList = commonService.findTeacherBySubject(gradeId, subjectId);
        result.put("teacherList", teacherList);
        return result;
    }

    /**
     * 更新老师和课时
     *
     * @param courseId
     * @param teacherId
     * @param teacherName
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping(value = "/teachers", method = RequestMethod.POST)
    @ResponseBody
    public RespObj setTeacher(String courseId, String teacherId, String teacherName, int weekTime) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            bianbanService.updateCourse(courseId, teacherId, teacherName, weekTime);
            respObj.setCode(RespObj.SUCCESS.code);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage("更新失败");
        }

        return respObj;
    }

    /**
     * 一键设置老师和教室
     *
     * @param term
     * @param gradeId
     * @param type
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/autoSetTeacherAndRoom")
    @ResponseBody
    public RespObj autoSetTeacherAndClassRoom(String term, String gradeId, int type) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            bianbanService.autoSetTeacher(term, gradeId, getSchoolId().toString(), type);
            if (type == ZoubanType.FEIZOUBAN.getType()) {
                paikeService.clearTimetableCourse(term, gradeId, 4, null);
            }
            if (type == ZoubanType.ODDEVEN.getType()) {
                paikeService.clearTimetableCourse(term, gradeId, 6, null);
            }
            respObj.setCode(RespObj.SUCCESS.code);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }

        return respObj;
    }

    /**
     * 一键设置老师和教室
     *
     * @param term
     * @param gradeId
     * @param type    1: 走班 2: 非走班
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/autoSetFZZBTeacherAndRoom")
    @ResponseBody
    public RespObj autoSetFZZBTeacherAndClassRoom(String term, String gradeId, int type) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            bianbanService.autoSetFZZBTeacher(term, gradeId, getSchoolId().toString(), type);
            bianbanService.autoSetFZZBClassRoom(term, gradeId, getSchoolId().toString(), type);

            //删除分层走班课
            paikeService.clearTimetableCourse(term, gradeId, 5, null);
            respObj.setCode(RespObj.SUCCESS.code);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }

        return respObj;
    }



}
