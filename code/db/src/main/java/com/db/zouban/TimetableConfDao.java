package com.db.zouban;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.pojo.zouban.IdNamePair;
import com.pojo.zouban.PointEntry;
import com.pojo.zouban.TimetableConfEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by wangkaidong on 2016/7/26.
 */
public class TimetableConfDao extends BaseDao {
    /**
     * 添加课表配置
     *
     * @param entry
     * @return
     */
    public ObjectId add(TimetableConfEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_TIMETABLE_CONF, entry.getBaseEntry());
        return entry.getID();
    }

    /**
     * 添加不可排课事务
     *
     * @param term
     * @param gradeId
     * @param event
     */
    public void addEvent(String term, ObjectId gradeId, TimetableConfEntry.Event event) {
        BasicDBObject query = new BasicDBObject("te", term).append("gid", gradeId);
        BasicDBObject updateValue = new BasicDBObject("evn", event.getBaseEntry());
        BasicDBObject update = new BasicDBObject(Constant.MONGO_PUSH, updateValue);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_TIMETABLE_CONF, query, update);
    }

    /**
     * 添加班级事务(年级及全校的维度)
     *
     * @param term
     * @param gradeId
     * @param event
     */
    public void addClassEvent(String term, ObjectId gradeId, TimetableConfEntry.ClassEvent event) {
        BasicDBObject query = new BasicDBObject("te", term).append("gid", gradeId);
        BasicDBObject updateValue = new BasicDBObject("clvn", event.getBaseEntry());
        BasicDBObject update = new BasicDBObject(Constant.MONGO_PUSH, updateValue);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_TIMETABLE_CONF, query, update);
    }

    /**
     * 更新不可排课事务
     *
     * @param term
     * @param gradeId
     * @param event
     */
    public void updateEvent(String term, ObjectId gradeId, TimetableConfEntry.Event event) {
        BasicDBObject query = new BasicDBObject("te", term)
                .append("gid", gradeId)
                .append("evn._id", event.getID());
        BasicDBObject updateValue = new BasicDBObject("evn.$.nm", event.getName())
                .append("evn.$.pl", MongoUtils.convert(MongoUtils.fetchDBObjectList(event.getPointList())))
                .append("evn.$.tl", MongoUtils.convert(MongoUtils.fetchDBObjectList(event.getTeacherList())));
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, updateValue);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_TIMETABLE_CONF, query, update);
    }


    /**
     * 删除事务
     *
     * @param term
     * @param eventName
     * @param pointList
     * @param teacherList
     */
    public void removeEvent(String term, String eventName, List<PointEntry> pointList, List<IdNamePair> teacherList) {
        BasicDBObject query = new BasicDBObject("te", term);
        BasicDBObject updateValue = new BasicDBObject("nm", eventName)
                .append("pl", MongoUtils.convert(MongoUtils.fetchDBObjectList(pointList)))
                .append("tl", MongoUtils.convert(MongoUtils.fetchDBObjectList(teacherList)));
        BasicDBObject update = new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("evn", updateValue));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_TIMETABLE_CONF, query, update);
    }

    /**
     * 删除班级事务
     *
     * @param term
     * @param gradeId
     */
    public void removeClassEvent(String term, ObjectId gradeId, int x ,int y) {
        BasicDBObject query = new BasicDBObject("te", term).append("gid", gradeId);
        BasicDBObject updateValue = new BasicDBObject()
                .append("x", x)
                .append("y", y);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("clvn", updateValue));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_TIMETABLE_CONF, query, update);
    }

    /**
     * 删除全校事务
     *
     * @param term
     * @param gradeIdList
     */
    public void removeAllEvent(String term, List<ObjectId> gradeIdList) {
        BasicDBObject query = new BasicDBObject("te", term)
                .append("gid", new BasicDBObject(Constant.MONGO_IN, gradeIdList));
        BasicDBObject updateValue = new BasicDBObject("evn", MongoUtils.convert(MongoUtils.fetchDBObjectList(
                new ArrayList<TimetableConfEntry.Event>())));
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, updateValue);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_TIMETABLE_CONF, query, update);
    }



    /**
     * 锁定/解锁课表配置
     *
     * @param term
     * @param gradeId
     * @param lock
     */
    public void lock(String term, ObjectId gradeId, int lock) {
        BasicDBObject query = new BasicDBObject("te", term).append("gid", gradeId);
        BasicDBObject updateValue = new BasicDBObject("lock", lock);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, updateValue);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_TIMETABLE_CONF, query, update);
    }

    /**
     * 是否锁定
     *
     * @param term
     * @param gradeId
     * @return
     */
    public boolean isLocked(String term, ObjectId gradeId) {
        BasicDBObject query = new BasicDBObject("te", term).append("gid", gradeId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_TIMETABLE_CONF, query, new BasicDBObject("lock", 1));
        TimetableConfEntry entry = new TimetableConfEntry((BasicDBObject) dbObject);

        return entry.getLock() == 1;
    }


    /**
     * 获取课表配置
     *
     * @param term
     * @param gradeId
     * @return
     */
    public TimetableConfEntry getTimetableConf(String term, ObjectId gradeId) {
        BasicDBObject query = new BasicDBObject("te", term).append("gid", gradeId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_TIMETABLE_CONF, query, Constant.FIELDS);
        if (dbObject != null) {
            return new TimetableConfEntry((BasicDBObject) dbObject);
        } else {
            return null;
        }
    }

    /**
     * 查询事务
     *
     * @param term
     * @param eventId
     * @return
     */
    public TimetableConfEntry findConfByEventId(String term, ObjectId eventId) {
        BasicDBObject query = new BasicDBObject("te", term)
                .append("evn._id", eventId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_TIMETABLE_CONF, query, Constant.FIELDS);
        if (dbObject != null) {
            return new TimetableConfEntry((BasicDBObject) dbObject);
        } else {
            return null;
        }
    }

    /**
     * 查询事务
     *
     * @param term
     * @param gradeId
     * @param eventName
     * @param pointList
     * @param teacherList
     * @return
     */
    public TimetableConfEntry findConfByEvent(String term, ObjectId gradeId, String eventName,
                                              List<PointEntry> pointList, List<IdNamePair> teacherList) {
        BasicDBObject query = new BasicDBObject("te", term)
                .append("gid", gradeId)
                .append("evn", new BasicDBObject(Constant.MONGO_ELEMATCH, new BasicDBObject()
                        .append("nm", eventName)
                        .append("pl", MongoUtils.convert(MongoUtils.fetchDBObjectList(pointList)))
                        .append("tl", MongoUtils.convert(MongoUtils.fetchDBObjectList(teacherList)))));
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_TIMETABLE_CONF, query, Constant.FIELDS);
        if (dbObject != null) {
            return new TimetableConfEntry((BasicDBObject) dbObject);
        } else {
            return null;
        }
    }
}
