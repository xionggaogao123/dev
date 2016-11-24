package com.fulaan.leave.dto;

/**
 * Created by qiangm on 2016/3/4.
 */
public class TeacherInfo {
    private String id;
    private String name;

    public TeacherInfo(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
