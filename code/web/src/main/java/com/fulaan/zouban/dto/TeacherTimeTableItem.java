package com.fulaan.zouban.dto;

/**
 * Created by qiangm on 2015/9/22.
 *
 * 教师课表
 */
public class TeacherTimeTableItem {
    private int xIndex;//星期几
    private int yIndex;//第几节
    private String className;//课程名
    private String classRoom;//教室

    public TeacherTimeTableItem() {
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
}
