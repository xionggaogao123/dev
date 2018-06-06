package com.pojo.excellentCourses;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by James on 2018-05-30.
 * 充值和消费的日志
 *
 * id
 * userId            uid          用户
 * description       des          描述
 * createTime        ctm          时间
 * contactId         cid          订单id
 */
public class AccountLogEntry extends BaseDBObject{
    public AccountLogEntry(){

    }

    public AccountLogEntry(BasicDBObject object){
        super(object);
    }


    //添加构造
    public AccountLogEntry(
            ObjectId userId,
            ObjectId contactId,
            String description

    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("uid", userId)
                .append("cid",contactId)
                .append("des", description)
                .append("ctm",new Date().getTime())
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }
    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }
    public ObjectId getContactId(){
        return getSimpleObjecIDValue("cid");
    }
    public void setContactId(ObjectId contactId){
        setSimpleValue("cid",contactId);
    }

    public String getDescription(){
        return getSimpleStringValue("des");
    }

    public void setDescription(int description){
        setSimpleValue("des", description);
    }

    public long getCreateTime(){
        return getSimpleLongValue("ctm");
    }

    public void setCreateTime(int createTime){
        setSimpleValue("ctm",createTime);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }


}
