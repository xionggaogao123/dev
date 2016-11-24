package com.fulaan.duty.dto;

import com.fulaan.utils.StringUtil;
import com.pojo.app.FileUploadDTO;
import com.pojo.lesson.LessonWare;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by wang_xinxin on 2016/7/10.
 */
public class DutyUserDTO {
    private String date;

    private String id;

    private String userName;

    private String projectName;

    private String timeDuan;

    private String times;

    private double salary;

    private String timeDesc;

    private String content;

    private String ip;

    private String realName;

    private String filePath;

    private String userId;

    private String checkIn;

    private String checkOut;

    private List<LessonWare> fileUploadDTOList;

    private int fileCnt;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTimeDuan() {
        return timeDuan;
    }

    public void setTimeDuan(String timeDuan) {
        this.timeDuan = timeDuan;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getTimeDesc() {
        return timeDesc;
    }

    public void setTimeDesc(String timeDesc) {
        this.timeDesc = timeDesc;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCheckIn() {
        if (!StringUtils.isEmpty(checkIn)) {
            return checkIn;
        } else {
            return "";
        }
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        if (!StringUtils.isEmpty(checkOut)) {
            return checkOut;
        } else {
            return "";
        }
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public List<LessonWare> getFileUploadDTOList() {
        return fileUploadDTOList;
    }

    public void setFileUploadDTOList(List<LessonWare> fileUploadDTOList) {
        this.fileUploadDTOList = fileUploadDTOList;
    }

    public int getFileCnt() {
        return fileCnt;
    }

    public void setFileCnt(int fileCnt) {
        this.fileCnt = fileCnt;
    }
}
