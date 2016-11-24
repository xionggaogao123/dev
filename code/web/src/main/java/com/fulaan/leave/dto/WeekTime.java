package com.fulaan.leave.dto;

/**
 * Created by qiangm on 2016/3/3.
 */
public class WeekTime {
    int week;
    int x;
    int y;
    String courseId;
    String courseName;

    public WeekTime(int week, int x, int y, String courseId, String courseName) {
        this.week = week;
        this.x = x;
        this.y = y;
        this.courseId = courseId;
        this.courseName = courseName;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
