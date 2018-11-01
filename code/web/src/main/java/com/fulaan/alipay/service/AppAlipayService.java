package com.fulaan.alipay.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.db.excellentCourses.*;
import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.MineCommunityDao;
import com.db.jiaschool.HomeSchoolDao;
import com.db.jiaschool.SchoolCommunityDao;
import com.db.jiaschool.SchoolFunctionDao;
import com.db.lancustom.MonetaryOrdersDao;
import com.db.user.UserDao;
import com.fulaan.alipay.config.AlipayNewConfig;
import com.fulaan.excellentCourses.service.ExcellentCoursesService;
import com.fulaan.excellentCourses.service.ExcellentGanKaoService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.excellentCourses.*;
import com.pojo.fcommunity.CommunityEntry;
import com.pojo.fcommunity.MineCommunityEntry;
import com.pojo.jiaschool.HomeSchoolEntry;
import com.pojo.lancustom.MonetaryOrdersEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.props.Resources;
import com.sys.utils.DateTimeUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by James on 2018-05-30.
 */
@Service
public class AppAlipayService  {
    private AccountLogDao accountLogDao = new AccountLogDao();

    private AccountOrderDao accountOrderDao = new AccountOrderDao();

    private UserBehaviorDao userBehaviorDao = new UserBehaviorDao();

    private AccountFrashDao accountFrashDao = new AccountFrashDao();

    private UserAccountDao userAccountDao = new UserAccountDao();

    private ExtractCashDao extractCashDao = new ExtractCashDao();

    private RechargeResultDao rechargeResultDao = new RechargeResultDao();

    private ExcellentCoursesDao excellentCoursesDao = new ExcellentCoursesDao();

    private HourClassDao hourClassDao = new HourClassDao();

    private ClassOrderDao classOrderDao  = new ClassOrderDao();

    private SchoolCommunityDao schoolCommunityDao = new SchoolCommunityDao();

    private HomeSchoolDao homeSchoolDao = new HomeSchoolDao();

    private CoursesOrderResultDao coursesOrderResultDao = new CoursesOrderResultDao();

    private MineCommunityDao mineCommunityDao = new MineCommunityDao();

    private CommunityDao communityDao = new CommunityDao();


    private UserDao userDao = new UserDao();

    private MonetaryOrdersDao ordersDao = new MonetaryOrdersDao();

    private SchoolFunctionDao schoolFunctionDao = new SchoolFunctionDao();

    private static final  int MAX_PRICE = 10000;
    //支付宝  1
    private static final  int PAY_TYPE = 1;
    //支付内容
    private static final  String PAY_BODY = "充值美豆";
    //直接支付内容
    private static final  String PAY_NOW_BODY = "充值美豆";
    //支付标题
    private static final  String PAY_SUBJECT = "充值美豆";
    //直接支付标题
    private static final  String PAY_NOW_SUBJECT = "购买课程";

    //小兰支付标题
    private static final  String XIAOLAN_PAY_SUBJECT = "小兰购物";

    //支付超时时间  ---最晚5分钟内付款
    private static final  String PAY_TIMEOUTEXPRESS= "5m";

    //充值回调
    //private static final String PAY_NOTIFYURL = "appapi.jiaxiaomei.com/jxmapi/appalipay/notify.do";
    private static final String PAY_NOTIFYURL = Resources.getProperty("alipay.app.notify.url");
    //购买课程回调
    //private static final String PAY_NOW_NOTIFYURL = "appapi.jiaxiaomei.com/jxmapi/appalipay/newNotify.do";
    private static final String PAY_NOW_NOTIFYURL = Resources.getProperty("alipay.app.buy.notify.url");

    //小兰购物回调
    private static final String XIAOLAN_PAY_NOTIFYURL = Resources.getProperty("alipay.app.order.notify.url");
   /* private static final String XIAOLAN_PAY_NOTIFYURL = "http://2175u3v245.imwork.net:55131/jxmapi/orders/aliNotify.do";*/
    
    private GanKaoPayMessageDao ganKaoPayMessageDao = new GanKaoPayMessageDao();
    
    @Autowired
    private ExcellentGanKaoService ExcellentGanKaoServiceExcellentGanKaoService;


