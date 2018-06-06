package com.fulaan.alipay.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.db.excellentCourses.AccountLogDao;
import com.db.excellentCourses.AccountOrderDao;
import com.db.user.UserDao;
import com.fulaan.alipay.config.AlipayNewConfig;
import com.pojo.excellentCourses.AccountLogEntry;
import com.pojo.excellentCourses.AccountOrderEntry;
import com.pojo.user.UserEntry;
import com.sys.utils.DateTimeUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by James on 2018-05-30.
 */
@Service
public class AppAlipayService  {
    private AccountLogDao accountLogDao = new AccountLogDao();

    private AccountOrderDao accountOrderDao = new AccountOrderDao();

    private UserDao userDao = new UserDao();

    private static final  int MAX_PRICE = 10000;
    //支付宝  1
    private static final  int PAY_TYPE = 1;
    //支付内容
    private static final  String PAY_BODY = "充值美豆";
    //支付标题
    private static final  String PAY_SUBJECT = "充值美豆";
    //支付超时时间  ---最晚5分钟内付款
    private static final  String PAY_TIMEOUTEXPRESS= "5m";

    private static final String PAY_NOTIFYURL = "";


    private static final Logger EBusinessLog = Logger.getLogger("AppAlipayService");
    /**
     * 充值消费日志
     */
    public void addLog(ObjectId userId,ObjectId contactId,String description){
        AccountLogEntry accountLogEntry = new AccountLogEntry(userId,contactId,description);
        accountLogDao.addEntry(accountLogEntry);
    }

    /**
     * 用户自定义充值（创建订单）
     */
    public Map<String,Object> appPay(int price,ObjectId userId,String ip) throws Exception{
        Map<String,Object> map = new HashMap<String, Object>();
        //生成订单唯一标识
        ObjectId contactId = new ObjectId();
        addLog(userId,contactId,"开始创建订单！");
        EBusinessLog.info(userId.toString()+"开始创建订单！");
        //用户鉴权
        UserEntry userEntry = userDao.findByUserId(userId);
        if(userEntry==null){
            addLog(userId,contactId,"用户信息不存在，订单终止");
            EBusinessLog.info(userId.toString()+"-"+contactId.toString()+"用户信息不存在，订单终止");
            throw new Exception("用户信息不存在，请确认！");
        }
        if(price<=0){
            addLog(userId,contactId,"充值金额有误，订单终止");
            EBusinessLog.info(userId.toString()+"-"+contactId.toString()+"充值金额有误，订单终止");
            throw new Exception("充值金额有误！");
        }
        //充值价格判断
        if(price>MAX_PRICE){
            addLog(userId,contactId,"单笔充值金额过大为"+price+"元，订单终止");
            EBusinessLog.info(userId.toString()+"-"+contactId.toString()+"单笔充值金额过大为"+price+"元，订单终止");
            throw new Exception("一次充值的金额不能超过"+MAX_PRICE+"元！");
        }
        //生成订单号
        String orderId = createOrderId(userEntry.getGenerateUserCode());
        addLog(userId,contactId,"生成了充值订单号："+orderId);
        EBusinessLog.info(userId.toString()+"-"+contactId.toString()+"生成了充值订单号："+orderId);
        //生成支付宝支付订单  String body,String subject,String outTradeNo,String timeoutExpress,String totalAmount
        String body = "家校美充值"+price+"美豆";
        String order = createAppChongPay(body,PAY_SUBJECT,orderId,PAY_TIMEOUTEXPRESS,price+"");
        if(order.equals("")){
            addLog(userId,contactId,"支付宝未支付订单生成失败，订单终止");
            EBusinessLog.info(userId.toString()+"-"+contactId.toString()+"支付宝未支付订单生成失败，订单终止");
            throw new Exception("订单创建失败！");
        }
        //生成未支付订单记录
        AccountOrderEntry accountOrderEntry = new AccountOrderEntry(contactId,userId,"",order,"",price,orderId,PAY_TYPE,ip,0l,"");
        addLog(userId,contactId,"生成了支付宝未支付订单："+order);
        EBusinessLog.info(userId.toString()+"-"+contactId.toString()+"生成了支付宝未支付订单："+order);
        accountOrderDao.addEntry(accountOrderEntry);
        addLog(userId,contactId,"生成了JXM未支付订单："+accountOrderEntry.getID());
        EBusinessLog.info(userId.toString()+"-"+contactId.toString()+"生成了JXM未支付订单："+accountOrderEntry.getID());
        map.put("order", order);
        map.put("id", accountOrderEntry.getID());
        return map;
    }

    public String createOrderId(String id){
        if(id.length()==6){
            //随机生成一个四位数
            int num = (int)(Math.random()*8999)+1000;
            id = id + num;
        }
        String str = System.currentTimeMillis()+id;
        return str;
    }

    /**
     * 生成订单
     */
    public void createPay(){
        //实例化客户端
        //AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", APP_ID, APP_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY, "RSA2");
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayNewConfig.APP_ID, AlipayNewConfig.USER_PRIVATE_KEY, "json", AlipayNewConfig.CHARSET, AlipayNewConfig.RSA_PUBLIC, "RSA2");
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody("我是测试数据");
        model.setSubject("App支付测试Java");
        //  model.setOutTradeNo(outtradeno);
        model.setTimeoutExpress("30m");
        model.setTotalAmount("0.01");
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl("商户外网可以访问的异步地址");
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成订单
     */
    public String createAppChongPay(String body,String subject,String outTradeNo,String timeoutExpress,String totalAmount){
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayNewConfig.APP_ID, AlipayNewConfig.USER_PRIVATE_KEY, "json", AlipayNewConfig.CHARSET, AlipayNewConfig.RSA_PUBLIC, "RSA2");
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();

        //对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body。
        model.setBody(body);

        //商品的标题/交易标题/订单标题/订单关键字等。
        model.setSubject(subject);

        //商户网站唯一订单号
        model.setOutTradeNo(outTradeNo);

        //该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。 该参数数值不接受小数点， 如 1.5h，可转换为 90m。注：若为空，则默认为15d。
        model.setTimeoutExpress(timeoutExpress);

        //订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
        model.setTotalAmount(totalAmount);

        //销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY
        model.setProductCode("QUICK_MSECURITY_PAY");

        request.setBizModel(model);
        request.setNotifyUrl(PAY_NOTIFYURL);
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
            return response.getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 支付宝回调，更新订单状态
     *
     * @throws Exception
     */
    public void payed(String orderId,String zhiFuId,String account, int price, String back,String backTime) throws Exception {
        AccountOrderEntry accountOrderEntry =  accountOrderDao.getEntry(orderId);
        if(accountOrderEntry!=null){
            if(accountOrderEntry.getStatus()==0){
                accountOrderEntry.setBack(back);
                if(backTime!=null && !backTime.equals("")){
                    accountOrderEntry.setBackTime(DateTimeUtils.getStrToLongTime(backTime));
                }
                accountOrderEntry.setAccount(account);
                accountOrderEntry.setTradeN(zhiFuId);
                accountOrderEntry.setOrderId(orderId);
                accountOrderEntry.setPrice(price);
                accountOrderDao.addEntry(accountOrderEntry);
            }
        }
    }

}
