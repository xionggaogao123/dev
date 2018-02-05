package com.fulaan.controlphone.dto;

/**
 * Created by James on 2018/2/5.
 */
public class PhoneTimeDTO {
    private String currentTime;//时间段（日期或星期）
    private String classTime;//管控时间段
    private String start;//
    private String end;//
    private int type;// 1 常规   2  特殊    3 时间段


    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getClassTime() {
        return classTime;
    }

    public void setClassTime(String classTime) {
        this.classTime = classTime;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
