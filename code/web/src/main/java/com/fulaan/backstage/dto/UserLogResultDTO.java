package com.fulaan.backstage.dto;

import com.pojo.backstage.UserLogResultEntry;

/**
 * Created by scott on 2017/12/4.
 */
public class UserLogResultDTO {

    private String id;
    private String userId;
    private String userName;
    private int userRole;

    public UserLogResultDTO(){

    }

    public UserLogResultDTO(UserLogResultEntry entry){
        this.id=entry.getID().toString();
        this.userId=entry.getUserId().toString();
        this.userRole=entry.getRole();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getUserRole() {
        return userRole;
    }

    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }
}
