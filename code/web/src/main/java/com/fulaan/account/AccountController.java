package com.fulaan.account;

import com.fulaan.account.dto.VerifyData;
import com.fulaan.account.service.AccountService;
import com.fulaan.annotation.LoginInfo;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.annotation.UserRoles;
import com.fulaan.cache.CacheHandler;
import com.fulaan.controller.BaseController;
import com.fulaan.dto.UserDTO;
import com.fulaan.playmate.service.MateService;
import com.fulaan.user.service.UserService;
import com.fulaan.user.util.QQLoginUtil;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.exceptions.UnLoginException;
import com.sys.utils.HttpClientUtils;
import com.sys.utils.MD5Utils;
import com.sys.utils.RespObj;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by moslpc on 2016/12/12.
 */
@Controller
@RequestMapping("/account")
public class AccountController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private MateService mateService;

    /**
     * 注册界面
     * @return
     */
    @RequestMapping("/register")
    @SessionNeedless
    public String register() {
        return "/account/register";
    }

    /**
     * 登录界面
     * @return
     */
    @RequestMapping("/login")
    @SessionNeedless
    public String login() {
        return "/account/login";
    }

    /**
     * 找回密码
     * @return
     */
    @RequestMapping("/findPassword")
    @SessionNeedless
    public String findPassword() {
        return "/account/findPassword";
    }

    /**
     * 我的账户
     * @return
     */
    @RequestMapping("/accountSafe")
    @LoginInfo
    public String accountSafe() {
        return "/account/accountSafe";
    }

    /**
     * 第三方登录成功
     *
     * @return
     */
    @RequestMapping("/thirdLoginSuccess")
    @SessionNeedless
    public String thirdLoginSuccess(@RequestParam(value = "bindSuccess",required = false,defaultValue = "-1") int bindSuccess,Model model) {
        model.addAttribute("bindSuccess",bindSuccess);
        return "/account/thirdLoginSuccess";
    }

    /**
     * 检查用户名是否可用
     * @param userName
     * @return
     */
    @RequestMapping("/userNameCheck")
    @SessionNeedless
    @ResponseBody
    public RespObj userNameCheck(String userName) {
        return RespObj.SUCCESS(userService.checkUserNameExist(userName));
    }

    /**
     * 检查手机号是否可用
     * @param phone
     * @return
     */
    @RequestMapping("/userPhoneCheck")
    @SessionNeedless
    @ResponseBody
    public RespObj userPhoneCheck(String phone) {
        return RespObj.SUCCESS(userService.searchUserByphone(phone) != null);
    }

    /**
     * 检查邮箱是否可用
     * @param email
     * @return
     */
    @RequestMapping("/userEmailCheck")
    @SessionNeedless
    @ResponseBody
    public RespObj userEmailCheck(String email) {
        return RespObj.SUCCESS(userService.searchUserByEmail(email) != null);
    }

    /**
     * 进行QQ 绑定
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @SessionNeedless
    @RequestMapping(value = "/qqBind")
    public void QQLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String state = "123456";
        request.getSession().setAttribute("qq_connect_state", state);
        String redirect_URI = HttpClientUtils.strURLEncodeUTF8(QQLoginUtil.getValue("redirect_URI").trim());
        String authorizeURL = QQLoginUtil.getValue("authorizeURL").trim();
        String app_ID = QQLoginUtil.getValue("app_ID").trim();
        String url = authorizeURL + "?client_id=" + app_ID + "&redirect_uri=" + redirect_URI + "&response_type=" + "code" + "&state=" + state;

        Cookie userKeycookie = new Cookie("bindQQ", getUserId().toString());
        userKeycookie.setMaxAge(Constant.SECONDS_IN_DAY);
        userKeycookie.setPath(Constant.BASE_PATH);
        response.addCookie(userKeycookie);

        response.sendRedirect(url);
    }

    /**
     * 进行微信绑定操作
     *
     * @param response
     * @throws IOException
     */
    @SessionNeedless
    @RequestMapping(value = "/wechatBind")
    public void WeChatLogin(HttpServletResponse response) throws IOException {
        String urlEncodeRedirectUrl = HttpClientUtils.strURLEncodeUTF8(Constant.WECHAT_REDIRECT_URL);
        String strWeChatConnectUrl = String.format(Constant.WECHAT_CONNECT_URL, Constant.WECHAT_APPID, urlEncodeRedirectUrl);

        Cookie userKeycookie = new Cookie("bindWechat", getUserId().toString());
        userKeycookie.setMaxAge(Constant.SECONDS_IN_DAY);
        userKeycookie.setPath(Constant.BASE_PATH);
        response.addCookie(userKeycookie);

        response.sendRedirect(strWeChatConnectUrl);
    }

    /**
     * 验证用户名与验证码
     *
     * @param name
     * @param verifyCode
     * @param verifyKey
     * @param response
     * @return
     */
    @RequestMapping("/verifyCodeWithName")
    @SessionNeedless
    @ResponseBody
    public RespObj verifyCodeWithName(String name, String verifyCode, @CookieValue(Constant.COOKIE_VALIDATE_CODE) String verifyKey, HttpServletResponse response) {

        if (!accountService.checkVerifyCode(verifyCode, verifyKey)) {
            return RespObj.FAILD("验证码不正确");
        }

        if (userService.findByRegular(name) == null) {
            return RespObj.FAILD("用户不存在");
        }

        ObjectId key = new ObjectId();
        String cacheKey = CacheHandler.getKeyString(CacheHandler.CACHE_FW_USERNAME_KEY, key.toString());
        CacheHandler.cache(cacheKey, name, Constant.SESSION_FIVE_MINUTE);//5分钟
        response.addCookie(new Cookie(Constant.FWCODE, key.toString()));
        return RespObj.SUCCESS;
    }

    /**
     * 验证用户与手机是否匹配
     *
     * @param phone
     * @param fwCode
     * @return
     */
    @SessionNeedless
    @RequestMapping("/verifyUserPhone")
    @ResponseBody
    public RespObj verifyUserPhone(String phone, @CookieValue(Constant.FWCODE) String fwCode) {
        String fwUser = CacheHandler.getStringValue(CacheHandler.getKeyString(CacheHandler.CACHE_FW_USERNAME_KEY, fwCode));
        if (fwUser == null) {
            return RespObj.FAILD(new VerifyData(false, "时间失效或不存在"));
        }
        UserDTO userDTO = userService.findByRegular(fwUser);
        if (userDTO == null) {
            return RespObj.FAILD(new VerifyData(false, "用户不存在"));
        }
        if (phone.equals(userDTO.getPhone())) {
            return RespObj.SUCCESS(new VerifyData(true, "验证成功"));
        } else {
            return RespObj.FAILD(new VerifyData(false, "该用户未绑定此手机号"));
        }
    }

    /**
     * 验证 用户与邮箱是否匹配
     *
     * @param email
     * @param fwCode
     * @return
     */
    @SessionNeedless
    @RequestMapping("/verifyUserEmail")
    @ResponseBody
    public RespObj verifyUserEmail(String email, @CookieValue(Constant.FWCODE) String fwCode) {
        String fwUser = CacheHandler.getStringValue(CacheHandler.getKeyString(CacheHandler.CACHE_FW_USERNAME_KEY, fwCode));
        if (fwUser == null) {
            return RespObj.FAILD(new VerifyData(false, "时间失效或不存在"));
        }
        UserDTO userDTO = userService.findByRegular(fwUser);
        if (userDTO == null) {
            return RespObj.FAILD(new VerifyData(false, "用户不存在"));
        }
        if (email.equals(userDTO.getEmail())) {
            ObjectId key = new ObjectId();
            String cacheKey = CacheHandler.getKeyString(CacheHandler.CACHE_FW_VALIDATE_EMAIL, key.toString());
            CacheHandler.cache(cacheKey, userDTO.getUserName(), Constant.SECONDS_IN_DAY);//1天
            accountService.processFindPasswordByEmail(userDTO.getUserName(), email, key.toString());
            return RespObj.SUCCESS(new VerifyData(true, "发送成功"));
        } else {
            return RespObj.FAILD(new VerifyData(false, "该用户未绑定此邮箱"));
        }
    }

    /**
     * 手机验证
     *
     * @param phone
     * @param code
     * @param cacheKeyId
     * @param fwCode
     * @param response
     * @return
     */
    @SessionNeedless
    @RequestMapping("/phoneValidate")
    @ResponseBody
    public RespObj phoneValidate(String phone, String code, String cacheKeyId, @CookieValue(Constant.FWCODE) String fwCode, HttpServletResponse response) {
        String fwUser = CacheHandler.getStringValue(CacheHandler.getKeyString(CacheHandler.CACHE_FW_USERNAME_KEY, fwCode));
        if (fwUser == null) {
            return RespObj.FAILD(new VerifyData(false, "时间失效或不存在"));
        }
        UserDTO userDTO = userService.findByRegular(fwUser);
        if (userDTO == null) {
            return RespObj.FAILD(new VerifyData(false, "用户不存在"));
        }
        if (!phone.equals(userDTO.getPhone())) {
            return RespObj.FAILD("用户未绑定此手机号");
        }

        String cacheKey = CacheHandler.getKeyString(CacheHandler.CACHE_SHORTMESSAGE, cacheKeyId);
        String value = CacheHandler.getStringValue(cacheKey);
        if (StringUtils.isBlank(value)) {
            return RespObj.FAILD("验证码失效，请重新获取");
        }
        String[] cache = value.split(",");
        if (!cache[1].equals(phone)) {
            return RespObj.FAILD("注册失败：手机号码与验证码不匹配");
        }

        if (cache[0].equals(code)) {
            ObjectId keyId = new ObjectId();
            String resetCacheKey = CacheHandler.getKeyString(CacheHandler.CACHE_FW_RESET_PASSWORD, keyId.toString());
            CacheHandler.cache(resetCacheKey, userDTO.getUserName(), Constant.SESSION_FIVE_MINUTE);
            response.addCookie(new Cookie(Constant.FW_RESET_PASSWORD_CODE, keyId.toString()));
            CacheHandler.deleteKey(CacheHandler.CACHE_SHORTMESSAGE, cacheKeyId);
            return RespObj.SUCCESS("验证成功");
        } else {
            return RespObj.FAILD("短信验证码输入错误");
        }
    }

    /**
     * 邮箱验证
     *
     * @param validateCode
     * @param response
     * @return
     * @throws Exception
     */
    @SessionNeedless
    @RequestMapping("/emailValidate")
    public ModelAndView emailValidate(String validateCode, HttpServletResponse response) throws Exception {
        String cacheKey = CacheHandler.getKeyString(CacheHandler.CACHE_FW_VALIDATE_EMAIL, validateCode);
        String userName = CacheHandler.getStringValue(cacheKey);
        if (userName == null) {
            throw new UnLoginException();
        }

        CacheHandler.deleteKey(CacheHandler.CACHE_FW_VALIDATE_EMAIL, validateCode);
        ObjectId key = new ObjectId();
        String resetPassKey = CacheHandler.getKeyString(CacheHandler.CACHE_FW_RESET_PASSWORD, key.toString());
        CacheHandler.cache(resetPassKey, userName, Constant.SESSION_FIVE_MINUTE);
        response.addCookie(new Cookie(Constant.FW_RESET_PASSWORD_CODE, key.toString()));
        return new ModelAndView("/account/findPassword", "verify", true);
    }

    /**
     * 重置密码
     *
     * @param password
     * @param resetCode
     * @return
     */
    @SessionNeedless
    @RequestMapping("/resetPassword")
    @ResponseBody
    public RespObj resetPassword(String password, @CookieValue(Constant.FW_RESET_PASSWORD_CODE) String resetCode) {
        String cacheKey = CacheHandler.getKeyString(CacheHandler.CACHE_FW_RESET_PASSWORD, resetCode);
        String userName = CacheHandler.getStringValue(cacheKey);
        if (userName == null) {
            return RespObj.FAILD("时间失效或不合法");
        }
        UserEntry userEntry = userService.searchUserByUserName(userName);
        if (userEntry == null) {
            return RespObj.FAILD("用户不存在");
        }
        userService.resetPassword(userEntry.getID(), MD5Utils.getMD5String(password));
        CacheHandler.deleteKey(CacheHandler.CACHE_FW_RESET_PASSWORD, resetCode);
        return RespObj.SUCCESS("重置密码成功");
    }

    /**
     * 更新出生年月 - 性别 - 昵称
     * @param year
     * @param month
     * @param day
     * @param sex
     * @param nickName
     * @return
     */
    @RequestMapping("/updateNickSexAge")
    @ResponseBody
    public RespObj updateNickSexAge(@RequestParam(value = "year", defaultValue = "0", required = false) int year,
                                    @RequestParam(value = "month", defaultValue = "0", required = false) int month,
                                    @RequestParam(value = "day", defaultValue = "0", required = false) int day,
                                    @RequestParam(value = "sex", defaultValue = "-1", required = false) int sex,
                                    @RequestParam(value = "nickName", defaultValue = "", required = false) String nickName) {

        if (year != 0 && month != 0 && day != 0) {
            try {
                String date = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date birthday = format.parse(date);
                userService.update(getUserId(), "bir", birthday.getTime());
                mateService.updateAged(getUserId(), birthday.getTime());
            } catch (Exception e) {
                e.printStackTrace();
                return RespObj.FAILD(e.getMessage());
            }
        }

        if (!"".equals(nickName)) {
            userService.updateNickNameById(getUserId().toString(), nickName);
        }
        userService.updateSexById(getUserId(), sex);
        return RespObj.SUCCESS;
    }

    /**
     * 检查用户密码
     * @param password
     * @return
     */
    @RequestMapping("/checkUserPassword")
    @ResponseBody
    public RespObj checkUserPassword(String password) {
        ObjectId userId = getUserId();
        UserEntry userEntry = userService.find(userId);
        try {
            if (MD5Utils.getMD5(password).equals(userEntry.getPassword())) {
                return RespObj.SUCCESS;
            }

            if(password.equals(userEntry.getPassword())) {
                return RespObj.SUCCESS;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return RespObj.FAILD("密码不正确");
        }
        return RespObj.FAILD("密码不正确");
    }

    /**
     * 修改用户密码
     *
     * @param password
     * @return
     */
    @RequestMapping("/changeUserPassword")
    @ResponseBody
    public RespObj changeUserPassword(String password) {
        try {
            userService.updatePassword(getUserId().toString(), MD5Utils.getMD5(password));
        } catch (Exception e) {
            e.printStackTrace();
            RespObj.FAILD("修改失败");
        }
        return RespObj.SUCCESS("修改成功");
    }

    /**
     * 更改用户邮箱
     *
     * @param email
     * @return
     */
    @RequestMapping("/changeUserEmail")
    @ResponseBody
    public RespObj changeUserEmail(String email) {

        UserEntry userEntry = userService.searchUserByEmail(email);
        if (userEntry != null && getUserId().equals(userEntry.getID())) {
            return RespObj.FAILD("邮箱已经绑定自己了，无需再次绑定");
        }
        if (userEntry != null && !getUserId().equals(userEntry.getID())) {
            return RespObj.FAILD("邮箱被别人绑定了");
        }
        userService.updateUserEmail(getUserId(), email);
        return RespObj.SUCCESS("修改成功");
    }

    /**
     * 更改用户手机号
     *
     * @param mobile
     * @param code
     * @param cacheKeyId
     * @return
     */
    @RequestMapping("/changeUserPhone")
    @ResponseBody
    public RespObj changeUserPhone(String mobile, String code, String cacheKeyId) {

        UserEntry userEntry = userService.searchUserByphone(mobile);
        if (userEntry != null && getUserId().equals(userEntry.getID())) {
            return RespObj.FAILD("手机号是自己的，已经绑定了无需绑定");
        }

        if (userEntry != null && !getUserId().equals(userEntry.getID())) {
            return RespObj.FAILD("该手机号已经被别人绑定了");
        }

        String cacheKey = CacheHandler.getKeyString(CacheHandler.CACHE_SHORTMESSAGE, cacheKeyId);
        String value = CacheHandler.getStringValue(cacheKey);
        if (StringUtils.isBlank(value)) {
            return RespObj.FAILD("验证码失效，请重新获取");
        }
        String[] cache = value.split(",");
        if (!cache[1].equals(mobile)) {
            return RespObj.FAILD("注册失败：手机号码与验证码不匹配");
        }

        if (cache[0].equals(code)) {
            userService.updateUserPhone(getUserId(), mobile);
            return RespObj.SUCCESS("验证成功");
        }

        return RespObj.FAILD("验证失败");
    }

    /**
     * 第三方登录信息
     *
     * @return
     */
    @RequestMapping("/thirdLoginInfo")
    @ResponseBody
    public RespObj thirdLoginInfo() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("isBindQQ", userService.isBindQQ(getUserId()));
        map.put("isBindWechat", userService.isBindWechat(getUserId()));
        return RespObj.SUCCESS(map);
    }

    /**
     * 三种情况：
     * 1.手机号是自己的
     * 2.手机号被别人绑定
     * 3.手机号未被绑定
     *
     * @param mobile
     * @return
     */
    @RequestMapping("/checkPhoneCanUse")
    @ResponseBody
    public RespObj checkPhoneCanUse(String mobile) {
        UserEntry userEntry = userService.searchUserByphone(mobile);

        if (userEntry != null && userEntry.getID().equals(getUserId())) {
            return RespObj.FAILD("你已经绑定这个手机号了");
        }
        if (userEntry != null && !userEntry.getID().equals(getUserId())) {
            return RespObj.FAILD("手机被别人绑定了");
        }

        return RespObj.SUCCESS;
    }

    /**
     * 清空用户手机  -- 测试用
     *
     * @param userName
     * @param phone
     * @param email
     * @return
     */
    @RequestMapping("/cleanUserPhone")
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj cleanUserPhone(String userName, String phone, String email) {
        userService.cleanUserPhoneOrEmtail(userName, phone, email);
        return RespObj.SUCCESS;
    }
}
