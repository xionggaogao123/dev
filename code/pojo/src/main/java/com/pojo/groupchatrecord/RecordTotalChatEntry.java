package com.pojo.groupchatrecord;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/12/26.
 */
public class RecordTotalChatEntry extends BaseDBObject{

    public RecordTotalChatEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public RecordTotalChatEntry(ObjectId userId){
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("uid",userId)
                .append("cc", Constant.ONE);
        setBaseEntry(basicDBObject);
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setChatCount(int chatCount){
        setSimpleValue("cc",chatCount);
    }

    public int getChatCount(){
        return getSimpleIntegerValue("cc");
    }
}
