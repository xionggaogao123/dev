package com.fulaan.reportCard.dto;

import org.apache.commons.lang3.StringUtils;

import com.pojo.reportCard.GroupExamUserRecordEntry;
import com.sys.constants.Constant;

/**
 * Created by scott on 2017/9/30.
 */
public class GroupExamUserRecordDTO {

    private String id;
    private String groupExamDetailId;
    private String userId;
    private double score;
    private int scoreLevel;
    private String scoreStr;
    private String scoreLevelStr;
    private String userName;
    private String userNumber;
    private String mainUserId;
    private String groupId;
    private String communityId;
    private int rank;
    private String rankStr;
    private String examType;
    private String subjectId;
    private Integer sort;
    //班次
    private String bc;
    //校次
    private String xc;


    public GroupExamUserRecordDTO(){

    }
    
    public GroupExamUserRecordDTO(GroupExamUserRecordStrDTO g){
        this.id = g.getId();
        this.groupExamDetailId = g.getGroupExamDetailId();
        this.userId = g.getUserId();
        if ("缺(免)考".equals(g.getScore())) {
            this.score = -1;
        } else if ("未填写".equals(g.getScore())||StringUtils.isBlank(g.getScore())) {
            this.score = -2;
        } else {
            this.score = Double.valueOf(g.getScore());
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
    
    public GroupExamUserRecordDTO(GroupExamUserRecordStrListDTO g){
        this.id = g.getId();
        this.groupExamDetailId = g.getGroupExamDetailId();
        this.userId = g.getUserId();
        StringBuffer b = new StringBuffer();
        StringBuffer bb = new StringBuffer();
        StringBuffer bbb = new StringBuffer();
        for(String s : g.getScore()) {
            if (StringUtils.isNotBlank(s)) {
                if (("缺(免)考").equals(s.trim()) || ("缺").equals(s.trim())) {
                    s = "-1";
                    b.append(s.trim()).append(",");
                } else {
                    b.append(s.trim()).append(",");
                }
            } else {
                s = "-2";
                b.append(s.trim()).append(",");
            }
            
            
        }
        this.scoreStr = b.toString();
        for(Integer t : g.getScoreLevel()) {
            bb.append(String.valueOf(t)).append(",");
        }
        this.scoreLevelStr = bb.toString();
        this.userName = g.getUserName();
        this.userNumber = g.getUserNumber();
        this.mainUserId = g.getMainUserId();
        this.groupId = g.getGroupId();
        this.communityId = g.getCommunityId();
        this.rank = g.getRank();
        this.examType = g.getExamType();
        for (String s : g.getSubjectId()) {
            bbb.append(s).append(",");
        }
        this.subjectId = bbb.toString();
        
    }

    public GroupExamUserRecordDTO(GroupExamUserRecordEntry examUserRecordEntry){
        this.id=examUserRecordEntry.getID().toString();
        this.groupExamDetailId=null!=examUserRecordEntry.getGroupExamDetailId()?
                examUserRecordEntry.getGroupExamDetailId().toString(): Constant.EMPTY;
        this.userId=null!=examUserRecordEntry.getUserId()?
                examUserRecordEntry.getUserId().toString():Constant.EMPTY;
        this.score=examUserRecordEntry.getScore();
        this.scoreLevel=examUserRecordEntry.getScoreLevel();
        this.communityId=null!=examUserRecordEntry.getCommunityId()?
                examUserRecordEntry.getCommunityId().toString():Constant.EMPTY;
        this.groupId=null!=examUserRecordEntry.getGroupId()?
                examUserRecordEntry.getGroupId().toString():Constant.EMPTY;
        this.mainUserId=null!=examUserRecordEntry.getMainUserId()?
                examUserRecordEntry.getMainUserId().toString():Constant.EMPTY;
        this.examType=null!=examUserRecordEntry.getExamType()?
                examUserRecordEntry.getExamType().toString():Constant.EMPTY;
        this.subjectId=null!=examUserRecordEntry.getSubjectId()?
                examUserRecordEntry.getSubjectId().toString():Constant.EMPTY;
    }
    
    public GroupExamUserRecordDTO(GroupExamUserRecordEntry examUserRecordEntry,int i){
        this.id=examUserRecordEntry.getID().toString();
        this.groupExamDetailId=null!=examUserRecordEntry.getGroupExamDetailId()?
                examUserRecordEntry.getGroupExamDetailId().toString(): Constant.EMPTY;
        this.userId=null!=examUserRecordEntry.getUserId()?
                examUserRecordEntry.getUserId().toString():Constant.EMPTY;
        this.scoreStr=examUserRecordEntry.getScoreStr();
        this.scoreLevelStr=examUserRecordEntry.getScoreLevelStr();
        this.communityId=null!=examUserRecordEntry.getCommunityId()?
                examUserRecordEntry.getCommunityId().toString():Constant.EMPTY;
        this.groupId=null!=examUserRecordEntry.getGroupId()?
                examUserRecordEntry.getGroupId().toString():Constant.EMPTY;
        this.mainUserId=null!=examUserRecordEntry.getMainUserId()?
                examUserRecordEntry.getMainUserId().toString():Constant.EMPTY;
        this.examType=null!=examUserRecordEntry.getExamType()?
                examUserRecordEntry.getExamType().toString():Constant.EMPTY;
        this.subjectId=null!=examUserRecordEntry.getSubjectIds()?
                examUserRecordEntry.getSubjectIds():Constant.EMPTY;
                this.sort=examUserRecordEntry.getSort();
                this.bc = examUserRecordEntry.getBc();
                this.xc = examUserRecordEntry.getXc();
        /*this.rankStr=null!=examUserRecordEntry.getRankStr()?
            examUserRecordEntry.getRankStr():Constant.EMPTY;*/
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

    public String getScoreStr() {
        return scoreStr;
    }

    public void setScoreStr(String scoreStr) {
        this.scoreStr = scoreStr;
    }

    public String getScoreLevelStr() {
        return scoreLevelStr;
    }

    public void setScoreLevelStr(String scoreLevelStr) {
        this.scoreLevelStr = scoreLevelStr;
    }

    public String getRankStr() {
        return rankStr;
    }

    public void setRankStr(String rankStr) {
        this.rankStr = rankStr;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getBc() {
        return bc;
    }

    public void setBc(String bc) {
        this.bc = bc;
    }

    public String getXc() {
        return xc;
    }

    public void setXc(String xc) {
        this.xc = xc;
    }
    
    
}
