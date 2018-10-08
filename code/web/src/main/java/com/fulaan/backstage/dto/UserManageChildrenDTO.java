package com.fulaan.backstage.dto;

/**
 * @Auther: taotao.chan
 * @Date: 2018/9/6 13:46
 * @Description: 存储孩子信息
 */
public class UserManageChildrenDTO {
    private String userId;
    private String userName;
    private String nickName;
    private String mobilePhone;
    private String status;
    private String jiaId;

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

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJiaId() {
        return jiaId;
    }

    public void setJiaId(String jiaId) {
        this.jiaId = jiaId;
    }
}
