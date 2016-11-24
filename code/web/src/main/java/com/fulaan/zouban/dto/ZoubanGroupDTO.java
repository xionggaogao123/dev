package com.fulaan.zouban.dto;

/**
 * Created by admin on 2016/9/28.
 */
public class ZoubanGroupDTO {
    private String zoubanCourseId;
    private String className;
    private String teacherName;
    private String teacherId;
    private String classRoomId;
    private String teId;
    private String group;
    private String term;
    private String gradeId;
    private String crId;

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getClassRoomId() {
        return classRoomId;
    }

    public void setClassRoomId(String classRoomId) {
        this.classRoomId = classRoomId;
    }

    public String getZoubanCourseId() {
        return zoubanCourseId;
    }

    public void setZoubanCourseId(String zoubanCourseId) {
        this.zoubanCourseId = zoubanCourseId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTeId() {
        return teId;
    }

    public void setTeId(String teId) {
        this.teId = teId;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getCrId() {
        return crId;
    }

    public void setCrId(String crId) {
        this.crId = crId;
    }
}
