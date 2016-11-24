package com.fulaan.mall.pojo;

import com.fulaan.alipay.config.AlipayConfig;
import com.fulaan.alipay.util.AlipayCore;
import com.fulaan.alipay.util.AlipaySubmit;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jerry on 2016/10/13.
 */
public class OrderInfo {

  private String partner = AlipayConfig.partner;
  private String seller_id = AlipayConfig.SELLER;
  private String service = "mobile.securitypay.pay";
  private String payment_type = "1";
  private String _input_charset = "utf-8";
  private String it_b_pay = "30m";
  private String sign_type = "RSA";

  private String out_trade_no;
  private String subject;
  private String body;
  private String total_fee;
  private String notify_url;
  private String sign;
  private String preSign;

  public OrderInfo(String out_trade_no, String subject, String body, String total_fee, String notify_url) {
    this.out_trade_no = out_trade_no;
    this.subject = subject;
    this.body = body;
    this.total_fee = total_fee;
    this.notify_url = notify_url;
    signRSA();
  }

  private void signRSA() {

    Map<String, String> map = new HashMap<String, String>();
    map.put("partner", partner);
    map.put("seller_id", seller_id);
    map.put("service", service);
    map.put("payment_type", payment_type);
    map.put("_input_charset", _input_charset);
    map.put("it_b_pay", it_b_pay);
    map.put("out_trade_no", out_trade_no);
    map.put("subject", subject);
    map.put("body", body);
    map.put("total_fee", total_fee);
    map.put("notify_url", notify_url);

    preSign = AlipayCore.createPreSignString(map);
    sign = AlipaySubmit.sign(preSign);

    try {
      /**
       * 仅需对sign 做URL编码
       */
      sign = URLEncoder.encode(sign, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }

    /**
     * 完整的符合支付宝参数规范的订单信息
     */
    sign = preSign + "&sign=" + "\"" + sign + "\"" + "&" + "sign_type=" + "\"" + sign_type + "\"";

  }

  private String getOrderInfo(String subject, String body, String price, String orderid) {
    // 签约合作者身份ID
    String orderInfo = "partner=" + "\"" + AlipayConfig.partner + "\"";
    // 签约卖家支付宝账号
    orderInfo += "&seller_id=" + "\"" + "zhifu@fulaan.com" + "\"";
    // 商户网站唯一订单号
    orderInfo += "&out_trade_no=" + "\"" + orderid + "\"";
    // 商品名称
    orderInfo += "&subject=" + "\"" + subject + "\"";
    // 商品详情
    orderInfo += "&body=" + "\"" + body + "\"";
    // 商品金额
    orderInfo += "&total_fee=" + "\"" + price + "\"";
    // 服务器异步通知页面路径
    orderInfo += "&notify_url=" + "\"" + notify_url + "\"";
    // 服务接口名称， 固定值
    orderInfo += "&service=\"mobile.securitypay.pay\"";
    // 支付类型， 固定值
    orderInfo += "&payment_type=\"1\"";
    // 参数编码， 固定值
    orderInfo += "&_input_charset=\"utf-8\"";
    // 设置未付款交易的超时时间
    // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
    // 取值范围：1m～15d。
    // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
    // 该参数数值不接受小数点，如1.5h，可转换为90m。
    orderInfo += "&it_b_pay=\"30m\"";
    // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
    // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";
    // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
//        orderInfo += "&return_url=\"m.alipay.com\"";
    // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
    // orderInfo += "&paymethod=\"expressGateway\"";

    return orderInfo;
  }


  public String getPartner() {
    return partner;
  }

  public void setPartner(String partner) {
    this.partner = partner;
  }

  public String getSeller_id() {
    return seller_id;
  }

  public void setSeller_id(String seller_id) {
    this.seller_id = seller_id;
  }

  public String getService() {
    return service;
  }

  public void setService(String service) {
    this.service = service;
  }

  public String getPayment_type() {
    return payment_type;
  }

  public void setPayment_type(String payment_type) {
    this.payment_type = payment_type;
  }

  public String get_input_charset() {
    return _input_charset;
  }

  public void set_input_charset(String _input_charset) {
    this._input_charset = _input_charset;
  }

  public String getIt_b_pay() {
    return it_b_pay;
  }

  public void setIt_b_pay(String it_b_pay) {
    this.it_b_pay = it_b_pay;
  }

  public String getSign_type() {
    return sign_type;
  }

  public void setSign_type(String sign_type) {
    this.sign_type = sign_type;
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

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public String getTotal_fee() {
    return total_fee;
  }

  public void setTotal_fee(String total_fee) {
    this.total_fee = total_fee;
  }

  public String getNotify_url() {
    return notify_url;
  }

  public void setNotify_url(String notify_url) {
    this.notify_url = notify_url;
  }

  public String getSign() {
    return sign;
  }

  public void setSign(String sign) {
    this.sign = sign;
  }

  public String getPreSign() {
    return preSign;
  }

  public void setPreSign(String preSign) {
    this.preSign = preSign;
  }


}
