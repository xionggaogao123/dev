package com.db.guard;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.guard.StudentEnterEntry;
import com.pojo.guard.StudentOutEntry;
import com.sys.constants.Constant;

/**
 * @author 作者 E-mail:chengwei@ycode.cn
 * @version 创建时间：2015年12月7日 下午8:38:35
 *          类说明
 */
public class StudentOutDao extends BaseDao {
    /**
     * 增加出校记录(cw)
     *
     * @param e
     * @return
     */
    public ObjectId addStudentOutEntry(StudentOutEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_OUT, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 删除一条进校记录(cw)
     *
     * @param id
     */
    public void deleteStudentOutEntry(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ir", Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_OUT, query, updateValue);
    }

    /**
     * 根据条件查询出校记录(cw)
     *
     * @param grade
     * @param classroom
     * @return
     */
    public List<StudentOutEntry> queryOutStudents(String grade, String classroom, int skip, int size) {
        BasicDBObject query = new BasicDBObject("ir", Constant.ZERO);

        if (!("全部年级".equals(grade))) {
            query.append("gd", grade);
            if (!("全部班级".equals(classroom))) {
                query.append("cs", classroom);
            }
        }

        BasicDBObject orderBy = new BasicDBObject("ot", Constant.DESC);
        List<DBObject> objects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_OUT, query, Constant.FIELDS, orderBy, skip, size);
        List<StudentOutEntry> resultList = new ArrayList<StudentOutEntry>();
        for (DBObject dbObject : objects) {
            StudentOutEntry studentOutEntry = new StudentOutEntry((BasicDBObject) dbObject);
            resultList.add(studentOutEntry);
        }
        return resultList;
    }

    /**
     * 根据id查询学生出校记录(cw)
     *
     * @param id
     * @return
     */
    public StudentEnterEntry getOutStudent(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_OUT, query, Constant.FIELDS);
        if (dbObject != null) {
            return new StudentEnterEntry((BasicDBObject) dbObject);
        }
        return null;
    }

    /**
     * 获取出校人数
     */
    public int countOut(String grade, String classroom) {
        BasicDBObject query = new BasicDBObject("ir", Constant.ZERO);

        if (!("全部".equals(grade))) {
            query.append("gd", grade);
            if (!("全部".equals(classroom))) {
                query.append("cs", classroom);
            }
        }
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_OUT, query);
    }
}
