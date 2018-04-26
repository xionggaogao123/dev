package com.fulaan.mall.controller;

import com.db.user.NewVersionUserRoleDao;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.BaseController;
import com.fulaan.cache.CacheHandler;
import com.fulaan.cache.RedisUtils;
import com.fulaan.forum.service.FInformationService;
import com.fulaan.forum.service.FInvitationService;
import com.fulaan.forum.service.FScoreService;
import com.fulaan.mall.service.EBusinessVoucherService;
import com.fulaan.pojo.Validate;
import com.fulaan.user.controller.UserController;
import com.fulaan.user.service.UserService;
import com.pojo.app.SessionValue;
import com.pojo.ebusiness.EVoucherEntry;
import com.pojo.forum.FInvitationEntry;
import com.pojo.forum.FScoreDTO;
import com.pojo.user.NewVersionUserRoleEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.mails.MailUtils;
import com.sys.utils.MD5Utils;
import com.sys.utils.RespObj;
import com.sys.utils.ValidationUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by fl on 2016/2/3.
 */
@Api(value="创蓝短信平台",hidden = true)
@Controller
@RequestMapping("/jxmapi/mall/users")
public class DefaultEBusinessUserController extends BaseController {
    //创蓝短信平台
    private String url = "http://222.73.117.156/msg/";// 应用地址
    private String account = "vip_flkj";// 账号
    private String pswd = "Tch888888";// 密码
    private boolean needstatus = true;// 是否需要状态报告，需要true，不需要false
    private String product = null;// 产品ID
    private String extno = null;// 扩展码

