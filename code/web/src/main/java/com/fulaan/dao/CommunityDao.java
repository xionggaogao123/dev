package com.fulaan.dao;

import com.db.base.BaseDao;
import com.fulaan.entry.CommunityEntry;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jerry on 2016/10/24.
 * 社区Entry Dao层
 */
@Service
public class CommunityDao extends BaseDao {

  private String getCollection() {
    return Constant.COLLECTION_FORUM_COMMUNITY;
  }

  /**
   * 保存社区
   *
   * @param entry
   * @return 返回保存是否成功
   */
  public void save(CommunityEntry entry) {
    save(getDB(), getCollection(), entry.getBaseEntry());
  }

  /**
   * 返回社区Entry
   *
   * @param id
   * @return
   */
  public CommunityEntry findByObjectId(ObjectId id) {
    BasicDBObject query = new BasicDBObject()
            .append(Constant.ID, id);
    DBObject dbObject = findOne(getDB(), getCollection(), query);
    if (null != dbObject) {
      return new CommunityEntry(dbObject);
    }
    return null;
  }

  /**
   * 返回社区Entry
   *
   * @param searchId 搜索id
   * @return
   */
  public CommunityEntry findBySearchId(String searchId) {
    BasicDBObject query = new BasicDBObject()
            .append("cmid", searchId);
    DBObject dbObject = findOne(getDB(), getCollection(), query);
    if (null != dbObject) {
      return new CommunityEntry(dbObject);
    }
    return null;
  }

  /**
   * 返回社区Entry
   *
   * @param groupId 环信id
   * @return
   */
  public CommunityEntry findByGroupId(String emChatId) {
    BasicDBObject query = new BasicDBObject()
            .append("emid", emChatId);
    DBObject dbObject = findOne(getDB(), getCollection(), query);
    if (null != dbObject) {
      return new CommunityEntry(dbObject);
    }
    return null;
  }

  /**
   * 名称精确查找
   *
   * @param communityName
   * @return
   */
  public CommunityEntry findByName(String communityName) {
    BasicDBObject query = new BasicDBObject()
            .append("cmmn", communityName);
    DBObject dbObject = findOne(getDB(), getCollection(), query);
    if (null != dbObject) {
      return new CommunityEntry(dbObject);
    }
    return null;
  }

  /**
   * 社团名称:正则查找
   *
   * @param regular
   * @return
   */
  public List<CommunityEntry> findByRegularName(String regular) {
    BasicDBObject query = new BasicDBObject()
            .append("cmmn", MongoUtils.buildRegular(regular));
    List<DBObject> dbObjects = find(getDB(), getCollection(), query);
    List<CommunityEntry> communitys = new ArrayList<CommunityEntry>();
    for (DBObject dbo : dbObjects) {
      communitys.add(new CommunityEntry(dbo));
    }
    return communitys;
  }

  /**
   * 获取社区
   *
   * @param commIds
   * @return
   */
  public List<CommunityEntry> getCommunitysByIds(List<ObjectId> communityIds) {
    List<CommunityEntry> communityEntries = new ArrayList<CommunityEntry>();
    BasicDBObject query = new BasicDBObject()
            .append(Constant.ID, new BasicDBObject(Constant.MONGO_IN, communityIds));
    List<DBObject> dbObjects = find(getDB(), getCollection(), query, Constant.FIELDS);
    for (DBObject dbo : dbObjects) {
      communityEntries.add(new CommunityEntry(dbo));
    }
    return communityEntries;
  }

  /**
   * 获取用户创建的所有CommunityEntry
   *
   * @param uid
   * @return
   */
  public List<CommunityEntry> getOwerCommunitys(ObjectId userId) {
    List<CommunityEntry> list = new ArrayList<CommunityEntry>();
    BasicDBObject query = new BasicDBObject()
            .append("cmow", userId);
    List<DBObject> dbObjects = find(getDB(), getCollection(), query);
    for (DBObject dbo : dbObjects) {
      list.add(new CommunityEntry(dbo));
    }
    return list;
  }

  /**
   * 获取 - 公开群
   *
   * @return
   */
  public List<CommunityEntry> getOpenCommunitys() {
    List<CommunityEntry> list = new ArrayList<CommunityEntry>();
    BasicDBObject query = new BasicDBObject()
            .append("op", 1);
    List<DBObject> dbObjects = find(getDB(), getCollection(), query);
    for (DBObject dbo : dbObjects) {
      list.add(new CommunityEntry(dbo));
    }
    return list;
  }

