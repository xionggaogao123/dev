package com.pojo.business;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018-06-07.
 * 模块统计（为运营准备的数据）
 *
 */
public class ModuleTimeEntry extends BaseDBObject {


    public ModuleTimeEntry(){

    }

    public ModuleTimeEntry(BasicDBObject object){
        super(object);
    }

    //添加构造
    public ModuleTimeEntry(
            ObjectId userId,
            int moduleType,
            ObjectId communtiyId
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("uid", userId)
                .append("mty", moduleType)
                .append("cid",communtiyId)
                .append("ctm", System.currentTimeMillis())
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

    public ObjectId getCommunityId(){
        return  getSimpleObjecIDValue("cid");
    }

    public void setCommunityId(ObjectId communityId){
        setSimpleValue("cid", communityId);
    }
}
