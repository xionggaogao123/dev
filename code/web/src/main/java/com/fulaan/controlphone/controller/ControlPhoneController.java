package com.fulaan.controlphone.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.base.BaseController;
import com.fulaan.controlphone.dto.ControlPhoneDTO;
import com.fulaan.controlphone.service.ControlPhoneService;
import com.sys.utils.RespObj;
import io.swagger.annotations.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by James on 2017/11/3.
 */
@Controller
@RequestMapping("/jxmapi/controlphone")
@Api(value = "管控端手机")
public class ControlPhoneController extends BaseController {

    @Autowired
    private ControlPhoneService controlPhoneService;

    /**
     * 添加管控手机号
     * @param dto
     * @return
     */
    @ApiOperation(value = "添加管控手机号", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/addControlPhone")
    @ResponseBody
    public String addControlPhone(@ApiParam @RequestBody ControlPhoneDTO dto){
        //
        dto.setParentId(getUserId().toString());
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            String result = controlPhoneService.addControlPhone(dto);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("添加管控手机号失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 查询手机list
     * @return
     */
    @ApiOperation(value = "查询手机list", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getControlPhoneList")
    @ResponseBody
    public String getControlPhoneList(@ApiParam(name = "sonId", required = true, value = "孩子id") @RequestParam("sonId") String sonId){
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            List<ControlPhoneDTO> result = controlPhoneService.getControlPhoneList(getUserId(),new ObjectId(sonId));
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("查询手机list失败!");
        }
        return JSON.toJSONString(respObj);
    }


    /**
     * 删除手机号
     * @return
     */
    @ApiOperation(value = "删除手机号", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/delControlPhone")
    @ResponseBody
    public String delControlPhone(@ApiParam(name = "id", required = true, value = "手机号id") @RequestParam("id") String id){
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            controlPhoneService.delControlPhone(new ObjectId(id));
            respObj.setMessage("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("删除手机号失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 修改手机号
     * @return
     */
    @ApiOperation(value = "修改手机号", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/updateControlPhone")
    @ResponseBody
    public String updateControlPhone(@ApiParam(name = "id", required = true, value = "手机号id") @RequestParam("id") String id,
                                     @ApiParam(name = "name", required = true, value = "姓名") @RequestParam("name") String name,
                                     @ApiParam(name = "phone", required = true, value = "手机号") @RequestParam("phone") String phone){
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            controlPhoneService.updateControlPhone(new ObjectId(id),name,phone);
            respObj.setMessage("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("修改手机号失败!");
        }
        return JSON.toJSONString(respObj);
    }



}
