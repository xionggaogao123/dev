package com.db.forum;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.forum.FCollectionEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangkaidong on 2016/5/30.
 * <p>
 * 收藏帖子DAO
 */
public class FCollectionDao extends BaseDao {

    /**
     * 新增收藏
     */
    public ObjectId addCollection(FCollectionEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COLLECTION, entry.getBaseEntry());
        return entry.getID();
    }

    /**
     * 获取收藏(分页)
     */
    public List<FCollectionEntry> getCollections(ObjectId userId, int type, int skip, int limit) {
        List<FCollectionEntry> collectionEntries = new ArrayList<FCollectionEntry>();

        BasicDBObject query = new BasicDBObject("uid", userId).append("tp", type);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COLLECTION, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, skip, limit);

        for (DBObject dbObject : dbObjectList) {
            collectionEntries.add(new FCollectionEntry((BasicDBObject) dbObject));
        }

        return collectionEntries;
    }

    /**
     * 获取收藏(分页)
     */
    public List<FCollectionEntry> getCollection(ObjectId userId, int type) {
        List<FCollectionEntry> collectionEntries = new ArrayList<FCollectionEntry>();

        BasicDBObject query = new BasicDBObject("uid", userId).append("tp", type);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COLLECTION, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC);

        for (DBObject dbObject : dbObjectList) {
            collectionEntries.add(new FCollectionEntry((BasicDBObject) dbObject));
        }

        return collectionEntries;
    }

    /**
     * 通过id获取收藏
     *
     * @param userId
     * @param postSectionId
     */
    public FCollectionEntry getCollection(ObjectId userId, ObjectId postSectionId) {
        BasicDBObject query = new BasicDBObject()
                .append("uid", userId)
                .append("psid", postSectionId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COLLECTION, query, Constant.FIELDS);
        if (dbObject != null) {
            return new FCollectionEntry((BasicDBObject) dbObject);
        } else {
            return null;
        }
    }


    /**
     * 删除收藏
     */
    public void removeCollection(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COLLECTION, query);
    }


    /**
     * 查询该帖子/板块收藏的人数
     *
     * @param postSectionId 帖子/板块Id
     * @return
     */
    public int getCollectionCount(ObjectId postSectionId) {

        BasicDBObject query = new BasicDBObject();
        if (postSectionId != null) {
            query.append("psid", postSectionId);
        }
        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COLLECTION, query);
        return count;
    }

}
