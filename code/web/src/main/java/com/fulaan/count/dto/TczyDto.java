package com.fulaan.count.dto;
//学科作业dto
public class TczyDto {
    //班级名称
    private String className;
    //姓名
    private String userName;
    //学科
    private String subjectName;
    //发布次数
    private int faNum;
    //提交次数
    private int tjNum;
    //提交人数
    private int tjPeoNum;
    //最后一次发布时间
    private String fbDate;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getFaNum() {
        return faNum;
    }

    public void setFaNum(int faNum) {
        this.faNum = faNum;
    }

    public int getTjNum() {
        return tjNum;
    }

    public void setTjNum(int tjNum) {
        this.tjNum = tjNum;
    }

    public int getTjPeoNum() {
        return tjPeoNum;
    }

    public void setTjPeoNum(int tjPeoNum) {
        this.tjPeoNum = tjPeoNum;
    }

    public String getFbDate() {
        return fbDate;
    }

    public void setFbDate(String fbDate) {
        this.fbDate = fbDate;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
    
    
}
