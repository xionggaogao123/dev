package com.db.itempool;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.exercise.ExerciseItemType;
import com.pojo.itempool.StudentExerciseEntry;
import com.sys.constants.Constant;

/**
 * 学生练习情况
 *
 * @author fourer
 */
public class StudentExerciseDao extends BaseDao {

    /**
     * 增加
     *
     * @param e
     * @return
     */
    public ObjectId addStudentExerciseEntry(StudentExerciseEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_EXERCISE, e.getBaseEntry());
        return e.getID();
    }


    /**
     * 详情
     *
     * @param id
     * @return
     */
    public StudentExerciseEntry getStudentExerciseEntry(ObjectId id, DBObject fields) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_EXERCISE, query, fields);
        if (null != dbo) {
            return new StudentExerciseEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * 删除一个学生的练习
     *
     * @param userId
     * @param e
     */
    public void deleteStudentExercise(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_EXERCISE, query);
    }


    /**
     * 更新某个练习为完成状态
     *
     * @param userId
     * @param testPaperId
     */
    public void updateExerciseStateToFinish(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("st", 1).append("t", System.currentTimeMillis()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_EXERCISE, query, updateValue);
    }


    /**
     * 得到学生的练习情况
     *
     * @return
     */
    public List<StudentExerciseEntry> getStudentExerciseEntrys(ObjectId userId, int state, int skip, int limit) {
        List<StudentExerciseEntry> experienceList = new ArrayList<StudentExerciseEntry>();
        DBObject query = new BasicDBObject("ui", userId);
        if (state != -1) {
            query.put("st", state);
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_EXERCISE, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, skip, limit);
        if (null != list && list.size() > 0) {
            for (DBObject dbo : list) {
                experienceList.add(new StudentExerciseEntry((BasicDBObject) dbo));
            }
        }
        return experienceList;
    }


    /**
     * 得到学生的练习情况
     *
     * @return
     */
    public List<StudentExerciseEntry> getStudentExerciseEntrys(List<ObjectId> userIds, int state, int skip, int limit) {
        List<StudentExerciseEntry> experienceList = new ArrayList<StudentExerciseEntry>();
        DBObject query = new BasicDBObject("ui", new BasicDBObject(Constant.MONGO_IN, userIds));
        if (state != -1) {
            query.put("st", state);
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_EXERCISE, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, skip, limit);
        if (null != list && list.size() > 0) {
            for (DBObject dbo : list) {
                experienceList.add(new StudentExerciseEntry((BasicDBObject) dbo));
            }
        }
        return experienceList;
    }


    /**
     * 更新题目ID
     *
     * @param id
     * @param itemId
     * @param type
     */
    public void updateItem(ObjectId id, ObjectId itemId, ExerciseItemType type) {
        DBObject query = new BasicDBObject(Constant.ID, id).append(type.getField() + ".id", null);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject(type.getField() + ".$.id", itemId));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_EXERCISE, query, updateValue);
    }


    /**
     * 更新答案
     *
     * @param id
     * @param itemId
     * @param type
     * @param answer
     * @param right  正确答案的增长数量
     * @param finish 已经完成的增长数量
     */
    public void updateItemAnswer(ObjectId id, ObjectId itemId, ExerciseItemType type, String answer, int right) {
        DBObject query = new BasicDBObject(Constant.ID, id).append(type.getField() + ".id", itemId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject(type.getField() + ".$.nm", answer).append(type.getField() + ".$.v", right));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_EXERCISE, query, updateValue);
    }


}
