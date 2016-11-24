package com.fulaan.teacherevaluation.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.fulaan.teacherevaluation.service.EvaluationService;
import com.pojo.teacherevaluation.MemberGroupDTO;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by fl on 2016/7/29.
 */
@Controller
@RequestMapping("/teacherevaluation")
public class EvaluationController extends BaseController {

    @Autowired
    private EvaluationService evaluationService;

    @RequestMapping(method = RequestMethod.GET)
    public String toPage(Map<String, Object> model){
        int userRole = getSessionValue().getUserRole();
        if(UserRole.isHeadmaster(userRole)){
            model.put("headmaster", true);
        } else {
            model.put("headmaster", false);
        }
        return "/teacherevaluation/list";
    }

    /**
     * 列表
     * @param year
     * @return
     */
    @RequestMapping(value = "/{year}/list", method = RequestMethod.GET)
    @ResponseBody
    public RespObj getEvaluations(@PathVariable String year){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        int role = getSessionValue().getUserRole();
        List<MemberGroupDTO> memberGroupDTOList = evaluationService.getEvaluations(getSchoolId(), year, getUserId(),
                UserRole.isHeadmaster(role) || UserRole.isManager(role));
        respObj.setMessage(memberGroupDTOList);
        return respObj;
    }

    /**
     * 未开始评价列表
     * @return
     */
    @RequestMapping(value = "/list/unbegin", method = RequestMethod.GET)
    @ResponseBody
    public RespObj getUnBeginEvaluationsForTeacher(){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        List<MemberGroupDTO> memberGroupDTOList = evaluationService.getUnBeginEvaluationsForTeacher(getSchoolId(), getUserId());
        respObj.setMessage(memberGroupDTOList);
        return respObj;
    }

    /**
     * 未开始评价列表
     * @return
     */
    @RequestMapping(value = "/list/manager/unbegin", method = RequestMethod.GET)
    @ResponseBody
    public RespObj getUnBeginEvaluationsForManager(){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        List<MemberGroupDTO> memberGroupDTOList = evaluationService.getUnBeginEvaluationsForManager(getSchoolId());
        respObj.setMessage(memberGroupDTOList);
        return respObj;
    }

    /**
     * 新增
     * @param year
     * @param name
     * @return
     */
    @RequestMapping(value = "/{year}", method = RequestMethod.POST)
    @ResponseBody
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    public RespObj addEvaluation(@PathVariable String year, String name){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        evaluationService.addEvaluations(getSchoolId(), year, name);
        return respObj;
    }

    /**
     * 更新
     * @param evaluationId
     * @param name
     * @return
     */
    @RequestMapping(value = "/{evaluationId}/put", method = RequestMethod.POST)
    @ResponseBody
    public RespObj updateEvaluation(@PathVariable @ObjectIdType ObjectId evaluationId, String name){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        evaluationService.updateEvaluation(evaluationId, name);
        return respObj;
    }

    /**
     * 删除
     * @param evaluationId
     * @return
     */
    @RequestMapping(value = "/{evaluationId}/deletion", method = RequestMethod.POST)
    @ResponseBody
    public RespObj deleteEvaluation(@PathVariable @ObjectIdType ObjectId evaluationId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        evaluationService.removeEvaluation(evaluationId);
        return respObj;
    }

    /**
     * 报名或取消报名
     * @param evaluationId
     * @return
     */
    @RequestMapping(value = "/{evaluationId}/sign", method = RequestMethod.POST)
    @ResponseBody
    public RespObj sign(@PathVariable @ObjectIdType ObjectId evaluationId,
                        @RequestParam(required = false) @ObjectIdType ObjectId teacherId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
       if(teacherId == null){
           teacherId = getUserId();
       }
        try {
            int state = evaluationService.sign(evaluationId, teacherId);
            respObj.setMessage(state);
        } catch (Exception e) {
            respObj = new RespObj(Constant.FAILD_CODE, e.getMessage());
        }
        return respObj;
    }







}
