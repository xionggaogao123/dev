package com.fulaan.controlphone.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.base.BaseController;
import com.fulaan.controlphone.service.ControlSchoolPhoneService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import io.swagger.annotations.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by James on 2018-09-27.
 *   新校管控逻辑
 *
 *
 */
@Api(value = "新校管控")
@Controller
@RequestMapping("/jxmapi/controlschool")
public class ControlSchoolController extends BaseController {
    @Autowired
    private ControlSchoolPhoneService controlSchoolPhoneService;

    /**
     * 老师首页加载基础信息
     */
    @ApiOperation(value = "老师首页加载", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getSimpleMessageForTea")
    @ResponseBody
    public String getSimpleMessageForTea(){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> dtos= controlSchoolPhoneService.getSimpleMessageForTea(getUserId());
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("老师首页加载失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 老师首页加载一条基础信息
     */
    @ApiOperation(value = "老师首页加载一条基础信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getOneCommunityMessageForTea")
    @ResponseBody
    public String getOneCommunityMessageForTea(@ApiParam(name = "communityId", required = true, value = "社群id") @RequestParam(value = "communityId") String communityId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> message= controlSchoolPhoneService.getOneCommunityMessageForTea(new ObjectId(communityId),getUserId());
            respObj.setMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("老师首页加载一条基础信息失败!");
        }
        return JSON.toJSONString(respObj);
    }


    /**
     * 设置班级自由时间
     */
    @ApiOperation(value = "设置班级自由时间", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/addCommunityFreeTime")
    @ResponseBody
    public String addCommunityFreeTime(@ApiParam(name = "communityId", required = true, value = "社群id") @RequestParam(value = "communityId") String communityId,
                                       @ApiParam(name = "appId", required = true, value = "应用id") @RequestParam(value = "appId") String appId,
                                       @ApiParam(name = "freeTime", required = true, value = "自由时间") @RequestParam(value = "freeTime") int freeTime){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            controlSchoolPhoneService.addCommunityFreeTime(new ObjectId(appId),new ObjectId(communityId),getUserId(), freeTime);
            respObj.setMessage("设置成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 关闭班级自由时间
     */
    @ApiOperation(value = "关闭班级自由时间", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/deleteCommunuityFreeTime")
    @ResponseBody
    public String deleteCommunuityFreeTime(@ApiParam(name = "communityId", required = true, value = "社群id") @RequestParam(value = "communityId") String communityId,
                                       @ApiParam(name = "appId", required = true, value = "应用id") @RequestParam(value = "appId") String appId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            controlSchoolPhoneService.deleteCommunuityFreeTime(new ObjectId(appId), new ObjectId(communityId), getUserId());
            respObj.setMessage("关闭成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 设置放学防沉迷时间
     */
    @ApiOperation(value = "设置放学防沉迷时间", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/updateHomeAppTime")
    @ResponseBody
    public String updateHomeAppTime(@ApiParam(name = "communityId", required = true, value = "社群id") @RequestParam(value = "communityId") String communityId,
                                           @ApiParam(name = "appId", required = true, value = "应用id") @RequestParam(value = "appId") String appId,
                                           @ApiParam(name = "freeTime", required = true, value = "自由时间") @RequestParam(value = "freeTime") int freeTime,
                                           @ApiParam(name = "type", required = true, value = "类型") @RequestParam(value = "type") int type){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            controlSchoolPhoneService.updateHomeAppTime(new ObjectId(appId), new ObjectId(communityId), getUserId(),freeTime,type);
            respObj.setMessage("设置放学防沉迷时间成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 查询应用的具体放学设置
     */
    @ApiOperation(value = "查询应用的具体放学设置", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/selectHomeAppList")
    @ResponseBody
    public String selectHomeAppList(@ApiParam(name = "communityId", required = true, value = "社群id") @RequestParam(value = "communityId") String communityId,
                                    @ApiParam(name = "appId", required = true, value = "应用id") @RequestParam(value = "appId") String appId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> result = controlSchoolPhoneService.selectHomeAppList(new ObjectId(appId), new ObjectId(communityId), getUserId());
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return JSON.toJSONString(respObj);
    }


    /**
     * 学生登陆情况获得所有管控信息
     */
    @ApiOperation(value = "学生登陆情况获得所有管控信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getStudentLoginControl")
    @ResponseBody
    public String getStudentLoginControl(@ApiParam(name = "communityId", required = true, value = "社群id") @RequestParam(value = "communityId") String communityId,
                                    @ApiParam(name = "appId", required = true, value = "应用id") @RequestParam(value = "appId") String appId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> result = controlSchoolPhoneService.selectHomeAppList(new ObjectId(appId), new ObjectId(communityId), getUserId());
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 学生未登陆情况获得所有管控信息
     */
    @ApiOperation(value = "学生登陆情况获得所有管控信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getStudentUnLoginControl")
    @ResponseBody
    public String getStudentUnLoginControl(@ApiParam(name = "communityId", required = true, value = "社群id") @RequestParam(value = "communityId") String communityId,
                                         @ApiParam(name = "appId", required = true, value = "应用id") @RequestParam(value = "appId") String appId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> result = controlSchoolPhoneService.selectHomeAppList(new ObjectId(appId), new ObjectId(communityId), getUserId());
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return JSON.toJSONString(respObj);
    }
}
