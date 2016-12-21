package com.pojo.fcommunity;

import sun.security.provider.SHA;

/**
 * Created by admin on 2016/10/26.
 * ANNOUNCEMENT:
 * ACTIVITY:
 * SHARA:
 * HOMEWORK:
 * MATERIALS:
 */
public enum CommunityDetailType {

    ANNOUNCEMENT(1, "ANNOUNCEMENT"),
    ACTIVITY(2, "ACTIVITY"),
    SHARE(3, "SHARE"),
    MEANS(4, "MEANS"),
    HOMEWORK(5, "HOMEWORK"),
    MATERIALS(6, "MATERIALS"),;

    CommunityDetailType(int type, String decs) {
        this.type = type;
        this.decs = decs;
    }

    private int type;
    private String decs;

    public static CommunityDetailType getType(int type) {
        switch (type) {
            case 1:
                return ANNOUNCEMENT;
            case 2:
                return ACTIVITY;
            case 3:
                return SHARE;
            case 4:
                return MEANS;
            case 5:
                return HOMEWORK;
            case 6:
                return MATERIALS;
        }
        return ANNOUNCEMENT;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDecs() {
        return decs;
    }

    public void setDecs(String decs) {
        this.decs = decs;
    }

}
