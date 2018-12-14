package com.fulaan.instantmessage.controller;

import cn.jiguang.commom.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.fulaan.base.BaseController;
import com.fulaan.instantmessage.dto.RedResultDTO;
import com.fulaan.instantmessage.service.RedDotService;
import com.sys.constants.Constant;
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
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            RedResultDTO result = redDotService.selectAllResultDTO(getUserId());
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获得该用户的红点记录失败!");

        }
        return JSON.toJSONString(respObj);

    }

    /**
     * 首页清除红点
     * @return
     */
    @ApiOperation(value="首页清除红点",httpMethod = "POST",produces = "application/json")
    @ApiResponse(code=200,message = "success", response = String.class)
    @RequestMapping("/cleanIndexResult")
    @ResponseBody
    public String cleanIndexResult(){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            redDotService.cleanIndexResult(getUserId());
            respObj.setMessage("清除红点成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("清除红点成功!");

        }
        return JSON.toJSONString(respObj);

    }


    /**
     * 首页获得该用户每日通知红点记录
     * @return
     */
    @ApiOperation(value="首页获得该用户每日通知红点记录",httpMethod = "POST",produces = "application/json")
    @ApiResponse(code=200,message = "success", response = String.class)
    @RequestMapping("/getDayNotice")
    @ResponseBody
    public String getDayNotice(){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> result = redDotService.selectDayNotice(getUserId());
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("首页获得该用户每日通知红点记录失败!");

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
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String,Object> result = redDotService.selectResult(getUserId());
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
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
    public String cleanResult(
            @ApiParam(name = "type", required = true, value = "应用类型") @RequestParam(required = false,defaultValue = "1") int type,
            @ApiParam(name = "dataTime", required = true, value = "日期（2017-10-26）") @RequestParam(required = false,defaultValue = "") String dataTime){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            if(StringUtils.isNotEmpty(dataTime)) {
                long time = DateTimeUtils.getStrToLongTime(dataTime, "yyyy-MM-dd");
                redDotService.cleanResult(getUserId(), type, time);
            }else{
                redDotService.cleanResult(getUserId(), type, 0L);
            }
            respObj.setMessage("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获得该用户的红点记录失败!");
        }
        return JSON.toJSONString(respObj);

    }
}
