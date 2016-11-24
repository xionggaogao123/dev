package com.pojo.activity.enums;

/**
 * Created by Hao on 14-11-5.
 */
public enum  ActTrackDevice {
    FromPC(0,"消息来源于pc"),
    FromAndroid(1,"来源于安卓手机"),
    FromIOS(2,"来源于苹果手机");


    private int state;

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

    private String description;

    ActTrackDevice(int i, String description) {
        this.state=i;
        this.description=description;
    }
}
