package com.pojo.appmarket;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/10/10.
 */
public class AppDetailCommentEntry extends BaseDBObject{

    public AppDetailCommentEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public AppDetailCommentEntry(ObjectId appDetailId,
                                 ObjectId userId,
                                 int star,
                                 String content){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("apd",appDetailId)
                .append("uid",userId)
                .append("sr",star)
                .append("ct",content)
                .append("ti",System.currentTimeMillis())
                .append("ir", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    public void setTime(long time){
        setSimpleValue("ti",time);
    }

    public long getTime(){
        return getSimpleLongValue("ti");
    }

    public void setContent(String content){
        setSimpleValue("ct",content);
    }

    public String getContent(){
        return getSimpleStringValue("ct");
    }

    public void setStar(int star){
        setSimpleValue("sr",star);
    }

    public int getStar(){
        return getSimpleIntegerValue("sr");
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public ObjectId getAppDetailId(){
        return getSimpleObjecIDValue("apd");
    }

    public void setAppDetailId(ObjectId appDetailId){
        setSimpleValue("apd",appDetailId);
    }
}
