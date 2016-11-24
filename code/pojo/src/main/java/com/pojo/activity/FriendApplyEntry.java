package com.pojo.activity;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by yan on 2015/3/3.
 */
public class FriendApplyEntry extends BaseDBObject {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3097185912155371972L;
	
	/*
    * uid   发出人id
    * rid   响应人id
    * ad    申请日期
    * rd    响应日期
    * ac    是否接受
    * ct    附带的申请内容
    * */
    public FriendApplyEntry (){
        BasicDBObject baseEntry = new BasicDBObject().append("uid", null).
                append("rid", null).
                append("ad", null).
                append("rd", null).
                append("ac", null).
                append("ct", "");
        setBaseEntry(baseEntry);
    }
    
    public FriendApplyEntry(BasicDBObject basicDBObject){
        super(basicDBObject);
    }
    public FriendApplyEntry(ObjectId userId,ObjectId respondent,long applyDate,long respondDate,int accepted,String content){
        BasicDBObject baseEntry = new BasicDBObject().append("uid", userId).
                append("rid", respondent).
                append("ad", applyDate).
                append("rd", respondDate).
                append("ac", accepted).
                append("ct", content);
        setBaseEntry(baseEntry);
    }
    
    //get
    public  ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }
    public ObjectId getRespondent(){
        return getSimpleObjecIDValue("rid");
    }
    public long getApplyDate(){
        return getSimpleLongValue("ad");
    }
    public long getRespondDate(){
        return  getSimpleLongValue("rd");
    }
    public int getAccepted(){
        return  getSimpleIntegerValue("ac");
    }
    public String getContent(){
        return getSimpleStringValue("ct");
    }

    //set
    public void setUserId(ObjectId objectId){
        setSimpleValue("uid",objectId);
    }
    public void setRespond(ObjectId objectId){
        setSimpleValue("rid",objectId);
    }
    public void setApplyDate(long applyDate){
        setSimpleValue("ad",applyDate);
    }
    public void setRespondDate(long respondDate){
        setSimpleValue("rd",respondDate);
    }
    public void setAccepted(int accepted){
        setSimpleValue("ac",accepted);
    }
    public void setContent(String content){
        setSimpleValue("ct",content);
    }
}
