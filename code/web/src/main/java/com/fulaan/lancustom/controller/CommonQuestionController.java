package com.fulaan.lancustom.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.integralmall.dto.GoodsDto;
import com.fulaan.lancustom.dto.CommonQuestionDto;
import com.fulaan.lancustom.service.CommonQuestionService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * <简述>小兰客服常见问题
 * <详细描述>
 * @author   Brant
 * @version  $Id$
 * @since
 * @see
 */
@Api(value="小兰客服")
@Controller
@RequestMapping("/web/commonQuestion")
public class CommonQuestionController {
    
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
    
    
    @ApiOperation(value = "保存问题", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "保存问题",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/saveQuestion")
    @ResponseBody
    public RespObj saveQuestion(@RequestBody CommonQuestionDto cDto){
        
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            commonQuestionService.saveQuestion(cDto);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("成功！");
        } catch (Exception e) {
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 新版常见问题
     * @param cDto
     * @return
     */
    @ApiOperation(value = "保存问题类型", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "保存问题",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/saveQuestionType")
    @ResponseBody
    public RespObj saveQuestionType(@RequestBody CommonQuestionDto cDto){

        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            commonQuestionService.saveQuestionType(cDto);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("成功！");
        } catch (Exception e) {
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }
    
    @ApiOperation(value = "删除问题", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "删除问题",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/delGoods")
    @ResponseBody
    public RespObj delGoods(@ObjectIdType ObjectId goodId){
        
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            commonQuestionService.updateIsr(goodId);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("成功！");
        } catch (Exception e) {
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }
}
