package com.fulaan.newVersionBind.dto;

/**
 * Created by scott on 2017/12/14.
 */
public class UserLoginStatus {

    private String userName;
    private String nickName;
    private String avatar;
    private boolean isLogin;

    public UserLoginStatus(){

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}
