package com.fulaan.duty.dto;

import com.pojo.duty.DutyModelEntry;

/**
 * Created by wang_xinxin on 2016/7/4.
 */
public class DutyModelDTO {

    private String id;

    private String name;

//    private int index;

    private String dutySetId;

    private String jbUserId;

    private String date;

    private String startTime;

    private String endTime;

    private String cause;

    private String shUserId;

    public DutyModelDTO() {

    }

    public DutyModelDTO(DutyModelEntry dutyModelEntry) {
        this.id = dutyModelEntry.getID().toString();
        this.name = dutyModelEntry.getModelName();
        this.dutySetId = dutyModelEntry.getDutySetId()!=null?dutyModelEntry.getDutySetId().toString():"";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
//
//    public int getIndex() {
//        return index;
//    }
//
//    public void setIndex(int index) {
//        this.index = index;
//    }

    public String getJbUserId() {
        return jbUserId;
    }

    public void setJbUserId(String jbUserId) {
        this.jbUserId = jbUserId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getShUserId() {
        return shUserId;
    }

    public void setShUserId(String shUserId) {
        this.shUserId = shUserId;
    }

    public String getDutySetId() {
        return dutySetId;
    }

    public void setDutySetId(String dutySetId) {
        this.dutySetId = dutySetId;
    }
}
