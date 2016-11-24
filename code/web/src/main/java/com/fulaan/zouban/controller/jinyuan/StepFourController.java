package com.fulaan.zouban.controller.jinyuan;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.fulaan.examresult.controller.IdNameDTO;
import com.fulaan.zouban.dto.*;
import com.fulaan.zouban.service.*;
import com.pojo.app.SessionValue;
import com.pojo.user.UserRole;
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
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * Created by wangkaidong on 2016/7/6.
 * <p/>
 * 排课Controller
 */

@Controller
@RequestMapping("/zouban/paike")
public class StepFourController extends BaseController {
    @Autowired
    private TimetableConfService timetableConfService;
    @Autowired
    private PaikeService paikeService;
    @Autowired
    private TimeTableService timeTableService;
    @Autowired
    private ZoubanStateService zoubanStateService;
    @Autowired
    private ZoubanModeService zoubanModeService;
    @Autowired
    private EventConflictService eventConflictService;






    /**
     * 排课页面
     *
     * @param term
     * @param gid
     * @param gnm
     * @param model
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping
    public String paike(String term, String gid, String gnm, Model model, HttpServletResponse response) {
        try {
            model.addAttribute("term", URLDecoder.decode(term, "UTF-8"));
            model.addAttribute("gradeId", gid);
            model.addAttribute("gradeName", URLDecoder.decode(gnm, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
        return "zoubannew/admin/paike";
    }




    //========================================================走班课排课=================================================

    /**
     * 获取走班课已排课课表
     *
     * @param term
     * @param gradeId
     * @param groupId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/getArrangedZBCourse")
    @ResponseBody
    public Map<String, Object> getArrangedZBCourse(String term, @ObjectIdType ObjectId gradeId, @ObjectIdType(isRequire = false) ObjectId groupId) {
        int mode = zoubanModeService.getGradeMode(getSchoolId().toString(), gradeId.toString());
        TimetableConfDTO timetableConf = timetableConfService.getTimetableConf(term, gradeId);
        List<CourseItemDTO> courseItemDTOList = paikeService.getArrangedZBCourse(term, gradeId, groupId, mode);

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("timetableConf", timetableConf);
        result.put("courseItemList", courseItemDTOList);
        return result;
    }


    /**
     * 获取走班课未排课程
     *
     * @param term
     * @param gradeId
     * @param groupId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/getArrangingZBCourse")
    @ResponseBody
    public RespObj getArrangingZBCourse(String term, String gradeId, String groupId) {
        int mode = zoubanModeService.getGradeMode(getSchoolId().toString(), gradeId);
        RespObj respObj = new RespObj(RespObj.FAILD.getCode());
        try {
            List<List<IdNameDTO>> arrangingZBCourseList = paikeService.getArrangingZBCourse(term, gradeId, groupId, mode);
            respObj.setCode(RespObj.SUCCESS.code);
            respObj.setMessage(arrangingZBCourseList);
        } catch (Exception e) {
            respObj.setMessage(e.getMessage());
        }

        return respObj;
    }

    /**
     * 获取走班课可用时间点
     *
     * @param term
     * @param gradeId
     * @param courseIdStr
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/getAvailablePointForZB")
    @ResponseBody
    public RespObj getAvailablePointForZB(String term, String gradeId, String courseIdStr) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            List<PointJson> list = paikeService.getAvailablePointForZB(term, getSchoolId(), gradeId, courseIdStr);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(list);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }

        return respObj;
    }


    /**
     * 新增走班课
     *
     * @param term
     * @param groupId
     * @param x
     * @param y
     * @param courseIdStr
     * @return
     */
    @UserRoles(value = {UserRole.HEADMASTER, UserRole.ADMIN})
    @RequestMapping("/addZBCourse")
    @ResponseBody
    public RespObj addZBCourse(String term, String gradeId, String groupId, int x, int y, String courseIdStr) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            int mode = zoubanModeService.getGradeMode(getSchoolId().toString(), gradeId);
            paikeService.addZBCourse(term, gradeId, groupId, x, y, courseIdStr, mode);
            respObj.setCode(RespObj.SUCCESS.code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respObj;
    }

