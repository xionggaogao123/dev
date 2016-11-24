package com.db.zouban;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.zouban.CourseConfEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiangm on 2015/9/15.
 */
public class CourseConfDao extends BaseDao {
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
     * 根据学期和年级id获取课表配置
     *
     * @param term
     * @return
     */
    public CourseConfEntry findCourseConfByGradeId(String term, ObjectId gradeId) {
        BasicDBObject query = new BasicDBObject()
                .append("te", term)
                .append("gid", gradeId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_CONF, query, Constant.FIELDS);
        if (dbObject == null)
            return null;
        return new CourseConfEntry((BasicDBObject) dbObject);
    }

    /**
     * 获取课表配置(来凤专用)
     *
     * @param schoolId
     * @param term
     * @return
     */
    public CourseConfEntry findCourseConfBySchool(ObjectId schoolId, String term) {
        BasicDBObject query = new BasicDBObject()
                .append("sid", schoolId)
                .append("te", term);
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
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_CONF, query, Constant.FIELDS);
        return new CourseConfEntry((BasicDBObject) dbObject);
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
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_CONF, query);
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
