package com.db.zouban;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.QueryOperators;
import com.pojo.base.BaseDBObject;
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
     * 查询班级课表
     *
     * @param term
     * @param classId
     * @param type
     * @return
     */
    public TimeTableEntry findTimeTable(String term, ObjectId classId, int type, int week) {
        BasicDBObject query = new BasicDBObject()
                .append("te", term)
                .append("cid", classId)
                .append("week", week);
        if (type != 0) {
            query.append("ty", type);
        }
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query, Constant.FIELDS);
        if (dbObject == null)
            return null;
        return new TimeTableEntry((BasicDBObject) dbObject);
    }


    /**
     * 根据学校和type查询课表
     *
     * @param term
     * @param schoolId
     * @param type
     * @return
     */
    public TimeTableEntry findTimetable(String term, ObjectId schoolId, int type) {
        BasicDBObject query = new BasicDBObject()
                .append("sid", schoolId)
                .append("ty", type);

        String term1 = term + "第一学期";
        String term2 = term + "第二学期";
        List<String> termList = new ArrayList<String>();
        termList.add(term1);
        termList.add(term2);
        query.append("te", new BasicDBObject(Constant.MONGO_IN, termList));

        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query, Constant.FIELDS);
        if (dbObject == null) {
            return null;
        }
        return new TimeTableEntry((BasicDBObject) dbObject);
    }

    /**
     * 根据班级id列表查询
     *
     * @param term
     * @param classId
     * @param week
     * @return
     */
    public List<TimeTableEntry> findTimeTableList(String term, List<ObjectId> classId, int week) {
        BasicDBObject query = new BasicDBObject()
                .append("te", term)
                .append("cid", new BasicDBObject(Constant.MONGO_IN, classId))
                .append("week", week);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query, Constant.FIELDS);
        List<TimeTableEntry> timeTableEntryList = new ArrayList<TimeTableEntry>();

        for (DBObject dbObject : dbObjectList) {
            timeTableEntryList.add(new TimeTableEntry((BasicDBObject) dbObject));
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
        BasicDBObject query = new BasicDBObject()
                .append("te", term)
                .append("sid", schoolId)
                .append("week", week);

        if (type != 0) {
            query.append("ty", type);
        }

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
     * 获取全年级的课表
     *
     * @param term
     * @param gradeId
     * @param type
     * @param week
     * @return
     */
    public List<TimeTableEntry> findTimeTableByGrade(String term, ObjectId gradeId, int type, int week) {
        BasicDBObject query = new BasicDBObject()
                .append("te", term)
                .append("gid", gradeId)
                .append("week", week);
        if (type != 0) {
            query.append("ty", type);
        } else {
            query.append("ty", 1);
        }

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
     * 添加排课结果
     *
     * @param term
     * @param classId
     * @param type
     * @param courseItemList
     */
    public void addCourseList(String term, ObjectId classId, int type, List<CourseItem> courseItemList) {
        BasicDBObject query = new BasicDBObject()
                .append("te", term)
                .append("cid", classId);
        BasicDBObject updateValue = new BasicDBObject("cli", MongoUtils.fetchDBObjectList(courseItemList));
        BasicDBObject update = new BasicDBObject(Constant.MONGO_PUSHALL, updateValue);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query, update);
    }


    /**
     * 添加课程
     *
     * @param term
     * @param classId
     * @param courseItem
     */
    public void addCourse(String term, ObjectId classId, CourseItem courseItem) {
        BasicDBObject query = new BasicDBObject()
                .append("te", term)
                .append("cid", classId)
                .append("ty", 1)
                .append("week", 0);
        BasicDBObject updateValue = new BasicDBObject("cli", courseItem.getBaseEntry());
        BasicDBObject update = new BasicDBObject(Constant.MONGO_PUSH, updateValue);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query, update);
    }


    /**
     * 更新课表项
     *
     * @param term
     * @param classId
     * @param courseItemId
     * @param courseIds
     */
    public void updateCourseItem(String term, ObjectId classId, ObjectId courseItemId, List<ObjectId> courseIds) {
        BasicDBObject query = new BasicDBObject()
                .append("te", term)
                .append("cid", classId)
                .append("ty", 1)
                .append("week", 0)
                .append("cli.id", courseItemId);
        BasicDBObject updateValue = new BasicDBObject("cli.$.cou", MongoUtils.convert(courseIds));
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, updateValue);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query, update);
    }


    /**
     * 更新课表（调课用）
     *
     * @param term
     * @param classId
     * @param type
     * @param weekList
     * @param courseItemId
     * @param x
     * @param y
     */
    public void updateCourseItem(String term, ObjectId classId, int type, List<Integer> weekList, ObjectId courseItemId, int x, int y) {
       BasicDBObject query = new BasicDBObject("te", term)
               .append("cid", classId)
               .append("ty", type)
               .append("week", new BasicDBObject(Constant.MONGO_IN, weekList))
               .append("cli.id", courseItemId);
        BasicDBObject updateValue = new BasicDBObject("cli.$.xi", x).append("cli.$.yi", y);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, updateValue);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query, update);
    }

    /**
     * 保存调课结果（调课用）
     *
     * @param term
     * @param classId
     * @param type
     * @param weekList
     * @param courseItemList
     */
    public void updateCourseItems(String term, ObjectId classId, int type, List<Integer> weekList, List<CourseItem> courseItemList) {
        BasicDBObject query = new BasicDBObject("te", term)
                .append("cid", classId)
                .append("ty", type)
                .append("week", new BasicDBObject(Constant.MONGO_IN, weekList));
        BasicDBObject updateValue = new BasicDBObject("cli", MongoUtils.convert(MongoUtils.fetchDBObjectList(courseItemList)));
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, updateValue);
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
        BasicDBObject query = new BasicDBObject()
                .append("te", term)
                .append("cid", classId)
                .append("ty", 1)
                .append("week", 0);

        BasicDBObject update = new BasicDBObject();
        update.append(Constant.MONGO_PULL, new BasicDBObject("cli",
                new BasicDBObject("id", courseItem.getId())));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query, update);
    }

    /**
     * 删除部分班级某个时间点的课
     *
     * @param term
     * @param classIdList
     * @param x
     * @param y
     */
    public void removeCourse(String term, List<ObjectId> classIdList, int x, int y) {
        BasicDBObject query = new BasicDBObject()
                .append("te", term)
                .append("cid", new BasicDBObject(Constant.MONGO_IN, classIdList))
                .append("ty", 1)
                .append("week", 0);
        BasicDBObject updatevalue = new BasicDBObject("xi", x).append("yi", y);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("cli", updatevalue));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query, update);
    }


    /**
     * 移除年级某个位置的课
     *
     * @param term
     * @param gradeId
     * @param x
     * @param y
     */
    public void removeCourse(String term, ObjectId gradeId, int x, int y, int type) {
        BasicDBObject query = new BasicDBObject()
                .append("te", term)
                .append("gid", gradeId)
                .append("ty", 1)
                .append("week", 0);
        BasicDBObject updatevalue = new BasicDBObject("xi", x).append("yi", y).append("ty", type);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("cli", updatevalue));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query, update);
    }

    /**
     * 删除全校课表某一行或某一列的课
     *
     * @param term
     * @param schoolId
     * @param x
     * @param y
     */
    public void removeCourseByXY(String term, ObjectId schoolId, int x, int y) {
        BasicDBObject query = new BasicDBObject()
                .append("te", term)
                .append("sid", schoolId)
                .append("ty", 1)
                .append("week", 0);
        BasicDBObject updatevalue = new BasicDBObject();
        if (x != -1) {//删除某行
            updatevalue.append("xi", x);
        }
        if (y != -1) {//删除某列
            updatevalue.append("yi", y);
        }
        BasicDBObject update = new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("cli", updatevalue));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query, update);
    }

    /**
     * 删除本年级课表
     *
     * @param term
     * @param gradeId
     * @param type 0:删除所有类型的课表
     */
    public void deleteTimetableByGradeId(String term, ObjectId gradeId, int type) {
        BasicDBObject query = new BasicDBObject()
                .append("te", term)
                .append("gid", gradeId);
        if (type != 0) {
            query.append("ty", type);
        }
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query);
    }

    /**
     * 删除年级某种类型的课
     *
     * @param term
     * @param gradeId
     * @param type
     */
    public void removeCourse(String term, ObjectId gradeId, int type) {
        BasicDBObject query = new BasicDBObject()
                .append("te", term)
                .append("gid", gradeId)
                .append("ty", 1)
                .append("week", 0);
        BasicDBObject updateValue = new BasicDBObject("cli", new BasicDBObject("ty", type));
        BasicDBObject update = new BasicDBObject(Constant.MONGO_PULL, updateValue);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query, update);
    }

    /**
     * 删除课表中某种类型的课
     *
     * @param term
     * @param classIds
     * @param type 0：删除所有课
     */
    public void removeCourse(String term, List<ObjectId> classIds, int type) {
        BasicDBObject query = new BasicDBObject()
                .append("te", term)
                .append("ty", 1)
                .append("week", 0)
                .append("cid", new BasicDBObject(Constant.MONGO_IN, classIds));

        BasicDBObject update = new BasicDBObject();

        if (type == 0) {
            BasicDBObject updateValue = new BasicDBObject("cli", MongoUtils.convert(MongoUtils.fetchDBObjectList(new ArrayList<CourseItem>())));
            update.append(Constant.MONGO_SET, updateValue);
        } else {
            BasicDBObject updateValue = new BasicDBObject("cli", new BasicDBObject("ty", type));
            update.append(Constant.MONGO_PULL, updateValue);
        }

        update(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query, update);
    }

    /**
     * 根据课程的id删除相关的课程
     * @param term
     * @param classIdList
     * @param couIds
     */
    public void removeCourseByIds(String term, List<ObjectId> classIdList, List<ObjectId> couIds){
        BasicDBObject query = new BasicDBObject("te", term)
                .append("ty", 1)
                .append("week", 0);
        if (classIdList != null) {
            query.append("cid", new BasicDBObject(Constant.MONGO_IN, classIdList));
        }

        BasicDBObject updateValue = new BasicDBObject("cli", new BasicDBObject("cou",
                new BasicDBObject(Constant.MONGO_ALL,couIds)));
        BasicDBObject update = new BasicDBObject(Constant.MONGO_PULL, updateValue);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query, update);
    }

    /**
     * 根据课程的id删除相关的课程
     * @param term
     * @param classId
     * @param courseId
     */
    public void removeCourseById(String term, ObjectId classId, ObjectId courseId){
        BasicDBObject query = new BasicDBObject("te", term)
                .append("ty", 1)
                .append("week", 0);
        if (classId != null) {
            query.append("cid", classId);
        }

        BasicDBObject updateValue = new BasicDBObject("cli", new BasicDBObject("cou", courseId));
        BasicDBObject update = new BasicDBObject(Constant.MONGO_PULL, updateValue);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query, update);
    }



    /**
     * 删除课表中的所有课
     *
     * @param term
     * @param gradeId
     */
    public void removeAllCourseItem(String term, ObjectId gradeId) {
        BasicDBObject query = new BasicDBObject("te", term).append("gid", gradeId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("cli", MongoUtils.convert(MongoUtils.fetchDBObjectList(new ArrayList<CourseItem>()))));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query, update);
    }

    /**
     * 删除全校课表
     * @param schoolId
     */
    public void deleteTimetableBySchoolId(ObjectId schoolId) {
        BasicDBObject query = new BasicDBObject("sid", schoolId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query);
    }


    /**
     * 删除已发布课表
     *
     * @param schoolId
     * @param gradeId
     */
    public void removeAllPublishedTimetable(ObjectId schoolId, ObjectId gradeId) {
        BasicDBObject query = new BasicDBObject("sid", schoolId)
                .append("gid", gradeId)
                .append("ty", 2);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query);
    }


    /**
     * 修改课表内容//TODO
     *
     * @param term
     * @param schoolId
     * @param classId
     * @param week
     * @param type
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
                .append("cli.cou", oldCourseIds);
        BasicDBObject updateValue = new BasicDBObject()
                .append(Constant.MONGO_SET, new BasicDBObject("cli.$.cou", courseIds));
        try {

            update(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE, query, updateValue);
        } catch (Exception e) {
            System.out.println(e);
        }
    }


}
