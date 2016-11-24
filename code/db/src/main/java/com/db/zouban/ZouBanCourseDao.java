package com.db.zouban;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.utils.MongoUtils;
import com.pojo.zouban.ZouBanCourseEntry;
import com.pojo.zouban.ZoubanType;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * Created by wang_xinxin on 2015/10/15.
 */
public class ZouBanCourseDao extends BaseDao {

    /**
     * 添加课程
     *
     * @param zouBanCourseEntry
     * @return
     */
    public ObjectId addZouBanCourseEntry(ZouBanCourseEntry zouBanCourseEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, zouBanCourseEntry.getBaseEntry());
        return zouBanCourseEntry.getID();
    }

    /**
     * 修改拓展课
     */
    public void updateZoubanCourse(ZouBanCourseEntry zouBanCourseEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, zouBanCourseEntry.getBaseEntry());
    }


    /**
     * 更新课时
     *
     * @param term
     * @param gradeId
     * @param subjectId
     * @param type
     * @param lessonCount
     */
    public void updateZoubanCourse(String term, ObjectId gradeId, ObjectId subjectId, int type, int lessonCount) {
        DBObject query = new BasicDBObject("te", term)
                .append("gid", gradeId)
                .append("subid", subjectId)
                .append("type", type);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("lscnt", lessonCount));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, updateValue);
    }


    public void updateZoubanCourseBySubConfId(String term, ObjectId gradeId, ObjectId subConfId, int type, int lessonCount) {
        DBObject query = new BasicDBObject("te", term)
                .append("gid", gradeId)
                .append("scid", subConfId)
                .append("type", type);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("lscnt", lessonCount));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, updateValue);
    }
    /**
     * 更新老师
     *
     * @param id
     * @param teacherId
     * @param teacherName
     */
    public void updateTeacher(ObjectId id, ObjectId teacherId, String teacherName) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject updateValue = new BasicDBObject("tid", teacherId).append("tnm", teacherName);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, updateValue);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, update);
    }

    /**
     * 更新教室
     *
     * @param id
     * @param classroomId
     */
    public void updateClassroom(ObjectId id, ObjectId classroomId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject updateValue = new BasicDBObject("crid", classroomId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, updateValue);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, update);
    }


    /**
     * 更新老师和教室
     *
     * @param id
     * @param teacherId
     * @param teacherName
     * @param classRoomId
     */
    public void updateTeacherAndClassRoom(ObjectId id, ObjectId teacherId, String teacherName, ObjectId classRoomId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject update = new BasicDBObject("tid", teacherId)
                .append("tnm", teacherName)
                .append("crid", classRoomId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, update);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, updateValue);
    }

    /**
     * 更新老师和课时
     *
     * @param id
     */
    public void updateTeacherAndLessonCount(ObjectId id, ObjectId teacherId, String teacherName, int lessonCount) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject update = new BasicDBObject("tid", teacherId)
                .append("tnm", teacherName)
                .append("lscnt", lessonCount);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, update);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, updateValue);
    }

    /**
     * 重命名
     *
     * @param courseId
     * @param courseName
     */
    public void updateName(ObjectId courseId, String courseName) {
        BasicDBObject query = new BasicDBObject(Constant.ID, courseId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("clsnm", courseName));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, updateValue);
    }


    /**
     * 更新教学班关联的行政班
     *
     * @param courseId
     * @param classIdList
     */
    public void updateClassIds(ObjectId courseId, List<ObjectId> classIdList) {
        BasicDBObject query = new BasicDBObject(Constant.ID, courseId);
        BasicDBObject updateValue = new BasicDBObject("cid", classIdList);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, updateValue);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, update);
    }


    /**
     * 删除班级
     *
     * @param term
     * @param gradeId
     */
    public void removeCourse(String term, ObjectId gradeId) {
        BasicDBObject query = new BasicDBObject("te", term).append("gid", gradeId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query);
    }

    /**
     * 删除教学班
     *
     * @param term
     * @param gradeId
     * @param type
     */
    public void removeCourseByType(String term, ObjectId gradeId, int type) {
        BasicDBObject query = new BasicDBObject("te", term)
                .append("gid", gradeId)
                .append("type", type);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query);
    }

    /**
     * 删除某学科的走班课
     *
     * @param term
     * @param gradeId
     * @param type
     * @param subjectId
     */
    public void removeCourseBySubjectId(String term, ObjectId gradeId, int type, ObjectId subjectId,ObjectId subjectConfId) {
        BasicDBObject query = new BasicDBObject("te", term)
                .append("gid", gradeId)
                .append("type", type)
                .append("subid", subjectId);
        if(null!=subjectConfId){
            query.append("scid",subjectConfId);
        }
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query);
    }

    /**
     * 删除某个分段的走班课
     *
     * @param term
     * @param gradeId
     * @param groupId //分段id
     */
    public void removeCourseByGroupId(String term, ObjectId gradeId, ObjectId groupId) {
        BasicDBObject query = new BasicDBObject("te", term)
                .append("gid", gradeId)
                .append("type", ZoubanType.ZOUBAN.getType());
        if (groupId != null) {
            query.append("grpid", groupId);
        }
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query);
    }


    /**
     * 删除教学班(课程)
     *
     * @param ids
     */
    public void removeCourseByIds(Collection<ObjectId> ids) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids));
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query);
    }


    /**
     * 删除课程，主要用户兴趣班
     */
    public void removeCourseById(ObjectId courseId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, courseId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query);
    }


    /**
     * 获取课程列表
     *
     * @param term
     * @param gradeId
     * @param groupId
     * @param subjectId
     * @param type
     * @return
     */
    public List<ZouBanCourseEntry> findCourseListBySubjectId(String term, ObjectId gradeId, ObjectId groupId, ObjectId subjectId, int type) {
        List<ZouBanCourseEntry> retList = new ArrayList<ZouBanCourseEntry>();

        BasicDBObject query = new BasicDBObject()
                .append("te", term)
                .append("gid", gradeId)
                .append("type", type);
        if (subjectId != null) {
            query.append("subid", subjectId);
        }
        if (type == 1) {
            if (groupId != null) {
                query.append("grpid", groupId);
            }
        } else if (type == 7) {//分层走班group是分组，不是分段
            if (groupId != null) {
                query.append("group", groupId);
            }
        }
        BasicDBObject orderBy = new BasicDBObject("grpid", 1).append("group", 1).append("clsnm", 1);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, Constant.FIELDS, orderBy);
        for (DBObject dbo : list) {
            retList.add(new ZouBanCourseEntry((BasicDBObject) dbo));
        }
        return retList;
    }

    /**
     * 通过学科配置查询教学班
     *
     * @param term
     * @param gradeId
     * @param groupId
     * @param subConfigId
     * @param type
     * @return
     */
    public List<ZouBanCourseEntry> findCourseListBySubConfigId(String term, ObjectId gradeId, ObjectId groupId, ObjectId subConfigId, int type) {
        List<ZouBanCourseEntry> retList = new ArrayList<ZouBanCourseEntry>();

        BasicDBObject query = new BasicDBObject()
                .append("te", term)
                .append("gid", gradeId)
                .append("type", type);
        if (subConfigId != null) {
            query.append("scid", subConfigId);
        }
        if (type == 1) {
            if (groupId != null) {
                query.append("grpid", groupId);
            }
        } else if (type == 7) {
            if (groupId != null) {
                query.append("group", groupId);
            }
        }
        BasicDBObject orderBy = new BasicDBObject("grpid", 1).append("group", 1).append("clsnm", 1);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, Constant.FIELDS, orderBy);
        for (DBObject dbo : list) {
            retList.add(new ZouBanCourseEntry((BasicDBObject) dbo));
        }
        return retList;
    }
    /**
     * 查询走班课
     *
     * @param term
     * @param gradeId
     * @param groupId
     * @return
     */
    public List<ZouBanCourseEntry> findCourseList(String term, ObjectId gradeId, ObjectId groupId) {
        List<ZouBanCourseEntry> retList = new ArrayList<ZouBanCourseEntry>();

        BasicDBObject query = new BasicDBObject()
                .append("te", term)
                .append("gid", gradeId)
                .append("type", 1);
        if (groupId != null) {
            query.append("grpid", groupId);
        }
        BasicDBObject orderBy = new BasicDBObject(Constant.ID, 1).append("grpid", 1).append("group", 1).append("clsnm", 1);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, Constant.FIELDS, orderBy);
        for (DBObject dbo : list) {
            retList.add(new ZouBanCourseEntry((BasicDBObject) dbo));
        }
        return retList;
    }

    /**
     * 查询走班课
     *
     * @param term
     * @param gradeId
     * @param courseName
     * @return
     */
    public List<ZouBanCourseEntry> findCourseList(String term, ObjectId gradeId, String courseName) {
        List<ZouBanCourseEntry> retList = new ArrayList<ZouBanCourseEntry>();

        BasicDBObject query = new BasicDBObject()
                .append("te", term)
                .append("gid", gradeId)
                .append("type", 1)
                .append("clsnm", courseName);
        BasicDBObject orderBy = new BasicDBObject("grpid", 1).append("group", 1).append("clsnm", 1);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, Constant.FIELDS, orderBy);
        for (DBObject dbo : list) {
            retList.add(new ZouBanCourseEntry((BasicDBObject) dbo));
        }
        return retList;
    }

    /**
     * 查询走班课
     *
     * @param term
     * @param gradeId
     * @param groupId
     * @param level
     * @return
     */
    public List<ZouBanCourseEntry> findCourseList(String term, ObjectId gradeId, ObjectId groupId, int level) {
        List<ZouBanCourseEntry> retList = new ArrayList<ZouBanCourseEntry>();

        BasicDBObject query = new BasicDBObject()
                .append("te", term)
                .append("gid", gradeId)
                .append("type", 1)
                .append("lv", level);
        if (groupId != null) {
            query.append("grpid", groupId);
        }
        BasicDBObject orderBy = new BasicDBObject("grpid", 1).append("group", 1).append("clsnm", 1);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, Constant.FIELDS, orderBy);
        for (DBObject dbo : list) {
            retList.add(new ZouBanCourseEntry((BasicDBObject) dbo));
        }
        return retList;
    }


    /**
     * 通过年级id获取课程列表
     *
     * @param term
     * @param gradeId
     * @param type
     * @return
     */
    public List<ZouBanCourseEntry> findCourseList(String term, ObjectId gradeId, int type) {
        List<ZouBanCourseEntry> zouBanCourseEntryList = new ArrayList<ZouBanCourseEntry>();
        BasicDBObject query = new BasicDBObject()
                .append("te", term)
                .append("gid", gradeId);
        if (type != -1) {
            query.append("type", type);
        }
        BasicDBObject orderBy = new BasicDBObject(Constant.ID, 1)
                .append("grpid", 1)
                .append("group", 1)
                .append("clsnm", 1);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, Constant.FIELDS, orderBy);
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                zouBanCourseEntryList.add(new ZouBanCourseEntry((BasicDBObject) dbObject));
            }
        }
        return zouBanCourseEntryList;
    }

    /**
     * 查询某个逻辑位置的教学班
     *
     * @param group
     * @return
     */
    public List<ZouBanCourseEntry> findCourseList(ObjectId group) {
        BasicDBObject query = new BasicDBObject("group", group);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, Constant.FIELDS);
        List<ZouBanCourseEntry> zouBanCourseEntryList = new ArrayList<ZouBanCourseEntry>();

        for (DBObject dbObject : dbObjectList) {
            zouBanCourseEntryList.add(new ZouBanCourseEntry((BasicDBObject) dbObject));
        }
        return zouBanCourseEntryList;
    }


    /**
     * 获取某段内的所有课
     *
     * @param term
     * @param type
     * @param groupId
     * @return
     */
    public List<ZouBanCourseEntry> findCourseListByGroupId(String term, ObjectId groupId, int type) {
        List<ZouBanCourseEntry> zouBanCourseEntries = new ArrayList<ZouBanCourseEntry>();
        BasicDBObject query = new BasicDBObject();
        query.append("te", term);
        query.append("type", type);
        if (groupId != null && !groupId.equals("")) {
            query.append("grpid", groupId);
        }
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, Constant.FIELDS);
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                zouBanCourseEntries.add(new ZouBanCourseEntry((BasicDBObject) dbObject));
            }
        }
        return zouBanCourseEntries;
    }

    /**
     * 通过group字段获取course
     * @param term
     * @param groupId
     * @param type
     * @return
     */
    public List<ZouBanCourseEntry> findCourseListByGroup(String term, ObjectId groupId, int type) {
        List<ZouBanCourseEntry> zouBanCourseEntries = new ArrayList<ZouBanCourseEntry>();
        BasicDBObject query = new BasicDBObject();
        query.append("te", term);
        query.append("type", type);
        if (groupId != null && !groupId.equals("")) {
            query.append("group", groupId);
        }
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, Constant.FIELDS);
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                zouBanCourseEntries.add(new ZouBanCourseEntry((BasicDBObject) dbObject));
            }
        }
        return zouBanCourseEntries;
    }
    /**
     * 根据逻辑位置获取教学班
     *
     * @param term
     * @param gradeId
     * @param group
     * @param type
     * @return
     */
    public List<ZouBanCourseEntry> findCourseListByGroup(String term, ObjectId gradeId, ObjectId group, int type) {
        List<ZouBanCourseEntry> zouBanCourseEntries = new ArrayList<ZouBanCourseEntry>();
        BasicDBObject query = new BasicDBObject()
                .append("te", term)
                .append("gid", gradeId)
                .append("group", group)
                .append("type", type);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, Constant.FIELDS);
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                zouBanCourseEntries.add(new ZouBanCourseEntry((BasicDBObject) dbObject));
            }
        }
        return zouBanCourseEntries;
    }

    /**
     * 通过id获取课程信息
     *
     * @param id
     * @return
     */
    public ZouBanCourseEntry getCourseInfoById(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, Constant.FIELDS);
        if (dbo == null) {
            return null;
        } else {
            return new ZouBanCourseEntry((BasicDBObject) dbo);
        }
    }


    /**
     * 查询同一逻辑位置同一学科的另一个教学班
     *
     * @param subjectId
     * @param group
     * @param courseId
     * @return
     */
    public List<ZouBanCourseEntry> findAvailableCourse(ObjectId subjectId, ObjectId group, ObjectId courseId) {
        if (group == null) {
            return new ArrayList<ZouBanCourseEntry>();
        }
        BasicDBObject query = new BasicDBObject()
                .append("subid", subjectId)
                .append("group", group)
                .append(Constant.ID, new BasicDBObject(Constant.MONGO_NE, courseId));
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, Constant.FIELDS);
        List<ZouBanCourseEntry> zouBanCourseEntryList = new ArrayList<ZouBanCourseEntry>();
        if (dbObjectList != null) {
            for (DBObject dbObject : dbObjectList) {
                zouBanCourseEntryList.add(new ZouBanCourseEntry((BasicDBObject) dbObject));
            }
        }
        return zouBanCourseEntryList;
    }

    /**
     * 根据课程id列表获取课程信息
     *
     * @param ids
     * @return
     */
    public List<ZouBanCourseEntry> findCourseListByIds(List<ObjectId> ids) {
        List<ZouBanCourseEntry> zouBanCourseEntryList = new ArrayList<ZouBanCourseEntry>();
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids));
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, Constant.FIELDS);
        if (dbObjectList != null) {
            for (DBObject dbObject : dbObjectList) {
                zouBanCourseEntryList.add(new ZouBanCourseEntry((BasicDBObject) dbObject));
            }
        }
        return zouBanCourseEntryList;
    }


    /**
     * 通过老师id获取课程列表
     *
     * @param term
     * @param teacherId
     * @return
     */
    public List<ZouBanCourseEntry> findCourseListByTeacherId(String term, ObjectId teacherId, boolean isZhuzhou) {
        List<ZouBanCourseEntry> zouBanCourseEntryList = new ArrayList<ZouBanCourseEntry>();
        BasicDBObject query = new BasicDBObject()
                .append("te", term)
                .append("tid", teacherId);
        if (isZhuzhou) {
            query.append("type", 6);
        }
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, Constant.FIELDS);
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                zouBanCourseEntryList.add(new ZouBanCourseEntry((BasicDBObject) dbObject));
            }
        }
        return zouBanCourseEntryList;
    }

    /**
     * 考勤获取课程列表
     *
     * @param term
     * @param gradeId
     * @param courseType
     * @param subject
     * @param teacherId
     * @param schoolId
     */
    public List<ZouBanCourseEntry> findCourseList(String term, ObjectId gradeId, int courseType, String subject, ObjectId teacherId, ObjectId schoolId, int skip, int limit) {
        BasicDBObject query = new BasicDBObject()
                .append("te", term)
                .append("gid", gradeId)
                .append("sid", schoolId);
        if (courseType == 6) {
            query.append("clsnm", subject);
            query.append("type", courseType);
        } else {
            query.append("subid", new ObjectId(subject));
            if (courseType == 0) {
                List<Integer> typeList = new ArrayList<Integer>();
                typeList.add(1);
                typeList.add(2);
                typeList.add(6);
                query.append("type", new BasicDBObject(Constant.MONGO_NOTIN, typeList));
            } else {
                query.append("type", courseType);
            }
        }


        if (teacherId != null) {
            query.append("tid", teacherId);
        }
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, Constant.FIELDS, Constant.MONGO_SORTBY_ASC, skip, limit);

        List<ZouBanCourseEntry> zouBanCourseEntryList = new ArrayList<ZouBanCourseEntry>();
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                zouBanCourseEntryList.add(new ZouBanCourseEntry((BasicDBObject) dbObject));
            }
        }
        return zouBanCourseEntryList;
    }

    /**
     * 考勤获取课程数量
     */
    public int courseListCount(String term, ObjectId gradeId, int courseType, String subject, ObjectId teacherId, ObjectId schoolId) {
        BasicDBObject query = new BasicDBObject()
                .append("te", term)
                .append("gid", gradeId)
                .append("sid", schoolId);
        if (courseType == 6) {
            query.append("clsnm", subject);
            query.append("type", courseType);
        } else {
            query.append("subid", new ObjectId(subject));
            if (courseType == 0) {
                List<Integer> typeList = new ArrayList<Integer>();
                typeList.add(1);
                typeList.add(2);
                typeList.add(6);
                query.append("type", new BasicDBObject(Constant.MONGO_NOTIN, typeList));
            } else {
                query.append("type", courseType);
            }
        }

        if (teacherId != null) {
            query.append("tid", teacherId);
        }
        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query);

        return count;
    }


    /**
     * 批量设置学生
     *
     * @param courseClassId
     * @param users
     */
    public void updateSetClassCourseUserInfo(ObjectId courseClassId, List<ObjectId> users) {
        BasicDBObject query = new BasicDBObject(Constant.ID, courseClassId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("stus", MongoUtils.convert(users)));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, updateValue);
    }

    /**
     * 添加学生
     *
     * @param courseId
     * @param studentId
     */
    public void addStudent(ObjectId courseId, ObjectId studentId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, courseId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_PUSH, new BasicDBObject("stus", studentId));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, updateValue);
    }


    /**
     * 添加学生
     * @param courseId
     * @param stuIds
     */
    public void addStudents(ObjectId courseId, List<ObjectId> stuIds) {
        BasicDBObject query = new BasicDBObject(Constant.ID, courseId);
        BasicDBObject updateValue = new BasicDBObject("stus", MongoUtils.convert(stuIds));
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, updateValue);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, update);
    }

    /**
     * 删除学生
     *
     * @param courseId
     * @param studentId
     */
    public void removeStudent(ObjectId courseId, ObjectId studentId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, courseId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("stus", studentId));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, updateValue);
    }


    /**
     * 通过学生id获取选修课
     */
    public List<ZouBanCourseEntry> getStudentCourse(String term, ObjectId studentId) {
        BasicDBObject query = new BasicDBObject("stus", studentId).append("te", term);
        List<ZouBanCourseEntry> entryList = new ArrayList<ZouBanCourseEntry>();
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, Constant.FIELDS);
        for (DBObject dbo : dbObjectList) {
            entryList.add(new ZouBanCourseEntry((BasicDBObject) dbo));
        }
        return entryList;
    }

    /**
     * 获取学生选择的课
     *
     * @param stuId
     * @param term
     * @param gradeId
     * @return
     */
    public List<ZouBanCourseEntry> getStudentChooseZB(ObjectId stuId, String term, ObjectId gradeId) {
        BasicDBObject query = new BasicDBObject();
        query.append("te", term);
        query.append("gid", gradeId);
        query.append("stus", stuId);
        List<ZouBanCourseEntry> zouBanCourseEntries = new ArrayList<ZouBanCourseEntry>();
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, Constant.FIELDS);
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                zouBanCourseEntries.add(new ZouBanCourseEntry((BasicDBObject) dbObject));
            }
        }
        return zouBanCourseEntries;
    }

    /**
     * 获取学生选择的课
     * @param stuId
     * @param term
     * @param gradeId
     * @param subjectId
     * @return
     */
    public ZouBanCourseEntry getStudentChooseZB(ObjectId stuId, String term, ObjectId gradeId, ObjectId subjectId) {
        BasicDBObject query = new BasicDBObject();
        query.append("te", term);
        query.append("gid", gradeId);
        query.append("stus", stuId);
        query.append("subid", subjectId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, Constant.FIELDS);
        if(dbObject != null){
            return new ZouBanCourseEntry((BasicDBObject)dbObject);
        }
        return null;
    }


    /**
     * 获取未排课的非走班课程
     *
     * @param term
     * @param classId
     * @return
     */
    public List<ZouBanCourseEntry> getCourseListByClassId(String term, ObjectId classId, int type) {
        List<ZouBanCourseEntry> zouBanCourseEntries = new ArrayList<ZouBanCourseEntry>();
        BasicDBObject query = new BasicDBObject()
                .append("te", term)
                .append("cid", classId)
                .append("type", type);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, Constant.FIELDS);
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                zouBanCourseEntries.add(new ZouBanCourseEntry((BasicDBObject) dbObject));
            }
        }
        return zouBanCourseEntries;
    }

    /**
     * 更新体育课
     *
     * @param id
     * @param classIds
     * @param className
     * @param teacherId
     * @param studentIdList
     */
    public void updatePECourse(ObjectId id, List<ObjectId> classIds, String className, ObjectId teacherId, String teacherName, List<ObjectId> studentIdList, int lessonCount) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject update = new BasicDBObject("cid", classIds)
                .append("clsnm", className)
                .append("tid", teacherId)
                .append("tnm", teacherName)
                .append("stus", studentIdList)
                .append("lscnt", lessonCount);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, update);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, updateValue);
    }


    /**
     * 删除非法教学班
     *
     * @param term
     * @param schoolId
     * @return
     */
    public int removeInvalidCourse(String term, ObjectId schoolId) {
        BasicDBObject query = new BasicDBObject("te", term)
                .append("sid", schoolId)
                .append("type", 2);
        BasicDBList basicDBList = new BasicDBList();
        basicDBList.add(new BasicDBObject("tnm", ""));
        basicDBList.add(new BasicDBObject("tnm", null));
        basicDBList.add(new BasicDBObject("crid", null));
        query.append(Constant.MONGO_OR, basicDBList);
        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query);
        return count;
    }

}
