package com.fulaan.overallquality.dto;

import com.pojo.overallquality.ClassOverallQualityScore;

/**
 * Created by guojing on 2016/8/28.
 */
public class ClassOverallQualityScoreInfo {
    private String scoreId;
    private String type;
    private int score;

    public ClassOverallQualityScoreInfo(){

    }

    public ClassOverallQualityScoreInfo(ClassOverallQualityScore e){
        this.scoreId = e.getScoreId().toString();
        this.type = e.getType();
        this.score = e.getScore();
    }

    public String getScoreId() {
        return scoreId;
    }

    public void setScoreId(String scoreId) {
        this.scoreId = scoreId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
