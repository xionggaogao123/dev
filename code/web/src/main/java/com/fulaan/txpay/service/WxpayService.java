package com.fulaan.txpay.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.db.excellentCourses.AccountFrashDao;
import com.db.excellentCourses.AccountLogDao;
import com.db.excellentCourses.AccountOrderDao;
import com.db.excellentCourses.ClassOrderDao;
import com.db.excellentCourses.ExcellentCoursesDao;
import com.db.excellentCourses.ExtractCashDao;
import com.db.excellentCourses.HourClassDao;
import com.db.excellentCourses.RechargeResultDao;
import com.db.excellentCourses.UserAccountDao;
import com.db.excellentCourses.UserBehaviorDao;
import com.db.user.UserDao;
import com.fulaan.txpay.Utils.HttpXmlUtils;
import com.fulaan.txpay.Utils.ParseXMLUtils;
import com.fulaan.txpay.Utils.RandCharsUtils;
import com.fulaan.txpay.Utils.WXSignUtils;
import com.fulaan.txpay.entity.Unifiedorder;
import com.fulaan.txpay.entity.UnifiedorderResult;
import com.mongodb.DBObject;
import com.pojo.excellentCourses.AccountFrashEntry;
import com.pojo.excellentCourses.AccountLogEntry;
import com.pojo.excellentCourses.AccountOrderEntry;
import com.pojo.excellentCourses.ClassOrderEntry;
import com.pojo.excellentCourses.ExcellentCoursesEntry;
import com.pojo.excellentCourses.HourClassEntry;
import com.pojo.excellentCourses.RechargeResultEntry;
import com.pojo.excellentCourses.UserBehaviorEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.StringUtil;

import cn.jiguang.commom.utils.StringUtils;

@Service
public class WxpayService {

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


    private UserDao userDao = new UserDao();

    private static final  int MAX_PRICE = 10000;
    //微信  1
    private static final  int PAY_TYPE = 1;
    //支付内容
    private static final  String PAY_BODY = "充值美豆";
    //直接支付内容
    private static final  String PAY_NOW_BODY = "充值美豆";
    //支付标题
    private static final  String PAY_SUBJECT = "充值美豆";
    //直接支付标题
    private static final  String PAY_NOW_SUBJECT = "购买课程";
    //支付超时时间  ---最晚5分钟内付款
    private static final  String PAY_TIMEOUTEXPRESS= "5m";
    
  //appid微信
    private static final String PAY_APPID = "wx6f60f76361871720";
    
  //mch_id商户号
    private static final String PAY_MCHID = "1509679261";

    //充值回调
    //private static final String PAY_NOTIFYURL = "appapi.jiaxiaomei.com/jxmapi/appwxpay/notify.do";
    //private static final String PAY_NOTIFYURL = "http://118.242.18.202:90/jxmapi/appwxpay/notify.do";
    private static final String PAY_NOTIFYURL = "http://s215l05201.51mypc.cn:24697/jxmapi/appwxpay/notify.do";
    //购买课程回调
    //private static final String PAY_NOW_NOTIFYURL = "appapi.jiaxiaomei.com/jxmapi/appwxpay/newNotify.do";
    //private static final String PAY_NOW_NOTIFYURL = "http://118.242.18.202:90/jxmapi/appwxpay/newNotify.do";
    private static final String PAY_NOW_NOTIFYURL = "http://s215l05201.51mypc.cn:24697/jxmapi/appwxpay/newNotify.do";


    private static final Logger EBusinessLog = Logger.getLogger("WxpayService");
    
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
    
    public String createOrderId(String id){
        if(id.length()==6){
            //随机生成一个四位数
            int num = (int)(Math.random()*8999)+1000;
            id = id + num;
        }
        return System.currentTimeMillis()+id;
    }
    
