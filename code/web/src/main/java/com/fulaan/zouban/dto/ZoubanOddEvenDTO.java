package com.fulaan.zouban.dto;

/**
 * Created by admin on 2016/9/28.
 */
public class ZoubanOddEvenDTO {
    private String zoubanOddEvenId;
    private String subjectName;
    private String teacherName;
    private String className;
    private String subjectId;
    private String teacherId;
    private String group;

    public String getZoubanOddEvenId() {
        return zoubanOddEvenId;
    }

    public void setZoubanOddEvenId(String zoubanOddEvenId) {
        this.zoubanOddEvenId = zoubanOddEvenId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