    //激活邮箱地址
    private static final String validateUrl = "http://fulaan.com/mall/users/emailValidate.do?";
    private static final Logger EBusinessUserController = Logger.getLogger(DefaultEBusinessUserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private UserController userController;
    @Autowired
    private FInformationService fInformationService;
    @Autowired
    private FInvitationService fInvitationService;
    @Autowired
    private EBusinessVoucherService eBusinessVoucherService;
    @Autowired
    private FScoreService fScoreService;

    NewVersionUserRoleDao newVersionUserRoleDao =new NewVersionUserRoleDao();

    @ApiOperation(value = "shortMessage", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @SessionNeedless
    @RequestMapping(value = "/sendInsetMessage", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> shortMessage(HttpServletRequest request, HttpServletResponse response) {
//        EBusinessUserController.info("进入-------");message_list
        Map<String, Object> model = new HashMap<String, Object>();
//        List<String> mobiles = KeyWordFilterUtil.;
//        EBusinessUserController.info(mobiles);
//        String msg = "【复兰教育社区】冬季大赛来袭！火热“春节才艺秀”、“家乡的冬天”摄影比赛，缤纷大奖，邀你参加！登录“复兰教育社区”APP，进入【发现】中的【大赛】版块或访问www.fulaan.com的【大赛】版块参与比赛。";
//        try {
//            EBusinessUserController.info("开始-----");
//            for (String mobile : mobiles) {
//                sendMessageInfo(mobile, msg);
//            }
//            EBusinessUserController.info("结束-------");
//        } catch (Exception e) {
//            EBusinessUserController.error("", e);
//        }
        return model;
    }

    private void sendMessageInfo(String mobile, String msg) throws Exception {
        System.out.println(mobile);
        String resp = batchSend(url, account, pswd, mobile, msg, needstatus, product, extno);
        String responseCode = resp.split("\\n")[0].split(",")[1];
        if (responseCode.equals("0")) {
            EBusinessUserController.info(mobile);
            Thread.sleep(2000);
        } else {
            Thread.sleep(2000);
            EBusinessUserController.info("失败" + mobile);
        }
    }


    /**
     * 防盗链信息
     * @param request
     * @return
     */
    @ApiOperation(value = "防盗链信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @SessionNeedless
    @RequestMapping(value = "/securitychain", method = RequestMethod.GET)
    @ResponseBody
    public RespObj securitychain(HttpServletRequest request){
        RespObj respObj=new RespObj(Constant.SUCCESS_CODE);
        String referer = request.getHeader("referer");
        // 如果是直接输入的地址，或者不是从本网站访问的重定向到本网站的首页
        respObj.setMessage(referer);
        return respObj;
    }


    /**
     * 获取验证码
     *
     * @param mobile 手机号
     * @return
     */
    @ApiOperation(value = "获取验证码", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @SessionNeedless
    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> shortMessage(String mobile, String verifyCode, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("code", 500);

        //加入防盗链结束
//        String referer = request.getHeader("referer");
//        if(referer==null){
//            model.put("message", "非法攻击!");
//            return model;
//        }
//
//        if(!referer.contains("fulaan.com")){
//            model.put("message", "非法攻击!");
//            return model;
//        }

        if (!ValidationUtils.isMobile(mobile)) {
            model.put("message", "非法手机");
            return model;
        }
        String mobileNumber = CacheHandler.getKeyString(CacheHandler.CACHE_MOBILE, mobile);
        String mobileNumberTime = CacheHandler.getStringValue(mobileNumber);
        if (StringUtils.isNotBlank(mobileNumberTime)) {
            model.put("message", "获取验证码太频繁");
            return model;
        }

        if (!checkVerifyCode(verifyCode)) {
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


        String msg = "亲爱的客户您好，您的验证码为" + num + "，有效期为5分钟。";
        try {
            String resp = batchSend(url, account, pswd, mobile, msg, needstatus, product, extno);
            String responseCode = resp.split("\\n")[0].split(",")[1];
            if (responseCode.equals("0")) {
                CacheHandler.cache(mobileNumber, String.valueOf(System.currentTimeMillis()), Constant.SESSION_ONE_MINUTE);//一分钟
                model.put("code", 200);
                model.put("message", resp);
            }else{
                EBusinessUserController.error(mobile+"-"+verifyCode+"-"+responseCode);
            }
        } catch (Exception e) {
            EBusinessUserController.error("error",e);
            EBusinessUserController.error(url+"---"+account+"---"+pswd+"---"+mobile+"---"+msg+"---"+needstatus+"---"+product+"---"+extno);
            e.printStackTrace();
        }
        return model;
    }

    /**
     * 加密
     */
    public static String decryptByPrivateKey(byte[] encrypted,HttpServletRequest request) throws Exception {
        String rootPath = request.getRealPath("/");
        // 得到私钥对象
       // File file = new File("D:\\jiak6\\code\\web\\src\\main\\webapp\\WEB-INF\\jsp\\account/key.pem");
        // 得到私钥对象
        File file = new File(rootPath+"/WEB-INF/jsp/account/key.pem");

        FileInputStream fis = new FileInputStream(file);

        int length = fis.available();

        byte [] buffer = new byte[length];
        fis.read(buffer);

        fis.close();
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey keyPrivate = kf.generatePrivate(keySpec);
        // 解密数据
        Cipher cp = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cp.init(Cipher.DECRYPT_MODE, keyPrivate);
        byte[] arr = cp.doFinal(encrypted);
        String s = new String(arr);
        return s;
    }

    /**
     * 获取验证码
     *
     * @param phone 手机号
     * @return
     */
    @ApiOperation(value = "获取验证码", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @SessionNeedless
    @RequestMapping(value = "/newMessages", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> newMessages(byte[] phone, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("code", 500);
        //byte[] bs = mobile.getBytes();
        String mobile = "";
        try{
                mobile = decryptByPrivateKey(phone,request);
        }catch (Exception e){
                e.printStackTrace();
        }
        //加入防盗链结束
//        String referer = request.getHeader("referer");
//        if(referer==null){
//            model.put("message", "非法攻击!");
//            return model;
//        }
//
//        if(!referer.contains("fulaan.com")){
//            model.put("message", "非法攻击!");
//            return model;
//        }

        if (!ValidationUtils.isMobile(mobile)) {
            model.put("message", "非法手机");
            return model;
        }
        String mobileNumber = CacheHandler.getKeyString(CacheHandler.CACHE_MOBILE, mobile);
        String mobileNumberTime = CacheHandler.getStringValue(mobileNumber);
        if (StringUtils.isNotBlank(mobileNumberTime)) {
            model.put("message", "获取验证码太频繁");
            return model;
        }

        /*if (!checkVerifyCode(verifyCode)) {
            model.put("message", "图片验证码错误或已失效");
            return model;
        }*/

        model.put("message", "获取验证码失败");
        Random random = new Random();
        int num = random.nextInt(899999) + 100000;
        String cacheKeyId = new ObjectId().toString();
        model.put("cacheKeyId", cacheKeyId);
        String cacheKey = CacheHandler.getKeyString(CacheHandler.CACHE_SHORTMESSAGE, cacheKeyId);
        CacheHandler.cache(cacheKey, num + "," + mobile, Constant.SESSION_FIVE_MINUTE);//5分钟


        String msg = "亲爱的客户您好，您的验证码为" + num + "，有效期为5分钟。";
        try {
            String resp = batchSend(url, account, pswd, mobile, msg, needstatus, product, extno);
            String responseCode = resp.split("\\n")[0].split(",")[1];
            if (responseCode.equals("0")) {
                CacheHandler.cache(mobileNumber, String.valueOf(System.currentTimeMillis()), Constant.SESSION_ONE_MINUTE);//一分钟
                model.put("code", 200);
                model.put("message", resp);
            }else{
                EBusinessUserController.error(phone+"---"+responseCode);
            }
        } catch (Exception e) {
            EBusinessUserController.error("error",e);
            EBusinessUserController.error(url+"---"+account+"---"+pswd+"---"+mobile+"---"+msg+"---"+needstatus+"---"+product+"---"+extno);
            e.printStackTrace();
        }
        return model;
    }

    private Boolean checkVerifyCode(String verifyCode) {
        //获得请求信息中的Cookie数据
        String cookieValue = getCookieValue(Constant.COOKIE_VALIDATE_CODE);
        String verifyCodeKey = CacheHandler.getKeyString(CacheHandler.CACHE_VALIDATE_CODE, cookieValue);
        String validateCode = CacheHandler.getStringValue(verifyCodeKey);
        CacheHandler.deleteKey(CacheHandler.CACHE_VALIDATE_CODE, verifyCodeKey);

        if (StringUtils.isBlank(validateCode) || StringUtils.isBlank(verifyCode)) {
            return false;
        }
        verifyCode = verifyCode.toUpperCase();
        return verifyCode.equals(validateCode);
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
    @ApiOperation(value = "emailValidate", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @SessionNeedless
    @RequestMapping(value = "/emailValidate")
    public String emailValidate(String email, String validateCode, HttpServletResponse response, HttpServletRequest request,
                                Map<String, Object> model) {
        UserEntry userEntry = userService.findByEmail(email);
        //验证用户是否存在
        if (userEntry != null) {
            //验证用户激活状态
            if (userEntry.getEmailStatus() == 0) {
                ///没激活
                Date currentTime = new Date();//获取当前时间
                //验证链接是否过期
                long activateTime = userEntry.getRegisterTime() + 24 * 60 * 60 * 1000;
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(activateTime);
                if (currentTime.before(c.getTime())) {
                    if (validateCode.equals(MD5Utils.getMD5String(userEntry.getEmailValidateCode()))) {
                        try {
                            userService.updateUserEmailStatusById(userEntry.getID());
                            userController.login(userEntry.getUserName(), userEntry.getPassword(),1, response, request);
                            model.put("message", "激活成功！");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        model.put("message", "激活码不正确");
                    }
                } else {
                    model.put("message", "激活码已过期！");
                }
            } else {
                try {
                    model.put("message", "邮箱已激活，请登录！");
                    userController.login(userEntry.getUserName(), userEntry.getPassword(),1, response, request);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            model.put("message", "该邮箱未注册（邮箱地址不存在）！");
        }
        return "redirect:/";
    }

    /**
     * 检查用户是否存在
     *
     * @param email
     * @param phone
     * @param name
     * @return
     */
    @ApiOperation(value = "检查用户是否存在", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @SessionNeedless
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> checkUser(@RequestParam(required = false, defaultValue = "") String email,
                                         @RequestParam(required = false, defaultValue = "") String phone,
                                         @RequestParam(required = false, defaultValue = "") String name) {
        Map<String, Object> model = new HashMap<String, Object>();
        UserEntry userEntry = null;
        if (StringUtils.isNotBlank(email)) {
            userEntry = userService.findByEmail(email);
        } else if (StringUtils.isNotBlank(phone)) {
            userEntry = userService.findByMobile(phone);
        } else if (StringUtils.isNotBlank(name)) {
            userEntry = userService.findByUserName(name);
        }
        if (userEntry == null) {
            model.put("isExit", 0);
        } else {
            model.put("isExit", 1);
        }
        return model;
    }

    private void processRegister(String email, String code) {
        ///如果处于安全，可以将激活码处理的更复杂点，这里我稍做简单处理
        //发送邮箱
        MailUtils sendMail = new MailUtils();
        UserEntry userEntry = userService.findByEmail(email);
        if (userEntry != null) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("<p>");
            stringBuffer.append(userEntry.getUserName());
            stringBuffer.append("，您好！<br/>");
            String emailContent = validateUrl + "&email=" + email + "&validateCode=" + MD5Utils.getMD5String(code);
            stringBuffer.append("感谢注册复兰教育社区！点击以下链接验证您的帐号：<br/><a href=\"" + emailContent + "\"");
            stringBuffer.append(" >" + emailContent + "</a><br/>如果点击无效，请把下面网页地址复制到浏览器地址栏中打开<br/><br/>");
            stringBuffer.append("这是一封系统邮件，请勿回复</p>\n");
            try {
                sendMail.sendMail("用户邮箱验证", email, stringBuffer.toString());
            } catch (Exception e1) {
                EBusinessUserController.error("", e1);
            }
        }
    }
    @ApiOperation(value = "processValidate", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @SessionNeedless
    @RequestMapping(value = "/processValidate")
    @ResponseBody
    public RespObj processValidate(String email, String emailValidateCode) {
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        processRegister(email, emailValidateCode);
        return respObj;
    }
    @ApiOperation(value = "sendEmail", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @SessionNeedless
    @RequestMapping(value = "/sendEmail")
    public String sendEmail(HttpServletRequest request, HttpServletResponse response, Map<String, Object> model) {
        SessionValue sessionValue = (SessionValue) request.getAttribute(BaseController.SESSION_VALUE);
        if (null == sessionValue || sessionValue.isEmpty()) {
            model.put("userName", "");
            model.put("userId", "");
            model.put("login", false);
            model.put("k6kt", -1);
        } else {
            model.put("userName", sessionValue.getUserName());
            model.put("userId", sessionValue.getId());
            model.put("login", true);
            model.put("k6kt", sessionValue.getK6kt());
            model.put("avatar", sessionValue.getMinAvatar());
        }
        String email = request.getParameter("email");
        String emailValidateCode = request.getParameter("emailValidateCode");
        model.put("email", email);
        model.put("code", emailValidateCode);
        return "/forum/emailValidate";
    }

    /**
     * 用户注册
     *
     * @return
     */
    @ApiOperation(value = "用户注册", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @SessionNeedless
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addUser(String cacheKeyId, String code, final String email, String userName, String passWord, String phoneNumber,
                                       @RequestParam(defaultValue = "", required = false) String nickName,
                                       @RequestParam(defaultValue = "-1",required = false) int newRole,
                                       HttpServletResponse response, HttpServletRequest request) {
        boolean flag = false;
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("code", 500);
        model.put("type", 1);

        if (nickName.length() > 20) {
            model.put("message", "昵称太长");
            return model;
        }
        if (!checkEmailUserNamePhoneNumber(email, userName, phoneNumber, model)) {
            return model;
        }
        if (!"".equals(cacheKeyId) && !"".equals(phoneNumber) && !"".equals(code)) {
            Validate validate = userService.validatePhoneNumberCode(phoneNumber, code, cacheKeyId);
            flag = validate.isOk();
            if (!flag) {
                model.put("message", validate.getMessage());
                return model;
            }
        } else if (!"".equals(email)) {
            flag = true;
        }
        if (flag) {
            final UserEntry userEntry = registerUserEntry(email, userName, passWord, phoneNumber, nickName);
            final ObjectId userId = userService.addUser(userEntry);
            if(newRole!=-1) {
                if(null==newVersionUserRoleDao.getEntry(userEntry.getID())){
                    newVersionUserRoleDao.saveEntry(new NewVersionUserRoleEntry(userId, newRole));
                }
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    initRegister(userId);
                }
            }).start();
            //发起激活
            if (StringUtils.isNotBlank(email)) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        processRegister(email, userEntry.getEmailValidateCode());
                    }
                }).start();
                model.put("code", 200);
                model.put("type", 2);
                model.put("message", userEntry.getEmail() + "$" + userEntry.getEmailValidateCode());
                return model;
            }
            model.put("userId",userId.toString());
            model.put("message", "注册成功");
            model.put("code", 200);
            // 登录
            login(userName, passWord, response, request);
        } else {
            model.put("message", "注册错误");
        }
        return model;
    }

    private UserEntry registerUserEntry(String email, String userName, String passWord, String phoneNumber, String nickName) {
        UserEntry userEntry = new UserEntry(userName, MD5Utils.getMD5String(passWord), phoneNumber, email, nickName);
        userEntry.setK6KT(0);
        userEntry.setIsRemove(0);
        userEntry.setStatisticTime(0L);
        userEntry.setRegisterIP(getIP());
        userEntry.setSilencedStatus(0);
        userEntry.setEmailStatus(0);
        if (StringUtils.isNotBlank(email)) {
            userEntry.setEmailValidateCode(new ObjectId().toString());
        } else {
            userEntry.setEmailValidateCode(Constant.EMPTY);
        }
        return userEntry;
    }

    private void initRegister(ObjectId userId) {
        //更新第一次访问Ip
        userService.updateInterviewIPValue(userId, getIP());
        //更新第一次访问时间
        userService.updateInterviewTimeValue(userId);
        //更新第一次退出时间
        userService.updateQuitTimeValue(userId);

        //判断是否为邀请的
        String trackId = CacheHandler.getStringValue("trackId");
        if (StringUtils.isNotBlank(trackId)) {
            userService.updateForumExperience(new ObjectId(trackId), 10);
            userService.updateForumScore(new ObjectId(trackId), 10);

            FScoreDTO fScoreDTO = new FScoreDTO();
            fScoreDTO.setTime(System.currentTimeMillis());
            fScoreDTO.setType(1);
            fScoreDTO.setOperation("邀请好友");
            fScoreDTO.setPersonId(trackId);
            fScoreDTO.setScoreOrigin("邀请好友用户奖励积分");
            fScoreDTO.setScore(10);
            fScoreService.addFScore(fScoreDTO);
            //邀请成功注册
            FInvitationEntry fInvitationEntry = fInvitationService.getFInvitation(new ObjectId(trackId));
            if (null == fInvitationEntry) {
                fInvitationService.saveOrUpdate(new ObjectId(trackId), userId, 1L);
            } else {
                fInvitationService.updateCount(new ObjectId(trackId), userId);
            }
        }

        //注册成功赠送1个积分和1个经验值
        userService.updateForumExperience(userId, 1);
        userService.updateForumScore(userId, 1);

        FScoreDTO fScoreDTO = new FScoreDTO();
        fScoreDTO.setTime(System.currentTimeMillis());
        fScoreDTO.setType(1);
        fScoreDTO.setOperation("注册");
        fScoreDTO.setPersonId(trackId);
        fScoreDTO.setScoreOrigin("注册用户获得积分奖励");
        fScoreDTO.setScore(1);
        fScoreService.addFScore(fScoreDTO);
        //发送系统消息
        fInformationService.addFInformation(new ObjectId("568dd46a0cf2316dfff62d81"), userId.toString(), 1, "", 0);
        userService.giveVoucher(userId);//发优惠券
    }

    /**
     *
     */
    @ApiOperation(value = "giveVoucher", httpMethod = "POST", produces = "application/json")
    @RequestMapping(value = "/giveVoucher")
    public void giveVoucher() throws Exception {
        List<EVoucherEntry> entries = new ArrayList<EVoucherEntry>();
        entries.add(getActivityEVouchers(getUserId(), 5000, "2016-09-10"));
        entries.add(getActivityEVouchers(getUserId(), 2500, "2016-10-10"));
        entries.add(getActivityEVouchers(getUserId(), 1500, "2016-10-10"));
        entries.add(getActivityEVouchers(getUserId(), 1000, "2016-10-10"));
        eBusinessVoucherService.addActivityEVoucher(entries);
    }

    @ApiOperation(value = "getChristReward", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/getChristReward")
    @ResponseBody
    public RespObj getChristReward() throws Exception {
        try {
            long startTime = getTime("2016-12-22 00:00:00");
            long endTime = getTime("2016-12-26 23:59:59");
            long deadTime = getTime("2016-12-31 23:59:59");
            long currentTime = System.currentTimeMillis();
            if (currentTime < startTime) {
                return RespObj.FAILD("该活动还没开始!");
            } else if (currentTime > endTime) {
                return RespObj.FAILD("该活动已经结束!");
            } else {
                ObjectId userId = getUserId();
                String key = "Christmas" + userId.toString();
                String value = RedisUtils.getString(key);
                if (StringUtils.isBlank(value)) {
                    //以三万作为基数，5元40%、10元30%、20元20%、50元10%
                    String num = String.valueOf(System.currentTimeMillis());
                    num += RandomUtils.nextInt(Constant.MIN_PASSWORD);
                    int random = 1 + (int) (Math.random() * 30000);
                    int voucher;
                    EVoucherEntry eVoucherEntry;
                    if (30000 * 0.7 < random && random <= 30000 * 0.8) {
                        eVoucherEntry = new EVoucherEntry(userId, num, 5000, deadTime, 0, 1);
                        voucher = 50;
                    } else if (30000 * 0.4 < random && random <= 30000 * 0.7) {
                        eVoucherEntry = new EVoucherEntry(userId, num, 1000, deadTime, 0, 1);
                        voucher = 10;
                    } else if (30000 * 0.1 < random && random <= 30000 * 0.3) {
                        eVoucherEntry = new EVoucherEntry(userId, num, 2000, deadTime, 0, 1);
                        voucher = 20;
                    } else {
                        eVoucherEntry = new EVoucherEntry(userId, num, 500, deadTime, 0, 1);
                        voucher = 5;
                    }
                    ObjectId voucherId = eBusinessVoucherService.addEVoucher(eVoucherEntry);
                    RedisUtils.cacheString(key, voucherId.toString(), Constant.SECONDS_IN_FIVE_DAY);
                    return RespObj.SUCCESS(voucher);
                } else {
                    return RespObj.FAILD("你已经获取到优惠券了，<br/>每个人只有一次机会哦!");
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public long getTime(String strTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date endDate = sdf.parse(strTime);
        return endDate.getTime();
    }


    private EVoucherEntry getActivityEVouchers(ObjectId userId, int amount, String time) throws Exception {
        String num = String.valueOf(System.currentTimeMillis());
        num += RandomUtils.nextInt(Constant.MIN_PASSWORD);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date t = sdf.parse(time);
        return new EVoucherEntry(userId, num, amount, t.getTime(), 0, 1);
    }


    private Boolean checkEmailUserNamePhoneNumber(String email, String userName, String phoneNumber, Map<String, Object> map) {
        Pattern emailPattern = Pattern.compile("^.+@.+\\..+$");
        if (!"".equals(email) && !emailPattern.matcher(email).matches()) {
            map.put("message", "注册失败：邮箱格式不正确");
            return false;
        }
        Pattern phonePattern = Pattern.compile("^1[3|4|5|6|7|8|9][0-9]\\d{8}$");
        if (!"".equals(phoneNumber) && !phonePattern.matcher(phoneNumber).matches()) {
            map.put("message", "注册失败：手机号不正确");
            return false;
        }
        if (!"".equals(email) && (Integer) checkUser(email, "", "").get("isExit") == 1) {
            map.put("message", "注册失败：邮箱已被使用");
            return false;
        }
        if (!"".equals(phoneNumber) && (Integer) checkUser("", phoneNumber, "").get("isExit") == 1) {
            map.put("message", "注册失败：手机号已被使用");
            return false;
        }
        if ((Integer) checkUser("", "", userName).get("isExit") == 1) {
            map.put("message", "注册失败：用户名已被使用");
            return false;
        }
        return true;
    }

    /**
     * 登录
     *
     * @param account  用户名/邮箱/手机号
     * @param password
     * @param response
     * @return
     */
    @ApiOperation(value = "登录", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @SessionNeedless
    @RequestMapping("/login")
    @ResponseBody
    public RespObj login(String account, String password, HttpServletResponse response, HttpServletRequest request) {
        RespObj respObj = RespObj.FAILD;
        UserEntry userEntry = userService.getUserByAccount(account);
        if (userEntry == null) {
            respObj.setMessage("accountError");
        } else {
            String email = userEntry.getEmail();
            if (StringUtils.isNotBlank(email)) {
                if (userEntry.getEmailStatus() == 0) {
                    respObj.setMessage("邮箱未激活");
                    return respObj;
                }
            }
            respObj = userController.login(userEntry.getUserName(), password,1, response, request);
        }

        return respObj;

    }

}
