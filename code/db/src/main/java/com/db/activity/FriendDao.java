package com.db.activity;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.pojo.activity.FriendEntry;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jerry on 2015/3/3.
 * 更改:   区分: 好友，伙伴
 *
 */
public class FriendDao extends BaseDao {

  /**
   * 普通好友个数
   *
   * @param objectId
   * @return
   */
  public int countFriend(ObjectId objectId) {
    FriendEntry friendEntry = get(objectId);
    if (null != friendEntry) {
      return friendEntry.getFriendIds().size();
    }
    return Constant.ZERO;
  }

  /**
   * 伙伴个数
   *
   * @param objectId
   * @return
   */
  public int countIdfFriend(ObjectId objectId) {
    FriendEntry friendEntry = get(objectId);
    if (null != friendEntry) {
      return friendEntry.getPartnerIds().size();
    }
    return Constant.ZERO;
  }

  /**
   * 获取普通好友
   *
   * @param objectId
   * @return
   */
  public List<ObjectId> findMyFriendIds(ObjectId objectId) {
    FriendEntry friendEntry = get(objectId);
    if (null != friendEntry) {
      return friendEntry.getFriendIds();
    }
    return new ArrayList<ObjectId>();
  }

  /**
   * 获取认证好友
   *
   * @param objectId
   * @return
   */
  public List<ObjectId> findMyIdfFriendIds(ObjectId objectId) {
    FriendEntry friendEntry = get(objectId);
    if (null != friendEntry) {
      return friendEntry.getPartnerIds();
    }
    return new ArrayList<ObjectId>();
  }

  public FriendEntry get(ObjectId id) {
    BasicDBObject query = new BasicDBObject("uid", id);
    DBObject dbObject = findOne(getDB(), getCollection(), query, Constant.FIELDS);
    if (dbObject == null) return null;
    return new FriendEntry((BasicDBObject) dbObject);
  }

  public boolean isFriend(ObjectId u1, ObjectId u2) {
    BasicDBObject query = new BasicDBObject("uid", u1).append("fid", u2);
    DBObject dbObject = findOne(getDB(), getCollection(), query, Constant.FIELDS);
    return dbObject != null;
  }


  public void deleteOneFriend(ObjectId objectId, ObjectId objectId1) {
    BasicDBObject query = new BasicDBObject("uid", objectId);
    BasicDBObject update = new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("fid", objectId1));
    update(getDB(), getCollection(), query, update);
  }

  public boolean recordIsExist(ObjectId userId) {
    BasicDBObject query = new BasicDBObject("uid", userId);
    int k = count(getDB(), getCollection(), query);
    return k != 0;
  }

  public void addOneFriend(ObjectId userId, ObjectId friendId) {
    BasicDBObject query = new BasicDBObject("uid", userId);
    BasicDBObject update = new BasicDBObject(Constant.MONGO_PUSH, new BasicDBObject("fid", friendId));
    update(getDB(), getCollection(), query, update);
  }

  public void addFriendEntry(ObjectId objectId, List<ObjectId> objectIdList) {
    BasicDBObject basicDBObject = new BasicDBObject("uid", objectId).append("fid", objectIdList);
    save(getDB(), getCollection(), basicDBObject);
  }

  private String getCollection() {
    return Constant.COLLECTION_FRIEND_NAME;
  }

  /*
  * 根据学校推荐好友
  * */
  public List<UserEntry> recommendFriendBySchool(ObjectId objectId, List<ObjectId> objectIdList, Integer begin, Integer pageSize) {
    BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_NOTIN, objectIdList)).append("si", objectId).append("r", UserRole.STUDENT.getRole()).append("ir", 0);
    List<DBObject> dbObjectList = find(getDB(), Constant.COLLECTION_USER_NAME, query, new BasicDBObject("nm", 1).append("nnm", 1).append("avt", 1), Constant.MONGO_SORTBY_DESC, begin, pageSize);
    List<UserEntry> userEntryList = new ArrayList<UserEntry>();
    for (DBObject dbObject : dbObjectList) {
      UserEntry userEntry = new UserEntry((BasicDBObject) dbObject);
      userEntryList.add(userEntry);
    }
    return userEntryList;
  }

  public int recommendFriendBySchoolCount(ObjectId objectId, List<ObjectId> objectIdList) {
    BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_NOTIN, objectIdList)).append("si", objectId).append("r", UserRole.STUDENT.getRole()).append("ir", 0);
    return count(getDB(), Constant.COLLECTION_USER_NAME, query);
  }


}
