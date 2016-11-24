package com.fulaan.pojo;

import java.util.List;

/**
 * Created by jerry on 2016/10/18.
 */
public class LikeInfo {
    private String replyId = "";
    private String userId = "";
    private String userName = "";
    private String userNickName = "";
    private String userEmail = "";
    private String userPhone = "";
    private int floor = 0;
    private String replyContent = "";
    private int likeCount = 0;
    private List<UserInfo> userInfoList = null;

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

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

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public List<UserInfo> getUserInfoList() {
        return userInfoList;
    }

    public void setUserInfoList(List<UserInfo> userInfoList) {
        this.userInfoList = userInfoList;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public static class UserInfo {
        private String id;
        private String name;
        private String nick;
        private String ip;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        @Override
        public String toString() {
            return "[" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", nick='" + nick + '\'' +
                    ", ip='" + ip + '\'' +
                    ']';
        }
    }

    @Override
    public String toString() {
        return "LikeInfo{" +
                "replyId='" + replyId + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userNickName='" + userNickName + '\'' +
                ", floor=" + floor +
                ", replyContent='" + replyContent + '\'' +
                ", likeCount=" + likeCount +
                ", userInfoList=" + userInfoList +
                '}';
    }
}
