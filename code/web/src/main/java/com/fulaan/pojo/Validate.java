package com.fulaan.pojo;

/**
 * Created by jerry on 2017/1/12.
 */
public class Validate {

    private boolean isOk = false;
    private String message;

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}