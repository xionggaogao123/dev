package com.db.zouban;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.utils.MongoUtils;
import com.pojo.zouban.ZouBanCourseEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by wang_xinxin on 2015/10/15.
 */
public class ZouBanCourseDao extends BaseDao {

    /**
     * 添加编好的班级
     *
     * @param zouBanCourseEntry
     * @return
     */
    public ObjectId addZouBanCourseEntry(ZouBanCourseEntry zouBanCourseEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, zouBanCourseEntry.getBaseEntry());
        return zouBanCourseEntry.getID();
    }

    /**
     * 删除走班课，主要用户兴趣班
     */
    public void deleteZoubanCourse(ObjectId courseId) {
        BasicDBObject query = new BasicDBObject();
        query.append(Constant.ID, courseId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query);
    }

    /**
     * 修改拓展课
     */
    public void updateZoubanCourse(ObjectId courseId, ZouBanCourseEntry zouBanCourseEntry) {
        BasicDBObject query = new BasicDBObject(Constant.ID, courseId);
        BasicDBObject updateValue = new BasicDBObject(
                Constant.MONGO_SET, new BasicDBObject("clsnm", zouBanCourseEntry.getClassName())
                .append("tid", zouBanCourseEntry.getTeacherId())
                .append("tnm", zouBanCourseEntry.getTeacherName())
                .append("subid", zouBanCourseEntry.getSubjectId())
                .append("lscnt", zouBanCourseEntry.getLessonCount())
                .append("max", zouBanCourseEntry.getMax())
                .append("time", MongoUtils.convert(MongoUtils.fetchDBObjectList(zouBanCourseEntry.getPointEntry())))
                .append("crid", zouBanCourseEntry.getClassRoomId())
                .append("grpid", zouBanCourseEntry.getGroupId())
        );
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, updateValue);
    }

    /**
     * 获取编班列表
     *
     * @param term
     * @param gradeId
     * @param groupId
     * @param subjectId
     * @return
     */
    public List<ZouBanCourseEntry> findBianBanList(String term, ObjectId gradeId, ObjectId groupId, ObjectId subjectId, ObjectId schoolid, int type) {
        List<ZouBanCourseEntry> retList = new ArrayList<ZouBanCourseEntry>();
        List<DBObject> list = new ArrayList<DBObject>();

        BasicDBObject query = new BasicDBObject("sid", schoolid);
        if (subjectId != null) {
            query.append("subid", subjectId);
        }
        query.append("te", term);
        query.append("gid", gradeId);
        if (type == 1) {
            if (groupId != null) {
                query.append("grpid", groupId);
            }
        }
        query.append("type", type);
        list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, null, new BasicDBObject("grpid", 1).append("clsnm", 1));
        for (DBObject dbo : list) {
            retList.add(new ZouBanCourseEntry((BasicDBObject) dbo));
        }
        return retList;
    }

    /**
     * 删除班级
     *
     * @param term
     * @param gradeId
     * @param schoolId
     */
    public void removeZouBanCourseEntry(String term, ObjectId gradeId, ObjectId schoolId) {
        BasicDBObject query = new BasicDBObject("te", term).append("gid", gradeId).append("sid", schoolId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query);
    }

    public void removeZouBanCourseEntry(String term, ObjectId gradeId, ObjectId schoolId, int type) {
        BasicDBObject query = new BasicDBObject("te", term).append("gid", gradeId).append("sid", schoolId).append("type", type);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query);
    }

    /**
     * 班级列表
     *
     * @param courseClassId
     * @return
     */
    public ZouBanCourseEntry findClassStudentList(ObjectId courseClassId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, courseClassId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, null);
        if (null != dbo) {
            return new ZouBanCourseEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * 更新班级的老师、教室
     *
     * @param courseClassId
     * @param teacherId
     * @param classRoomId
     */
    public void updateClassCourseInfo(ObjectId courseClassId, ObjectId teacherId, ObjectId classRoomId, String teacherName, int weekcnt, int type) {
        DBObject query = new BasicDBObject(Constant.ID, courseClassId);
        BasicDBObject value = new BasicDBObject("tid", teacherId).append("tnm", teacherName);
        if (classRoomId != null) {
            value.append("crid", classRoomId);
        }
        if (type == 2) {
            value.append("lscnt", weekcnt);
        }
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, value);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, updateValue);
    }

    /**
     * 更新走班课教室---设置为null
     *
     * @param courseClassId
     */
    public void updateZoubanStudentsOrRoom(ObjectId courseClassId, int type) {
        DBObject query = new BasicDBObject(Constant.ID, courseClassId);
        BasicDBObject value = new BasicDBObject();
        if (type == 1) {
            value.append("crid", null);
        } else if (type == 2) {
            value.append("stus", MongoUtils.convert(new ArrayList<Object>()));
        }
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, value);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, updateValue);
    }

    /**
     * 获取全校的走班课
     *
     * @param term
     * @param schoolId
     * @param type
     * @return
     */
    public List<ZouBanCourseEntry> findCourseListBySchoolId(String term, ObjectId schoolId, int type) {
        List<ZouBanCourseEntry> zouBanCourseEntryList = new ArrayList<ZouBanCourseEntry>();
        BasicDBObject query = new BasicDBObject()
                .append("sid", schoolId)
                .append("te", term)
                .append("type", type);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, Constant.FIELDS);
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                zouBanCourseEntryList.add(new ZouBanCourseEntry((BasicDBObject) dbObject));
            }
        }
        return zouBanCourseEntryList;
    }

    /**
     * 获取某段内的所有走班课
     *
     * @param term
     * @param groupId
     * @param type
     * @return
     */
    public List<ZouBanCourseEntry> findCourseListByGroup(String term, ObjectId groupId, int type) {
        List<ZouBanCourseEntry> zouBanCourseEntryList = new ArrayList<ZouBanCourseEntry>();
        BasicDBObject query = new BasicDBObject()
                .append("grpid", groupId)
                .append("te", term)
                .append("type", type);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, Constant.FIELDS);
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                zouBanCourseEntryList.add(new ZouBanCourseEntry((BasicDBObject) dbObject));
            }
        }
        return zouBanCourseEntryList;
    }

    /**
     * 批量设置学生
     *
     * @param courseClassId
     * @param users
     */
    public void updateSetClassCourseUserInfo(ObjectId courseClassId, List<ObjectId> users) {
        BasicDBObject query = new BasicDBObject(Constant.ID, courseClassId);
        BasicDBObject updateValue = new BasicDBObject();
        updateValue.append(Constant.MONGO_SET, new BasicDBObject("stus", MongoUtils.convert(users)));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, updateValue);
    }

    /**
     * 添加学生
     *
     * @param courseClassId
     * @param users
     */
    public void updateAddClassCourseUserInfo(String courseClassId, List<ObjectId> users) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new ObjectId(courseClassId));
        BasicDBObject updateValue = new BasicDBObject();
        updateValue.append(Constant.MONGO_PUSHALL, new BasicDBObject("stus", MongoUtils.convert(users)));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, updateValue);
    }

    /**
     * 删除学生
     *
     * @param orgcourseClassId
     * @param users
     */
    public void updateMissClassCourseUserInfo(String orgcourseClassId, List<ObjectId> users) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new ObjectId(orgcourseClassId));
        BasicDBObject updateValue = new BasicDBObject();
        updateValue.append(Constant.MONGO_PULLALL, new BasicDBObject("stus", MongoUtils.convert(users)));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, updateValue);
    }

    /**
     * 批量获取zoubancourseEntry
     * add by mq
     *
     * @param ids
     * @return
     */
    public List<ZouBanCourseEntry> findCourseListByIds(List<ObjectId> ids) {
        List<ZouBanCourseEntry> zouBanCourseEntryList = new ArrayList<ZouBanCourseEntry>();
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids));
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, Constant.FIELDS);
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                zouBanCourseEntryList.add(new ZouBanCourseEntry((BasicDBObject) dbObject));
            }
        }
        return zouBanCourseEntryList;
    }

    /**
     * 通过课id，找到相对应的课程
     *
     * @param subjectId
     * @param groupId
     * @param coursename
     * @param id
     * @return
     */
    public List<ZouBanCourseEntry> findZouBanCourseList(ObjectId subjectId, ObjectId groupId, String coursename, ObjectId id) {
        List<ZouBanCourseEntry> zouBanCourseEntryList = new ArrayList<ZouBanCourseEntry>();
        Pattern pattern = Pattern.compile("^.*" + coursename + ".*$", Pattern.MULTILINE);
        BasicDBObject query = new BasicDBObject("subid", subjectId).append("grpid", groupId).append(Constant.ID, new BasicDBObject("$ne", id)).append("clsnm", new BasicDBObject(Constant.MONGO_REGEX, pattern));
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, Constant.FIELDS);
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
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
     * 通过；老师id列表获取课程列表
     *
     * @param term
     * @param teacherIdList
     * @return
     */
    public List<ZouBanCourseEntry> findCourseListByTeacherIdList(String term, List<ObjectId> teacherIdList) {
        List<ZouBanCourseEntry> zouBanCourseEntryList = new ArrayList<ZouBanCourseEntry>();
        BasicDBObject query = new BasicDBObject()
                .append("te", term)
                .append("tid", new BasicDBObject(Constant.MONGO_IN, teacherIdList));
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, Constant.FIELDS);
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                zouBanCourseEntryList.add(new ZouBanCourseEntry((BasicDBObject) dbObject));
            }
        }
        return zouBanCourseEntryList;
    }

    /**
     * 通过年级id获取课程列表
     *
     * @param term
     * @param gradeId
     * @return
     */
    public List<ZouBanCourseEntry> findCourseListByGradeId(String term, ObjectId gradeId) {
        List<ZouBanCourseEntry> zouBanCourseEntryList = new ArrayList<ZouBanCourseEntry>();
        BasicDBObject query = new BasicDBObject()
                .append("te", term)
                .append("gid", gradeId)
                .append("type", 1);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, Constant.FIELDS);
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                zouBanCourseEntryList.add(new ZouBanCourseEntry((BasicDBObject) dbObject));
            }
        }
        return zouBanCourseEntryList;
    }

    /**
     * 通过年级id获取课程列表
     *
     * @param term
     * @param gradeId
     * @return
     */
    public List<ZouBanCourseEntry> findInterestCourseListByGradeId(String term, ObjectId gradeId) {
        List<ZouBanCourseEntry> zouBanCourseEntryList = new ArrayList<ZouBanCourseEntry>();
        BasicDBObject query = new BasicDBObject()
                .append("te", term)
                .append("gid", gradeId)
                .append("type", 6);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, Constant.FIELDS);
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                zouBanCourseEntryList.add(new ZouBanCourseEntry((BasicDBObject) dbObject));
            }
        }
        return zouBanCourseEntryList;
    }

    /**
     * 通过年级 学科id查询
     *
     * @param term
     * @param gradeId
     * @param subjectId
     * @return
     */
    public List<ZouBanCourseEntry> findCourseListByGradeIdSubId(String term, ObjectId gradeId, ObjectId subjectId) {
        List<ZouBanCourseEntry> zouBanCourseEntryList = new ArrayList<ZouBanCourseEntry>();
        BasicDBObject query = new BasicDBObject()
                .append("te", term)
                .append("gid", gradeId)
                .append("subid", subjectId);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, Constant.FIELDS);
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                zouBanCourseEntryList.add(new ZouBanCourseEntry((BasicDBObject) dbObject));
            }
        }
        return zouBanCourseEntryList;
    }

    public List<ZouBanCourseEntry> findCourseListByGradeIdSubIdType(String term, ObjectId gradeId, ObjectId subjectId, int type) {
        List<ZouBanCourseEntry> zouBanCourseEntryList = new ArrayList<ZouBanCourseEntry>();
        BasicDBObject query = new BasicDBObject()
                .append("te", term)
                .append("gid", gradeId)
                .append("type", type)
                .append("subid", subjectId);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, Constant.FIELDS);
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                zouBanCourseEntryList.add(new ZouBanCourseEntry((BasicDBObject) dbObject));
            }
        }
        return zouBanCourseEntryList;
    }

    /**
     * 通过年级id获取课程列表
     *
     * @param term
     * @param gradeId
     * @return
     */
    public List<ZouBanCourseEntry> findCourseListBySchoolGradeId(String term, ObjectId gradeId, ObjectId schoolId) {
        List<ZouBanCourseEntry> zouBanCourseEntryList = new ArrayList<ZouBanCourseEntry>();
        BasicDBObject query = new BasicDBObject()
                .append("te", term)
                .append("gid", gradeId)
                .append("sid", schoolId);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, Constant.FIELDS);
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                zouBanCourseEntryList.add(new ZouBanCourseEntry((BasicDBObject) dbObject));
            }
        }
        return zouBanCourseEntryList;
    }

    public List<ZouBanCourseEntry> findCourseListBySchoolGradeId(String term, ObjectId gradeId, ObjectId schoolId, int type) {
        List<ZouBanCourseEntry> zouBanCourseEntryList = new ArrayList<ZouBanCourseEntry>();
        BasicDBObject query = new BasicDBObject()
                .append("te", term)
                .append("gid", gradeId)
                .append("sid", schoolId)
                .append("type", type);
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
     * 通过id获取课程
     */
    public ZouBanCourseEntry findCourseById(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, Constant.FIELDS);
        return new ZouBanCourseEntry((BasicDBObject) dbObject);
    }


    /**
     * 删除一个年级下的所有小走班课或体育走班
     *
     * @param year
     * @param gradeId
     */
    public void deleteXZBinGrade(String year, ObjectId gradeId, int type) {
        BasicDBObject query = new BasicDBObject();
        query.append("te", year);
        query.append("gid", gradeId);
        query.append("type", type);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query);
    }

    /**
     * 添加学生
     *
     * @param year
     * @param gradeId
     * @param courseId
     * @param users
     */
    public void addStudentById(String year, String gradeId, ObjectId courseId, ObjectId groupId, List<ObjectId> users) {
        BasicDBObject query = new BasicDBObject("te", year)
                .append("gid", new ObjectId(gradeId))
                .append("_id", courseId);
        BasicDBObject updateValue = new BasicDBObject();
        /*updateValue.append(Constant.MONGO_PUSHALL,new BasicDBObject("stus", MongoUtils.convert(users)))
        .append(Constant.MONGO_SET,new BasicDBObject("group",groupId));*/
        updateValue.append(Constant.MONGO_SET, new BasicDBObject("stus", MongoUtils.convert(users)).append("group", groupId));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, updateValue);
    }


    /**
     * 通过id获取课程信息
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
     * 通过学生id后期课程
     */
    public List<ZouBanCourseEntry> getStudentCourse(ObjectId studentId) {
        BasicDBObject query = new BasicDBObject("stus", studentId).append("type", 6);
        List<ZouBanCourseEntry> entryList = new ArrayList<ZouBanCourseEntry>();
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_COURSE, query, Constant.FIELDS);
        for (DBObject dbo : dbObjectList) {
            entryList.add(new ZouBanCourseEntry((BasicDBObject) dbo));
        }
        return entryList;
    }


}
