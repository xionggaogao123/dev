package com.fulaan.ebusiness.controller;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fulaan.ebusiness.service.EBusinessVoucherService;
import com.pojo.ebusiness.EVoucherEntry;
import com.sys.utils.DateTimeUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.controller.BaseController;
import com.fulaan.cache.CacheHandler;
import com.fulaan.user.controller.UserController;
import com.fulaan.user.service.UserService;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.ValidationUtils;

/**
 * Created by fl on 2016/2/3.
 */
@Controller
@RequestMapping("/mall/users")
public class EBusinessUserController extends BaseController {
    //创蓝短信平台
    private String url = "http://222.73.117.156/msg/";// 应用地址
    private String account = "vip_flkj";// 账号
    private String pswd = "Tch888888";// 密码
    private boolean needstatus = true;// 是否需要状态报告，需要true，不需要false
    private String product = null;// 产品ID
    private String extno = null;// 扩展码


 
    @Autowired
    private UserService userService;

    @Autowired
    private EBusinessVoucherService eBusinessVoucherService;

    @Autowired
    private UserController userController;

 

    /**
     * 获取验证码
     *
     * @param mobile 手机号
     * @return
     */
    @SessionNeedless
    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> shortMessage(String mobile, String verifyCode, HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("code", 500);
        if (!ValidationUtils.isRequestModile(mobile)) {
            model.put("message", "非法手机");
            return model;
        }
        String mobileNumber = CacheHandler.getKeyString(CacheHandler.CACHE_MOBILE, mobile);

        String mobileNumberTime = CacheHandler.getStringValue(mobileNumber);
        if (StringUtils.isNotBlank(mobileNumberTime)) {
            model.put("message", "获取验证码太频繁");
            return model;
        }

        if(!checkVerifyCode(verifyCode, request, response)){
            model.put("message", "图片验证码错误或已失效");
            return model;
        }

        model.put("message", "获取验证码失败");
        Random random = new Random();
        int num = random.nextInt(899999) + 100000;
        String cacheKeyId = new ObjectId().toString();
        model.put("cacheKeyId", cacheKeyId);
        String cacheKey = CacheHandler.getKeyString(CacheHandler.CACHE_SHORTMESSAGE, cacheKeyId);
        CacheHandler.cache(cacheKey, num + "," + mobile, Constant.SESSION_FIVE_MINUTE);//5分钟

        CacheHandler.cache(mobileNumber, String.valueOf(System.currentTimeMillis()), Constant.SESSION_ONE_MINUTE);//一分钟

        String msg = "亲爱的客户您好，您的验证码为" + num + "，有效期为5分钟。复兰商城客服绝不会索取此验证码，请勿将验证码告诉他人。";
        try {
            String resp = batchSend(url, account, pswd, mobile, msg, needstatus, product, extno);
            String responseCode = resp.split("\\n")[0].split(",")[1];
            if (responseCode.equals("0")) {
                model.put("code", 200);
                model.put("message", resp);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    private Boolean checkVerifyCode(String verifyCode, HttpServletRequest request, HttpServletResponse response){
        //验证码
        String validateCode = "";
        String vckey = "";
        //获得请求信息中的Cookie数据
        Cookie[] cookies = request.getCookies();
        
        
        if(null!=cookies && cookies.length>0)
        {
	        for (Cookie c : cookies) {
	            if (Constant.COOKIE_VALIDATE_CODE.equals(c.getName())) {
	                vckey = CacheHandler.getKeyString(CacheHandler.CACHE_VALIDATE_CODE, c.getValue());
	                validateCode = CacheHandler.getStringValue(vckey);
	                CacheHandler.deleteKey(CacheHandler.CACHE_VALIDATE_CODE, vckey);
	                c.setMaxAge(0);
	                c.setPath(Constant.BASE_PATH);
	                response.addCookie(c);
	            }
	        }
        }

        if (validateCode == null || "".equals(validateCode)) {
            return false;
        }
        verifyCode = verifyCode.toUpperCase();
        if (verifyCode == null || "".equals(verifyCode)) {
            return false;
        }
        if (!verifyCode.equals(validateCode)) {
            return false;
        }
        return true;
    }

    /**
     * @param url        应用地址，类似于http://ip:port/msg/
     * @param account    账号
     * @param pswd       密码
     * @param mobile     手机号码，多个号码使用","分割
     * @param msg        短信内容
     * @param needstatus 是否需要状态报告，需要true，不需要false
     * @param product    产品id
     * @param extno      扩展码
     * @return 返回值定义参见HTTP协议文档
     * @throws Exception
     */
    private String batchSend(String url, String account, String pswd, String mobile, String msg,
                             boolean needstatus, String product, String extno) throws Exception {
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod();
        try {
            URI base = new URI(url, false);
            method.setURI(new URI(base, "HttpBatchSendSM", false));
            method.setQueryString(new NameValuePair[]{
                    new NameValuePair("account", account),
                    new NameValuePair("pswd", pswd),
                    new NameValuePair("mobile", mobile),
                    new NameValuePair("needstatus", String.valueOf(needstatus)),
                    new NameValuePair("msg", msg),
                    new NameValuePair("product", product),
                    new NameValuePair("extno", extno),
            });
            int result = client.executeMethod(method);
            if (result == HttpStatus.SC_OK) {
                InputStream in = method.getResponseBodyAsStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = in.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }
                return URLDecoder.decode(baos.toString(), "UTF-8");
            } else {
                throw new Exception("HTTP ERROR Status: " + method.getStatusCode() + ":" + method.getStatusText());
            }
        } finally {
            method.releaseConnection();
        }

    }

    /**
     * 检查用户是否存在
     *
     * @param email
     * @param phone
     * @param name
     * @return
     */
    @SessionNeedless
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> checkUser(@RequestParam(required = false, defaultValue = "") String email,
                                         @RequestParam(required = false, defaultValue = "") String phone,
                                         @RequestParam(required = false, defaultValue = "") String name,
                                         @RequestParam(required = false, defaultValue = "") String loginName
    ) {
        Map<String, Object> model = new HashMap<String, Object>();
        UserEntry userEntry = null;
        if (!email.equals("")) {
            userEntry = userService.searchUserByEmail(email);
        } else if (!phone.equals("")) {
            userEntry = userService.searchUserByMobile(phone);
        } else if (!name.equals("")) {
            userEntry = userService.searchUserByUserName(name);
        }


        if (userEntry == null) {
            model.put("isExit", 0);
        } else {
            model.put("isExit", 1);
        }
        return model;
    }

    @RequestMapping(value="/giveVoucher")
    @ResponseBody
    public Map giveVoucher() throws  Exception{
        Map map = new HashMap();
        UserEntry user = userService.searchUserId(getUserId());
        if (StringUtils.isEmpty(user.getMobileNumber())) {
            map.put("resultCode",1);
            return map;
        }
        if (user.getCoupon()==0) {
            List<EVoucherEntry> entries=new ArrayList<EVoucherEntry>();
            entries.add(getActivityEVouchers(getUserId(), 5000, "2016-09-10 23:59:59"));
            entries.add(getActivityEVouchers(getUserId(), 2500, "2016-10-10 23:59:59"));
            entries.add(getActivityEVouchers(getUserId(), 1500, "2016-10-20 23:59:59"));
            entries.add(getActivityEVouchers(getUserId(), 1000, "2016-11-10 23:59:59"));
            eBusinessVoucherService.addActivityEVoucher(entries);
            userService.updateUserCoupon(getUserId());
        }
        map.put("resultCode",0);
        return map;
    }


    public EVoucherEntry getActivityEVouchers(ObjectId userId,int amount,String time)throws Exception{
        String num = String.valueOf(System.currentTimeMillis());
        num += RandomUtils.nextInt(Constant.MIN_PASSWORD);
        SimpleDateFormat sdf=new SimpleDateFormat(DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H);
        Date t=sdf.parse(time);
        EVoucherEntry eVoucherEntry = new EVoucherEntry(userId, num, amount, t.getTime(), 0, 1);
        return eVoucherEntry;
    }

    /**
     * 获取验证码
     *
     * @param mobile 手机号
     * @return
     */
    @SessionNeedless
    @RequestMapping(value = "/getVerificationCode")
    @ResponseBody
    public Map<String, Object> getVerificationCode(String mobile) {

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("code", 500);
        if (!ValidationUtils.isRequestModile(mobile)) {
            model.put("message", "非法手机");
            return model;
        }
        String mobileNumber = CacheHandler.getKeyString(CacheHandler.CACHE_MOBILE, mobile);

        String mobileNumberTime = CacheHandler.getStringValue(mobileNumber);
        if (StringUtils.isNotBlank(mobileNumberTime)) {
            model.put("message", "获取验证码太频繁");
            return model;
        }
        model.put("message", "获取验证码失败");
        Random random = new Random();
        int num = random.nextInt(899999) + 100000;
        String cacheKeyId = new ObjectId().toString();
        model.put("cacheKeyId", cacheKeyId);
        String cacheKey = CacheHandler.getKeyString(CacheHandler.CACHE_SHORTMESSAGE, cacheKeyId);
        CacheHandler.cache(cacheKey, num + "," + mobile, Constant.SESSION_FIVE_MINUTE);//5分钟

        CacheHandler.cache(mobileNumber, String.valueOf(System.currentTimeMillis()), Constant.SESSION_ONE_MINUTE);//一分钟

        String msg = "亲爱的客户您好，您的验证码为" + num + "，有效期为5分钟。复兰商城客服绝不会索取此验证码，请勿将验证码告诉他人。";
        try {
            String resp = batchSend(url, account, pswd, mobile, msg, needstatus, product, extno);
            String responseCode = resp.split("\\n")[0].split(",")[1];
            if (responseCode.equals("0")) {
                model.put("code", 200);
                model.put("message", resp);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

}
