package com.db.guard;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.guard.StudentEnterEntry;
import com.sys.constants.Constant;

/**
 * @author chengwei@ycode.cn
 * @version 2015年12月7日 下午8:38:35
 *          类说明
 */
public class StudentEnterDao extends BaseDao {
    /**
     * 增加进校记录(cw)
     *
     * @param e
     * @return
     */
    public ObjectId addStudentEnterEntry(StudentEnterEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_ENTER, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 删除一条进校记录(cw)
     *
     * @param id
     */
    public void deleteStudentEnterEntry(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ir", Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_ENTER, query, updateValue);
    }

    /**
     * 根据条件查询进校记录(cw)
     *
     * @param grade
     * @param classroom
     * @return
     */
    public List<StudentEnterEntry> queryEnterStudents(String grade, String classroom, int skip, int size) {
        BasicDBObject query = new BasicDBObject("ir", Constant.ZERO);

        if (!("全部年级".equals(grade))) {
            query.append("gd", grade);
            if (!("全部班级".equals(classroom))) {
                query.append("cr", classroom);
            }
        }

        BasicDBObject orderBy = new BasicDBObject("et", Constant.DESC);
        List<DBObject> objects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_ENTER, query, Constant.FIELDS, orderBy, skip, size);
        List<StudentEnterEntry> resultList = new ArrayList<StudentEnterEntry>();
        for (DBObject dbObject : objects) {
            StudentEnterEntry studentEntry = new StudentEnterEntry((BasicDBObject) dbObject);
            resultList.add(studentEntry);
        }
        return resultList;
    }

    /**
     * 根据id查询学生进校记录(cw)
     *
     * @param id
     * @return
     */
    public StudentEnterEntry getEnterStudent(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_ENTER, query, Constant.FIELDS);
        if (dbObject != null) {
            return new StudentEnterEntry((BasicDBObject) dbObject);
        }
        return null;
    }

    /**
     * 获取进校人数
     */
    public int countEnter(String grade, String classroom) {
        BasicDBObject query = new BasicDBObject("ir", Constant.ZERO);

        if (!("全部".equals(grade))) {
            query.append("gd", grade);
            if (!("全部".equals(classroom))) {
                query.append("cr", classroom);
            }
        }
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_ENTER, query);
    }

}
