package com.pojo.fcommunity;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/3/21.
 * 留言反馈功能
 * {
 *    uid:userId 反馈人Id
 *    cn:content 反馈内容
 *    ti:time 反馈时间
 * }
 */
public class FeedbackEntry extends BaseDBObject{

    public FeedbackEntry(DBObject dbObject){
       setBaseEntry((BasicDBObject)dbObject);
    }

    public FeedbackEntry(ObjectId userId,String content){
        BasicDBObject dbObject=new BasicDBObject()
               .append("uid",userId)
               .append("cn",content)
               .append("ti",System.currentTimeMillis())
               .append("ir", Constant.ZERO);
        setBaseEntry(dbObject);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public String getContent(){
        return getSimpleStringValue("cn");
    }

    public void setContent(String content){
        setSimpleValue("cn",content);
    }

    public long getTime(){
        return getSimpleLongValue("ti");
    }

    public void setTime(long time){
        setSimpleValue("ti",time);
    }
}
