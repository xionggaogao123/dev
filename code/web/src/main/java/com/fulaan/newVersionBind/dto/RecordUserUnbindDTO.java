package com.fulaan.newVersionBind.dto;

import com.pojo.user.RecordUserUnbindEntry;

/**
 * Created by scott on 2018/1/12.
 */
public class RecordUserUnbindDTO {

    private String id;
    private String  mainUserId;
    private String  userId;
    private String  userKey;
    private String nickName;
    private String avatar;

    public RecordUserUnbindDTO(){

    }

    public RecordUserUnbindDTO(RecordUserUnbindEntry entry){
        this.id=entry.getID().toString();
        this.mainUserId=entry.getMainUserId().toString();
        this.userId=entry.getUserId().toString();
        this.userKey=entry.getUserKey();
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }
}
