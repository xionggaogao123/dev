package com.fulaan.zouban.dto;

/**
 * Created by wangkaidong on 2016/7/27.
 *
 * 课表状态枚举
 */
public enum TimetableState {
    NOTPUBLISHED(1, "未发布"),
    PUBLISHED(2, "已发布"),
    ADJUSTING(3, "调课中"),
    ADJUSTED(4, "调课已发布");

    private int state;
    private String description;

    private TimetableState(int state, String description) {
        this.state = state;
        this.description = description;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
