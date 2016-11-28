package com.db.fcommunity;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.fcommunity.MemberEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jerry on 2016/11/1.
 * 成员Dao层
 */
public class MemberDao extends BaseDao {


    public void save(MemberEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, entry.getBaseEntry());
    }

    /**
     * 获取群聊成员 分页
     *
     * @param grid
     * @param page
     * @param pageSize
     * @return
     */
    public List<MemberEntry> getMembers(ObjectId grid, int page, int pageSize) {
        BasicDBObject query = new BasicDBObject("grid", grid).append("r", Constant.ZERO);
        BasicDBObject orderBy = new BasicDBObject("rl", Constant.DESC).append(Constant.ID, Constant.DESC);
        List<MemberEntry> memberEntries = new ArrayList<MemberEntry>();
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query, Constant.FIELDS, orderBy, (page - Constant.ONE) * pageSize, pageSize);
        for (DBObject dbo : dbObjects) {
            memberEntries.add(new MemberEntry(dbo));
        }
        return memberEntries;
    }

    /**
     * 获取群聊成员  前20个
     *
     * @param grid
     * @return
     */
    public List<MemberEntry> getMembers(ObjectId grid, int count) {
        BasicDBObject query = new BasicDBObject("grid", grid).append("r", Constant.ZERO);
        BasicDBObject orderBy = new BasicDBObject("rl", Constant.DESC).append(Constant.ID, Constant.DESC);
        List<MemberEntry> memberEntries = new ArrayList<MemberEntry>();
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query, Constant.FIELDS, orderBy, Constant.ZERO, count);
        for (DBObject dbo : dbObjects) {
            memberEntries.add(new MemberEntry(dbo));
        }
        return memberEntries;
    }

    /**
     * 查询群聊人数
     *
     * @param grid
     * @return
     */
    public int countMember(ObjectId grid) {
        BasicDBObject query = new BasicDBObject("grid", grid).append("r", Constant.ZERO);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query);
    }

    /**
     * 更新昵称
     *
     * @param userId
     * @param groupId
     * @param nickName
     */
    public void updateSetting(ObjectId userId, ObjectId groupId, String nickName) {
        BasicDBObject query = new BasicDBObject("uid", userId).append("cmid", groupId);
        BasicDBObject update = new BasicDBObject().append(Constant.MONGO_SET, new BasicDBObject("nk", nickName));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query, update);
    }

    /**
     * 更新免打扰
     *
     * @param userId
     * @param groupId
     * @param status
     */
    public void updateStatus(ObjectId userId, ObjectId groupId, int status) {
        BasicDBObject query = new BasicDBObject()
                .append("uid", userId)
                .append("cmid", groupId);
        BasicDBObject update = new BasicDBObject()
                .append(Constant.MONGO_SET, new BasicDBObject("st", status));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query, update);
    }

    /**
     * 删除讨论组成员
     *
     * @param groupId
     * @param userId
     */
    public void deleteMember(ObjectId groupId, ObjectId userId) {
        BasicDBObject query = new BasicDBObject()
                .append("grid", groupId)
                .append("uid", userId);
        BasicDBObject updateValue = new BasicDBObject()
                .append("r", 1)
                .append("rl", 0);

        BasicDBObject update = new BasicDBObject()
                .append(Constant.MONGO_SET, updateValue);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query, update);
    }

    /**
     * 删除多个成员
     * 角色 -
     *
     * @param membersId
     */
    public void deleteMember(List<ObjectId> membersId) {
        BasicDBObject query = new BasicDBObject()
                .append(Constant.ID, new BasicDBObject(Constant.MONGO_IN, membersId));
        BasicDBObject updateValue = new BasicDBObject()
                .append("r", 1)
                .append("rl", 0);
        BasicDBObject update = new BasicDBObject()
                .append(Constant.MONGO_SET, updateValue);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query, update);
    }

    /**
     * 设置副社长
     *
     * @param membersId
     * @param role
     */
    public void setDeputyHead(List<ObjectId> membersId, int role) {
        BasicDBObject query = new BasicDBObject()
                .append(Constant.ID, new BasicDBObject(Constant.MONGO_IN, membersId));
        BasicDBObject updateValue = new BasicDBObject()
                .append(Constant.MONGO_SET, new BasicDBObject("rl", role));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query, updateValue);
    }

    /**
     * 设置某个人为副社长
     *
     * @param groupId
     * @param userId
     */
    public void setDeputyHead(ObjectId groupId, ObjectId userId) {
        BasicDBObject query = new BasicDBObject()
                .append("grid", groupId)
                .append("uid", userId)
                .append("r", 0);
        BasicDBObject updateValue = new BasicDBObject()
                .append(Constant.MONGO_SET, new BasicDBObject("rl", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query, updateValue);
    }

    /**
     * 设置多个人为副社长
     *
     * @param groupId
     * @param userIds
     */
    public void setDeputyHead(ObjectId groupId, List<ObjectId> userIds) {
        BasicDBObject query = new BasicDBObject()
                .append("grid", groupId)
                .append("uid", new BasicDBObject("$in", userIds))
                .append("r", 0);
        BasicDBObject updateValue = new BasicDBObject()
                .append(Constant.MONGO_SET, new BasicDBObject("rl", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query, updateValue);
    }

    /**
     * 取消设置一个人为副社长
     *
     * @param groupId
     * @param userId
     */
    public void unsetDeputyHead(ObjectId groupId, ObjectId userId) {
        BasicDBObject query = new BasicDBObject()
                .append("grid", groupId)
                .append("uid", userId)
                .append("r", 0);
        BasicDBObject updateValue = new BasicDBObject()
                .append(Constant.MONGO_SET, new BasicDBObject("rl", 0));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query, updateValue);
    }

    /**
     * 设置管理员
     *
     * @param groupId
     * @param userId
     */
    public void setHead(ObjectId groupId, ObjectId userId) {
        BasicDBObject query = new BasicDBObject()
                .append("grid", groupId)
                .append("uid", userId)
                .append("r", 0);
        BasicDBObject updateValue = new BasicDBObject()
                .append(Constant.MONGO_SET, new BasicDBObject("rl", 2));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query, updateValue);
    }

    /**
     * 查询该社区副社长人数
     *
     * @param groupId
     * @return
     */
    public int countDeputyHead(ObjectId groupId) {
        BasicDBObject query = new BasicDBObject()
                .append("grid", groupId)
                .append("rl", 1)
                .append("r", 0);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query);
    }

    /**
     * 获取副群主
     *
     * @param groupId
     * @return
     */
    public List<MemberEntry> getDeputyHead(ObjectId groupId) {
        List<MemberEntry> members = new ArrayList<MemberEntry>();
        BasicDBObject query = new BasicDBObject()
                .append("grid", groupId)
                .append("rl", 1)
                .append("r", 0);
        List<DBObject> dbos = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query);
        for (DBObject dbo : dbos) {
            members.add(new MemberEntry(dbo));
        }
        return members;
    }

    /**
     * 判断 - 是否是讨论组成员
     *
     * @param groupId
     * @param userId
     * @return
     */
    public boolean isMember(ObjectId groupId, ObjectId userId) {
        BasicDBObject query = new BasicDBObject()
                .append("grid", groupId)
                .append("uid", userId)
                .append("r", 0);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query) == 1;
    }


    /**
     * 通过热区以及用户Id查找数据
     *
     * @param groupIds
     * @param userId
     * @return
     */
    public Map<ObjectId, MemberEntry> getHotOwenr(List<ObjectId> groupIds, ObjectId userId) {
        Map<ObjectId, MemberEntry> map = new HashMap<ObjectId, MemberEntry>();
        BasicDBObject query = new BasicDBObject()
                .append(Constant.MONGO_IN, new BasicDBObject("grid", groupIds))
                .append("uid", userId)
                .append("r", 0);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query, Constant.FIELDS);
        if (null != list && !list.isEmpty()) {
            MemberEntry memberEntry;
            for (DBObject dbo : list) {
                memberEntry = new MemberEntry(dbo);
                map.put(memberEntry.getGroupId(), memberEntry);
            }
        }
        return map;
    }

    /**
     * 退出社区后再加入社区
     */
    public void updateMember(ObjectId groupId, ObjectId userId, int remove) {
        BasicDBObject query = new BasicDBObject()
                .append("grid", groupId)
                .append("uid", userId);
        BasicDBObject updateValue = new BasicDBObject()
                .append("r", remove);
        BasicDBObject update = new BasicDBObject()
                .append(Constant.MONGO_SET, updateValue);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query, update);
    }

    /**
     * 判断该用户是否存在数据
     */
    public boolean isBeforeMember(ObjectId groupId, ObjectId userId) {
        BasicDBObject query = new BasicDBObject()
                .append("grid", groupId)
                .append("uid", userId);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query) == 1;
    }


    /**
     * 获取管理员
     *
     * @param id
     * @return
     */
    public List<MemberEntry> getManagers(ObjectId id) {
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        List<MemberEntry> members = new ArrayList<MemberEntry>();
        BasicDBObject query = new BasicDBObject().append("grid", id)
                .append("rl", new BasicDBObject(Constant.MONGO_IN, list))
                .append("r", 0);
        List<DBObject> dbos = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query);
        for (DBObject dbo : dbos) {
            members.add(new MemberEntry(dbo));
        }
        return members;
    }

    /**
     * 获取群聊中的某个人
     *
     * @param groupId
     * @param userId
     * @return
     */
    public MemberEntry getUser(ObjectId groupId, ObjectId userId) {
        BasicDBObject query = new BasicDBObject("grid", groupId).append("uid", userId).append("r", 0);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query);
        return dbo == null ? null : new MemberEntry(dbo);
    }

    /**
     * 判断是否是管理员: 包含群主，副群主
     *
     * @param groupId
     * @param userId
     * @return
     */
    public boolean isManager(ObjectId groupId, ObjectId userId) {
        BasicDBObject query = new BasicDBObject()
                .append("grid", groupId)
                .append("uid", userId)
                .append("r", 0);
        BasicDBObject field = new BasicDBObject()
                .append("rl", 1);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query, field);
        if (dbo != null) {
            int role = (Integer) dbo.get("rl");
            return role >= 1;
        }
        return false;
    }

    /**
     * 判断是否是群主
     *
     * @param groupId
     * @param userId
     * @return
     */
    public boolean isHead(ObjectId groupId, ObjectId userId) {
        BasicDBObject query = new BasicDBObject().append("grid", groupId)
                .append("uid", userId)
                .append("rl", 2)
                .append("r", 0);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query) == Constant.ONE;
    }

    /**
     * 判断是否是副群主
     *
     * @param groupId
     * @param userId
     * @return
     */
    public boolean isDeputyHead(ObjectId groupId, ObjectId userId) {
        BasicDBObject query = new BasicDBObject().append("grid", groupId)
                .append("uid", userId)
                .append("rl", 1)
                .append("r", 0);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query) == Constant.ONE;
    }


    /**
     * 获取可退出列表
     *
     * @param groupId
     * @return
     */
    public List<ObjectId> getQuitList(ObjectId groupId) {
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        BasicDBObject query = new BasicDBObject()
                .append("grid", groupId)
                .append("rl", 0)
                .append("r", 0);
        BasicDBObject field = new BasicDBObject()
                .append("uid", 1);
        List<DBObject> dbos = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query, field);
        for (DBObject dbo : dbos) {
            userIds.add((ObjectId) dbo.get("uid"));
        }
        return userIds;
    }

    /**
     * 更新我在群里的昵称
     *
     * @param groupId
     * @param userId
     * @param nickName
     */
    public void updateMyNickname(ObjectId groupId, ObjectId userId, String nickName) {
        BasicDBObject query = new BasicDBObject("grid", groupId).append("uid", userId).append("r", 0);
        BasicDBObject update = new BasicDBObject()
                .append(Constant.MONGO_SET, new BasicDBObject("nk", nickName));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query, update);
    }

    /**
     * 更新免打扰设置
     *
     * @param groupId
     * @param userId
     * @param status
     */
    public void updateMyStatus(ObjectId groupId, ObjectId userId, int status) {
        BasicDBObject query = new BasicDBObject("grid", groupId).append("uid", userId).append("r", 0);
        BasicDBObject update = new BasicDBObject()
                .append(Constant.MONGO_SET, new BasicDBObject("st", status));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query, update);
    }

    /**
     * 获取全部成员
     *
     * @param groupId
     * @return
     */
    public List<MemberEntry> getAllMembers(ObjectId groupId) {
        BasicDBObject query = new BasicDBObject().append("grid", groupId).append("r", 0);
        BasicDBObject orderBy = new BasicDBObject().append("rl", -1).append(Constant.ID, -1);
        List<MemberEntry> memberEntries = new ArrayList<MemberEntry>();
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query, Constant.FIELDS, orderBy);
        for (DBObject dbo : dbObjects) {
            memberEntries.add(new MemberEntry(dbo));
        }
        return memberEntries;
    }

    /**
     * 获取群主
     *
     * @param groupId
     * @return
     */
    public MemberEntry getHead(ObjectId groupId) {
        BasicDBObject query = new BasicDBObject("grid", groupId).append("rl", 2).append("r", Constant.ZERO);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query, Constant.FIELDS);
        return dbo == null ? null : new MemberEntry(dbo);
    }

    /**
     * 清除所有的副社长
     *
     * @param groupId
     */
    public void cleayDeputyHead(ObjectId groupId) {
        BasicDBObject query = new BasicDBObject("grid", groupId).append("rl", 1).append("r", Constant.ZERO);
        BasicDBObject update = new BasicDBObject().append(Constant.MONGO_SET, new BasicDBObject("rl", 0));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query, update);
    }

    /**
     * 更新在群组中的头像
     *
     * @param userId
     * @param avatar
     */
    public void updateAllAvatar(ObjectId userId, String avatar) {
        BasicDBObject query = new BasicDBObject("uid", userId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("av", avatar));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query, update);
    }
}
