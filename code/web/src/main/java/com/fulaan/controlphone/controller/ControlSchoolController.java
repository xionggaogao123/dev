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
                                       @ApiParam(name = "freeTime", required = true, value = "自由时间") @RequestParam(value = "freeTime") int freeTime){
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
}
