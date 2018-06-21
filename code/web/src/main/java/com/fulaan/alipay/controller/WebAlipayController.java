package com.fulaan.alipay.controller;

import com.alibaba.fastjson.JSON;
import com.alipay.api.internal.util.AlipaySignature;
import com.fulaan.alipay.config.AlipayConfig;
import com.fulaan.alipay.config.AlipayNewConfig;
import com.fulaan.alipay.service.AppAlipayService;
import com.fulaan.alipay.util.AlipayCore;
import com.fulaan.alipay.util.AlipaySubmit;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.BaseController;
import com.sys.constants.Constant;
import com.sys.props.Resources;
import com.sys.utils.RespObj;
import io.swagger.annotations.*;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by James on 2018-05-30.
 * 支付宝接入
 *
 */
@Api("支付宝")
@Controller
@RequestMapping("/web/appalipay")
public class WebAlipayController extends BaseController {
    @Autowired
    private AppAlipayService appAlipayService;

    private static final Logger EBusinessLog = Logger.getLogger("AppAlipayController");
    
    
    private static String payment_type = "1";

    private String notify_url = Resources.getProperty("fulaan.domain", Resources.getProperty("domain")) + "/web/appalipay/notify.do";

    private String return_url = Resources.getProperty("fulaan.domain", Resources.getProperty("domain")) + "/#/balanceNext";

    private String seller_email = "zhifu@fulaan.com";

    private String show_url = "http://www.fulaan.com/mall/index.do";

    private String anti_phishing_key = "";

    private String exter_invoke_ip = "";


    /**
     * 账户充值(用户自定义充值)
     *//*
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
            Map<String,Object> map = appAlipayService.appPay(price, getUserId(), ipconfig);
            respObj.setMessage(map);
        }catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }*/
    
