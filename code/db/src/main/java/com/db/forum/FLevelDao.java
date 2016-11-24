package com.db.forum;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.forum.FLevelEntry;
import com.pojo.forum.FPostEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/7/11.
 */
public class FLevelDao extends BaseDao {

    /**
     * 新增/更新论坛等级
     */
    public ObjectId saveOrUpdate(FLevelEntry fLevelEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_LEVEL, fLevelEntry.getBaseEntry());
        return fLevelEntry.getID();
    }

    /**
     * 删除论坛等级
     *
     * @param id
     */
    public void remove(ObjectId id) {
        BasicDBObject query = new BasicDBObject();
        if (null != id) {
            query.append(Constant.ID, id);
        }
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_LEVEL, query);
    }

    /**
     * 查询所有的论坛等级信息
     *
     * @return
     */
    public List<FLevelEntry> findFLevel() {
        List<FLevelEntry> fLevelEntryList = new ArrayList<FLevelEntry>();
        BasicDBObject query = new BasicDBObject();
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_LEVEL, query, null, new BasicDBObject("ss", 1));
        for (DBObject dbObject : dbObjectList) {
            fLevelEntryList.add(new FLevelEntry((BasicDBObject) dbObject));
        }
        return fLevelEntryList;
    }

    /**
     * 查询某论坛等级信息
     *
     * @param id
     * @return
     */
    public FLevelEntry findLevelById(ObjectId id) {
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_LEVEL, new BasicDBObject(Constant.ID, id), Constant.FIELDS);
        if (dbObject != null) {
            return new FLevelEntry((BasicDBObject) dbObject);
        } else {
            return null;
        }
    }


    /**
     * 根据星星数获取最小的经验值
     *
     * @param stars
     * @return
     */
    public long getMinLevel(Long stars) {
        BasicDBObject query = new BasicDBObject();
        query.append("ss", stars);
        List<FLevelEntry> fLevelEntryList = new ArrayList<FLevelEntry>();
        List<DBObject> dbo = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_LEVEL, query, Constant.FIELDS);
        for (DBObject dbObject : dbo) {
            FLevelEntry item = new FLevelEntry((BasicDBObject) dbObject);
            fLevelEntryList.add(item);
        }
        if (null != fLevelEntryList && fLevelEntryList.size() > 0) {
            return fLevelEntryList.get(0).getStartLevel();
        } else {
            return 1L;
        }
    }

    /**
     * 根据等级获取星星数
     *
     * @param levels
     * @return
     */
    public long getStars(long levels) {
        BasicDBObject query = new BasicDBObject();
        query.append("sl", new BasicDBObject(Constant.MONGO_LTE, levels));
        query.append("el", new BasicDBObject(Constant.MONGO_GTE, levels));
        List<FLevelEntry> fLevelEntryList = new ArrayList<FLevelEntry>();
        List<DBObject> dbo = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_LEVEL, query, Constant.FIELDS);
        for (DBObject dbObject : dbo) {
            FLevelEntry item = new FLevelEntry((BasicDBObject) dbObject);
            fLevelEntryList.add(item);
        }
        if (null != fLevelEntryList && fLevelEntryList.size() > 0) {
            return fLevelEntryList.get(0).getStars();
        } else {
            return 1L;
        }

    }

}
