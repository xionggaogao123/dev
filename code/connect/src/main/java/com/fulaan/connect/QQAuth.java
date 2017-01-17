package com.fulaan.connect;

import com.fulaan.ConnectException;
import com.fulaan.model.Sex;
import com.fulaan.model.UserInfo;
import com.fulaan.util.Util;
import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.qq.connect.utils.http.HttpClient;
import com.qq.connect.utils.http.PostParameter;

import java.io.Externalizable;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by moslpc on 2017/1/16.
 */
public class QQAuth implements Auth {

    private static Properties properties;
    private HttpClient client = new HttpClient();
    private String appId;
    private String appKey;
    private String redirectUrl;

    static {
        properties = new Properties();
        try {
            properties.load(QQAuth.class.getClassLoader().getResourceAsStream("qqconnectconfig.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public QQAuth() {
        this.appId = properties.getProperty("app_ID").trim();
        this.appKey = properties.getProperty("app_KEY").trim();
        this.redirectUrl = Util.strURLEncodeUTF8(properties.getProperty("redirect_URI").trim());
    }

    @Override
    public String getAuthUrl() {
        String authorizeURL = properties.getProperty("authorizeURL").trim();
        return authorizeURL + "?client_id=" + this.appId + "&redirect_uri=" + this.redirectUrl + "&response_type=" + "code" + "&state=" + "123456";
    }

    @Override
    public UserInfo getUserInfo(String authCode) throws ConnectException {
        try {
            AccessToken accessToken = getAccessTokenByRequest(authCode);
            if(accessToken == null) {
                throw new ConnectException();
            }
            OpenID openIDObj =  new OpenID(accessToken.getAccessToken());
            String openId = openIDObj.getUserOpenID();
            com.qq.connect.api.qzone.UserInfo qzoneUser = new com.qq.connect.api.qzone.UserInfo(accessToken.getAccessToken(), openId);
            UserInfoBean userInfoBean = qzoneUser.getUserInfo();
            UserInfo userInfo = new UserInfo();
            userInfo.setUniqueId(openId);
            userInfo.setNickName(userInfoBean.getNickname());
            userInfo.setSex(Sex.get(userInfoBean.getGender()));
            if(!"".equals(userInfoBean.getAvatar().getAvatarURL100())) {
                userInfo.setAvatar(userInfoBean.getAvatar().getAvatarURL100());
            } else if(!"".equals(userInfoBean.getAvatar().getAvatarURL50())) {
                userInfo.setAvatar(userInfoBean.getAvatar().getAvatarURL50());
            } else {
                userInfo.setAvatar(userInfoBean.getAvatar().getAvatarURL30());
            }
            return userInfo;
        } catch (QQConnectException e) {
            e.printStackTrace();
            throw new ConnectException();
        }
    }

    private AccessToken getAccessTokenByRequest(String authCode) {
        String accessTokenURL = properties.getProperty("accessTokenURL").trim();
        try {
            return new AccessToken(this.client.post(accessTokenURL, new PostParameter[]{new PostParameter("client_id", this.appId), new PostParameter("client_secret", this.appKey), new PostParameter("grant_type", "authorization_code"), new PostParameter("code", authCode), new PostParameter("redirect_uri", properties.getProperty("redirect_URI").trim())}, Boolean.TRUE));
        } catch (QQConnectException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {

        QQAuth qqAuth = new QQAuth();
        System.out.println(qqAuth.getAuthUrl());
    }
}