    /**
     * 账户充值(用户自定义充值)
     */
    @ApiOperation(value = "账户充值", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/appPay")
    @ResponseBody
    public String appPay(@ApiParam(name="price",required = false,value="充值金额") @RequestParam(value="price",defaultValue = "0") int price,
                          HttpServletRequest request){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            respObj.setCode(Constant.SUCCESS_CODE);
            Map<String, String> sParaTemp = new HashMap<String, String>();
            sParaTemp.put("service", "create_direct_pay_by_user");
            sParaTemp.put("partner", AlipayConfig.partner);
            sParaTemp.put("_input_charset", AlipayConfig.input_charset);
            sParaTemp.put("payment_type", payment_type);
            sParaTemp.put("notify_url", notify_url);
            sParaTemp.put("return_url", return_url+"/"+String.valueOf(Double.valueOf(price))+"/1");
            sParaTemp.put("seller_email", seller_email);
            sParaTemp.put("out_trade_no", new ObjectId().toString());
            sParaTemp.put("subject", "账户充值");
            sParaTemp.put("total_fee", String.valueOf(Double.valueOf(price)));
            sParaTemp.put("body", "");
            sParaTemp.put("show_url", show_url);
            sParaTemp.put("anti_phishing_key", anti_phishing_key);
            sParaTemp.put("exter_invoke_ip", exter_invoke_ip);
            Map<String, String> sPara = AlipayCore.paraFilter(sParaTemp);
            String sign = AlipaySubmit.buildRequestMysign(sPara, "MD5");
            EBusinessLog.info("OrderCreateSign:" + sign);
            // 建立请求
            String sHtmlText = AlipaySubmit.buildRequest(sParaTemp, "get", "确认");
            respObj.setMessage(sHtmlText);
            return sHtmlText;
        }catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
            return "";
        }
        
    }

    /**
     * 支付宝购买课节(家长)
     */
    @ApiOperation(value = "支付宝购买课节（家长）", httpMethod = "GET", produces = "application/json")
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
            String result = appAlipayService.buyAlipayChildClassList(new ObjectId(id), getUserId(), classIds, new ObjectId(sonId), ipconfig);
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
        String code = "";
        EBusinessLog.info("支付宝回调;" + request.getParameterMap());
        EBusinessLog.error("支付宝回调:"+request.getParameterMap());
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        StringBuffer str = new StringBuffer();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            EBusinessLog.info("-------name-----" + name + "----values" + values);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
            EBusinessLog.info("name-----" + name + "----value" + valueStr);
            str.append("   name-----" + name + "----value" + valueStr);
        }

        EBusinessLog.info("OrderNotify;" + params);
        EBusinessLog.error("支付宝回调:"+params);
        try {

        String trade_no = request.getParameter("trade_no");                //支付宝交易号
        String out_trade_no = request.getParameter("out_trade_no");            //获取订单号
        String total_fee_string = request.getParameter("total_amount");            //获取总金额
        String trade_status = request.getParameter("trade_status");   //订单状态
        String user_id = request.getParameter("buyer_logon_id");    //支付人支付宝
        String notify_time = request.getParameter("notify_time");    //支付人支付宝

        EBusinessLog.info("trade_no;" + trade_no);
        EBusinessLog.info("out_trade_no;" + out_trade_no);
        EBusinessLog.info("total_fee_string;" + total_fee_string);
        EBusinessLog.info("trade_status;" + trade_status);


            if (verify(params)) {
                EBusinessLog.error("支付宝回调验证通过");
                if (trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")) {
                    EBusinessLog.info("TRADE_SUCCESS");
                    int price = (int)Double.parseDouble(total_fee_string);
                    //修改订单状态
                    //appAlipayService.payed(out_trade_no,trade_no,user_id,price,str.toString(),notify_time);
                    String ipconfig = "";
                    if (request.getHeader("x-forwarded-for") == null) {
                        ipconfig =  request.getRemoteAddr();
                    }else{
                        ipconfig =  request.getHeader("x-forwarded-for");
                    }
                    Map<String,Object> map = appAlipayService.appPay(price, getUserId(), ipconfig);
                    code = "success";
                }
            } else {
                EBusinessLog.error("支付宝验证失败");
            }
        } catch (Exception ex) {
            EBusinessLog.error("支付宝报错", ex);
        }
        return code;
    }

    public static boolean verify(Map<String, String> params){
        boolean flag = false;
        try {
            flag = AlipaySignature.rsaCheckV1(params, AlipayNewConfig.RSA_PUBLIC, AlipayNewConfig.CHARSET, "RSA2");
        }catch (Exception e){
            EBusinessLog.error("支付宝报错", e);
            EBusinessLog.error("支付宝验证失败");
        }
        return flag;
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
        String code = "";
        EBusinessLog.info("支付宝回调;" + request.getParameterMap());
        EBusinessLog.error("支付宝回调:"+request.getParameterMap());
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        StringBuffer str = new StringBuffer();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            EBusinessLog.info("-------name-----" + name + "----values" + values);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
            EBusinessLog.info("name-----" + name + "----value" + valueStr);
            str.append("   name-----" + name + "----value" + valueStr);
        }

        EBusinessLog.info("OrderNotify;" + params);
        EBusinessLog.error("支付宝回调:"+params);
        String trade_no = request.getParameter("trade_no");                //支付宝交易号
        String out_trade_no = request.getParameter("out_trade_no");            //获取订单号
        String total_fee_string = request.getParameter("total_amount");            //获取总金额
        String trade_status = request.getParameter("trade_status");   //订单状态
        String user_id = request.getParameter("buyer_logon_id");    //支付人支付宝
        String notify_time = request.getParameter("notify_time");    //支付人支付宝

        EBusinessLog.info("trade_no;" + trade_no);
        EBusinessLog.info("out_trade_no;" + out_trade_no);
        EBusinessLog.info("total_fee_string;" + total_fee_string);
        EBusinessLog.info("trade_status;" + trade_status);

        try {
            if (verify(params)) {
                EBusinessLog.info("支付宝回调验证通过");
                if (trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")) {
                    EBusinessLog.info("TRADE_SUCCESS");
                    int price = (int)Double.parseDouble(total_fee_string);
                    //修改订单状态
                    appAlipayService.nowPayed(out_trade_no,trade_no,user_id,price,str.toString(),notify_time);
                    code = "success";
                }
            } else {
                EBusinessLog.info("支付宝验证失败");
            }
        } catch (Exception ex) {
            EBusinessLog.error("支付宝报错", ex);
        }
        return code;
    }

    /**
     *  申请提现
     */
    @ApiOperation(value = "申请提现", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/goMyMoney")
    @ResponseBody
    public String goMyMoney(@ApiParam(name="price",required = false,value="充值金额") @RequestParam(value="price",defaultValue = "0") int price){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            appAlipayService.goMyMoney(getUserId(), price);
            respObj.setMessage("申请成功！");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return JSON.toJSONString(respObj);
    }

    /**
     *  用户充值账户余额
     */
    @ApiOperation(value = "用户充值账户余额", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getMyMoney")
    @ResponseBody
    public RespObj getMyMoney(){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            double d = appAlipayService.getAccount(getUserId());
            respObj.setMessage(d);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }
    

}
