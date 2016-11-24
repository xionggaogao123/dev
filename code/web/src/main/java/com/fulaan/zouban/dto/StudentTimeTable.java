package com.fulaan.zouban.dto;

/**
 * 学生及任课老师的课表
 * Created by qiangm on 2015/9/21.
 */
public class StudentTimeTable {
    private int xIndex;//星期几
    private int yIndex;//第几天
    private String courseId;//课程id
    private String className;//课程名
    private String classRoom;//教室
    private String teacherName;//老师姓名
    private int type;//课程类型
    private String teacherId;
    private String courseItemId;//课程信息Id

    public StudentTimeTable() {
    }
    public StudentTimeTable(int x,int y)
    {
        this.setxIndex(x);
        this.setyIndex(y);
        this.setCourseId("");
        this.setClassName("");
        this.setClassRoom("");
        this.setTeacherName("");
        this.setTeacherId("");
        this.setType(2);
    }
    public int getxIndex() {
        return xIndex;
    }

    public void setxIndex(int xIndex) {
        this.xIndex = xIndex;
    }

    public int getyIndex() {
        return yIndex;
    }

    public void setyIndex(int yIndex) {
        this.yIndex = yIndex;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getCourseItemId() {
        return courseItemId;
    }

    public void setCourseItemId(String courseItemId) {
        this.courseItemId = courseItemId;
    }
}
