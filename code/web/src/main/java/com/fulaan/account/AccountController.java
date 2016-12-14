package com.fulaan.account;

import com.fulaan.account.dto.VerifyData;
import com.fulaan.account.service.AccountService;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.cache.CacheHandler;
import com.fulaan.controller.BaseController;
import com.fulaan.dto.UserDTO;
import com.fulaan.user.service.UserService;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.exceptions.UnLoginException;
import com.sys.utils.MD5Utils;
import com.sys.utils.RespObj;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

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


    @RequestMapping("/register")
    @SessionNeedless
    public String register() {
        return "/account/register";
    }

    @RequestMapping("/login")
    @SessionNeedless
    public String login() {
        return "/account/login";
    }

    @RequestMapping("/findPassword")
    @SessionNeedless
    public String findPassword() {
        return "/account/findPassword";
    }

    @RequestMapping("/accountSafe")
    @SessionNeedless
    public String accountSafe() {
        return "/account/accountSafe";
    }

    @RequestMapping("/userNameCheck")
    @SessionNeedless
    @ResponseBody
    public RespObj userNameCheck(String userName) {
        return RespObj.SUCCESS(userService.checkUserNameExist(userName));
    }

    @RequestMapping("/userPhoneCheck")
    @SessionNeedless
    @ResponseBody
    public RespObj userPhoneCheck(String phone) {
        return RespObj.SUCCESS(userService.searchUserByphone(phone) != null);
    }

    @RequestMapping("/userEmailCheck")
    @SessionNeedless
    @ResponseBody
    public RespObj userEmailCheck(String email) {
        return RespObj.SUCCESS(userService.searchUserByEmail(email) != null);
    }

    /**
     * 验证用户名与验证码
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
            String cacheKey = CacheHandler.getKeyString(CacheHandler.CACHE_FW_VALIDATE_EMAIL,key.toString());
            CacheHandler.cache(cacheKey, userDTO.getUserName(), Constant.SECONDS_IN_DAY);//1天
            accountService.processFindPasswordByEmail(userDTO.getUserName(), email, key.toString());
            return RespObj.SUCCESS(new VerifyData(true, "发送成功"));
        } else {
            return RespObj.FAILD(new VerifyData(false, "该用户未绑定此邮箱"));
        }
    }

    /**
     * 手机验证
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
    public RespObj  phoneValidate(String phone,String code,String cacheKeyId,@CookieValue(Constant.FWCODE) String fwCode,HttpServletResponse response) {
        String fwUser = CacheHandler.getStringValue(CacheHandler.getKeyString(CacheHandler.CACHE_FW_USERNAME_KEY, fwCode));
        if (fwUser == null) {
            return RespObj.FAILD(new VerifyData(false, "时间失效或不存在"));
        }
        UserDTO userDTO = userService.findByRegular(fwUser);
        if (userDTO == null) {
            return RespObj.FAILD(new VerifyData(false, "用户不存在"));
        }
        if(!phone.equals(userDTO.getPhone())) {
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
            String resetCacheKey = CacheHandler.getKeyString(CacheHandler.CACHE_FW_RESET_PASSWORD,keyId.toString());
            CacheHandler.cache(resetCacheKey,userDTO.getUserName(),Constant.SESSION_FIVE_MINUTE);
            response.addCookie(new Cookie(Constant.FW_RESET_PASSWORD_CODE,keyId.toString()));
            CacheHandler.deleteKey(CacheHandler.CACHE_SHORTMESSAGE, cacheKeyId);
            return RespObj.SUCCESS("验证成功");
        } else {
            return RespObj.FAILD("短信验证码输入错误");
        }
    }

    /**
     * 邮箱验证
     * @param validateCode
     * @param response
     * @return
     * @throws Exception
     */
    @SessionNeedless
    @RequestMapping("/emailValidate")
    public ModelAndView emailValidate(String validateCode, HttpServletResponse response) throws Exception{
        String cacheKey = CacheHandler.getKeyString(CacheHandler.CACHE_FW_VALIDATE_EMAIL,validateCode);
        String userName = CacheHandler.getStringValue(cacheKey);
        if(userName == null) {
            throw new UnLoginException();
        }

        CacheHandler.deleteKey(CacheHandler.CACHE_FW_VALIDATE_EMAIL,validateCode);
        ObjectId key = new ObjectId();
        String resetPassKey = CacheHandler.getKeyString(CacheHandler.CACHE_FW_RESET_PASSWORD,key.toString());
        CacheHandler.cache(resetPassKey,userName,Constant.SESSION_FIVE_MINUTE);
        response.addCookie(new Cookie(Constant.FW_RESET_PASSWORD_CODE,key.toString()));
        return new ModelAndView("/account/findPassword","verify",true);
    }

    /**
     * 重置密码
     * @param password
     * @param resetCode
     * @return
     */
    @SessionNeedless
    @RequestMapping("/resetPassword")
    @ResponseBody
    public RespObj resetPassword(String password,@CookieValue(Constant.FW_RESET_PASSWORD_CODE) String resetCode) {
        String cacheKey = CacheHandler.getKeyString(CacheHandler.CACHE_FW_RESET_PASSWORD,resetCode);
        String userName = CacheHandler.getStringValue(cacheKey);
        if(userName == null) {
            return RespObj.FAILD("时间失效或不合法");
        }
        UserEntry userEntry = userService.searchUserByUserName(userName);
        if(userEntry == null) {
            return RespObj.FAILD("用户不存在");
        }
        userService.resetPassword(userEntry.getID(),MD5Utils.getMD5String(password));
        CacheHandler.deleteKey(CacheHandler.CACHE_FW_RESET_PASSWORD,resetCode);
        return RespObj.SUCCESS("重置密码成功");
    }

    /**
     * 第三方登录成功
     * @return
     */
    @RequestMapping("/thirdLoginSuccess")
    @SessionNeedless
    public String thirdLoginSuccess() {
        return "/account/thirdLoginSuccess";
    }
}
