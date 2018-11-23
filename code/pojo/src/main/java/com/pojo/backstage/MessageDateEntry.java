package com.pojo.backstage;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by James on 2018-11-23.
 * 各类信息提醒时间记录表
 *
 * userId             用户id               uid
 * dateTime           提醒时间             dtm
 * type               提醒类型             typ          1 社群  2 好友
 *
 *
 */
public class MessageDateEntry extends BaseDBObject {
    public MessageDateEntry(){

    }

    public MessageDateEntry(BasicDBObject dbObject){
        super(dbObject);
    }

    //添加构造
    public MessageDateEntry(
            ObjectId userId,
            int type
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("uid", userId)
                .append("typ", type)
                .append("ctm", new Date().getTime())
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public ObjectId getUserId(){
        return  getSimpleObjecIDValue("uid");
    }

    public void setType(int type){
        setSimpleValue("typ",type);
    }

    public int getType(){
        return getSimpleIntegerValue("typ");
    }

    public void setDateTime(long dateTime){
        setSimpleValue("dtm",dateTime);
    }

    public long getDateTime(){
        return getSimpleLongValue("dtm");
    }
}
