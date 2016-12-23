package com.db.fcommunity;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.fcommunity.MineCommunityEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jerry on 2016/11/17.
 * 我的社区
 * uid： 用户id
 * cmid： 社区id
 * prio: 优先级 1：自己加入的社区  2：自己创建的社区  3：系统设定(如复兰社区)
 */
public class MineCommunityDao extends BaseDao {

    public void save(MineCommunityEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MINE_COMMUNITY, entry.getBaseEntry());
    }

    public List<MineCommunityEntry> findAll(ObjectId userId, int page, int pageSize) {
        BasicDBObject query = new BasicDBObject()
                .append("uid", userId);
        BasicDBObject orderBy = new BasicDBObject()
                .append("prio", -1)
                .append("tp", -1)
                .append(Constant.ID, Constant.DESC);
        List<DBObject> dbos;
        if (page != -1) {
            dbos = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MINE_COMMUNITY, query, Constant.FIELDS, orderBy, (page - 1) * pageSize, pageSize);
        } else {
            dbos = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MINE_COMMUNITY, query, Constant.FIELDS, orderBy);
        }
        List<MineCommunityEntry> mineCommunityEntries = new ArrayList<MineCommunityEntry>();
        for (DBObject dbo : dbos) {
            mineCommunityEntries.add(new MineCommunityEntry(dbo));
        }
        return mineCommunityEntries;
    }

    public void updatePriority(ObjectId communityId, int prio) {
        BasicDBObject query = new BasicDBObject("cmid", communityId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("prio", prio));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MINE_COMMUNITY, query, update);
    }

    public int count(ObjectId userId) {
        BasicDBObject query = new BasicDBObject().append("uid", userId);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MINE_COMMUNITY, query);
    }

    public void delete(ObjectId communityId, ObjectId userId) {
        BasicDBObject query = new BasicDBObject().append("uid", userId).append("cmid", communityId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MINE_COMMUNITY, query);
    }

    public List<MineCommunityEntry> findByCount(ObjectId userId, int count) {
        BasicDBObject query = new BasicDBObject().append("uid", userId);
        BasicDBObject orderBy = new BasicDBObject().append(Constant.ID, -1);
        List<DBObject> dbos = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MINE_COMMUNITY, query, Constant.FIELDS, orderBy, 0, count);
        List<MineCommunityEntry> mineCommunityEntries = new ArrayList<MineCommunityEntry>();
        for (DBObject dbo : dbos) {
            mineCommunityEntries.add(new MineCommunityEntry(dbo));
        }
        return mineCommunityEntries;
    }

    public void setDefaultSort() {
        BasicDBObject query = new BasicDBObject().
                append("prio", new BasicDBObject(Constant.MONGO_NOTIN, new Integer[]{3}));
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("prio", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MINE_COMMUNITY, query, updateValue);
    }

    public MineCommunityEntry findById(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MINE_COMMUNITY, query);
        return dbObject == null ? null : new MineCommunityEntry(dbObject);
    }

    public MineCommunityEntry findByUserAndCommunity(ObjectId communityId, ObjectId userId) {
        BasicDBObject query = new BasicDBObject("cmid", communityId).append("uid", userId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MINE_COMMUNITY, query);
        return dbObject == null ? null : new MineCommunityEntry(dbObject);
    }

    public void cleanNecessaryCommunity(ObjectId userId, ObjectId communityId) {
        BasicDBObject query = new BasicDBObject("uid", userId).append("cmid", communityId);
        if (count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MINE_COMMUNITY, query) >= 2) {
            delete(communityId, userId);
            MineCommunityEntry communityEntry = new MineCommunityEntry(userId, communityId, 3);
            save(communityEntry);
        }
    }
}
