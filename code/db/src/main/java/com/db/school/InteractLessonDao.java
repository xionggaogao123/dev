package com.db.school;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.school.InteractLessonEntry;
import com.pojo.utils.DeleteState;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 互动课堂操作类
 *
 * @author fourer
 */
public class InteractLessonDao extends BaseDao {

    /**
     * 增加
     *
     * @param e
     * @return
     */
    public ObjectId addInteractLessonEntry(InteractLessonEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_INTERACTLESSON,
                e.getBaseEntry());
        return e.getID();
    }

    /**
     * 获取课的列表
     *
     * @param csid
     * @param lock
     * @return
     */
    public List<InteractLessonEntry> getInteractLessonList(
            ObjectId csid, int lock, int skip, int limit) {
        List<InteractLessonEntry> retList = new ArrayList<InteractLessonEntry>();
        BasicDBObject dbo = new BasicDBObject();
        dbo.append("st", DeleteState.NORMAL.getState());
        if (null != csid) {
            dbo.append("tcsid", csid);
        }
        if (Constant.NEGATIVE_ONE != lock) {
            dbo.append("lock", lock);
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(),
                Constant.COLLECTION_INTERACTLESSON, dbo, Constant.FIELDS,
                Constant.MONGO_SORTBY_DESC, skip, limit);
        if (null != list && !list.isEmpty()) {
            InteractLessonEntry e = null;
            for (DBObject dbo1 : list) {
                e = new InteractLessonEntry((BasicDBObject) dbo1);
                retList.add(e);
            }
        }
        return retList;
    }

    /**
     * 根据视频Id查询
     *
     * @param videoId
     * @return
     */
    public InteractLessonEntry getInteractLessonEntryByVideoId(ObjectId videoId) {
        BasicDBObject query = new BasicDBObject("vi", videoId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(),
                Constant.COLLECTION_INTERACTLESSON, query, Constant.FIELDS);
        if (null != dbo) {
            InteractLessonEntry e = new InteractLessonEntry((BasicDBObject) dbo);
            return e;
        }
        return null;
    }

    /**
     * 删除
     *
     * @param id
     */
    public void removeInteractLessonEntry(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_INTERACTLESSON,
                query);
    }

    /**
     * 推动一个课程
     */
    public void pushInteractLesson(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("pu", Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_INTERACTLESSON, query, updateValue);
    }

    public void updInteractLessonEntry(InteractLessonEntry e) {
        BasicDBObject query = new BasicDBObject(Constant.ID, e.getID());
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject(e.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_INTERACTLESSON, query, update);
    }

    public InteractLessonEntry getInteractLessonEntryById(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_INTERACTLESSON, query, Constant.FIELDS);
        if (null != dbo) {
            InteractLessonEntry e = new InteractLessonEntry((BasicDBObject) dbo);
            return e;
        }
        return null;
    }

    /**
     * @param userId
     * @param schoolId
     * @param classId
     * @param push     是否推送
     * @return
     */
    public List<InteractLessonEntry> getInteractLessonEntryList(
            ObjectId userId, ObjectId schoolId, ObjectId classId, int push) {
        List<InteractLessonEntry> retList = new ArrayList<InteractLessonEntry>();
        BasicDBObject dbo = new BasicDBObject();
        if (null != userId) {
            dbo.append("ui", userId);
        }
        if (null != schoolId) {
            dbo.append("si", schoolId);
        }
        if (null != classId) {
            dbo.append("cid", classId);
        }
        if (Constant.NEGATIVE_ONE != push) {
            dbo.append("pu", push);
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(),
                Constant.COLLECTION_INTERACTLESSON, dbo, Constant.FIELDS,
                Constant.MONGO_SORTBY_DESC);
        if (null != list && !list.isEmpty()) {
            InteractLessonEntry e = null;
            for (DBObject dbo1 : list) {
                e = new InteractLessonEntry((BasicDBObject) dbo1);
                retList.add(e);
            }
        }
        return retList;
    }

    public void updateInteractLessonEntry(InteractLessonEntry entry) {
        BasicDBObject query = new BasicDBObject(Constant.ID, entry.getID());
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject(entry.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_INTERACTLESSON, query, update);
    }

    /**
     * @param csid
     * @param lock
     * @return
     */
    public int getInteractLessonCount(ObjectId csid, int lock) {
        BasicDBObject dbo = new BasicDBObject();
        dbo.append("st", DeleteState.NORMAL.getState());
        if (null != csid) {
            dbo.append("tcsid", csid);
        }
        if (Constant.NEGATIVE_ONE != lock) {
            dbo.append("lock", lock);
        }
        return count(MongoFacroty.getAppDB(),
                Constant.COLLECTION_INTERACTLESSON, dbo);
    }

    /**
     * @param cids
     * @param tcsids
     * @return
     */
    public int getInteractLessonCountByParam(List<ObjectId> cids, List<ObjectId> tcsids) {
        BasicDBObject dbo = new BasicDBObject();
        dbo.append("st", DeleteState.NORMAL.getState());
        if (cids.size() > 0) {
            dbo.append("cid", new BasicDBObject(Constant.MONGO_IN, cids));
        }
        if (tcsids.size() > 0) {
            dbo.append("tcsid", new BasicDBObject(Constant.MONGO_IN, tcsids));
        }
        dbo.append("lock", Constant.ZERO);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_INTERACTLESSON, dbo);
    }

    /**
     * 获取课的列表
     *
     * @param cids
     * @param tcsids
     * @return
     */
    public List<InteractLessonEntry> getInteractLessonListByParam(List<ObjectId> cids, List<ObjectId> tcsids, int skip, int limit) {
        List<InteractLessonEntry> retList = new ArrayList<InteractLessonEntry>();
        BasicDBObject dbo = new BasicDBObject();
        dbo.append("st", DeleteState.NORMAL.getState());
        if (cids.size() > 0) {
            dbo.append("cid", new BasicDBObject(Constant.MONGO_IN, cids));
        }
        if (tcsids.size() > 0) {
            dbo.append("tcsid", new BasicDBObject(Constant.MONGO_IN, tcsids));
        }
        dbo.append("lock", Constant.ZERO);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_INTERACTLESSON, dbo, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, skip, limit);
        if (null != list && !list.isEmpty()) {
            InteractLessonEntry e = null;
            for (DBObject dbo1 : list) {
                e = new InteractLessonEntry((BasicDBObject) dbo1);
                retList.add(e);
            }
        }
        return retList;
    }
}
