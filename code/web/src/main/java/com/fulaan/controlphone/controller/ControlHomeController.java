package com.fulaan.controlphone.controller;

import com.fulaan.base.BaseController;
import com.fulaan.controlphone.service.ControlHomeService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import io.swagger.annotations.*;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by James on 2018-11-16.
 */
@Api(value = "新家管控")
@Controller
@RequestMapping("/jxmapi/controlhome")
public class ControlHomeController  extends BaseController{
    @Autowired
    private ControlHomeService controlHomeService;

    private static final Logger logger = Logger.getLogger(ControlHomeController.class);


    @ApiOperation(value = "设置家管控记录", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/addHomeEntry")
    @ResponseBody
    public RespObj addHomeEntry(@ApiParam(value="孩子id",required = true, name = "sonId")@RequestParam("sonId") String sonId,
                                @ApiParam(value="周常开始时间",required = true, name = "dayStartTime")@RequestParam("dayStartTime") String dayStartTime,
                                @ApiParam(value="周常结束时间",required = true, name = "dayEndTime")@RequestParam("dayEndTime") String dayEndTime,
                                @ApiParam(value="周常防沉迷",required = true, name = "dayHour")@RequestParam("dayHour") int dayHour,
                                @ApiParam(value="周末开始时间",required = true, name = "weekStartTime")@RequestParam("weekStartTime") String weekStartTime,
                                @ApiParam(value="周末结束时间",required = true, name = "weekEndTime")@RequestParam("weekEndTime") String weekEndTime,
                                @ApiParam(value="周末防沉迷",required = true, name = "weekHour")@RequestParam("weekHour") int weekHour){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            controlHomeService.addHomeEntry(getUserId(),new ObjectId(sonId),dayStartTime,dayEndTime,dayHour,weekStartTime,weekEndTime,weekHour);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("设置家管控时间成功");
        }catch(Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("设置家管控时间失败");
            logger.error("error",e);
        }
       return respObj;
    }


    @ApiOperation(value = "设置家管控记录", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/getHomeEntryList")
    @ResponseBody
    public RespObj getHomeEntryList(@ApiParam(value="孩子id",required = true, name = "sonId")@RequestParam("sonId") String sonId){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            Map<String,Object> map = controlHomeService.getHomeEntryList(getUserId(),new ObjectId(sonId));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(map);
        }catch(Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("设置家管控时间失败");
            logger.error("error",e);
        }
        return respObj;
    }


    @ApiOperation(value = "启动监听", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/startMqtt")
    @ResponseBody
    public RespObj startMqtt(){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
           controlHomeService.startMqtt();
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("启动成功");
        }catch(Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
            logger.error("error",e);
        }
        return respObj;
    }
}
