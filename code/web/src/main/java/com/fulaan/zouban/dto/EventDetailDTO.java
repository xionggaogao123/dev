package com.fulaan.zouban.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangkaidong on 2016/7/26.
 */
public class EventDetailDTO {
    private String name;
    private List<String> teacherList = new ArrayList<String>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTeacherList() {
        return teacherList;
    }

    public void setTeacherList(List<String> teacherList) {
        this.teacherList = teacherList;
    }
}
