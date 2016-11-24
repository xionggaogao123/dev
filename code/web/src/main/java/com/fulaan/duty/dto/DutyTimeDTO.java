package com.fulaan.duty.dto;

import com.pojo.duty.DutyTimeEntry;

/**
 * Created by wang_xinxin on 2016/7/1.
 */
public class DutyTimeDTO {

    private String id;

    private String timeDesc;

    private String startTime;

    private String endTime;

    public DutyTimeDTO() {

    }

    public DutyTimeDTO(DutyTimeEntry dutyTimeEntry) {
        this.id = dutyTimeEntry.getID().toString();
        this.timeDesc = dutyTimeEntry.getTimeDesc();
        this.startTime = dutyTimeEntry.getStartTime();
        this.endTime = dutyTimeEntry.getEndTime();

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimeDesc() {
        return timeDesc;
    }

    public void setTimeDesc(String timeDesc) {
        this.timeDesc = timeDesc;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
