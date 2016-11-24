package com.pojo.activity;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by Hao on 2015/3/3.
 */
public class ActInvitationEntry  extends BaseDBObject {
    /**
	 * 
	 */
	private static final long serialVersionUID = -1247525113195689914L;
	/*
    * aid 活动id
    * gid 被邀请人id
    * msg 邀请信息
    * st  邀请状态
    * */
    public ActInvitationEntry(){
        BasicDBObject base = new BasicDBObject().append("aid", null).append("gid", null).append("msg","").append("st", "");
        setBaseEntry(base);
    }
    public ActInvitationEntry(BasicDBObject basicDBObject){
        setBaseEntry(basicDBObject);
    }
    public ActInvitationEntry(ObjectId actId,ObjectId guestId,String msg,int status){
        BasicDBObject base = new BasicDBObject().append("aid", actId).append("gid", guestId).append("msg",msg).append("st", status);
        setBaseEntry(base);
    }


    public void setActId(ObjectId objectId){
        setSimpleValue("aid",objectId);
    }
    public void setGuestId(ObjectId objectId){
        setSimpleValue("gid",objectId);
    }
    public void setMsg(String msg){
        setSimpleValue("msg",msg);
    }
    public void setStatus(int status){
        setSimpleValue("st",status);
    }

    public ObjectId getActId(){
        return getSimpleObjecIDValue("aid");
    }
    public ObjectId getGuestId(){
        return  getSimpleObjecIDValue("gid");
    }
    public String getMsg(){
        return  getSimpleStringValue("msg");
    }
    public int getStatus(){
        return  getSimpleIntegerValue("st");
    }
}
