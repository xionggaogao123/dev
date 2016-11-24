package com.db.zouban;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.zouban.EventConflictEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangkaidong on 2016/10/10.
 * <p/>
 * 事务冲突DAO
 */
public class EventConflictDao extends BaseDao {
    /**
     * 添加冲突
     *
     * @param eventConflictEntry
     * @return
     */
    public ObjectId addEventConflict(EventConflictEntry eventConflictEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_CONFLICT, eventConflictEntry.getBaseEntry());
        return eventConflictEntry.getID();
    }

    /**
     * 根据老师列表删除冲突
     *
     * @param x
     * @param y
     * @param teacherIdList
     */
    public void removeEventConflictByTeacherList(String term, ObjectId gradeId, int x, int y, List<ObjectId> teacherIdList) {
        BasicDBObject query = new BasicDBObject("te", term)
                .append("gid", gradeId)
                .append("x", x)
                .append("y", y)
                .append("tid", new BasicDBObject(Constant.MONGO_IN, teacherIdList));
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_CONFLICT, query);
    }


    /**
     * 根据课程列表删除冲突
     *
     * @param x
     * @param y
     * @param courseIdList
     */
    public void removeEventConflictByCourseList(String term, ObjectId gradeId, int x, int y, List<ObjectId> courseIdList) {
        BasicDBObject query = new BasicDBObject("te", term)
                .append("gid", gradeId)
                .append("x", x)
                .append("y", y)
                .append("cid", new BasicDBObject(Constant.MONGO_IN, courseIdList));
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_CONFLICT, query);
    }

    /**
     * 根据事务Id删除冲突
     * @param eventId
     */
    public void removeEventConflictByEventId(ObjectId eventId){
        BasicDBObject query = new BasicDBObject("eid", eventId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_CONFLICT, query);
    }

    /**
     * 根据课程idList删除相关的冲突
     * @param courseIdList
     */
    public void removeEventConfictByCourseIdList(List<ObjectId> courseIdList){
        BasicDBObject query = new BasicDBObject("cid", new BasicDBObject(Constant.MONGO_IN,courseIdList));
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_CONFLICT, query);
    }

    /**
     * 删除某天或某节的事务冲突
     *
     * @param term
     * @param gradeId
     * @param x
     * @param y
     */
    public void removeEventConflictByXY(String term, ObjectId gradeId, int x, int y) {
        BasicDBObject query = new BasicDBObject("te", term)
                .append("gid", gradeId);
        if (x != -1) {
            query.append("x", x);
        }
        if (y != -1) {
            query.append("y", y);
        }
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_CONFLICT, query);
    }


    /**
     * 查询本学期本年级全部冲突
     *
     * @param term
     * @param gradeId
     * @return
     */
    public List<EventConflictEntry> findEventConflict(String term, ObjectId gradeId) {
        BasicDBObject query = new BasicDBObject("te", term).append("gid", gradeId);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_CONFLICT, query, Constant.FIELDS);
        List<EventConflictEntry> eventConflictEntries = new ArrayList<EventConflictEntry>();
        for (DBObject dbObject : dbObjectList) {
            eventConflictEntries.add(new EventConflictEntry((BasicDBObject) dbObject));
        }
        return eventConflictEntries;
    }
}
