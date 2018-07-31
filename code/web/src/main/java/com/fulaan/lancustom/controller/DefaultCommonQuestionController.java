package com.fulaan.lancustom.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fulaan.lancustom.dto.CommonQuestionDto;
import com.fulaan.lancustom.service.CommonQuestionService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value="小兰客服")
@Controller
@RequestMapping("/jxmapi/commonQuestion")
public class DefaultCommonQuestionController {

    @Autowired
    private CommonQuestionService commonQuestionService;
    
    
    @ApiOperation(value = "常见问题列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "商品列表",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getCommonQuestion")
    @ResponseBody
    public RespObj getCommonQuestion(String pid,
                                          String name,
                                          @RequestParam(required = false, defaultValue = "1")int page,
                                          @RequestParam(required = false, defaultValue = "10")int pageSize) {
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            List<CommonQuestionDto> list = commonQuestionService.getCommonQuestion(pid, name, page, pageSize);
            Map<String,Object> retMap=new HashMap<String,Object>();
            retMap.put("list", list);
            retMap.put("count", commonQuestionService.getCommonQuestionCount(pid, name));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(retMap);
        } catch (Exception e) {
            // TODO: handle exception
            respObj.setErrorMessage(e.getMessage());
        }
        
        return respObj;
    }
    
    @ApiOperation(value = "常见问题列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "商品列表",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getCommonQuestionByType")
    @ResponseBody
    public RespObj getCommonQuestionByType(@RequestParam(required = false, defaultValue = "0")int type,
                                          String name,
                                          @RequestParam(required = false, defaultValue = "1")int page,
                                          @RequestParam(required = false, defaultValue = "10")int pageSize) {
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            List<CommonQuestionDto> list = commonQuestionService.getCommonQuestion(type, name, page, pageSize);
            Map<String,Object> retMap=new HashMap<String,Object>();
            retMap.put("list", list);
            retMap.put("count", commonQuestionService.getCommonQuestionCountByType(type, name));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(retMap);
        } catch (Exception e) {
            // TODO: handle exception
            respObj.setErrorMessage(e.getMessage());
        }
        
        return respObj;
    }
}
