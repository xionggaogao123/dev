package com.pojo.backstage;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by James on 2017/12/4.
 * id               id
 * userId           用户id
 * contactId        联系id
 * createTime       创建时间
 * function         模块名称
 * content          内容
 *
 */
public class LogMessageEntry extends BaseDBObject {

    public LogMessageEntry(){

    }

    public LogMessageEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }
    //添加构造
    public LogMessageEntry(
            ObjectId userId,
            String function,
            ObjectId contactId,
            String content,
            int type
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("uid", userId)
                .append("con", content)
                .append("fun", function)
                .append("cid",contactId)
                .append("typ", type)
                .append("ctm", new Date().getTime())
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public LogMessageEntry(
            ObjectId id,
            ObjectId userId,
            String function,
            ObjectId contactId,
            String content,
            int type
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID,id)
                .append("uid", userId)
                .append("con", content)
                .append("fun", function)
                .append("cid", contactId)
                .append("typ", type)
                .append("ctm", new Date().getTime())
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

    public String getContent(){
        return getSimpleStringValue("con");
    }

    public void setContent(String content){
        setSimpleValue("con",content);
    }


    public int getType(){
        return getSimpleIntegerValue("typ");
    }

    public void setType(int type){
        setSimpleValue("typ",type);
    }

    public String getFunction(){
        return getSimpleStringValue("fun");
    }

    public void setFunction(String function){
        setSimpleValue("fun",function);
    }

    public long getCreateTime(){
        return getSimpleLongValue("ctm");
    }
    public void setCreateTime(long createTime){
        setSimpleValue("ctm",createTime);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }
}
