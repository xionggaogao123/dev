package com.db.fcommunity;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.*;
import com.pojo.fcommunity.CommunityEntry;
import com.pojo.indexPage.WebHomePageEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jerry on 2016/10/24.
 * 社区Entry Dao层
 */
public class CommunityDao extends BaseDao {

    /**
     * 保存社区
     *
     * @param entry
     * @return 返回保存是否成功
     */
    public void save(CommunityEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, entry.getBaseEntry());
    }

    /**
     * 返回社区Entry
     *
     * @param id
     * @return
     */
    public CommunityEntry findByObjectId(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query);
        return dbObject == null ? null : new CommunityEntry(dbObject);
    }

    public List<CommunityEntry> findByObjectIds(List<ObjectId> ids) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN,ids));
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query);
        List<CommunityEntry> communitys = new ArrayList<CommunityEntry>();
        for (DBObject dbo : dbObjects) {
            communitys.add(new CommunityEntry(dbo));
        }
        return communitys;
    }

    /**
     * 返回社区Entry
     *
     * @param searchId 搜索id
     * @return
     */
    public CommunityEntry findBySearchId(String searchId) {
        BasicDBObject query = new BasicDBObject("cmid", searchId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query);
        return dbObject == null ? null : new CommunityEntry(dbObject);
    }

    /**
     * 返回社区Entry
     *
     * @param emChatId 环信id
     * @return
     */
    public CommunityEntry findByEmChatId(String emChatId) {
        BasicDBObject query = new BasicDBObject("emid", emChatId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query);
        return dbObject == null ? null : new CommunityEntry(dbObject);
    }

    /**
     * 名称精确查找
     *
     * @param communityName
     * @return
     */
    public CommunityEntry findByName(String communityName) {
        BasicDBObject query = new BasicDBObject("cmmn", communityName);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query);
        return dbObject == null ? null : new CommunityEntry(dbObject);
    }

    /**
     * 社团名称:正则查找
     *
     * @param regular
     * @return
     */
    public List<CommunityEntry> findByRegularName(String regular) {
        BasicDBObject query = new BasicDBObject("cmmn", MongoUtils.buildRegex(regular));
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query);
        List<CommunityEntry> communitys = new ArrayList<CommunityEntry>();
        for (DBObject dbo : dbObjects) {
            communitys.add(new CommunityEntry(dbo));
        }
        return communitys;
    }

    /**
     * 获取 - 公开群
     *
     * @return
     */
    public List<CommunityEntry> getOpenCommunitys(int page, int pageSize, String lastId) {
        List<CommunityEntry> retList = new ArrayList<CommunityEntry>();
        BasicDBObject query = new BasicDBObject().append("op", Constant.ONE);
        DB myMongo = MongoFacroty.getAppDB();
        DBCollection communityCollection = myMongo.getCollection(Constant.COLLECTION_FORUM_COMMUNITY);
        DBCursor limit;
        if (page == 1) {
            limit = communityCollection.find(query)
                    .sort(Constant.MONGO_SORTBY_ASC).limit(pageSize);
        } else {
            if (StringUtils.isNotBlank(lastId)) {
                limit = communityCollection
                        .find(new BasicDBObject(Constant.ID, new BasicDBObject(
                                Constant.MONGO_GT, new ObjectId(lastId))))
                        .sort(Constant.MONGO_SORTBY_ASC).limit(pageSize);
            } else {
                limit = communityCollection.find(query).skip((page - 1) * pageSize)
                        .sort(Constant.MONGO_SORTBY_ASC).limit(pageSize);
            }
        }

        while (limit.hasNext()) {
            retList.add(new CommunityEntry(limit.next()));
        }
        return retList;
    }

    /**
     * 更改 - 社区名称
     *
     * @param communityId
     * @param name
     */
    public void updateCommunityName(ObjectId communityId, String name) {
        BasicDBObject query = new BasicDBObject().append(Constant.ID, communityId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("cmmn", name));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query, update);
    }

    /**
     * 更改 -社区公开状态
     *
     * @param communityId
     * @param open
     */
    public void updateCommunityOpenStatus(ObjectId communityId, int open) {
        BasicDBObject query = new BasicDBObject().append(Constant.ID, communityId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("op", open));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query, update);
    }


    /**
     * 更改 - 社区描述
     *
     * @param communityId
     * @param desc
     */
    public void updateCommunityDesc(ObjectId communityId, String desc) {
        BasicDBObject query = new BasicDBObject().append(Constant.ID, communityId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("cmde", desc));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query, update);
    }

    /**
     * 更改logo
     *
     * @param communityId
     * @param logo
     */
    public void updateCommunityLogo(ObjectId communityId, String logo) {
        BasicDBObject query = new BasicDBObject().append(Constant.ID, communityId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("cmlg", logo));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query, update);
    }

    /**
     * 获取 - 社区的群组 id
     *
     * @param communityId
     * @return
     */
    public ObjectId getGroupId(ObjectId communityId) {
        BasicDBObject query = new BasicDBObject().append(Constant.ID, communityId);
        BasicDBObject field = new BasicDBObject().append("grid", 1);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query, field);
        return dbo == null ? null : (ObjectId) dbo.get("grid");
    }

    public ObjectId getCommunityIdByGroupId(ObjectId groupId){
        BasicDBObject query = new BasicDBObject().append("grid", groupId);
        BasicDBObject field = new BasicDBObject().append(Constant.ID, 1);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query, field);
        return dbo == null ? null : (ObjectId) dbo.get(Constant.ID);
    }


    /**
     * 通过群组Ids列表查询社区列表
     * @param groupIds
     * @return
     */
    public List<CommunityEntry> getCommunityEntriesByGroupIds(List<ObjectId> groupIds){
        List<CommunityEntry> entries=new ArrayList<CommunityEntry>();
        BasicDBObject query = new BasicDBObject().append("grid", new BasicDBObject(Constant.MONGO_IN,groupIds));
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new CommunityEntry(dbObject));
            }
        }
        return entries;
    }


    public Boolean isCommunityNameUnique(String communityName) {
        return findByName(communityName) == null;
    }

    public boolean judgeCommunityName(String communityName, ObjectId id) {
        BasicDBObject query = new BasicDBObject().append(Constant.ID, id)
                .append("cmmn", communityName);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query) == 1;
    }

    /**
     * 通过id查找社区消息
     *
     * @param ids
     * @return
     */
    public Map<ObjectId, CommunityEntry> findMapInfo(List<ObjectId> ids) {
        BasicDBObject query = new BasicDBObject()
                .append(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids)).append("r", 0);
        Map<ObjectId, CommunityEntry> retMap = new HashMap<ObjectId, CommunityEntry>();
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query, Constant.FIELDS);
        if (null != list && !list.isEmpty()) {
            CommunityEntry e;
            for (DBObject dbo : list) {
                e = new CommunityEntry(dbo);
                retMap.put(e.getID(), e);
            }
        }
        return retMap;
    }

    public List<ObjectId> getGroupIdsByCommunityIds(List<ObjectId> ids){
        List<ObjectId> groupIds=new ArrayList<ObjectId>();
        BasicDBObject query = new BasicDBObject()
                .append(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids)).append("r", 0);
        Map<ObjectId, CommunityEntry> retMap = new HashMap<ObjectId, CommunityEntry>();
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query, Constant.FIELDS);
        if (null != list && !list.isEmpty()) {
            for(DBObject dbObject:list){
                CommunityEntry entry=new CommunityEntry(dbObject);
                groupIds.add(entry.getGroupId());
            }
        }
        return groupIds;
    }



    public Map<ObjectId,ObjectId> getGroupIds(List<ObjectId> ids){
        Map<ObjectId,ObjectId> groupIds=new HashMap<ObjectId, ObjectId>();
        BasicDBObject query = new BasicDBObject()
                .append(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids)).append("r", 0);
        Map<ObjectId, CommunityEntry> retMap = new HashMap<ObjectId, CommunityEntry>();
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query, Constant.FIELDS);
        if (null != list && !list.isEmpty()) {
            CommunityEntry e;
            for (DBObject dbo : list) {
                e = new CommunityEntry(dbo);
                groupIds.put(e.getID(),e.getGroupId());
            }
        }
        return groupIds;
    }

    public List<ObjectId> getListGroupIds(List<ObjectId> ids){
        List<ObjectId> groupIds=new ArrayList<ObjectId>();
        BasicDBObject query = new BasicDBObject()
                .append(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids)).append("r", 0);
        Map<ObjectId, CommunityEntry> retMap = new HashMap<ObjectId, CommunityEntry>();
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query, Constant.FIELDS);
        if (null != list && !list.isEmpty()) {
            CommunityEntry e;
            for (DBObject dbo : list) {
                e = new CommunityEntry(dbo);
                groupIds.add(e.getGroupId());
            }
        }
        return groupIds;
    }

    public List<ObjectId> selectCommunityByGroupIds(List<ObjectId> ids){
        List<ObjectId> groupIds=new ArrayList<ObjectId>();
        BasicDBObject query = new BasicDBObject()
                .append("grid", new BasicDBObject(Constant.MONGO_IN, ids)).append("r", 0);
        Map<ObjectId, CommunityEntry> retMap = new HashMap<ObjectId, CommunityEntry>();
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query, Constant.FIELDS);
        if (null != list && !list.isEmpty()) {
            CommunityEntry e;
            for (DBObject dbo : list) {
                e = new CommunityEntry(dbo);
                groupIds.add(e.getID());
            }
        }
        return groupIds;
    }

    /**
     * 根据社区id
     *
     */
    public ObjectId getGroupIdByCommunityId(ObjectId cid) {
        BasicDBObject query = new BasicDBObject();
        query.append("r",Constant.ZERO);
        query.append(Constant.ID,cid);
        DBObject obj =
                findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query, Constant.FIELDS);
        if (obj != null) {
            return new CommunityEntry(obj).getGroupId();
        }
        return null;
    }
}
