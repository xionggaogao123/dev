package com.db.zouban;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
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
public class TimeTableDao extends BaseDao {
    /**
     * 查询课表
     *
     * @param term
     * @param classId
     * @param type
     * @return
     */
    public TimeTableEntry findTimeTable(String term, ObjectId classId, int type, int week) {
        BasicDBObject query = new BasicDBObject();
        query.append("te", term);
        query.append("cid", classId);
        query.append("ty", type);
        query.append("week", week);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query, Constant.FIELDS);
        if (dbObject == null)
            return null;
        return new TimeTableEntry((BasicDBObject) dbObject);
    }

    /**
     * 获取全年级的课表
     *
     * @param term
     * @param gradeId
     * @param type
     * @param week
     * @return
     */
    public List<TimeTableEntry> findTimeTableByGrade(String term, ObjectId gradeId, int type, int week) {
        BasicDBObject query = new BasicDBObject();
        query.append("te", term);
        query.append("gid", gradeId);
        query.append("ty", type);
        query.append("week", week);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query, Constant.FIELDS);
        List<TimeTableEntry> timeTableEntryList = new ArrayList<TimeTableEntry>();
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject dbObject1 : dbObjectList) {
                TimeTableEntry timeTableEntry = new TimeTableEntry((BasicDBObject) dbObject1);
                timeTableEntryList.add(timeTableEntry);
            }
        }
        return timeTableEntryList;
    }

    /**
     * 获取全校的课表
     *
     * @param term
     * @param schoolId
     * @param type
     * @return
     */
    public List<TimeTableEntry> findAllTimeTable(String term, ObjectId schoolId, int type, int week) {
        BasicDBObject query = new BasicDBObject();
        query.append("te", term);
        query.append("sid", schoolId);
        query.append("ty", type);
        query.append("week", week);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query, Constant.FIELDS);
        List<TimeTableEntry> timeTableEntries = new ArrayList<TimeTableEntry>();
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                timeTableEntries.add(new TimeTableEntry((BasicDBObject) dbObject));
            }
        }
        return timeTableEntries;
    }

    /**
     * 获取全校的课表---介于两周之间
     *
     * @param term
     * @param schoolId
     * @param type
     * @return
     */
    public List<TimeTableEntry> findAllTimeTableWeeks(String term, ObjectId schoolId, int type, int week1, int week2) {
        BasicDBObject query = new BasicDBObject();
        query.append("te", term);
        query.append("sid", schoolId);
        query.append("ty", type);

        BasicDBList basicDBList = new BasicDBList();
        basicDBList.add(new BasicDBObject("week", new BasicDBObject(Constant.MONGO_GTE, week1)));
        basicDBList.add(new BasicDBObject("week", new BasicDBObject(Constant.MONGO_LTE, week2)));
        query.append(Constant.MONGO_AND, basicDBList);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query, Constant.FIELDS);
        List<TimeTableEntry> timeTableEntries = new ArrayList<TimeTableEntry>();
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                timeTableEntries.add(new TimeTableEntry((BasicDBObject) dbObject));
            }
        }
        return timeTableEntries;
    }

    /**
     * 查询介于两周之间的所有课表
     *
     * @param term
     * @param schoolId
     * @param classId
     * @param type
     * @param week1
     * @param week2
     * @return
     */
    public List<TimeTableEntry> findTimeTableByWeek(String term, ObjectId schoolId, ObjectId classId, int type, int week1, int week2) {
        BasicDBObject query = new BasicDBObject();
        query.append("te", term);
        query.append("sid", schoolId);
        query.append("cid", classId);
        query.append("ty", type);
        BasicDBList basicDBList = new BasicDBList();
        basicDBList.add(new BasicDBObject("week", new BasicDBObject(Constant.MONGO_GTE, week1)));
        basicDBList.add(new BasicDBObject("week", new BasicDBObject(Constant.MONGO_LTE, week2)));
        query.append(Constant.MONGO_AND, basicDBList);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query, Constant.FIELDS);
        List<TimeTableEntry> timeTableEntries = new ArrayList<TimeTableEntry>();
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                timeTableEntries.add(new TimeTableEntry((BasicDBObject) dbObject));
            }
        }
        return timeTableEntries;
    }

    /**
     * 添加课表
     *
     * @param timeTableEntry
     * @return
     */
    public boolean addTimeTable(TimeTableEntry timeTableEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, timeTableEntry.getBaseEntry());
        return true;
    }

    /**
     * 删除课表
     *
     * @param term
     * @param classId
     * @param type
     * @return
     */
    public boolean deleteTimeTable(String term, ObjectId classId, int type, int week) {
        BasicDBObject query = new BasicDBObject();
        query.append("te", term);
        query.append("cid", classId);
        query.append("week", week);
        query.append("ty", type);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query);
        return true;
    }

    /**
     * 统计课表数量
     *
     * @param term
     * @param classId
     * @param type
     * @return
     */
    public int countTimeTable(String term, ObjectId classId, int type) {
        BasicDBObject query = new BasicDBObject();
        query.append("te", term);
        query.append("cid", classId);
        query.append("ty", type);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query);
    }

    /**
     * 添加排课结果
     *
     * @param term
     * @param classId
     * @param type
     * @param courseItemList
     */
    public void addCourseList(String term, ObjectId classId, int type, List<CourseItem> courseItemList) {
        BasicDBObject query = new BasicDBObject();
        query.append("te", term);
        query.append("cid", classId);
        query.append("ty", type);
        BasicDBObject update = new BasicDBObject();
        update.append(Constant.MONGO_PUSHALL, new BasicDBObject("cli", MongoUtils.fetchDBObjectList(courseItemList)));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query, update);
    }

    public void addCourseList(String term, ObjectId classId, ObjectId gradeId, int type, List<CourseItem> courseItemList) {
        BasicDBObject query = new BasicDBObject();
        query.append("te", term);
        query.append("cid", classId);
        query.append("gid", gradeId);
        query.append("ty", type);
        BasicDBObject update = new BasicDBObject();
        update.append(Constant.MONGO_PUSHALL, new BasicDBObject("cli", MongoUtils.fetchDBObjectList(courseItemList)));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query, update);
    }

    /**
     * 添加课程----调课专用
     *
     * @param term
     * @param classId
     * @param type
     * @param week
     * @param courseItemList
     */
    public void addCourseListByWeek(String term, ObjectId classId, int type, int week, List<CourseItem> courseItemList) {
        BasicDBObject query = new BasicDBObject();
        query.append("te", term);
        query.append("cid", classId);
        query.append("week", week);
        query.append("ty", type);
        BasicDBObject update = new BasicDBObject();
        update.append(Constant.MONGO_PUSHALL, new BasicDBObject("cli", MongoUtils.fetchDBObjectList(courseItemList)));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query, update);
    }

    /**
     * 移除课程
     *
     * @param term
     * @param classId
     * @param type
     * @param courseItem
     */
    public void removeCourse(String term, ObjectId classId, int type, CourseItem courseItem) {
        BasicDBObject query = new BasicDBObject();
        query.append("te", term);
        query.append("cid", classId);
        query.append("ty", type);
        BasicDBObject update = new BasicDBObject();
        update.append(Constant.MONGO_PULL, new BasicDBObject("cli", courseItem.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query, update);
    }

    /**
     * 移除课程
     *
     * @param term
     * @param classId
     * @param type
     * @param courseItem
     */
    public void removeCourseByWeek(String term, ObjectId classId, int type, int week, CourseItem courseItem) {
        BasicDBObject query = new BasicDBObject();
        query.append("te", term);
        query.append("cid", classId);
        query.append("week", week);
        query.append("ty", type);
        BasicDBObject update = new BasicDBObject();
        update.append(Constant.MONGO_PULL, new BasicDBObject("cli", courseItem.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query, update);
    }

    /**
     * 根据学科、班级、学期获取课表
     *
     * @param term
     * @param classId
     * @return
     */
    public TimeTableEntry getCourseTableBySubId(String term, ObjectId classId) {
        BasicDBObject query = new BasicDBObject();
        query.append("te", term);
        query.append("cid", classId);
        query.append("ty", 0);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query, Constant.FIELDS);
        if (dbObject == null)
            return null;
        return new TimeTableEntry((BasicDBObject) dbObject);
    }

    /**
     * 根据年级获取全部课表
     *
     * @param term
     * @param gradeId
     * @return
     */
    public List<TimeTableEntry> getCourseTableByGradeId(String term, ObjectId gradeId, int type, int week) {
        BasicDBObject query = new BasicDBObject();
        query.append("te", term);
        query.append("gid", gradeId);
        query.append("ty", type);
        query.append("week", week);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query, Constant.FIELDS);
        List<TimeTableEntry> timeTableEntries = new ArrayList<TimeTableEntry>();
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject d : dbObjectList) {
                timeTableEntries.add(new TimeTableEntry((BasicDBObject) d));
            }
        }
        return timeTableEntries;
    }


    /**
     * 根据courseId获取课程详细内容
     *
     * @param courseIds
     * @return
     */
    public List<ZouBanCourseEntry> getCourseEntriesByIds(List<ObjectId> courseIds) {
        List<ZouBanCourseEntry> zouBanCourseEntries = new ArrayList<ZouBanCourseEntry>();
        BasicDBObject query = new BasicDBObject();
        query.append(Constant.ID, new BasicDBObject(Constant.MONGO_IN, courseIds));
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, Constant.FIELDS);
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject d : dbObjectList) {
                ZouBanCourseEntry zouBanCourseEntry = new ZouBanCourseEntry((BasicDBObject) d);
                zouBanCourseEntries.add(zouBanCourseEntry);
            }
        }
        return zouBanCourseEntries;
    }

    /**
     * 根据学校获取走班课列表
     *
     * @param schoolId
     * @return
     */
    public List<ZouBanCourseEntry> getZoubanCourseList(String year, ObjectId schoolId) {
        BasicDBObject query = new BasicDBObject();
        query.append("sid", schoolId);
        query.append("te", year);
        //query.append("subid",new BasicDBObject(Constant.MONGO_IN,subjectIds));
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, Constant.FIELDS);
        List<ZouBanCourseEntry> zouBanCourseEntries = new ArrayList<ZouBanCourseEntry>();
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject d : dbObjectList) {
                zouBanCourseEntries.add(new ZouBanCourseEntry((BasicDBObject) d));
            }
        }
        return zouBanCourseEntries;
    }

    /**
     * 获取教师课表----finish
     *
     * @param term
     * @param teacherId
     * @return
     */
    public List<ZouBanCourseEntry> findTeacherCourseList(String term, ObjectId teacherId) {
        List<ZouBanCourseEntry> zouBanCourseEntries = new ArrayList<ZouBanCourseEntry>();
        BasicDBObject query = new BasicDBObject();
        query.append("te", term);
        query.append("tid", teacherId);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, Constant.FIELDS);
        for (DBObject d : dbObjectList) {
            zouBanCourseEntries.add(new ZouBanCourseEntry((BasicDBObject) d));
        }
        return zouBanCourseEntries;
    }

    /**
     * 根据课程id获取基本配置
     *
     * @param courseId
     * @return
     */
    public ZouBanCourseEntry findZoubanConfByCourseId(ObjectId courseId) {
        BasicDBObject query = new BasicDBObject();
        query.append(Constant.ID, courseId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, Constant.FIELDS);
        ZouBanCourseEntry zouBanCourseEntry = new ZouBanCourseEntry((BasicDBObject) dbObject);
        return zouBanCourseEntry;
    }

    /**
     * 根据科目id获取zoubancoursentry列表
     *
     * @param subIds
     * @return
     */
    public List<ZouBanCourseEntry> getZoubanCouseEntryList(List<ObjectId> subIds) {
        List<ZouBanCourseEntry> zouBanCourseEntries = new ArrayList<ZouBanCourseEntry>();
        BasicDBObject query = new BasicDBObject();
        query.append("subid", new BasicDBObject(Constant.MONGO_IN, subIds));
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, Constant.FIELDS);
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                zouBanCourseEntries.add(new ZouBanCourseEntry((BasicDBObject) dbObject));
            }
        }
        return zouBanCourseEntries;
    }

    /**
     * 删除本年级的已选科目
     *
     * @param term
     * @param gradeId
     * @param type
     */
    public void deleteCourseByGradeId(String term, ObjectId gradeId, int type) {
        BasicDBObject query = new BasicDBObject();
        query.append("te", term);
        query.append("gid", gradeId);
        query.append("ty", type);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query);
    }

    /**
     * 更新锁定状态
     *
     * @param term
     * @param classIds
     * @param lock
     */
    public void updateCourseTable(String term, List<ObjectId> classIds, int lock) {
        BasicDBObject query = new BasicDBObject();
        query.append("te", term);
        query.append("cid", new BasicDBObject(Constant.MONGO_IN, classIds));
        BasicDBObject updateValue = new BasicDBObject()
                .append(Constant.MONGO_SET, new BasicDBObject("lock", lock));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query, updateValue);
    }

    /**
     * 删除课表，仅保留排课状态，即type=3
     *
     * @param term1
     * @param schoolId
     * @param gradeId
     */
    public void removeAllCourse(String term1, String term2, ObjectId schoolId, ObjectId gradeId) {
        BasicDBObject query = new BasicDBObject();
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject("te", term1));
        values.add(new BasicDBObject("te", term2));
        query.put(Constant.MONGO_OR, values);
        query.append("sid", schoolId);
        query.append("gid", gradeId);
        query.append("ty", new BasicDBObject(Constant.MONGO_LT, 3));
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query);
    }

    /**
     * 根据id删除课表
     *
     * @param tableIds
     */
    public void removeTable(List<ObjectId> tableIds) {
        BasicDBObject query = new BasicDBObject();
        query.append(Constant.ID, new BasicDBObject(Constant.MONGO_IN, tableIds));
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query);
    }

    /**
     * 获取有调课的班级
     *
     * @param term
     * @param gradeId
     * @param week
     * @return
     */
    public List<TimeTableEntry> findAdjustClass(String term, ObjectId gradeId, ObjectId schoolId, int week) {
        List<TimeTableEntry> result = new ArrayList<TimeTableEntry>();
        BasicDBObject query = new BasicDBObject();
        query.append("te", term);
        query.append("gid", gradeId);
        query.append("sid", schoolId);
        query.append("week", week);
        query.append("ty", 1);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query, Constant.FIELDS);
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                result.add(new TimeTableEntry((BasicDBObject) dbObject));
            }
        }
        return result;
    }

    /**
     * 修改课表内容//TODO
     *
     * @param term
     * @param schoolId
     * @param classId
     * @param week
     * @param type
     * @param x
     * @param y
     * @param newCourseId
     */
    public void updateTable(String term, ObjectId schoolId, ObjectId classId, int week, int type, ObjectId newCourseId, ObjectId oldCourseId) {
        List<ObjectId> courseIds = new ArrayList<ObjectId>();
        courseIds.add(newCourseId);
        List<ObjectId> oldCourseIds = new ArrayList<ObjectId>();
        oldCourseIds.add(oldCourseId);
        BasicDBObject query = new BasicDBObject()
                .append("te", term)
                .append("sid", schoolId)
                .append("cid", classId)
                .append("week", week)
                .append("ty", type)
                .append("cli.cou", oldCourseIds);
        /*BasicDBList queryList = new BasicDBList();
        queryList.add(new BasicDBObject("cli.xi", x));
        queryList.add(new BasicDBObject("cli.yi", y));
        query.append(Constant.MONGO_AND,queryList);*/
        BasicDBObject updateValue = new BasicDBObject()
                .append(Constant.MONGO_SET, new BasicDBObject("cli.$.cou", courseIds));
        try {

            update(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query, updateValue);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * 体育调课
     *
     * @param term
     * @param gradeId
     * @param xFrom
     * @param yFrom
     * @param xTo
     * @param yTo
     */
    public void adjustPhysical(String term, ObjectId gradeId, int xFrom, int yFrom, int xTo, int yTo) {
        BasicDBObject query = new BasicDBObject("te", term)
                .append("gid", gradeId)
                .append("cli", new BasicDBObject(Constant.MONGO_ELEMATCH, new BasicDBObject("xi", xFrom).append("yi", yFrom)));

        BasicDBObject update = new BasicDBObject("cli.$.xi", xTo).append("cli.$.yi", yTo);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, update);

        update(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query, updateValue);
    }


}
