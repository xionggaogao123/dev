package com.db.fcommunity;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.fcommunity.ConcernEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/11/7.
 */
public class ConcernDao extends BaseDao {

    public void save(ConcernEntry concernEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_CONCERNED, concernEntry.getBaseEntry());
    }

    public void pullConcern(ObjectId id) {
        BasicDBObject dbObject = new BasicDBObject()
                .append(Constant.ID, id);
        BasicDBObject updateValue = new BasicDBObject()
                .append(Constant.MONGO_SET, new BasicDBObject("r", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_CONCERNED, dbObject, updateValue);
    }


    public ConcernEntry getConcernEntry(ObjectId id) {
        BasicDBObject dbObject = new BasicDBObject().append(Constant.ID, id).append("r", 0);
        DBObject dto = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_CONCERNED, dbObject);
        return dto == null ? null : new ConcernEntry(dto);
    }

    public int countConcernByUserId(ObjectId userId) {
        BasicDBObject dbObject = new BasicDBObject()
                .append("uid", userId).append("r", 0);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_CONCERNED, dbObject);
    }

    public List<ConcernEntry> getConcernByUserId(ObjectId userId, int page, int pageSize) {
        BasicDBObject orderBy = new BasicDBObject()
                .append(Constant.ID, -1);
        List<ConcernEntry> concernEntries = new ArrayList<ConcernEntry>();
        BasicDBObject dbObject = new BasicDBObject().append("uid", userId).append("r", 0);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_CONCERNED, dbObject, Constant.FIELDS, orderBy, (page - 1) * pageSize, pageSize);
        for (DBObject dbo : list) {
            concernEntries.add(new ConcernEntry(dbo));
        }
        return concernEntries;
    }

    public void setConcernData(ObjectId userId, ObjectId concernId, int remove) {
        BasicDBObject dbObject = new BasicDBObject()
                .append("uid", userId)
                .append("cid", concernId);
        BasicDBObject updateValue = new BasicDBObject()
                .append(Constant.MONGO_SET, new BasicDBObject("r", remove));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_CONCERNED, dbObject, updateValue);
    }

    public ConcernEntry getConcernData(ObjectId userId, ObjectId concernId, int remove) {
        BasicDBObject dbObject = new BasicDBObject()
                .append("uid", userId)
                .append("cid", concernId);
        if (remove != -1) {
            dbObject.append("r", remove);
        }
        DBObject dto = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_CONCERNED, dbObject);
        if (null != dto) {
            return new ConcernEntry(dto);
        }
        return null;

    }

}
