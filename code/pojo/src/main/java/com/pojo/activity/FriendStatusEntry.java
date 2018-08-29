package com.pojo.activity;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by yan on 2015/3/3.
 */
public class FriendStatusEntry extends BaseDBObject {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3097185912155371972L;
	
	/*
    * uid   	发出人id
   	  status	0:(未点击好友请求列表) 1：点击好友请求列表
    * */
    public FriendStatusEntry (){
        
    }
    
    public FriendStatusEntry(BasicDBObject basicDBObject){
        super(basicDBObject);
    }
    
    public  ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }
    
    public void setUserId(ObjectId objectId){
        setSimpleValue("uid",objectId);
    }
    
    public String getStatus(){
        return getSimpleStringValue("status");
    }

    public void setStatus(String status){
        setSimpleValue("status",status);
    }
    
}
