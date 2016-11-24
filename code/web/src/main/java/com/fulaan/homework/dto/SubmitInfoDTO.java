package com.fulaan.homework.dto;

/**
 * Created by fl on 2015/8/26.
 */
public class SubmitInfoDTO {
    String stuName;
    String className;
    int subNum;

    public SubmitInfoDTO() {

    }
    public SubmitInfoDTO(String stuName, String className, int subNum) {
        this.stuName = stuName;
        this.className = className;
        this.subNum = subNum;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getSubNum() {
        return subNum;
    }

    public void setSubNum(int subNum) {
        this.subNum = subNum;
    }

}
