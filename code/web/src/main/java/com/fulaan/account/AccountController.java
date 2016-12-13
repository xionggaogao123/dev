package com.fulaan.account;

import com.fulaan.account.service.AccountService;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.cache.CacheHandler;
import com.fulaan.controller.BaseController;
import com.fulaan.user.service.UserService;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
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

    @RequestMapping("/verifyCodeWithName")
    @SessionNeedless
    @ResponseBody
    public RespObj verifyCodeWithName(String name, String verifyCode, @CookieValue(Constant.COOKIE_VALIDATE_CODE) String verifyKey, HttpServletResponse response) {

        if (!accountService.checkVerifyCode(verifyCode, verifyKey)) {
            return RespObj.FAILD("验证码不正确");
        }

        if (userService.find(name) == null) {
            return RespObj.FAILD("用户不存在");
        }
        ObjectId key = new ObjectId();
        String cacheKey = CacheHandler.getKeyString(CacheHandler.CACHE_FW_USERNAME_KEY, key.toString());
        CacheHandler.cache(cacheKey, name, Constant.SESSION_FIVE_MINUTE);//5分钟
        response.addCookie(new Cookie("fw_code",key.toString()));
        return RespObj.SUCCESS;
    }

    @SessionNeedless
    @RequestMapping("/verifyUserPhone")
    @ResponseBody
    public RespObj verifyUserPhone(String phone,@CookieValue(Constant.FWCODE) String fwCode) {
        Map<String,Object> result = new HashMap<String,Object>();
        String fwUser = CacheHandler.getStringValue(CacheHandler.getKeyString(CacheHandler.CACHE_FW_USERNAME_KEY,fwCode));
        if(fwUser == null) {
            result.put("verify",false);
            result.put("msg","时间失效或不存在");
            return RespObj.FAILD(result);
        }
        UserEntry userEntry = userService.searchUserByphone(phone);
        if(userEntry == null) {
            result.put("verify",false);
            result.put("msg","手机未绑定");
            return RespObj.FAILD(result);
        }
        if(fwUser.equals(userEntry.getUserName())) {
            result.put("verify",true);
            result.put("msg","验证成功");
            return RespObj.SUCCESS(result);
        } else {
            result.put("verify",false);
            result.put("msg","该用户未绑定此手机号");
            return RespObj.FAILD(result);
        }
    }

    @RequestMapping("/validateData")
    @ResponseBody
    public RespObj validateData() {
        return RespObj.SUCCESS;
    }

    @RequestMapping("/thirdLoginSuccess")
    @SessionNeedless
    public String thirdLoginSuccess() {
        return "/account/thirdLoginSuccess";
    }
}
