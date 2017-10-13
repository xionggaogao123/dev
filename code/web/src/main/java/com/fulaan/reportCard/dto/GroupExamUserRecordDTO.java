package com.fulaan.reportCard.dto;

import com.pojo.reportCard.GroupExamUserRecordEntry;

/**
 * Created by scott on 2017/9/30.
 */
public class GroupExamUserRecordDTO {

    private String id;
    private String groupExamDetailId;
    private String userId;
    private double score;
    private int scoreLevel;
    private String userName;
    private String userNumber;
    private String mainUserId;
    private String groupId;
    private String communityId;
    private int rank;
    private String examType;
    private String subjectId;


    public GroupExamUserRecordDTO(){

    }

    public GroupExamUserRecordDTO(GroupExamUserRecordEntry examUserRecordEntry){
        this.id=examUserRecordEntry.getID().toString();
        this.groupExamDetailId=examUserRecordEntry.getGroupExamDetailId().toString();
        this.userId=examUserRecordEntry.getUserId().toString();
        this.score=examUserRecordEntry.getScore();
        this.scoreLevel=examUserRecordEntry.getScoreLevel();
        this.communityId=examUserRecordEntry.getCommunityId().toString();
        this.groupId=examUserRecordEntry.getGroupId().toString();
        this.mainUserId=examUserRecordEntry.getMainUserId().toString();
        this.examType=examUserRecordEntry.getExamType().toString();
        this.subjectId=examUserRecordEntry.getSubjectId().toString();
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