    /**
     * 删除走班课
     *
     * @param term
     * @param groupId
     * @param x
     * @param y
     * @return
     */
    @UserRoles(value = {UserRole.HEADMASTER, UserRole.ADMIN})
    @RequestMapping("/removeZBCourse")
    @ResponseBody
    public RespObj removeZBCourse(String term, String gradeId, String groupId, int x, int y) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            paikeService.removeZBCourse(term, gradeId, groupId, x, y);
            respObj.setCode(RespObj.SUCCESS.code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respObj;
    }

    /**
     * 根据type清空课表中的课程
     *
     * @param term
     * @param gradeId
     * @param type
     * @param classId 行政班id，type=3时需要
     * @return
     */
    @UserRoles(value = {UserRole.HEADMASTER, UserRole.ADMIN})
    @RequestMapping("/clearTimetableCourse")
    @ResponseBody
    public RespObj clearTimetableCourse(String term, String gradeId, int type, String classId) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            paikeService.clearTimetableCourse(term, gradeId, type, classId);
            respObj.setCode(RespObj.SUCCESS.code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respObj;
    }

    /**
     * 检查走班课是否排完
     *
     * @param term
     * @return
     */
    @UserRoles(value = {UserRole.HEADMASTER, UserRole.ADMIN})
    @RequestMapping("/getZBLockState")
    @ResponseBody
    public RespObj getZBLockState(String term, String gradeId) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            String lock = "UNLOCK";
            if (paikeService.checkZBFinished(term, gradeId)) {
                lock = "LOCK";
            }
            respObj.setCode(RespObj.SUCCESS.code);
            respObj.setMessage(lock);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respObj;
    }




    //======================================================分组走班排课==================================================

    /**
     * 获取分组走班课已排课课表
     *
     * @param term
     * @param gradeId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/getArrangedFZZBCourse")
    @ResponseBody
    public Map<String, Object> getArrangedFZZBCourse(String term, @ObjectIdType ObjectId gradeId) {
        TimetableConfDTO timetableConf = timetableConfService.getTimetableConf(term, gradeId);
        List<CourseItemDTO> courseItemDTOList = paikeService.getArrangedFZZBCourse(term, gradeId);

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("timetableConf", timetableConf);
        result.put("courseItemList", courseItemDTOList);
        return result;
    }


    /**
     * 获取分组走班课未排课程
     *
     * @param term
     * @param gradeId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/getArrangingFZZBCourse")
    @ResponseBody
    public RespObj  getArrangingFZZBCourse(String term, @ObjectIdType ObjectId gradeId) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            List<List<IdNameDTO>> arrangingFZZBCourseList = paikeService.getArrangingFZZBCourse(term, gradeId);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(arrangingFZZBCourseList);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }

        return respObj;
    }

    /**
     * 获取分组走班课可用时间点
     *
     * @param term
     * @param gradeId
     * @param courseIdStr
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/getAvailablePointForFZZB")
    @ResponseBody
    public RespObj getAvailablePointForFZZB(String term, String gradeId, String courseIdStr) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            List<PointJson> list = paikeService.getAvailablePointForFZZB(term, getSchoolId(), gradeId, courseIdStr);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(list);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     * 新增分组走班课
     *
     * @param term
     * @param x
     * @param y
     * @param courseIdStr
     * @return
     */
    @UserRoles(value = {UserRole.HEADMASTER, UserRole.ADMIN})
    @RequestMapping("/addFZZBCourse")
    @ResponseBody
    public RespObj addFZZBCourse(String term, int x, int y, String courseIdStr) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            paikeService.addFZZBCourse(term, courseIdStr, x, y);
            respObj.setCode(RespObj.SUCCESS.code);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 删除分组走班课
     *
     * @param term
     * @param x
     * @param y
     * @return
     */
    @UserRoles(value = {UserRole.HEADMASTER, UserRole.ADMIN})
    @RequestMapping("/removeFZZBCourse")
    @ResponseBody
    public RespObj removeFZZBCourse(String term, @ObjectIdType ObjectId gradeId, int x, int y) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            paikeService.removeFZZBCourse(term, gradeId, x, y);
            respObj.setCode(RespObj.SUCCESS.code);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 清空分组走班课
     *
     * @param term
     * @param gradeId
     * @return
     */
    @UserRoles(value = {UserRole.HEADMASTER, UserRole.ADMIN})
    @RequestMapping("/clearFZZBCourse")
    @ResponseBody
    public RespObj clearFZZBCourse(String term, @ObjectIdType ObjectId gradeId) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            paikeService.clearFZZBCourse(term, gradeId);
            respObj.setCode(RespObj.SUCCESS.code);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }



    //======================================================体育课排课===================================================

    /**
     * 获取体育课未排课程
     *
     * @param term
     * @param gradeId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/getArrangingPECourse")
    @ResponseBody
    public Map<String, Object> getArrangingPECourse(String term, String gradeId) {
        List<List<IdNameDTO>> arrangingZBCourseList = paikeService.getArrangingPECourse(term, gradeId);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("arrangingPECourseList", arrangingZBCourseList);
        return result;
    }

    /**
     * 获取体育课已排课程
     *
     * @param term
     * @param gradeId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/getArrangedPECourse")
    @ResponseBody
    public Map<String, Object> getArrangedPECourse(String term, @ObjectIdType ObjectId gradeId) {
        List<CourseItemDTO> list = paikeService.getPETimetable(term, gradeId);
        TimetableConfDTO timetableConfDTO = timetableConfService.getTimetableConf(term, gradeId);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("courseItemList", list);
        map.put("timetableConf", timetableConfDTO);
        return map;
    }

    /**
     * 体育课自动排课
     *
     * @param term
     * @param gradeId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/autoArrangePECourse")
    @ResponseBody
    public RespObj autoArrangePhysical(String term, String gradeId) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            paikeService.autoArrangePE(term, getSchoolId(), gradeId);
            respObj.setCode(RespObj.SUCCESS.code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respObj;
    }

    /**
     * 获取体育课可用时间点
     *
     * @param term
     * @param gradeId
     * @param courseIdStr
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/getAvailablePointForPE")
    @ResponseBody
    public RespObj getAvailablePointForPE(String term, String gradeId, String courseIdStr) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            List<PointJson> list = paikeService.getAvailablePointForPE(term, getSchoolId(), gradeId, courseIdStr);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(list);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 新增体育课
     *
     * @param term
     * @param courseIdStr
     * @param x
     * @param y
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/addPECourse")
    @ResponseBody
    public RespObj addPECourse(String term, String courseIdStr, int x, int y) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            paikeService.addPECourse(term, courseIdStr, x, y);
            respObj.setCode(RespObj.SUCCESS.code);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 删除体育课
     *
     * @param term
     * @param gradeId
     * @param x
     * @param y
     * @return
     */
    @UserRoles(value = {UserRole.HEADMASTER, UserRole.ADMIN})
    @RequestMapping("/removePECourse")
    @ResponseBody
    public RespObj removePECourse(String term, String gradeId, int x, int y) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            paikeService.removePECourse(term, gradeId, x, y);
            respObj.setCode(RespObj.SUCCESS.code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respObj;
    }

    /**
     * 检查体育课是否排完
     *
     * @param term
     * @return
     */
    @UserRoles(value = {UserRole.HEADMASTER, UserRole.ADMIN})
    @RequestMapping("/getPEState")
    @ResponseBody
    public RespObj getPEState(String term, String gradeId) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            String lock = "UNLOCK";
            if (paikeService.checkPEFinished(term, gradeId)) {
                lock = "LOCK";
            }
            respObj.setCode(RespObj.SUCCESS.code);
            respObj.setMessage(lock);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respObj;
    }

    //======================================================非走班排课===================================================

    /**
     * 获取非走班课已排课课表
     *
     * @param term
     * @param classId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/getArrangingFZBCourse")
    @ResponseBody
    public Map<String, Object> getArrangingFZBCourse(String term, String classId) {
        List<List<IdNameDTO>> arrangingFZBCourseList = paikeService.getArrangingFZBCourse(term, classId);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("arrangingFZBCourseList", arrangingFZBCourseList);
        return result;
    }

    /**
     * 获取非走班已排课程
     *
     * @param term
     * @param gradeId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/getArrangedFZBCourse")
    @ResponseBody
    public Map<String, Object> getArrangedFZBCourse(String term, @ObjectIdType ObjectId gradeId, String classId) {
        List<CourseItemDTO> list = paikeService.getFZBTimetable(term, classId);
        TimetableConfDTO timetableConfDTO = timetableConfService.getTimetableConf(term, gradeId);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("courseItemList", list);
        map.put("timetableConf", timetableConfDTO);
        return map;
    }

    /**
     * 获取非走班可用时间点
     *
     * @param term
     * @param gradeId
     * @param courseId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/getAvailablePointForFZB")
    @ResponseBody
    public RespObj getAvailablePointForFZB(String term, String gradeId, String courseId) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            List<PointJson> pointList = paikeService.getAvailablePointForFZB(term, getSchoolId(), gradeId, courseId);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(pointList);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 添加非走班课
     *
     * @param term
     * @param courseId
     * @param x
     * @param y
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/addFZBCourse")
    @ResponseBody
    public RespObj addFZBCourse(String term, String gradeId, String courseId, int x, int y) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            paikeService.addFZBCourse(term, gradeId, courseId, x, y);
            respObj.setCode(RespObj.SUCCESS.code);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 删除非走班课
     *
     * @param term
     * @param classId
     * @param x
     * @param y
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/removeFZBCourse")
    @ResponseBody
    public RespObj removeFZBCourse(String term, String gradeId, String classId, int x, int y) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            paikeService.removeFZBCourse(term, gradeId, classId, x, y);
            respObj.setCode(RespObj.SUCCESS.code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respObj;
    }

    /**
     * 非走班自动排课
     *
     * @param term
     * @param gradeId
     * @param classId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/autoArrangeFZBCourse")
    @ResponseBody
    public RespObj autoArrangeFZBCourse(String term, String gradeId, String classId) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            paikeService.autoArrangeFZBCourse(term, getSchoolId(), gradeId, classId);
            respObj.setCode(RespObj.SUCCESS.code);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 课表是否发布
     *
     * @param term
     * @param gradeId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/isPublic")
    @ResponseBody
    public Map<String, Object> isPublic(String term, String gradeId) {
        int isPublished = paikeService.isPublished(term, getSchoolId().toString(), gradeId);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("state", isPublished);
        return result;
    }


    /**
     * 检查是否有课表发布
     * @param term
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/checkPublish")
    @ResponseBody
    public RespObj checkPublish(String term) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            boolean isPublished = paikeService.isPublic(term, getSchoolId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(isPublished);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e);
        }
        return respObj;
    }



    /**
     * 发布课表
     * 流程---检测本年级所有班级课表是否排课完成，完成则根据原始课表（第0周课表）生成每周课表
     *
     * @param term
     * @param gradeId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/publishTimetable")
    @ResponseBody
    public RespObj publishCourse(String term, String gradeId) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            Map<String, String> map = timeTableService.publishCourse(term, getSchoolId(), new ObjectId(gradeId));
            if (map.get("result").equals("SUCCESS")) {
                zoubanStateService.setZoubanState(term, gradeId, 6);
                respObj.setCode(RespObj.SUCCESS.code);
            } else {
                respObj.setMessage(map.get("reason"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respObj;
    }

    /**
     * 取消发布
     *
     * @param term
     * @param gradeId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/unPublishTimetable")
    @ResponseBody
    public RespObj cancelPublishCourse(String term, String gradeId) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            Map<String, String> map = timeTableService.cancelPublishCourse(term, getSchoolId(), new ObjectId(gradeId));
            if (map.get("result").equals("SUCCESS")) {
                zoubanStateService.setZoubanState(term, gradeId, 4);
                respObj.setCode(RespObj.SUCCESS.code);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respObj;
    }


    /**
     * 获取所有事务冲突
     * 
     * @param term
     * @param gradeId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/getConflictList")
    @ResponseBody
    public RespObj getConflictList(String term, @ObjectIdType ObjectId gradeId) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            List<EventConflictDTO> eventConflictDTOs = eventConflictService.getConflictList(term, gradeId);
            respObj.setCode(RespObj.SUCCESS.code);
            respObj.setMessage(eventConflictDTOs);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }


}
