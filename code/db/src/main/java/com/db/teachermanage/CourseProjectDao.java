package com.db.teachermanage;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.teachermanage.CourseProjectEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang_xinxin on 2016/2/29.
 */
public class CourseProjectDao extends BaseDao {

    /**
     * 增加教师课程项目
     *
     * @param e
     * @return
     */
    public ObjectId addProjectEntry(CourseProjectEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSEPROJECT, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delProjectEntry(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("flg", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSEPROJECT, query, updateValue);
    }

    /**
     * 某个学校教师课程项目
     *
     * @param schoolId
     * @param fields
     * @return
     */
    public List<CourseProjectEntry> getCourseProjectListBySchoolId(ObjectId schoolId, DBObject fields) {
        BasicDBObject query = new BasicDBObject("si", schoolId).append("flg", 0);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSEPROJECT, query, fields);
        List<CourseProjectEntry> courseProjectEntries = new ArrayList<CourseProjectEntry>();
        for (DBObject dbObject : list) {
            CourseProjectEntry courseProjectEntry = new CourseProjectEntry((BasicDBObject) dbObject);
            courseProjectEntries.add(courseProjectEntry);
        }
        return courseProjectEntries;
    }

    /**
     * 某个课程项目
     *
     * @param id
     * @param fields
     * @return
     */
    public CourseProjectEntry getCourseProjectListById(ObjectId id, DBObject fields) {
        BasicDBObject query = new BasicDBObject("_id", id).append("flg", 0);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSEPROJECT, query, fields);

        return new CourseProjectEntry((BasicDBObject) dbObject);
    }
}
