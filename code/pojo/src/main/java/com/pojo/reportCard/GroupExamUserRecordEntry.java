package com.pojo.reportCard;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/9/30.
 */
public class GroupExamUserRecordEntry extends BaseDBObject{

    public GroupExamUserRecordEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject) dbObject);
    }

    public GroupExamUserRecordEntry(ObjectId groupExamDetailId,
                                    ObjectId userId,
                                    ObjectId groupId,
                                    ObjectId communityId,
                                    int status){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("eid",groupExamDetailId)
                .append("uid",userId)
                .append("st",status)
                .append("gid",groupId)
                .append("cmId",communityId)
                .append("ir", Constant.ZERO);
        setBaseEntry(basicDBObject);
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
