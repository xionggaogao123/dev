package com.fulaan.zouban.dto;

/**
 * Created by qiangm on 2015/10/27.
 */
public class StudentClass {
    private String student;
    private int sex;
    private String className;

    public StudentClass() {
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
