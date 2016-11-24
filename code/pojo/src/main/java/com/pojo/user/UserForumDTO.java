package com.pojo.user;

/**
 * Created by admin on 2016/7/19.
 */
public class UserForumDTO {

    private String nickName;
    private long experience;
    private int postCount;
    private String level;
    private int silencedStatus;
    private int silencedTen;
    private String silencedReason;
    private String userId;
    private String together;
    private String ban;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getSilencedStatus() {
        return silencedStatus;
    }

    public void setSilencedStatus(int silencedStatus) {
        this.silencedStatus = silencedStatus;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getPostCount() {
        return postCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }


    public int getSilencedTen() {
        return silencedTen;
    }

    public void setSilencedTen(int silencedTen) {
        this.silencedTen = silencedTen;
    }

    public String getSilencedReason() {
        return silencedReason;
    }

    public void setSilencedReason(String silencedReason) {
        this.silencedReason = silencedReason;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getExperience() {
        return experience;
    }

    public void setExperience(long experience) {
        this.experience = experience;
    }

    public String getTogether() {
        return together;
    }

    public void setTogether(String together) {
        this.together = together;
    }

    public String getBan() {
        return ban;
    }

    public void setBan(String ban) {
        this.ban = ban;
    }
}
