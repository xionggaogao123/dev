package com.pojo.groupchatrecord;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/12/26.
 */
public class RecordChatPersonalEntry extends BaseDBObject{

    public RecordChatPersonalEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject) dbObject);
    }

    public RecordChatPersonalEntry(ObjectId userId,
                                   ObjectId receiveId,
                                   int chatType){
        BasicDBObject basicDBObject =new BasicDBObject()
                .append("uid",userId)
                .append("rid",receiveId)
                .append("ty",chatType)
                .append("uti",System.currentTimeMillis())
                .append("ir", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    public void setUpdateTime(long updateTime){
        setSimpleValue("uti",updateTime);
    }

    public long getUpdateTime(){
        return getSimpleLongValue("uti");
    }

    public void setChatType(int chatType){
        setSimpleValue("ty",chatType);
    }

    public int getChatType(){
        return getSimpleIntegerValue("ty");
    }

    public void setReceiveId(ObjectId receiveId){
        setSimpleValue("rid",receiveId);
    }

    public ObjectId getReceiveId(){
        return getSimpleObjecIDValue("rid");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }
}
