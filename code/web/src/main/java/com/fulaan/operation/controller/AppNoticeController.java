package com.fulaan.operation.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.base.BaseController;
import com.fulaan.operation.dto.AppNoticeDTO;
import com.fulaan.operation.service.AppNoticeService;
import com.fulaan.pojo.User;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import io.swagger.annotations.Api;
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

import java.util.List;
import java.util.Map;

/**
 * Created by scott on 2017/9/22.
 */
@Api(value = "通知相关接口")
@Controller
@RequestMapping("/appNotice")
public class AppNoticeController extends BaseController {

    @Autowired
    private AppNoticeService appNoticeService;

    @RequestMapping("/getReadNoticeUserList")
    @ResponseBody
    @ApiOperation(value = "查询已阅和未阅列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    public RespObj getReadNoticeUserList(@ObjectIdType ObjectId id){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,List<User>> userList=appNoticeService.getReadNoticeUserList(id);
            respObj.setMessage(userList);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    @RequestMapping("/saveAppNoticeEntry")
    @ResponseBody
    @ApiOperation(value = "保存通知信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    public RespObj getReadNoticeUserList(@RequestBody AppNoticeDTO dto){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
           appNoticeService.saveAppNoticeEntry(dto,getUserId());
            respObj.setMessage("保存通知信息成功！");
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    @RequestMapping("/getMySendAppNoticeDtos")
    @ResponseBody
    @ApiOperation(value = "获取我发送的通知", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    public RespObj getMySendAppNoticeDtos(
            @RequestParam(required = false, defaultValue = "1")int page,
            @RequestParam(required = false, defaultValue = "10")int pageSize
            ){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            List<AppNoticeDTO> dtos=appNoticeService.getMySendAppNoticeDtos(getUserId(),page,pageSize);
            respObj.setMessage(dtos);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    @RequestMapping("/getMyReceivedAppNoticeDtos")
    @ResponseBody
    @ApiOperation(value = "获取我接收到的通知", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    public RespObj getMyReceivedAppNoticeDtos(
            @RequestParam(required = false, defaultValue = "1")int page,
            @RequestParam(required = false, defaultValue = "10")int pageSize
    ){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            List<AppNoticeDTO> dtos=appNoticeService.getMyReceivedAppNoticeDtos(getUserId(),page,pageSize);
            respObj.setMessage(dtos);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }



    @RequestMapping("/pushRead")
    @ResponseBody
    @ApiOperation(value = "阅读该通知消息", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    public RespObj pushRead(
           @ObjectIdType ObjectId id
    ){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            appNoticeService.pushRead(id,getUserId());
            respObj.setMessage("阅读成功!");
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

}
