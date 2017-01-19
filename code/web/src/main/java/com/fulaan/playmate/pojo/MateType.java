package com.fulaan.playmate.pojo;

/**
 * Created by moslpc on 2017/1/19.
 */
public enum MateType {

    TAG("标签",1),
    AGE("年龄段",2),
    DISTANCE("距离",3),
    TIME("时间段",4);

    private String type;
    private int code;

    MateType(String type,int code) {
        this.type = type;
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "MateType{" +
                "type='" + type + '\'' +
                ", code=" + code +
                '}';
    }
}
