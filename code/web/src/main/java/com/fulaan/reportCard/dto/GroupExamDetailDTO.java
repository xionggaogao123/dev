package com.fulaan.reportCard.dto;

import com.pojo.reportCard.GroupExamDetailEntry;
import com.sys.utils.DateTimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/9/30.
 */
public class GroupExamDetailDTO {

    private String id;
    private String groupId;
    private String communityId;
    private int examType;
    private int recordScoreType;
    private String userId;
    private String examName;
    private String subjectId;
    private int maxScore;
    private int qualifyScore;
    private int excellentScore;
    private long examTime;
    private long submitTime;
    private String examStrTime;

    private String userName;
    private String subjectName;
    private String groupName;

    //我的孩子的分值
    private String childUserId;
    private String childUserName;
    private double score;
    private int scoreLevel;
    private String childrenName;
    private int isSign;
    private double avgScore;
    private double groupMaxScore;
    private double excellentPercent;
    private double aPercent;
    private double bPercent;
    private double cPercent;
    private double dPercent;

    private int signCount;
    private int signedCount;
    private int unSignCount;

    private String singleScoreId;
    private int singleRank;

    private double qualifyPercent;
    private double unQualifyPercent;
    private int status;

    public GroupExamDetailDTO(){

    }

    public GroupExamDetailEntry buildEntry(){
        ObjectId uid=null;
        if(StringUtils.isNotBlank(userId)&&ObjectId.isValid(userId)){
            uid=new ObjectId(userId);
        }
        ObjectId gId=null;
        if(StringUtils.isNotBlank(groupId)&&ObjectId.isValid(groupId)){
            gId=new ObjectId(groupId);
        }
        ObjectId cId=null;
        if(StringUtils.isNotBlank(communityId)&&ObjectId.isValid(communityId)){
            cId=new ObjectId(communityId);
        }
        ObjectId sId=null;
        if(StringUtils.isNotBlank(subjectId)&&ObjectId.isValid(subjectId)){
            sId=new ObjectId(subjectId);
        }

        return new GroupExamDetailEntry(gId,
                cId, examType, recordScoreType, uid, examName,
                sId, maxScore, qualifyScore, excellentScore, examTime,signCount,signedCount);
    }

    public GroupExamDetailDTO(GroupExamDetailEntry entry){
        this.id=entry.getID().toString();
        this.groupId=entry.getGroupId().toString();
        this.communityId=entry.getCommunityId().toString();
        this.examName=entry.getExamName();
        this.examType=entry.getExamType();
        this.recordScoreType=entry.getRecordScoreType();
        this.userId=entry.getUserId().toString();
        this.subjectId=entry.getSubjectId().toString();
        this.maxScore=entry.getMaxScore();
        this.qualifyScore=entry.getQualifyScore();
        this.excellentScore=entry.getExcellentScore();
        this.examTime=entry.getExamTime();
        this.submitTime=entry.getSubmitTime();
        this.signCount=entry.getSignCount();
        this.signedCount=entry.getSignedCount();
        this.status=entry.getStatus();
        this.examStrTime= DateTimeUtils.convert(entry.getExamTime(),DateTimeUtils.DATE_YYYY_MM_DD);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getQualifyPercent() {
        return qualifyPercent;
    }

    public void setQualifyPercent(double qualifyPercent) {
        this.qualifyPercent = qualifyPercent;
    }

    public double getUnQualifyPercent() {
        return unQualifyPercent;
    }

    public void setUnQualifyPercent(double unQualifyPercent) {
        this.unQualifyPercent = unQualifyPercent;
    }

    public double getExcellentPercent() {
        return excellentPercent;
    }

    public void setExcellentPercent(double excellentPercent) {
        this.excellentPercent = excellentPercent;
    }

    public int getSingleRank() {
        return singleRank;
    }

    public void setSingleRank(int singleRank) {
        this.singleRank = singleRank;
    }

    public double getGroupMaxScore() {
        return groupMaxScore;
    }

    public void setGroupMaxScore(double groupMaxScore) {
        this.groupMaxScore = groupMaxScore;
    }

    public double getbPercent() {
        return bPercent;
    }

    public void setbPercent(double bPercent) {
        this.bPercent = bPercent;
    }

    public double getcPercent() {
        return cPercent;
    }

    public void setcPercent(double cPercent) {
        this.cPercent = cPercent;
    }

    public double getdPercent() {
        return dPercent;
    }

    public void setdPercent(double dPercent) {
        this.dPercent = dPercent;
    }

    public String getSingleScoreId() {
        return singleScoreId;
    }

    public void setSingleScoreId(String singleScoreId) {
        this.singleScoreId = singleScoreId;
    }

    public String getChildUserName() {
        return childUserName;
    }

    public void setChildUserName(String childUserName) {
        this.childUserName = childUserName;
    }

    public int getUnSignCount() {
        return unSignCount;
    }

    public void setUnSignCount(int unSignCount) {
        this.unSignCount = unSignCount;
    }

    public int getSignCount() {
        return signCount;
    }

    public void setSignCount(int signCount) {
        this.signCount = signCount;
    }

    public int getSignedCount() {
        return signedCount;
    }

    public void setSignedCount(int signedCount) {
        this.signedCount = signedCount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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

    public String getChildrenName() {
        return childrenName;
    }

    public void setChildrenName(String childrenName) {
        this.childrenName = childrenName;
    }

    public int getIsSign() {
        return isSign;
    }

    public void setIsSign(int isSign) {
        this.isSign = isSign;
    }

    public double getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(double avgScore) {
        this.avgScore = avgScore;
    }

    public double getaPercent() {
        return aPercent;
    }

    public void setaPercent(double aPercent) {
        this.aPercent = aPercent;
    }

    public String getExamStrTime() {
        return examStrTime;
    }

    public void setExamStrTime(String examStrTime) {
        this.examStrTime = examStrTime;
    }

    public long getExamTime() {
        return examTime;
    }

    public void setExamTime(long examTime) {
        this.examTime = examTime;
    }

    public long getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(long submitTime) {
        this.submitTime = submitTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getExamType() {
        return examType;
    }

    public void setExamType(int examType) {
        this.examType = examType;
    }

    public int getRecordScoreType() {
        return recordScoreType;
    }

    public void setRecordScoreType(int recordScoreType) {
        this.recordScoreType = recordScoreType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public int getQualifyScore() {
        return qualifyScore;
    }

    public void setQualifyScore(int qualifyScore) {
        this.qualifyScore = qualifyScore;
    }

    public int getExcellentScore() {
        return excellentScore;
    }

    public void setExcellentScore(int excellentScore) {
        this.excellentScore = excellentScore;
    }

    public String getChildUserId() {
        return childUserId;
    }

    public void setChildUserId(String childUserId) {
        this.childUserId = childUserId;
    }
}
