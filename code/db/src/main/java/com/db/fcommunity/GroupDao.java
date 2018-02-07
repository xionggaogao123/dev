package com.db.fcommunity;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.fcommunity.GroupEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public GroupEntry findByEmchatId(String emChatId) {
        BasicDBObject query = new BasicDBObject("grcd", emChatId);
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


    public GroupEntry getGroupEntryByEmchatId(String emChatId) {
        BasicDBObject query = new BasicDBObject("grcd", emChatId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_GROUP, query, Constant.FIELDS);
        if(null!=dbo){
            return new GroupEntry(dbo);
        }else{
            return null;
        }
    }

    public List<GroupEntry> getGroupEntrysByEmchatIds(List<String> emchatIds) {
        BasicDBObject query = new BasicDBObject("grcd", new BasicDBObject(Constant.MONGO_IN, emchatIds));
        List<DBObject> dbos = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_GROUP, query, Constant.FIELDS);
        List<GroupEntry> groupEntries = new ArrayList<GroupEntry>();
        for (DBObject dbo : dbos) {
            groupEntries.add(new GroupEntry(dbo));
        }
        return groupEntries;
    }

    /**
     * 获取群组对应的组合头像
     */
    public Map<ObjectId,GroupEntry> getGroupEntries(List<ObjectId> ids){
        Map<ObjectId,GroupEntry> retMap=new HashMap<ObjectId, GroupEntry>();
        BasicDBObject query=new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_IN,ids));
        List<DBObject> dbObjects=find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_GROUP, query, Constant.FIELDS);
        if(null!=dbObjects&&!dbObjects.isEmpty()){
            for(DBObject dbObject:dbObjects){
                GroupEntry entry=new GroupEntry(dbObject);
                retMap.put(entry.getID(),entry);
            }
        }
        return retMap;
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
    /**
     * 根据用户id，查询管理员权限的gruopId列表
     *
     */
    public List<ObjectId> getGroupIdsList(List<ObjectId> groupId) {
        BasicDBObject query = new BasicDBObject().append(Constant.ID, new BasicDBObject(Constant.MONGO_IN,groupId)).append("r", 0);
        List<ObjectId> memberEntries = new ArrayList<ObjectId>();
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_GROUP, query, Constant.FIELDS);
        for (DBObject dbo : dbObjects) {
            GroupEntry memberEntry = new GroupEntry(dbo);
            if(memberEntry.getCommunityId() != null){
                memberEntries.add(memberEntry.getCommunityId());
            }
        }
        return memberEntries;
    }
    /**
     * 根据社区id
     *
     */
   /* public ObjectId getGroupIdByCommunityId(ObjectId cid) {
        BasicDBObject query = new BasicDBObject();
        query.append("isr",Constant.ZERO);
        query.append("cmid",cid);
        DBObject obj =
                findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_GROUP, query, Constant.FIELDS);
        if (obj != null) {
            return new GroupEntry(obj).getID();
        }
        return null;
    }*/
}
