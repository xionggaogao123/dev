package com.fulaan.wrongquestion.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.BaseController;
import com.fulaan.wrongquestion.dto.ResourcesDictionaryDTO;
import com.fulaan.wrongquestion.service.ResourceDictionaryService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import io.swagger.annotations.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by guojing on 2017/3/13.
 */
@Api(value = "知识点")
@Controller
@RequestMapping("/jxmapi/resDict")
public class DefaultResourceDictionaryController extends BaseController {

    private static final Logger logger =Logger.getLogger(DefaultResourceDictionaryController.class);

    @Autowired
    private ResourceDictionaryService resourceDictionaryService;

    /**
     * 查询练习题目知识点
     * @return
     */
    @ApiOperation(value = "查询练习题目知识点", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getKnowledgePointList")
    @ResponseBody
    public String getKnowledgePointList(
            @ApiParam(name = "type", required = true, value = "知识点年级英文标识") @RequestParam("type") String type,
            @ApiParam(name = "subjectName", required = true, value = "学科名（英语）") @RequestParam("subjectName") String subjectName){
        RespObj respObj=new RespObj(Constant.SUCCESS_CODE);
        try {
            //群组种类list
            List<ResourcesDictionaryDTO> list=resourceDictionaryService.getKnowledgePointList(type, subjectName);
            respObj.setMessage(list);
        }catch (Exception e){
            logger.error("查询练习题目类型报错：", e);
            respObj = RespObj.FAILD;
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 查询练习题目章节点
     * @return
     */
    @ApiOperation(value = "查询练习题目章节点", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @SessionNeedless
    @RequestMapping(value = "/getChapterPointList/{type}/{gradeName}/{subjectName}",method= RequestMethod.GET)
    public String getChapterPointList(
            @ApiParam(name = "type", required = true, value = "知识点年级英文标识") @PathVariable String type,
            @ApiParam(name = "gradeName", required = true, value = "年级名（八年级）") @PathVariable String gradeName,
            @ApiParam(name = "subjectName", required = true, value = "学科名") @PathVariable String subjectName){
        RespObj respObj=new RespObj(Constant.SUCCESS_CODE);
        try {
            //群组种类list
            List<ResourcesDictionaryDTO> list=resourceDictionaryService.getChapterPointList(type, gradeName, subjectName);
            respObj.setMessage(list);
        }catch (Exception e){
            logger.error("查询练习题目类型报错：", e);
            respObj = RespObj.FAILD;
        }
        return JSON.toJSONString(respObj);
    }






}
