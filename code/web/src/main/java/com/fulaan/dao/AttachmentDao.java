package com.fulaan.dao;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.fulaan.entry.AttachmentEntry;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/10/25.
 */
@Service
public class AttachmentDao extends BaseDao {

  /**
   * 添加附件信息
   *
   * @param e
   * @return
   */
  public ObjectId addAttachment(AttachmentEntry e) {
    save(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_ATTACHMENT, e.getBaseEntry());
    return e.getID();
  }

  /**
   * 根据id查询
   *
   * @param id
   * @return
   */
  public AttachmentEntry getAttachmentById(ObjectId id) {
    DBObject query = new BasicDBObject(Constant.ID, id);
    DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_ATTACHMENT, query, Constant.FIELDS);
    if (null != dbo) {
      return new AttachmentEntry((BasicDBObject) dbo);
    }
    return null;
  }

  /**
   * 逻辑删除附件
   *
   * @param id
   */
  public void removeLogicAttach(ObjectId id) {
    DBObject query = new BasicDBObject(Constant.ID, id);
    BasicDBObject updateValue = new BasicDBObject("r", 1);
    update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_ATTACHMENT, query, updateValue);
  }

  /**
   * 根据ID集合查询，并且返回map形式(主要用的)
   *
   * @param col
   * @param fields
   * @return
   */
  public Map<ObjectId, AttachmentEntry> getVideoEntryMap(Collection<ObjectId> col, DBObject fields) {
    Map<ObjectId, AttachmentEntry> map = new HashMap<ObjectId, AttachmentEntry>();
    DBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, col)).append("r", 0);
    List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_ATTACHMENT, query, fields);
    if (null != list && !list.isEmpty()) {
      AttachmentEntry e;
      for (DBObject dbo : list) {
        e = new AttachmentEntry((BasicDBObject) dbo);
        map.put(e.getID(), e);
      }
    }
    return map;
  }


}
