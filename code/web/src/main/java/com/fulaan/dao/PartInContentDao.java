package com.fulaan.dao;

import com.db.base.BaseDao;
import com.fulaan.entry.PartInContentEntry;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moslpc on 2016/11/8.
 */

@Service
public class PartInContentDao extends BaseDao {

  private String getCollection() {
    return Constant.COLLECTION_FORUM_PARTINCONTENT;
  }

  public PartInContentEntry find(ObjectId id){
    BasicDBObject query=new BasicDBObject(Constant.ID,id);
    DBObject dbo = findOne(getDB(), getCollection(), query, Constant.FIELDS);
    if (dbo != null) {
      PartInContentEntry partInContentEntry = new PartInContentEntry(dbo);
      return partInContentEntry;
    }else{
      return null;
    }
  }

  public void pushImage(ObjectId id,String imageUrl){
    BasicDBObject query=new BasicDBObject(Constant.ID,id);
    BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_PUSH,new BasicDBObject("iml",imageUrl));
    update(getDB(),getCollection(),query,updateValue);
  }

  public void pullImage(ObjectId id,String oldImageUrl){
    BasicDBObject query=new BasicDBObject(Constant.ID,id);
    BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_PULL,new BasicDBObject("iml",oldImageUrl));
    update(getDB(),getCollection(),query,updateValue);
  }

  public List<PartInContentEntry> getPartInContent(ObjectId detailId, int type, int page, int pageSize) {
    BasicDBObject query = new BasicDBObject()
            .append("cdid", detailId)
            .append("r", 0);
    if (type != -1) {
      query.append("ty", type);
    }
    BasicDBObject order = new BasicDBObject()
            .append("zan", -1)
            .append(Constant.ID, -1);
    List<DBObject> list = find(getDB(), getCollection(), query, Constant.FIELDS, order, (page - 1) * pageSize, pageSize);
    List<PartInContentEntry> entries = new ArrayList<PartInContentEntry>();
    for (DBObject dbo : list) {
      entries.add(new PartInContentEntry(dbo));
    }
    return entries;
  }

  public int countPartPartInContent(ObjectId detailId) {
    BasicDBObject query = new BasicDBObject()
            .append("cdid", detailId)
            .append("r", 0);
    return count(getDB(), getCollection(), query);
  }

  public void saveParInContent(PartInContentEntry partInContentEntry) {
    save(getDB(), getCollection(), partInContentEntry.getBaseEntry());
  }

  public void setZanToPartInContent(ObjectId partInContentId, ObjectId userId) {
    BasicDBObject query = new BasicDBObject()
            .append(Constant.ID, partInContentId);
    BasicDBObject update = new BasicDBObject()
            .append("$push", new BasicDBObject("zali", userId))
            .append(Constant.MONGO_INC, new BasicDBObject("zan", 1));
    update(getDB(), getCollection(), query, update);
  }

  public void downZanToPartInContent(ObjectId partInContentId, ObjectId userId) {
    BasicDBObject query = new BasicDBObject()
            .append(Constant.ID, partInContentId)
            .append("r", 0);
    BasicDBObject update = new BasicDBObject()
            .append("$pull", new BasicDBObject("zali", userId))
            .append(Constant.MONGO_INC, new BasicDBObject("zan", -1));
    update(getDB(), getCollection(), query, update);
  }

  public boolean isZanToPartInContent(ObjectId partInContentId, ObjectId userId) {
    BasicDBObject query = new BasicDBObject()
            .append(Constant.ID, partInContentId);
    DBObject dbo = findOne(getDB(), getCollection(), query, Constant.FIELDS);
    if (dbo != null) {
      PartInContentEntry partInContentEntry = new PartInContentEntry(dbo);
      List<ObjectId> list = partInContentEntry.getZanList();
      if (list.contains(userId)) {
        return true;
      }
    }
    return false;
  }

  public PartInContentEntry getPartInContent(ObjectId detailId, ObjectId userId) {
    BasicDBObject query = new BasicDBObject()
            .append("cdid", detailId)
            .append("uid", userId)
            .append("ty",2)
            .append("r", 0);
    DBObject dbo = findOne(getDB(), getCollection(), query);
    if (dbo != null) {
      return new PartInContentEntry(dbo);
    }
    return null;
  }


  /**
   * 用户退出社区时对他所参与的该社区的内容置为废弃，即r=1加入时r=0
   *
   * @param communityId
   * @param userId
   * @param remove
   */
  public void setPartIncontentStatus(ObjectId communityId, ObjectId userId, int remove) {
    BasicDBObject query = new BasicDBObject()
            .append("cmid", communityId)
            .append("uid", userId);
    BasicDBObject updateValue = new BasicDBObject()
            .append("r", remove);
    BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, updateValue);
    update(getDB(), getCollection(), query, update);
  }
}
