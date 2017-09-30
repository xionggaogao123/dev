package com.fulaan.reportCard.dto;

import com.pojo.reportCard.RecordExamScoreEntry;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/9/30.
 */
public class RecordExamScoreDTO {

    private String id;
    private String groupExamDetailId;
    private String userId;
    private double score;
    private int scoreLevel;

    public RecordExamScoreDTO(){

    }

    public RecordExamScoreDTO(RecordExamScoreEntry examScoreEntry){
        this.id=examScoreEntry.getID().toString();
        this.groupExamDetailId=examScoreEntry.getGroupExamDetailId().toString();
        this.userId=examScoreEntry.getUserId().toString();
        this.score=examScoreEntry.getScore();
        this.scoreLevel=examScoreEntry.getScoreLevel();
    }

    public RecordExamScoreEntry buildEntry(){
        ObjectId uId=null;
        if(StringUtils.isNotBlank(userId)&&ObjectId.isValid(userId)){
            uId=new ObjectId(userId);
        }
        ObjectId eId=null;
        if(StringUtils.isNotBlank(groupExamDetailId)&&ObjectId.isValid(groupExamDetailId)){
            eId=new ObjectId(groupExamDetailId);
        }
        return new RecordExamScoreEntry(eId,
                uId, score, scoreLevel);
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getScoreLevel() {
        return scoreLevel;
    }

    public void setScoreLevel(int scoreLevel) {
        this.scoreLevel = scoreLevel;
    }
}
