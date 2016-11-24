package com.pojo.examregional;

/**
 * Created by fl on 2015/10/30.
 */
public class RegionalExamDTO {
    private String regionExamId;
    private String name;
    private String gradeName;
    private int gradeType;
    private String examType;
    private long examDate;
    private String term;
    private int rankFlag;

    public RegionalExamDTO(){}
    public RegionalExamDTO(RegionalExamEntry regionalExamEntry){
        this.regionExamId = regionalExamEntry.getID().toString();
        this.name = regionalExamEntry.getName();
        this.gradeName = regionalExamEntry.getGradeName();
        this.gradeType = regionalExamEntry.getGradeType();
        this.term = regionalExamEntry.getTerm();
        this.examType = regionalExamEntry.getExamType();
        this.rankFlag = regionalExamEntry.getRankingFlag();
    }

    public String getRegionExamId() {
        return regionExamId;
    }

    public void setRegionExamId(String regionExamId) {
        this.regionExamId = regionExamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public int getGradeType() {
        return gradeType;
    }

    public void setGradeType(int gradeType) {
        this.gradeType = gradeType;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public long getExamDate() {
        return examDate;
    }

    public void setExamDate(long examDate) {
        this.examDate = examDate;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public int getRankFlag() {
        return rankFlag;
    }

    public void setRankFlag(int rankFlag) {
        this.rankFlag = rankFlag;
    }
}
