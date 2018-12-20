package com.pojo.reportCard;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/9/30.
 * {
 *    groupExamDetailId:考试Id
 *    mainUserId:发起人Id
 *    userId:学生Id
 *    groupId:群组Id
 *    communityId:社区Id
 *    status: 0保存（待发送） 1表示已删除 2已发送 3已读
 * }
 */
public class GroupExamUserRecordEntry extends BaseDBObject{

    public GroupExamUserRecordEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject) dbObject);
    }

    public GroupExamUserRecordEntry(ObjectId groupExamDetailId,
                                    ObjectId mainUserId,
                                    ObjectId userId,
                                    ObjectId groupId,
                                    ObjectId examType,
                                    ObjectId subjectId,
                                    ObjectId communityId,
                                    double score,
                                    int scoreLevel,
                                    int rank,
                                    int status){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("sid",subjectId)
                .append("etp",examType)
                .append("eid",groupExamDetailId)
                .append("uid",userId)
                .append("muid",mainUserId)
                .append("st",status)
                .append("gid",groupId)
                .append("sc",score)
                .append("scl",scoreLevel)
                .append("rk",rank)
                .append("cmId",communityId);
        setBaseEntry(basicDBObject);
    }
    
    public GroupExamUserRecordEntry(ObjectId groupExamDetailId,
                                    ObjectId mainUserId,
                                    ObjectId userId,
                                    ObjectId groupId,
                                    ObjectId examType,
                                    ObjectId subjectId,
                                    ObjectId communityId,
                                    double score,
                                    int scoreLevel,
                                    int rank,
                                    int status,
                                    int sort){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("sid",subjectId)
                .append("etp",examType)
                .append("eid",groupExamDetailId)
                .append("uid",userId)
                .append("muid",mainUserId)
                .append("st",status)
                .append("gid",groupId)
                .append("sc",score)
                .append("scl",scoreLevel)
                .append("rk",rank)
                .append("cmId",communityId)
                .append("sort", sort);
        setBaseEntry(basicDBObject);
    }
    
    public GroupExamUserRecordEntry(ObjectId groupExamDetailId,
                                    ObjectId mainUserId,
                                    ObjectId userId,
                                    ObjectId groupId,
                                    ObjectId examType,
                                    String subjectIds,
                                    ObjectId communityId,
                                    String score,
                                    String scoreLevel,
                                    int rank,
                                    int status,
                                    String rankStr){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("sids",subjectIds)
                .append("etp",examType)
                .append("eid",groupExamDetailId)
                .append("uid",userId)
                .append("muid",mainUserId)
                .append("st",status)
                .append("gid",groupId)
                .append("scs",score)
                .append("scls",scoreLevel)
                .append("rk",rank)
                .append("cmId",communityId)
                .append("rks", rankStr);
        setBaseEntry(basicDBObject);
    }
    public GroupExamUserRecordEntry(ObjectId groupExamDetailId,
                                    ObjectId mainUserId,
                                    ObjectId userId,
                                    ObjectId groupId,
                                    ObjectId examType,
                                    String subjectIds,
                                    ObjectId communityId,
                                    String score,
                                    String scoreLevel,
                                    int rank,
                                    int status,
                                    String rankStr,
                                    int sort){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("sids",subjectIds)
                .append("etp",examType)
                .append("eid",groupExamDetailId)
                .append("uid",userId)
                .append("muid",mainUserId)
                .append("st",status)
                .append("gid",groupId)
                .append("scs",score)
                .append("scls",scoreLevel)
                .append("rk",rank)
                .append("cmId",communityId)
                .append("rks", rankStr)
                .append("sort", sort);
        setBaseEntry(basicDBObject);
    }
    public GroupExamUserRecordEntry(ObjectId groupExamDetailId,
                                    ObjectId mainUserId,
                                    ObjectId userId,
                                    ObjectId groupId,
                                    ObjectId examType,
                                    String subjectIds,
                                    ObjectId communityId,
                                    String score,
                                    String scoreLevel,
                                    int rank,
                                    int status,
                                    String rankStr,
                                    int sort,
                                    String bc,
                                    String xc){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("sids",subjectIds)
                .append("etp",examType)
                .append("eid",groupExamDetailId)
                .append("uid",userId)
                .append("muid",mainUserId)
                .append("st",status)
                .append("gid",groupId)
                .append("scs",score)
                .append("scls",scoreLevel)
                .append("rk",rank)
                .append("cmId",communityId)
                .append("rks", rankStr)
                .append("sort", sort)
                .append("bc",bc)
                .append("xc",xc);
        setBaseEntry(basicDBObject);
    }
    
    public void setScoreStr(String scoreStr){
        setSimpleValue("scs",scoreStr);
    }

    public String getScoreStr(){
        return getSimpleStringValue("scs");
    }
    
    public void setScoreLevelStr(String scoreLevelStr){
        setSimpleValue("scls",scoreLevelStr);
    }

    public String getScoreLevelStr(){
        return getSimpleStringValue("scls");
    }
    
    public void setSubjectIds(String subjectIds){
        setSimpleValue("sids",subjectIds);
    }

    public String getSubjectIds(){
        return getSimpleStringValue("sids");
    }
    
    public void setRankStr(String rankStr){
        setSimpleValue("rks",rankStr);
    }

    public String getRankStr(){
        return getSimpleStringValue("rks");
    }
    
    public void setBc(String bc){
        setSimpleValue("bc",bc);
    }

    public String getBc(){
        return getSimpleStringValue("bc");
    }
    
    public void setXc(String xc){
        setSimpleValue("xc",xc);
    }

    public String getXc(){
        return getSimpleStringValue("xc");
    }

    public ObjectId getSubjectId(){
        return getSimpleObjecIDValue("sid");
    }

    public void setSubjectId(ObjectId subjectId){
        setSimpleValue("sid",subjectId);
    }

    public void setExamType(ObjectId examType){
        setSimpleValue("etp",examType);
    }

    public ObjectId getExamType(){
        return getSimpleObjecIDValue("etp");
    }

    public int getRank(){
        return getSimpleIntegerValue("rk");
    }

    public void setRank(int rank){
        setSimpleValue("rk",rank);
    }

    public int getScoreLevel(){
        return getSimpleIntegerValue("scl");
    }

    public void setScoreLevel(int scoreLevel){
        setSimpleValue("scl",scoreLevel);
    }

    public double getScore(){
        return getSimpleDoubleValueDef("sc",-1D);
    }

    public void setScore(double score){
        setSimpleValue("sc",score);
    }

    public void setMainUserId(ObjectId mainUserId){
        setSimpleValue("muid",mainUserId);
    }

    public ObjectId getMainUserId(){
        return getSimpleObjecIDValue("muid");
    }

    public void setGroupId(ObjectId groupId){
        setSimpleValue("gid",groupId);
    }

    public ObjectId getGroupId(){
        return getSimpleObjecIDValue("gid");
    }

    public void setCommunityId(ObjectId communityId){
        setSimpleValue("cmId",communityId);
    }

    public ObjectId getCommunityId(){
        return getSimpleObjecIDValue("cmId");
    }

    public void setStatus(int status){
        setSimpleValue("st",status);
    }

    public int getStatus(){
        return getSimpleIntegerValue("st");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setGroupExamDetailId(ObjectId groupExamDetailId){
        setSimpleValue("eid",groupExamDetailId);
    }

    public ObjectId getGroupExamDetailId(){
        return getSimpleObjecIDValue("eid");
    }
    
    public void setSort(int status){
        setSimpleValue("sort",status);
    }

    public int getSort(){
        return getSimpleIntegerValueDef("sort",0);
    }
}
