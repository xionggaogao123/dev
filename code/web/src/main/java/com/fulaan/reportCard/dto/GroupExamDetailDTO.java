package com.fulaan.reportCard.dto;

import com.pojo.reportCard.GroupExamDetailEntry;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;

import java.util.List;

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
    private String subjectIds;//学科id，以逗号分开
    private int maxScore;//最高分
    private List<String> maxScoreList;
    private int qualifyScore;//合格分
    private List<String> qualifyScoreList;
    private int excellentScore;//优秀分
    private List<String> excellentScoreList;
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
    private List<String> excellentPercentList;
    private List<String> qualifyPercentList;
    private List<String> unQualifyPercentList;
    private List<String> avgScoreList;
    private List<String> groupMaxScoreList;
    private List<String> groupMinScoreList;
    private double aPercent;//a率
    private double bPercent;//b率
    private double cPercent;//c率
    private double dPercent;//d率
    private List<String> aPercentList;
    private List<String> bPercentList;
    private List<String> cPercentList;
    private List<String> dPercentList;
    
    //参考人数
    private int examCount;
    private List<Integer> examCountList;
    //未填写人数
    private int unCompleteCount;
    private List<Integer> unCompleteCountList;
    //总人数
    private int allCount;

    private int signCount;//签到总数
    private int signedCount;//已签到人数
    private int unSignCount;//未签到人数

    private String singleScoreId;//每条成绩的Id
    private int singleRank;//孩子排名

    private double qualifyPercent;//合格率
    private double unQualifyPercent;//不合格率
    private int status;//状态 0保存 1删除 2发送（未签字）3已签字
    private boolean isOwner;
    private int showType;
    //录入方式
    private int lrType;
    //是否显示排名  0家长可见1家长不可见
    private int pmType;
    //成绩类型：4种
    private int fsShowType;

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
        
        if (sId != null) {
            return new GroupExamDetailEntry(gId,
                cId, examTypeId, recordScoreType, uid, examName,
                sId, maxScore, qualifyScore, excellentScore, examTime,signCount,signedCount,showType,lrType,pmType);
        } else {
            return new GroupExamDetailEntry(gId,
                cId, examTypeId, recordScoreType, uid, examName,
                subjectIds, maxScore, qualifyScore, excellentScore, examTime,signCount,signedCount,showType,lrType,pmType);
        }
        
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
        this.subjectIds=null!=entry.getSubjectIds()?entry.getSubjectIds().toString():Constant.EMPTY;
        this.maxScore=entry.getMaxScore();
        this.qualifyScore=entry.getQualifyScore();
        this.excellentScore=entry.getExcellentScore();
        this.examTime=entry.getExamTime();
        this.submitTime=entry.getSubmitTime();
        this.signCount=entry.getSignCount();
        this.signedCount=entry.getSignedCount();
        this.status=entry.getStatus();
        this.examStrTime= DateTimeUtils.convert(entry.getExamTime(),DateTimeUtils.DATE_YYYY_MM_DD);
        this.showType = entry.getShowType();
        this.lrType = entry.getLrType();
        this.pmType = entry.getPmType();
        this.fsShowType = entry.getFsShowType();
    }
    
    

    public int getPmType() {
        return pmType;
    }

    public void setPmType(int pmType) {
        this.pmType = pmType;
    }
    
    

    public String getSubjectIds() {
        return subjectIds;
    }

    public void setSubjectIds(String subjectIds) {
        this.subjectIds = subjectIds;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
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

    
    
    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
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

    public int getExamCount() {
        return examCount;
    }

    public void setExamCount(int examCount) {
        this.examCount = examCount;
    }

    public int getUnCompleteCount() {
        return unCompleteCount;
    }

    public void setUnCompleteCount(int unCompleteCount) {
        this.unCompleteCount = unCompleteCount;
    }

    public int getLrType() {
        return lrType;
    }

    public void setLrType(int lrType) {
        this.lrType = lrType;
    }

    public List<String> getExcellentPercentList() {
        return excellentPercentList;
    }

    public void setExcellentPercentList(List<String> excellentPercentList) {
        this.excellentPercentList = excellentPercentList;
    }

    public List<String> getQualifyPercentList() {
        return qualifyPercentList;
    }

    public void setQualifyPercentList(List<String> qualifyPercentList) {
        this.qualifyPercentList = qualifyPercentList;
    }

    public List<String> getUnQualifyPercentList() {
        return unQualifyPercentList;
    }

    public void setUnQualifyPercentList(List<String> unQualifyPercentList) {
        this.unQualifyPercentList = unQualifyPercentList;
    }

    public List<String> getAvgScoreList() {
        return avgScoreList;
    }

    public void setAvgScoreList(List<String> avgScoreList) {
        this.avgScoreList = avgScoreList;
    }

    public List<String> getGroupMaxScoreList() {
        return groupMaxScoreList;
    }

    public void setGroupMaxScoreList(List<String> groupMaxScoreList) {
        this.groupMaxScoreList = groupMaxScoreList;
    }

    public List<String> getGroupMinScoreList() {
        return groupMinScoreList;
    }

    public void setGroupMinScoreList(List<String> groupMinScoreList) {
        this.groupMinScoreList = groupMinScoreList;
    }

    public List<String> getaPercentList() {
        return aPercentList;
    }

    public void setaPercentList(List<String> aPercentList) {
        this.aPercentList = aPercentList;
    }

    public List<String> getbPercentList() {
        return bPercentList;
    }

    public void setbPercentList(List<String> bPercentList) {
        this.bPercentList = bPercentList;
    }

    public List<String> getcPercentList() {
        return cPercentList;
    }

    public void setcPercentList(List<String> cPercentList) {
        this.cPercentList = cPercentList;
    }

    public List<String> getdPercentList() {
        return dPercentList;
    }

    public void setdPercentList(List<String> dPercentList) {
        this.dPercentList = dPercentList;
    }

    public List<Integer> getExamCountList() {
        return examCountList;
    }

    public void setExamCountList(List<Integer> examCountList) {
        this.examCountList = examCountList;
    }

    public List<Integer> getUnCompleteCountList() {
        return unCompleteCountList;
    }

    public void setUnCompleteCountList(List<Integer> unCompleteCountList) {
        this.unCompleteCountList = unCompleteCountList;
    }

    

    public List<String> getMaxScoreList() {
        return maxScoreList;
    }

    public void setMaxScoreList(List<String> maxScoreList) {
        this.maxScoreList = maxScoreList;
    }

    public List<String> getQualifyScoreList() {
        return qualifyScoreList;
    }

    public void setQualifyScoreList(List<String> qualifyScoreList) {
        this.qualifyScoreList = qualifyScoreList;
    }

    public List<String> getExcellentScoreList() {
        return excellentScoreList;
    }

    public void setExcellentScoreList(List<String> excellentScoreList) {
        this.excellentScoreList = excellentScoreList;
    }

    public int getAllCount() {
        return allCount;
    }

    public void setAllCount(int allCount) {
        this.allCount = allCount;
    }

    public int getFsShowType() {
        return fsShowType;
    }

    public void setFsShowType(int fsShowType) {
        this.fsShowType = fsShowType;
    }

   

    
    

    

    
    
    
}
