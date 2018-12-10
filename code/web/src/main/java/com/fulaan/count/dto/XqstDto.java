package com.fulaan.count.dto;

import java.util.ArrayList;
import java.util.List;

//兴趣社团图表dto
public class XqstDto {
    
    private String dateStr;
    
    private String name;
    
    private String userName;
    
    private Integer yueNum;
    
    private Integer pllNum;
    
    private Integer zannNum;
    
    private Integer selfPlNum;

    private List<String> dateList = new ArrayList<String>();
    //发帖数
    private List<Integer> num = new ArrayList<Integer>();
    //点赞数
    private List<Integer> zanNum = new ArrayList<Integer>();
    //评论数
    private List<Integer> plNum = new ArrayList<Integer>();
    //所有发帖数
    private Integer allNum;
    //所有点赞数
    private Integer allZanNum;
    //所有评论数
    private Integer allPlNum;
    public List<String> getDateList() {
        return dateList;
    }

    public void setDateList(List<String> dateList) {
        this.dateList = dateList;
    }

    public List<Integer> getNum() {
        return num;
    }

    public void setNum(List<Integer> num) {
        this.num = num;
    }

    public List<Integer> getZanNum() {
        return zanNum;
    }

    public void setZanNum(List<Integer> zanNum) {
        this.zanNum = zanNum;
    }

    public List<Integer> getPlNum() {
        return plNum;
    }

    public void setPlNum(List<Integer> plNum) {
        this.plNum = plNum;
    }

    public Integer getAllZanNum() {
        return allZanNum;
    }

    public void setAllZanNum(Integer allZanNum) {
        this.allZanNum = allZanNum;
    }

    public Integer getAllPlNum() {
        return allPlNum;
    }

    public void setAllPlNum(Integer allPlNum) {
        this.allPlNum = allPlNum;
    }

    public Integer getAllNum() {
        return allNum;
    }

    public void setAllNum(Integer allNum) {
        this.allNum = allNum;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getYueNum() {
        return yueNum;
    }

    public void setYueNum(Integer yueNum) {
        this.yueNum = yueNum;
    }

    public Integer getPllNum() {
        return pllNum;
    }

    public void setPllNum(Integer pllNum) {
        this.pllNum = pllNum;
    }

    public Integer getZannNum() {
        return zannNum;
    }

    public void setZannNum(Integer zannNum) {
        this.zannNum = zannNum;
    }

    public Integer getSelfPlNum() {
        return selfPlNum;
    }

    public void setSelfPlNum(Integer selfPlNum) {
        this.selfPlNum = selfPlNum;
    } 
    
    
}
