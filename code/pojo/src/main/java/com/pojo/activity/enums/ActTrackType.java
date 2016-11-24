package com.pojo.activity.enums;

/**
 * Created by Hao on 2014/10/24.
 */
public enum ActTrackType {
    PROMOTE(0,"发表了活动"), //发表了活动
    FRIEND(1,"成为了朋友"),  //成为了朋友
    ATTEND(2,"参加了活动"),  //参加了活动
    REPLY(3,"回复了活动");   //回复了活动

    private int state;
    private String description;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }


    ActTrackType(int i, String description) {
        this.state=i;
        this.description=description;
    }
}