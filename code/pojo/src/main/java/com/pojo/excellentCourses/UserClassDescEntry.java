package com.pojo.excellentCourses;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018-09-06.
 *
 * userId             用户id                          uid
 * contactId          关联id                          cid
 * parentId           课节id                          pid
 * currentTime        课节时间                        ctm
 * number             进出次数                        num
 * allTime            总时间                          atm
 * startTime          开始时间                        stm
 * endTime            结束时间                        etm
 * status             状态                            sta
 *
 *
 */
public class UserClassDescEntry extends BaseDBObject {
    public UserClassDescEntry(){

    }

    public UserClassDescEntry(BasicDBObject object){
        super(object);
    }


    //添加构造
    public UserClassDescEntry(
            ObjectId userId,
            ObjectId contactId,
            ObjectId parentId,
            int currentTime,
            int number,
            int allTime,
            long startTime,
            long endTime,
            int status
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("uid", userId)
                .append("cid", contactId)
                .append("pid", parentId)
                .append("ctm",currentTime)
                .append("num",number)
                .append("atm", allTime)
                .append("stm",startTime)
                .append("etm",endTime)
                .append("sta",status)
                .append("isr", Constant.ZERO);
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

    public ObjectId getParentId(){
        return getSimpleObjecIDValue("pid");
    }
    public void setParentId(ObjectId parentId){
        setSimpleValue("pid",parentId);
    }

    public int getAllTime(){
        return getSimpleIntegerValue("atm");
    }

    public void setAllTime(int allTime){
        setSimpleValue("atm",allTime);
    }

    public int getCurrentTime(){
        return getSimpleIntegerValue("ctm");
    }

    public void setNumber(int number){
        setSimpleValue("num",number);
    }

    public int getNumber(){
        return getSimpleIntegerValue("num");
    }

    public void setCurrentTime(int currentTime){
        setSimpleValue("ctm",currentTime);
    }

    public long getEndTime(){
        return getSimpleLongValue("etm");
    }

    public void setEndTime(long endTime){
        setSimpleValue("etm",endTime);
    }


    public long getStartTime(){
        return getSimpleLongValue("stm");
    }

    public void setStartTime(long startTime){
        setSimpleValue("stm",startTime);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }


}
