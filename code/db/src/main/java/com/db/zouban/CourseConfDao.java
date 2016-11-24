package com.db.zouban;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.utils.MongoUtils;
import com.pojo.zouban.*;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiangm on 2015/9/15.
 */
public class CourseConfDao extends BaseDao {
    /**
     * 获取课表配置
     *
     * @param schoolId
     * @param term
     * @param gradeId
     * @return
     */
    public CourseConfEntry findCourseConf(ObjectId schoolId, String term, ObjectId gradeId) {
        BasicDBObject query = new BasicDBObject();
        query.append("sid", schoolId);
        query.append("te", term);
        query.append("gid", gradeId);
        //query.append("cid", classId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_CONF, query, Constant.FIELDS);
        if (dbObject == null)
            return null;
        return new CourseConfEntry((BasicDBObject) dbObject);
    }

    /**
     * 获取课表配置
     *
     * @param schoolId
     * @param term
     * @return
     */
    public CourseConfEntry findCourseConfBySchool(ObjectId schoolId, String term) {
        BasicDBObject query = new BasicDBObject();
        query.append("sid", schoolId);
        query.append("te", term);
        //query.append("cid", classId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_CONF, query, Constant.FIELDS);
        if (dbObject == null)
            return null;
        return new CourseConfEntry((BasicDBObject) dbObject);
    }

