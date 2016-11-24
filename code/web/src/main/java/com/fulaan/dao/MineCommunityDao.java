package com.fulaan.dao;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.fcommunity.MineCommunityEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jerry on 2016/11/17.
 * 我的社区
 * uid： 用户id
 * cmid： 社区id
 */
public class MineCommunityDao extends BaseDao {

  public void save(MineCommunityEntry entry) {
    save(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MINE_COMMUNITY, entry.getBaseEntry());
  }

  public List<MineCommunityEntry> findByPage(ObjectId userId, int page, int pageSize) {
    BasicDBObject query = new BasicDBObject().append("uid", userId);
    BasicDBObject orderBy = new BasicDBObject().append("prio", -1).append(Constant.ID, -1);
    List<DBObject> dbos = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MINE_COMMUNITY, query, Constant.FIELDS, orderBy, (page - 1) * pageSize, pageSize);
    List<MineCommunityEntry> mineCommunityEntries = new ArrayList<MineCommunityEntry>();
    for (DBObject dbo : dbos) {
      mineCommunityEntries.add(new MineCommunityEntry(dbo));
    }
    return mineCommunityEntries;
  }



  public List<MineCommunityEntry> findAll(ObjectId userId,int page,int pageSize){

    BasicDBObject query = new BasicDBObject()
            .append("uid", userId);
    BasicDBObject orderBy = new BasicDBObject()
              .append("prio", -1)
              .append("ti", -1);
    List<DBObject> dbos;
    if(page!=-1) {
      dbos = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MINE_COMMUNITY, query, Constant.FIELDS, orderBy,(page-1)*pageSize,pageSize);
    }else{
      dbos = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MINE_COMMUNITY, query, Constant.FIELDS, orderBy);
    }
    List<MineCommunityEntry> mineCommunityEntries = new ArrayList<MineCommunityEntry>();
    for (DBObject dbo : dbos) {
      mineCommunityEntries.add(new MineCommunityEntry(dbo));
    }
    return mineCommunityEntries;
  }

  public int count(ObjectId userId) {
    BasicDBObject query = new BasicDBObject().append("uid", userId);
    return count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MINE_COMMUNITY, query);
  }


  public void delete(ObjectId communityId, ObjectId userId) {
    BasicDBObject query = new BasicDBObject().append("uid", userId).append("cmid", communityId);
    remove(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MINE_COMMUNITY, query);
  }

  public List<MineCommunityEntry> findByCount(ObjectId userId, int count) {
    BasicDBObject query = new BasicDBObject().append("uid", userId);
    BasicDBObject orderBy = new BasicDBObject().append(Constant.ID, -1);
    List<DBObject> dbos = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MINE_COMMUNITY, query, Constant.FIELDS, orderBy, 0, count);
    List<MineCommunityEntry> mineCommunityEntries = new ArrayList<MineCommunityEntry>();
    for (DBObject dbo : dbos) {
      mineCommunityEntries.add(new MineCommunityEntry(dbo));
    }
    return mineCommunityEntries;
  }

  public void resetMineCommunitys(ObjectId userId,ObjectId communityId,int priory) {
    BasicDBObject query = new BasicDBObject("uid",userId).append("cmid",communityId);
    BasicDBObject update = new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("prio",priory));
    update(MongoFacroty.getAppDB(),Constant.COLLECTION_FORUM_MINE_COMMUNITY,query,update);
  }
}
