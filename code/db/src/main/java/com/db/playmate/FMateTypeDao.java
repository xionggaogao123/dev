package com.db.playmate;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.playmate.FMateTypeEntry;
import com.sys.constants.Constant;

/**
 * Created by moslpc on 2016/12/7.
 */
public class FMateTypeDao extends BaseDao {

    public void save(FMateTypeEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_F_MATETYPE, entry.getBaseEntry());
    }

    public void pushType(int type, int code, String data) {
        BasicDBObject query = new BasicDBObject("ty", type);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_PUSH, new BasicDBObject("data", new BasicDBObject("co", code).append("da", data)));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_F_MATETYPE, query, update);
    }

    public FMateTypeEntry getType(int type) {
        BasicDBObject query = new BasicDBObject("ty", type);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_F_MATETYPE, query);
        return dbo == null ? null : new FMateTypeEntry(dbo);
    }


}
