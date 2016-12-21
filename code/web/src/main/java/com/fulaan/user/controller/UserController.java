package com.fulaan.user.controller;

import com.easemob.server.EaseMobAPI;
import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.annotation.UserRoles;
import com.fulaan.cache.CacheHandler;
import com.fulaan.base.BaseController;
import com.fulaan.forum.service.FLogService;
import com.fulaan.forum.service.FScoreService;
import com.fulaan.friendscircle.service.FriendService;
import com.fulaan.log.service.LogService;
import com.fulaan.playmate.service.MateService;
import com.fulaan.pojo.FLoginLog;
import com.fulaan.pojo.User;
import com.fulaan.school.SchoolService;
import com.fulaan.user.model.ThirdLoginEntry;
import com.fulaan.user.model.ThirdType;
import com.fulaan.user.service.UserService;
import com.fulaan.user.util.MapUtil;
import com.fulaan.user.util.QQLoginUtil;
import com.fulaan.user.util.WeChatLoginUtil;
import com.fulaan.util.Validator;
import com.fulaan.utils.CollectionUtil;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.pojo.app.IdValuePairDTO;
import com.pojo.app.Platform;
import com.pojo.app.RegionEntry;
import com.pojo.app.SessionValue;
import com.pojo.educationbureau.EducationBureauEntry;
import com.pojo.forum.FLogDTO;
import com.pojo.forum.FScoreDTO;
import com.pojo.log.LogType;
import com.pojo.school.ClassInfoDTO;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.pojo.utils.LoginLog;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.exceptions.UnLoginException;
import com.sys.mails.MailUtils;
import com.sys.props.Resources;
import com.sys.utils.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.Collator;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 用户controller,处理用户请求，比如登录
 *
 * @author fourer
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    private static final Logger logger = Logger.getLogger(UserController.class);
    private static final Logger loginLogger = Logger.getLogger("LOGIN");
    private final static String cas_url = Resources.getProperty("ssoservice.cas.url", "http://ah.sso.cycore.cn/sso");
    private final static String service_url = Resources.getProperty("ssoservice.service.url", "http://www.k6kt.com/user/sso/login.do");
    private final static String cas_login_url = Resources.getProperty("ssoservice.cas.login.url", "http://ah.sso.cycore.cn/sso/login");
    //找回密码邮箱链接
    private final static String linkFormat = Resources.getProperty("domain", "http://www.k6kt.com") + "/user/email/callback.do?ukId={0}&email={1}&token={2}";
    //k6kt单点登录URl
    private final static String K6KT_SSO_URL = "http://www.mysso.com";

    @Autowired
    private UserService userService;
    @Autowired
    private SchoolService schoolService;
    @Resource
    private FriendService friendService;
    @Autowired
    private LogService logService;
    @Autowired
    private FLogService fLogService;
    @Autowired
    private FScoreService fScoreService;
    @Autowired
    private MateService mateService;

    /**
     * 通过sso登录
     *
     * @param response
     * @param request
     * @throws IOException
     */
    @SessionNeedless
    @RequestMapping("/k6kt/sso/login")
    public void k6ktssoLogin(HttpServletResponse response, HttpServletRequest request) throws IOException {
        String token = request.getParameter("token");
        String cookieValue = request.getParameter("cValue");

        logger.info("k6kt.sso.login token=" + token);
        logger.info("k6kt.sso.login cookieValue=" + cookieValue);

        SessionValue value = new SessionValue();

        boolean isLogin = false;
        //放入缓存
        String cacheUserKey = new ObjectId().toString();
        if (StringUtils.isBlank(cookieValue)) {
            String uid = HttpClientUtils.get(K6KT_SSO_URL + "/sso/simUserInfo.do?ssoKey=" + request.getParameter("token"));
            UserEntry e = userService.findByUserId(new ObjectId(uid));
            //处理SessionValue
            value.setId(e.getID().toString());
            value.setUserName(e.getUserName());
            value.setRealName(e.getNickName());
            value.setK6kt(e.getK6KT());
            isLogin = true;
        } else {
            value = CacheHandler.getSessionValue(cookieValue);
            if (null != value && !value.isEmpty()) {
                cacheUserKey = cookieValue;
                isLogin = true;
            }
        }

        if (isLogin) {
            String ip = getIP();
            String ipKey = CacheHandler.getKeyString(CacheHandler.CACHE_USER_KEY_IP, cacheUserKey.toString());
            CacheHandler.cache(ipKey, ip, Constant.SECONDS_IN_DAY);
            //ck_key
            CacheHandler.cacheUserKey(value.getId(), cacheUserKey, Constant.SECONDS_IN_DAY);
            //s_key
            CacheHandler.cacheSessionValue(cacheUserKey.toString(), value, Constant.SECONDS_IN_DAY);
            //处理cookie
            Cookie userKeycookie = new Cookie(Constant.COOKIE_USER_KEY, cacheUserKey.toString());
            userKeycookie.setMaxAge(Constant.SECONDS_IN_DAY);
            userKeycookie.setPath(Constant.BASE_PATH);
            response.addCookie(userKeycookie);

            try {
                Cookie nameCookie = new Cookie(Constant.COOKIE_USERNAME_KEY, URLEncoder.encode(value.getUserName(), Constant.UTF_8));
                nameCookie.setMaxAge(Constant.SECONDS_IN_MONTH);
                nameCookie.setPath(Constant.BASE_PATH);
                response.addCookie(nameCookie);
            } catch (UnsupportedEncodingException e1) {
                logger.error("", e1);
            }
        }

        response.setContentType("text/plain");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        PrintWriter out = response.getWriter();
        Gson g = new Gson();
        String jsonpCallback = request.getParameter("jsonpCallback");//客户端请求参数

        if (isLogin) {
            RespObj obj = new RespObj(Constant.SUCCESS_CODE, "fulaan");
            out.println(jsonpCallback + "(" + g.toJson(obj) + ")");//返回jsonp格式数据
        } else {
            out.println(jsonpCallback + "(" + g.toJson(RespObj.FAILD) + ")");//返回jsonp格式数据
        }
        out.flush();
        out.close();
    }


    /**
     * 用户登录
     *
     * @param name
     * @param pwd
     * @return
     */
    @SessionNeedless
    @RequestMapping("/login")
    @ResponseBody
    public RespObj login(String name, String pwd, HttpServletResponse response) {
        RespObj faild = new RespObj(Constant.FAILD_CODE);
        String ip = getIP();

        //判断用户登录平台，只对PC登录用户进行密码输入错误3次，需要验证码
        String client = getRequest().getHeader("User-Agent");
        Platform pf = null;
        if (client.contains("iOS")) {
            pf = Platform.IOS;
        } else if (client.contains("Android")) {
            pf = Platform.Android;
        } else {
            pf = Platform.PC;
        }

        logger.info("try login;the name=" + name + ";pwd=" + pwd);

        //数据库验证
        UserEntry e = userService.login(name);
        if (null == e) {
            faild.setMessage("accountError");
            return faild;
        } else {
            String emailValidateCode = e.getEmailValidateCode();
            if (StringUtils.isNotBlank(emailValidateCode)) {
                if (e.getEmailStatus() == 0) {
                    faild.setMessage("邮箱未被激活！");
                    return faild;
                }
            }
        }

        //登录日志
        long time = System.currentTimeMillis();
        FLoginLog log = new FLoginLog(e.getUserName(), e.getNickName(), time, getIP(), getPlatform());
        fLogService.loginLog(log);

        if (!ValidationUtils.isRequestPassword(pwd) || (!e.getPassword().equalsIgnoreCase(MD5Utils.getMD5String(pwd)) && !e.getPassword().equalsIgnoreCase(pwd))) {

            if (!ValidationUtils.isRequestPassword(pwd)) {
                faild.setMessage("密码非法");
                return faild;
            }
            if (!e.getPassword().equalsIgnoreCase(MD5Utils.getMD5String(pwd)) && !e.getPassword().equalsIgnoreCase(pwd)) {
                faild.setMessage("密码错误");
                return faild;
            }
        }

        try {
            if (0l == e.getLastActiveDate()) {
                String params = e.getID().toString() + 0l;
                String flkey = CacheHandler.getKeyString(CacheHandler.CACHE_USER_FIRST_LOGIN, params);
                CacheHandler.cache(flkey, Constant.USER_FIRST_LOGIN, Constant.SESSION_FIVE_MINUTE);
            }
            //更新最后登录日期
            userService.updateLastActiveDate(e.getID());

            //登录日志
            FLogDTO fLogDTO = new FLogDTO();
            fLogDTO.setActionName("login");
            fLogDTO.setPersonId(e.getID().toString());
            fLogDTO.setPath("/user/login.do");
            fLogDTO.setTime(System.currentTimeMillis());
            fLogService.addFLog(fLogDTO);

            //更新本次活动时间
            userService.updateInterviewTimeValue(e.getID());
        } catch (Exception ex) {
            logger.error("", ex);
        }
        if (Constant.ONE == e.getUserType()) //有效时间用户
        {
            long validBeginTime = e.getValidBeginTime();
            long validTime = e.getValidTime();
            if (0L == validBeginTime) //第一次登陆
            {
                try {
                    userService.update(e.getID(), "vabt", System.currentTimeMillis());
                } catch (IllegalParamException e1) {

                }
            } else {
                if (System.currentTimeMillis() > validBeginTime + validTime * 1000) {
                    faild.setMessage("该用户已经失效");
                    return faild;
                }
            }
        }


        SchoolEntry schoolEntry = null;
        try {
            schoolEntry = schoolService.getSchoolEntryByUserId(e.getID());
        } catch (IllegalParamException ie) {
            logger.error("Can not find school for user:" + e);
        }

        //处理SessionValue
        SessionValue value = new SessionValue();
        value.setId(e.getID().toString());
        value.setUserName(e.getUserName());
        value.setRealName(e.getNickName());
        value.setAvatar(e.getAvatar());
        value.setK6kt(e.getK6KT());
        value.setUserRole(e.getRole());
        if (e.getK6KT() == 1) {//k6kt用户
            value.setSchoolId(e.getSchoolID().toString());
            if (schoolEntry != null && schoolEntry.getLogo() != null) {
                value.setSchoolLogo(schoolEntry.getLogo());
            }
            value.setUserRole(e.getRole());
            value.setAvatar(e.getAvatar());
            value.setUserPermission(e.getPermission());
            value.setUserRemovePermission(e.getRemovePermission());
            value.setExperience(e.getExperiencevalue());
            value.setChatid(e.getChatId());
            value.setSchoolNavs(schoolEntry.getSchoolNavs());
            value.setSchoolName(schoolEntry.getName());
        }
        //放入缓存
        ObjectId cacheUserKey = new ObjectId();

        String ipKey = CacheHandler.getKeyString(CacheHandler.CACHE_USER_KEY_IP, cacheUserKey.toString());
        CacheHandler.cache(ipKey, ip, Constant.SECONDS_IN_DAY);
        //ck_key
        CacheHandler.cacheUserKey(e.getID().toString(), cacheUserKey.toString(), Constant.SECONDS_IN_DAY);
        //s_key
        CacheHandler.cacheSessionValue(cacheUserKey.toString(), value, Constant.SECONDS_IN_DAY);
        //处理cookie
        Cookie userKeycookie = new Cookie(Constant.COOKIE_USER_KEY, cacheUserKey.toString());
        userKeycookie.setMaxAge(Constant.SECONDS_IN_DAY);
        userKeycookie.setPath(Constant.BASE_PATH);
        response.addCookie(userKeycookie);

        try {
            Cookie nameCookie = new Cookie(Constant.COOKIE_USERNAME_KEY, URLEncoder.encode(name, Constant.UTF_8));
            nameCookie.setMaxAge(Constant.SECONDS_IN_MONTH);
            nameCookie.setPath(Constant.BASE_PATH);
            response.addCookie(nameCookie);
        } catch (UnsupportedEncodingException e1) {
            logger.error("", e1);
        }
        try {
            LoginLog loginLog = new LoginLog();
            loginLog.setIpAddr(ip + e.getUserName());
            loginLog.setPlatform(pf.getName());
            loginLog.setUserId(e.getID().toString());
            loginLog.setUserName(e.getUserName());
            if (null != schoolEntry) {
                RegionEntry region = schoolService.getRegionEntry(schoolEntry.getRegionId());
                //获取客户端信息
                if (e.getK6KT() == 1) {//k6kt用户
                    loginLog.setRole(e.getRole());
                    loginLog.setSchoolId(schoolEntry.getID().toString());
                    loginLog.setSchoolName(schoolEntry.getName());
                    loginLog.setCity(region.getName());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        respObj.setMessage(value);

        //检查是否注册环信
        boolean isRegister = e.isRegisterHuanXin();
        String nickName = StringUtils.isNotBlank(e.getNickName()) ? e.getNickName() : e.getUserName();
        if (!isRegister) {
            EaseMobAPI.createUser(e.getID().toString(), nickName);
        }

        if (StringUtils.isBlank(e.getNickName())) {
            userService.updateNickNameAndSexById(e.getID().toString(), e.getUserName(), e.getSex());
        }
        userService.updateHuanXinTag(e.getID());

        //找玩伴
        boolean isMateExist = mateService.isMateRecoreExist(e.getID());
        if (!isMateExist) {
            mateService.saveMateEntry(e.getID());
        }
        mateService.updateAged(e.getID(), e.getBirthDate());
        return respObj;
    }


    /**
     * 用户登录
     *
     * @param name
     * @param pwd
     * @return
     */
    @SessionNeedless
    @RequestMapping("/third/login")
    @ResponseBody
    public RespObj login(String name, String pwd, String cacheThirdId, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("name", name);
        redirectAttributes.addFlashAttribute("pwd", pwd);
        RespObj respObj = login(name, pwd, response);
        if (respObj.getCode().equals("200")) {
            SessionValue sessionValue = CacheHandler.getSessionValue(cacheThirdId);
            String openId = sessionValue.get("openId");
            //保存第三方登录数据
            String unionid = sessionValue.get("unionId");
            int type = Integer.parseInt(sessionValue.get("type"));
            SessionValue userSession = (SessionValue) respObj.getMessage();
            ObjectId userId = new ObjectId(userSession.getId());
            ThirdLoginEntry thirdLoginEntry = new ThirdLoginEntry(userId, openId, unionid, ThirdType.getThirdType(type));
            userService.saveThidLogininfo(thirdLoginEntry);
        }

        return respObj;
    }


    @SessionNeedless
    @RequestMapping("/third/create")
    @ResponseBody
    public RespObj create(String cacheThirdId, HttpServletResponse response) {
        UserEntry userEntry;
        SessionValue sessionValue = CacheHandler.getSessionValue(cacheThirdId);
        String openId = sessionValue.get("openId");
        String unionId = sessionValue.get("unionId");
        String nickName = sessionValue.get("nickName");
        String avatar = sessionValue.get("avatar");
        int sex = 1;
        int type = Integer.parseInt(sessionValue.get("type"));
        UserEntry userEntry1 = userService.findByUserName(nickName);
        String userName = nickName;
        if (userEntry1 == null) {
            userEntry = new UserEntry(userName, userName, "*", avatar, sex);
        } else {

            String uuidName = new ObjectId().toString();
            userName = userName + uuidName;
            userEntry = new UserEntry(userName, userName, "*", avatar, sex);
        }

        //检查是否注册环信
        boolean isRegister = userEntry.isRegisterHuanXin();
        String nickName2 = StringUtils.isNotBlank(userEntry.getNickName()) ? userEntry.getNickName() : userEntry.getUserName();
        if (!isRegister) {
            EaseMobAPI.createUser(userEntry.getID().toString(), nickName2);
        }

        //找玩伴
        boolean isMateExist = mateService.isMateRecoreExist(userEntry.getID());
        if (!isMateExist) {
            mateService.saveMateEntry(userEntry.getID());
        }

        userService.addUser(userEntry);
        userEntry = userService.findByUserName(userName);
        //保存第三方登录数据
        ThirdLoginEntry thirdLoginEntry = new ThirdLoginEntry(userEntry.getID(), openId, unionId, ThirdType.getThirdType(type));
        userService.saveThidLogininfo(thirdLoginEntry);
        return login(userEntry.getUserName(), userEntry.getPassword(), response);
    }


    @SessionNeedless
    @RequestMapping("/cloud/login")
    public String loginForCloudPlatform(String key, String pf, @RequestParam(required = false, defaultValue = "0") Integer tag, HttpServletRequest req, HttpServletResponse response, HttpServletRequest request) throws UnLoginException, ClientProtocolException, IOException {
        String ip = getIP();
        logger.info("cloud try login;key=" + key + "&pf=" + pf);
        if (StringUtils.isBlank(key) || StringUtils.isBlank(pf)) {
            logger.info("UnLoginException");
            throw new UnLoginException();
        }
        //todo 判断pf是否是合法的云平台
        String id = key;
        if (StringUtils.isBlank(id)) {
            logger.info("UnLoginException");
            throw new UnLoginException();
        }
        //数据库验证
        UserEntry e = userService.findByUserId(new ObjectId(id));
        if (null == e) {
            logger.info("UnLoginException");
            throw new UnLoginException();
        }

        try {
            //更新最后登录日期
            userService.updateLastActiveDate(e.getID());
        } catch (Exception ex) {
            logger.error("", ex);
        }


        if (Constant.ONE == e.getUserType()) //有效时间用户
        {
            long validBeginTime = e.getValidBeginTime();
            long validTime = e.getValidTime();
            if (0L == validBeginTime) //第一次登陆
            {
                try {
                    userService.update(e.getID(), "vabt", System.currentTimeMillis());
                } catch (IllegalParamException e1) {

                }
                ;
            } else {
                if (System.currentTimeMillis() > validBeginTime + validTime * 1000) {
                    throw new UnLoginException();
                }
            }
        }
        SchoolEntry schoolEntry = null;
        EducationBureauEntry eduEntry = null;
        try {
            String schoolId = e.getSchoolID() == null ? "" : e.getSchoolID().toString();
            schoolEntry = schoolService.getSchoolEntryByUserId(e.getID());
        } catch (IllegalParamException ie) {
            logger.error("Can not find school for user:" + e);
        }
        String url = request.getHeader("Referer");
        String cloud_url = url == null ? "http://yun.k6kt.com/" : url.substring(0, url.lastIndexOf("com/") + 4);
        //处理SessionValue
        SessionValue value = new SessionValue();
        value.setCloudUrl(cloud_url);
        value.setId(e.getID().toString());
        value.setSchoolId(e.getSchoolID().toString());
        if (schoolEntry != null && schoolEntry.getLogo() != null) {
            value.setSchoolName(schoolEntry.getName());
            value.setSchoolLogo(schoolEntry.getLogo());
        }
        value.setEducationLogo("/upload/img_cloud/default/default-logo.png");
        if (eduEntry != null) {
            if (eduEntry.getEducationLogo() != null && !"".equals(eduEntry.getEducationLogo())) {
                value.setEducationLogo(eduEntry.getEducationLogo());
            }
        }
        value.setUserName(e.getUserName());
        value.setRealName(e.getNickName());
        value.setUserRole(e.getRole());
        value.setAvatar(e.getAvatar());
        value.setUserPermission(e.getPermission());
        value.setUserRemovePermission(e.getRemovePermission());
        value.setExperience(e.getExperiencevalue());
        value.setChatid(e.getChatId());
        value.setSchoolNavs(schoolEntry.getSchoolNavs());
        //放入缓存
        ObjectId cacheUserKey = new ObjectId();
        //ck_key
        CacheHandler.cacheUserKey(e.getID().toString(), cacheUserKey.toString(), Constant.SECONDS_IN_DAY);
        //s_key
        CacheHandler.cacheSessionValue(cacheUserKey.toString(), value, Constant.SECONDS_IN_DAY);
        //处理cookie
        Cookie userKeycookie = new Cookie(Constant.COOKIE_USER_KEY, cacheUserKey.toString());
        userKeycookie.setMaxAge(Constant.SECONDS_IN_DAY);
        userKeycookie.setPath(Constant.BASE_PATH);
        response.addCookie(userKeycookie);


        try {
            Cookie nameCookie = new Cookie(Constant.COOKIE_USERNAME_KEY, URLEncoder.encode(e.getUserName(), Constant.UTF_8));
            nameCookie.setMaxAge(Constant.SECONDS_IN_MONTH);
            nameCookie.setPath(Constant.BASE_PATH);
            response.addCookie(nameCookie);
        } catch (UnsupportedEncodingException e1) {
            logger.error("", e1);
        }
        try {
            RegionEntry region = schoolService.getRegionEntry(schoolEntry.getRegionId());
            //获取客户端信息
            LoginLog loginLog = new LoginLog();
            loginLog.setIpAddr(ip + e.getUserName());
            loginLog.setPlatform(getPlatform().getName());
            loginLog.setRole(e.getRole());
            loginLog.setUserId(e.getID().toString());
            loginLog.setUserName(e.getUserName());
            loginLog.setSchoolId(schoolEntry.getID().toString());
            loginLog.setSchoolName(schoolEntry.getName());
            loginLog.setCity(region.getName());
            loginLogger.info(loginLog);
            logService.insertLog(e, getPlatform(), LogType.CLICK_LOGIN, "login.do");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (tag == 1) {
            return "redirect:/microlesson/micropage.do?version=1b&a=10000";
        }
        if (tag == 2) {
            return "redirect:/score/teacher.do?version=17&a=10000";
        }
        if (tag == 3) {
            return "redirect:/registration/list.do?version=5g&a=10000";
        }
        if (tag == 4) {
            return "redirect:/docflow/documentList.do?type=0&version=51&a=10000";
        }
        if (tag == 5) {
            return "redirect:/manageCount/schooltotal.do?version=88&schoolid=" + schoolEntry.getID().toString() + "&a=10000";
        }

        return "redirect:/user/homepage.do";
    }

    @SessionNeedless
    @RequestMapping("/logout")
    @ResponseBody
    public RespObj logout(HttpServletRequest request) {
        SessionValue sv = getSessionValue();
        if (null != sv) {
            //更新退出时间
            userService.updateQuitTimeValue(new ObjectId(sv.getId()));
            //更新上次访问Ip
            userService.updateInterviewIPValue(new ObjectId(sv.getId()), getIP());
            //更新在线时间
            userService.updateStatisticTimeValue(new ObjectId(sv.getId()));

            String yearMonth = DateTimeUtils.convert(System.currentTimeMillis(), DateTimeUtils.DATE_YYYY_MM);
            CacheHandler.deleteKey(CacheHandler.CACHE_USER_CALENDAR, sv.getId(), yearMonth);

            logger.info("try loginout;the ui=" + sv.getId());
            logger.info("delete session value for user:" + sv.getId());

            Cookie cookies[] = request.getCookies();
            Cookie c = null;
            for (int i = 0; i < cookies.length; i++) {
                c = cookies[i];
                c.setMaxAge(0);
                if (c.getName().equals(Constant.COOKIE_USER_KEY)) {
                    CacheHandler.deleteKey(CacheHandler.CACHE_SESSION_KEY,
                            c.getValue());
                }
            }
        }

        return RespObj.SUCCESS;
    }

    /**
     * 得到用户基本信息
     *
     * @return
     */
    @RequestMapping("/info")
    @ResponseBody
    public UserDetailInfoDTO getUserDetailInfoDTO() {
        try {

            UserDetailInfoDTO userInfoDTO4WB = userService.getUserInfoById(getSessionValue().getId());
            if (StringUtils.isNotBlank(getSessionValue().getSchoolId())) {
                SchoolEntry se = schoolService.getSchoolEntry(new ObjectId(getSessionValue().getSchoolId()), Constant.FIELDS);
                if (null != se) {
                    userInfoDTO4WB.setSchoolName(se.getName());
                    userInfoDTO4WB.setSchoolLogo(se.getLogo());
                }
            }
            return userInfoDTO4WB;
        } catch (Exception ex) {
            logger.error("", ex);
        }
        return new UserDetailInfoDTO();
    }


    /**
     * @return
     */
    @UserRoles(noValue = {UserRole.STUDENT, UserRole.PARENT})
    @RequestMapping("/school/teacher")
    @ResponseBody
    public List<IdValuePairDTO> getSchoolTeacher() {
        List<IdValuePairDTO> retList = new ArrayList<IdValuePairDTO>();
        List<UserEntry> list = userService.getTeacherEntryBySchoolId(new ObjectId(getSessionValue().getSchoolId()), new BasicDBObject("nm", 1));
        for (UserEntry e : list) {
            IdValuePairDTO dto = new IdValuePairDTO(e.getID(), e.getUserName());
            retList.add(dto);
        }
        return retList;
    }


    @SuppressWarnings("unchecked")
    @RequestMapping("getAddressBookPc")
    @ResponseBody
    public Map<String, Object> getAddressBookPc() {
        Map<String, Object> model = new HashMap<String, Object>();
        List<UserDetailInfoDTO> userList = new ArrayList<UserDetailInfoDTO>();
        List<UserDetailInfoDTO> studentsList = new ArrayList<UserDetailInfoDTO>();
        List<UserDetailInfoDTO> parentsList = new ArrayList<UserDetailInfoDTO>();
        SessionValue sv = getSessionValue();
        ObjectId uId = new ObjectId(sv.getId());
        if (UserRole.isStudentOrParent(sv.getUserRole())) {
            ObjectId userid = null;
            if (UserRole.isStudent(sv.getUserRole())) {
                userid = uId;
            } else if (UserRole.isParent(sv.getUserRole())) {
                UserEntry user = userService.findByUserId(new ObjectId(getSessionValue().getId()));
                userid = user.getConnectIds().get(0);
            }

            Set<ObjectId> userIdSet = new HashSet<ObjectId>();
            if (studentsList != null && studentsList.size() != 0) {
                for (UserDetailInfoDTO userdetail : studentsList) {
                    if (userdetail.getConnectIds() != null && userdetail.getConnectIds().size() != 0) {
                        for (String connectId : userdetail.getConnectIds()) {
                            userIdSet.add(new ObjectId(connectId));
                        }
                    }
                }
            }

            List<UserDetailInfoDTO> teachersList = new ArrayList<UserDetailInfoDTO>();
            Set<ObjectId> totalUserIdSet = new HashSet<ObjectId>(userIdSet);

            Map<ObjectId, UserEntry> userMap = userService.getUserEntryMap(totalUserIdSet, new BasicDBObject("nm", 1).append("nnm", 1).append("r", 1));

            for (Map.Entry<ObjectId, UserEntry> entry : userMap.entrySet()) {
                try {
                    if (userIdSet.contains(entry.getKey())) {
                        parentsList.add(new UserDetailInfoDTO(entry.getValue()));
                    } else {
                        teachersList.add(new UserDetailInfoDTO(entry.getValue()));
                    }
                } catch (Exception ex) {
                }
            }

            parentsList = sortList(parentsList);
            model.put("parentsList", parentsList);

            List<UserDetailInfoDTO> presidentList = userService.getUserInfoBySchoolid(new ObjectId(sv.getSchoolId()));
            if (UserRole.isStudent(sv.getUserRole())) {
                studentsList = sortList(studentsList);
                model.put("studentsList", studentsList);
                userList.addAll(studentsList);
            }
            teachersList = sortList(teachersList);
            presidentList = sortList(presidentList);
            model.put("teachersList", teachersList);
            model.put("presidentList", presidentList);
        } else if (UserRole.isHeadmaster(sv.getUserRole())) {

            List<UserDetailInfoDTO> userInfoDTOList = userService.findUserBySchoolId2(sv.getSchoolId());
            List<UserDetailInfoDTO> teachersList = new ArrayList<UserDetailInfoDTO>();
            List<UserDetailInfoDTO> presidentList = new ArrayList<UserDetailInfoDTO>();

            for (UserDetailInfoDTO userinfo : userInfoDTOList) {
                if (UserRole.isTeacher(userinfo.getRole()) || UserRole.isManager(userinfo.getRole())) {
                    teachersList.add(userinfo);
                }
                if (UserRole.isHeadmaster(userinfo.getRole())) {
                    presidentList.add(userinfo);
                }
                if (UserRole.isLeaderClass(userinfo.getRole())) {
                    teachersList.add(userinfo);
                }
                if (UserRole.isLeaderOfGrade(userinfo.getRole())) {
                    teachersList.add(userinfo);
                }
                if (UserRole.isLeaderOfSubject(userinfo.getRole())) {
                    teachersList.add(userinfo);
                }
                if (UserRole.isStudent(userinfo.getRole())) {
                    studentsList.add(userinfo);
                }
                if (UserRole.isParent(userinfo.getRole())) {
                    parentsList.add(userinfo);
                }
            }
            for (UserDetailInfoDTO userInfo : presidentList) {
                if (uId.toString().equals(userInfo.getId())) {
                    parentsList.remove(userInfo);
                    break;
                }
            }
            List<UserDetailInfoDTO> bureauList = new ArrayList<UserDetailInfoDTO>();
            presidentList = sortList(presidentList);
            bureauList = sortList(bureauList);
            teachersList = sortList(teachersList);
            studentsList = sortList(studentsList);
            parentsList = sortList(parentsList);
            model.put("presidentList", presidentList);
            model.put("bureauList", bureauList);
            model.put("teachersList", teachersList);
            model.put("studentsList", studentsList);
            model.put("parentsList", parentsList);
            userList.addAll(presidentList);
            userList.addAll(teachersList);
        } else if (UserRole.isTeacher(sv.getUserRole())) {

            Set<ObjectId> userIdSet = new HashSet<ObjectId>();

            if (studentsList.size() != 0) {
                for (UserDetailInfoDTO userdetail : studentsList) {
                    if (userdetail.getRelationId() != null) {
                        userIdSet.add(new ObjectId(userdetail.getRelationId()));
                    }
                }
            }
            Map<ObjectId, UserEntry> userMap = userService.getUserEntryMap(userIdSet, new BasicDBObject("nm", 1).append("nnm", 1).append("r", 1));


            for (Map.Entry<ObjectId, UserEntry> entry : userMap.entrySet()) {
                try {
                    {
                        parentsList.add(new UserDetailInfoDTO(entry.getValue()));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            studentsList = sortList(studentsList);
            parentsList = sortList(parentsList);
            model.put("studentsList", studentsList);
            model.put("parentsList", parentsList);
            List<UserDetailInfoDTO> presidentList = (List<UserDetailInfoDTO>) model.get("presidentList");
            userList.addAll(sortList(presidentList));
            List<UserDetailInfoDTO> teacherList = (List<UserDetailInfoDTO>) model.get("teachersList");
            userList.addAll(sortList(teacherList));

        }
        List<UserDetailInfoDTO> friendList = new ArrayList<UserDetailInfoDTO>();
        friendList = friendService.selectAllFriend(uId.toString());
        friendList = sortList(friendList);
        model.put("friendList", friendList);
        userList.addAll(friendList);
        try {
            model.put("userList", (List<UserDetailInfoDTO>) CollectionUtil.removeDuplicateWithOrder(userList));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    /**
     * 好友根据名字排序
     * add by miaoqiang
     *
     * @param list
     * @return
     */
    private List<UserDetailInfoDTO> sortList(List<UserDetailInfoDTO> list) {

        Collections.sort(list, new Comparator<UserDetailInfoDTO>() {
            public int compare(UserDetailInfoDTO arg0, UserDetailInfoDTO arg1) {
                Collator cmp = Collator.getInstance(java.util.Locale.CHINA);
                if (cmp.compare(arg0.getUserName(), arg1.getUserName()) > 0) {
                    return 1;
                } else if (cmp.compare(arg0.getUserName(), arg1.getUserName()) < 0) {
                    return -1;
                }
                return 0;
            }
        });
        return list;
    }

    /**
     * 得到用户所在的班级
     *
     * @return
     */
    @RequestMapping("classes")
    @ResponseBody
    public List<ClassInfoDTO> getUserClasses() {
        return userService.getClassDTOList(getUserId(), getSessionValue().getUserRole());
    }


    /**
     * 更新用户基本信息
     *
     * @param userLoginName
     * @param mobile
     * @param valiCode
     * @param cacheKeyId
     * @param email
     * @param sex
     * @return
     * @throws IllegalParamException
     */
    @RequestMapping("/update/basic")
    @ResponseBody
    public RespObj updateUserBasicInfos(@RequestParam(defaultValue = "") String userLoginName, @RequestParam(defaultValue = "") String mobile, String valiCode, String cacheKeyId, String email, Integer sex) throws IllegalParamException {

        UserEntry ue = userService.findByUserId(getUserId());
        RespObj ret = new RespObj(Constant.FAILD_CODE);

        UserEntry e;
        if (!ue.getLoginName().equals(userLoginName)) {
            if (StringUtils.isNotBlank(userLoginName)) {
                if (ValidationUtils.isNumber(userLoginName)) {
                    ret.setMessage("登录名不能是数字");
                    return ret;
                }
                if (ValidationUtils.isEmail(userLoginName)) {
                    ret.setMessage("登录名不能是邮箱");
                    return ret;
                }

                e = userService.findByUserName(userLoginName);
                if (null != e) {
                    ret.setMessage("名字重复");
                    return ret;
                }

                userService.update(ue.getID(), "logn", userLoginName.toLowerCase());

            }
        }


        if (!ue.getMobileNumber().equals(mobile)) {
            if (StringUtils.isNotBlank(mobile)) {
                if (!ValidationUtils.isRequestModile(mobile)) {
                    ret.setMessage("手机错误");
                    return ret;
                }

                e = userService.findByUserPhone(mobile);
                if (null != e) {
                    ret.setMessage("手机号码被占用");
                    return ret;
                }

                String cacheKey = CacheHandler.getKeyString(CacheHandler.CACHE_SHORTMESSAGE, cacheKeyId);
                String value = CacheHandler.getStringValue(cacheKey);
                if (StringUtils.isBlank(value)) {
                    ret.setMessage("验证码失效，请重新获取");
                    return ret;
                }

                String[] cache = value.split(",");
                if (!cache[0].equals(valiCode)) {
                    ret.setMessage("注册失败：手机号码与验证码不匹配");
                    return ret;
                }
                userService.update(ue.getID(), "mn", mobile.toLowerCase());
            }
        }
        if (!ue.getEmail().equals(email)) {
            if (StringUtils.isNotBlank(email)) {
                if (!ValidationUtils.isEmail(email)) {
                    ret.setMessage("邮箱错误");
                    return ret;
                }
                e = userService.findByUserEmail(email);
                if (null != e) {
                    ret.setMessage("邮箱重复");
                    return ret;
                }
                userService.update(ue.getID(), "e", email.toLowerCase());
            }
        }
        if (sex != Constant.NEGATIVE_ONE) {
            userService.update(ue.getID(), "sex", sex);
        }
        return RespObj.SUCCESS;
    }


    @RequestMapping("/userpage")
    public String userpage(Map<String, Object> model, HttpServletResponse res) {
        String url = "homepage/homepage";
        model.put("classInfoList",
                userService.getClassDTOList(getUserId(), getSessionValue().getUserRole()));
        if (UserRole.isStudentOrParent(getSessionValue().getUserRole())) {
            url = "homepage/homepage";
            return url;
        } else if (UserRole.isTeacher(getSessionValue().getUserRole())) {
            url = "homepage/homepage";
            return url;
        } else if (UserRole.isHeadmaster(getSessionValue().getUserRole())) {
            url = "homepage/homepage";
            return url;
        } else if (UserRole.isDoorKeeper(getSessionValue().getUserRole())) {
            url = "homepage/homepage";
            return url;
        } else if (UserRole.isFunctionRoomManager(getSessionValue().getUserRole())) {
            url = "homepage/homepage";
            return url;
        } else if (UserRole.isDormManager(getSessionValue().getUserRole())) {
            url = "homepage/homepage";
            return url;
        }
        try {
            res.sendRedirect("/myschool/managesubject?version=55&tag=1");
        } catch (IOException e) {
            logger.error("", e);
        }
        return null;
    }


    @RequestMapping("/homepage")
    public String homepage(Map<String, Object> model, HttpServletResponse res) throws IOException {
        if (getSessionValue().getUserRole() == UserRole.EDUCATION.getRole()) {
            res.sendRedirect("/manageCount/countMain.do?version=1&index=2");
            return null;
        }
        String url = "homeschool/headmaster";
        try {
            String params = getUserId().toString() + 0l;
            String key = CacheHandler.getKeyString(CacheHandler.CACHE_USER_FIRST_LOGIN, params);
            String firstlogin = CacheHandler.getStringValue(key);
            if (firstlogin != null && Constant.USER_FIRST_LOGIN.equals(firstlogin)) {
                res.sendRedirect("/password");
                return null;
            }
        } catch (Exception ex) {
            logger.error("", ex);
        }
        UserEntry user = userService.findByUserId(getUserId());
        SessionValue sv = getSessionValue();
        sv.setExperience(user.getExperiencevalue());
        int validityTime = Constant.SECONDS_IN_DAY + (int) ((user.getLastActiveDate() - System.currentTimeMillis()) / 1000);
        String userKey = CacheHandler.getUserKey(sv.getId());
        CacheHandler.cacheSessionValue(userKey, sv, validityTime);
        if (ObjectId.isValid(getSessionValue().getSchoolId())) {
            SchoolEntry se = schoolService.getSchoolEntry(new ObjectId(getSessionValue().getSchoolId()), new BasicDBObject("bindId", 1).append("nm", 1));
            if (StringUtils.isNotBlank(se.getBindId())) {
                model.put("bindSchoolName", se.getName());
            }
        }
        model.put("classInfoList",
                userService.getClassDTOList(getUserId(), getSessionValue().getUserRole()));

        try {
            if (StringUtils.isNotBlank(getSessionValue().getSso())) {
                return "newHomepage11";
            }
        } catch (Exception ex) {
            logger.error("", ex);
        }

        if (!getSessionValue().getSchoolNavs().equals("navs")) {
            int navIndex = Integer.parseInt(getSessionValue().getSchoolNavs().substring(4, getSessionValue().getSchoolNavs().length()));
            url = "newHomepage" + navIndex;
            if (navIndex == 3) {
                int role = getSessionValue().getUserRole();
                if (UserRole.isManagerOnly(role)) {
                    try {
                        res.sendRedirect("/myschool/managesubject?index=18&version=1&tag=1");
                    } catch (IOException e) {
                        logger.error("", e);
                    }
                    return null;
                }
                if (UserRole.isDoorKeeper(role) || UserRole.isDormManager(role)) {
                    try {
                        res.sendRedirect("/user?version=91&index=6");
                    } catch (IOException e) {
                        logger.error("", e);
                    }
                    return null;
                }
                return url;
            }
            //导航5~9纯管理员直接不进入磁铁页面
            else if (UserRole.isManagerOnly(getSessionValue().getUserRole())) {
                try {
                    res.sendRedirect("/docflow/documentList.do?type=0&index=5&version=1");
                } catch (IOException e) {
                    logger.error("", e);
                }
                return null;
            }

            return url;
        }

        if (UserRole.isStudentOrParent(getSessionValue().getUserRole())) {
            url = "newHomepage";
            return url;
        } else if (UserRole.isTeacher(getSessionValue().getUserRole())) {
            url = "newHomepage";
            return url;
        } else if (UserRole.isHeadmaster(getSessionValue().getUserRole())) {
            url = "newHomepage";
            return url;
        } else if (UserRole.isDoorKeeper(getSessionValue().getUserRole())) {
            url = "homepage/homepage";
            return url;
        } else if (UserRole.isFunctionRoomManager(getSessionValue().getUserRole())) {
            url = "homepage/homepage";
            return url;
        } else if (UserRole.isDormManager(getSessionValue().getUserRole())) {
            url = "homepage/homepage";
            return url;
        }


        if (UserRole.isEducation(getSessionValue().getUserRole())) {
            try {
                res.sendRedirect("/manageCount/countMain.do?version=90&tag=1");
                return null;
            } catch (Exception ex) {
            }
        }

        try {
            res.sendRedirect("/myschool/managesubject?version=55&tag=1");
        } catch (IOException e) {
            logger.error("", e);
        }
        return null;
    }

    @RequestMapping("/userSchoolYearExpManage")
    public String userSchoolYearExpManage() {
        String url = "";
        if (UserRole.isManager(getSessionValue().getUserRole())) {
            url = "experienceHistory/userSchoolYearExp";
        }
        return url;
    }

    @RequestMapping("/scores")
    @SessionNeedless
    @ResponseBody
    public RespObj getScores(String pid) {
        if (StringUtils.isBlank(pid) || !ObjectId.isValid(pid)) {
            return RespObj.FAILD;
        }
        List<FScoreDTO> fScoreEntries = fScoreService.getFScoreByPersonId(pid);
        return RespObj.SUCCESS(fScoreEntries);
    }

    @RequestMapping("/updSchoolHomeDate")
    public void updSchoolHomeDate() {
        userService.updSchoolHomeDate(getUserId());
    }

    @RequestMapping("/updFamilyHomeDate")
    public void updFamilyHomeDate() {
        userService.updFamilyHomeDate(getUserId());
    }

    /**
     * k6kt增加经验值
     *
     * @param name
     */
    @RequestMapping("/updateExp")
    public void updateExp(String name) {
        userService.updateExp(name);
    }

    /**
     * 找回密码
     *
     * @param name
     */
    @SessionNeedless
    @RequestMapping("/findPwd")
    public String findPwd(String name) {
        return "user/userAccount";
    }


    /**
     * 找回密码第一次验证
     *
     * @param username
     * @param vCode
     * @return
     */
    @SessionNeedless
    @ResponseBody
    @RequestMapping("/check/first")
    public RespObj checkFirst(String username, String vCode) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(vCode)) {
            return RespObj.FAILD("用户名或验证码为空");
        }

        UserEntry e = userService.findByUserName(username.toLowerCase());
        if (null == e) {
            return RespObj.FAILD("找不到用户");
        }

        String cookieValue = getCookieValue(Constant.COOKIE_VALIDATE_CODE);
        String key = CacheHandler.getKeyString(CacheHandler.CACHE_VALIDATE_CODE, cookieValue);
        String rightCode = CacheHandler.getStringValue(key);

        if (StringUtils.isBlank(rightCode)) {
            return RespObj.FAILD("验证码失效");
        }

        if (!rightCode.equalsIgnoreCase(vCode)) {
            return RespObj.FAILD("验证码错误");
        }
        int type;

        if (UserRole.isStudent(e.getRole())) {
            type = 0;
        } else if (UserRole.isParent(e.getRole())) {
            type = 1;
        } else if (UserRole.TEACHER.getRole() == e.getRole()) {
            type = 2;
        } else {
            type = 3;
        }
        return RespObj.SUCCESS(type);
    }

    /**
     * 移动端找回密码第一次验证
     *
     * @param username
     * @return
     */
    @SessionNeedless
    @ResponseBody
    @RequestMapping("/mobile/check/first")
    public RespObj mobileCheckFirst(String username, HttpServletRequest request) {
        RespObj obj = new RespObj(Constant.FAILD_CODE);

        if (StringUtils.isBlank(username)) {
            obj.setMessage("用户名或验证码为空");
            return obj;
        }

        UserEntry e = userService.findByUserName(username.toLowerCase());
        if (null == e) {
            obj.setMessage("找不到用户");
            return obj;
        }

        String cookieValue = getCookieValue(Constant.COOKIE_VALIDATE_CODE);

        String key = CacheHandler.getKeyString(CacheHandler.CACHE_VALIDATE_CODE, cookieValue);

        String rightCode = CacheHandler.getStringValue(key);

        obj = new RespObj(Constant.SUCCESS_CODE);
        int type;

        if (UserRole.isStudent(e.getRole())) {
            type = 0;
        } else if (UserRole.isParent(e.getRole())) {
            type = 1;
        } else if (UserRole.TEACHER.getRole() == e.getRole()) {
            type = 2;
        } else {
            type = 3;
        }
        obj.setMessage(type);
        return obj;
    }


    /**
     * 重设密码
     *
     * @param username
     * @param vCode
     * @return
     */
    @SessionNeedless
    @ResponseBody
    @RequestMapping("/resetPwd")
    public RespObj reSetPwd(String username, String pwd, String pwdAgain, String vCode, HttpServletRequest request) {
        RespObj obj = new RespObj(Constant.FAILD_CODE);

        String cacheTag = CacheHandler.getStringValue(getCookieValue("JSESSIONID"));

        if (StringUtils.isBlank(cacheTag) || !"1".equals(cacheTag)) {
            obj.setMessage("参数错误");
            return obj;
        }

        if (StringUtils.isBlank(pwd) || StringUtils.isBlank(pwdAgain) || !pwd.equals(pwdAgain)) {
            obj.setMessage("参数错误");
            return obj;
        }

        UserEntry e = userService.findByUserName(username.toLowerCase());
        if (null == e) {
            obj.setMessage("找不到用户");
            return obj;
        }
        boolean isCheckRole = false;
        if (UserRole.isParent(e.getRole()) || UserRole.isStudent(e.getRole()) || e.getRole() == UserRole.TEACHER.getRole()) {
            isCheckRole = true;
        }

        if (!isCheckRole) {
            obj.setMessage("身份错误");
            return obj;
        }

        String cookieValue = getCookieValue(Constant.COOKIE_VALIDATE_CODE);

        String key = CacheHandler.getKeyString(CacheHandler.CACHE_VALIDATE_CODE, cookieValue);

        String rightCode = CacheHandler.getStringValue(key);

        if (StringUtils.isNotBlank(vCode) && !rightCode.equalsIgnoreCase(vCode)) {
            obj.setMessage("验证码错误");
            return obj;
        }

        try {
            userService.update(e.getID(), "pw", MD5Utils.getMD5(pwdAgain));
        } catch (IllegalParamException e1) {
            logger.error("", e1);
        } catch (Exception e1) {
            logger.error("", e1);
        }

        return RespObj.SUCCESS;
    }


    /**
     * 用户更新基本信息
     *
     * @param userName
     * @param mobile
     * @param valiCode
     * @param cacheKeyId
     * @param clientType 客户端类型 0是网页 1是手机
     * @return
     * @throws IllegalParamException
     */
    @SessionNeedless
    @RequestMapping("/update/basic2")
    @ResponseBody
    public RespObj updateUserBasicInfos2(String userName, String mobile, String valiCode, String cacheKeyId, @RequestParam(required = false, defaultValue = "0") Integer clientType, HttpServletRequest req) throws IllegalParamException {
        RespObj ret = new RespObj(Constant.FAILD_CODE);
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(mobile) || StringUtils.isBlank(valiCode)) {
            ret.setMessage("请正确输入手机和邮箱");
            return ret;
        }

        UserEntry ue = userService.findByUserName(userName);

        if (null == ue) {
            ret.setMessage("用户名错误");
            return ret;
        }


        if (!ValidationUtils.isRequestModile(mobile)) {
            ret.setMessage("手机非法");
            return ret;
        }

        UserEntry mobileEntry = userService.findByUserPhone(mobile);

        if (null != mobileEntry && !mobileEntry.getUserName().toLowerCase().equals(ue.getUserName())) {
            ret.setMessage("此手机已经被占用");
            return ret;
        }

        String cacheKey = CacheHandler.getKeyString(CacheHandler.CACHE_SHORTMESSAGE, cacheKeyId);
        String value = CacheHandler.getStringValue(cacheKey);
        if (StringUtils.isBlank(value)) {
            ret.setMessage("验证码失效，请重新获取");
            return ret;
        }
        String[] cache = value.split(",");
        if (!cache[0].equals(valiCode)) {
            ret.setMessage("注册失败：手机号码与验证码不匹配");
            return ret;
        }
        String jsessionIdNow = getCookieValue("JSESSIONID");

        if (clientType == 0) {
            cacheKey = CacheHandler.getKeyString(CacheHandler.SESSIONID_EMALL, jsessionIdNow);
            String emailCache = CacheHandler.getStringValue(cacheKey);

            if (StringUtils.isBlank(emailCache)) {
                ret.setMessage("邮箱验证错误");
                return ret;
            }
            UserEntry emailUser = userService.findByUserEmail(emailCache);
            if (null != emailUser && !emailUser.getUserName().equalsIgnoreCase(userName)) {
                ret.setMessage("邮箱已经被登记使用");
                return ret;
            }
            userService.update(ue.getID(), "e", emailCache.toLowerCase());
        }
        userService.update(ue.getID(), "mn", mobile.toLowerCase());
        CacheHandler.cache(jsessionIdNow, "1", Constant.SECONDS_IN_HOUR);
        return RespObj.SUCCESS;
    }

    /**
     * @param email
     * @param req
     * @return
     * @throws IllegalParamException
     */
    @SessionNeedless
    @RequestMapping("/email")
    @ResponseBody
    public RespObj sendEmail(String email, HttpServletRequest req) throws IllegalParamException {
        if (!ValidationUtils.isEmail(email)) {
            return RespObj.FAILD("邮箱错误");
        }

        ObjectId ukId = new ObjectId();
        String cacheKey = CacheHandler.getKeyString(CacheHandler.SESSIONID_EMALL, ukId.toString());

        String jsessionId = getCookieValue("JSESSIONID");
        CacheHandler.cache(cacheKey, jsessionId, Constant.SECONDS_IN_HOUR);

        try {
            String templateContent = FileUtils.readFileToString(new File(req.getServletContext().getRealPath("/email/findPwd.html")), "utf-8");
            String link = MessageFormat.format(linkFormat, ukId.toString(), email, UUID.randomUUID().toString());
            String emailContent = MessageFormat.format(templateContent, link);
            MailUtils sendMail = new MailUtils();
            sendMail.sendMail("用户邮箱验证", email, emailContent);
        } catch (Exception e1) {
            logger.error("", e1);
            return RespObj.FAILD("邮箱错误");
        }
        return RespObj.SUCCESS;
    }


    /**
     * 邮件认证用户回调
     *
     * @param email
     * @param req
     * @return
     * @throws Exception
     */
    @SessionNeedless
    @RequestMapping("/email/callback")
    public String emailCall(String ukId, String email, Map<String, Object> model, HttpServletRequest req) throws Exception {
        if (null == ukId || !ObjectId.isValid(ukId)) {
            throw new Exception();
        }

        if (!ValidationUtils.isEmail(email)) {
            throw new Exception();
        }

        String cacheKey = CacheHandler.getKeyString(CacheHandler.SESSIONID_EMALL, ukId.toString());
        String jsessionId = CacheHandler.getStringValue(cacheKey);

        cacheKey = CacheHandler.getKeyString(CacheHandler.SESSIONID_EMALL, jsessionId);
        CacheHandler.cache(cacheKey, email, Constant.SECONDS_IN_HOUR);

        model.put("email", email);
        return "user/emailsuccess";
    }

    /**
     * 复兰商城登录前通知
     *
     * @param response
     * @param request
     * @return
     */
    @ResponseBody
    @SessionNeedless
    @RequestMapping("/mall/cachekey")
    public RespObj mallCachekey(String mallToken, String cookieKey, HttpServletResponse response, HttpServletRequest request) {
        if (StringUtils.isBlank(mallToken) || StringUtils.isBlank(cookieKey)) {
            return RespObj.FAILD;
        }

        String ip = getIP();
        logger.info("mall cachekey ip:" + ip);
        //对IP进行判断
        if (!ip.equalsIgnoreCase("120.55.115.218") && !ip.equalsIgnoreCase("10.168.156.180")
                && !ip.equalsIgnoreCase("120.55.184.233") && !ip.equalsIgnoreCase("10.117.17.109")) {
        }
        CacheHandler.cache(mallToken, cookieKey, Constant.SESSION_TWO_MINUTE);
        return RespObj.SUCCESS;
    }

    /**
     * 进行QQ登录
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @SessionNeedless
    @RequestMapping(value = "/qqlogin")
    public void QQLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String state = "123456";
        request.getSession().setAttribute("qq_connect_state", state);
        String redirect_URI = HttpClientUtils.strURLEncodeUTF8(QQLoginUtil.getValue("redirect_URI").trim());
        String authorizeURL = QQLoginUtil.getValue("authorizeURL").trim();
        String app_ID = QQLoginUtil.getValue("app_ID").trim();
        String url = authorizeURL + "?client_id=" + app_ID + "&redirect_uri=" + redirect_URI + "&response_type=" + "code" + "&state=" + state;
        response.sendRedirect(url);
    }


    /**
     * PC QQ登录回调
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @SessionNeedless
    @RequestMapping(value = "/qqcallback")
    public String afterQQLogin(HttpServletRequest request, HttpServletResponse response, String state, RedirectAttributes redirectAttributes) throws IOException {
        String code = request.getParameter("code");
        if (code == null || code.equals("")) {//未授权
            return "redirect:/";
        }


        Map<String, String> result = QQLoginUtil.getAccessToken(code);
        logger.info("" + result);
        String access_token = result.get("access_token");
        Map<String, Object> maps = QQLoginUtil.getOpenId(access_token);
        String openId = (String) maps.get("openid");
        Map<String, Object> userInfoMaps = QQLoginUtil.getUserInfo(access_token, openId);

        String nickName = (String) userInfoMaps.get("nickname");
        Map<String, Object> query = new HashMap<String, Object>();
        query.put("oid", openId);
        query.put("type", 2);

        int sex = 2;
        String gender = (String) userInfoMaps.get("gender");
        if (gender != null && gender.equals("男")) {
            sex = 1;
        }

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("bindQQ") && getUserId() != null) {
                if (cookie.getValue().equals(getUserId().toString())) {
                    //绑定qq
                    boolean isBind = userService.isOpenIdBindQQ(openId);
                    if (isBind) {
                        redirectAttributes.addAttribute("bindSuccess", 0);
                        return "redirect:/account/thirdLoginSuccess";
                    }

                    //开始绑定
                    ThirdLoginEntry thirdLoginEntry = new ThirdLoginEntry(getUserId(), openId, null, ThirdType.QQ);
                    userService.saveThidLogininfo(thirdLoginEntry);

                    redirectAttributes.addAttribute("bindSuccess", 1);
                    return "redirect:/account/thirdLoginSuccess";
                }
            }
        }

        UserEntry userEntry = userService.getThirdEntryByMap(query);
        if (userEntry == null) { //创建用户
            userEntry = userService.createUser(nickName, sex);
            //保存第三方登录数据
            ThirdLoginEntry thirdLoginEntry = new ThirdLoginEntry(userEntry.getID(), openId, null, ThirdType.QQ);
            userService.saveThidLogininfo(thirdLoginEntry);
        }

        //检查是否注册环信
        boolean isRegister = userEntry.isRegisterHuanXin();
        String nickName2 = StringUtils.isNotBlank(userEntry.getNickName()) ? userEntry.getNickName() : userEntry.getUserName();
        if (!isRegister) {
            EaseMobAPI.createUser(userEntry.getID().toString(), nickName2);
        }

        String redirectUrl = userService.getRedirectUrl(request);
        userService.setCookieValue(getIP(), userEntry, response, request);
        if (StringUtils.isNotBlank(redirectUrl) && getPlatform() != Platform.PC) {
            return redirectUrl;
        }
        return "redirect:/account/thirdLoginSuccess";
    }

    /**
     * 进行微信登录
     *
     * @param response
     * @throws IOException
     */
    @SessionNeedless
    @RequestMapping(value = "/wechatlogin")
    public void weChatLogin(HttpServletResponse response) throws IOException {
        String urlEncodeRedirectUrl = HttpClientUtils.strURLEncodeUTF8(Constant.WECHAT_REDIRECT_URL);
        String strWeChatConnectUrl = String.format(Constant.WECHAT_CONNECT_URL, Constant.WECHAT_APPID, urlEncodeRedirectUrl);
        response.sendRedirect(strWeChatConnectUrl);
    }

    /**
     * 微信登录回调接口
     *
     * @param request
     * @param response
     * @return
     */
    @SessionNeedless
    @RequestMapping(value = "/wechatcallback")
    public String wechatCallBack(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) throws IOException {
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        Map<String, Object> maps;
        //没有授权，重定向
        if (code == null || state == null) {
            return "redirect:/";
        }

        //PC端登录
        if (getPlatform() == Platform.PC) {
            maps = WeChatLoginUtil.getAccess_Token(code, Constant.WECHAT_APPID, Constant.WECHAT_APPSECRET);
        } else {
            //Wap 端登录
            maps = WeChatLoginUtil.getAccess_Token(code, Constant.WECHAT_THIRD_PART_APPID, Constant.WECHAT_THIRD_PART_APPSECRET);
        }

        if (maps != null && !maps.containsKey("access_token")) { //没有取得access_token
            return "redirect:/";
        }

        String access_token = (String) maps.get("access_token");
        String openid = (String) maps.get("openid");

        Map<String, Object> userInfoMaps = WeChatLoginUtil.getWeChatUserInfo(access_token, openid);

        String unionId = (String) userInfoMaps.get("unionid");
        //拿到unionid之后要去数据库查询，找一遍有没有这个用户
        logger.info("查询数据库");

        Map<String, Object> query = new HashMap<String, Object>();
        query.put("unionid", unionId);
        query.put("type", 1);

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("bindWechat") && getUserId() != null) {
                if (cookie.getValue().equals(getUserId().toString())) {
                    //绑定qq
                    boolean isBind = userService.isUnionIdBindWechat(unionId);
                    if (isBind) {
                        redirectAttributes.addAttribute("bindSuccess", 0);
                        return "redirect:/account/thirdLoginSuccess";
                    }

                    //开始绑定
                    ThirdLoginEntry thirdLoginEntry = new ThirdLoginEntry(getUserId(), null, unionId, ThirdType.WECHAT);
                    userService.saveThidLogininfo(thirdLoginEntry);

                    redirectAttributes.addAttribute("bindSuccess", 1);
                    return "redirect:/account/thirdLoginSuccess";
                }
            }
        }


        UserEntry userEntry = userService.getThirdEntryByMap(query);

        if (userEntry == null) { //不存在,创建账号
            String nickName = (String) userInfoMaps.get("nickname");
            Integer sex = (Integer) userInfoMaps.get("sex");
            userEntry = userService.createUser(nickName, sex);
            //保存第三方登录数据
            ThirdLoginEntry thirdLoginEntry = new ThirdLoginEntry(userEntry.getID(), openid, unionId, ThirdType.WECHAT);
            userService.saveThidLogininfo(thirdLoginEntry);
        }

        //检查是否注册环信
        boolean isRegister = userEntry.isRegisterHuanXin();
        String nickName2 = StringUtils.isNotBlank(userEntry.getNickName()) ? userEntry.getNickName() : userEntry.getUserName();
        if (!isRegister) {
            EaseMobAPI.createUser(userEntry.getID().toString(), nickName2);
        }

        String redirectUrl = userService.getRedirectUrl(request);
        userService.setCookieValue(getIP(), userEntry, response, request);
        if (redirectUrl != null && getPlatform() != Platform.PC) {
            return redirectUrl;
        }
        return "redirect:/account/thirdLoginSuccess";
    }

    /**
     * 判断数据库中是否存在
     *
     * @param openId
     * @param type
     * @param unionId
     * @return
     */
    @SessionNeedless
    @RequestMapping("/third/check")
    @ResponseBody
    public RespObj checkUserFromThird(String openId, Integer type, String unionId,
                                      HttpServletResponse response, HttpServletRequest request) {
        UserEntry userEntry = userService.searchThirdEntry(openId, unionId, type);
        if (userEntry == null) {
            return RespObj.SUCCESS(MapUtil.put("isExist", "No"));
        }
        SessionValue value = userService.setCookieValue(getIP(), userEntry, response, request);

        //检查是否注册环信
        boolean isRegister = userEntry.isRegisterHuanXin();
        String nickName2 = StringUtils.isNotBlank(userEntry.getNickName()) ? userEntry.getNickName() : userEntry.getUserName();
        if (!isRegister) {
            EaseMobAPI.createUser(userEntry.getID().toString(), nickName2);
        }

        //找玩伴
        boolean isMateExist = mateService.isMateRecoreExist(userEntry.getID());
        if (!isMateExist) {
            mateService.saveMateEntry(userEntry.getID());
        }

        return RespObj.SUCCESS(value);
    }

    /**
     * 创建账户
     *
     * @param openId
     * @param type
     * @param unionId
     * @param nickName
     * @param avatar
     * @return
     */
    @SessionNeedless
    @RequestMapping("/third/users")
    @ResponseBody
    public RespObj createUserFromThird(String openId, Integer type, String unionId, String nickName,
                                       String avatar, HttpServletResponse response, HttpServletRequest request, Integer sex) {
        if (type == null) {
            return RespObj.FAILD("参数不对");
        }
        UserEntry userEntry = userService.searchThirdEntry(openId, unionId, type);
        if (userEntry == null) { //第一次进入应用
            userEntry = userService.createUser(nickName, sex);
            if (avatar != null) {
                userEntry.setAvatar(avatar);
                try {
                    userService.updateAvatar(userEntry.getID().toString(), avatar);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //保存第三方登录数据
            ThirdLoginEntry thirdLoginEntry = new ThirdLoginEntry(userEntry.getID(), openId, unionId, ThirdType.getThirdType(type));
            userService.saveThidLogininfo(thirdLoginEntry);
        }
        SessionValue value = userService.setCookieValue(getIP(), userEntry, response, request);
        return RespObj.SUCCESS(value);
    }


    /**
     * 第三方登录提交绑定接口
     *
     * @param openId
     * @param unionId
     * @param type
     * @param userId
     * @return
     */
    @SessionNeedless
    @RequestMapping(value = "/third/bind")
    @ResponseBody
    public RespObj appThirdLoginSubmit(String openId, String unionId, Integer type, String userId) {
        if (StringUtils.isBlank(userId) || type == null) {
            return RespObj.FAILD("参数不完整");
        }
        if (type == 1) { //微信
            if (unionId == null || openId == null) {
                return RespObj.FAILD("参数不完整");
            }
        } else {
            if (openId == null) {
                return RespObj.FAILD("参数不完整");
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("uid", new ObjectId(userId));
        ThirdLoginEntry third = userService.searchThirdLoginEntry(map);
        if (third != null) {
            return RespObj.FAILD("用户已绑定");
        }
        //保存第三方登录数据
        ThirdLoginEntry thirdLoginEntry = new ThirdLoginEntry(new ObjectId(userId), openId, unionId, ThirdType.getThirdType(type));
        ObjectId objectId = userService.saveThidLogininfo(thirdLoginEntry);
        if (objectId != null) {
            return RespObj.SUCCESS("成功");
        }
        return RespObj.FAILD("失败");
    }

    /**
     * 跳转到k6kt
     *
     * @throws ClientProtocolException
     * @throws IOException
     */
    @SessionNeedless
    @RequestMapping("/loginK6kt")
    public void tryLoginK6kt() throws IOException {
        SessionValue sv = getSessionValue();
        if (null == sv || sv.isEmpty() || sv.getK6kt() == 0) {
            getResponse().sendRedirect("http://www.k6kt.com/");
            return;
        }
        String cookieKey = getCookieValue(Constant.COOKIE_USER_KEY);
        String mallToken = UUID.randomUUID().toString();
        String url = "http://www.k6kt.com/user/mall/cachekey.do?mallToken=" + mallToken + "&cookieKey=" + cookieKey;
        HttpClientUtils.get(url);
        getResponse().sendRedirect("http://www.k6kt.com/user/mall/login.do?mallToken=" + mallToken);
    }

    /**
     * 验证用户是否绑定手机号
     *
     * @return
     */
    @RequestMapping("/isBindPhone")
    @ResponseBody
    public RespObj isBindPhone() {
        ObjectId uid = getUserId();
        UserEntry user = userService.findByUserId(uid);
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(user.getMobileNumber())) {
            map.put("isBind", true);
            map.put("phone", user.getMobileNumber());
            return RespObj.SUCCESS(map);
        }
        map.put("isBind", false);
        return RespObj.SUCCESS(map);
    }

    /**
     * 绑定手机号
     *
     * @param mobile
     * @return
     */
    @RequestMapping("/addUserPhone")
    @ResponseBody
    public RespObj addUserPhone(String mobile) {
        if (StringUtils.isBlank(mobile)) {
            return RespObj.FAILD("手机号为空");
        }
        if (!Validator.isMobile(mobile)) {
            return RespObj.FAILD("手机号不合法");
        }
        ObjectId uid = getUserId();
        UserEntry user = userService.findByUserId(uid);
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(user.getPhoneNumber())) {
            if (mobile.endsWith(user.getPhoneNumber())) {
                map.put("bind", false);
                map.put("msg", "被别人绑定了");
                return RespObj.SUCCESS(map);
            }
        }
        userService.addUserPhone(uid, mobile);
        map.put("bind", true);
        return RespObj.SUCCESS(map);
    }

    @RequestMapping(value = "/userInfo")
    @ResponseBody
    @SessionNeedless
    public Map<String, Object> getUserInfo(@ObjectIdType ObjectId userId) {
        UserDetailInfoDTO user = userService.getUserInfoById(userId.toString());
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("userName", user.getUserName());
        result.put("nickName", user.getNickName());
        result.put("sex", user.getSex());
        result.put("phone", user.getMobileNumber());
        result.put("userId", user.getId());
        result.put("avator", user.getImgUrl());

        Date birthdate = user.getBirthDate();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(birthdate);
        String[] dateArr = date.split("-");
        int year = Integer.valueOf(dateArr[0]);
        int month = Integer.valueOf(dateArr[1]);
        int day = Integer.valueOf(dateArr[2]);

        result.put("year", year);
        result.put("month", month);
        result.put("day", day);
        return result;
    }

    @RequestMapping("/userInfos")
    public
    @ResponseBody
    RespObj getUserInfos(String userIds) {
        List<ObjectId> userList = new ArrayList<ObjectId>();
        String[] userStr = userIds.split(",");
        for (String e : userStr) {
            if (ObjectId.isValid(e)) {
                userList.add(new ObjectId(e));
            }
        }

        List<User> userDatas = new ArrayList<User>();
        List<UserDetailInfoDTO> userDetailInfoDTOS = userService.findUserInfoByIds(userList);

        for (UserDetailInfoDTO e : userDetailInfoDTOS) {
            User user = new User();
            user.setId(e.getId());
            user.setAvator(e.getImgUrl());
            user.setNickName(e.getNickName());
            user.setUserName(e.getUserName());
            user.setSex(e.getSex());
            userDatas.add(user);
        }

        return RespObj.SUCCESS(userDatas);
    }

}
