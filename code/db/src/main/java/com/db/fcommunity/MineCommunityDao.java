package com.db.fcommunity;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.fcommunity.MineCommunityEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void batchSave(List<MineCommunityEntry> entries){
        List<DBObject> list= MongoUtils.fetchDBObjectList(entries);
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MINE_COMMUNITY, list);
    }

    public List<MineCommunityEntry> findAll(ObjectId userId, int page, int pageSize) {
        BasicDBObject query = new BasicDBObject()
                .append("uid", userId);
        BasicDBObject orderBy = new BasicDBObject()
                .append("prio", -1)
                .append("cust",-1)
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

    public void deleteList(ObjectId communityId, List<ObjectId> userIds) {
        BasicDBObject query = new BasicDBObject().append("uid", new BasicDBObject(Constant.MONGO_IN,userIds)).append("cmid", communityId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MINE_COMMUNITY, query);
    }

    public void updateInitSort(ObjectId userId){
        List<Integer> integers=new ArrayList<Integer>();
        integers.add(0);
        BasicDBObject query=new BasicDBObject("uid", userId).append("cust",new BasicDBObject(Constant.MONGO_NOTIN,integers));
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("cust",0));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MINE_COMMUNITY, query,updateValue);
    }


    public Map<ObjectId,MineCommunityEntry> getMySortCommunities(ObjectId userId, List<ObjectId> communityIds){
        Map<ObjectId,MineCommunityEntry> mineCommunityEntries = new HashMap<ObjectId, MineCommunityEntry>();
        BasicDBObject query=new BasicDBObject("uid", userId).append("cmid",new BasicDBObject(Constant.MONGO_IN,communityIds));
        List<DBObject> dbos = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MINE_COMMUNITY, query, Constant.FIELDS);
        if(null!=dbos&&!dbos.isEmpty()){
            for(DBObject dbo : dbos){
                MineCommunityEntry entry=new MineCommunityEntry(dbo);
                mineCommunityEntries.put(entry.getCommunityId(),entry);
            }
        }
        return mineCommunityEntries;
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
            MineCommunityEntry communityEntry = new MineCommunityEntry(userId, communityId, 3 ,0);
            save(communityEntry);
        }
    }

    public void cleanPrior(){
        List<ObjectId> communities=new ArrayList<ObjectId>();
        communities.add(new ObjectId("582f00033d4df91126ff2b9b"));
        BasicDBObject query=new BasicDBObject("cmid",new BasicDBObject(Constant.MONGO_NOTIN,communities)).append("prio",2);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("prio",1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MINE_COMMUNITY, query,updateValue);
    }

    public Map<ObjectId,List<ObjectId>> findAllGroupByUserId(List<ObjectId> userIds) {
        BasicDBObject query = new BasicDBObject()
                .append("uid", new BasicDBObject(Constant.MONGO_IN,userIds));
        List<DBObject> dbos;
        dbos = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MINE_COMMUNITY, query, Constant.FIELDS);
        List<MineCommunityEntry> mineCommunityEntries = new ArrayList<MineCommunityEntry>();
        for (DBObject dbo : dbos) {
            mineCommunityEntries.add(new MineCommunityEntry(dbo));
        }

        //开始封装返回数据
        Map<ObjectId,List<ObjectId>> result = new HashMap<ObjectId, List<ObjectId>>();
        for (ObjectId userId : userIds) {
            List<ObjectId> ObjectIds = new ArrayList<ObjectId>();
            for (MineCommunityEntry entry : mineCommunityEntries){
                if(userId.equals(entry.getUserId())){
                    ObjectIds.add(entry.getCommunityId());
                }
            }
            result.put(userId,ObjectIds);
        }
    return result;
    }
}
