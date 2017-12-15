package com.pojo.user;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/12/15.
 */
public class UserActiveRecordEntry extends BaseDBObject{

    public UserActiveRecordEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public UserActiveRecordEntry(ObjectId userId,
                                 long updateTime){
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("uid",userId)
                .append("upt",updateTime)
                .append("ir", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }


    public void setUpdateTime(long updateTime){
        setSimpleValue("upt",updateTime);
    }

    public long getUpdateTime(){
        return getSimpleLongValue("upt");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

}
