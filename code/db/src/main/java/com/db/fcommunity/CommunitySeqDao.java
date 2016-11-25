package com.db.fcommunity;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.fcommunity.CommunitySeqEntry;
import com.sys.constants.Constant;

/**
 * Created by jerry on 2016/10/26.
 * CommunitySeqDao
 */
public class CommunitySeqDao extends BaseDao {

    private String getCollection() {
        return Constant.COLLECTION_FORUM_COMMUNITY_SEQ;
    }

    public void save(CommunitySeqEntry entry) {
        save(MongoFacroty.getAppDB(), getCollection(), entry.getBaseEntry());
    }

    public CommunitySeqEntry getRandom() {
        BasicDBObject query = new BasicDBObject()
                .append("ty", 1)
                .append("r", 0)
                .append("ran", new BasicDBObject(Constant.MONGO_GTE, Math.random()));
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("r", 1));
        DBObject dbo = findAndModifed(MongoFacroty.getAppDB(), getCollection(), query, update);
        return new CommunitySeqEntry(dbo);
    }

    private int count(BasicDBObject query) {
        return count(MongoFacroty.getAppDB(), getCollection(), query);
    }

}
