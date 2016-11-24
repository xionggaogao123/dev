package com.fulaan.teachermanage.dto;

import com.pojo.teachermanage.TitleEntry;

/**
 * Created by wang_xinxin on 2016/3/8.
 */
public class TitleDTO {
   private String name;
    private String time;
    private String jobType;
    private String level;
    private String appointmentTime;
    private String open;

    public TitleDTO(){

    }
    public TitleDTO(TitleEntry entry) {
        this.name = entry.getName();
        this.time = entry.getTime();
        this.jobType = entry.getJobType();
        this.level = entry.getLevel();
        this.appointmentTime = entry.getAppointmentTime();
        this.open = String.valueOf(entry.getOpen());
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }
}
