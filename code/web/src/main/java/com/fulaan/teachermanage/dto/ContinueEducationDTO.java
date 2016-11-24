package com.fulaan.teachermanage.dto;

import com.pojo.teachermanage.ContinueEducationEntry;

/**
 * Created by wang_xinxin on 2016/3/7.
 */
public class ContinueEducationDTO {
    private String userid;

    private String username;

    private String course;

    private int score;

    private String certificate;

    private String open;
    private String time;
    private String institutions;
    private String address;
    private String classHour;
    private int record;
    private String content;
    private String courseName;
    public ContinueEducationDTO() {

    }

    public ContinueEducationDTO(ContinueEducationEntry entry) {
        this.course = entry.getCourse();
        this.certificate = entry.getCertificate();
        this.time = entry.getTime();
        this.institutions = entry.getInstitutions();
        this.address = entry.getAddress();
        this.classHour = entry.getClassHour();
        this.record = entry.getRecord();
        this.content = entry.getContent();
        this.open = String.valueOf(entry.getOpen());
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getInstitutions() {
        return institutions;
    }

    public void setInstitutions(String institutions) {
        this.institutions = institutions;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getClassHour() {
        return classHour;
    }

    public void setClassHour(String classHour) {
        this.classHour = classHour;
    }

    public int getRecord() {
        return record;
    }

    public void setRecord(int record) {
        this.record = record;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
