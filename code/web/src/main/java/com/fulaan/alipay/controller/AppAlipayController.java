package com.fulaan.alipay.controller;

import com.fulaan.alipay.service.AppAlipayService;
import com.fulaan.alipay.util.AlipayNotify;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.BaseController;
import com.pojo.ebusiness.EOrderEntry;
import com.pojo.emarket.OrderState;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import io.swagger.annotations.*;
import org.apache.log4j.Logger;
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
@RequestMapping("/jxmapi/appalipay")
public class AppAlipayController extends BaseController {
    @Autowired
    private AppAlipayService appAlipayService;

    private static final Logger EBusinessLog = Logger.getLogger("AppAlipayController");

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
            Map<String,Object> map = appAlipayService.appPay(price, getUserId(), ipconfig);
            respObj.setMessage(map);
        }catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
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
    public void callBackNotify(HttpServletRequest request) throws Exception {


        EBusinessLog.info("支付宝回调;" + request.getParameterMap());

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
            if (AlipayNotify.verify(params)) {
                EBusinessLog.info("支付宝回调验证通过");
                if (trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")) {
                    EBusinessLog.info("TRADE_SUCCESS");
                    //修改订单状态
                    appAlipayService.payed(out_trade_no,trade_no,user_id,Integer.parseInt(total_fee_string),str.toString(),notify_time);
                }
            } else {
                EBusinessLog.info("支付宝验证失败");
            }
        } catch (Exception ex) {
            EBusinessLog.error("支付宝报错", ex);
        }

    }


    private boolean shouldUpdateOrder(EOrderEntry eoe, int price) {
        EBusinessLog.info("eoePrice" + eoe.getTotalPrice() + "price" + price);
        if (eoe.getState() != OrderState.READY.getType())
            return false;
        return true;
    }



   /* private void sendEmail(final EOrderEntry eoe) {

        final StringBuilder goodsInfo = new StringBuilder();

        for (EOrderEntry.EOrderGoods eog : eoe.getOrderGoods()) {
            EGoodsEntry ge = eGoodsService.getEGoodsEntry(eog.geteGoodsId());
            goodsInfo.append(ge.getName()).append(" ");
            List<ObjectId> kinds = eog.getKindIds();
            if (null != kinds && kinds.size() > 0) {
                StringBuilder kindInfo = new StringBuilder();
                List<EGoodsEntry.Kind> kindList = ge.getKindList();
                for (EGoodsEntry.Kind k : kindList) {
                    kindInfo.append(k.getName()).append(":");
                    for (EGoodsEntry.Spec ivp : k.getList()) {
                        if (kinds.contains(ivp.getSpecId())) {
                            kindInfo.append(ivp.getSpecName()).append("	");
                        }
                    }
                }
                goodsInfo.append(kindInfo.toString()).append(";");
                goodsInfo.append("数量：" + eog.getCount());
                goodsInfo.append("单价(单位分)：" + eog.getPrice());

            }
        }


        final StringBuilder addressInfo = new StringBuilder();

        EOrderAddressDTO dto = orderService.getEOrderAddressDTO(eoe.getAddressId());
        addressInfo.append(dto.getUserName()).append(" ");
        addressInfo.append(dto.getTelephone()).append(" ");
        addressInfo.append(dto.getAddress()).append(" ");


        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                MailUtils sendMail = new MailUtils();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("订单ID：" + eoe.getEorderNumber());
                stringBuilder.append("时间：" + DateTimeUtils.convert(System.currentTimeMillis(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A));
                stringBuilder.append("， 商品信息：" + goodsInfo.toString());
                stringBuilder.append("， 物流信息：" + addressInfo.toString());
                stringBuilder.append("， 总价(单位分)：" + eoe.getTotalPrice());
                stringBuilder.append("， 支付单号：" + eoe.getPayNumber());

                String[] emailArr = emails.split(",");
                for (String email : emailArr) {
                    try {
                        sendMail.sendMail("订单信息", email, stringBuilder.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                String[] mails = new String[]{"siri@fulaan.com", "carry.xue@fulaan.com"};
                for (String email : mails) {
                    try {
                        sendMail.sendMail("订单信息", email, stringBuilder.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        th.start();

    }*/

}
