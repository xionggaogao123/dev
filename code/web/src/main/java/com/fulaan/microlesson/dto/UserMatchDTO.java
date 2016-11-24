package com.fulaan.microlesson.dto;

import com.pojo.user.UserEntry;

/**
 * Created by wang_xinxin on 2015/8/24.
 */
public class UserMatchDTO {

    private String userid;

    private String userName;

    public String getUserid() {
        return userid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UserMatchDTO(UserEntry userEntry){
        this.userid = userEntry.getID().toString();
        this.userName = userEntry.getUserName();
    }
}
