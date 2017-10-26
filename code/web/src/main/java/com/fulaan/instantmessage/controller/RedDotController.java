package com.fulaan.instantmessage.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.base.BaseController;
import com.fulaan.instantmessage.service.RedDotService;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.RespObj;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by James on 2017/10/25.
 */
@Api(value = "红点记录")
@Controller
@RequestMapping("/jxmapi/reddot")
public class RedDotController extends BaseController {
    @Autowired
    private RedDotService redDotService;

    /**
     * 首页获得该用户的红点记录
     * @return
     */
    @ApiOperation(value="首页获得该用户的红点记录",httpMethod = "POST",produces = "application/json")
    @ApiResponse(code=200,message = "success", response = String.class)
    @RequestMapping("/selectResult")
    @ResponseBody
    public String selectResult(){
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            Map<String,Object> result = redDotService.selectResult(getUserId());
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("获得该用户的红点记录失败!");

        }
        return JSON.toJSONString(respObj);

    }


    /**
     * 学生端获得该用户的红点记录
     * @return
     */
    @ApiOperation(value="学生端获得该用户的红点记录",httpMethod = "POST",produces = "application/json")
    @ApiResponse(code=200,message = "success", response = String.class)
    @RequestMapping("/selectStudentResult")
    @ResponseBody
    public String selectStudentResult(){
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            Map<String,Object> result = redDotService.selectResult(getUserId());
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("获得该用户的红点记录失败!");

        }
        return JSON.toJSONString(respObj);

    }

    /**
     * 清除指点日期的记录
     * @return
     */
    @ApiOperation(value="清除指点日期的记录",httpMethod = "POST",produces = "application/json")
    @ApiResponse(code=200,message = "success", response = String.class)
    @RequestMapping("/cleanResult")
    @ResponseBody
    public String cleanResult(@ApiParam(name = "type", required = true, value = "应用类型") @RequestParam("type") int type,
                              @ApiParam(name = "dataTime", required = true, value = "日期（2017-10-26）") @RequestParam("dataTime") String dataTime){
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            long time = DateTimeUtils.getStrToLongTime(dataTime, "yyyy-MM-dd");
            redDotService.cleanResult(getUserId(),type,time);
            respObj.setMessage("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setErrorMessage("获得该用户的红点记录失败!");
        }
        return JSON.toJSONString(respObj);

    }
}
