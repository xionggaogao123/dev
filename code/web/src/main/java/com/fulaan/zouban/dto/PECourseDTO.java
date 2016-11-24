package com.fulaan.zouban.dto;

/**
 * Created by wangkaidong on 2016/6/14.
 */
public class PECourseDTO {
    private String groupClassName;
    private String adminClassName;
    private String adminClassId;
    private String mClassName;//男生班
    private String fClassName;//女生班
    private String mClassId;
    private String fClassId;
    private String mCountStr;
    private String fCountStr;
    private String mTeacherId;
    private String fTeacherId;
    private String mTeacherName;
    private String fTeacherName;
    private int lessonCount;


    public String getGroupClassName() {
        return groupClassName;
    }

    public void setGroupClassName(String groupClassName) {
        this.groupClassName = groupClassName;
    }

    public String getAdminClassName() {
        return adminClassName;
    }

    public void setAdminClassName(String adminClassName) {
        this.adminClassName = adminClassName;
    }

    public String getAdminClassId() {
        return adminClassId;
    }

    public void setAdminClassId(String adminClassId) {
        this.adminClassId = adminClassId;
    }

    public String getmClassName() {
        return mClassName;
    }

    public void setmClassName(String mClassName) {
        this.mClassName = mClassName;
    }

    public String getfClassName() {
        return fClassName;
    }

    public void setfClassName(String fClassName) {
        this.fClassName = fClassName;
    }

    public String getmClassId() {
        return mClassId;
    }

    public void setmClassId(String mClassId) {
        this.mClassId = mClassId;
    }

    public String getfClassId() {
        return fClassId;
    }

    public void setfClassId(String fClassId) {
        this.fClassId = fClassId;
    }

    public String getmCountStr() {
        return mCountStr;
    }

    public void setmCountStr(String mCountStr) {
        this.mCountStr = mCountStr;
    }

    public String getfCountStr() {
        return fCountStr;
    }

    public void setfCountStr(String fCountStr) {
        this.fCountStr = fCountStr;
    }

    public String getmTeacherId() {
        return mTeacherId;
    }

    public void setmTeacherId(String mTeacherId) {
        this.mTeacherId = mTeacherId;
    }

    public String getfTeacherId() {
        return fTeacherId;
    }

    public void setfTeacherId(String fTeacherId) {
        this.fTeacherId = fTeacherId;
    }

    public String getmTeacherName() {
        return mTeacherName;
    }

    public void setmTeacherName(String mTeacherName) {
        this.mTeacherName = mTeacherName;
    }

    public String getfTeacherName() {
        return fTeacherName;
    }

    public void setfTeacherName(String fTeacherName) {
        this.fTeacherName = fTeacherName;
    }

    public int getLessonCount() {
        return lessonCount;
    }

    public void setLessonCount(int lessonCount) {
        this.lessonCount = lessonCount;
    }
}
