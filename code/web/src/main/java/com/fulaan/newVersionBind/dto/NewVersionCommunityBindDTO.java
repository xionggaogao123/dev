package com.fulaan.newVersionBind.dto;

import com.pojo.fcommunity.NewVersionCommunityBindEntry;

/**
 * Created by scott on 2018/1/15.
 */
public class NewVersionCommunityBindDTO {

    private String bindId;
    private String communityId;
    private String mainUserId;
    private String userId;
    private String thirdName;
    private String number;
    private String nickName;
    private String avatar;
    private String userName;
    private int shiftOut;

    public NewVersionCommunityBindDTO(){

    }

    public NewVersionCommunityBindDTO(NewVersionCommunityBindEntry entry){
        this.bindId=entry.getID().toString();
        this.communityId=entry.getCommunityId().toString();
        this.mainUserId=entry.getMainUserId().toString();
        this.userId=entry.getUserId().toString();
        this.thirdName=entry.getThirdName();
        this.number=entry.getNumber();
    }

    public int getShiftOut() {
        return shiftOut;
    }

    public void setShiftOut(int shiftOut) {
        this.shiftOut = shiftOut;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBindId() {
        return bindId;
    }

    public void setBindId(String bindId) {
        this.bindId = bindId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getMainUserId() {
        return mainUserId;
    }

    public void setMainUserId(String mainUserId) {
        this.mainUserId = mainUserId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getThirdName() {
        return thirdName;
    }

    public void setThirdName(String thirdName) {
        this.thirdName = thirdName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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
}
