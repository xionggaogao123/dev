package com.fulaan.overtime.dto;

import java.util.List;

/**
 * Created by wang_xinxin on 2016/7/16.
 */
public class MyOverTimeDTO {

    private String times;

    private int isToday;

    private int count;

    private List<OverTimeDTO> overTimeDTOs;

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public int getIsToday() {
        return isToday;
    }

    public void setIsToday(int isToday) {
        this.isToday = isToday;
    }

    public List<OverTimeDTO> getOverTimeDTOs() {
        return overTimeDTOs;
    }

    public void setOverTimeDTOs(List<OverTimeDTO> overTimeDTOs) {
        this.overTimeDTOs = overTimeDTOs;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
