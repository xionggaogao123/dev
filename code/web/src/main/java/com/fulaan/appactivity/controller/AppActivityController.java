package com.fulaan.appactivity.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.appactivity.dto.AppActivityDTO;
import com.fulaan.appactivity.service.AppActivityService;
import com.fulaan.base.BaseController;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by scott on 2017/12/27.
 */
@Controller
@RequestMapping("/jxmapi/appActivity")
public class AppActivityController extends BaseController {

    @Autowired
    private AppActivityService appActivityService;

    @ApiOperation(value = "保存活动记录", httpMethod = "GET", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful — 请求已完成", response = RespObj.class)})
    @RequestMapping("/saveAppActivity")
    @ResponseBody
    public RespObj saveAppActivity(@RequestBody AppActivityDTO appActivityDTO) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            appActivityService.saveEntry(appActivityDTO);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("保存成功");
        } catch (Exception e) {
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    @ApiOperation(value = "获取集合活动列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/gatherAppActivities")
    @ResponseBody
    public RespObj gatherAppActivities(@RequestParam(required = false,defaultValue = "1")int page,
                                  @RequestParam(required = false,defaultValue = "10")int pageSize){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> retMap=appActivityService.getGatherAppActivities(getUserId(),page,pageSize);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(retMap);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    @ApiOperation(value = "获取活动参与以及未参与人员列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/getPartInActivityUserList")
    @ResponseBody
    public RespObj getPartInActivityUserList(@ObjectIdType ObjectId activityId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> retMap=appActivityService.getPartInActivityUserList(activityId);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(retMap);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    @ApiOperation(value = "获取学生收到的活动", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/getStudentReceivedAppActivities")
    @ResponseBody
    public RespObj getStudentReceivedAppActivities(@RequestParam(required = false,defaultValue = "1")int page,
                                              @RequestParam(required = false,defaultValue = "10")int pageSize){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> retMap=appActivityService.getStudentReceivedAppVotes(getUserId(),page,pageSize);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(retMap);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    @ApiOperation(value = "报名/取消报名", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/partInActivity")
    @ResponseBody
    public RespObj partInActivity(@ObjectIdType ObjectId activityId,int type){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            appActivityService.partInActivity(activityId,type,getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            if(type==Constant.ONE){
                respObj.setMessage("报名成功");
            }else{
                respObj.setMessage("取消报名成功");
            }
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


}