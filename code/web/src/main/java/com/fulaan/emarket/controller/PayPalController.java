package com.fulaan.emarket.controller;

import com.fulaan.alipay.config.AlipayConfig;
import com.fulaan.alipay.util.AlipayCore;
import com.fulaan.alipay.util.AlipaySubmit;
import com.fulaan.base.controller.BaseController;
import com.fulaan.emarket.service.OrderService;
import com.pojo.emarket.OrderEntry;
import com.pojo.emarket.OrderType;
import com.pojo.emarket.UserBalanceDTO;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sql.dao.UserBalanceDao;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wang_xinxin on 2015/6/4.
 */

@Controller
@RequestMapping("/pay")
public class PayPalController extends BaseController {

    private UserBalanceDao userBalanceDao = new UserBalanceDao();

    private OrderService orderService = new OrderService();
    // 支付类型
    private String payment_type = "1";
    // 必填，不能修改
    // 服务器异步通知页面路径
    // private String notify_url = "http://114.215.190.61/notify_url.jsp";
    private String notify_url = "http://www.k6kt.com/fznotify_url.jsp";
    // 需http://格式的完整路径，不能加?id=123这类自定义参数

    // 页面跳转同步通知页面路径
    // private String return_url = "http://114.215.190.61";
    private String return_url = "http://www.k6kt.com/return_url.jsp";
    // 需http://格式的完整路径，不能加?id=123这类自定义参数，不能写成http://localhost/ /return_url.jsp

    // 卖家支付宝帐户
    private String seller_email = "zhifu@fulaan.com";// = new
    // String(request.getParameter("WIDseller_email").getBytes("ISO-8859-1"),"UTF-8");
    // 必填

    // 商户订单号
    private String out_trade_no = "";// = new
    // String(request.getParameter("WIDout_trade_no").getBytes("ISO-8859-1"),"UTF-8");
    // 商户网站订单系统中唯一订单号，必填

    // 订单名称
    private String subject = "";// = new String(request.getParameter("WIDsubject").getBytes("ISO-8859-1"),"UTF-8");
    // 必填

    // 付款金额
    private String total_fee = "0.00";// = new
    // String(request.getParameter("WIDtotal_fee").getBytes("ISO-8859-1"),"UTF-8");
    // 必填

    // 订单描述

    private String body = "This is for test";// = new
    // String(request.getParameter("WIDbody").getBytes("ISO-8859-1"),"UTF-8");
    // 商品展示地址
    private String show_url = "http://www.fulaan.com";// = new
    // String(request.getParameter("WIDshow_url").getBytes("ISO-8859-1"),"UTF-8");
    // 需以http://开头的完整路径，例如：http://www.xxx.com/myorder.html

    // 防钓鱼时间戳
    private String anti_phishing_key = "";
    // 若要使用请调用类文件submit中的query_timestamp函数

    // 客户端的IP地址
    private String exter_invoke_ip = "";
    // 非局域网的外网IP地址，如：221.0.0.1

    private String orderid;

    Map<String, Object> dataMap = new HashMap<String, Object>();

    String sHtmlText;
    private String sign = "";

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getReturn_url() {
        return return_url;
    }

    public void setReturn_url(String return_url) {
        this.return_url = return_url;
    }

    public String getSeller_email() {
        return seller_email;
    }

    public void setSeller_email(String seller_email) {
        this.seller_email = seller_email;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getShow_url() {
        return show_url;
    }

    public void setShow_url(String show_url) {
        this.show_url = show_url;
    }

    public String getAnti_phishing_key() {
        return anti_phishing_key;
    }

    public void setAnti_phishing_key(String anti_phishing_key) {
        this.anti_phishing_key = anti_phishing_key;
    }

    public String getExter_invoke_ip() {
        return exter_invoke_ip;
    }

    public void setExter_invoke_ip(String exter_invoke_ip) {
        this.exter_invoke_ip = exter_invoke_ip;
    }

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, Object> dataMap) {
        this.dataMap = dataMap;
    }

    public String getsHtmlText() {
        return sHtmlText;
    }

