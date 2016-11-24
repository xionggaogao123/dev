package com.fulaan.overallquality.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojing on 2016/8/23.
 */
public class ClassOverallQualityDTO {
    private String itemId;
    private String itemName;
    private String gradeId;
    private String classId;
    private List<ClassOverallQualityInfo> coqiList=new ArrayList<ClassOverallQualityInfo>();

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public List<ClassOverallQualityInfo> getCoqiList() {
        return coqiList;
    }

    public void setCoqiList(List<ClassOverallQualityInfo> coqiList) {
        this.coqiList = coqiList;
    }
}
