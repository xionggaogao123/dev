package com.fulaan.quality.dto;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang_xinxin on 2016/11/11.
 */
public enum TeacherProject {
    PROJECT_1(1,"教学计划"),
    PROJECT_2(2,"备课"),
    PROJECT_3(3,"作业批改"),
    PROJECT_4(4,"学生辅导"),
    PROJECT_5(5,"质量检测"),
    PROJECT_6(6,"听课评课"),
    PROJECT_7(7,"集体备课");

    private TeacherProject(int type, String des) {
        this.type = type;
        this.des = des;
    }

    private int type;
    private String des;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public static List<String> getTeacherProject() {
        List<String> map = new ArrayList<String>();
        map.add(TeacherProject.PROJECT_1.getDes());
        map.add(TeacherProject.PROJECT_2.getDes());
        map.add(TeacherProject.PROJECT_3.getDes());
        map.add(TeacherProject.PROJECT_4.getDes());
        map.add(TeacherProject.PROJECT_5.getDes());
        map.add(TeacherProject.PROJECT_6.getDes());
        map.add(TeacherProject.PROJECT_7.getDes());
        return map;
    }
}
