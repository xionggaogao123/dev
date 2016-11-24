package com.fulaan.meeting.dto;

/**
 * Created by wang_xinxin on 2016/8/19.
 */
public class MessageDTO {

    private String userName;

    private String content;

    private String time;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
