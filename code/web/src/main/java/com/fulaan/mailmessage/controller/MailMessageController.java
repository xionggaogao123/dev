package com.fulaan.mailmessage.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.base.BaseController;
import com.fulaan.mailmessage.service.MailMessageService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by James on 2017/12/4.
 */
@Api(value="邮件")
@Controller
@RequestMapping("/mailMessage")
public class MailMessageController extends BaseController {
    @Autowired
    private MailMessageService mailMessageService;

    /**
     * 联系我们
     * @return
     */
    @ApiOperation(value = "联系我们", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/addMailMessage")
    @ResponseBody
    public String addMailMessage(@ApiParam(name = "message", required = true, value = "内容") @RequestParam(value = "message") String message){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            mailMessageService.sendMailMessage(getUserId(), message);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("发送成功！");
        }catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("联系我们失败");
        }
        return JSON.toJSONString(respObj);
    }
}
