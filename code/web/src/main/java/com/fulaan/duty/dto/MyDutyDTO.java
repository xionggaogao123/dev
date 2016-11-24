package com.fulaan.duty.dto;

import java.util.List;

/**
 * Created by wang_xinxin on 2016/7/7.
 */
public class MyDutyDTO {

    private String times;

    private int isToday;

    private String timeDesc;

    private String timeDuan;

    private String dutyProject;

    private String id;

    private List<MyDutyDTO> myDutyDTOs;

    private int type;

    private int count;

    private int type2;

    private int type3;

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getTimeDesc() {
        return timeDesc;
    }

    public void setTimeDesc(String timeDesc) {
        this.timeDesc = timeDesc;
    }

    public String getTimeDuan() {
        return timeDuan;
    }

    public void setTimeDuan(String timeDuan) {
        this.timeDuan = timeDuan;
    }

    public String getDutyProject() {
        return dutyProject;
    }

    public void setDutyProject(String dutyProject) {
        this.dutyProject = dutyProject;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<MyDutyDTO> getMyDutyDTOs() {
        return myDutyDTOs;
    }

    public void setMyDutyDTOs(List<MyDutyDTO> myDutyDTOs) {
        this.myDutyDTOs = myDutyDTOs;
    }

    public int getIsToday() {
        return isToday;
    }

    public void setIsToday(int isToday) {
        this.isToday = isToday;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getType2() {
        return type2;
    }

    public void setType2(int type2) {
        this.type2 = type2;
    }

    public int getType3() {
        return type3;
    }

    public void setType3(int type3) {
        this.type3 = type3;
    }
}
