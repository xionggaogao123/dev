package com.fulaan.lancustom.controller;

import com.alibaba.fastjson.JSON;
import com.alipay.api.internal.util.AlipaySignature;
import com.fulaan.alipay.config.AlipayNewConfig;
import com.fulaan.alipay.service.AppAlipayService;
import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.BaseController;
import com.fulaan.integralmall.dto.WuliuInfoDto;
import com.fulaan.lancustom.dto.MonetaryOrdersDto;
import com.fulaan.lancustom.service.MonetaryOrdersService;
import com.fulaan.mall.service.EBusinessOrderService;
import com.fulaan.txpay.Utils.DateUtil;
import com.fulaan.txpay.Utils.PayCommonUtil;
import com.fulaan.txpay.Utils.StringUtil;
import com.fulaan.txpay.service.WxpayService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;

/**
 * @Auther: taotao.chan
 * @Date: 2018/8/7 15:41
 * @Description: 小兰订单
 */
@Controller
@RequestMapping(value="/jxmapi/orders")
@Api(value = "小兰商城")
public class DefaultOrdersController extends BaseController {

    private static final Logger EBusinessLog = Logger.getLogger("AppAlipayService");

    @Autowired
    private MonetaryOrdersService service;

    @Autowired
    private WxpayService wxpayService;

    @Autowired
    private AppAlipayService appAlipayService;

