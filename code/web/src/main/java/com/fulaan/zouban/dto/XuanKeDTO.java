package com.fulaan.zouban.dto;

import com.pojo.zouban.XuankeConfEntry;
import com.sys.utils.DateTimeUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by wang_xinxin on 2015/9/23.
 */
public class XuanKeDTO {

    private String xuankeId;
    private String term; //学期
    private String gradeId; //年级id
    private List<SubjectConfDTO> subConfList;
    private String startDate; //选课开始时间
    private String endDate; //选课结束日期
    private String opentime;
    private String endtime;
    private int isRelease; //选课开始
    private int end;//选课结束
    private String xuankecount;
    private String info;


    public XuanKeDTO() {}

    public XuanKeDTO(XuankeConfEntry xuankeConfEntry) {
        this.xuankeId = xuankeConfEntry.getID().toString();
        long startTime = xuankeConfEntry.getStartDate() == 0 ? new Date().getTime() : xuankeConfEntry.getStartDate();
        this.startDate = DateTimeUtils.convert(startTime, DateTimeUtils.DATE_YYYY_MM_DD);
        this.opentime = DateTimeUtils.convert(startTime, DateTimeUtils.DATE_YYYY_MM_DD_HH_MM);
        long endTime = xuankeConfEntry.getEndDate() == 0 ? new Date().getTime() : xuankeConfEntry.getEndDate();
        this.endDate = DateTimeUtils.convert(endTime, DateTimeUtils.DATE_YYYY_MM_DD);
        this.endtime = DateTimeUtils.convert(endTime, DateTimeUtils.DATE_YYYY_MM_DD_HH_MM);

        this.isRelease = xuankeConfEntry.getIsRelease();
        this.end = xuankeConfEntry.getEnd();
        this.info = xuankeConfEntry.getInfo();
    }

    public XuanKeDTO(String term,String gradeId,String startDate,String endDate) {
        this.term = term;
        this.gradeId = gradeId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public List<SubjectConfDTO> getSubConfList() {
        return subConfList;
    }

    public void setSubConfList(List<SubjectConfDTO> subConfList) {
        this.subConfList = subConfList;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getXuankeId() {
        return xuankeId;
    }

    public void setXuankeId(String xuankeId) {
        this.xuankeId = xuankeId;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public int getIsRelease() {
        return isRelease;
    }

    public void setIsRelease(int isRelease) {
        this.isRelease = isRelease;
    }

    public String getXuankecount() {
        return xuankecount;
    }

    public void setXuankecount(String xuankecount) {
        this.xuankecount = xuankecount;
    }

    public String getOpentime() {
        return opentime;
    }

    public void setOpentime(String opentime) {
        this.opentime = opentime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
