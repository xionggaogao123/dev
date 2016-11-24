package com.pojo.activity;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * id
 * ui
 * [
 *  u2,t
 *  u3,
 * ]
 * u2
 * Created by yan on 2015/3/3.
 */
public class FriendEntry extends BaseDBObject {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1232296937168011907L;
	/*
    *
    * uid  用户id
    * fid  好友id 数组
    *
    * */
    public FriendEntry(){
        BasicDBObject basicDBObject=new BasicDBObject().append("uid", null).append("fid",null);
        setBaseEntry(basicDBObject);
    }
    public FriendEntry(BasicDBObject baseDBObject){
        super(baseDBObject);
    }

    public FriendEntry(ObjectId userId1,List<ObjectId> friendIds){
        BasicDBObject basicDBObject=new BasicDBObject().append("uid", userId1).append("fid",friendIds);
        setBaseEntry(basicDBObject);
    }
    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }
    public void setUserId(ObjectId objectId){
        setSimpleValue("uid",objectId);
    }
    public List<ObjectId> getFriendIds(){
        List<ObjectId> objectIdList=new ArrayList<ObjectId>();
        BasicDBList basicDBList = (BasicDBList) getSimpleObjectValue("fid");
        if(basicDBList!=null){
            for(Object object:basicDBList){
                objectIdList.add((ObjectId) object);
            }
        }
        return objectIdList;
    }
    public void setFriendIds(List<ObjectId> objectIdList){
        setSimpleValue("fid",MongoUtils.convert(objectIdList));
    }
}
