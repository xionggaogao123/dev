package com.fulaan.zouban.dto;


import com.pojo.zouban.ZoubanNoticeEntry;

import java.io.Serializable;

/**
 * Created by qiangm on 2015/11/13.
 *
 */
public class NoticeDetailDTO implements Serializable {
    private String teacherName;
    private String className;
    private String courseName;
    private String oldTime;
    private String newTime;

    public NoticeDetailDTO() {}

    public NoticeDetailDTO(ZoubanNoticeEntry.NoticeDetail noticeDetail) {
        this.teacherName = noticeDetail.getTeacherName();
        this.className = noticeDetail.getClassName();
        this.courseName = noticeDetail.getCourseName();
        this.oldTime = noticeDetail.getOldTime();
        this.newTime = noticeDetail.getNewTime();
    }


    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getOldTime() {
        return oldTime;
    }

    public void setOldTime(String oldTime) {
        this.oldTime = oldTime;
    }

    public String getNewTime() {
        return newTime;
    }

    public void setNewTime(String newTime) {
        this.newTime = newTime;
    }


}
