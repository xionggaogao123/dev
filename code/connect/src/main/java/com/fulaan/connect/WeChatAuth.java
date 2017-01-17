package com.fulaan.connect;

import com.fulaan.model.UserInfo;
import com.fulaan.util.Util;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by moslpc on 2017/1/16.
 */
public class WeChatAuth implements Auth {

    private static Properties properties;

    static {
        properties = new Properties();
        try {
            properties.load(QQAuth.class.getClassLoader().getResourceAsStream("wechatconnect.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getAuthUrl() {
        String appId = properties.getProperty("app_ID").trim();
        String redirectUrl = properties.getProperty("redirect_URI").trim();
        String connectURL = properties.getProperty("connectURL").trim();
        return String.format(connectURL, appId, Util.strURLEncodeUTF8(redirectUrl));
    }

    @Override
    public UserInfo getUserInfo(String uniqueId) {
        return null;
    }
}
