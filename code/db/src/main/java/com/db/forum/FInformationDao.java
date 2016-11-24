package com.db.forum;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.pojo.forum.FInformationEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/7/6.
 */
public class FInformationDao extends BaseDao {

    /**
     * 保存消息
     **/
    public ObjectId addFInformation(FInformationEntry fInformationEntry) {
        save(fInformationEntry.getBaseEntry());
        return fInformationEntry.getID();
    }

    /**
     * 发送系统消息
     **/
    public int sendSystemMessage(ObjectId uid, String content) {
        long time = System.currentTimeMillis();
        FInformationEntry entry = new FInformationEntry(uid, time, content.trim());
        return save(entry.getBaseEntry());
    }

    public void sendSystemMessage(List<ObjectId> uids, String content) {
        long time = System.currentTimeMillis();
        List<DBObject> infos = new ArrayList<DBObject>();
        for (ObjectId uid : uids) {
            FInformationEntry entry = new FInformationEntry(uid, time, content.trim());
            infos.add(entry.getBaseEntry());
        }
        save(getDB(), getCollection(), infos);
    }

    /**
     * 获取系统消息
     **/
    public List<FInformationEntry> getSystemInformation(ObjectId uid) {
        List<FInformationEntry> fInformationEntries = new ArrayList<FInformationEntry>();
        BasicDBObject query = new BasicDBObject()
                .append("psid", uid)
                .append("ty", 1);
        List<DBObject> dbObjectList = queryList(query, Constant.MONGO_SORTBY_DESC);
        for (DBObject dbObject : dbObjectList) {
            fInformationEntries.add(new FInformationEntry((BasicDBObject) dbObject));
        }
        return fInformationEntries;
    }

    /**
     * 获取未读系统消息个数
     **/
    public int getUnreadSystemCount(ObjectId personId) {
        BasicDBObject query = new BasicDBObject()
                .append("psid", personId)
                .append("ty", 1)
                .append("sc", 0);
        return count(query);
    }

    /**
     * 获取消息
     **/
    public List<FInformationEntry> getMessages(ObjectId userId, ObjectId personId, int type) {
        List<FInformationEntry> fInformationEntries = new ArrayList<FInformationEntry>();
        List<ObjectId> condition = new ArrayList<ObjectId>();
        condition.add(userId);
        condition.add(personId);
        BasicDBObject query = new BasicDBObject();
        BasicDBObject sort = new BasicDBObject();
        sort.append(Constant.ID, Constant.ASC);
        if (null != userId) {
            query.append("uid", new BasicDBObject(Constant.MONGO_IN, condition));
        }
        if (null != personId) {
            query.append("psid", new BasicDBObject(Constant.MONGO_IN, condition));
        }
        if (type != -1) {
            query.append("ty", type);
        }
        List<DBObject> dbObjectList = find(getDB(), getCollection(), query, Constant.FIELDS, sort);

        for (DBObject dbObject : dbObjectList) {
            fInformationEntries.add(new FInformationEntry((BasicDBObject) dbObject));
        }

        return fInformationEntries;
    }

