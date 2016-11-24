package com.pojo.examregional;

/**
 * Created by fl on 2015/10/30.
 */
public class SubjectDetailsDTO {
    private String subjectId;
    private String subjectName;
    private double averageScore;
    private double maxScore;
    private double minScore;
    private int passNum;
    private double passRate;
    private int excellentNumber;
    private double excellentRate;
    private int compositeRanking;
    private double compositeScores;
    private double biJun;//比均：全校平均分/全区平均分
    private double chaoJun;//超均：（全校平均分-全区平均分）/全区平均分

    public SubjectDetailsDTO(){}
    public SubjectDetailsDTO(SubjectDetails subjectDetails){
        this.subjectId = subjectDetails.getSubjectId().toString();
        this.subjectName = subjectDetails.getSubjectName();
        this.averageScore = Math.round(subjectDetails.getAverageScore()*100)/100.0;
        this.maxScore = subjectDetails.getMaxScore();
        this.minScore = subjectDetails.getMinScore();
        this.passNum = subjectDetails.getPassNumber();
        this.passRate = Math.round(subjectDetails.getPassRate()*100)/100.0;
        this.excellentNumber = subjectDetails.getExcellentNumber();
        this.excellentRate = Math.round(subjectDetails.getExcellentRate()*100)/100.0;
        this.compositeRanking = subjectDetails.getCompositeRanking();
        this.compositeScores = Math.round(subjectDetails.getCompositeScores()*100)/100.0;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }

    public double getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(double maxScore) {
        this.maxScore = maxScore;
    }

    public double getMinScore() {
        return minScore;
    }

    public void setMinScore(double minScore) {
        this.minScore = minScore;
    }

    public int getPassNum() {
        return passNum;
    }

    public void setPassNum(int passNum) {
        this.passNum = passNum;
    }

    public double getPassRate() {
        return passRate;
    }

    public void setPassRate(double passRate) {
        this.passRate = passRate;
    }

    public int getExcellentNumber() {
        return excellentNumber;
    }

    public void setExcellentNumber(int excellentNumber) {
        this.excellentNumber = excellentNumber;
    }

    public double getExcellentRate() {
        return excellentRate;
    }

    public void setExcellentRate(double excellentRate) {
        this.excellentRate = excellentRate;
    }

    public int getCompositeRanking() {
        return compositeRanking;
    }

    public void setCompositeRanking(int compositeRanking) {
        this.compositeRanking = compositeRanking;
    }

    public double getCompositeScores() {
        return compositeScores;
    }

    public void setCompositeScores(double compositeScores) {
        this.compositeScores = compositeScores;
    }

    public double getBiJun() {
        return biJun;
    }

    public void setBiJun(double biJun) {
        this.biJun = biJun;
    }

    public double getChaoJun() {
        return chaoJun;
    }

    public void setChaoJun(double chaoJun) {
        this.chaoJun = chaoJun;
    }
}
