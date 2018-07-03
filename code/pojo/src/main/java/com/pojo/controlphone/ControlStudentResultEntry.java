package com.pojo.controlphone;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * 学生数据缓存（只保留 7 天）(一天一条记录)
 * Created by James on 2018-05-21.
 * Id
 * parentId                 所属家长用户                pid
 * userId                   用户id                      uid
 * newAppUser               最新用户应用使用时间        nau
 * newAppTime               最新用户应用使用版本        nat
 * dateTime                 记录日期                    dtm
 *
 */
public class ControlStudentResultEntry  extends BaseDBObject {
    public ControlStudentResultEntry(){

    }

    public ControlStudentResultEntry(BasicDBObject object){
        super(object);
    }

    //添加构造
    public ControlStudentResultEntry(
            ObjectId parentId,
            ObjectId userId,
            long newAppUser,
            long newAppTime,
            long time,
            int electric,
            long dateTime
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("pid", parentId)
                .append("uid", userId)
                .append("nau", newAppUser)
                .append("nat",newAppTime)
                .append("tim",time)
                .append("ele",electric)
                .append("dtm",dateTime)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public ControlStudentResultEntry(
            ObjectId id,
            ObjectId parentId,
            ObjectId userId,
            long newAppUser,
            long newAppTime,
            long time,
            int electric,
            long dateTime
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID,id)
                .append("pid", parentId)
                .append("uid", userId)
                .append("nau", newAppUser)
                .append("nat",newAppTime)
                .append("tim", time)
                .append("ele",electric)
                .append("dtm",dateTime)
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

    public long getNewAppTime(){
        return getSimpleLongValue("nat");
    }
    public void setNewAppTime(long newAppTime){
        setSimpleValue("nat",newAppTime);
    }

    public long getNewAppUser(){
        return getSimpleLongValue("nau");
    }
    public void setNewAppUser(long newAppUser){
        setSimpleValue("nau",newAppUser);
    }
    public long getTime(){
        return getSimpleLongValue("tim");
    }
    public void setTime(long time){
        setSimpleValue("tim",time);
    }
    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }

    public int getElectric(){
        return getSimpleIntegerValueDef("ele",100);
    }

    public void setElectric(int electric){
        setSimpleValue("ele",electric);
    }
}
