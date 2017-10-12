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
                                    int examType,
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

    public ObjectId getSubjectId(){
        return getSimpleObjecIDValue("sid");
    }

    public void setSubjectId(ObjectId subjectId){
        setSimpleValue("sid",subjectId);
    }

    public void setExamType(int examType){
        setSimpleValue("etp",examType);
    }

    public int getExamType(){
        return getSimpleIntegerValue("etp");
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
}
