package com.fulaan.overtime.dto;

import com.pojo.lesson.LessonWare;
import com.pojo.overtime.OverTimeEntry;
import com.sys.utils.DateTimeUtils;

import java.util.List;

/**
 * Created by wang_xinxin on 2016/7/14.
 */
public class OverTimeDTO {

    private String id;

    private String sqUserName;

    private String jbUserName;

    private String date;

    private String timeDuan;

    private String cause;

    private String shUserName;

    private String content;

    private String filePath;

    private String realName;

    private String sqUserId;

    private String jbUserId;

    private String shUserId;

    private int type;

    private String startTime;

    private String endTime;

    private double salary;

    private String times;

    private long inTime;

    private long outTime;

    private List<LessonWare> lessonWareList;

    public OverTimeDTO() {

    }

    public OverTimeDTO(OverTimeEntry overTimeEntry) {
        this.id = overTimeEntry.getID().toString();
        this.date = DateTimeUtils.convert(overTimeEntry.getDate(),DateTimeUtils.DATE_YYYY_MM_DD_N);
        this.content = overTimeEntry.getContent();
        this.salary = overTimeEntry.getSalary();
        this.filePath = overTimeEntry.getfilePath();
        this.realName = overTimeEntry.getRealName();
        this.sqUserId = overTimeEntry.getApplicantUserId().toString();
        this.jbUserId = overTimeEntry.getJbUserId().toString();
        this.shUserId = overTimeEntry.getAuditUserId().toString();
        this.type = overTimeEntry.getType();
        this.cause = overTimeEntry.getCause();
        this.timeDuan = overTimeEntry.getStartTime() + "-" + overTimeEntry.getEndTime();
        this.startTime = overTimeEntry.getStartTime();
        this.endTime = overTimeEntry.getEndTime();
        this.inTime = overTimeEntry.getInTime();
        this.outTime = overTimeEntry.getOutTime();
        this.lessonWareList = overTimeEntry.getLessonWareList();

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSqUserName() {
        return sqUserName;
    }

    public void setSqUserName(String sqUserName) {
        this.sqUserName = sqUserName;
    }

    public String getJbUserName() {
        return jbUserName;
    }

    public void setJbUserName(String jbUserName) {
        this.jbUserName = jbUserName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeDuan() {
        return timeDuan;
    }

    public void setTimeDuan(String timeDuan) {
        this.timeDuan = timeDuan;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getShUserName() {
        return shUserName;
    }

    public void setShUserName(String shUserName) {
        this.shUserName = shUserName;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getSqUserId() {
        return sqUserId;
    }

    public void setSqUserId(String sqUserId) {
        this.sqUserId = sqUserId;
    }

    public String getJbUserId() {
        return jbUserId;
    }

    public void setJbUserId(String jbUserId) {
        this.jbUserId = jbUserId;
    }

    public String getShUserId() {
        return shUserId;
    }

    public void setShUserId(String shUserId) {
        this.shUserId = shUserId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public long getInTime() {
        return inTime;
    }

    public void setInTime(long inTime) {
        this.inTime = inTime;
    }

    public long getOutTime() {
        return outTime;
    }

    public void setOutTime(long outTime) {
        this.outTime = outTime;
    }

    public List<LessonWare> getLessonWareList() {
        return lessonWareList;
    }

    public void setLessonWareList(List<LessonWare> lessonWareList) {
        this.lessonWareList = lessonWareList;
    }
}
