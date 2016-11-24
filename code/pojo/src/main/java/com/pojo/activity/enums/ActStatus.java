package com.pojo.activity.enums;

/**
 * Created by Hao on 2014/10/23.
 */
public enum ActStatus {
    ACTIVE(0,"可用"),
    CANCEL(1,"活动取消"),//活动取消
    CLOSE(2,"活动关闭");

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

    ActStatus(int i, String description) {
        this.state=i;
        this.description=description;
    } //活动已经关闭

}
