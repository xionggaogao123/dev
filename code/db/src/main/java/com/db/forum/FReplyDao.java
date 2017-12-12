package com.db.forum;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.pojo.forum.FReplyEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/5/31.
 */
public class FReplyDao extends BaseDao {

    /**
     * 添加回帖
     *
     * @param e
     * @return
     */
    public ObjectId addFReply(FReplyEntry e) {
        save(MongoFacroty.getAppDB(), getCollection(), e.getBaseEntry());
        ObjectId postId = e.getPostId();
        BasicDBObject query = new BasicDBObject(Constant.ID, postId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("upt", System.currentTimeMillis()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_POST, query, updateValue);
        return e.getID();
    }

    /**
     * 保存楼层
     * @param fReplyEntry
     */
    public void saveFReplyEntryForFloor(FReplyEntry fReplyEntry){
        save(MongoFacroty.getAppDB(), getCollection(), fReplyEntry.getBaseEntry());
    }

    /**
     * 更新回复
     */
    public void updateReplyList(FReplyEntry e, boolean flag, ObjectId replyId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, replyId);
        try {
            BasicDBObject updateValue = new BasicDBObject();
            if (flag) {
                updateValue.append(Constant.MONGO_PUSH, new BasicDBObject("rl", e.getRepliesList().get(0)));
            } else {
                updateValue.append(Constant.MONGO_SET, new BasicDBObject("rl", e.getRepliesList().get(0)));
            }
            update(MongoFacroty.getAppDB(), getCollection(), query, updateValue);
        } catch (Exception e1) {
            e1.printStackTrace();
            System.out.print(System.currentTimeMillis());
        }

    }


