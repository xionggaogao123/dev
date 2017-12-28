package com.pojo.appactivity;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/12/27.
 */
public class AppActivityUserEntry extends BaseDBObject{

    public AppActivityUserEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject) dbObject);
    }

    public AppActivityUserEntry(ObjectId activityId,
                                ObjectId userId){
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("ai",activityId)
                .append("uid",userId)
                .append("sti",System.currentTimeMillis())
                .append("ir", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    public void setSubmitTime(long submitTime){
        setSimpleValue("sti",submitTime);
    }

    public long getSubmitTime(){
        return getSimpleLongValue("sti");
    }


    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setActivityId(ObjectId activityId){
        setSimpleValue("ai",activityId);
    }

    public ObjectId getActivityId(){
        return getSimpleObjecIDValue("ai");
    }
}