    /**
     * 获取第一条消息
     **/
    public List<FInformationEntry> getInformationFirst(ObjectId userId, int type) {
        List<FInformationEntry> fInformationEntries = new ArrayList<FInformationEntry>();
        List<FInformationEntry> returnData = new ArrayList<FInformationEntry>();
        List<DBObject> dbs=new ArrayList<DBObject>();
        BasicDBObject query = new BasicDBObject("psid", userId).append("ty", type);
        BasicDBObject query1 = new BasicDBObject("uid", userId).append("ty", type);
        List<DBObject> dbObjectList = find(getDB(), getCollection(), query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        List<DBObject> dbObjectList1 = find(getDB(), getCollection(), query1, Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        dbs.addAll(dbObjectList);
        dbs.addAll(dbObjectList1);
        for (DBObject dbObject : dbs) {
            fInformationEntries.add(new FInformationEntry((BasicDBObject) dbObject));
        }
        if (fInformationEntries.size() > 0) {
            returnData.add(fInformationEntries.get(0));
            for (FInformationEntry fInformationEntry : fInformationEntries) {
                boolean flag = true;
                for (FInformationEntry item : returnData) {
                    if (item.getUserId().equals(fInformationEntry.getUserId()) && item.getPersonId().equals(fInformationEntry.getPersonId())) {
                        flag = false;
                        break;
                    } else if(item.getUserId().equals(fInformationEntry.getPersonId()) && item.getPersonId().equals(fInformationEntry.getUserId())){
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    returnData.add(fInformationEntry);
                }
            }
        }
        return returnData;
    }

    public List<FInformationEntry> getInformationUnRead(ObjectId userId) {
        List<FInformationEntry> fInformationEntries = new ArrayList<FInformationEntry>();
        BasicDBObject query = new BasicDBObject("psid", userId).append("sc", 0);
        List<DBObject> dbObjectList = find(getDB(), getCollection(), query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        for (DBObject dbObject : dbObjectList) {
            fInformationEntries.add(new FInformationEntry((BasicDBObject) dbObject));
        }
        return fInformationEntries;
    }

    //清除读过的消息
    public void clearReadedMessage(ObjectId userId) {
        BasicDBObject query = new BasicDBObject("psid", userId).append("sc", 0);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("sc", 1));
        update(query, update);
    }

    /**
     * 清除读过的系统消息
     **/
    public void clearReadedSysMessage(ObjectId userId) {
        BasicDBObject query = new BasicDBObject("psid", userId)
                .append("sc", 0)
                .append("ty", 1);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("sc", 1));
        update(query, update);
    }

    //保存点赞消息
    public void saveZanInformation(ObjectId uid, ObjectId psid, ObjectId postId) {
        FInformationEntry entry = new FInformationEntry(uid, psid, 2, System.currentTimeMillis(), 0, postId);
        save(entry.getBaseEntry());
    }

    //保存回复消息
    public void saveReplyInformation(ObjectId uid, ObjectId psid, ObjectId postId, ObjectId replyId) {
        FInformationEntry entry = new FInformationEntry(uid, psid, 3, System.currentTimeMillis(), 0, postId, replyId);
        save(entry.getBaseEntry());
    }

    //删除点赞消息
    public void deleteZanInformation(ObjectId uid, ObjectId psid, ObjectId postId) {
        BasicDBObject query = new BasicDBObject();
        query.append("uid", uid).append("psid", psid).append("ptid", postId);
        delete(query);
    }


    /**
     * 删除消息
     **/
    public void removeFInformation(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        delete(query);
    }

    /**
     * 删除消息
     **/
    public void remove(ObjectId userId, ObjectId personId) {
        List<ObjectId> ids=new ArrayList<ObjectId>();
        ids.add(userId);
        ids.add(personId);
        BasicDBObject query = new BasicDBObject();
        if (null != userId) {
            query.append("uid", new BasicDBObject(Constant.MONGO_IN, ids));
        }
        if (null != personId) {
            query.append("psid", new BasicDBObject(Constant.MONGO_IN, ids));
        }
        delete(query);
    }


    /**
     * 更新消息浏览
     *
     * @param userId
     * @param personId
     */
    public void updateScan(ObjectId userId, ObjectId personId, int type) {
        BasicDBObject query = new BasicDBObject();
        if (null != userId) {
            query.append("uid", userId);
        }
        if (null != personId) {
            query.append("psid", personId);
        }
        if (type != -1) {
            query.append("ty", type);
        }
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("sc", 1));
        update(query, updateValue);
    }

    /**
     * 收到消息请求的数量
     *
     * @param personId
     * @param type
     * @param scan
     * @return
     */
    public int getCount(ObjectId personId, int type, int scan) {
        BasicDBObject query = new BasicDBObject();
        if (null != personId) {
            query.append("psid", personId);
        }
        if (type != -1) {
            query.append("ty", type);
        }
        if (scan != -1) {
            query.append("sc", scan);
        }
        return count(query);
    }

    public int getFInformationCount(ObjectId userId, ObjectId personId) {
        BasicDBObject query = new BasicDBObject();
        query.append("ty", 0);
        List<ObjectId> condition = new ArrayList<ObjectId>();
        condition.add(userId);
        condition.add(personId);
        BasicDBObject sort = new BasicDBObject();
        sort.append(Constant.ID, Constant.ASC);
        if (null != userId) {
            query.append("uid", new BasicDBObject(Constant.MONGO_IN, condition));
        }
        if (null != personId) {
            query.append("psid", new BasicDBObject(Constant.MONGO_IN, condition));
        }
        return count(query);
    }

    private DBObject query(DBObject query) {
        return findOne(getDB(), getCollection(), query, Constant.FIELDS);
    }

    private List<DBObject> queryList(DBObject query) {
        return find(getDB(), getCollection(), query, Constant.FIELDS);
    }

    private List<DBObject> queryList(DBObject query, BasicDBObject sort) {
        return find(getDB(), getCollection(), query, Constant.FIELDS, sort);
    }

    private int save(DBObject entry) {
        WriteResult writeResult = save(getDB(), getCollection(), entry);
        return writeResult.getN();
    }

    private int update(DBObject query, DBObject update) {
        WriteResult writeResult = update(getDB(), getCollection(), query, update);
        return writeResult.getN();
    }

    private int delete(DBObject query) {
        WriteResult writeResult = remove(getDB(), getCollection(), query);
        return writeResult.getN();
    }

    private int count(DBObject query) {
        return count(getDB(), getCollection(), query);
    }

    private String getCollection() {
        return Constant.COLLECTION_FORUM_INFORMATION;
    }
}
