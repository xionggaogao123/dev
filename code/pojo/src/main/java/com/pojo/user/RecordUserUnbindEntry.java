package com.pojo.user;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2018/1/5.
 * {
 *     老师Id:mainUserId
 *     学生的用户名/手机号:userKey
 *     学生的用户Id:userId
 * }
 */
public class RecordUserUnbindEntry extends BaseDBObject{

    public RecordUserUnbindEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public RecordUserUnbindEntry(ObjectId mainUserId,
                                 ObjectId userId,
                                 String userKey){
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("muid",mainUserId)
                .append("uid",userId)
                .append("uk",userKey)
                .append("ir", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    public void setUserKey(String userKey){
        setSimpleValue("uk",userKey);
    }

    public String getUserKey(){
        return getSimpleStringValue("uk");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setMainUserId(ObjectId mainUserId){
        setSimpleValue("muid",mainUserId);
    }

    public ObjectId getMainUserId(){
        return getSimpleObjecIDValue("muid");
    }


}
