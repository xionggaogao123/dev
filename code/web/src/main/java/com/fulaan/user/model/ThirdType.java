package com.fulaan.user.model;

/**
 * Created by jerry on 2016/12/19.
 * 第三方类型
 */
public enum ThirdType {

    WECHAT(1, "wechat"),
    QQ(2, "qq");

    ThirdType(int code, String type) {
        this.code = code;
        this.type = type;
    }

    private int code;
    private String type;

    public static ThirdType getThirdType(int code) {
        switch (code) {
            case 1:
                return WECHAT;
            case 2:
                return QQ;
        }
        return QQ;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
