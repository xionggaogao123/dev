package com.db.activity;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.activity.FriendApplyEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yan on 2015/3/3.
 * index:uid_rid_ac
 * {"uid":1,"rid":1,"ac":1}
 */
public class FriendApplyDao extends BaseDao {

  public int countNoResponseReply(ObjectId objectId) {
    BasicDBObject query = new BasicDBObject("rid", objectId).append("ac", 0);
    int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_FRIEND_APPLY_NAME, query);
    return count;
  }

  public void insertApply(FriendApplyEntry friendApplyEntry) {
    friendApplyEntry.setAccepted(0);
    save(MongoFacroty.getAppDB(), Constant.COLLECTION_FRIEND_APPLY_NAME, friendApplyEntry.getBaseEntry());
  }

  /*
  * 发出的申请
  *
  * */
  public List<FriendApplyEntry> findFriendApplyList(ObjectId objectId) {
    BasicDBObject basicDBObject = new BasicDBObject("uid", objectId).append("ac", 0);
    List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FRIEND_APPLY_NAME, basicDBObject, Constant.FIELDS);

    List<FriendApplyEntry> friendApplyEntries = new ArrayList<FriendApplyEntry>();
    for (DBObject dbObject : dbObjects) {
      FriendApplyEntry friendApplyEntry = new FriendApplyEntry((BasicDBObject) dbObject);
      friendApplyEntries.add(friendApplyEntry);
    }
    return friendApplyEntries;
  }

  /*
  *
  * 发出的申请 带分页功能
  *
  * */
  public List<FriendApplyEntry> findFriendApplyList(ObjectId objectId, Integer skip, Integer size) {
    BasicDBObject basicDBObject = new BasicDBObject("rid", objectId).append("ac", 0);//此处将uid改为rid
    List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FRIEND_APPLY_NAME, basicDBObject, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, skip, size);
    List<FriendApplyEntry> friendApplyEntries = new ArrayList<FriendApplyEntry>();
    for (DBObject dbObject : dbObjects) {
      FriendApplyEntry friendApplyEntry = new FriendApplyEntry((BasicDBObject) dbObject);
      friendApplyEntries.add(friendApplyEntry);
    }
    return friendApplyEntries;
  }

  /**
   * 不带分页功能（查看多少人申请好友）
   *
   * @param objectId
   * @return
   */
  public List<FriendApplyEntry> findFriendApplyListByCondition(ObjectId objectId,int accepted) {
    BasicDBObject basicDBObject = new BasicDBObject("rid", objectId)
            .append("ac",accepted);//此处将uid改为rid
    List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FRIEND_APPLY_NAME, basicDBObject, Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
    List<FriendApplyEntry> friendApplyEntries = new ArrayList<FriendApplyEntry>();
    for (DBObject dbObject : dbObjects) {
      FriendApplyEntry friendApplyEntry = new FriendApplyEntry((BasicDBObject) dbObject);
      friendApplyEntries.add(friendApplyEntry);
    }
    return friendApplyEntries;
  }

  /**
   * 带分页功能的好友申请列表
   * @param objectId
   * @param accepted
   * @param page
   * @param pageSize
   * @return
   */
  public List<FriendApplyEntry> findFriendApplys(ObjectId objectId,int accepted,int page,int pageSize) {
    BasicDBObject basicDBObject = new BasicDBObject("rid", objectId)
            .append("ac",accepted);//此处将uid改为rid
    List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FRIEND_APPLY_NAME, basicDBObject, Constant.FIELDS, Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
    List<FriendApplyEntry> friendApplyEntries = new ArrayList<FriendApplyEntry>();
    for (DBObject dbObject : dbObjects) {
      FriendApplyEntry friendApplyEntry = new FriendApplyEntry((BasicDBObject) dbObject);
      friendApplyEntries.add(friendApplyEntry);
    }
    return friendApplyEntries;
  }


  public FriendApplyEntry findFriendApplyById(ObjectId objectId) {
    BasicDBObject query = new BasicDBObject(Constant.ID, objectId);
    DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FRIEND_APPLY_NAME, query, Constant.FIELDS);
    if(dbObject == null) return null;
    return new FriendApplyEntry((BasicDBObject) dbObject);
  }

  public void acceptApply(ObjectId objectId) {
    BasicDBObject query = new BasicDBObject(Constant.ID, objectId);
    BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ac", 1));
    update(MongoFacroty.getAppDB(), Constant.COLLECTION_FRIEND_APPLY_NAME, query, update);
  }

  public void refuseApply(ObjectId objectId) {
    BasicDBObject query = new BasicDBObject(Constant.ID, objectId);
    BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ac", 2));
    update(MongoFacroty.getAppDB(), Constant.COLLECTION_FRIEND_APPLY_NAME, query, update);
  }

  public List<FriendApplyEntry> findApplyBySponsorIdAndRespondentId(ObjectId sponsorId, ObjectId respondentId) {
    BasicDBObject basicDBObject = new BasicDBObject("uid", sponsorId).append("rid", respondentId).append("ac", 0);
    List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FRIEND_APPLY_NAME, basicDBObject, Constant.FIELDS);

    List<FriendApplyEntry> friendApplyEntries = new ArrayList<FriendApplyEntry>();
    for (DBObject dbObject : dbObjects) {
      FriendApplyEntry friendApplyEntry = new FriendApplyEntry((BasicDBObject) dbObject);
      friendApplyEntries.add(friendApplyEntry);
    }
    return friendApplyEntries;
  }

  public void updateApplyByIds(List<ObjectId> applyIds) {
    BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, applyIds));
    BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ac", 1));
    update(MongoFacroty.getAppDB(), Constant.COLLECTION_FRIEND_APPLY_NAME, query, update);
  }

  /**
   * 通过发起人Id以及回复列表人Id查找所有可接受用户
   * @param sponsorId
   * @param respondentIds
   * @return
   */
  public Map<ObjectId,FriendApplyEntry> getFriendApplyMap(ObjectId sponsorId, List<ObjectId> respondentIds){
    Map<ObjectId,FriendApplyEntry> retList=new HashMap<ObjectId, FriendApplyEntry>();
    BasicDBObject basicDBObject = new BasicDBObject("uid", sponsorId).append("rid", new BasicDBObject(Constant.MONGO_IN, respondentIds)).append("ac", 0);
    List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FRIEND_APPLY_NAME, basicDBObject, Constant.FIELDS);

    for (DBObject dbObject : dbObjects) {
      FriendApplyEntry friendApplyEntry = new FriendApplyEntry((BasicDBObject) dbObject);
      retList.put(friendApplyEntry.getRespondent(),friendApplyEntry);
    }
    return retList;
  }

  public void deleteApplyById(ObjectId applyId){
    BasicDBObject query = new BasicDBObject(Constant.ID,applyId);
    remove(MongoFacroty.getAppDB(),Constant.COLLECTION_FRIEND_APPLY_NAME,query);
  }
  
  /**
   * 带分页功能（查看多少人申请好友）
   *
   * @param objectId
   * @return
   */
  public List<FriendApplyEntry> newAllFriends(ObjectId objectId,int page,int pageSize) {
    BasicDBObject basicDBObject = new BasicDBObject("rid", objectId);//此处将uid改为rid
    BasicDBObject timeDESC = new BasicDBObject("ad", -1);
    List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FRIEND_APPLY_NAME, basicDBObject, Constant.FIELDS, timeDESC,(page - 1) * pageSize, pageSize);
    List<FriendApplyEntry> friendApplyEntries = new ArrayList<FriendApplyEntry>();
    for (DBObject dbObject : dbObjects) {
      FriendApplyEntry friendApplyEntry = new FriendApplyEntry((BasicDBObject) dbObject);
      friendApplyEntries.add(friendApplyEntry);
    }
    return friendApplyEntries;
  }
  
  
  public List<FriendApplyEntry> getFriNum(ObjectId sponsorId) {
	    BasicDBObject basicDBObject = new BasicDBObject("rid", sponsorId).append("ac", 0);
	    List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FRIEND_APPLY_NAME, basicDBObject, Constant.FIELDS);

	    List<FriendApplyEntry> friendApplyEntries = new ArrayList<FriendApplyEntry>();
	    for (DBObject dbObject : dbObjects) {
	      FriendApplyEntry friendApplyEntry = new FriendApplyEntry((BasicDBObject) dbObject);
	      friendApplyEntries.add(friendApplyEntry);
	    }
	    return friendApplyEntries;
	  }
  
}
