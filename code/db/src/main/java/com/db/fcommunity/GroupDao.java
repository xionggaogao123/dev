package com.db.fcommunity;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.fcommunity.GroupEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jerry on 2016/11/1.
 * 讨论组 Dao层
 */
public class GroupDao extends BaseDao {

    public ObjectId add(GroupEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_GROUP, entry.getBaseEntry());
        return entry.getID();
    }

    /**
     * 获得 GroupEntry
     *
     * @param groupId
     * @return
     */
    public GroupEntry findByObjectId(ObjectId groupId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, groupId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_GROUP, query, Constant.FIELDS);
        return dbo == null ? null : new GroupEntry(dbo);
    }

    /**
     * 获得 List
     *
     * @param ids
     * @return
     */
    public List<GroupEntry> findByIdList(List<ObjectId> ids) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids));
        List<DBObject> dbos = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_GROUP, query);
        List<GroupEntry> groupEntrys = new ArrayList<GroupEntry>();
        for (DBObject dbo : dbos) {
            groupEntrys.add(new GroupEntry(dbo));
        }
        return groupEntrys;
    }

    /**
     * 根据环信id得到 群聊id
     *
     * @param emChatId
     * @return
     */
    public ObjectId getGroupIdByEmchatId(String emChatId) {
        BasicDBObject query = new BasicDBObject("grcd", emChatId);
        BasicDBObject field = new BasicDBObject(Constant.ID, 1);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_GROUP, query, field);
        return dbo == null ? null : (ObjectId) dbo.get(Constant.ID);
    }

    /**
     * 获取群聊头像
     *
     * @param groupId
     * @return
     */
    public String getHeadImage(ObjectId groupId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, groupId);
        BasicDBObject field = new BasicDBObject("himg", 1);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_GROUP, query, field);
        return dbo == null ? null : (String) dbo.get("himg");
    }

    /**
     * 设置群聊头像
     *
     * @param groupId
     * @param url
     */
    public void updateHeadImage(ObjectId groupId, String url) {
        BasicDBObject query = new BasicDBObject(Constant.ID, groupId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("himg", url));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_GROUP, query, update);
    }

    /**
     * 更新群聊名称
     *
     * @param groupId
     * @param name
     */
    public void updateGroupName(ObjectId groupId, String name) {
        BasicDBObject query = new BasicDBObject(Constant.ID, groupId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("grnm", name));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_GROUP, query, update);
    }

    /**
     * 获取群聊名称
     *
     * @param groupId
     * @return
     */
    public String getGroupName(ObjectId groupId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, groupId);
        BasicDBObject field = new BasicDBObject("grnm", 1);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_GROUP, query, field);
        return dbo == null ? null : (String) dbo.get("grnm");
    }

    /**
     * 获取环信id
     *
     * @param groupId
     * @return
     */
    public String getEmchatIdByGroupId(ObjectId groupId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, groupId);
        BasicDBObject field = new BasicDBObject("grcd", 1);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_GROUP, query, field);
        return dbo == null ? null : (String) dbo.get("grcd");
    }

    /**
     * 更新群组拥有者
     *
     * @param groupId
     * @param owerId
     */
    public void updateOwerId(ObjectId groupId, ObjectId owerId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, groupId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("grow", owerId));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_GROUP, query, update);
    }

    /**
     * 删除群组
     *
     * @param groupId
     */
    public void delete(ObjectId groupId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, groupId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("r", Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_GROUP, query, update);
    }

    /**
     * 根据群组id 获取 社区id
     *
     * @param groupId
     * @return
     */
    public ObjectId getCommunityIdByGroupId(ObjectId groupId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, groupId);
        BasicDBObject field = new BasicDBObject("cmid", 1);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_GROUP, query, field);
        return (ObjectId) dbo.get("cmid");
    }

    /**
     * 更新是否更改群聊名称
     *
     * @param groupId
     * @param ism
     */
    public void updateIsM(ObjectId groupId, int ism) {
        BasicDBObject query = new BasicDBObject(Constant.ID, groupId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ism", ism));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_GROUP, query, update);
    }
}