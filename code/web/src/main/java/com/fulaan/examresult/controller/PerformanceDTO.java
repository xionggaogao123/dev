package com.fulaan.examresult.controller;

/**
 * Created by fl on 2015/6/25.
 */
public class PerformanceDTO {
    private String performanceId;
    private String studentName;
    private String subjectId;
    private Double subjectScore;
    private Integer absence;
    private Integer exemption;

    public Integer getAbsence() {
        return absence;
    }

    public void setAbsence(Integer absence) {
        this.absence = absence;
    }

    public Integer getExemption() {
        return exemption;
    }

    public void setExemption(Integer exemption) {
        this.exemption = exemption;
    }

    public String getPerformanceId() {
        return performanceId;

    }

    public void setPerformanceId(String performanceId) {
        this.performanceId = performanceId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public Double getSubjectScore() {
        return subjectScore;
    }

    public void setSubjectScore(Double subjectScore) {
        this.subjectScore = subjectScore;
    }
}
