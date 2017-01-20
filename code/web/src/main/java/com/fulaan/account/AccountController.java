package com.fulaan.account;

import com.fulaan.account.dto.VerifyData;
import com.fulaan.account.service.AccountService;
import com.fulaan.annotation.LoginInfo;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.BaseController;
import com.fulaan.cache.CacheHandler;
import com.fulaan.playmate.service.MateService;
import com.fulaan.pojo.Validate;
import com.fulaan.user.model.ThirdType;
import com.fulaan.user.service.UserService;
import com.pojo.mobile.UserMobileEntry;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.MD5Utils;
import com.sys.utils.RespObj;
import com.sys.utils.ValidationUtils;
import fulaan.social.connect.Auth;
import fulaan.social.factory.AuthFactory;
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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private Auth qqAuth = AuthFactory.getQQAuth();
    private Auth wechatAuth = AuthFactory.getWechatAuth();

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
        return RespObj.SUCCESS(userService.findByMobile(phone) != null);
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
    public void QQLogin(HttpServletResponse response) throws IOException {
        String url = qqAuth.getAuthUrl();
        Cookie cookie = new Cookie("bindQQ", getUserId().toString());
        cookie.setMaxAge(Constant.SECONDS_IN_DAY);
        cookie.setPath(Constant.BASE_PATH);
        response.addCookie(cookie);
        response.sendRedirect(url);
    }

    /**
     * 进行微信绑定操作
     */
    @SessionNeedless
    @RequestMapping(value = "/wechatBind")
    public void weChatLogin(HttpServletResponse response) throws IOException {
        String strWeChatConnectUrl = wechatAuth.getAuthUrl();
        Cookie cookie = new Cookie("bindWechat", getUserId().toString());
        cookie.setMaxAge(Constant.SECONDS_IN_DAY);
        cookie.setPath(Constant.BASE_PATH);
        response.addCookie(cookie);
        response.sendRedirect(strWeChatConnectUrl);
    }

    /**
     * 绑定 手机号
     *
     * @param phone
     * @param code
     * @param cacheKeyId
     * @return
     */
    @RequestMapping("/bindPhoneNumber")
    @ResponseBody
    public RespObj bindPhone(String phone, String code, String cacheKeyId) {
        UserEntry userEntry = userService.findById(getUserId());
        if (phone.equals(userEntry.getMobileNumber())) {
            return RespObj.FAILDWithErrorMsg("你已经绑定了此手机号，无需再次绑定");
        }
        Validate validate = userService.validatePhoneNumberCode(phone, code, cacheKeyId);
        if (!validate.isOk()) {
            return RespObj.FAILDWithErrorMsg(validate.getMessage());
        }
        Validate bindValidate = accountService.bindMobile(getUserId(), phone);
        if (bindValidate.isOk()) {
            userService.updateUserMobile(getUserId(), phone);
            return RespObj.SUCCESS("绑定成功");
        }
        return RespObj.FAILDWithErrorMsg(bindValidate.getMessage());
    }

    /**
     * 列出绑定的用户名
     *
     * @param phone
     * @return
     */
    @SessionNeedless
    @ResponseBody
    @RequestMapping("/listBindUserName")
    public RespObj listBindUserName(String phone) {
        List<String> userNameList = new ArrayList<String>();
        UserMobileEntry userMobileEntry = accountService.findByMobile(phone);
        if (userMobileEntry == null) {
            return RespObj.FAILDWithErrorMsg("老账号请直接登录");
        }
        List<ObjectId> userIds = userMobileEntry.getUserIds();
        List<UserEntry> userEntries = userService.getUserByList(userIds);
        for (UserEntry userEntry1 : userEntries) {
            userNameList.add(userEntry1.getUserName());
        }
        return RespObj.SUCCESS(userNameList);
    }

    /**
     * 验证用户名与验证码
     */
    @RequestMapping("/verifyAccount")
    @SessionNeedless
    @ResponseBody
    public RespObj verifyAccount(String name, String verifyCode, @CookieValue(Constant.COOKIE_VALIDATE_CODE) String verifyKey) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (!accountService.checkVerifyCode(verifyCode, verifyKey)) {
            return RespObj.FAILDWithErrorMsg("验证码不正确");
        }
        if (ValidationUtils.isEmail(name)) {
            UserEntry e = userService.findByEmail(name);
            if (e == null) {
                return RespObj.FAILDWithErrorMsg("账号不存在");
            }
            result.put("type", "email");
            result.put("email", name);
            result.put("userName", e.getUserName());
            result.put("phone", e.getMobileNumber());
            return RespObj.SUCCESS(result);
        }
        if (ValidationUtils.isMobile(name)) {
            UserMobileEntry mobileEntry = accountService.findByMobile(name);
            if (mobileEntry != null) {
                List<ObjectId> userIdList = mobileEntry.getUserIds();
                List<UserDetailInfoDTO> userDetailInfoDTOS = userService.findUserInfoByIds(userIdList);
                result.put("users", userDetailInfoDTOS);
                result.put("type", "mobile");
                result.put("mobile", name);
                result.put("protectedMobile", getProtectedMobile(name));
                return RespObj.SUCCESS(result);
            }
        }
        UserEntry userEntry = userService.findByUserName(name);
        if (userEntry != null) {
            result.put("type", "userName");
            result.put("userName", name);
            result.put("email", userEntry.getEmail());
            result.put("mobile", userEntry.getMobileNumber());
            result.put("protectedMobile", getProtectedMobile(userEntry.getMobileNumber()));
            if (StringUtils.isBlank(userEntry.getEmail()) && StringUtils.isBlank(userEntry.getMobileNumber())) {
                return RespObj.FAILDWithErrorMsg("此账号无法通过邮箱和手机找回!");
            }
            return RespObj.SUCCESS(result);
        }
        if (ValidationUtils.isMobile(name)) {
            userEntry = userService.findByMobile(name);
            result.put("type", "userName");
            result.put("userName", userEntry.getUserName());
            result.put("email", userEntry.getEmail());
            result.put("mobile", userEntry.getMobileNumber());
            result.put("protectedMobile", getProtectedMobile(name));
            return RespObj.SUCCESS(result);
        }
        return RespObj.FAILDWithErrorMsg("失败");
    }

    /**
     * 验证 手机号 验证码是否匹配
     *
     * @param phone
     * @param code
     * @param cacheKeyId
     * @return
     */
    @SessionNeedless
    @RequestMapping("/phoneValidate")
    @ResponseBody
    public RespObj phoneValidate(String phone, String code, String cacheKeyId) {
        Validate validate = userService.validatePhoneNumberCode(phone, code, cacheKeyId);
        if (!validate.isOk()) {
            return RespObj.FAILDWithErrorMsg(validate.getMessage());
        }
        return RespObj.SUCCESS("验证成功");
    }

    /**
     * 验证用户名 和 手机号是否匹配
     *
     * @param userName
     * @param phone
     * @return
     */
    @SessionNeedless
    @RequestMapping("/verifyUserNameAndPhone")
    @ResponseBody
    public RespObj verifyUserNameAndPhone(String userName, String phone) {
        UserEntry userEntry = userService.findByUserName(userName);
        if (StringUtils.isNotBlank(phone) && phone.equals(userEntry.getMobileNumber())) {
            return RespObj.SUCCESS;
        }
        return RespObj.FAILDWithErrorMsg("验证失败");
    }

    /**
     * 发送验证邮件
     *
     * @param email
     * @return
     */
    @SessionNeedless
    @RequestMapping("/sendVerifyEmail")
    @ResponseBody
    public RespObj sendVerifyEmail(String email) {
        UserEntry user = userService.findByEmail(email);
        if (user == null) {
            return RespObj.FAILDWithErrorMsg("邮箱未绑定账号");
        }
        ObjectId key = new ObjectId();
        String cacheKey = CacheHandler.getKeyString(CacheHandler.CACHE_FW_VALIDATE_EMAIL, key.toString());
        CacheHandler.cache(cacheKey, email, Constant.SECONDS_IN_DAY);//1天
        accountService.sendEmailForFindPassword(user.getUserName(), email, key.toString());
        return RespObj.SUCCESS(new VerifyData(true, "发送成功"));
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
        response.addCookie(new Cookie(Constant.FW_RESET_PASSWORD_CODE, validateCode));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("verify", true);
        return new ModelAndView("/account/findPassword", map);
    }

    /**
     * 通过邮箱重置密码
     *
     * @param password
     * @param validateCode
     * @return
     */
    @SessionNeedless
    @RequestMapping("/resetPasswordByEmail")
    @ResponseBody
    public RespObj resetPasswordByEmail(String password, @CookieValue(value = Constant.FW_RESET_PASSWORD_CODE) String validateCode) {
        String cacheKey = CacheHandler.getKeyString(CacheHandler.CACHE_FW_VALIDATE_EMAIL, validateCode);
        if (StringUtils.isBlank(cacheKey)) {
            return RespObj.FAILDWithErrorMsg("链接已失效");
        }
        String email = CacheHandler.getStringValue(cacheKey);
        UserEntry e = userService.findByEmail(email);
        userService.resetPassword(e.getID(), MD5Utils.getMD5String(password));
        CacheHandler.deleteKey(CacheHandler.CACHE_FW_VALIDATE_EMAIL, validateCode);
        return RespObj.SUCCESS("重置密码成功");
    }

    /**
     * 重置密码
     *
     * @param userName
     * @param phone
     * @param code
     * @param cacheKeyId
     * @param password
     * @return
     */
    @SessionNeedless
    @RequestMapping("/resetPassword")
    @ResponseBody
    public RespObj resetPassword(String userName, String phone, String code, String cacheKeyId, String password) {
        Validate validate = userService.validatePhoneNumberCode(phone, code, cacheKeyId);
        if (!validate.isOk()) {
            return RespObj.FAILDWithErrorMsg(validate.getMessage());
        }
        UserEntry userEntry = userService.findByUserName(userName);
        if (userEntry == null) {
            return RespObj.FAILDWithErrorMsg("用户不存在");
        }
        if (!userEntry.getMobileNumber().equals(phone)) {
            return RespObj.FAILDWithErrorMsg("手机号与用户不匹配");
        }
        userService.resetPassword(userEntry.getID(), MD5Utils.getMD5String(password));
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
     * 解除第三方绑定
     *
     * @param type
     * @return
     */
    @RequestMapping("/removeThirdBind")
    @ResponseBody
    public RespObj removeThirdBind(int type) {
        userService.removeThirdBind(getUserId(), ThirdType.getThirdType(type));
        return RespObj.SUCCESS;
    }

    /**
     * 获取账户信息
     *
     * @param userName
     * @return
     */
    @RequestMapping("/accountSafeInfo")
    @ResponseBody
    @SessionNeedless
    public RespObj accountSafeInfo(String userName) {
        UserDetailInfoDTO user = userService.getUserInfoByUserName(userName);
        if (user == null) {
            return RespObj.FAILD("用户不存在");
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("userName", user.getUserName());
        result.put("nickName", user.getNickName());
        result.put("sex", user.getSex());
        result.put("phone", getProtectedMobile(user.getMobileNumber()));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(user.getBirthDate());
        String[] dateArr = date.split("-");
        int year = Integer.valueOf(dateArr[0]);
        int month = Integer.valueOf(dateArr[1]);
        int day = Integer.valueOf(dateArr[2]);

        result.put("year", year);
        result.put("month", month);
        result.put("day", day);
        result.put("avatar", user.getImgUrl());

        result.put("email", user.getEmail());
        return RespObj.SUCCESS(result);
    }


    @RequestMapping("/unsetData")
    @ResponseBody
    public RespObj unsetPhone(String phone, String email) {
        if (StringUtils.isNotBlank(phone)) {
            userService.clearUserPhone(phone);
            accountService.clearPhone(phone);
        }
        if (StringUtils.isNotBlank(email)) {
            userService.clearUserEmail(email);
        }
        return RespObj.SUCCESS;
    }

    /**
     * 验证手机号
     *
     * @param phone
     * @param code
     * @param cacheKeyId
     * @return
     */
    @RequestMapping("/validatePhone")
    @SessionNeedless
    @ResponseBody
    public RespObj validatePhone(String phone, String code, String cacheKeyId) {
        Validate validate = userService.validatePhoneNumberCode(phone, code, cacheKeyId);
        if (!validate.isOk()) {
            return RespObj.FAILDWithErrorMsg(validate.getMessage());
        }
        return RespObj.SUCCESS("验证成功");
    }
}
