package com.fulaan.examresult.controller;

import java.util.Comparator;
import java.util.List;

/**
 * Created by fl on 2015/6/25.
 */
public class ExamInfoDTO {
    private String examId;
    private String examName;
    private String schoolYear;
    private String process;
    private String date;
    private String type;
    private Integer isGrade;
    private Integer classSize;//本次考试参加的班级数

    public Integer getClassSize() {
        return classSize;
    }

    public void setClassSize(Integer classSize) {
        this.classSize = classSize;
    }

    private List<String> subjectNameList;

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ExamInfoDTO(String examName, String examId, String process, String date) {
        this.examName = examName;
        this.examId = examId;
        this.process = process;
        this.date = date;
    }

    public ExamInfoDTO(String examId, String examName, String schoolYear) {
        this.examId = examId;
        this.examName = examName;
        this.schoolYear = schoolYear;
    }

    public ExamInfoDTO() {
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
    }

    public List<String> getSubjectNameList() {
        return subjectNameList;
    }

    public void setSubjectNameList(List<String> subjectNameList) {
        this.subjectNameList = subjectNameList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getIsGrade() {
        return isGrade;
    }

    public void setIsGrade(Integer isGrade) {
        this.isGrade = isGrade;
    }
}

class SortByDate implements Comparator{
    public int compare(Object o1, Object o2) {
        ExamInfoDTO e1 = (ExamInfoDTO) o1;
        ExamInfoDTO e2 = (ExamInfoDTO) o2;
        return e2.getDate().compareTo(e1.getDate());
    }
}

class SortByProcess implements Comparator{
    public int compare(Object o1, Object o2) {
        ExamInfoDTO e1 = (ExamInfoDTO) o1;
        ExamInfoDTO e2 = (ExamInfoDTO) o2;
        return e1.getProcess().compareTo(e2.getProcess());
    }
}
