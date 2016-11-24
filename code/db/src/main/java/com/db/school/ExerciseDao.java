package com.db.school;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.FieldValuePair;
import com.pojo.app.IdValuePair;
import com.pojo.exercise.ExerciseEntry;
import com.sys.constants.Constant;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 考试和习题操作
 *
 * @author fourer
 */
public class ExerciseDao extends BaseDao {

    /**
     * 添加
     *
     * @param e
     * @return
     */
    public ObjectId add(ExerciseEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_EXERCISE_NAME, e.getBaseEntry());
        return e.getID();
    }


    /**
     * 添加一个优秀试卷
     *
     * @param id
     * @param uid
     */
    public void addGoodUser(ObjectId id, ObjectId uid) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_ADDTOSET, new BasicDBObject("gu", uid));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXERCISE_NAME, query, updateValue);
    }


    /**
     * 删除一个优秀试卷
     *
     * @param id
     * @param uid
     */
    public void removeGoodUser(ObjectId id, ObjectId uid) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("gu", uid));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXERCISE_NAME, query, updateValue);
    }


    /**
     * 更新字段值
     *
     * @param id
     * @param pairs
     */
    public void update(ObjectId id, FieldValuePair... pairs) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject valueDBO = new BasicDBObject();
        for (FieldValuePair pair : pairs) {
            valueDBO.append(pair.getField(), pair.getValue());
        }
        valueDBO.append("lut", System.currentTimeMillis());
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, valueDBO);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXERCISE_NAME, query, updateValue);
    }


    /**
     * 删除文档
     *
     * @param id
     * @param teacherId
     */
    public void delete(ObjectId id, ObjectId teacherId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id).append("ti", teacherId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_EXERCISE_NAME, query);
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public ExerciseEntry getExerciseEntry(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_EXERCISE_NAME, query, Constant.FIELDS);
        if (null != dbo) {
            return new ExerciseEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * 根据老师ID查询文档
     *
     * @param type
     * @param teacherId
     * @param fields
     * @return
     */
    public List<ExerciseEntry> getExerciseEntryList(int type, ObjectId teacherId, DBObject fields, int skip, int limit) {
        List<ExerciseEntry> retList = new ArrayList<ExerciseEntry>();
        BasicDBObject query = new BasicDBObject("ti", teacherId).append("ty", type);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXERCISE_NAME, query, fields, Constant.MONGO_SORTBY_DESC, skip, limit);
        if (null != list && !list.isEmpty()) {
            ExerciseEntry e;
            for (DBObject dbo : list) {
                e = new ExerciseEntry((BasicDBObject) dbo);
                retList.add(e);
            }
        }
        return retList;
    }


    /**
     * 取得个数
     *
     * @param type
     * @param teacherId
     * @return
     */
    public int count(int type, ObjectId teacherId) {
        BasicDBObject query = new BasicDBObject("ti", teacherId).append("ty", type);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_EXERCISE_NAME, query);
    }


    /**
     * 学生提交考试
     *
     * @param id
     * @param studentId
     */
    public void studentSubmit(ObjectId id, ObjectId studentId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        IdValuePair pair = new IdValuePair(studentId, System.currentTimeMillis());
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_ADDTOSET, new BasicDBObject("sts", pair.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXERCISE_NAME, query, updateValue);
    }

    /**
     * 根据班级ID查询
     *
     * @param type
     * @param classes
     * @param fields
     * @return
     */
    public List<ExerciseEntry> getExerciseEntryList(ObjectId classId, DBObject fields, int skip, int limit) {
        List<ExerciseEntry> retList = new ArrayList<ExerciseEntry>();
        BasicDBObject query = new BasicDBObject("cis", classId).append("ty", new BasicDBObject(Constant.MONGO_NE, 0));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXERCISE_NAME, query, fields, Constant.MONGO_SORTBY_DESC, skip, limit);
        if (null != list && !list.isEmpty()) {
            ExerciseEntry e;
            for (DBObject dbo : list) {
                e = new ExerciseEntry((BasicDBObject) dbo);
                retList.add(e);
            }
        }
        return retList;
    }

    /**
     * 添加一个推送班级
     *
     * @param id
     * @param classiD
     */
    public void addClassId(ObjectId id, ObjectId classiD) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject updateValue = new BasicDBObject().append(Constant.MONGO_ADDTOSET, new BasicDBObject("cis", classiD))
                .append(Constant.MONGO_SET, new BasicDBObject("lut", System.currentTimeMillis()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXERCISE_NAME, query, updateValue);
    }


    /**
     * 通过一个班级，查询考试总数
     *
     * @param classid
     * @return
     */
    public int count(ObjectId classid) {
        BasicDBObject query = new BasicDBObject("cis", classid);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_EXERCISE_NAME, query);
    }


    public int selPaperCount(List<ObjectId> usIds, int type, long dsl, long del) {
        BasicDBObject query = new BasicDBObject("ti", new BasicDBObject(Constant.MONGO_IN, usIds));

        BasicDBList dblist = new BasicDBList();
        if (dsl > 0) {
            dblist.add(new BasicDBObject("lut", new BasicDBObject(Constant.MONGO_GTE, dsl)));
        }
        if (del > 0) {
            dblist.add(new BasicDBObject("lut", new BasicDBObject(Constant.MONGO_LTE, del)));
        }
        if (dblist.size() > 0) {
            query.append(Constant.MONGO_AND, dblist);
        }

        query.append("ty", type);

        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_EXERCISE_NAME, query);
    }

    public List<ExerciseEntry> getPapersUploadByParamList(List<ObjectId> usIds, int type, long dsl, long del, int skip, int limit, BasicDBObject fields, String orderBy) {
        List<ExerciseEntry> retList = new ArrayList<ExerciseEntry>();
        BasicDBObject query = new BasicDBObject("ti", new BasicDBObject(Constant.MONGO_IN, usIds));
        BasicDBList dblist = new BasicDBList();
        if (dsl > 0) {
            dblist.add(new BasicDBObject("lut", new BasicDBObject(Constant.MONGO_GTE, dsl)));
        }
        if (del > 0) {
            dblist.add(new BasicDBObject("lut", new BasicDBObject(Constant.MONGO_LTE, del)));
        }
        if (dblist.size() > 0) {
            query.append(Constant.MONGO_AND, dblist);
        }

        query.append("ty", type);

        BasicDBObject sort = null;
        if (!"".equals(orderBy)) {
            sort = new BasicDBObject(orderBy, Constant.DESC);
        } else {
            sort = new BasicDBObject("lut", Constant.DESC);
        }
        List<DBObject> list = new ArrayList<DBObject>();
        if (skip >= 0 && limit > 0) {
            list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXERCISE_NAME, query, fields, sort, skip, limit);
        } else {
            list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXERCISE_NAME, query, fields, sort);
        }

        for (DBObject dbo : list) {
            retList.add(new ExerciseEntry((BasicDBObject) dbo));
        }
        return retList;
    }

    /**
     * 根据班级id统计
     * add by miaoqiang
     *
     * @param type
     * @param classId
     * @return
     */
    public int countByClassId(int type, ObjectId classId) {
        BasicDBObject query = new BasicDBObject("cis", classId).append("ty", type);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_EXERCISE_NAME, query);
    }

    /**
     * 根据班级id获取本班所有的考试
     *
     * @param type
     * @param classId
     * @param fields
     * @param skip
     * @param limit
     * @return
     */
    public List<ExerciseEntry> getExerciseEntryListByClassId(int type, ObjectId classId, DBObject fields, int skip, int limit) {
        List<ExerciseEntry> retList = new ArrayList<ExerciseEntry>();
        BasicDBObject query = new BasicDBObject("cis", classId).append("ty", type);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXERCISE_NAME, query, fields, Constant.MONGO_SORTBY_DESC, skip, limit);
        if (null != list && !list.isEmpty()) {
            ExerciseEntry e;
            for (DBObject dbo : list) {
                e = new ExerciseEntry((BasicDBObject) dbo);
                retList.add(e);
            }
        }
        return retList;
    }
}
