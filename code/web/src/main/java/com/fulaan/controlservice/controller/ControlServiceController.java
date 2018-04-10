package com.fulaan.controlservice.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.BaseController;
import com.fulaan.controlservice.service.ControlTokenService;
import com.fulaan.log.service.LogService;
import com.fulaan.school.SchoolService;
import com.fulaan.user.service.UserService;
import com.pojo.app.SessionValue;
import com.pojo.log.LogType;
import com.pojo.user.UserEntry;
import com.pojo.utils.LoginLog;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  国家数字教育资源公共服务体系接入
 * Created by James on 2018-04-08.
 */
@Api(value="国家数字教育资源公共服务体系接入")
@Controller
@RequestMapping("/web/ticketAccessToken")
public class ControlServiceController extends BaseController {

    private static final Logger logger = Logger.getLogger(ControlServiceController.class);

    @Autowired
    private LogService logService;

    private static final Logger loginLogger = Logger.getLogger("LOGIN");

    @Autowired
    private ControlTokenService controlTokenService;
    @Autowired
    private UserService userService;
    @Autowired
    private SchoolService schoolService;

    /**
     * 获取接口访问令牌
     */
    @ApiOperation(value = "获取接口访问令牌", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/getAccessToken")
    @ResponseBody
    public String getAccessToken(@RequestParam("sysCode")String sysCode){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            String dto = controlTokenService.getAccessToken(sysCode);
            respObj.setMessage(dto);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获取接口访问令牌失败!");
        }
        return JSON.toJSONString(respObj);
    }



    /**
     * 获取用户登陆会话 usessionId
     */
    @ApiOperation(value = "获取用户登陆会话 usessionId", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/getUsessionId")
    @ResponseBody
    public String getUsessionId(@RequestParam("sysCode")String sysCode){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            String dto = controlTokenService.getUsessionId(sysCode);
            respObj.setMessage(dto);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获取用户登陆会话 usessionId失败");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 使用体系会话创建 Ticket
     */
    @ApiOperation(value = "使用体系会话创建 Ticket", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/getTicket")
    @ResponseBody
    public String getTicket(@RequestParam("sysCode")String sysCode){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            String dto = controlTokenService.getTicket(sysCode);
            respObj.setMessage(dto);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("使用体系会话创建 Ticket失败");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 接受会话 Ticket 传递
     */
    @SessionNeedless
    @ApiOperation(value = "接受会话 Ticket 传递", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/receiveTicket")
    @ResponseBody
    public String receiveTicket(HttpServletRequest request, HttpServletResponse response,@RequestParam("ticket") String ticket,@RequestParam("sysCode") String sysCode){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            final UserEntry userEntry = controlTokenService.getUserInfo(ticket, sysCode);
            if(userEntry!=null){
                SessionValue value = getSessionValue(userEntry);
                userService.setCookieValue(userEntry, value, getIP(), response, request);
                //成功返回
                respObj.setMessage("300");
            }else{
                //失败返回
                respObj.setMessage("500");
            }
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("接受会话 Ticket 传递失败");
        }
        return JSON.toJSONString(respObj);
    }

    private SessionValue getSessionValue(UserEntry e) {
        //处理SessionValue
        SessionValue value = new SessionValue();
        value.setId(e.getID().toString());
        value.setUserName(e.getUserName());
        value.setRealName(e.getNickName());
        value.setAvatar(e.getAvatar());
        value.setK6kt(e.getK6KT());
        value.setUserRole(e.getRole());
        value.setPackageCode(e.getGenerateUserCode());
        try {
            //获取客户端信息
            LoginLog loginLog = new LoginLog();
            loginLog.setIpAddr(getIP() + e.getUserName());
            loginLog.setPlatform(getPlatform().getName());
            loginLog.setUserId(e.getID().toString());
            loginLog.setUserName(e.getUserName());
            loginLogger.info(loginLog);
            logService.insertLog(e, getPlatform(), LogType.CLICK_LOGIN, "login.do");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return value;
    }

}
