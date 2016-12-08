package com.fulaan.playmate.pojo;

import com.pojo.user.UserTag;

import java.util.List;

/**
 * Created by moslpc on 2016/12/7.
 */
public class User{
    private String userId;
    private String nickName;
    private String userName;
    private String avatar;
    private List<UserTag> tags;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<UserTag> getTags() {
        return tags;
    }

    public void setTags(List<UserTag> tags) {
        this.tags = tags;
    }
}