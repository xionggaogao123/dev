package com.fulaan.dorm.dto;

import com.pojo.dormitory.RoomUserEntry;

/**
 * Created by wang_xinxin on 2016/9/28.
 */
public class RoomUserDTO {

    private int bedNum;

    private String userName;

    private String userId;

    public RoomUserDTO() {

    }

    public RoomUserDTO(RoomUserEntry roomUserEntry) {
        this.bedNum = roomUserEntry.getBedNum();
        this.userId = roomUserEntry.getUserId()==null?"":roomUserEntry.getUserId().toString();
        this.userName = roomUserEntry.getUserName();
    }

    public int getBedNum() {
        return bedNum;
    }

    public void setBedNum(int bedNum) {
        this.bedNum = bedNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
