package com.db.fcommunity;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.fcommunity.CommunityDetailEntry;
import com.pojo.fcommunity.CommunityHyEntry;
import com.sys.constants.Constant;

public class CommunityHyDao extends BaseDao {

    public ObjectId save(CommunityHyEntry detailEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_HY, detailEntry.getBaseEntry());
        return detailEntry.getID();
    }
    
    public int communityHyCount(long startTime, long endTime) {
        BasicDBObject query = new BasicDBObject();
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject("ti",new BasicDBObject(Constant.MONGO_GTE, startTime)));
        values.add(new BasicDBObject("ti", new BasicDBObject(Constant.MONGO_LT, endTime)));
        query.put(Constant.MONGO_AND, values);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_HY, query);
    }
    
    public List<CommunityHyEntry> communityHyCountEntry(List<ObjectId> communityIds, Long startTime, Long endTime) {
        List<CommunityHyEntry> detailEntries = new ArrayList<CommunityHyEntry>();
        BasicDBObject query = new BasicDBObject();
        BasicDBObject query1 = new BasicDBObject();
        BasicDBObject query2 = new BasicDBObject();
        query1.append("cmid",new BasicDBObject(Constant.MONGO_IN, communityIds));
        BasicDBList values = new BasicDBList();
        if (startTime != null && startTime != 0l) {
            query1.append("ti", new BasicDBObject(Constant.MONGO_GTE, startTime));
        }
        if (endTime != null && endTime != 0l) {
            query2.append("ti", new BasicDBObject(Constant.MONGO_LT, endTime));
        }
        values.add(query1);
        values.add(query2);
        query.put(Constant.MONGO_AND, values);
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_HY, query);
        for (DBObject dbo : dbObjects) {
            detailEntries.add(new CommunityHyEntry(dbo));
        }
        return detailEntries;
    }
    
    
}
