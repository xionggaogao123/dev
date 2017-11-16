package com.pojo.controlphone;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2017/11/6.
 Id
 userId        用户id                    uid
 parentId      父id                      pid
 appId         应用id                    aid
 appName       应用名                    anm
 useTime       使用时间（int）           utm
 dateTime      使用日期（long）          dtm
 isNew          是否最新                  isn
 */
public class ControlAppResultEntry extends BaseDBObject {
    public ControlAppResultEntry(){

    }
    public ControlAppResultEntry(BasicDBObject object){
        super(object);
    }

    //添加构造
    public ControlAppResultEntry(
            ObjectId parentId,
            ObjectId userId,
            ObjectId appId,
            String appName,
            long useTime,
            long dateTime
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("pid", parentId)
                .append("uid", userId)
                .append("aid", appId)
                .append("anm", appName)
                .append("utm", useTime)
                .append("dtm", dateTime)
                .append("isr", 0)
                .append("isn",0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public ControlAppResultEntry(
            ObjectId id,
            ObjectId parentId,
            ObjectId userId,
            ObjectId appId,
            String appName,
            long useTime,
            long dateTime
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID,id)
                .append("pid", parentId)
                .append("uid", userId)
                .append("aid", appId)
                .append("anm", appName)
                .append("utm", useTime)
                .append("dtm", dateTime)
                .append("isr", 0)
                .append("isn", 0);
        setBaseEntry(dbObject);
    }

    public ObjectId getParentId(){
        return getSimpleObjecIDValue("pid");
    }
    public void setParentId(ObjectId parentId){
        setSimpleValue("pid",parentId);
    }
    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }
    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }
    public ObjectId getAppid(){
        return getSimpleObjecIDValue("aid");
    }
    public void setAppId(ObjectId appId){
        setSimpleValue("aid", appId);
    }

    public String getAppName(){
        return getSimpleStringValue("anm");
    }
    public void setAppName(String appName){
        setSimpleValue("anm",appName);
    }
    public long getUseTime(){
        return getSimpleLongValue("utm");
    }
    public void setUseTime(long useTime){
        setSimpleValue("utm",useTime);
    }
    public long getDateTime(){
        return getSimpleLongValue("dtm");
    }
    public void setDateTime(long dateTime){
        setSimpleValue("dtm",dateTime);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }
    public int getIsNew(){
        return getSimpleIntegerValue("isn");
    }

    public void setIsNew(int isNew){
        setSimpleValue("isn",isNew);
    }
}
