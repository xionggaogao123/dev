package com.fulaan.account.service;

import com.fulaan.cache.CacheHandler;
import org.springframework.stereotype.Service;

/**
 * Created by moslpc on 2016/12/12.
 */
@Service
public class AccountService {

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
}
