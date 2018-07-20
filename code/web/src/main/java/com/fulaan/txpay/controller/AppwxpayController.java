package com.fulaan.txpay.controller;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alipay.api.internal.util.AlipaySignature;
import com.fulaan.alipay.config.AlipayNewConfig;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.BaseController;
import com.fulaan.txpay.Utils.DateUtil;
import com.fulaan.txpay.Utils.PayCommonUtil;
import com.fulaan.txpay.Utils.StringUtil;
import com.fulaan.txpay.entity.UnifiedorderResult;
import com.fulaan.txpay.service.WxpayService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


/**
 * Created by James on 2018-05-30.
 * 微信接入
 *
 */
@Api("微信")
@Controller
@RequestMapping("/jxmapi/appwxpay")
public class AppwxpayController extends BaseController {

    @Autowired
    private WxpayService wxpayService;
    
    private static final Logger EBusinessLog = Logger.getLogger("AppwxpayController");
    
    /**
     * 账户充值(用户自定义充值)
     */
    @ApiOperation(value = "账户充值", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/appPay")
    @ResponseBody
    public RespObj appPay(@ApiParam(name="price",required = false,value="充值金额") @RequestParam(value="price",defaultValue = "0") int price,
                          HttpServletRequest request){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            respObj.setCode(Constant.SUCCESS_CODE);
            String ipconfig = "";
            if (request.getHeader("x-forwarded-for") == null) {
                ipconfig =  request.getRemoteAddr();
            }else{
                ipconfig =  request.getHeader("x-forwarded-for");
            }
            Map<String,Object> map = wxpayService.appPay(price, getUserId(), ipconfig);
            respObj.setMessage(map);
        }catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }
    
    /**
     * 支付宝购买课节(家长)
     */
    @ApiOperation(value = "微信购买课节（家长）", httpMethod = "GET", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/buyAlipayChildClassList")
    @ResponseBody
    public String buyAlipayChildClassList(@ApiParam(name = "id", required = true, value = "id") @RequestParam("id") String id,
                                          @ApiParam(name = "sonId", required = true, value = "sonId") @RequestParam("sonId") String sonId,
                                          @ApiParam(name = "classIds", required = true, value = "classIds") @RequestParam("classIds") String classIds,
                                          HttpServletRequest request){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            String ipconfig = "";
            if (request.getHeader("x-forwarded-for") == null) {
                ipconfig =  request.getRemoteAddr();
            }else{
                ipconfig =  request.getHeader("x-forwarded-for");
            }
            UnifiedorderResult result = wxpayService.buyAlipayChildClassList(new ObjectId(id), getUserId(), classIds, new ObjectId(sonId), ipconfig);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return JSON.toJSONString(respObj);
    }
    
    
    /**
     * 支付回调
     *
     * @param request
     * @throws NumberFormatException
     * @throws Exception
     */
    @ApiOperation(value = "支付回调", httpMethod = "POST", produces = "application/json")
    @SessionNeedless
    @RequestMapping("/notify")
    public String callBackNotify(HttpServletRequest request) throws Exception {
       
        System.out.println("微信支付回调");
        InputStream inStream = request.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        String resultxml = new String(outSteam.toByteArray(), "utf-8");
        Map<String, String> params = PayCommonUtil.doXMLParse(resultxml);
        outSteam.close();
        inStream.close();
        
        
        Map<String,String> return_data = new HashMap<String,String>();  
        if (!PayCommonUtil.isTenpaySign(params)) {
            // 支付失败
            return_data.put("return_code", "FAIL");  
            return_data.put("return_msg", "return_code不正确");
            return StringUtil.GetMapToXML(return_data);
        } else {
            System.out.println("===============付款成功==============");
            // ------------------------------
            // 处理业务开始
            // ------------------------------
            // 此处处理订单状态，结合自己的订单数据完成订单状态的更新
            // ------------------------------
 
            
            
            String total_fee = params.get("total_fee");
            double v = Double.valueOf(total_fee) / 100;
            String out_trade_no = String.valueOf(Long.parseLong(params.get("out_trade_no").split("O")[0]));
            String notify_time = params.get("time_end");
           // Date accountTime = DateUtil.stringtoDate(params.get("time_end"), "yyyyMMddHHmmss");
            String ordertime = DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
            //String totalAmount = String.valueOf(v);
            //String appId = params.get("appid");
            String tradeNo = params.get("transaction_id");
            String resultCode= params.get("result_code");
            String openid = params.get("openid");
            EBusinessLog.error("微信回调验证通过");
            if (resultCode.equals("SUCCESS")) {
                EBusinessLog.info("TRADE_SUCCESS");
                //double price = Double.parseDouble(total_fee_string);
                //修改订单状态
                wxpayService.payed(out_trade_no,tradeNo,openid,v,"",notify_time);
 
            }
        
            return_data.put("return_code", "SUCCESS");  
            return_data.put("return_msg", "OK");  
            return StringUtil.GetMapToXML(return_data);
        }

    }

    /**
     * 直接购买支付回调
     *
     * @param request
     * @throws NumberFormatException
     * @throws Exception
     */
    @ApiOperation(value = "支付回调", httpMethod = "POST", produces = "application/json")
    @SessionNeedless
    @RequestMapping("/newNotify")
    public String callBackNewNotify(HttpServletRequest request) throws Exception {
      
        System.out.println("微信支付回调");
        InputStream inStream = request.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        String resultxml = new String(outSteam.toByteArray(), "utf-8");
        Map<String, String> params = PayCommonUtil.doXMLParse(resultxml);
        outSteam.close();
        inStream.close();
        
        
        Map<String,String> return_data = new HashMap<String,String>();  
        if (!PayCommonUtil.isTenpaySign(params)) {
            // 支付失败
            return_data.put("return_code", "FAIL");  
            return_data.put("return_msg", "return_code不正确");
            return StringUtil.GetMapToXML(return_data);
        } else {
            System.out.println("===============付款成功==============");
            // ------------------------------
            // 处理业务开始
            // ------------------------------
            // 此处处理订单状态，结合自己的订单数据完成订单状态的更新
            // ------------------------------
 
            
            
            String total_fee = params.get("total_fee");
            double v = Double.valueOf(total_fee) / 100;
            String out_trade_no = String.valueOf(Long.parseLong(params.get("out_trade_no").split("O")[0]));
            String notify_time = params.get("time_end");
           // Date accountTime = DateUtil.stringtoDate(params.get("time_end"), "yyyyMMddHHmmss");
            String ordertime = DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
            //String totalAmount = String.valueOf(v);
            //String appId = params.get("appid");
            String tradeNo = params.get("transaction_id");
            String resultCode= params.get("result_code");
            String openid = params.get("openid");
            EBusinessLog.error("微信回调验证通过");
            if (resultCode.equals("SUCCESS")) {
                EBusinessLog.info("TRADE_SUCCESS");
                //double price = Double.parseDouble(total_fee_string);
                //修改订单状态
                wxpayService.nowPayed(out_trade_no,tradeNo,openid,v,"",notify_time);
 
            }
        
            return_data.put("return_code", "SUCCESS");  
            return_data.put("return_msg", "OK");  
            return StringUtil.GetMapToXML(return_data);
        }
    }
    
}
