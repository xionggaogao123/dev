package com.fulaan.pojo;

import com.pojo.app.Platform;
import com.pojo.fcommunity.FLoginLogEntry;

/**
 * Created by jerry on 2016/9/21.
 * 登录日志
 */
public class FLoginLog {

    private String userName;
    private String nickName;
    private long loginTime;
    private String ip;
    private Platform pf;

    public FLoginLog(String userName, String nickName, long loginTime, String ip, com.pojo.app.Platform pf) {
        this.userName = userName;
        this.nickName = nickName;
        this.loginTime = loginTime;
        this.ip = ip;
        this.pf = pf;
    }

    public FLoginLog(FLoginLogEntry loginLogEntry){
        this.userName = loginLogEntry.getUserName();
        this.nickName = loginLogEntry.getUserNick();
        this.loginTime = loginLogEntry.getLoginTime();
        this.ip = loginLogEntry.getLoginIp();
        this.pf = Platform.platform(loginLogEntry.getLoginPf());
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public com.pojo.app.Platform getPf() {
        return pf;
    }

    public void setPf(com.pojo.app.Platform pf) {
        this.pf = pf;
    }
}
