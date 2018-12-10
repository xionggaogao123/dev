package com.fulaan.count.dto;
//通知dto
public class TzDto {

    private String name;
    
    private String className;
    
    private String subjectName;
    
    private Integer fbNum;
    
    private Integer peoNum;
    
    private String fbDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Integer getFbNum() {
        return fbNum;
    }

    public void setFbNum(Integer fbNum) {
        this.fbNum = fbNum;
    }

    public Integer getPeoNum() {
        return peoNum;
    }

    public void setPeoNum(Integer peoNum) {
        this.peoNum = peoNum;
    }

    public String getFbDate() {
        return fbDate;
    }

    public void setFbDate(String fbDate) {
        this.fbDate = fbDate;
    }
    
    
}
