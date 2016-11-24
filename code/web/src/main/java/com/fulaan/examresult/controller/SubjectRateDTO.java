package com.fulaan.examresult.controller;

/**
 * Created by fl on 2015/9/8.
 */
public class SubjectRateDTO {

    private String subjectName; //科目名称
    private long avecer; //平时优秀率
    private long avecpr;  //平时及格率
    private long midcer;  //期中优秀率
    private long midcpr;  //期中及格率
    private long endcer;  //期末优秀率
    private long endcpr;  //期末及格率

    public long getEndcpr() {
        return endcpr;
    }

    public void setEndcpr(long endcpr) {
        this.endcpr = endcpr;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public long getAvecer() {
        return avecer;
    }

    public void setAvecer(long avecer) {
        this.avecer = avecer;
    }

    public long getAvecpr() {
        return avecpr;
    }

    public void setAvecpr(long avecpr) {
        this.avecpr = avecpr;
    }

    public long getMidcer() {
        return midcer;
    }

    public void setMidcer(long midcer) {
        this.midcer = midcer;
    }

    public long getMidcpr() {
        return midcpr;
    }

    public void setMidcpr(long midcpr) {
        this.midcpr = midcpr;
    }

    public long getEndcer() {
        return endcer;
    }

    public void setEndcer(long endcer) {
        this.endcer = endcer;
    }


}
