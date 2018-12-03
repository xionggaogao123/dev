package com.fulaan.count.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fulaan.count.dto.JxmCountDto;
import com.fulaan.count.service.CountService;
import com.fulaan.jiaschool.dto.HomeSchoolDTO;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value="后台统计")
@Controller
@RequestMapping("/web/count")
public class CountController {

    @Autowired
    private CountService countService;
    
    
    @ApiOperation(value = "获取学校列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getSimpleSchoolList")
    @ResponseBody
    public RespObj getSimpleSchoolList() {
        RespObj respObj=new RespObj(Constant.SUCCESS_CODE);
        try {
            List<HomeSchoolDTO> list = countService.getSimpleSchoolList();
            respObj.setMessage(list);
        } catch (Exception e) {
            // TODO: handle exception
            respObj.setErrorMessage(e.getMessage());
            respObj.setCode(Constant.FAILD_CODE);
        }
        
        return respObj;
    }
    
    @ApiOperation(value = "获取家校美统计详情", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/jxmCount")
    @ResponseBody
    
    public RespObj jxmCount(String schooleId) {
        RespObj respObj=new RespObj(Constant.SUCCESS_CODE);
        try {
            JxmCountDto jxmCountDto = countService.jxmCount(schooleId);
            respObj.setMessage(jxmCountDto);
        } catch (Exception e) {
            // TODO: handle exception
            respObj.setErrorMessage(e.getMessage());
            respObj.setCode(Constant.FAILD_CODE);
        }
        
        return respObj;
    }
    
    @ApiOperation(value = "作业发布统计图表", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/zytb")
    @ResponseBody
    public RespObj zytb(String startTime, String endTime) {
        RespObj respObj=new RespObj(Constant.SUCCESS_CODE);
        
        return respObj;
    }
}
