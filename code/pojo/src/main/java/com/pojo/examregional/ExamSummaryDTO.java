package com.pojo.examregional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fl on 2015/10/30.
 */
public class ExamSummaryDTO {
    private String examSummaryId;
    private String regionalExamId;
    private String schoolId;
    private String schoolName;
    private int studentNum;
    private double compositeScores;
    private int compositeRanking;
    private List<SubjectDetailsDTO> subjectDetailsDTOList;

    public ExamSummaryDTO(){}
    public ExamSummaryDTO(ExamSummaryEntry examSummaryEntry){
        this.examSummaryId = examSummaryEntry.getID().toString();
        this.regionalExamId = examSummaryEntry.getAreaExamId().toString();
        this.schoolId = examSummaryEntry.getSchoolId().toString();
        this.schoolName = examSummaryEntry.getSchoolName();
        this.studentNum = examSummaryEntry.getStudentNumber();
        this.compositeScores = Math.round(examSummaryEntry.getCompositeScores()*100)/100.0;
        this.compositeRanking = examSummaryEntry.getCompositeRanking();
        List<SubjectDetails> list = examSummaryEntry.getSubjectDetails();
        List<SubjectDetailsDTO> dtoList = new ArrayList<SubjectDetailsDTO>();
        if(list!=null && list.size()>0) {
            for (SubjectDetails subjectDetails : list) {
                SubjectDetailsDTO subjectDetailsDTO = new SubjectDetailsDTO(subjectDetails);
                dtoList.add(subjectDetailsDTO);
            }
        }
        this.subjectDetailsDTOList = dtoList;
    }

    public String getExamSummaryId() {
        return examSummaryId;
    }

    public void setExamSummaryId(String examSummaryId) {
        this.examSummaryId = examSummaryId;
    }

    public String getRegionalExamId() {
        return regionalExamId;
    }

    public void setRegionalExamId(String regionalExamId) {
        this.regionalExamId = regionalExamId;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public int getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(int studentNum) {
        this.studentNum = studentNum;
    }

    public double getCompositeScores() {
        return compositeScores;
    }

    public void setCompositeScores(double compositeScores) {
        this.compositeScores = compositeScores;
    }

    public int getCompositeRanking() {
        return compositeRanking;
    }

    public void setCompositeRanking(int compositeRanking) {
        this.compositeRanking = compositeRanking;
    }

    public List<SubjectDetailsDTO> getSubjectDetailsDTOList() {
        return subjectDetailsDTOList;
    }

    public void setSubjectDetailsDTOList(List<SubjectDetailsDTO> subjectDetailsDTOList) {
        this.subjectDetailsDTOList = subjectDetailsDTOList;
    }
}
