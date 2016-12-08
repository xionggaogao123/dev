package com.fulaan.playmate.dto;

import com.fulaan.playmate.pojo.MateData;
import com.fulaan.pojo.User;
import com.pojo.user.UserTag;

import java.util.List;

/**
 * Created by moslpc on 2016/11/30.
 */
public class FMateDTO {
    private String userId;
    private String userName;
    private String nickName;
    private String avatar;
    private String distance;
    private int aged;
    private MateData ons;

    private List<UserTag> tags;
    private List<User> commonFriends;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public List<UserTag> getTags() {
        return tags;
    }

    public void setTags(List<UserTag> tags) {
        this.tags = tags;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public List<User> getCommonFriends() {
        return commonFriends;
    }

    public void setCommonFriends(List<User> commonFriends) {
        this.commonFriends = commonFriends;
    }

    public int getAged() {
        return aged;
    }

    public void setAged(int aged) {
        this.aged = aged;
    }

    public MateData getOns() {
        return ons;
    }

    public void setOns(MateData ons) {
        this.ons = ons;
    }
}
