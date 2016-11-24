package com.fulaan.zouban.controller.jinyuan;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.fulaan.utils.StringUtil;
import com.fulaan.zouban.dto.*;
import com.fulaan.zouban.service.*;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangkaidong on 2016/10/12.
 * <p/>
 * 调课Controller
 */
@Controller
@RequestMapping("/zouban/tiaoke")
public class StepSixController extends BaseController {

    @Autowired
    private AdjustCourseService adjustCourseService;
    @Autowired
    private ZoubanModeService zoubanModeService;
    @Autowired
    private TermService termService;
    @Autowired
    private TimetableConfService timetableConfService;


    /**
     * 调课页
     *
     * @param term
     * @param gid
     * @param gnm
     * @param model
     * @param response
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping
    public String tiaoke(String term, String gid, String gnm, Model model, HttpServletResponse response) {
        try {
            Map<String, Object> map = termService.getCurrentTerm(getSessionValue().getSchoolId());

            model.addAttribute("year", URLDecoder.decode(term, "UTF-8"));
            model.addAttribute("term", URLDecoder.decode((String) map.get("term"), "UTF-8"));
            model.addAttribute("gradeId", gid);
            model.addAttribute("gradeName", URLDecoder.decode(gnm, "UTF-8"));
            model.addAttribute("allweek", map.get("allweek"));
            model.addAttribute("curweek", map.get("curweek"));
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
        }
        return "zoubannew/admin/tiaoke";
    }


    /**
     * 查询调课记录
     *
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/getZoubanNotice")
    @ResponseBody
    public Map<String, Object> getZoubanNotice(String term, String gradeId, int page, int pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        //查询数据
        List<ZoubanNoticeDTO> zoubanNoticeDTOs = adjustCourseService.findNoticeList(term, new ObjectId(gradeId), page, pageSize);
        int count = adjustCourseService.getNoticeCount(term, new ObjectId(gradeId));
        map.put("zoubanNotice", zoubanNoticeDTOs);
        map.put("count", count);
        map.put("page", page);
        map.put("pageSize", pageSize);
        return map;
    }


    /**
     * 查询调课记录
     *
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/getNoticeInfo")
    @ResponseBody
    public RespObj getNoticeById(@ObjectIdType ObjectId noticeId) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(adjustCourseService.getNoticeInfo(noticeId));
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     * 检查是否可以跨周调课
     *
     * @param term
     * @param startWeek
     * @param endWeek
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/checkNotice")
    @ResponseBody
    public RespObj checkNotice(String term, int startWeek, int endWeek) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            boolean hasNotice = false;
            if (startWeek != endWeek) {//长期调课需要检查是否可调
                hasNotice = adjustCourseService.checkNotice(term, getSchoolId(), startWeek, endWeek);
            }
            respObj.setCode(Constant.SUCCESS_CODE);
            if (hasNotice) {//有调课记录则不可调
                respObj.setMessage("false");
            } else {//无调课记录则可调
                respObj.setMessage("true");
            }
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     * 添加调课记录
     *
     * @param term
     * @param gradeId
     * @param userName
     * @param description
     * @param startWeek
     * @param endWeek
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/addNotice")
    @ResponseBody
    public RespObj addNotice(String term, @ObjectIdType ObjectId gradeId, String userName, String description, int startWeek, int endWeek) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            adjustCourseService.addNotice(term, getSchoolId(), gradeId, userName, description, startWeek, endWeek);
            respObj.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     * 查询课表
     *
     * @param term
     * @param gradeId
     * @param classId
     * @param startWeek
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/getTimetable")
    @ResponseBody
    public RespObj getTimetable(String term, @ObjectIdType ObjectId gradeId, @ObjectIdType ObjectId classId, int startWeek) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            List<StudentTimeTable> list = adjustCourseService.getTimetable(term, classId, startWeek);
            TimetableConfDTO timetableConfDTO = timetableConfService.getTimetableConf(term.substring(0, 11), gradeId);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("courseItemList", list);
            map.put("timetableConf", timetableConfDTO);

            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(map);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 获取调课可用时间点
     *
     * @param term
     * @param classId
     * @param teacherId
     * @param week
     * @param x
     * @param y
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/getAvailablePoint")
    @ResponseBody
    public RespObj getAvailablePoint(String term, String gradeId, @ObjectIdType ObjectId classId, @ObjectIdType ObjectId teacherId,
                                     int week, int x, int y) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            List<PointJson> pointList = adjustCourseService.getAvailablePoint(term, gradeId, classId, week, teacherId, x, y);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(pointList);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     * 添加调课（交换两个课的坐标）
     *
     * @param term
     * @param classId
     * @param weekStr
     * @param courseItemId1
     * @param x1
     * @param y1
     * @param courseItemId2
     * @param x2
     * @param y2
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/addChange")
    @ResponseBody
    public RespObj addAdjustCourse(String term, @ObjectIdType ObjectId classId, String weekStr,
                                   @ObjectIdType ObjectId courseItemId1, @ObjectIdType ObjectId courseItemId2,
                                   int x1, int y1, int x2, int y2) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            adjustCourseService.addChange(term, classId, weekStr, courseItemId1, courseItemId2, x1, y1, x2, y2);
            respObj.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     * 保存调课结果
     *
     * @param term
     * @param gradeId
     * @param weekStr
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/saveChange")
    @ResponseBody
    public RespObj saveChange(String term, @ObjectIdType ObjectId gradeId, String weekStr, @ObjectIdType ObjectId noticeId) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            adjustCourseService.saveChange(term, gradeId, weekStr, noticeId, getUserId(),
                    getSessionValue().getChatid().toString(), getSessionValue().getUserName(), getSessionValue().getMaxAvatar());
            respObj.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     * 删除调课结果
     *
     * @param term
     * @param gradeId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/removeChange")
    @ResponseBody
    public RespObj removeChange(String term, @ObjectIdType ObjectId gradeId) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            adjustCourseService.removeChange(term, gradeId);
            respObj.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 导出调课notice
     * @param noticeId
     * @param response
     */
    @RequestMapping("/exportTKNotice")
    @ResponseBody
    public  void exportTKNotice(String noticeId, HttpServletResponse response){
        if(StringUtils.isNotBlank(noticeId)){
            adjustCourseService.exportTKNotice(noticeId, response);
        }
    }
}
