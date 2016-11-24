package com.fulaan.docflow.dto;

import com.pojo.school.SchoolDTO;
import com.pojo.school.SchoolEntry;

/**
 * Created by qiangm on 2016/1/19.
 */
public class SimpleSchoolDTO {
    private String id;
    private String name;

    public SimpleSchoolDTO() {
    }
    public SimpleSchoolDTO(SchoolEntry schoolEntry) {
        this.id=schoolEntry.getID().toString();
        this.name=schoolEntry.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
