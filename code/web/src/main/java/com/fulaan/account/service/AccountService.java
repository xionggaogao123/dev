package com.fulaan.account.service;

import com.db.mobile.UserMobileDao;
import com.fulaan.base.BaseService;
import com.fulaan.cache.CacheHandler;
import com.fulaan.pojo.Validate;
import com.pojo.mobile.UserMobileEntry;
import com.sys.mails.MailUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

/**
 * Created by jerry on 2016/12/12.
 * 账户 Service
 */
@Service
public class AccountService extends BaseService {

    private static final String validateUrl = "http://fulaan.com/account/emailValidate.do?";

    private UserMobileDao userMobileDao = new UserMobileDao();

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

    public void sendEmailForFindPassword(String userName, String email, String code) {
        //发送邮箱
        MailUtils sendMail = new MailUtils();
        StringBuilder stringBuffer = getEmailText(userName, email, code);
        try {
            sendMail.sendMail("用户邮箱验证", email, stringBuffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private StringBuilder getEmailText(String userName, String email, String code) {
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("<p>");
        stringBuffer.append(userName);
        stringBuffer.append("，您好！<br/>");
        String emailContent = validateUrl + "&email=" + email + "&validateCode=" + code;
        stringBuffer.append("您正在找回密码！点击以下链接验证您的帐号：<br/><a href=\"" + emailContent + "\"");
        stringBuffer.append(" >" + emailContent + "</a><br/>如果点击无效，请把下面网页地址复制到浏览器地址栏中打开<br/><br/>");
        stringBuffer.append("这是一封系统邮件，请勿回复</p>\n");
        return stringBuffer;
    }

    public Validate bindMobile(ObjectId userId, String mobile) {
        Validate validate = new Validate();
        if (!userMobileDao.isCanBind(mobile)) {
            validate.setMessage("手机号绑定已达三个");
            return validate;
        }
        if (userMobileDao.isExist(mobile)) {
            userMobileDao.pushUserId(mobile, userId);
        } else {
            UserMobileEntry userMobileEntry = new UserMobileEntry(mobile, userId);
            userMobileDao.save(userMobileEntry);
        }
        validate.setOk(true);
        return validate;
    }

    public UserMobileEntry findByMobile(String mobile) {
        return userMobileDao.findByMobile(mobile);
    }
}
