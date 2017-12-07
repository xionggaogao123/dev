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
     * @param groupId
     * @param page
     * @param pageSize
     * @return
     */
    public List<MemberEntry> getMembers(ObjectId groupId, int page, int pageSize) {
        BasicDBObject query = new BasicDBObject("grid", groupId).append("r", Constant.ZERO);
        BasicDBObject orderBy = new BasicDBObject("rl", Constant.DESC).append(Constant.ID, Constant.DESC);
        List<MemberEntry> memberEntries = new ArrayList<MemberEntry>();
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query, Constant.FIELDS, orderBy, (page - Constant.ONE) * pageSize, pageSize);
        for (DBObject dbo : dbObjects) {
            memberEntries.add(new MemberEntry(dbo));
        }
        return memberEntries;
    }

    public List<ObjectId> getPageMembers(ObjectId groupId,int page,int pageSize){
        BasicDBObject query=new BasicDBObject("grid",groupId).append("r", Constant.ZERO);
        List<ObjectId> memberEntries = new ArrayList<ObjectId>();
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER,
                query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        for (DBObject dbo : dbObjects) {
            memberEntries.add(new MemberEntry(dbo).getUserId());
        }
        return memberEntries;
    }

    public List<MemberEntry> getMembersFromTeacher(List<ObjectId> userIds,int page, int pageSize) {
        BasicDBObject query = new BasicDBObject("r", Constant.ZERO);
        List<Integer> integers = new ArrayList<Integer>();
        integers.add(1);
        integers.add(2);
        query.append("rl", new BasicDBObject(Constant.MONGO_IN, integers));
        query.append("uid",new BasicDBObject(Constant.MONGO_NOTIN,userIds));
        BasicDBObject orderBy = new BasicDBObject("rl", Constant.DESC).append(Constant.ID, Constant.DESC);
        List<MemberEntry> memberEntries = new ArrayList<MemberEntry>();
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query, Constant.FIELDS, orderBy, (page - Constant.ONE) * pageSize, pageSize);
        for (DBObject dbo : dbObjects) {
            memberEntries.add(new MemberEntry(dbo));
        }
        return memberEntries;
    }
    public List<ObjectId> getMembersByList(List<ObjectId> groupId) {
        BasicDBObject query = new BasicDBObject("grid", new BasicDBObject(Constant.MONGO_IN,groupId)).append("r", Constant.ZERO);
        query.append("rl",0);
        BasicDBObject orderBy = new BasicDBObject(Constant.ID, Constant.DESC);
        List<ObjectId> memberEntries = new ArrayList<ObjectId>();
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query, Constant.FIELDS, orderBy);
        for (DBObject dbo : dbObjects) {
            memberEntries.add(new MemberEntry(dbo).getUserId());
        }
        return memberEntries;
    }
    public List<ObjectId> getMembersByList2(List<ObjectId> groupId,ObjectId userId) {
        BasicDBObject query = new BasicDBObject("grid", new BasicDBObject(Constant.MONGO_IN,groupId)).append("r", Constant.ZERO);
        query.append("uid", new BasicDBObject(Constant.MONGO_NE, userId));
        BasicDBObject orderBy = new BasicDBObject(Constant.ID, Constant.DESC);
        List<ObjectId> memberEntries = new ArrayList<ObjectId>();
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query, Constant.FIELDS, orderBy);
        for (DBObject dbo : dbObjects) {
            memberEntries.add(new MemberEntry(dbo).getUserId());
        }
        return memberEntries;
    }
    /**
     * 获取群聊成员  前20个
     *
     * @param groupId
     * @return
     */
    public List<MemberEntry> getMembers(ObjectId groupId, int count) {
        BasicDBObject query = new BasicDBObject("grid", groupId).append("r", Constant.ZERO);
        BasicDBObject orderBy = new BasicDBObject("rl", Constant.DESC).append(Constant.ID, Constant.DESC);
        List<MemberEntry> memberEntries = new ArrayList<MemberEntry>();
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query, Constant.FIELDS, orderBy, Constant.ZERO, count);
        for (DBObject dbo : dbObjects) {
            memberEntries.add(new MemberEntry(dbo));
        }
        return memberEntries;
    }


    /**
     * 查询群组对应的群昵称
     */
    public Map<String,MemberEntry> getGroupNick(List<ObjectId> groupIds,List<ObjectId> userIds){
        Map<String,MemberEntry> map=new HashMap<String, MemberEntry>();
        BasicDBObject query = new BasicDBObject("grid", new BasicDBObject(Constant.MONGO_IN, groupIds)).
                append("uid",new BasicDBObject(Constant.MONGO_IN, userIds)).
                append("r", Constant.ZERO);
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query, Constant.FIELDS);
        for (DBObject dbo : dbObjects) {
            MemberEntry entry=new MemberEntry(dbo);
            map.put(entry.getGroupId()+"$"+entry.getUserId(),entry);
        }
        return map;
    }

    //获得个人非管理员的groupId
    public List<ObjectId> selectMyRoleList(ObjectId userId){
        List<ObjectId> olist = new ArrayList<ObjectId>();
        BasicDBObject query = new BasicDBObject().
                append("uid",userId).append("r", Constant.ZERO).append("rl",0);
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query, Constant.FIELDS);
        for (DBObject dbo : dbObjects) {
            MemberEntry entry=new MemberEntry(dbo);
            olist.add(entry.getGroupId());
        }
        return olist;
    }

    /**
     * 查询群聊人数
     *
     * @param groupId
     * @return
     */
    public int getMemberCount(ObjectId groupId) {
        BasicDBObject query = new BasicDBObject("grid", groupId).append("r", Constant.ZERO);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query);
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
        BasicDBObject query = new BasicDBObject("grid", groupId).append("uid", userId);
        BasicDBObject updateValue = new BasicDBObject("r", 1).append("rl", 0);
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
     * 设置多个人为副社长
     *
     * @param groupId
     * @param userIds
     */
    public void setDeputyHead(ObjectId groupId, List<ObjectId> userIds) {
        BasicDBObject query = new BasicDBObject()
                .append("grid", groupId)
                .append("uid", new BasicDBObject(Constant.MONGO_IN, userIds))
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
        return findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query) != null;
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
        return findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query) != null;
    }


    /**
     * 获取管理员
     *
     * @param id
     * @return
     */
    public List<MemberEntry> getManagers(ObjectId id) {
        List<MemberEntry> members = new ArrayList<MemberEntry>();
        BasicDBObject query = new BasicDBObject("grid", id)
                .append("rl", new BasicDBObject(Constant.MONGO_IN, new Integer[]{1, 2}))
                .append("r", 0);
        BasicDBObject orderBy = new BasicDBObject("rl", Constant.DESC).append(Constant.ID, Constant.DESC);
        List<DBObject> dos = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query, Constant.FIELDS, orderBy);
        for (DBObject dbo : dos) {
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
        return dbo != null && (Integer) dbo.get("rl") >= 1;
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
        return findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query) != null;
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
     * 更新我在某个群里的昵称
     *
     * @param groupId
     * @param userId
     * @param nickName
     */
    public void updateMyNickname(ObjectId groupId, ObjectId userId, String nickName) {
        BasicDBObject query = new BasicDBObject("grid", groupId).append("uid", userId).append("r", 0);
        BasicDBObject update = new BasicDBObject().append(Constant.MONGO_SET, new BasicDBObject("nk", nickName));
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
        BasicDBObject update = new BasicDBObject().append(Constant.MONGO_SET, new BasicDBObject("st", status));
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
        BasicDBObject orderBy = new BasicDBObject("rl", Constant.DESC).append(Constant.ID, Constant.DESC);
        List<MemberEntry> memberEntries = new ArrayList<MemberEntry>();
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query, Constant.FIELDS, orderBy);
        for (DBObject dbo : dbObjects) {
            memberEntries.add(new MemberEntry(dbo));
        }
        return memberEntries;
    }


    /**
     * 获取所有的成员Id
     *
     * @param groupId
     * @return
     */
    public List<ObjectId> getAllMemberIds(ObjectId groupId) {
        BasicDBObject query = new BasicDBObject().append("grid", groupId).append("r", 0);
        BasicDBObject orderBy = new BasicDBObject().append("rl", -1).append(Constant.ID, -1);
        List<ObjectId> memberEntries = new ArrayList<ObjectId>();
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query, Constant.FIELDS, orderBy);
        for (DBObject dbo : dbObjects) {
            MemberEntry memberEntry = new MemberEntry(dbo);
            memberEntries.add(memberEntry.getUserId());
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
    public void cleanDeputyHead(ObjectId groupId) {
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
    /**
     * 根据用户id，查询管理员权限的gruopId列表
     *
     */
    public List<ObjectId> getGroupIdsList(ObjectId userId) {
        BasicDBObject query = new BasicDBObject().append("uid", userId).append("r", 0);
        BasicDBObject orderBy = new BasicDBObject().append("rl", -1).append(Constant.ID, -1);
        List<ObjectId> memberEntries = new ArrayList<ObjectId>();
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query, Constant.FIELDS, orderBy);
        for (DBObject dbo : dbObjects) {
            MemberEntry memberEntry = new MemberEntry(dbo);
            if(memberEntry.getRole()==1 || memberEntry.getRole()==2 ){
                memberEntries.add(memberEntry.getGroupId());
            }
        }
        return memberEntries;
    }


    /**
     * 根据用户Id,查询不具有管理员权限的groupIds列表
     * @param userId
     * @return
     */
    public List<ObjectId> getGroupIdsByUserId(ObjectId userId) {
        BasicDBObject query = new BasicDBObject().append("uid", userId).append("r", Constant.ZERO);
        BasicDBObject orderBy = new BasicDBObject().append("rl", -1).append(Constant.ID, -1);
        List<ObjectId> memberEntries = new ArrayList<ObjectId>();
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query, Constant.FIELDS, orderBy);
        for (DBObject dbo : dbObjects) {
            MemberEntry memberEntry = new MemberEntry(dbo);
            memberEntries.add(memberEntry.getGroupId());
        }
        return memberEntries;
    }


    /**
     * 查询该用户具有管理员权限的群组列表
     * @param userId
     * @return
     */
    public List<ObjectId> getManagerGroupIdsByUserId(ObjectId userId) {
        List<Integer> roles=new ArrayList<Integer>();
        roles.add(Constant.ONE);
        roles.add(Constant.TWO);
        BasicDBObject query = new BasicDBObject().append("uid", userId).append("rl", new BasicDBObject(Constant.MONGO_IN,roles)).append("r", Constant.ZERO);
        BasicDBObject orderBy = new BasicDBObject().append("rl", -1).append(Constant.ID, -1);
        List<ObjectId> memberEntries = new ArrayList<ObjectId>();
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query, Constant.FIELDS, orderBy);
        for (DBObject dbo : dbObjects) {
            MemberEntry memberEntry = new MemberEntry(dbo);
            memberEntries.add(memberEntry.getGroupId());
        }
        return memberEntries;
    }


    public boolean  judgeIsParent(ObjectId userId){
            BasicDBObject query = new BasicDBObject().append("uid", userId).append("rl", Constant.ZERO).append("r", Constant.ZERO);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query)>0;
    }


    public boolean judgeManagePermissionOfUser(ObjectId userId){
        List<Integer> roles=new ArrayList<Integer>();
        roles.add(Constant.ONE);
        roles.add(Constant.TWO);
        BasicDBObject query = new BasicDBObject().append("uid", userId).append("rl", new BasicDBObject(Constant.MONGO_IN,roles)).append("r", Constant.ZERO);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_MEMBER, query)>0;
    }

}