    /**
     * 跳转到购买页面，查看订单和地址详情
     * @param goodId
     * @return
     */
    @ApiOperation(value = "确认订单", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "确认订单",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/orderConfirm")
    @ResponseBody
    public RespObj orderConfirm(@ObjectIdType ObjectId goodId){

        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            MonetaryOrdersDto dto = service.orderConfirm(goodId, getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(dto);
        } catch (Exception e) {
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 生成订单
     * param goodNum
     * param goodId
     * param addressId
     * param money
     * param style
     * @return
     */
    @ApiOperation(value = "生成订单", httpMethod = "POST", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "生成订单", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/saveOrder")
    @ResponseBody
    public String saveOrder(@RequestBody MonetaryOrdersDto ordersDto/*Integer goodNum, @ObjectIdType ObjectId goodId, @ObjectIdType ObjectId addressId, Integer costScore*/) {

        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
//            map.put("userId",getUserId().toString());
            ordersDto.setUserId(getUserId().toString());
            String  message= service.saveOrder(ordersDto);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(message);
        } catch (Exception e) {
            respObj.setErrorMessage(e.getMessage());
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 购买成功订单
     * @param page
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "个人订单列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "个人订单列表", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getMonetaryPersonalOrderList")
    @ResponseBody
    public RespObj getMonetaryOrderList(@RequestParam(required = false, defaultValue = "1") int page,
                                        @RequestParam(required = false, defaultValue = "10") int pageSize) {
        RespObj obj = new RespObj(Constant.FAILD_CODE);
        try {
            Map<String, Object> pMap = new HashMap<String, Object>();
            pMap.put("userId",getUserId().toString());
            pMap.put("page",page);
            pMap.put("pageSize",pageSize);
            List<MonetaryOrdersDto> list = service.getMonetaryPersonalOrderList(pMap);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("list", list);
            int count = service.getMonetaryPersonalOrderListCount(pMap);
            map.put("count", count);
            obj.setMessage(map);
            obj.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            obj.setErrorMessage(e.getMessage());
        }
        return obj;
    }

    @ApiOperation(value = "个人订单详情", httpMethod = "GET", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "个人订单详情", response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getMonetaryPersonalOrderDetail")
    @ResponseBody
    public RespObj getMonetaryPersonalOrderDetail(@RequestParam String orderId) {
        RespObj obj = new RespObj(Constant.FAILD_CODE);
        try {
            Map<String, Object> pMap = new HashMap<String, Object>();
            pMap.put("userId",getUserId().toString());
            pMap.put("orderId",orderId);
            MonetaryOrdersDto ordersDto = service.getMonetaryPersonalOrderDetail(pMap);
            obj.setMessage(ordersDto);
            obj.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            obj.setErrorMessage(e.getMessage());
        }
        return obj;
    }

    @ApiOperation(value = "物流信息", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "物流信息",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/wuLiuInfo")
    @ResponseBody
    public RespObj wuLiuInfo(@ObjectIdType ObjectId orderId){

        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            WuliuInfoDto wuliuInfoDto = service.wuLiuInfo(orderId, getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(wuliuInfoDto);
        } catch (Exception e) {
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     *
     * 小兰客服微信支付
     * param orderId
     */
    @ApiOperation(value = "微信支付", httpMethod = "GET", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful — 请求已完成", response = String.class)})
    @RequestMapping("/weChatPayBuyGoods")
    @ResponseBody
    public RespObj weChatPayBuyGoods(@RequestParam String orderId,
                                     HttpServletRequest request) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            String ipconfig = "";
            if (request.getHeader("x-forwarded-for") == null) {
                ipconfig = request.getRemoteAddr();
            } else {
                ipconfig = request.getHeader("x-forwarded-for");
            }
            Map<String, Object> map = wxpayService.weChatPayBuyGoods(new ObjectId(orderId), getUserId(), ipconfig);
            respObj.setMessage(map);
        } catch (Exception e) {
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * WeChat支付回调
     *
     * @param request
     * @throws NumberFormatException
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @ApiOperation(value = "支付回调", httpMethod = "POST", produces = "application/json")
    @SessionNeedless
    @RequestMapping("/weChatNotify")
    public String weChatNotify(HttpServletRequest request) throws Exception {
        String msg = "";
        InputStream inStream = null;
        ByteArrayOutputStream outSteam = null;
        try {
            inStream = request.getInputStream();
            outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            String resultxml = new String(outSteam.toByteArray(), "utf-8");
            Map<String, String> params = PayCommonUtil.doXMLParse(resultxml);


            Map<String,String> return_data = new HashMap<String,String>();
            System.out.println("===============付款成功==============");
            // ------------------------------
            // 处理业务开始
            // ------------------------------
            // 此处处理订单状态，结合自己的订单数据完成订单状态的更新
            // ------------------------------

            String total_fee = params.get("total_fee");
            double v = Double.valueOf(total_fee) / 100;
            //获得返回的订单Id
            String orderId = String.valueOf(Long.parseLong(params.get("out_trade_no").split("O")[0]));
            String payOrderTimeStr = params.get("time_end"); //交易时间

            String tradeNo = params.get("transaction_id");
            String resultCode= params.get("result_code");
            String openid = params.get("openid");
            if (resultCode.equals("SUCCESS")) {
                //修改订单状态 订单号 交易号 交易时间 支付方式
                String payMethod = "0";
                service.updatePayOrder(orderId,tradeNo,payOrderTimeStr,payMethod);
            }
            return_data.put("return_code", "SUCCESS");
            return_data.put("return_msg", "OK");
            msg = StringUtil.GetMapToXML(return_data);
        } catch (Exception e) {
            e.printStackTrace();
            msg = "error!";
        }finally {
            if (outSteam != null) {
                outSteam.close();
            }
            if (inStream != null) {
                inStream.close();
            }
        }
        return msg;
    }

    /**
     * 支付宝支付
     * param orderId
     */
    @ApiOperation(value = "支付宝支付", httpMethod = "GET", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/aliPayBuyGoods")
    @ResponseBody
    public String buyAlipayChildClassList(@RequestParam String orderId,
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
            String result = appAlipayService.aliPayBuyGoods(new ObjectId(orderId), getUserId(), ipconfig);
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
    @RequestMapping("/aliNotify")
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

            String tradeNo = request.getParameter("trade_no");                //支付宝交易号
            String orderId = request.getParameter("out_trade_no");            //获取订单号
            String total_fee_string = request.getParameter("total_amount");            //获取总金额
            String trade_status = request.getParameter("trade_status");   //订单状态
            String user_id = request.getParameter("buyer_logon_id");    //支付人支付宝
            String payOrderTimeStr = request.getParameter("notify_time");    //支付人支付宝

            EBusinessLog.info("trade_no;" + tradeNo);
            EBusinessLog.info("out_trade_no;" + orderId);
            EBusinessLog.info("total_fee_string;" + total_fee_string);
            EBusinessLog.info("trade_status;" + trade_status);


            if (verify(params)) {
                EBusinessLog.error("支付宝回调验证通过");
                if (trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")) {
                    EBusinessLog.info("TRADE_SUCCESS");
                    double price = Double.parseDouble(total_fee_string);
                    //修改订单状态 订单号 交易号 交易时间 支付方式
                    String payMethod = "1";
                    service.updatePayOrder(orderId,tradeNo,payOrderTimeStr,payMethod);
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

}
