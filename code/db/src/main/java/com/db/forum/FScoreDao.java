package com.db.forum;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.forum.FScoreEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/8/16.
 */
public class FScoreDao extends BaseDao {

    public ObjectId addFScore(FScoreEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_SCORE, e.getBaseEntry());
        return e.getID();
    }

    public List<FScoreEntry> getFScore(ObjectId personId) {
        List<FScoreEntry> retList = new ArrayList<FScoreEntry>();
        BasicDBObject query = new BasicDBObject();
        if (null != personId) {
            query.append("pid", personId);
        }
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_SCORE, query, null);
        for (DBObject dbObject : dbObjectList) {
            retList.add(new FScoreEntry((BasicDBObject) dbObject));
        }
        return retList;
    }

}
