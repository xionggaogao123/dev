package com.db.classroom;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.classroom.ClassroomEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qiangm on 2015/10/10.
 */
public class ClassroomDao extends BaseDao {
    /**
     * 增加一个教室
     *
     * @param classroomEntry
     */
    public void addClassRoom(ClassroomEntry classroomEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASSROOM, classroomEntry.getBaseEntry());
    }

    /**
     * 根据学校id获取所有的班级entry列表
     *
     * @param schoolId
     * @return
     */
    public List<ClassroomEntry> findClassRoomEntryList(ObjectId schoolId, int page, int pageSize) {
        List<ClassroomEntry> classroomEntryList = new ArrayList<ClassroomEntry>();
        DBObject query = new BasicDBObject("sid", schoolId).append("df", Constant.ZERO);
        int skip = (page - 1) * pageSize;
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASSROOM, query,
                Constant.FIELDS, new BasicDBObject(Constant.ID, -1), skip, pageSize);
        if (null != list && !list.isEmpty()) {
            ClassroomEntry e;
            for (DBObject dbo : list) {
                classroomEntryList.add(new ClassroomEntry((BasicDBObject) dbo));
            }
        }
        return classroomEntryList;
    }

    /**
     * 根据学校id获取所有的班级entry列表
     *
     * @param schoolId
     * @return
     */
    public List<ClassroomEntry> findClassRoomEntryList(ObjectId schoolId) {
        List<ClassroomEntry> classroomEntryList = new ArrayList<ClassroomEntry>();
        DBObject query = new BasicDBObject("sid", schoolId).append("df", Constant.ZERO);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASSROOM, query, Constant.FIELDS);
        if (null != list && !list.isEmpty()) {
            ClassroomEntry e;
            for (DBObject dbo : list) {
                classroomEntryList.add(new ClassroomEntry((BasicDBObject) dbo));
            }
        }
        return classroomEntryList;
    }

    /**
     * 根据学校id获取所有的班级entry Map
     *
     * @param schoolId
     * @return
     */
    public Map<ObjectId, ClassroomEntry> findClassRoomEntryMap(ObjectId schoolId) {
        Map<ObjectId, ClassroomEntry> retMap = new HashMap<ObjectId, ClassroomEntry>();
        DBObject query = new BasicDBObject("sid", schoolId).append("df", Constant.ZERO);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASSROOM, query, Constant.FIELDS);
        if (null != list && !list.isEmpty()) {
            ClassroomEntry e;
            for (DBObject dbo : list) {
                e = new ClassroomEntry((BasicDBObject) dbo);
                retMap.put(e.getID(), e);
            }
        }
        return retMap;
    }

    /**
     * 检查是否存在该名称，存在返回true
     *
     * @param classRoomId
     * @param schoolId
     * @param name
     * @return
     */
    public boolean checkClassRoomIfHave(ObjectId classRoomId, ObjectId schoolId, String name) {
        DBObject query = new BasicDBObject("sid", schoolId).append("nm", name).append("df", Constant.ZERO);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASSROOM, query, Constant.FIELDS);
        if (dbObject != null) {
            ClassroomEntry classroomEntry = new ClassroomEntry((BasicDBObject) dbObject);
            if (classroomEntry.getID().equals(classRoomId))
                return false;
            else
                return true;
        }
        return false;
    }

    /**
     * 逻辑删除
     *
     * @param classRoomId
     */
    public void deleteClassRoom(ObjectId classRoomId) {
        DBObject query = new BasicDBObject(Constant.ID, classRoomId).append("df", Constant.ZERO);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("df", Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASSROOM, query, updateValue);
    }

    /**
     * 修改
     *
     * @param classroomEntry
     */
    public void updateClassRoom(ClassroomEntry classroomEntry) {
        DBObject query = new BasicDBObject(Constant.ID, classroomEntry.getID()).append("df", Constant.ZERO);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("nm", classroomEntry.getRoomName())
                .append("re", classroomEntry.getRemark()).append("cid", classroomEntry.getClassId()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASSROOM, query, updateValue);
    }

    /**
     * ID获取教室列表
     *
     * @param classRoomIds
     * @param fields
     * @return
     */
    public Map<ObjectId, ClassroomEntry> findClassRoomEntryMap(List<ObjectId> classRoomIds, BasicDBObject fields) {
        Map<ObjectId, ClassroomEntry> retMap = new HashMap<ObjectId, ClassroomEntry>();
        DBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, classRoomIds));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASSROOM, query, fields);
        if (null != list && !list.isEmpty()) {
            ClassroomEntry e;
            for (DBObject dbo : list) {
                e = new ClassroomEntry((BasicDBObject) dbo);
                retMap.put(e.getID(), e);
            }
        }
        return retMap;
    }

    /**
     * id获取教室ENTRY
     *
     * @param classRoomId
     * @return
     */
    public ClassroomEntry findClassRoomById(ObjectId classRoomId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, classRoomId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASSROOM, query, null);
        if (null != dbo) {
            return new ClassroomEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * 统计总数
     *
     * @param schoolId
     * @return
     */
    public int count(ObjectId schoolId) {
        DBObject query = new BasicDBObject("sid", schoolId).append("df", Constant.ZERO);
        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASSROOM, query);
        return count;
    }

    /**
     * 根据班级id获取教室
     *
     * @param classId
     * @return
     */
    public ClassroomEntry findClassroomByClassId(ObjectId classId) {
        BasicDBObject query = new BasicDBObject()
                .append("cid", classId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASSROOM, query, null);
        if (null != dbo) {
            return new ClassroomEntry((BasicDBObject) dbo);
        }
        return null;
    }
}
