package com.fulaan.reportCard.dto;

import com.pojo.reportCard.GroupExamDetailEntry;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/9/30.
 */
public class GroupExamDetailDTO {

    private String id; //关键字Id
    private String groupId;//群组Id
    private String communityId;//社区Id
    private String examType;//考试类型Id
    private int recordScoreType;//分数类型 1:分值制 2:等地址
    private String userId;//用户Id
    private String examName;//考试名称
    private String subjectId;//学科Id
    private int maxScore;//最高分
    private int qualifyScore;//合格分
    private int excellentScore;//优秀分
    private long examTime;//考试时间
    private long submitTime;//提交时间
    private String examStrTime;//考试时间格式yyyy-MM-dd

    private String userName;//用户名
    private String subjectName;//学科名
    private String groupName;//群组名
    private String examTypeName;//考试类型名称

    //我的孩子的分值
    private String childUserId;//孩子用户Id
    private String childUserName;//孩子用户姓名
    private double score;//分数
    private int scoreLevel;//等第制
    private int isSign;//没有用
    private double avgScore;//平均分
    private double groupMaxScore;//群组最高分
    private double groupMinScore;//群组最低分
    private double excellentPercent;//优秀率
    private double aPercent;//a率
    private double bPercent;//b率
    private double cPercent;//c率
    private double dPercent;//d率

    private int signCount;//签到总数
    private int signedCount;//已签到人数
    private int unSignCount;//未签到人数

    private String singleScoreId;//每条成绩的Id
    private int singleRank;//孩子排名

    private double qualifyPercent;//合格率
    private double unQualifyPercent;//不合格率
    private int status;//状态 0保存 1删除 2发送（未签字）3已签字

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
        ObjectId examTypeId=null;
        if(StringUtils.isNotBlank(examType)&&ObjectId.isValid(examType)){
            examTypeId=new ObjectId(examType);
        }
        return new GroupExamDetailEntry(gId,
                cId, examTypeId, recordScoreType, uid, examName,
                sId, maxScore, qualifyScore, excellentScore, examTime,signCount,signedCount);
    }

    public GroupExamDetailDTO(GroupExamDetailEntry entry){
        this.id=null!=entry.getID()?entry.getID().toString():Constant.EMPTY;
        this.groupId=null!=entry.getGroupId()?entry.getGroupId().toString(): Constant.EMPTY;
        this.communityId=null!=entry.getCommunityId()?entry.getCommunityId().toString():Constant.EMPTY;
        this.examName=entry.getExamName();
        this.examType=null!=entry.getExamType()?entry.getExamType().toString():Constant.EMPTY;
        this.recordScoreType=entry.getRecordScoreType();
        this.userId=null!=entry.getUserId()?entry.getUserId().toString():Constant.EMPTY;
        this.subjectId=null!=entry.getSubjectId()?entry.getSubjectId().toString():Constant.EMPTY;
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

    public double getGroupMinScore() {
        return groupMinScore;
    }

    public void setGroupMinScore(double groupMinScore) {
        this.groupMinScore = groupMinScore;
    }

    public String getExamTypeName() {
        return examTypeName;
    }

    public void setExamTypeName(String examTypeName) {
        this.examTypeName = examTypeName;
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

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
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
