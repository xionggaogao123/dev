package com.fulaan.teachermanage.dto;

import com.pojo.teachermanage.EducationEntry;

/**
 * Created by wang_xinxin on 2016/3/8.
 */
public class EducationDTO {
    private String education;
    private String degree;
    private String major;
    private String time;
    private String open;

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public EducationDTO() {

    }

    public EducationDTO(EducationEntry entry) {
        this.education = entry.getEducation();
        this.degree = entry.getDegree();
        this.major = entry.getMajor();
        this.time = entry.getTime();
        this.open = String.valueOf(entry.getOpen());

    }
}
