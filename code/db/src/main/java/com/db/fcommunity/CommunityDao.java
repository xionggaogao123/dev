package com.db.fcommunity;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.fcommunity.CommunityEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

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
     * 获取社区
     *
     * @param communityIds
     * @return
     */
    public List<CommunityEntry> getCommunitysByIds(List<ObjectId> communityIds) {
        List<CommunityEntry> communityEntries = new ArrayList<CommunityEntry>();
        BasicDBObject query = new BasicDBObject().append(Constant.ID, new BasicDBObject(Constant.MONGO_IN, communityIds));
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query, Constant.FIELDS);
        for (DBObject dbo : dbObjects) {
            communityEntries.add(new CommunityEntry(dbo));
        }
        return communityEntries;
    }

    /**
     * 获取 - 公开群
     *
     * @return
     */
    public List<CommunityEntry> getOpenCommunitys() {
        List<CommunityEntry> list = new ArrayList<CommunityEntry>();
        BasicDBObject query = new BasicDBObject().append("op", Constant.ONE);
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query);
        for (DBObject dbo : dbObjects) {
            list.add(new CommunityEntry(dbo));
        }
        return list;
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
    public void updateCommunityOpen(ObjectId communityId, int open) {
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
     * 更改 二维码
     * @param communtiyId
     * @param qrUrl
     */
    public void updateCommunityQrUrl(ObjectId communtiyId, String qrUrl) {
        BasicDBObject query = new BasicDBObject().append(Constant.ID, communtiyId);
        BasicDBObject update = new BasicDBObject().append(Constant.MONGO_SET, new BasicDBObject("cmco", qrUrl));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query, update);
    }


    /**
     * 删除 - 社区 - 逻辑删除
     *
     * @param communityId
     */
    public void deleteCommunity(ObjectId communityId) {
        BasicDBObject query = new BasicDBObject().append(Constant.ID, communityId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("r",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query,update);
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


    public Boolean isCommunityNameUsed(String communityName) {
        CommunityEntry dbo = findByName(communityName);
        return dbo != null;
    }

    public boolean judgeCommunityName(String communityName, ObjectId id) {
        BasicDBObject query = new BasicDBObject().append(Constant.ID, id)
                .append("cmmn", communityName);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query) == 1;
    }

    public int getCommunityCountByOwerId(ObjectId userId) {
        BasicDBObject query = new BasicDBObject().append("cmow", userId);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query);
    }
}
