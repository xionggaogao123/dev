package com.fulaan.account;

import com.fulaan.account.dto.VerifyData;
import com.fulaan.account.service.AccountService;
import com.fulaan.annotation.LoginInfo;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.annotation.UserRoles;
import com.fulaan.base.BaseController;
import com.fulaan.cache.CacheHandler;
import com.fulaan.dto.UserDTO;
import com.fulaan.playmate.service.MateService;
import com.fulaan.user.model.ThirdType;
import com.fulaan.user.service.UserService;
import com.fulaan.user.util.QQLoginUtil;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.exceptions.UnLoginException;
import com.sys.utils.DateTimeUtils;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jerry on 2016/12/12.
 * 账户 Controller
 * 修改密码，登录，账户安全，注册界面
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
     *
     * @return register page
     */
    @RequestMapping("/register")
    @SessionNeedless
    public String registerPage() {
        return "/account/register";
    }

    /**
     * 登录界面
     *
     * @return login page
     */
    @RequestMapping("/login")
    @SessionNeedless
    public String loginPage() {
        return "/account/login";
    }

    /**
     * 找回密码
     *
     * @return findPassword page
     */
    @RequestMapping("/findPassword")
    @SessionNeedless
    public String findPasswordPage() {
        return "/account/findPassword";
    }

    /**
     * 我的账户
     *
     * @return account page
     */
    @RequestMapping("/accountSafe")
    @LoginInfo
    public String accountSafePage() {
        return "/account/accountSafe";
    }

    /**
     * 第三方登录成功
     *
     * @return page
     */
    @RequestMapping("/thirdLoginSuccess")
    @SessionNeedless
    public String thirdLoginSuccessPage(@RequestParam(value = "bindSuccess", required = false, defaultValue = "-1") int bindSuccess, Model model) {
        model.addAttribute("bindSuccess", bindSuccess);
        return "/account/thirdLoginSuccess";
    }

    /**
     * 检查用户名是否可用
     */
    @RequestMapping("/userNameCheck")
    @SessionNeedless
    @ResponseBody
    public RespObj userNameCheck(String userName) {
        return RespObj.SUCCESS(userService.checkUserNameExist(userName));
    }

    /**
     * 检查手机号是否可用
     */
    @RequestMapping("/userPhoneCheck")
    @SessionNeedless
    @ResponseBody
    public RespObj userPhoneCheck(String phone) {
        return RespObj.SUCCESS(userService.findByPhone(phone) != null);
    }

    /**
     * 检查邮箱是否可用
     */
    @RequestMapping("/userEmailCheck")
    @SessionNeedless
    @ResponseBody
    public RespObj userEmailCheck(String email) {
        return RespObj.SUCCESS(userService.findByEmail(email) != null);
    }

    /**
     * 进行QQ 绑定
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
     */
    @SessionNeedless
    @RequestMapping(value = "/wechatBind")
    public void weChatLogin(HttpServletResponse response) throws IOException {
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
            accountService.sendEmailForFindPassword(userDTO.getUserName(), email, key.toString());
            return RespObj.SUCCESS(new VerifyData(true, "发送成功"));
        } else {
            return RespObj.FAILD(new VerifyData(false, "该用户未绑定此邮箱"));
        }
    }

    /**
     * 手机验证
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
        UserEntry userEntry = userService.findByUserName(userName);
        if (userEntry == null) {
            return RespObj.FAILD("用户不存在");
        }
        userService.resetPassword(userEntry.getID(), MD5Utils.getMD5String(password));
        CacheHandler.deleteKey(CacheHandler.CACHE_FW_RESET_PASSWORD, resetCode);
        return RespObj.SUCCESS("重置密码成功");
    }

    /**
     * 更新出生年月 - 性别 - 昵称
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
                long birTimeStamp = DateTimeUtils.dateTime(year, month, day);
                userService.update(getUserId(), "bir", birTimeStamp);
                mateService.updateAged(getUserId(), birTimeStamp);
            } catch (Exception e) {
                e.printStackTrace();
                return RespObj.FAILD(e.getMessage());
            }
        }

        if (StringUtils.isNotBlank(nickName)) {
            userService.updateNickNameById(getUserId().toString(), nickName);
        }
        userService.updateSexById(getUserId(), sex);
        return RespObj.SUCCESS;
    }

    /**
     * 检查用户密码
     */
    @RequestMapping("/checkUserPassword")
    @ResponseBody
    public RespObj checkUserPassword(String password) {
        ObjectId userId = getUserId();
        UserEntry userEntry = userService.findById(userId);
        try {
            if (MD5Utils.getMD5(password).equals(userEntry.getPassword())) {
                return RespObj.SUCCESS;
            }

            if (password.equals(userEntry.getPassword())) {
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
     */
    @RequestMapping("/changeUserEmail")
    @ResponseBody
    public RespObj changeUserEmail(String email) {
        UserEntry userEntry = userService.findByEmail(email);
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
     */
    @RequestMapping("/changeUserPhone")
    @ResponseBody
    public RespObj changeUserPhone(String mobile, String code, String cacheKeyId) {
        UserEntry userEntry = userService.findByPhone(mobile);
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
     */
    @RequestMapping("/checkPhoneCanUse")
    @ResponseBody
    public RespObj checkPhoneCanUse(String mobile) {
        UserEntry userEntry = userService.findByPhone(mobile);
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
     */
    @RequestMapping("/cleanUserPhone")
    @ResponseBody
    @UserRoles(UserRole.DISCUSS_MANAGER)
    public RespObj cleanUserPhone(String userName) {
        userService.clearUserPhoneAndEmail(userName);
        return RespObj.SUCCESS;
    }

    /**
     * 解除第三方绑定
     * @param type
     * @return
     */
    @RequestMapping("/removeThirdBind")
    @ResponseBody
    public RespObj removeThirdBind(int type) {
        UserDetailInfoDTO user = userService.getUserInfoById(getUserId().toString());
        if("*".equals(user.getPassWord())) {
            return RespObj.FAILD("不能解除绑定");
        }
        userService.removeThirdBind(getUserId(), ThirdType.getThirdType(type));
        return RespObj.SUCCESS;
    }

    /**
     * 获取账户信息
     * @param userName
     * @return
     */
    @RequestMapping("/accountSafeInfo")
    @ResponseBody
    @SessionNeedless
    public RespObj accountSafeInfo(String userName) {
        UserDetailInfoDTO user = userService.getUserInfoByUserName(userName);
        if(user == null) {
            return RespObj.FAILD("用户不存在");
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("userName", user.getUserName());
        result.put("nickName", user.getNickName());
        result.put("sex", user.getSex());
        result.put("phone",getProtectedMobile(user.getMobileNumber()));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(user.getBirthDate());
        String[] dateArr = date.split("-");
        int year = Integer.valueOf(dateArr[0]);
        int month = Integer.valueOf(dateArr[1]);
        int day = Integer.valueOf(dateArr[2]);

        result.put("year", year);
        result.put("month", month);
        result.put("day", day);
        result.put("avatar",user.getImgUrl());

        result.put("email",user.getEmail());
        return RespObj.SUCCESS(result);
    }
}
