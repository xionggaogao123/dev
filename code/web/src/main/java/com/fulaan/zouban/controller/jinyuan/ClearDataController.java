package com.fulaan.zouban.controller.jinyuan;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.fulaan.zouban.service.PaikeService;
import com.fulaan.zouban.service.TimeTableService;
import com.fulaan.zouban.service.TimetableConfService;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by wangkaidong on 2016/11/11.
 *
 * 用于清空bug导致的非法数据
 */

@Controller
@RequestMapping("/clearData")
public class ClearDataController extends BaseController {
    @Autowired
    private TimetableConfService timetableConfService;
    @Autowired
    private PaikeService paikeService;
    @Autowired
    private TimeTableService timeTableService;




    /**
     * 删除全校事务（用于清空残留事务）
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/removeAllEvent")
    @ResponseBody
    public RespObj removeAllEvent(String term) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            timetableConfService.removeAllEvent(getSchoolId().toString(), term);
            respObj.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e);
        }
        return respObj;
    }



    /**
     * 删除脏数据（删除信息不完整的教学班）
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/removeInvalidCourse")
    @ResponseBody
    public RespObj removeInvalidCourse(String term) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            int count = paikeService.removeInvalidCourse(term, getSchoolId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(count);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e);
        }
        return respObj;
    }



    /**
     * 删除全校课表
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/removeAllTimetable")
    @ResponseBody
    public RespObj removeAllTimetable() {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            timeTableService.removeTimetableBySchoolId(getSchoolId());
            respObj.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            respObj.setMessage(e);
        }
        return respObj;
    }

    /**
     * 清空课程
     * @param term
     * @param gradeId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/removeCourse")
    @ResponseBody
    public RespObj removeCourse(String term, @ObjectIdType ObjectId gradeId, int type) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            timeTableService.removeCourse(term, gradeId, type);
            respObj.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            respObj.setMessage(e);
        }
        return respObj;
    }


}
