package com.pojo.controlphone;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * 管控基础数据设定表
 * Created by James on 2017/11/6.
 * Id
 userId             用户id                uid
 parentId            父id                 pid
 time              控制时间              tim
 */
public class ControlTimeEntry extends BaseDBObject {
    public ControlTimeEntry(){

    }
    public ControlTimeEntry(BasicDBObject object){
        super(object);
    }

    //添加构造
    public ControlTimeEntry(
            ObjectId parentId,
            ObjectId userId,
            long time
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("pid", parentId)
                .append("uid", userId)
                .append("tim", time)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public ControlTimeEntry(
            ObjectId id,
            ObjectId parentId,
            ObjectId userId,
            long time
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID,id)
                .append("pid", parentId)
                .append("uid",userId)
                .append("tim", time)
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
}
