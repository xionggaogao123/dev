package com.fulaan.alipay.controller;

import com.fulaan.alipay.service.AppAlipayService;
import com.fulaan.base.BaseController;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by James on 2018-05-30.
 * 支付宝接入
 *
 */
@Api("支付宝")
@Controller
@RequestMapping("/jxmapi/appalipay")
public class AppAlipayController extends BaseController {
    @Autowired
    private AppAlipayService appAlipayService;

    /**
     * 账户充值(用户自定义充值)
     */
    @ApiOperation(value = "账户充值", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/appPay")
    @ResponseBody
    public RespObj appPay(@ApiParam(name="price",required = false,value="充值金额") @RequestParam(value="price",defaultValue = "") int price){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        respObj.setMessage("账户充值失败");
        //AlipayClient
        respObj.setCode(Constant.SUCCESS_CODE);
        respObj.setMessage("账户充值成功");
        return respObj;
    }

}
