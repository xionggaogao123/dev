package com.fulaan.dao;


import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.fulaan.entry.RemarkEntry;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/11/1.
 */
@Service
public class RemarkDao extends BaseDao {

  private String getCollection() {
    return Constant.COLLECTION_FORUM_REMARK;
  }

  public void save(RemarkEntry entry) {
    save(getDB(), getCollection(), entry.getBaseEntry());
  }

  public void update(ObjectId id, String remark) {
    BasicDBObject query = new BasicDBObject()
            .append(Constant.ID, id);
    BasicDBObject update = new BasicDBObject()
            .append(Constant.MONGO_SET, new BasicDBObject("rm", remark));
    update(getDB(), getCollection(), query, update);
  }

  /**
   * 查询出被修改备注的名称
   *
   * @param startUserId
   * @param endUserIds
   * @return
   */
  public Map<ObjectId, RemarkEntry> find(ObjectId startUserId, List<ObjectId> endUserIds) {
    Map<ObjectId, RemarkEntry> retMap = new HashMap<ObjectId, RemarkEntry>();
    BasicDBObject query = new BasicDBObject()
            .append("suid", startUserId)
            .append("euid", new BasicDBObject(Constant.MONGO_IN, endUserIds));
    List<DBObject> list = find(getDB(), getCollection(), query, Constant.FIELDS);
    if (null != list && !list.isEmpty()) {
      RemarkEntry e;
      for (DBObject dbo : list) {
        e = new RemarkEntry((BasicDBObject) dbo);
        retMap.put(e.getEndUserId(), e);
      }
    }
    return retMap;
  }


}
