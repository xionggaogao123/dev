package com.fulaan.user.util;

import com.fulaan.utils.JsonUtil;
import com.sys.utils.HttpClientUtils;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by jerry on 2016/8/12.
 * QQ 登录 Util
 * 主要用于PC端
 */
public class QQLoginUtil {

    private static final Logger logger = Logger.getLogger(QQLoginUtil.class);

    private static Properties props = new Properties();

    /**
     * 获取QQ Accesstoken
     *
     * @param code QQ Code
     * @return Map
     */
    public static Map<String, String> getAccessToken(String code) {

        Map<String, String> maps = new HashMap<String, String>();
        maps.put("code", code);
        maps.put("grant_type", "authorization_code");
        maps.put("client_id", getValue("app_ID"));
        maps.put("client_secret", getValue("app_KEY"));
        maps.put("redirect_uri", getValue("redirect_URI"));

        try {
            String response = HttpClientUtils.get(getValue("accessTokenURL"), maps);
            logger.info(response + "----");
            String[] result = response.split("&");

            Map<String, String> returnMap = new HashMap<String, String>();
            for (String str : result) {
                String[] splitStr = str.split("=");
                returnMap.put(splitStr[0], splitStr[1]);
            }
            logger.info(response + "----");
            return returnMap;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取QQ openID
     *
     * @param accessToken QQ的accessToken
     * @return 键值对
     */
    public static Map<String, Object> getOpenId(String accessToken) {

        Map<String, String> parms = new HashMap<String, String>();
        parms.put("access_token", accessToken);
        try {
            String content = HttpClientUtils.get(getValue("getOpenIDURL"), parms);

            int start = content.indexOf("(");
            int end = content.indexOf(")");

            String result = content.substring(start + 1, end);
            logger.info(result + "----");
            Map<String, Object> maps = JsonUtil.json2Map(result);

            logger.info(maps + "===");
            return maps;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取QQ 个人信息
     *
     * @param access_token
     * @param openId
     * @return
     */
    public static Map<String, Object> getUserInfo(String access_token, String openId) {

        Map<String, String> parms = new HashMap<String, String>();
        parms.put("access_token", access_token);
        parms.put("oauth_consumer_key", getValue("app_ID"));
        parms.put("openid", openId);

        try {
            String content = HttpClientUtils.get(getValue("getUserInfoURL"), parms);

            Map<String, Object> maps = JsonUtil.json2Map(content);

            logger.info(maps + "===");
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
            props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("properties/qqconnect.properties"));
        } catch (FileNotFoundException var1) {
            var1.printStackTrace();
        } catch (IOException var2) {
            var2.printStackTrace();
        }

    }

}
