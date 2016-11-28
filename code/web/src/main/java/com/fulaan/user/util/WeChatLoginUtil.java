package com.fulaan.user.util;

import com.fulaan.utils.JsonUtil;
import com.sys.constants.Constant;
import com.sys.utils.HttpClientUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by jerry on 2016/8/4.
 * 微信登录Util
 */
public class WeChatLoginUtil {

    private static Properties props = new Properties();

    /**
     * 获取微信的Access_Token
     *
     * @param code
     * @param appId
     * @param appSecret
     * @return
     */
    public static Map<String, Object> getAccess_Token(String code, String appId, String appSecret) {

        try {
            Map<String, String> parms = new HashMap<String, String>();
            parms.put("appid", appId);
            parms.put("secret", appSecret);
            parms.put("code", code);
            parms.put("grant_type", "authorization_code");
            String content = HttpClientUtils.get(Constant.WECHAT_ACCESS_TOKEN_URL, parms);

            Map<String, Object> maps = JsonUtil.Json2Map(content);
            return maps;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取微信个人信息
     *
     * @param access_token
     * @param openId
     * @return
     */
    public static Map<String, Object> getWeChatUserInfo(String access_token, String openId) {

        Map<String, String> userInfoParms = new HashMap<String, String>();
        userInfoParms.put("access_token", access_token);
        userInfoParms.put("openid", openId);
        try {
            String content = HttpClientUtils.get(Constant.WECHAT_GET_USERINFO_URL, userInfoParms);

            Map<String, Object> maps = JsonUtil.Json2Map(content);
            return maps;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getValue(String key) {
        return props.getProperty(key);
    }

    static {
        try {
            props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("properties/wechatconnect.properties"));
        } catch (FileNotFoundException var1) {
            var1.printStackTrace();
        } catch (IOException var2) {
            var2.printStackTrace();
        }

    }

}
