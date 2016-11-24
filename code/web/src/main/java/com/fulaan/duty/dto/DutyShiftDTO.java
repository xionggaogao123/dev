package com.fulaan.duty.dto;

/**
 * Created by wang_xinxin on 2016/7/10.
 */
public class DutyShiftDTO {
    private String id;

    private String userName;

    private String userId;

    private String date;

    private String timeDesc;

    private String cause;

    private int type;

    private String projectName;

    private String timeDuan;

    public DutyShiftDTO() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeDesc() {
        return timeDesc;
    }

    public void setTimeDesc(String timeDesc) {
        this.timeDesc = timeDesc;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTimeDuan() {
        return timeDuan;
    }

    public void setTimeDuan(String timeDuan) {
        this.timeDuan = timeDuan;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
