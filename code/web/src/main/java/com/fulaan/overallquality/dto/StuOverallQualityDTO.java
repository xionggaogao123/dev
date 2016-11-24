package com.fulaan.overallquality.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojing on 2016/8/23.
 */
public class StuOverallQualityDTO {
    private int currencyType;
    private String currencyName;
    private String schoolId;
    private String gradeId;
    private String classId;
    private String userId;
    private List<ClassOverallQualityInfo> coqiList=new ArrayList<ClassOverallQualityInfo>();

    public int getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(int currencyType) {
        this.currencyType = currencyType;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public List<ClassOverallQualityInfo> getCoqiList() {
        return coqiList;
    }

    public void setCoqiList(List<ClassOverallQualityInfo> coqiList) {
        this.coqiList = coqiList;
    }
}
