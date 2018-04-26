package com.fulaan.reportCard.dto;

import com.pojo.reportCard.GroupExamUserRecordEntry;
import com.sys.constants.Constant;

/**
 * Created by scott on 2017/9/30.
 */
public class GroupExamUserRecordStrDTO {

    private String id;
    private String groupExamDetailId;
    private String userId;
    private String score;
    private int scoreLevel;
    private String userName;
    private String userNumber;
    private String mainUserId;
    private String groupId;
    private String communityId;
    private int rank;
    private String examType;
    private String subjectId;


    public GroupExamUserRecordStrDTO(){

    }
    
    public GroupExamUserRecordStrDTO(GroupExamUserRecordDTO g){
        this.id = g.getId();
        this.groupExamDetailId = g.getGroupExamDetailId();
        this.userId = g.getUserId();
        if (g.getScore() == -1) {
            this.score = "缺(免)考";
        } else if (g.getScore() == -2) {
            this.score = "";
        } else {
            this.score = String.valueOf(g.getScore());
        }
        
        this.scoreLevel= g.getScoreLevel();
        this.userName = g.getUserName();
        this.userNumber = g.getUserNumber();
        this.mainUserId = g.getMainUserId();
        this.groupId = g.getGroupId();
        this.communityId = g.getCommunityId();
        this.rank = g.getRank();
        this.examType = g.getExamType();
        this.subjectId = g.getSubjectId();
    } 

    

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getMainUserId() {
        return mainUserId;
    }

    public void setMainUserId(String mainUserId) {
        this.mainUserId = mainUserId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
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

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public int getScoreLevel() {
        return scoreLevel;
    }

    public void setScoreLevel(int scoreLevel) {
        this.scoreLevel = scoreLevel;
    }
}