    public List<FReplyEntry> getFReplyByCondition(DBObject fields, DBObject sort, ObjectId personId,
                                                  int repliesToReply, int skip, int limit) {
        List<FReplyEntry> retList = new ArrayList<FReplyEntry>();

        BasicDBObject query = new BasicDBObject();
        if (personId != null) {
            query.append("psid", personId);
        }
        if (repliesToReply != -1) {
            query.append("rfr", repliesToReply);
        }
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), getCollection(), query, fields, sort, skip, limit);

        for (DBObject dbObject : dbObjectList) {
            retList.add(new FReplyEntry((BasicDBObject) dbObject));
        }
        return retList;
    }

    public List<FReplyEntry> getFReplyByPerson(DBObject fields, DBObject sort, ObjectId personId, int repliesToReply) {
        List<FReplyEntry> retList = new ArrayList<FReplyEntry>();

        BasicDBObject query = new BasicDBObject();
        if (personId != null) {
            query.append("psid", personId);
        }
        if (repliesToReply != -1) {
            query.append("rfr", repliesToReply);
        }
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), getCollection(), query, fields, sort);
        for (DBObject dbObject : dbObjectList) {
            retList.add(new FReplyEntry((BasicDBObject) dbObject));
        }
        return retList;
    }

    /**
     * 获取回帖（分页）
     *
     * @param fields
     * @param sort
     * @param postSectionId
     * @param postId
     * @param skip
     * @param limit
     * @return
     */
    public List<FReplyEntry> getFReplyEntries(int floor, DBObject fields, DBObject sort, ObjectId postSectionId, ObjectId postId,
                                              ObjectId personId, ObjectId replyPostId, int repliesToReply, int skip, int limit) {
        List<FReplyEntry> retList = new ArrayList<FReplyEntry>();
        BasicDBObject query = new BasicDBObject();
        if (postId != null) {
            query.append("ptid", postId);
        }
        if (personId != null) {
            query.append("psid", personId);
        }
        if (repliesToReply != -1) {
            query.append("rfr", repliesToReply);
        }
        if (replyPostId != null) {
            query.append("rpid", replyPostId);
        }
        if (floor != -1) {
            query.append("fl", floor);
        }
        query.append("rm", new BasicDBObject(Constant.MONGO_NOTIN, new int[]{1}));
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), getCollection(), query, fields, sort, skip, limit);

        for (DBObject dbObject : dbObjectList) {
            retList.add(new FReplyEntry(dbObject));
        }
        return retList;
    }


    public int getMaxFloor(ObjectId postId) {
        List<FReplyEntry> retList = new ArrayList<FReplyEntry>();
        BasicDBObject query = new BasicDBObject();
        if (postId != null) {
            query.append("ptid", postId);
        }
        query.append("rm", new BasicDBObject(Constant.MONGO_NOTIN, new int[]{1}));
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), getCollection(), query, Constant.FIELDS, new BasicDBObject("_id", -1), 1, 1);

        for (DBObject dbObject : dbObjectList) {
            retList.add(new FReplyEntry(dbObject));
        }
        if (retList.size() > 0) {
            return retList.get(0).getFloor();
        } else {
            return 0;
        }

    }

    public List<FReplyEntry> getFloor(ObjectId postId) {
        List<FReplyEntry> retList = new ArrayList<FReplyEntry>();
        BasicDBObject query = new BasicDBObject();
        if (postId != null) {
            query.append("ptid", postId);
        }
        query.append("rm", new BasicDBObject(Constant.MONGO_NOTIN, new int[]{1}));
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), getCollection(), query, Constant.FIELDS, new BasicDBObject("_id", -1));
        for (DBObject dbObject : dbObjectList) {
            retList.add(new FReplyEntry((BasicDBObject) dbObject));
        }
        return retList;
    }

    /**
     * 查帖子数量
     *
     * @param postId
     * @param personId
     * @param replyPostId
     * @param repliesToReply 楼中楼标志
     * @return
     */
    public int getFReplyEntriesCount(ObjectId postId, ObjectId personId, ObjectId replyPostId, int repliesToReply, int remove) {

        BasicDBObject query = new BasicDBObject();
        if (postId != null) {
            query.append("ptid", postId);
        }
        if (personId != null) {
            query.append("psid", personId);
        }
        if (replyPostId != null) {
            query.append("rpid", replyPostId);
        }
        if (repliesToReply != -1) {
            query.append("rfr", repliesToReply);
        }
        if (remove != -1) {
            query.append("rm", new BasicDBObject(Constant.MONGO_NOTIN, new int[]{1}));
        }
        return count(query);
    }

    /**
     * 根据postID（发帖Id)来删除回帖
     */
    public void removeFReplyByPostId(ObjectId postId) {
        BasicDBObject query = new BasicDBObject("ptid", postId);
        BasicDBObject updateValue = new BasicDBObject();
        updateValue.append(Constant.MONGO_SET, new BasicDBObject("rm", 1));
        update(MongoFacroty.getAppDB(), getCollection(), query, updateValue);
    }

    /**
     * 逻辑删除
     */
    public void removeReplyLogic(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject updateValue = new BasicDBObject();
        updateValue.append(Constant.MONGO_SET, new BasicDBObject("rm", 1));
        update(MongoFacroty.getAppDB(), getCollection(), query, updateValue);
    }

    /**
     * 回帖详情
     *
     * @param id
     * @return
     */
    public FReplyEntry getFReplyEntry(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), getCollection(), query, Constant.FIELDS);
        if (null != dbo) {
            return new FReplyEntry((BasicDBObject) dbo);
        }
        return null;
    }


    /**
     * 获取板块的总帖数
     *
     * @param postSectionId
     * @return
     */
    public int getFReplyCount(ObjectId postSectionId, String name) {
        BasicDBObject query = new BasicDBObject();
        if (postSectionId != null) {
            query.append("pstid", postSectionId);
        }
        if (!"".equals(name)) {
            query.append("nm", name);
        }
        return count(query);
    }

    /**
     * 根据楼层获取时间
     **/
    public long timeText(ObjectId postId, int flooor) {
        BasicDBObject query = new BasicDBObject()
                .append("ptid", postId)
                .append("fl", flooor);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), getCollection(), query, Constant.FIELDS);
        if (dbObject != null) {
            FReplyEntry entry = new FReplyEntry(dbObject);
            return entry.getTime();
        }
        return 0;
    }

    /**
     * 更新点赞
     */
    public void updateBtnZan(ObjectId userReplyId, ObjectId replyId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, replyId);
        BasicDBObject updateValue = new BasicDBObject();
        Long updateTime = System.currentTimeMillis();
        updateValue.append(Constant.MONGO_ADDTOSET, new BasicDBObject("url", userReplyId)).append(Constant.MONGO_INC, new BasicDBObject("prc", 1))
                .append(Constant.MONGO_SET, new BasicDBObject("upt", updateTime));
        update(MongoFacroty.getAppDB(), getCollection(), query, updateValue);
    }


    public void removeBtnZan(ObjectId userReplyId, ObjectId replyId){
        BasicDBObject query = new BasicDBObject(Constant.ID, replyId);
        BasicDBObject updateValue = new BasicDBObject();
        Long updateTime = System.currentTimeMillis();
        updateValue.append(Constant.MONGO_PULL, new BasicDBObject("url", userReplyId)).append(Constant.MONGO_INC, new BasicDBObject("prc", Constant.NEGATIVE_ONE))
                .append(Constant.MONGO_SET, new BasicDBObject("upt", updateTime));
        update(MongoFacroty.getAppDB(), getCollection(), query, updateValue);
    }

    /**
     * 查询排行榜
     */
    public List<FReplyEntry> getFReplyListRank(ObjectId postId) {
        List<FReplyEntry> retList = new ArrayList<FReplyEntry>();
        BasicDBObject query = new BasicDBObject();
        if (postId != null) {
            query.append("ptid", postId);
        }
        query.append("rm", new BasicDBObject(Constant.MONGO_NOTIN, new int[]{1}));
        BasicDBObject sort = new BasicDBObject();
        sort.append("prc", -1);
        sort.append("upt", -1);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), getCollection(), query, Constant.FIELDS, sort);
        for (DBObject dbObject : dbObjectList) {
            retList.add(new FReplyEntry((BasicDBObject) dbObject));
        }
        return retList;
    }

    /**
     * 查询自己的点赞数
     */
    public int getPraiseCount(ObjectId postId, ObjectId personId) {
        List<FReplyEntry> retList = new ArrayList<FReplyEntry>();
        BasicDBObject query = new BasicDBObject();
        if (postId != null) {
            query.append("ptid", postId);
        }
        if (personId != null) {
            query.append("psid", personId);
        }
        BasicDBObject sort = new BasicDBObject();
        sort.append("prc", -1);
        sort.append("upt", -1);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), getCollection(), query, Constant.FIELDS, sort);
        for (DBObject dbObject : dbObjectList) {
            retList.add(new FReplyEntry((BasicDBObject) dbObject));
        }
        if (retList.size() > 0) {
            return retList.get(0).getPraiseCount();
        } else {
            return 0;
        }
    }

    /**
     * 删除楼中楼回帖
     * @param rpid 帖子或者回复id
     * @param lolId 楼中楼id
     * @return
     */
    public int deleteLol(ObjectId rpid,ObjectId lolId){
        BasicDBObject query = new BasicDBObject()
                .append("rpid",rpid);
        BasicDBObject pullSpec = new BasicDBObject()
                .append("id",lolId);
        BasicDBObject pull = new BasicDBObject()
                .append("rl",pullSpec);
        BasicDBObject update = new BasicDBObject()
                .append(Constant.MONGO_PULL, pull);
        update(MongoFacroty.getAppDB(), getCollection(), query, update);
        return 0;
    }

    /**
     * 获取回复 {点赞数递增}
     * @param postId {帖子id}
     * @return
     */
    public List<FReplyEntry> getReplyList(ObjectId postId){
        List<FReplyEntry> retList = new ArrayList<FReplyEntry>();
        BasicDBObject query = new BasicDBObject()
                .append("ptid",postId);
        BasicDBObject sort = new BasicDBObject()
                .append("prc",-1);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), getCollection(), query, Constant.FIELDS, sort);
        for (DBObject dbObject : dbObjectList) {
            retList.add(new FReplyEntry(dbObject));
        }
        return retList;
    }


    private int count(BasicDBObject query) {
        return count(MongoFacroty.getAppDB(), getCollection(), query);
    }

    private String getCollection() {
        return Constant.COLLECTION_FORUM_REPLY;
    }

}
