package com.fulaan.playmate.pojo;

/**
 * Created by moslpc on 2016/12/7.
 */
public class MateData {

    private int code;
    private String data;

    public MateData(int code,String data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
