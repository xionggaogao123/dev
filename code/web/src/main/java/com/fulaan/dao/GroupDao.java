package com.fulaan.dao;

import com.db.base.BaseDao;
import com.fulaan.entry.GroupEntry;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moslpc on 2016/11/1.
 * 讨论组 Dao层
 */
@Service
public class GroupDao extends BaseDao{

  private String getCollection(){
    return Constant.COLLECTION_FORUM_COMMUNITY_GROUP;
  }

  public void save(GroupEntry entry){
    save(getDB(),getCollection(),entry.getBaseEntry());
  }

  /**
   * 获得 GroupEntry
   *
   * @param _id
   * @return
   */
  public GroupEntry findByObjectId(ObjectId groupId){
    BasicDBObject query = new BasicDBObject()
            .append(Constant.ID,groupId);
    DBObject dbo = findOne(getDB(),getCollection(),query,Constant.FIELDS);
    if(dbo != null){
      return new GroupEntry(dbo);
    }
    return null;
  }

  /**
   * 获得 List
   * @param ids
   * @return
   */
  public List<GroupEntry> findByIdList(List<ObjectId> ids){
    BasicDBObject query = new BasicDBObject()
            .append(Constant.ID,new BasicDBObject("$in",ids));
    List<DBObject> dbos = find(getDB(),getCollection(),query);
    List<GroupEntry> groupEntrys = new ArrayList<GroupEntry>();
    for(DBObject dbo:dbos){
      groupEntrys.add(new GroupEntry(dbo));
    }
    return groupEntrys;
  }

  /**
   * 根据环信id得到 群聊id
   * @param emChatId
   * @return
   */
  public ObjectId getGroupIdByEmchatId(String emChatId){
    BasicDBObject query = new BasicDBObject()
            .append("grcd",emChatId);
    BasicDBObject field = new BasicDBObject()
            .append(Constant.ID,1);
    DBObject dbo = findOne(getDB(),getCollection(),query,field);
    if(dbo == null){
      return null;
    }
    return (ObjectId)dbo.get("_id");
  }

  /**
   * 获取群聊头像
   * @param groupId
   * @return
   */
  public String getHeadImage(ObjectId groupId) {
    BasicDBObject query = new BasicDBObject()
            .append(Constant.ID,groupId);
    BasicDBObject field = new BasicDBObject()
            .append("himg",1);
    DBObject dbo = findOne(getDB(),getCollection(),query,field);
    if(dbo != null){
      return (String)dbo.get("himg");
    }
    return "";
  }

  /**
   * 设置群聊头像
   * @param groupId
   * @param url
   */
  public void setHeadImage(ObjectId groupId,String url){
    BasicDBObject query = new BasicDBObject()
            .append(Constant.ID,groupId);
    BasicDBObject update = new BasicDBObject()
            .append(Constant.MONGO_SET,new BasicDBObject("himg",url));
    update(getDB(),getCollection(),query,update);
  }

  /**
   * 更新群聊名称
   *
   * @param _id
   * @param name
   */
  public void updateGroupName(ObjectId groupId, String name) {
    BasicDBObject query = new BasicDBObject()
            .append(Constant.ID,groupId);
    BasicDBObject update = new BasicDBObject()
            .append(Constant.MONGO_SET,new BasicDBObject("grnm",name));
    update(getDB(),getCollection(),query,update);
  }

  /**
   * 获取群聊名称
   * @param groupId
   * @return
   */
  public String getGroupName(ObjectId groupId) {
    BasicDBObject query = new BasicDBObject()
            .append(Constant.ID,groupId);
    BasicDBObject field = new BasicDBObject()
            .append("grnm",1);
    DBObject dbo = findOne(getDB(),getCollection(),query,field);
    if(dbo != null){
      return (String)dbo.get("grnm");
    }
    return "";
  }

  public String getEmchatIdByGroupId(ObjectId groupId) {
    BasicDBObject query = new BasicDBObject()
            .append(Constant.ID,groupId);
    BasicDBObject field = new BasicDBObject()
            .append("grcd",1);
    DBObject dbo = findOne(getDB(),getCollection(),query,field);
    if(dbo != null){
      return (String) dbo.get("grcd");
    }
    return null;
  }

  public void setOwerId(ObjectId groupId,ObjectId owerId){
    BasicDBObject query = new BasicDBObject()
            .append(Constant.ID,groupId);
    BasicDBObject update = new BasicDBObject()
            .append(Constant.MONGO_SET,new BasicDBObject("grow",owerId));
    update(getDB(),getCollection(),query,update);
  }

  public void delete(ObjectId groupId){
    BasicDBObject query = new BasicDBObject()
            .append(Constant.ID,groupId);
    remove(getDB(),getCollection(),query);
  }

  /**
   * 根据群组id 获取 社区id
   * @param groupId
   * @return
   */
  public ObjectId getCommunityIdByGroupId(ObjectId groupId) {
    BasicDBObject query = new BasicDBObject()
            .append(Constant.ID,groupId);
    BasicDBObject field = new BasicDBObject()
            .append("cmid",1);
    DBObject dbo = findOne(getDB(),getCollection(),query,field);
    return (ObjectId)dbo.get("cmid");
  }

  /**
   * 更新是否更改群聊名称
   * @param groupId
   * @param i
   */
  public void updateIsM(ObjectId groupId,int i) {
    BasicDBObject query = new BasicDBObject()
            .append(Constant.ID,groupId);
    BasicDBObject update = new BasicDBObject()
            .append(Constant.MONGO_SET,new BasicDBObject("ism",i));
    update(getDB(),getCollection(),query,update);
  }
}
