package com.fulaan.count.dto;

import java.util.ArrayList;
import java.util.List;
//小课堂dto
public class XktDto {

    private List<String> subjectList = new ArrayList<String>();
    
    private List<Integer> numList = new ArrayList<Integer>();
    
    private String className;
    
    private Integer classTimes;
    
    private Integer avgClassTime;
    
    private Integer classPerson;
    
    private String userName;
    
    private String subject;

    public List<String> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<String> subjectList) {
        this.subjectList = subjectList;
    }

    public List<Integer> getNumList() {
        return numList;
    }

    public void setNumList(List<Integer> numList) {
        this.numList = numList;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getClassTimes() {
        return classTimes;
    }

    public void setClassTimes(Integer classTimes) {
        this.classTimes = classTimes;
    }

    public Integer getAvgClassTime() {
        return avgClassTime;
    }

    public void setAvgClassTime(Integer avgClassTime) {
        this.avgClassTime = avgClassTime;
    }

    public Integer getClassPerson() {
        return classPerson;
    }

    public void setClassPerson(Integer classPerson) {
        this.classPerson = classPerson;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    
}
