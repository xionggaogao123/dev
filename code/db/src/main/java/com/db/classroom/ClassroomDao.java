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
     * 根据学校id获取所有的班级entry列表(分页)
     *
     * @param schoolId
     * @returns
     */
    public List<ClassroomEntry> findClassRoomEntryList(ObjectId schoolId, int page, int pageSize) {
        List<ClassroomEntry> classroomEntryList = new ArrayList<ClassroomEntry>();
        DBObject query = new BasicDBObject("sid", schoolId);
        int skip = (page - 1) * pageSize;

        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASSROOM, query,
                Constant.FIELDS, new BasicDBObject(Constant.ID, 1), skip, pageSize);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                classroomEntryList.add(new ClassroomEntry((BasicDBObject) dbo));
            }
        }
        return classroomEntryList;
    }


    /**
     * 根据学校id获取所有的班级entry列表(不分页)
     *
     * @param schoolId
     * @return
     */
    public List<ClassroomEntry> findClassRoomEntryList(ObjectId schoolId) {
        List<ClassroomEntry> classroomEntryList = new ArrayList<ClassroomEntry>();
        DBObject query = new BasicDBObject("sid", schoolId);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASSROOM, query, Constant.FIELDS);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                classroomEntryList.add(new ClassroomEntry((BasicDBObject) dbo));
            }
        }
        return classroomEntryList;
    }

    /**
     * 统计总数
     *
     * @param schoolId
     * @return
     */
    public int count(ObjectId schoolId) {
        DBObject query = new BasicDBObject("sid", schoolId);
        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASSROOM, query);
        return count;
    }

    /**
     * 检查教室名称是否存在
     *
     * @param schoolId
     * @param roomName
     * @return
     */
    public boolean classroomIsExisted(ObjectId schoolId, String roomName) {
        DBObject query = new BasicDBObject("sid", schoolId).append("nm", roomName);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASSROOM, query, Constant.FIELDS);
        if (dbObject != null) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 删除教室
     * @param id
     */
    public void remove(ObjectId id) {
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASSROOM, new BasicDBObject(Constant.ID, id));
    }

    /**
     * 修改
     *
     * @param classroomEntry
     */
    public void updateClassRoom(ClassroomEntry classroomEntry) {
        DBObject query = new BasicDBObject(Constant.ID, classroomEntry.getID());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("nm", classroomEntry.getRoomName())
                .append("cid", classroomEntry.getClassId()));
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
            for (DBObject dbo : list) {
                ClassroomEntry e = new ClassroomEntry((BasicDBObject) dbo);
                retMap.put(e.getID(), e);
            }
        }
        return retMap;
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



}
