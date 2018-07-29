package com.fulaan.txpay.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fulaan.txpay.service.WxpayService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * <简述>
 * <详细描述>
 * @author   Brant
 * @version  $Id$
 * @since
 * @see
 */
@Api("微信")
@Controller
@RequestMapping("/web/appwxpay")
public class WxpayController {

    @Autowired
    private WxpayService wxpayService;
    
    private static final Logger EBusinessLog = Logger.getLogger("WxpayController");
    
    /**
     * 微信购买课程批量更新
     */
    @ApiOperation(value = "微信购买课程批量更新", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/updateStat")
    @ResponseBody
    public RespObj updateStat(@ApiParam(name="orderId",required = false,value="订单id") @RequestParam(value="orderId",defaultValue = "0") String orderId,
                          HttpServletRequest request){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            respObj.setCode(Constant.SUCCESS_CODE);
           
            String s = wxpayService.updateClassOrderByOrderId(orderId);
            respObj.setMessage(s);
        }catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }
}
