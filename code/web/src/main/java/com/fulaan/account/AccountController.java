package com.fulaan.account;

import com.fulaan.account.service.AccountService;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.controller.BaseController;
import com.fulaan.user.service.UserService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public RespObj verifyCodeWithName(String name, String verifyCode, @CookieValue(Constant.COOKIE_VALIDATE_CODE) String verifyKey) {

        if (!accountService.checkVerifyCode(verifyCode, verifyKey)) {
            return RespObj.FAILD("验证码不正确");
        }

        if (userService.find(name) == null) {
            return RespObj.FAILD("用户不存在");
        }
        return RespObj.SUCCESS;
    }

    @RequestMapping("/validateData")
    @ResponseBody
    public RespObj validateData() {
        return RespObj.SUCCESS;
    }
}
