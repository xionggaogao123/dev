package com.fulaan.account;

import com.fulaan.account.dto.VerifyData;
import com.fulaan.account.service.AccountService;
import com.fulaan.annotation.LoginInfo;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.BaseController;
import com.fulaan.cache.CacheHandler;
import com.fulaan.dto.UserDTO;
import com.fulaan.playmate.service.MateService;
import com.fulaan.pojo.Validate;
import com.fulaan.user.model.ThirdType;
import com.fulaan.user.service.UserService;
import com.fulaan.user.util.QQLoginUtil;
import com.fulaan.util.Validator;
import com.pojo.mobile.UserMobileEntry;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
     * @return getUserEntryByAccount page
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
     * 绑定 手机号
     *
     * @param phone
     * @param code
     * @param cacheKeyId
     * @return
     */
    @RequestMapping("/bindPhoneNumber")
    @ResponseBody
    public Map<String, Object> bindPhone(String phone, String code, String cacheKeyId) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", "500");
        Validate validate = userService.validatePhoneNumber(phone, code, cacheKeyId);
        if (!validate.isOk()) {
            result.put("message", validate.getMessage());
            return result;
        }
        Validate bindValidate = accountService.bindMobile(getUserId(), phone);
        if (bindValidate.isOk()) {
            result.put("code", "200");
            result.put("message", "绑定成功");
            userService.updateBindUserMobile(getUserId(), phone);
        } else {
            result.put("message", bindValidate.getMessage());
        }
        return result;
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
            return RespObj.FAILD("验证码不正确");
        }
        Pattern userNamePattern = Pattern.compile(Validator.REGEX_USERNAME);
        if (userNamePattern.matcher(name).matches()) {
            if (userService.findByUserName(name) == null) {
                return RespObj.FAILDWithErrorMsg("账号不存在");
            }
            result.put("type", "userName");
            result.put("userName", name);
            return RespObj.SUCCESS(result);
        }
        Pattern emailPattern = Pattern.compile(Validator.REGEX_EMAIL);
        if (emailPattern.matcher(name).matches()) {
            UserEntry e = userService.findByEmail(name);
            if (e == null) {
                return RespObj.FAILDWithErrorMsg("账号不存在");
            }
            result.put("type", "email");
            result.put("email", name);
            result.put("userName", e.getUserName());
            return RespObj.SUCCESS(result);
        }
        Pattern mobilePattern = Pattern.compile(Validator.REGEX_MOBILE);
        if (mobilePattern.matcher(name).matches()) {
            if (userService.findByMobile(name) == null) {
                return RespObj.FAILDWithErrorMsg("账号不存在");
            }
            UserMobileEntry mobileEntry = accountService.findByMobile(name);
            if (mobileEntry != null) {
                List<ObjectId> userIdList = mobileEntry.getUserIds();
                List<UserDetailInfoDTO> userDetailInfoDTOS = userService.findUserInfoByIds(userIdList);
                result.put("users", userDetailInfoDTOS);
            }
            result.put("type", "mobile");
            result.put("mobile", name);
            result.put("protectedMobile", getProtectedMobile(name));
        }

        return RespObj.SUCCESS(result);
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
    public RespObj phoneValidate(String phone, String code, String cacheKeyId) {
        Validate validate = userService.validatePhoneNumber(phone, code, cacheKeyId);
        if (!validate.isOk()) {
            return RespObj.FAILDWithErrorMsg(validate.getMessage());
        }
        return RespObj.SUCCESS("验证成功");
    }

    /**
     * 邮箱验证
     */
    @SessionNeedless
    @RequestMapping("/emailValidate")
    public ModelAndView emailValidate(String userName, String validateCode, HttpServletResponse response) throws Exception {
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
    public RespObj resetPassword(String userName, String phone, String code, String cacheKeyId, String password) {
        Validate validate = userService.validatePhoneNumber(phone, code, cacheKeyId);
        if (!validate.isOk()) {
            return RespObj.FAILDWithErrorMsg(validate.getMessage());
        }
        UserEntry userEntry = userService.findByUserName(userName);
        if (userEntry == null) {
            return RespObj.FAILDWithErrorMsg("用户不存在");
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
    public RespObj unsetPhone(String phone,String email) {
        if(StringUtils.isNotBlank(phone)) {
            userService.clearUserPhone(phone);
            accountService.clearPhone(phone);
        }
        if(StringUtils.isNotBlank(email)) {
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
        Validate validate = userService.validatePhoneNumber(phone, code, cacheKeyId);
        if (!validate.isOk()) {
            return RespObj.FAILDWithErrorMsg(validate.getMessage());
        }
        return RespObj.SUCCESS("验证成功");
    }
}