    /**
     * 根据id获取课表配置
     *
     * @param id
     * @return
     */
    public CourseConfEntry findCourseConfById(ObjectId id) {
        BasicDBObject query = new BasicDBObject();
        query.append(Constant.ID, id);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_CONF, query, Constant.FIELDS);
        return new CourseConfEntry((BasicDBObject) dbObject);
    }

    /**
     * 根据学期和年级id获取课表配置
     *
     * @param term
     * @return
     */
    public CourseConfEntry findCourseConfByGradeId(String term, ObjectId gradeId) {
        BasicDBObject query = new BasicDBObject();
        query.append("te", term);
        query.append("gid", gradeId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_CONF, query, Constant.FIELDS);
        return new CourseConfEntry((BasicDBObject) dbObject);
    }

    /**
     * 增加课表配置
     *
     * @param courseConfEntry
     * @return
     */
    public boolean addCourseConf(CourseConfEntry courseConfEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_CONF, courseConfEntry.getBaseEntry());
        return true;
    }

    /**
     * 删除课表配置，用于修改时，先删除原有的，再添加新的
     *
     * @param schoolId
     * @param term
     * @param gradeId
     * @return
     */
    public boolean removeCourseConf(ObjectId schoolId, String term, ObjectId gradeId) {
        BasicDBObject query = new BasicDBObject();
        query.append("sid", schoolId);
        query.append("te", term);
        query.append("gid", gradeId);
        //query.append("cid", classId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_CONF, query);
        return true;
    }

    public boolean removeCourseConf(ObjectId schoolId, String term) {
        BasicDBObject query = new BasicDBObject();
        query.append("sid", schoolId);
        query.append("te", term);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_CONF, query);
        return true;
    }

    /**
     * 统计某班课表配置数量，为了避免多人重复创建
     *
     * @param schoolId
     * @param term
     * @param gradeId
     * @return
     */
    public int countCourseConf(ObjectId schoolId, String term, ObjectId gradeId) {
        BasicDBObject query = new BasicDBObject();
        query.append("sid", schoolId);
        query.append("te", term);
        query.append("gid", gradeId);
        // query.append("cid", classId);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_CONF, query);
    }

    public int countCourseConf(ObjectId schoolId, String term) {
        BasicDBObject query = new BasicDBObject();
        query.append("sid", schoolId);
        query.append("te", term);
        // query.append("cid", classId);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_CONF, query);
    }

    /**
     * 根据选课id获取分段情况
     *
     * @param xuankeId
     * @return
     */
    public List<ClassFengDuanEntry> getClassFenduanList(ObjectId xuankeId) {
        BasicDBObject query = new BasicDBObject("xkid", xuankeId);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FENGDUAN, query, Constant.FIELDS);
        List<ClassFengDuanEntry> list = new ArrayList<ClassFengDuanEntry>();
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject entry : dbObjectList) {
                list.add(new ClassFengDuanEntry((BasicDBObject) entry));
            }
        }
        return list;
    }

    /**
     * 根据班级id找到同一段内的所有班级id
     *
     * @param xuankeId
     * @param classId
     * @return
     */
    public List<ObjectId> getAllClassIds(ObjectId xuankeId, ObjectId classId) {
        BasicDBObject query = new BasicDBObject();
        query.append("xkid", xuankeId);
        query.append("cids", classId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FENGDUAN, query, Constant.FIELDS);
        ClassFengDuanEntry classFengDuanEntry = new ClassFengDuanEntry((BasicDBObject) dbObject);
        if (classFengDuanEntry != null)
            return classFengDuanEntry.getClassIds();
        return null;
    }

    /**
     * 根据班级Id列表删除非走班课
     *
     * @param term
     * @param classIds
     */
    public void removeFeizouban(String term, List<ObjectId> classIds, int courseType) {
        BasicDBObject query = new BasicDBObject();
        query.append("ty", 3);
        query.append("te", term);
        query.append("cid", new BasicDBObject(Constant.MONGO_IN, classIds));
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query, Constant.FIELDS);
        List<CourseItem> courseItemList = new ArrayList<CourseItem>();
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                List<CourseItem> list = new TimeTableEntry((BasicDBObject) dbObject).getCourseList();
                for (CourseItem c : list) {
                    if (c.getType() == courseType)//非走班
                    {
                        courseItemList.add(c);
                    }
                }
            }
        }
        BasicDBObject updateValue = new BasicDBObject();
        updateValue.append(Constant.MONGO_PULLALL, new BasicDBObject("cli", MongoUtils.convert(MongoUtils.fetchDBObjectList(courseItemList))));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query, updateValue);
    }

    /**
     * 获取非走班课程列表
     *
     * @param gradeId
     * @return
     */
    public List<ZouBanCourseEntry> getUnarrangeCourses(String term, ObjectId gradeId, int type, String groupId) {
        List<ZouBanCourseEntry> zouBanCourseEntries = new ArrayList<ZouBanCourseEntry>();
        BasicDBObject query = new BasicDBObject();
        query.append("te", term);
        query.append("gid", gradeId);
        query.append("type", type);
        if (!groupId.equals(""))
            query.append("grpid", new ObjectId(groupId));
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, Constant.FIELDS);
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                zouBanCourseEntries.add(new ZouBanCourseEntry((BasicDBObject) dbObject));
            }
        }
        return zouBanCourseEntries;
    }

    /**
     * 根据年级获取全部课程列表
     *
     * @param gradeId
     * @return
     */
    public List<ZouBanCourseEntry> getAllUnarrangeCourses(String term, ObjectId gradeId) {
        List<ZouBanCourseEntry> zouBanCourseEntries = new ArrayList<ZouBanCourseEntry>();
        BasicDBObject query = new BasicDBObject();
        query.append("te", term);
        query.append("gid", gradeId);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, Constant.FIELDS);
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                zouBanCourseEntries.add(new ZouBanCourseEntry((BasicDBObject) dbObject));
            }
        }
        return zouBanCourseEntries;
    }

    /**
     * 获取未排课的非走班课程
     *
     * @param term
     * @param type
     * @param classId
     * @return
     */
    public List<ZouBanCourseEntry> getUnarrangeFZBCourses(String term, int type, ObjectId classId) {
        List<ZouBanCourseEntry> zouBanCourseEntries = new ArrayList<ZouBanCourseEntry>();
        BasicDBObject query = new BasicDBObject();
        query.append("te", term);
        //query.append("gid",gradeId);
        query.append("cid", classId);
        query.append("type", type);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, Constant.FIELDS);
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                zouBanCourseEntries.add(new ZouBanCourseEntry((BasicDBObject) dbObject));
            }
        }
        return zouBanCourseEntries;
    }

    /**
     * 清空本班的非走班课程
     *
     * @param term
     * @param classId
     */
    public void removeClassFeizouban(String term, ObjectId classId) {
        BasicDBObject query = new BasicDBObject();
        query.append("ty", 3);
        query.append("te", term);
        query.append("cid", classId);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query, Constant.FIELDS);
        List<CourseItem> courseItemList = new ArrayList<CourseItem>();
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                List<CourseItem> list = new TimeTableEntry((BasicDBObject) dbObject).getCourseList();
                for (CourseItem c : list) {
                    if (c.getType() == 2)//非走班
                    {
                        courseItemList.add(c);
                    }
                }
            }
        }
        BasicDBObject updateValue = new BasicDBObject();
        updateValue.append(Constant.MONGO_PULLALL, new BasicDBObject("cli", MongoUtils.convert(MongoUtils.fetchDBObjectList(courseItemList))));
        try {
            update(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query, updateValue);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * 根据kechengid列表获取zoubancourseEntrylieb
     *
     * @param courseIds
     * @return
     */
    public List<ZouBanCourseEntry> getCourseList(List<ObjectId> courseIds) {
        List<ZouBanCourseEntry> zouBanCourseEntryList = new ArrayList<ZouBanCourseEntry>();
        BasicDBObject query = new BasicDBObject();
        query.append(Constant.ID, new BasicDBObject(Constant.MONGO_IN, courseIds));
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, Constant.FIELDS);
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                zouBanCourseEntryList.add(new ZouBanCourseEntry((BasicDBObject) dbObject));
            }
        }
        return zouBanCourseEntryList;
    }

    /**
     * 获取zoubancourseEntry
     *
     * @param courseId
     * @return
     */
    public ZouBanCourseEntry getZoubanCourse(ObjectId courseId) {
        BasicDBObject query = new BasicDBObject();
        query.append(Constant.ID, courseId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, Constant.FIELDS);
        return new ZouBanCourseEntry((BasicDBObject) dbObject);
    }

    /**
     * 获取分段列表
     *
     * @param ids
     * @return
     */
    public List<ClassFengDuanEntry> getDuanListByIds(List<ObjectId> ids) {
        List<ClassFengDuanEntry> classFengDuanEntries = new ArrayList<ClassFengDuanEntry>();
        BasicDBObject query = new BasicDBObject()
                .append(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids));
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FENGDUAN, query, Constant.FIELDS);
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                classFengDuanEntries.add(new ClassFengDuanEntry((BasicDBObject) dbObject));
            }
        }
        return classFengDuanEntries;
    }

    /**
     * 获取全校的课表配置
     *
     * @param term
     * @param schoolId
     * @return
     */
    public List<CourseConfEntry> findCourseConfsBySchoolId(String term, ObjectId schoolId) {
        List<CourseConfEntry> courseConfEntryList = new ArrayList<CourseConfEntry>();
        BasicDBObject query = new BasicDBObject();
        query.append("sid", schoolId);
        query.append("te", term);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_CONF, query, Constant.FIELDS);
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                courseConfEntryList.add(new CourseConfEntry((BasicDBObject) dbObject));
            }
        }
        return courseConfEntryList;
    }
}
