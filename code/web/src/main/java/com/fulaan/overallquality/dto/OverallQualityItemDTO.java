package com.fulaan.overallquality.dto;

import com.pojo.overallquality.OverallQualityItemEntry;

/**
 * Created by guojing on 2016/8/4.
 */
public class OverallQualityItemDTO {

    private String id;
    private String schoolId;
    private String itemName;
    private String scoreSetId;
    private String scoreName;

    public OverallQualityItemDTO(){

    }

    public OverallQualityItemDTO(OverallQualityItemEntry e){
        this.id=e.getID().toString();
        this.schoolId=e.getSchoolId().toString();
        this.itemName=e.getItemName();
        this.scoreSetId=e.getScoreSetId().toHexString();
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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getScoreSetId() {
        return scoreSetId;
    }

    public void setScoreSetId(String scoreSetId) {
        this.scoreSetId = scoreSetId;
    }

    public String getScoreName() {
        return scoreName;
    }

    public void setScoreName(String scoreName) {
        this.scoreName = scoreName;
    }
}