    public void setsHtmlText(String sHtmlText) {
        this.sHtmlText = sHtmlText;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    @RequestMapping("/fzpaypal")
    @ResponseBody
    public String pay(HttpServletRequest request,HttpServletResponse response,Model model) {
        String url = "www.k6kt.com";
        notify_url = "http://" + url + "/fznotify_url.jsp";
        return_url = "http://" + url + "/return_url.jsp";
        if (!StringUtils.isEmpty(orderid)) {
            out_trade_no = orderid;
        } else {
            out_trade_no = (String)request.getParameter("orderid");
            if (StringUtils.isEmpty(out_trade_no)) {
            	out_trade_no = (String)request.getAttribute("orderid");
            }
            
        }

        System.out.println(out_trade_no);
//        PaymentDaoImpl paymentDaoImpl = new PaymentDaoImpl();
//        UserInfoDaoImpl userInfoDaoImpl = new UserInfoDaoImpl();
        // 订单信息
        OrderEntry orders = orderService.getOrderEntryByOrderNum(out_trade_no);

        if (orders.getState() == 2) {
            dataMap.put("ret", "fail");
            dataMap.put("text", "订单已支付");
            return "error";
        }
//        HttpSession session = request.getSession();
//        UserInfo user = (UserInfo) session.getAttribute("currentUser");
//        user = userInfoDaoImpl.getUserById(user.getId());

        UserBalanceDTO user = userBalanceDao.getUserBalanceInfo(getSessionValue().getId());
        if(orders.getOrderType() == OrderType.EXCELLENTLESSON.getStatus()){
            double account = 0;
            double alipay = 0;
            if (!StringUtils.isEmpty(request.getParameter("account"))) {
                account = Double.parseDouble(request.getParameter("account"));
//                orders.setAccountprice(account);
                if (user.getBalance()<account) {
                    dataMap.put("ret", "fail");
                    dataMap.put("text", "账户余额不足");
                    return "error";
                }
            }
            if (!StringUtils.isEmpty(request.getParameter("alipay"))) {
                alipay = Double.parseDouble(request.getParameter("alipay"));
//                orders.setAlipayprice(alipay);
            } else {
                dataMap.put("ret", "fail");
                dataMap.put("text", "订单失败");
                return "error";
            }
//            paymentDaoImpl.updateFzOrdersPay(orders);
            total_fee = String.valueOf(alipay);
            subject = new String(request.getParameter("lessonName"));
            body = orders.getOrdernum();
        }else if(orders.getOrderType() == OrderType.RECHARGE.getStatus()){
            total_fee = String.valueOf(orders.getPrice());
            subject = orders.getOrdernum();
            body = orders.getOrdernum();
        }
        // total_fee= "0.01"; //测试用 暂时价格设为1分钱
        // ////////////////////////////////////////////////////////////////////////////////
        // 把请求参数打包成数组
        Map<String, String> sParaTemp = new HashMap<String, String>();
        sParaTemp.put("service", "create_direct_pay_by_user");
        sParaTemp.put("partner", AlipayConfig.partner);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
        sParaTemp.put("payment_type", payment_type);
        sParaTemp.put("notify_url", notify_url);
        sParaTemp.put("return_url", return_url);
        sParaTemp.put("seller_email", seller_email);
        sParaTemp.put("out_trade_no", out_trade_no);
        sParaTemp.put("subject", subject);
        sParaTemp.put("total_fee", total_fee);
        sParaTemp.put("body", body);
        sParaTemp.put("show_url", show_url);
        sParaTemp.put("anti_phishing_key", anti_phishing_key);
        sParaTemp.put("exter_invoke_ip", exter_invoke_ip);
        Map<String, String> sPara = AlipayCore.paraFilter(sParaTemp);
        sign = AlipaySubmit.buildRequestMysign(sPara);
        System.out.println(sign);
        // 建立请求
        sHtmlText = AlipaySubmit.buildRequest(sParaTemp, "get", "确认");
        System.out.println(sHtmlText);
        dataMap.put("ret", "ok");
        return sHtmlText;
    }
}
