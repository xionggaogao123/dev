package com.fulaan.teacherevaluation.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.fulaan.teacherevaluation.service.EvaluationTeacherService;
import com.pojo.teacherevaluation.EvaluationTeacherDTO;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fl on 2016/9/12.
 */
@Controller
@RequestMapping("/teacherevaluation/teacher")
public class EvaluationTeacherController extends BaseController {

    @Autowired
    private EvaluationTeacherService evaluationTeacherService;

    /**
     * 教师首页
     * @return
     */
    @RequestMapping("")
    public String teacherIndex(){
        return "/teacherevaluation/teacher";
    }

    /**
     * 管理员首页
     * @return
     */
    @RequestMapping("/manage")
    public String index(){
        return "/teacherevaluation/manager";
    }

    /**
     * 获取教师的个人陈述和实证资料
     * @param teacherId
     * @return
     */
    @RequestMapping("/statementAndEvidence")
    @ResponseBody
    public RespObj getTeacherStatementAndEvidence(@ObjectIdType(isRequire = false) ObjectId teacherId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        if(teacherId == null)
            teacherId = getUserId();
        EvaluationTeacherDTO dto = evaluationTeacherService.getEvaluationTeacherEntryByTeacherId(teacherId, getSchoolId());
        respObj.setMessage(dto);
        return respObj;
    }

    /**
     * 更新教师个人陈述
     * @param statement
     * @return
     */
    @RequestMapping(value = "/updateStatement", method = RequestMethod.POST)
    @ResponseBody
    public RespObj updateStatement(String statement){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        evaluationTeacherService.updateStatement(getUserId(), statement);
        return respObj;
    }

    /**
     * 更新教师个人陈述并推送
     * @param statement
     * @param evaluationId
     * @return
     */
    @RequestMapping(value = "/updateAndPushStatement", method = RequestMethod.POST)
    @ResponseBody
    public RespObj updateAndPushStatement(String statement, @ObjectIdType ObjectId evaluationId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        evaluationTeacherService.updateAndPushStatement(getUserId(), evaluationId, statement);
        return respObj;
    }

    /**
     * 推送教师个人陈述
     * @param evaluationId
     * @return
     */
    @RequestMapping("/pushStatement")
    @ResponseBody
    public RespObj pushStatement(@ObjectIdType ObjectId evaluationId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        evaluationTeacherService.pushStatement(getUserId(), evaluationId);
        return respObj;
    }

    /**
     * 获取教师的实证资料
     * @param teacherId
     * @return
     */
    @RequestMapping("/evidence")
    @ResponseBody
    public RespObj getTeacherEvidence(@ObjectIdType(isRequire = false) ObjectId teacherId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        if(teacherId == null)
            teacherId = getUserId();
        EvaluationTeacherDTO dto = evaluationTeacherService.getEvaluationTeacherEntryByTeacherId(teacherId, getSchoolId());
        dto.setStatement("");
        respObj.setMessage(dto);
        return respObj;
    }

    /**
     * 更新教师实证资料
     * @param evidence
     * @return
     */
    @RequestMapping(value = "/updateEvidence", method = RequestMethod.POST)
    @ResponseBody
    public RespObj updateEvidence(String evidence, @ObjectIdType ObjectId teacherId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        evaluationTeacherService.updateEvidence(teacherId, evidence);
        return respObj;
    }

    /**
     * 推送实证资料
     * @param evaluationId
     * @return
     */
    @RequestMapping("/pushEvidence")
    @ResponseBody
    public RespObj pushEvidence(@ObjectIdType ObjectId evaluationId, @ObjectIdType ObjectId teacherId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        evaluationTeacherService.pushEvidence(teacherId, evaluationId);
        return respObj;
    }

    /**
     * 批量推送实证资料
     * @param evaluationId
     * @param teacherIds
     * @return
     */
    @RequestMapping("/pushAllEvidence")
    @ResponseBody
    public RespObj pushAllEvidence(@ObjectIdType ObjectId evaluationId, String teacherIds) {
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        try {
            evaluationTeacherService.pushAllEvidence(teacherIds, evaluationId);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = new RespObj(Constant.FAILD_CODE, e.getMessage());
        }
        return respObj;
    }

    /**
     * 老师列表
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    @UserRoles(value = {UserRole.HEADMASTER, UserRole.ADMIN})
    public RespObj getEvaluationTeacherEntryBySchoolId(int page, int pageSize){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        List<EvaluationTeacherDTO> list = evaluationTeacherService.getEvaluationTeacherEntryBySchoolId(getSchoolId(), page, pageSize);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("list", list);
        data.put("page", page);
        data.put("pageSize", pageSize);
        data.put("count", evaluationTeacherService.countEvaluationTeacherEntryBySchoolId(getSchoolId()));
        respObj.setMessage(data);
        return respObj;
    }

    /**
     * 某评价报名老师列表
     * @param evaluationId
     * @return
     */
    @RequestMapping("/listByEva")
    @ResponseBody
    @UserRoles(value = {UserRole.HEADMASTER, UserRole.ADMIN})
    public RespObj getEvaluationTeacherEntryByEvaluationId(@ObjectIdType ObjectId evaluationId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        List<EvaluationTeacherDTO> list = evaluationTeacherService.getEvaluationTeacherEntryByEvaluationId(evaluationId, getSchoolId());
        respObj.setMessage(list);
        return respObj;
    }

}
