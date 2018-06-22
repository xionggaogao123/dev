package com.pojo.business;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018-06-21.
 * 模块禁言表
 * id
 * userId                     用户id                         uid
 * moduleType                 模块名称                       mot
 * type                       处理手段                       typ         0 禁言   1 禁用
 * startTime                  开始时间                       stm
 * endTime                    结束时间                       etm0
 *
 */
public class BanningSpeakingEntry extends BaseDBObject {

    public BanningSpeakingEntry(){


    }

    public BanningSpeakingEntry(BasicDBObject dbObject){
        super(dbObject);
    }

    //添加构造
    public BanningSpeakingEntry(
            ObjectId userId,
            int moduleType,
            int type,
            long startTime,
            long endTime
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("uid", userId)
                .append("mty", moduleType)
                .append("typ", type)
                .append("stm", startTime)
                .append("etm", endTime)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }


    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }
    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public int getModuleType(){
        return getSimpleIntegerValue("mty");
    }

    public void setModuleType(int moduleType){
        setSimpleValue("mty",moduleType);
    }
    public long getStartTime(){
        return getSimpleLongValue("stm");
    }

    public void setStartTime(long startTime){
        setSimpleValue("stm",startTime);
    }
    public long getEndTime(){
        return getSimpleLongValue("etm");
    }

    public void setEndTime(long endTime){
        setSimpleValue("etm",endTime);
    }

    public int getType(){
        return getSimpleIntegerValue("typ");
    }

    public void setType(int type){
        setSimpleValue("typ",type);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }

}
