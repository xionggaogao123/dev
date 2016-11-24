package com.fulaan.dao;

import com.db.base.BaseDao;
import com.fulaan.entry.CommunitySeqEntry;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sys.constants.Constant;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by jerry on 2016/10/26.
 * CommunitySeqDao
 */
@Service
public class CommunitySeqDao extends BaseDao {

  private String getCollection() {
    return Constant.COLLECTION_FORUM_COMMUNITY_SEQ;
  }

  public void save(CommunitySeqEntry entry) {
    save(getDB(), getCollection(), entry.getBaseEntry());
  }

  public CommunitySeqEntry getRandom() {
    BasicDBObject query = new BasicDBObject()
            .append("ty", 1)
            .append("r", 0)
            .append("ran", new BasicDBObject(Constant.MONGO_GTE, Math.random()));
    BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("r", 1));
    DBObject dbo = findAndModifed(getDB(), getCollection(), query, update);
    return new CommunitySeqEntry(dbo);
  }

  private int count(BasicDBObject query) {
    return count(getDB(), getCollection(), query);
  }

}