    private static final Logger EBusinessLog = Logger.getLogger("AppAlipayService");
    /**
     * 充值消费日志
     */
    public void addLog(ObjectId userId,ObjectId contactId,String description){
        AccountLogEntry accountLogEntry = new AccountLogEntry(userId,contactId,description);
        accountLogDao.addEntry(accountLogEntry);
    }
    public void addErrorLog(ObjectId userId,ObjectId contactId,String description){
        AccountLogEntry accountLogEntry = new AccountLogEntry(userId,contactId,description);
        accountLogEntry.setStatus(Constant.ONE);
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
        EBusinessLog.info(userId.toString()+"-"+contactId.toString()+"开始创建订单！");
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
            throw new Exception("充值金额不能为0！");
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
        String order = createAppChongPay(body,PAY_SUBJECT,orderId,PAY_TIMEOUTEXPRESS,price+"",PAY_NOTIFYURL);
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
    
    
    /**
     * 用户自定义充值（创建订单）(网页端)
     */
    public Map<String,Object> webAppPay(int price,ObjectId userId,String ip) throws Exception{
        Map<String,Object> map = new HashMap<String, Object>();
        //生成订单唯一标识
        ObjectId contactId = new ObjectId();
        addLog(userId,contactId,"开始创建订单！");
        EBusinessLog.info(userId.toString()+"-"+contactId.toString()+"开始创建订单！");
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
            throw new Exception("充值金额不能为0！");
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
        String order = createAppChongPay(body,PAY_SUBJECT,orderId,PAY_TIMEOUTEXPRESS,price+"",PAY_NOTIFYURL);
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

    /**
     * 用户购买课程（创建订单）
     */
    public String appNowPay(double price,ObjectId userId,String ip,List<ClassOrderEntry> entries) throws Exception{
        //生成订单唯一标识
        ObjectId contactId = new ObjectId();
        addLog(userId,contactId,"开始创建订单！");
        EBusinessLog.info(userId.toString()+"-"+contactId.toString()+"开始创建订单！");
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

        //生成订单号
        String orderId = createOrderId(userEntry.getGenerateUserCode());
        addLog(userId,contactId,"生成了充值订单号："+orderId);
        EBusinessLog.info(userId.toString()+"-"+contactId.toString()+"生成了充值订单号："+orderId);
        //生成支付宝支付订单  String body,String subject,String outTradeNo,String timeoutExpress,String totalAmount
        String body = "购买课程";
        String order = createAppChongPay(body,PAY_NOW_SUBJECT,orderId,PAY_TIMEOUTEXPRESS,getTwoDouble(price)+"",PAY_NOW_NOTIFYURL);
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
        for(ClassOrderEntry entry:entries){
            entry.setOrderId(orderId);
        }
        return order;
    }

    public String createOrderId(String id){
        if(id.length()==6){
            //随机生成一个四位数
            int num = (int)(Math.random()*8999)+1000;
            id = id + num;
        }
        return System.currentTimeMillis()+id;
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
    public String buyAlipayChildClassList(ObjectId id,ObjectId userId,String classIds,ObjectId sonId,String ip) throws  Exception{
        //创建订单
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(id);
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        if(classIds==null|| classIds.equals("")){
            throw  new Exception("请至少购买一节课程");
        }
        if(excellentCoursesEntry.getCourseType()==2 && userId.equals(sonId)){
            throw  new Exception("家长不可购买赶考网课程");
        }
        /*//美豆账户
        UserBehaviorEntry userBehaviorEntry = userBehaviorDao.getEntry(userId);
        if(userBehaviorEntry==null){
            throw new Exception("余额不足！");
        }

        //充值账户
        AccountFrashEntry accountFrashEntry = accountFrashDao.getEntry(userId);
        if(accountFrashEntry==null){
            throw new Exception("余额不足！");
        }*/

        String[] strings = classIds.split(",");
        for(String str:strings){
            objectIdList.add(new ObjectId(str));
        }
        //此次所下订单
        List<HourClassEntry> classEntries = hourClassDao.getEntryList(objectIdList);
        //孩子订单查询
        List<ObjectId> classOrderEntries = classOrderDao.getOwnEntry(objectIdList, sonId);
        List<ClassOrderEntry> classOrderEntries1 = new ArrayList<ClassOrderEntry>();
        long current = System.currentTimeMillis();
        double price = 0;
        if(excellentCoursesEntry!=null && classEntries.size()>0){
            for(HourClassEntry hourClassEntry: classEntries){
                if(!classOrderEntries.contains(hourClassEntry.getID())){
                    ClassOrderEntry classOrderEntry = new ClassOrderEntry();
                    //0 购买  1  未购买
                    classOrderEntry.setIsBuy(0);
                    //下单
                    classOrderEntry.setType(1);
                    classOrderEntry.setCreateTime(current);
                    classOrderEntry.setContactId(id);
                    classOrderEntry.setIsRemove(0);
                    classOrderEntry.setParentId(hourClassEntry.getID());
                    classOrderEntry.setFunction(1);
                    classOrderEntry.setPrice(hourClassEntry.getClassNewPrice());
                    classOrderEntry.setUserId(sonId);//孩子的订单
                    classOrderEntries1.add(classOrderEntry);
                    price = sum(price,hourClassEntry.getClassNewPrice());
                }
            }
            String order = this.appNowPay(price, userId, ip, classOrderEntries1);
            //添加课节订单
            this.addClassEntryBatch(classOrderEntries1);
            return order;
        }else{
            throw  new Exception("订单信息不存在！");
        }
    }

    /**
     * double 相加
     * @param d1
     * @param d2
     * @return
     */
    public static double sum(double d1,double d2){
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.add(bd2).doubleValue();
    }


    /**
     * double 相减
     * @param d1
     * @param d2
     * @return
     */
    public static double sub(double d1,double d2){
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.subtract(bd2).doubleValue();
    }

    public double getTwoDouble(double d){
        BigDecimal b = new BigDecimal(d);
        d = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return d;
    }

    /**
     * 批量增加课时
     * @param list
     */
    public void addClassEntryBatch(List<ClassOrderEntry> list) {
        List<DBObject> dbList = new ArrayList<DBObject>();
        for (int i = 0; list != null && i < list.size(); i++) {
            ClassOrderEntry si = list.get(i);
            dbList.add(si.getBaseEntry());
        }
        //导入新纪录
        if(dbList.size()>0) {
            classOrderDao.addBatch(dbList);
        }
    }

    /**
     * 生成直接支付订单
     */
    public String createAppChongPay(String body,String subject,String outTradeNo,String timeoutExpress,String totalAmount,String url){
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
        request.setNotifyUrl(url);
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
    public void payed(String orderId,String zhiFuId,String account, double price, String back,String backTime) throws Exception {
        AccountOrderEntry accountOrderEntry =  accountOrderDao.getEntry(orderId);
        if(accountOrderEntry!=null){
            ObjectId userId = accountOrderEntry.getUserId();
            ObjectId contactId = accountOrderEntry.getContactId();
            EBusinessLog.info(userId.toString()+"-"+contactId.toString()+"充值订单号："+orderId + "回调成功！");
            addLog(userId,contactId,"充值订单号："+orderId + "回调成功！");

            //修改订单状态
            if(accountOrderEntry.getStatus()==0){
                accountOrderEntry.setBack(back);
                if(backTime!=null && !backTime.equals("")){
                    accountOrderEntry.setBackTime(DateTimeUtils.getStrToLongTime(backTime));
                }
                accountOrderEntry.setAccount(account);
                accountOrderEntry.setTradeN(zhiFuId);
                accountOrderEntry.setOrderId(orderId);
                accountOrderEntry.setPrice(price);
                accountOrderEntry.setStatus(Constant.ONE);//改为已支付
                accountOrderDao.addEntry(accountOrderEntry);
                EBusinessLog.info(userId.toString()+"-"+contactId.toString()+"订单："+orderId+"已完成");
                addLog(userId, contactId, "订单：" + orderId + "已完成");
            }

            //增加账户金额
            AccountFrashEntry accountFrashEntry = accountFrashDao.getEntry(userId);
            if(accountFrashEntry!=null){
                accountFrashEntry.setAccount(sum(accountFrashEntry.getAccount(),price));
                accountFrashDao.addEntry(accountFrashEntry);
                EBusinessLog.info(userId.toString()+"-"+contactId.toString()+"订单："+orderId+"账户金额已增加");
                addLog(userId, contactId, "订单：" + orderId+"账户金额已增加");
            }else{
                AccountFrashEntry accountFrashEntry1 = new AccountFrashEntry(userId,price,Constant.ZERO,Constant.ZERO);
                accountFrashDao.addEntry(accountFrashEntry1);
                EBusinessLog.info(userId.toString()+"-"+contactId.toString()+"订单："+orderId+"账户金额已增加");
                addLog(userId, contactId, "订单：" + orderId+"账户金额已增加");
            }

            //增加美豆余额
            UserBehaviorEntry userBehaviorEntry = userBehaviorDao.getEntry(userId);
            if(userBehaviorEntry!=null){
                userBehaviorEntry.setAccount(sum(userBehaviorEntry.getAccount(),price));
                userBehaviorDao.addEntry(userBehaviorEntry);
                EBusinessLog.info(userId.toString()+"-"+contactId.toString()+"订单："+orderId+"美豆金额已增加");
                addLog(userId, contactId, "订单：" + orderId+"美豆金额已增加");
            }else{
                List<ObjectId>  objectIdList = new ArrayList<ObjectId>();
                List<ObjectId>  objectIdList2 = new ArrayList<ObjectId>();
                UserBehaviorEntry userBehaviorEntry1 = new UserBehaviorEntry(userId,price,objectIdList,objectIdList2,objectIdList);
                userBehaviorDao.addEntry(userBehaviorEntry1);
                EBusinessLog.info(userId.toString()+"-"+contactId.toString()+"订单："+orderId+"美豆金额已增加");
                addLog(userId, contactId, "订单：" + orderId+"美豆金额已增加");
            }

            //增加美豆充值记录
            RechargeResultEntry rechargeResultEntry = new RechargeResultEntry(userId,userId,"充值美豆"+price+"成功",Constant.FOUR,Constant.ONE,price,userId,null,new ArrayList<ObjectId>());
            rechargeResultDao.saveEntry(rechargeResultEntry);
            addLog(userId,contactId,"添加美豆账户充值记录成功！本次充值："+price);
            EBusinessLog.info(userId.toString()+"-"+contactId.toString()+"添加美豆账户充值记录成功！本次充值："+price);



            EBusinessLog.info(userId.toString()+"-"+contactId.toString()+"订单："+orderId+"完成");
            addLog(userId, contactId, "订单：" + orderId+"完成");
        }
    }

    /**
     * 支付宝回调，更新订单状态
     *
     * @throws Exception
     */
    public void nowPayed(String orderId,String zhiFuId,String account, double price, String back,String backTime) throws Exception {
        AccountOrderEntry accountOrderEntry =  accountOrderDao.getEntry(orderId);
        if(accountOrderEntry!=null){
            ObjectId userId = accountOrderEntry.getUserId();
            ObjectId contactId = accountOrderEntry.getContactId();
            if(accountOrderEntry.getPrice()!=price){
                addErrorLog(userId, contactId, "充值订单号：" + orderId + "异常，金额不符，订单金额：" + accountOrderEntry.getPrice() + ",实际金额：" + price);
            }
            EBusinessLog.info(userId.toString()+"-"+contactId.toString()+"充值订单号："+orderId + "回调成功！");
            addLog(userId,contactId,"充值订单号："+orderId + "回调成功！");
            //修改订单
            if(accountOrderEntry.getStatus()==0){
                accountOrderEntry.setBack(back);
                if(backTime!=null && !backTime.equals("")){
                    accountOrderEntry.setBackTime(DateTimeUtils.getStrToLongTime(backTime));
                }
                accountOrderEntry.setAccount(account);
                accountOrderEntry.setTradeN(zhiFuId);
                accountOrderEntry.setOrderId(orderId);
                accountOrderEntry.setPrice(price);
                accountOrderEntry.setStatus(Constant.ONE);//改为已支付
                accountOrderDao.addEntry(accountOrderEntry);
                EBusinessLog.info(userId.toString()+"-"+contactId.toString()+"订单："+orderId+"已完成");
                addLog(userId, contactId, "订单：" + orderId + "已完成");
            }
            //增加账户金额
            AccountFrashEntry accountFrashEntry = accountFrashDao.getEntry(userId);
            if(accountFrashEntry!=null){
                accountFrashEntry.setAccount(sum(accountFrashEntry.getAccount(),price));
                accountFrashDao.addEntry(accountFrashEntry);
                EBusinessLog.info(userId.toString()+"-"+contactId.toString()+"订单："+orderId+"账户金额已增加");
                addLog(userId, contactId, "订单：" + orderId+"账户金额已增加");
            }else{
                AccountFrashEntry accountFrashEntry1 = new AccountFrashEntry(userId,price,Constant.ZERO,Constant.ZERO);
                accountFrashDao.addEntry(accountFrashEntry1);
                accountFrashEntry = accountFrashEntry1;
                EBusinessLog.info(userId.toString()+"-"+contactId.toString()+"订单："+orderId+"账户金额已增加");
                addLog(userId, contactId, "订单：" + orderId+"账户金额已增加");
            }


            //增加美豆余额
            UserBehaviorEntry userBehaviorEntry = userBehaviorDao.getEntry(userId);
            if(userBehaviorEntry!=null){
                userBehaviorEntry.setAccount(sum(userBehaviorEntry.getAccount(),price));
                userBehaviorDao.addEntry(userBehaviorEntry);
                EBusinessLog.info(userId.toString()+"-"+contactId.toString()+"订单："+orderId+"美豆金额已增加");
                addLog(userId, contactId, "订单：" + orderId+"美豆金额已增加");
            }else{
                List<ObjectId>  objectIdList = new ArrayList<ObjectId>();
                List<ObjectId>  objectIdList2 = new ArrayList<ObjectId>();
                UserBehaviorEntry userBehaviorEntry1 = new UserBehaviorEntry(userId,price,objectIdList,objectIdList2,objectIdList);
                userBehaviorDao.addEntry(userBehaviorEntry1);
                userBehaviorEntry = userBehaviorEntry1;
                EBusinessLog.info(userId.toString()+"-"+contactId.toString()+"订单："+orderId+"美豆金额已增加");
                addLog(userId, contactId, "订单：" + orderId+"美豆金额已增加");
            }

            //增加美豆充值记录
            RechargeResultEntry rechargeResultEntry = new RechargeResultEntry(userId,userId,"充值美豆"+price+"成功",Constant.FOUR,Constant.ONE,price,userId,null,new ArrayList<ObjectId>());
            rechargeResultDao.saveEntry(rechargeResultEntry);
            addLog(userId,contactId,"添加美豆账户充值记录成功！本次充值："+price);
            EBusinessLog.info(userId.toString()+"-"+contactId.toString()+"添加美豆账户充值记录成功！本次充值："+price);


            //订课阶段
            EBusinessLog.info(userId.toString()+"-"+contactId.toString()+"订单："+orderId+"课节订单开始修改状态");
            addLog(userId, contactId, "订单：" + orderId+"课节订单开始修改状态");
            //调用自主订课
            List<ClassOrderEntry> classOrderEntries = classOrderDao.getOrderEntry(orderId);
            if(classOrderEntries.size()==0){
                EBusinessLog.info(userId.toString()+"-"+contactId.toString()+"订单："+orderId+"购买订单不存在");
                addLog(userId, contactId, "订单：" + orderId+"购买订单不存在");
                return;
            }
            List<ObjectId> objectIdList3 = new ArrayList<ObjectId>();
            List<ObjectId> objectIdList4 = new ArrayList<ObjectId>();
            for(ClassOrderEntry classOrderEntry :classOrderEntries){
                objectIdList3.add(classOrderEntry.getParentId());
                objectIdList4.add(classOrderEntry.getID());
            }
            ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(classOrderEntries.get(0).getContactId());
            if(excellentCoursesEntry==null){
                EBusinessLog.info(userId.toString()+"-"+contactId.toString()+"订单："+orderId+"购买课程不存在");
                addLog(userId, contactId, "订单：" + orderId+"购买课程不存在");
                return;
            }

            String description = "购买课程："+excellentCoursesEntry.getTitle()+"的";
            List<ObjectId>  cIds = new ArrayList<ObjectId>();
            //此次所下订单
            int newPrice = 0;
            List<HourClassEntry> classEntries = hourClassDao.getEntryList(excellentCoursesEntry.getID());
            for(HourClassEntry hourClassEntry:classEntries){
                if(objectIdList3.contains(hourClassEntry.getID())){
                    description  = description + "第"+hourClassEntry.getOrder()+"课时 ";
                    cIds.add(hourClassEntry.getID());
                    newPrice +=hourClassEntry.getClassNewPrice();
                }
            }

            if(userBehaviorEntry.getAccount()>= newPrice){
                double newPr = sub(userBehaviorEntry.getAccount(),price);
                try{
                    //修改美豆账户余额
                    userBehaviorDao.updateEntry(userBehaviorEntry.getID(), newPr);
                    addLog(userId,contactId,"修改美豆账户成功！余额:"+newPr);
                    EBusinessLog.info(userId.toString()+"-"+contactId.toString()+"修改美豆账户成功！余额:"+newPr);

                    //添加美豆账户消费记录
                    RechargeResultEntry rechargeResultEntry3 = new RechargeResultEntry(userId,userId,description,Constant.ZERO,Constant.ZERO,price,userId,excellentCoursesEntry.getID(),cIds);
                    rechargeResultDao.saveEntry(rechargeResultEntry3);
                    addLog(userId,contactId,"添加美豆账户消费记录成功！本次消费："+price);
                    EBusinessLog.info(userId.toString()+"-"+contactId.toString()+"添加美豆账户消费记录成功！本次消费："+price);

                    //修改充值账户余额
                    double newPrice2 = sub(accountFrashEntry.getAccount(),price);
                    accountFrashDao.updateEntry(accountFrashEntry.getID(),newPrice2);
                    addLog(userId,contactId,"修改充值账户消费记录成功！余额:"+newPrice2);
                    EBusinessLog.info(userId.toString()+"-"+contactId.toString()+"修改充值账户消费记录成功！余额:"+newPrice2);

                    //添加充值账户记录
                    AccountOrderEntry accountOrderEntry2 = new AccountOrderEntry(contactId,userId,"","","",price,"",Constant.ZERO,"",0l,"");
                    accountOrderDao.addEntry(accountOrderEntry2);
                    addLog(userId,contactId,"添加充值账户记录成功！余额:"+newPrice2);
                    EBusinessLog.info(userId.toString()+"-"+contactId.toString()+"添加充值账户记录成功！余额:"+newPrice2);

                    //修改课节订单为已购买
                    classOrderDao.updateEntryToBuy(objectIdList4);

                    //String or = "支付宝";
                    this.addCoursesOrderEntry(classOrderEntries.get(0).getUserId(),excellentCoursesEntry.getID(),Constant.ONE,cIds,price,orderId,Constant.ONE);
                    addLog(userId,contactId,"记录生成");
                }catch (Exception e){
                    addLog(userId,contactId,"订单输出有错误，请人工修改！");
                    EBusinessLog.error("error",e);
                }
            }else{
                addLog(userId,contactId,"余额不足，实际："+newPrice+",账户："+userBehaviorEntry.getAccount());

            }
            //修改课程人数
            Set<ObjectId> set = classOrderDao.getUserIdEntry(excellentCoursesEntry.getID());
            excellentCoursesEntry.setStudentNumber(set.size());
            excellentCoursesDao.addEntry(excellentCoursesEntry);

            //取消收藏
            List<ObjectId> collList = userBehaviorEntry.getCollectList();
            collList.remove(excellentCoursesEntry.getID());
            userBehaviorEntry.setCollectList(collList);
            userBehaviorDao.addEntry(userBehaviorEntry);

            //加入社群
            if(classOrderEntries.size()>0){
                List<ObjectId> uids = new ArrayList<ObjectId>();
                uids.add(userId);
                Map<ObjectId,ObjectId> map = new HashMap<ObjectId, ObjectId>();
                ObjectId sonId = classOrderEntries.get(0).getUserId();
                if(sonId!=null && !userId.equals(sonId)){
                    map.put(userId,sonId);
                }
                ExcellentCoursesService.inviteLessonMember(excellentCoursesEntry.getID(), uids, map);
            }
            //添加赶考网
            if(excellentCoursesEntry.getCourseType()==2){
                AppAlipayService.addGankao(userId.toString(), excellentCoursesEntry.getID().toString(), (int)price*100);
            }
            
        }



    }
    public List<ObjectId> getCommunitys3(ObjectId uid, int page, int pageSize) {
        List<MineCommunityEntry> allMineCommunitys = mineCommunityDao.findAll(uid, page, pageSize);
        List<ObjectId> list = new ArrayList<ObjectId>();
        List<ObjectId> communityIds = new ArrayList<ObjectId>();
        for (MineCommunityEntry mineCommunityEntry : allMineCommunitys) {
            communityIds.add(mineCommunityEntry.getCommunityId());
        }
        List<CommunityEntry> communityEntries = communityDao.findByNotObjectIds(communityIds);
        for(CommunityEntry communityEntry:communityEntries){
            list.add(communityEntry.getID());
        }
        return list;
    }

    //添加每日订单
    public void addCoursesOrderEntry(ObjectId userId,ObjectId coursesId,int type,List<ObjectId> classList,double price,String order,int source){
        long orderTime = System.currentTimeMillis();
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(coursesId);
        if(excellentCoursesEntry==null){
            EBusinessLog.error("error",new Exception("课程不存在，"+coursesId.toString()));
            return;
        }
        List<ObjectId>  communityIds =this.getCommunitys3(excellentCoursesEntry.getUserId(), 1, 100);
        //List<ObjectId> communityIds = excellentCoursesEntry.getCommunityIdList();
        ObjectId schoolId = null;
        String schoolName = "";
        if(communityIds!=null && communityIds.size()>0){
            //所在学校
            List<ObjectId> schoolIdsList = schoolCommunityDao.getSchoolIdsList(communityIds);
            //获得已被允许的学校
            List<ObjectId> schoolIds = schoolFunctionDao.getNewAllSchoolIdList(schoolIdsList,1, 1);
            if(schoolIds.size()>0){
                HomeSchoolEntry homeSchoolEntry =  homeSchoolDao.getEntryById(schoolIds.get(0));
                schoolId = homeSchoolEntry.getID();
                schoolName = homeSchoolEntry.getName();
            }
        }
        CoursesOrderResultEntry coursesOrderResultEntry = new CoursesOrderResultEntry(
                orderTime,userId,schoolId,schoolName,coursesId,excellentCoursesEntry.getTitle(),type
                ,classList,price,order,source);
        coursesOrderResultDao.addEntry(coursesOrderResultEntry);
    }
    //添加赶考网信息
    public static void addGankao(final String userId, final String courseId, final int price) {
        new Thread() {
            
            public void run() {
                UserDao userDao = new UserDao();
                GanKaoPayMessageDao ganKaoPayMessageDao = new GanKaoPayMessageDao();
                try {
                    
                    UserEntry u = userDao.getUserEntry(new ObjectId(userId), new BasicDBObject());
                    String mobilePhone = "";
                    if (u != null) {
                        mobilePhone = u.getMobileNumber();
                    }
                    long time = System.currentTimeMillis()+1000;
                    long endTime = System.currentTimeMillis()+30*60*1000;
                    GanKaoPayMessageEntry g = new GanKaoPayMessageEntry(new ObjectId(userId), new ObjectId(courseId), mobilePhone, 0, price, Constant.ZERO, 1, time, endTime);
                    ganKaoPayMessageDao.addEntry(g);
                    String code = ExcellentGanKaoService.createPaySign(g.getUserId().toString(), g.getMobilePhone(), g.getCourseId().toString(), g.getTruePrice());
                    ExcellentGanKaoService.TimeOut(code, g.getID(),g.getType(),g.getTime());
                } catch (Exception e) {
                    // TODO: handle exception
                    EBusinessLog.error("error",e);
                }
                
            }
           
        }.start();
    }



    /**
     *   支付宝提现
     * @param userId
     * @param price
     * @return
     * @throws Exception
     */
    public void goMyMoney(ObjectId userId,int price)throws Exception{
        if(price<=0){
            throw new Exception("提现金额不能小于0");
        }
        UserEntry userEntry = userDao.findByUserId(userId);
        if(userEntry==null){
            throw new Exception("用户信息有误，无法提现，请联系客服");
        }

        //查询用户美豆账户
        UserBehaviorEntry userBehaviorEntry = userBehaviorDao.getEntry(userId);
        if(userBehaviorEntry==null || price >userBehaviorEntry.getAccount()){
            throw new Exception("用户账户无可用余额!");
        }

        //查询用户充值账户
        AccountFrashEntry accountFrashEntry = accountFrashDao.getEntry(userId);
        if(accountFrashEntry == null || price>accountFrashEntry.getAccount()){
            throw new Exception("用户账户无可用余额!");
        }

        //查询用户绑定账户
        UserAccountEntry userAccountEntry = userAccountDao.getEntry(userId);
        if(userAccountEntry==null  || userAccountEntry.getAccountName()==null){
            throw new Exception("未绑定支付宝账户!");
        }
        //检测通过，发出申请
        ExtractCashEntry extractCashEntry = new ExtractCashEntry(userId,userEntry.getGenerateUserCode(),price,userAccountEntry.getAccountName(),userAccountEntry.getType() ,Constant.ONE);
        //生成美豆消费记录
        RechargeResultEntry rechargeResultEntry = new RechargeResultEntry(userBehaviorEntry.getUserId(),userId,"美豆提现",Constant.FOUR,Constant.TWO,price,null,null,new ArrayList<ObjectId>());
        //暂不执行现金消费记录,通过时执行 未通过美豆返回

        //账户消减
        UserBehaviorEntry userBehaviorEntry2 = userBehaviorDao.getEntry(userId);
        double newPrice = sub(userBehaviorEntry2.getAccount(),price);
        if(newPrice<0){
            throw new Exception("用户账户无可用余额!");
        }else{
            userBehaviorEntry2.setAccount(newPrice);
        }
        try{
            userBehaviorDao.addEntry(userBehaviorEntry2);
            extractCashDao.addEntry(extractCashEntry);
            rechargeResultDao.saveEntry(rechargeResultEntry);
        }catch (Exception e) {
            throw new Exception("系统繁忙，稍后再试");
        }
    }
    
    
    public double getAccount(ObjectId userId) {
        //查询用户充值账户
        AccountFrashEntry accountFrashEntry = accountFrashDao.getEntry(userId);
        if(accountFrashEntry != null) {
            return accountFrashEntry.getAccount();
        } else {
            return 0d;
        }
    }

    public String aliPayBuyGoods(ObjectId orderId, ObjectId userId, String ipconfig) throws Exception {
        //支付宝订单信息
        String orderMsg = "";
        //生成订单唯一标识
        ObjectId contactId = new ObjectId();
        addLog(userId, contactId, "开始创建订单！");
        EBusinessLog.info(userId.toString() + "-" + contactId.toString() + "开始创建订单！");
        //用户鉴权
        UserEntry userEntry = userDao.findByUserId(userId);
        if (userEntry == null) {
            addLog(userId, contactId, "用户信息不存在，订单终止");
            EBusinessLog.info(userId.toString() + "-" + contactId.toString() + "用户信息不存在，订单终止");
            throw new Exception("用户信息不存在，请确认！");
        }
        //获取未支付的订单
        MonetaryOrdersEntry ordersEntry = ordersDao.getEntryById(orderId);
        //判断订单是否支付
        if (ordersEntry.getStatus() == "1") {
            addLog(userId, contactId, "订单已支付");
            EBusinessLog.info(userId.toString() + "-" + contactId.toString() + "订单已支付");
            throw new Exception("订单已支付，请确认！");
        }
        //开始生成支付宝未支付订单
        String body = "小兰购物";
        orderMsg = createAppChongPay(body,XIAOLAN_PAY_SUBJECT,orderId.toString(),PAY_TIMEOUTEXPRESS,getTwoDouble(ordersEntry.getMoney())+"",XIAOLAN_PAY_NOTIFYURL);
        if("".equals(orderMsg)){
            addLog(userId,contactId,"支付宝未支付订单生成失败，订单终止");
            EBusinessLog.info(userId.toString()+"-"+contactId.toString()+"支付宝未支付订单生成失败，订单终止");
            throw new Exception("订单创建失败！");
        }
        return orderMsg;
    }
}
