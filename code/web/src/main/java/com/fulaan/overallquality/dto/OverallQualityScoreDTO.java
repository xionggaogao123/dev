package com.fulaan.overallquality.dto;

import com.pojo.overallquality.OverallQualityScoreSetEntry;

/**
 * Created by guojing on 2016/8/11.
 */
public class OverallQualityScoreDTO {

    private String id;
    private String schoolId;
    private String scoreName;
    private int scoreNum;

    public OverallQualityScoreDTO(){

    }

    public OverallQualityScoreDTO(OverallQualityScoreSetEntry e){
        this.id=e.getID().toString();
        this.schoolId=e.getSchoolId().toString();
        this.scoreName=e.getScoreName();
        this.scoreNum=e.getScoreNum();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getScoreName() {
        return scoreName;
    }

    public void setScoreName(String scoreName) {
        this.scoreName = scoreName;
    }

    public int getScoreNum() {
        return scoreNum;
    }

    public void setScoreNum(int scoreNum) {
        this.scoreNum = scoreNum;
    }
}
