package com.fulaan.reportCard.dto;

import com.pojo.reportCard.ScoreRepresentEntry;
import com.sys.constants.Constant;

public class ScoreRepresentDto {

    private String id;
    
    private String groupExamDetailId;
    
    private String subjectId;
    
    private String subjectName;
    
    private String maxScore;
    
    private String scoreOne;
    
    private String scoreTwo;
    
    private String scoreThree;
    
    private String scoreFour;
    
    private String scoreFive;
    
    private String scoreSix;
    
    private String scoreSeven;
    
    private String scoreEight;
    
    private int sort;
    
    private int representNameType;
    
    public ScoreRepresentDto() {
        
    }
    
    public ScoreRepresentDto(ScoreRepresentEntry entry) {
        this.id = entry.getID().toString();
        this.groupExamDetailId = entry.getGroupExamDetailId().toString();
        this.subjectId = null!=entry.getSubjectId()?entry.getSubjectId().toString():Constant.EMPTY;
        this.subjectName = entry.getSubjectName();
        this.maxScore = entry.getMaxScore();
        this.scoreOne = entry.getScoreOne();
        this.scoreTwo = entry.getScoreTwo();
        this.scoreThree = entry.getScoreThree();
        this.scoreFour = entry.getScoreFour();
        this.scoreFive = entry.getScoreFive();
        this.scoreSix = entry.getScoreSix();
        this.scoreSeven = entry.getScoreSeven();
        this.scoreEight = entry.getScoreEight();
        this.sort = entry.getSort();
        this.representNameType = entry.getRepresentNameType();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupExamDetailId() {
        return groupExamDetailId;
    }

    public void setGroupExamDetailId(String groupExamDetailId) {
        this.groupExamDetailId = groupExamDetailId;
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

    public String getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(String maxScore) {
        this.maxScore = maxScore;
    }

    public String getScoreOne() {
        return scoreOne;
    }

    public void setScoreOne(String scoreOne) {
        this.scoreOne = scoreOne;
    }

    public String getScoreTwo() {
        return scoreTwo;
    }

    public void setScoreTwo(String scoreTwo) {
        this.scoreTwo = scoreTwo;
    }

    public String getScoreThree() {
        return scoreThree;
    }

    public void setScoreThree(String scoreThree) {
        this.scoreThree = scoreThree;
    }

    public String getScoreFour() {
        return scoreFour;
    }

    public void setScoreFour(String scoreFour) {
        this.scoreFour = scoreFour;
    }

    public String getScoreFive() {
        return scoreFive;
    }

    public void setScoreFive(String scoreFive) {
        this.scoreFive = scoreFive;
    }

    public String getScoreSix() {
        return scoreSix;
    }

    public void setScoreSix(String scoreSix) {
        this.scoreSix = scoreSix;
    }

    public String getScoreSeven() {
        return scoreSeven;
    }

    public void setScoreSeven(String scoreSeven) {
        this.scoreSeven = scoreSeven;
    }

    public String getScoreEight() {
        return scoreEight;
    }

    public void setScoreEight(String scoreEight) {
        this.scoreEight = scoreEight;
    }

    public int getRepresentNameType() {
        return representNameType;
    }

    public void setRepresentNameType(int representNameType) {
        this.representNameType = representNameType;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
    
    
    
    
    
    
    
}
