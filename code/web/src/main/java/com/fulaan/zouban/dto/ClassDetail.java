package com.fulaan.zouban.dto;

/**
 * 行政班课表明细
 * Created by qiangm on 2015/9/23.
 */
public class ClassDetail {
    private String className;//教学班
    private int people;//教学班人数
    private String teacherName;//任课教师
    private String classRoom;//上课教室
    private int myClassAmount;//本班学生人数

    public ClassDetail() {}

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public int getMyClassAmount() {
        return myClassAmount;
    }

    public void setMyClassAmount(int myClassAmount) {
        this.myClassAmount = myClassAmount;
    }
}
