package com.fulaan.account.service;

import com.fulaan.cache.CacheHandler;
import com.pojo.user.UserEntry;
import com.sys.mails.MailUtils;
import com.sys.utils.MD5Utils;
import com.sys.utils.RespObj;
import org.springframework.stereotype.Service;

/**
 * Created by moslpc on 2016/12/12.
 */
@Service
public class AccountService {

    private static final String validateUrl = "http://fulaan.com/account/emailValidate.do?";

    public Boolean checkVerifyCode(String verifyCode, String verifyKey) {
        //验证码
        String validateCode;
        String vckey;
        //获得请求信息中的Cookie数据
        vckey = CacheHandler.getKeyString(CacheHandler.CACHE_VALIDATE_CODE, verifyKey);
        validateCode = CacheHandler.getStringValue(vckey);
        CacheHandler.deleteKey(CacheHandler.CACHE_VALIDATE_CODE, vckey);

        if (validateCode == null || "".equals(validateCode)) {
            return false;
        }
        verifyCode = verifyCode.toUpperCase();
        return !"".equals(verifyCode) && verifyCode.equals(validateCode);
    }

    public void processFindPasswordByEmail(String userName,String email, String code) {
        //发送邮箱
        MailUtils sendMail = new MailUtils();
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("<p>");
        stringBuffer.append(userName);
        stringBuffer.append("，您好！<br/>");
        String emailContent = validateUrl + "&email=" + email + "&validateCode=" + code;
        stringBuffer.append("您正在找回密码！点击以下链接验证您的帐号：<br/><a href=\"" + emailContent + "\"");
        stringBuffer.append(" >" + emailContent + "</a><br/>如果点击无效，请把下面网页地址复制到浏览器地址栏中打开<br/><br/>");
        stringBuffer.append("这是一封系统邮件，请勿回复</p>\n");
        try {
            sendMail.sendMail("用户邮箱验证", email, stringBuffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
