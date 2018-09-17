package com.pojo.controlphone;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

/**
 * Created by James on 2018-09-17.
 *
 *    分享管控权限表
 * id
 * userId                uid               用户id
 * shareId               hid               共享用户id
 * sonId                 sid               孩子id
 * type                  typ               管控类型
 * roleType              rlt               权限列表
 *
 */
public class ControlShareEntry extends BaseDBObject {


    public ControlShareEntry(){

    }

    public ControlShareEntry(BasicDBObject object){
        super(object);
    }

    //添加构造
    public ControlShareEntry(
            ObjectId userId,
            ObjectId shareId,
            ObjectId sonId,
            int type,
            List<String> roleType
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("uid", userId)
                .append("hid",shareId)
                .append("sid",sonId)
                .append("typ", type)
                .append("rlt", roleType)
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

    public ObjectId getShareId(){
        return getSimpleObjecIDValue("hid");
    }
    public void setShareId(ObjectId shareId){
        setSimpleValue("hid",shareId);
    }

    public ObjectId getSonId(){
        return getSimpleObjecIDValue("sid");
    }
    public void setSonId(ObjectId sonId){
        setSimpleValue("sid",sonId);
    }


    public int getType(){
        return getSimpleIntegerValue("typ");
    }

    public void setType(int type){
        setSimpleValue("typ",type);
    }




    public void setRoleType(List<String> roleType){
        setSimpleValue("rlt",roleType);
    }

    public List<String> getRoleType(){
        @SuppressWarnings("rawtypes")
        List roleType =(List)getSimpleObjectValue("rlt");
        return roleType;
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
