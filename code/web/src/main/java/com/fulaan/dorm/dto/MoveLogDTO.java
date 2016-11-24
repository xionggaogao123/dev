package com.fulaan.dorm.dto;

import com.pojo.dormitory.MoveLogEntry;
import com.sys.utils.DateTimeUtils;

/**
 * Created by wang_xinxin on 2016/9/28.
 */
public class MoveLogDTO {
    private String userName;

    private String time;

    private String cause;

    private String roomName;

    private String loopName;

    private String dormName;

    public MoveLogDTO() {

    }

    public MoveLogDTO(MoveLogEntry moveLogEntry) {
        this.cause = moveLogEntry.getCause();
        this.userName = moveLogEntry.getUserName();
        this.time = DateTimeUtils.getLongToStrTimeTwo(moveLogEntry.getTime());
        this.loopName = moveLogEntry.getLoopName();
        this.dormName = moveLogEntry.getDormName();
        this.roomName = moveLogEntry.getRoomName();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getLoopName() {
        return loopName;
    }

    public void setLoopName(String loopName) {
        this.loopName = loopName;
    }

    public String getDormName() {
        return dormName;
    }

    public void setDormName(String dormName) {
        this.dormName = dormName;
    }
}
