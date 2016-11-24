package com.fulaan.zouban.controller.jinyuan;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.fulaan.examresult.controller.IdNameDTO;
import com.fulaan.zouban.dto.*;
import com.fulaan.zouban.service.*;
import com.pojo.app.SessionValue;
import com.pojo.user.UserRole;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by wangkaidong on 2016/7/6.
 * <p/>
 * 分段编班Controller
 */

@Controller
@RequestMapping("/zouban/fenban")
public class StepThreeController extends BaseController {
    @Autowired
    private BianbanService bianbanService;
    @Autowired
    private ZoubanStateService zoubanStateService;
    @Autowired
    private ZoubanModeService zoubanModeService;
    @Autowired
    private PaikeService paikeService;




    /**
     * 分班页面
     *
     * @param term
     * @param gid
     * @param gnm
     * @param model
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping
    public String fenBan(String term, String gid, String gnm, Model model) {
        try {
            model.addAttribute("term", URLDecoder.decode(term, "UTF-8"));
            model.addAttribute("gradeId", gid);
            model.addAttribute("gradeName", URLDecoder.decode(gnm, "UTF-8"));
            model.addAttribute("mode", zoubanModeService.getGradeMode(getSchoolId().toString(), gid));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "zoubannew/admin/fenban";
    }

    /**
     * 分班进度
     *
     * @param gradeId
     * @param term
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/fenBanState")
    @ResponseBody
    public Map<String, Object> getFenBanState(String gradeId, String term) {
        int state = zoubanStateService.getSubState(term, gradeId);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("state", state);
        return result;
    }

    //=========================================分段============================================

    /**
     * 分段列表
     *
     * @param term
     * @param gradeId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER, UserRole.TEACHER})
    @RequestMapping("/findFenDuan")
    @ResponseBody
    public Map<String, Object> findFenDuanList(String term, String gradeId) {
        List<FenDuanDTO> fenDuanDTOList = bianbanService.getFenDuanList(term, gradeId,getSchoolId());
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("fenDuanList", fenDuanDTOList);
        return result;
    }

    /**
     * 自动分段
     *
     * @param gradeId
     * @param count
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/autoFenDuan")
    @ResponseBody
    public RespObj autoFenDuan(String term, String gradeId, int count) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            bianbanService.autoFenDuan(term, gradeId, count ,getSchoolId());
            respObj.setCode(RespObj.SUCCESS.code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respObj;
    }

    /**
     * 调整分段获取分段列表
     *
     * @param term
     * @param gradeId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/changeFenDuanList")
    @ResponseBody
    public Map<String, Object> getChangeFenDuanList(String term, String gradeId) {
        List<ChangeFenDuanDTO> fenDuanList = bianbanService.getChangeFenDuanList(term, gradeId);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("fenDuanList", fenDuanList);
        return result;
    }

    /**
     * 调整班级分段
     *
     * @param classId
     * @param oldGroupId
     * @param newGroupId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/changeFenDuan")
    @ResponseBody
    public RespObj changeFenDuan(String term, String gradeId, String classId, String oldGroupId, String newGroupId) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            bianbanService.updateFenDuan(classId, oldGroupId, newGroupId);
            zoubanStateService.setZoubanState(term, gradeId, 3);
            zoubanStateService.setZoubanSubState(term, gradeId, 3);
            respObj.setCode(RespObj.SUCCESS.code);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return respObj;
    }

    //============================================分层=============================================

    /**
     * 下载模板
     *
     * @param term
     * @param gradeId
     * @param subjects
     * @param response
     */
    @RequestMapping("/downloadTemplate")
    @ResponseBody
    public void downloadTemplate(String term, String gradeId, String subjects, HttpServletResponse response) {
        String[] subjectList = subjects.split(",");
        byte[] content = bianbanService.downloadChengjiTemplate(term, gradeId, getSchoolId().toString(), subjectList);
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("学生成绩模板.xls", "UTF-8"));
            response.setContentLength(content.length);
            outputStream.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                    outputStream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 导入学生成绩
     *
     * @param file
     * @param term
     * @param gradeId
     * @return
     */
    @RequestMapping("/import")
    @ResponseBody
    public RespObj importScore(MultipartFile file, String term, String gradeId) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);

        String fileName = file.getOriginalFilename();
        if (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx")) {
            respObj.setMessage("文件格式错误，请选择Excel文件");
        } else {
            try {
                bianbanService.importScore(file.getInputStream(), term, gradeId, getSchoolId().toString());
                respObj.setCode(RespObj.SUCCESS.code);
                respObj.setMessage("导入成功");
            } catch (Exception e) {
                e.printStackTrace();
                respObj.setMessage(e.getMessage());
            }
        }

        return respObj;
    }

    //===========================================分班===================================================

    /**
     * 自动编班
     *
     * @param term
     * @param gradeId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/autoFenBan")
    @ResponseBody
    public RespObj autoFenBan(String term, String gradeId, String groupId, int advMax, int advMin, int simMax, int simMin, int classroomCount) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            boolean flag = bianbanService.autoFenBan(term, getSchoolId().toString(), gradeId, groupId, advMax, advMin, simMax, simMin, classroomCount);
            if (flag) {
                respObj.setCode(RespObj.SUCCESS.code);
            }
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e);
        }

        return respObj;
    }

    /**
     * 走班课教学班列表
     *
     * @param term
     * @param gradeId
     * @param groupId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/findCourseList")
    @ResponseBody
    public Map<String, Object> findCourseList(String term, String gradeId, String groupId) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<SubjectCourseDTO> courseList = bianbanService.getCourseList(term, gradeId, groupId, getSchoolId().toString());
        map.put("courseList", courseList);
        return map;
    }

    /**
     * 下载走班课模板
     *
     * @param response
     */
    @RequestMapping("/downloadZBCourseTemplate")
    @ResponseBody
    public void downloadZoubanCourseTemplate(String gradeId, HttpServletResponse response) {
        int mode = zoubanModeService.getGradeMode(getSchoolId().toString(), gradeId);
        byte[] content = bianbanService.downloadZBCourseTemplate(mode);
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("走班分班结果模板.xls", "UTF-8"));
            response.setContentLength(content.length);
            outputStream.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                    outputStream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 导入走班分班结果
     *
     * @param file
     * @param term
     * @param gradeId
     * @return
     */
    @RequestMapping("/importZoubanCourse")
    @ResponseBody
    public RespObj importZoubanCourse(MultipartFile file, String term, String gradeId) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);

        String fileName = file.getOriginalFilename();
        if (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx")) {
            respObj.setMessage("文件格式错误，请选择Excel文件");
        } else {
            try {
                int mode = zoubanModeService.getGradeMode(getSchoolId().toString(), gradeId);
                bianbanService.importZBCourse(file.getInputStream(), term, gradeId, getSchoolId(), mode);
                respObj.setCode(RespObj.SUCCESS.code);
                respObj.setMessage("导入成功");
            } catch (Exception e) {
                e.printStackTrace();
                respObj.setMessage(e.getMessage());
            }
        }

        return respObj;
    }

    //===========================================学生调班===================================================
    /**
     * 教学班列表
     *
     * @param term
     * @param gradeId
     * @param groupId
     * @param level
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/courseList")
    @ResponseBody
    public RespObj findCourseList(String term, String gradeId, String groupId, int level) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            int mode = zoubanModeService.getGradeMode(getSchoolId().toString(), gradeId);
            List<Map<String, Object>> zouBanCourseDTOList = bianbanService.getCourseList(term, gradeId, groupId, level, mode);
            respObj.setCode(RespObj.SUCCESS.code);
            respObj.setMessage(zouBanCourseDTOList);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }

        return respObj;
    }

    /**
     * 检查名称是否重复
     *
     * @param term
     * @param gradeId
     * @param courseName
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/checkCourseName")
    @ResponseBody
    public Map<String, Object> checkCourseName(String term, String gradeId, String courseName) {
        int isRepeat = bianbanService.isRepeat(term, gradeId, courseName) ? 1 : 0;
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("result", isRepeat);
        return result;
    }


    /**
     * 修改教学班名称
     *
     * @param courseId
     * @param courseName
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/updateCourseName")
    @ResponseBody
    public RespObj updateCourseName(String courseId, String courseName) {
        RespObj respObj = RespObj.FAILD;
        try {
            bianbanService.updateCourseName(courseId, courseName);
            respObj = RespObj.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respObj;
    }


    /**
     * 教学班学生列表
     *
     * @param courseId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/findClassStudentList")
    @ResponseBody
    public Map<String,Object> findClassStudentList(String courseId) {
        Map<String,Object> map = bianbanService.getCourseStuList(courseId, getSchoolId());
        return map;
    }

    /**
     * 学生调班
     *
     * @param studentId
     * @param oldClassId
     * @param newClassId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/changeStuClass")
    @ResponseBody
    public RespObj changeStuClass(String studentId, String oldClassId, String newClassId) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            bianbanService.changeStuClass(studentId, oldClassId, newClassId);
            respObj.setCode(RespObj.SUCCESS.code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  respObj;
    }


    /**
     * 获取学科组合列表
     *
     * @param term
     * @param gradeId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/subjectGroupList")
    @ResponseBody
    public RespObj getSubjectGroupList(String term, @ObjectIdType ObjectId gradeId, @ObjectIdType ObjectId courseId) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            List<IdNameDTO> subjectGroupList = bianbanService.getSubjectGroupList(getSchoolId(), term, gradeId, courseId);
            respObj.setCode(RespObj.SUCCESS.code);
            respObj.setMessage(subjectGroupList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return respObj;
    }


    /**
     * 获取组合学生列表
     *
     * @param subjectGroupId
     * @param term
     * @param gradeId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/subjectGroupStuList")
    @ResponseBody
    public RespObj getSubjectGroupStuList(@ObjectIdType ObjectId subjectGroupId, String term,
                                          @ObjectIdType ObjectId gradeId,
                                          @ObjectIdType ObjectId courseId) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            int mode = zoubanModeService.getGradeMode(getSchoolId().toString(), gradeId.toString());
            List<IdNameDTO> subjectGroupStuList = bianbanService.getSubjectGroupStuList(subjectGroupId, getSchoolId(), term, gradeId, courseId, mode);
            respObj.setCode(RespObj.SUCCESS.code);
            respObj.setMessage(subjectGroupStuList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return respObj;
    }


    /**
     * 获取教学班学生列表
     *
     * @param courseId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/zoubanCourseStuList")
    @ResponseBody
    public RespObj getZoubanCourseStuList(@ObjectIdType ObjectId courseId) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            List<IdNameDTO> stuList = bianbanService.getZBCourseStuList(courseId);
            respObj.setCode(RespObj.SUCCESS.code);
            respObj.setMessage(stuList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return respObj;
    }


    /**
     * 更新教学班学生列表
     *
     * @param courseId
     * @param studentId
     * @param type 0: 添加, 1:删除
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/updateZBCourseStu")
    @ResponseBody
    public RespObj getZoubanCourseStuList(@ObjectIdType ObjectId courseId, @ObjectIdType ObjectId studentId, int type) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            bianbanService.updateZBCourseStu(courseId, studentId, type);
            respObj.setCode(RespObj.SUCCESS.code);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage("更新失败:" + e);
        }

        return respObj;
    }

    //==============================================老师和教室===========================================================
    /**
     * 一键设置老师和教室
     *
     * @param term
     * @param gradeId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/autoSetTeacherAndRoom")
    @ResponseBody
    public RespObj autoSetTeacherAndClassRoom(String term, String gradeId) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            //设置教室
            bianbanService.autoSetClassRoom(term, gradeId, getSchoolId().toString());
            //设置老师
            bianbanService.autoSetTeacher(term, gradeId, getSchoolId().toString(), 1);
            //设置进度
            bianbanService.setZouBanState(term, gradeId);
            //清空走班课
            paikeService.clearTimetableCourse(term, gradeId, 1, null);
            respObj.setCode(RespObj.SUCCESS.code);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage("自动分配失败," + e.getMessage());
        }

        return respObj;
    }

    /**
     * 获取老师和教室
     *
     * @param term
     * @param gradeId
     * @param group
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping(value = "/teacherAndClassroom", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getTeacherAndClassRoom(String term, String gradeId, String group) {
        Map<String, Object> result = new HashMap<String, Object>();
        List<TeacherClassroomDTO> teacherClassroomDTOList = bianbanService.getTeacherClassroomList(term, gradeId, group, getSchoolId().toString());
        result.put("list", teacherClassroomDTOList);
        return result;
    }

    /**
     * 更新老师和教室
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping(value = "/teacherAndClassroom", method = RequestMethod.POST)
    @ResponseBody
    public RespObj setTeacherAndClassRoom(@RequestBody List<TeacherClassroomDTO> list, String term, String gradeId) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            bianbanService.setTeacherAndClassroom(list);
            bianbanService.setZouBanState(term, gradeId);//设置进度
            respObj.setCode(RespObj.SUCCESS.code);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage("更新失败");
        }

        return respObj;
    }




}
