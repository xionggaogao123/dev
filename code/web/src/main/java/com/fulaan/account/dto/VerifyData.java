package com.fulaan.account.dto;

/**
 * Created by jerry on 2016/12/13.
 */
public class VerifyData {
    private boolean verify;
    private String msg;

    public VerifyData(boolean verify,String msg) {
        this.verify = verify;
        this.msg = msg;
    }

    public boolean isVerify() {
        return verify;
    }

    public void setVerify(boolean verify) {
        this.verify = verify;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
