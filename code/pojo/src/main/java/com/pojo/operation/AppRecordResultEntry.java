package com.pojo.operation;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 *
 * 签到提交记录表（后来的签到记录不用管）
 * Created by James on 2017/8/25.
 Id                                          	id
 ParentId             关联作业id                pid
 isLoad               是否签到                  isl(1 未签到 2已签到)
 dateTime             签到时间                  dtm
 userId               用户id                    uid
 */
public class AppRecordResultEntry extends BaseDBObject {
    public AppRecordResultEntry(){

    }
    public AppRecordResultEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }
    //添加构造
    public AppRecordResultEntry(
            ObjectId parentId,
            ObjectId userId,
            long dateTime,
            String userName,
            int isLoad
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("pid", parentId)
                .append("uid", userId)
                .append("dtm", dateTime)
                .append("una", userName)
                .append("ctm", new Date().getTime())
                .append("isl",isLoad)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public AppRecordResultEntry(
            ObjectId id,
            ObjectId parentId,
            ObjectId userId,
            long dateTime,
            String userName,
            int isLoad
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID, id)
                .append("pid", parentId)
                .append("uid",userId)
                .append("dtm", dateTime)
                .append("una",userName)
                .append("ctm", new Date().getTime())
                .append("isl", isLoad)
                .append("isr", 0);
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


    public int getIsLoad(){
        return getSimpleIntegerValue("isl");
    }

    public void setIsLoad(int isLoad){
        setSimpleValue("isl",isLoad);
    }

    public String getUserName(){
        return getSimpleStringValue("una");
    }

    public void setUserName(String userName){
        setSimpleValue("una",userName);
    }
    public long getCreateTime(){
        return getSimpleLongValue("ctm");
    }
    public void setCreateTime(long createTime){
        setSimpleValue("ctm",createTime);
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
}