  /**
   * 更改 - 社区名称
   *
   * @param cid
   * @param name
   */
  public void updateCommunityName(ObjectId communityId, String name) {
    BasicDBObject query = new BasicDBObject()
            .append(Constant.ID, communityId);
    BasicDBObject update = new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("cmmn", name));
    update(getDB(), getCollection(), query, update);
  }

  /**
   * 更改 -社区公开状态
   * @param cid
   * @param open
   */
  public void updateCommunityOpen(ObjectId communityId, int open) {
    BasicDBObject query = new BasicDBObject()
            .append(Constant.ID, communityId);
    BasicDBObject update = new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("op",open));
    update(getDB(), getCollection(), query, update);
  }


  /**
   * 更改 - 社区描述
   *
   * @param cid
   * @param desc
   */
  public void updateCommunityDesc(ObjectId cid, String desc) {
    BasicDBObject query = new BasicDBObject()
            .append(Constant.ID, cid);
    BasicDBObject update = new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("cmde", desc));
    update(getDB(), getCollection(), query, update);
  }

  /**
   * 更改logo
   *
   * @param cid
   * @param logo
   */
  public void updateCommunityLogo(ObjectId communityId, String logo) {
    BasicDBObject query = new BasicDBObject()
            .append(Constant.ID, communityId);
    BasicDBObject update = new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("cmlg", logo));
    update(getDB(), getCollection(), query, update);
  }


  /**
   * 删除 - 社区
   *
   * @param cid
   */
  public void deleteCommunity(ObjectId communityId) {
    BasicDBObject query = new BasicDBObject()
            .append(Constant.ID, communityId);
    remove(getDB(), getCollection(), query);
  }

  /**
   * 获取 - 社区的群组 id
   *
   * @param communityId
   * @return
   */
  public ObjectId getGroupId(ObjectId communityId) {
    BasicDBObject query = new BasicDBObject()
            .append(Constant.ID, communityId);
    BasicDBObject field = new BasicDBObject()
            .append("grid", 1);
    DBObject dbo = findOne(getDB(), getCollection(), query, field);
    if (dbo != null) {
      return (ObjectId) dbo.get("grid");
    }
    return null;
  }


  public Boolean judgeCommunity(String communityName){
    DBObject dbo=getDBObject(communityName);
    if (dbo != null) {
      return true;
    }
    return false;
  }

  public DBObject getDBObject(String communityName){
    BasicDBObject query =new BasicDBObject()
            .append("cmmn",communityName);
    return findOne(getDB(), getCollection(), query, Constant.FIELDS);
  }

  public CommunityEntry getDefaultEntry(String communityName){
    DBObject dbo=getDBObject(communityName);
    if (dbo != null) {
      return new CommunityEntry((BasicDBObject)dbo);
    }
    return null;
  }

  public boolean judgeCommunityName(String communityName,ObjectId id){
    BasicDBObject query =new BasicDBObject()
            .append(Constant.ID,id)
            .append("cmmn",communityName);
    int count=count(getDB(),getCollection(),query);
    return count==1;
  }


  public int getCommunityCountByOwerId(ObjectId userId) {
    BasicDBObject query =new BasicDBObject()
            .append("cmow",userId);
    return count(getDB(),getCollection(),query);
  }

  public void updateCommunityQrUrl(ObjectId communtiyId,String qrUrl) {
    BasicDBObject query =new BasicDBObject()
            .append(Constant.ID,communtiyId);
    BasicDBObject update = new BasicDBObject()
            .append(Constant.MONGO_SET,new BasicDBObject("cmco",qrUrl));
    update(getDB(),getCollection(),query,update);
  }

  public List<CommunityEntry> findAll() {
    BasicDBObject query = new BasicDBObject();
    List<DBObject> dbos = find(getDB(),getCollection(),query,Constant.FIELDS);
    List<CommunityEntry> communitys = new ArrayList<CommunityEntry>();
    for(DBObject dbo: dbos) {
      communitys.add(new CommunityEntry(dbo));
    }
    return communitys;
  }

  public void resetLogo(String communityId, String logo) {
    BasicDBObject query = new BasicDBObject(Constant.ID,new ObjectId(communityId));
    BasicDBObject update = new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("cmlg",logo));
    update(getDB(),getCollection(),query,update);
  }
}
