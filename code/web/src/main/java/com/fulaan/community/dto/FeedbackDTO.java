package com.fulaan.community.dto;

import com.pojo.fcommunity.FeedbackEntry;
import com.sys.utils.DateTimeUtils;

/**
 * Created by scott on 2017/3/21.
 */
public class FeedbackDTO {
    private String id;
    private String userName;
    private String userId;
    private String content;
    private long time;
    private String showTime;
    private int userPermission;

    public FeedbackDTO(){

    }

    public FeedbackDTO(FeedbackEntry entry){
        this.id=entry.getID().toString();
        this.userId=entry.getUserId().toString();
        this.content=entry.getContent();
        this.time=entry.getTime();
        this.showTime= DateTimeUtils.convert(entry.getTime(),DateTimeUtils.DATE_YYYY_MM_DD);
    }

    public int getUserPermission() {
        return userPermission;
    }

    public void setUserPermission(int userPermission) {
        this.userPermission = userPermission;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }
}
