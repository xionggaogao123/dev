package com.db.activity;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.activity.FriendEntry;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yan on 2015/3/3.
 */
public class FriendDao extends BaseDao {

    public int countFriend(ObjectId objectId) {
        BasicDBObject query=new BasicDBObject("uid",objectId);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(),Constant.COLLECTION_FRIEND_NAME,query,Constant.FIELDS);
        if(dbObject==null) return 0;
        FriendEntry friendEntry=new FriendEntry((BasicDBObject)dbObject);
        return friendEntry.getFriendIds().size();
    }
    

    public List<ObjectId> findMyFriendIds(ObjectId objectId) {
        BasicDBObject query=new BasicDBObject("uid",objectId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FRIEND_NAME, query, Constant.FIELDS);
        if(dbObject==null) return  new ArrayList<ObjectId>();
        FriendEntry friendEntry=new FriendEntry((BasicDBObject)dbObject);
        return friendEntry.getFriendIds();
    }
    //确定二者是否为好友关系
    public boolean isFriend(ObjectId u1, ObjectId u2) {
        BasicDBObject query=new BasicDBObject("uid",u1).append("fid",u2);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(),Constant.COLLECTION_FRIEND_NAME,query,Constant.FIELDS);
        return dbObject==null ? false:true;
    }
    /*
    * 根据学校推荐好友
    * */
    public List<UserEntry> recommendFriendBySchool(ObjectId objectId, List<ObjectId> objectIdList, Integer begin, Integer pageSize) {
    	
        BasicDBObject query=new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_NOTIN,objectIdList)).append("si",objectId).append("r", UserRole.STUDENT.getRole()).append("ir", 0);
        //List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(),Constant.COLLECTION_USER_NAME,query,Constant.FIELDS,Constant.MONGO_SORTBY_DESC,begin,pageSize);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(),Constant.COLLECTION_USER_NAME,query,new BasicDBObject("nm",1).append("nnm", 1).append("avt", 1),Constant.MONGO_SORTBY_DESC,begin,pageSize);
        List<UserEntry> userEntryList=new ArrayList<UserEntry>();
        for(DBObject dbObject:dbObjectList){
            UserEntry userEntry=new UserEntry((BasicDBObject)dbObject);
            userEntryList.add(userEntry);
        }
        return userEntryList;
    }

    
    
    
    public int recommendFriendBySchoolCount(ObjectId objectId, List<ObjectId> objectIdList) {
        BasicDBObject query=new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_NOTIN,objectIdList)).append("si",objectId).append("r", UserRole.STUDENT.getRole()).append("ir", 0);
        int count=count(MongoFacroty.getAppDB(),Constant.COLLECTION_USER_NAME,query);
        return count;
    }

    public void deleteOneFriend(ObjectId objectId, ObjectId objectId1) {
        BasicDBObject query=new BasicDBObject("uid",objectId);
        BasicDBObject update=new BasicDBObject(Constant.MONGO_PULL,new BasicDBObject("fid",objectId1));
        update(MongoFacroty.getAppDB(),Constant.COLLECTION_FRIEND_NAME,query,update);
    }

    public boolean recordIsExist(ObjectId userId){
        BasicDBObject query=new BasicDBObject("uid",userId);
        int k= count(MongoFacroty.getAppDB(),Constant.COLLECTION_FRIEND_NAME,query);
        return k==0? false:true;
    }
    public void addOneFriend(ObjectId userId, ObjectId friendId) {
        BasicDBObject query=new BasicDBObject("uid",userId);
        BasicDBObject update=new BasicDBObject(Constant.MONGO_PUSH,new BasicDBObject("fid",friendId));
        MongoFacroty.getAppDB().getCollection(Constant.COLLECTION_FRIEND_NAME).update(query,update);
    }

    public void addFriendEntry(ObjectId objectId, List<ObjectId> objectIdList) {
        BasicDBObject basicDBObject=new BasicDBObject("uid",objectId).append("fid",objectIdList);
        save(MongoFacroty.getAppDB(),Constant.COLLECTION_FRIEND_NAME,basicDBObject);
    }
}