    public UnifiedorderResult createAppChongPay(String bodyy,String subject,String outTradeNo,String timeoutExpress,int totalAmount,String url,String ip) {
        //应用id
        String appid = PAY_APPID;
        //商户号
        String mch_id = PAY_MCHID;
        //随机字符串
        String nonce_str = RandCharsUtils.getRandomString(16);
        //商品描述
        String body = bodyy;
        //商品详情
        String detail = bodyy;
        //附加数据
        String attach = bodyy;
        //商户订单号
        String out_trade_no = outTradeNo;
        //总金额
        int total_fee = totalAmount;
        //终端ip
        String spbill_create_ip = ip; 
        //交易起始时间
        String time_start = RandCharsUtils.timeStart();
        //交易结束时间
        String time_expire = RandCharsUtils.timeExpire();
        //通知地址
        String notify_url = url;
        //交易类型
        String trade_type = "APP";
        
      //参数：开始生成签名
        SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
        parameters.put("appid", appid);
        parameters.put("mch_id", mch_id);
        parameters.put("nonce_str", nonce_str);
        parameters.put("body", body);
        parameters.put("detail", detail);
        parameters.put("attach", attach);
        parameters.put("out_trade_no", out_trade_no);
        parameters.put("total_fee", total_fee);
        parameters.put("time_start", time_start);
        parameters.put("time_expire", time_expire);
        parameters.put("notify_url", notify_url);
        parameters.put("trade_type", trade_type);
        parameters.put("spbill_create_ip", spbill_create_ip);
        
        String sign = WXSignUtils.createSign("UTF-8", parameters);
        
        Unifiedorder unifiedorder = new Unifiedorder();
        unifiedorder.setAppid(appid);
        unifiedorder.setMch_id(mch_id);
        unifiedorder.setNonce_str(nonce_str);
        unifiedorder.setSign(sign);
        unifiedorder.setBody(body);
        unifiedorder.setDetail(detail);
        unifiedorder.setAttach(attach);
        unifiedorder.setOut_trade_no(out_trade_no);
        unifiedorder.setTotal_fee(total_fee);
        unifiedorder.setSpbill_create_ip(spbill_create_ip);
        unifiedorder.setTime_start(time_start);
        unifiedorder.setTime_expire(time_expire);
        unifiedorder.setNotify_url(notify_url);
        unifiedorder.setTrade_type(trade_type);
        
      //构造xml参数
        String xmlInfo = HttpXmlUtils.xmlInfo(unifiedorder);
        
        String wxUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        
        String method = "POST";
        
        String weixinPost = HttpXmlUtils.httpsRequest(wxUrl, method, xmlInfo).toString();
        
  
        
        Map<String, String> map = ParseXMLUtils.jdomParseXml(weixinPost);
        
        /**
         * 二次签名
         */
        Long s = new Date().getTime()/1000;
        SortedMap<Object,Object> mapp = new TreeMap<Object,Object>();
        mapp.put("appid", map.get("appid"));
        mapp.put("partnerid", map.get("mch_id"));
        mapp.put("prepayid", map.get("prepay_id"));
        mapp.put("noncestr", map.get("nonce_str"));
        mapp.put("timestamp", String.valueOf(s));
        mapp.put("package", "Sign=WXPay");
        String sign2 = WXSignUtils.createSign("UTF-8", mapp);

        UnifiedorderResult ufdr = new UnifiedorderResult();
        if (StringUtils.isNotEmpty(map.get("prepay_id"))) {
            ufdr.setAppid(map.get("appid"));
            ufdr.setMch_id(map.get("mch_id"));
            ufdr.setPrepay_id(map.get("prepay_id"));
            ufdr.setDevice_info(map.get("device_info"));
            ufdr.setNonce_str(map.get("nonce_str"));
            ufdr.setSign(sign2);
            ufdr.setResult_code(map.get("result_code"));
            ufdr.setErr_code(map.get("err_code"));
            ufdr.setErr_code_des("err_code_des");
            ufdr.setTrade_type(map.get("trade_type"));
            ufdr.setReturn_code(map.get("return_code"));
            ufdr.setReturn_msg(map.get("return_msg"));
            ufdr.setTimestamp(String.valueOf(s));
        }
        return ufdr;
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
        //生成微信支付订单  String body,String subject,String outTradeNo,String timeoutExpress,String totalAmount
        String body = "家校美充值"+price+"美豆";
        UnifiedorderResult order = createAppChongPay(body,PAY_SUBJECT,orderId,PAY_TIMEOUTEXPRESS,price,PAY_NOTIFYURL,ip);
        if(StringUtils.isEmpty(order.getPrepay_id())){
            addLog(userId,contactId,"微信未支付订单生成失败，订单终止");
            EBusinessLog.info(userId.toString()+"-"+contactId.toString()+"微信未支付订单生成失败，订单终止");
            throw new Exception("订单创建失败！");
        }
        //生成未支付订单记录
        AccountOrderEntry accountOrderEntry = new AccountOrderEntry(contactId,userId,"",order.getPrepay_id(),"",price,orderId,PAY_TYPE,ip,0l,"");
        addLog(userId,contactId,"生成了微信未支付订单："+order);
        EBusinessLog.info(userId.toString()+"-"+contactId.toString()+"生成了微信未支付订单："+order);
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
    public UnifiedorderResult appNowPay(double price,ObjectId userId,String ip,List<ClassOrderEntry> entries) throws Exception{
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
        //生成微信支付订单  String body,String subject,String outTradeNo,String timeoutExpress,String totalAmount
        String body = "购买课程";
        UnifiedorderResult order = createAppChongPay(body,PAY_NOW_SUBJECT,orderId,PAY_TIMEOUTEXPRESS,new BigDecimal(price*100).intValue(),PAY_NOW_NOTIFYURL,ip);
        if(StringUtils.isEmpty(order.getPrepay_id())){
            addLog(userId,contactId,"微信未支付订单生成失败，订单终止");
            EBusinessLog.info(userId.toString()+"-"+contactId.toString()+"微信未支付订单生成失败，订单终止");
            throw new Exception("订单创建失败！");
        }
        //生成未支付订单记录
        AccountOrderEntry accountOrderEntry = new AccountOrderEntry(contactId,userId,"",order.getPrepay_id(),"",price,orderId,PAY_TYPE,ip,0l,"");
        addLog(userId,contactId,"生成了微信未支付订单："+order);
        EBusinessLog.info(userId.toString()+"-"+contactId.toString()+"生成了微信未支付订单："+order);
        accountOrderDao.addEntry(accountOrderEntry);
        addLog(userId,contactId,"生成了JXM未支付订单："+accountOrderEntry.getID());
        EBusinessLog.info(userId.toString()+"-"+contactId.toString()+"生成了JXM未支付订单："+accountOrderEntry.getID());
        for(ClassOrderEntry entry:entries){
            entry.setOrderId(orderId);
        }
        return order;
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
    
    public UnifiedorderResult buyAlipayChildClassList(ObjectId id,ObjectId userId,String classIds,ObjectId sonId,String ip) throws  Exception{
        //创建订单
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(id);
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        if(classIds==null|| classIds.equals("")){
            throw  new Exception("请至少购买一节课程");
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
                    price = sum(price, hourClassEntry.getClassNewPrice());
                }
            }
            UnifiedorderResult order = this.appNowPay(price, userId, ip, classOrderEntries1);
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
     * 微信回调，更新订单状态
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
                accountFrashEntry.setAccount(accountFrashEntry.getAccount()+price);
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
            if(accountFrashEntry!=null){
                userBehaviorEntry.setAccount(userBehaviorEntry.getAccount()+price);
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
     * 微信回调，更新订单状态
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
                accountFrashEntry.setAccount(accountFrashEntry.getAccount()+price);
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
            if(accountFrashEntry!=null){
                userBehaviorEntry.setAccount(userBehaviorEntry.getAccount()+price);
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
                double newPr = userBehaviorEntry.getAccount() - price;
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
                    double newPrice2 = accountFrashEntry.getAccount()-price;
                    accountFrashDao.updateEntry(accountFrashEntry.getID(),newPrice2);
                    addLog(userId,contactId,"修改充值账户消费记录成功！余额:"+newPrice2);
                    EBusinessLog.info(userId.toString()+"-"+contactId.toString()+"修改美豆账户消费记录成功！余额:"+newPrice2);

                    //添加充值账户记录
                    AccountOrderEntry accountOrderEntry2 = new AccountOrderEntry(contactId,userId,"","","",price,"",Constant.ZERO,"",0l,"");
                    accountOrderDao.addEntry(accountOrderEntry2);
                    addLog(userId,contactId,"添加充值账户记录成功！余额:"+newPrice2);
                    EBusinessLog.info(userId.toString()+"-"+contactId.toString()+"添加充值账户记录成功！余额:"+newPrice2);

                    //修改课节订单为已购买
                    classOrderDao.updateEntryToBuy(objectIdList4);
                }catch (Exception e){

                }
            }else{
                addLog(userId,contactId,"余额不足，实际："+newPrice+",账户："+userBehaviorEntry.getAccount());
            }
            //修改课程人数
            Set<ObjectId> set = classOrderDao.getUserIdEntry(excellentCoursesEntry.getID());
            excellentCoursesEntry.setStudentNumber(set.size());
            excellentCoursesDao.addEntry(excellentCoursesEntry);
        }



    }
}
