package com.fulaan.wrongquestion.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.BaseController;
import com.fulaan.wrongquestion.dto.ResourcesDictionaryDTO;
import com.fulaan.wrongquestion.service.ResourceDictionaryService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by guojing on 2017/3/13.
 */
@RestController
@RequestMapping("/resDict")
public class ResourceDictionaryController extends BaseController {

    private static final Logger logger =Logger.getLogger(ResourceDictionaryController.class);

    @Autowired
    private ResourceDictionaryService resourceDictionaryService;

    /**
     * 查询练习题目知识点
     * @return
     */
    @RequestMapping("/getKnowledgePointList")
    @ResponseBody
    public String getKnowledgePointList(
            @RequestParam("type") String type,
            @RequestParam("subjectName") String subjectName){
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
    @SessionNeedless
    @RequestMapping(value = "/getChapterPointList/{type}/{gradeName}/{subjectName}",method= RequestMethod.GET)
    public String getChapterPointList(
            @PathVariable String type,
            @PathVariable String gradeName,
            @PathVariable String subjectName){
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
