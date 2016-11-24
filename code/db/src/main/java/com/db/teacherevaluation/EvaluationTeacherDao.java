package com.db.teacherevaluation;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.teacherevaluation.EvaluationTeacherEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fl on 2016/9/12.
 */
public class EvaluationTeacherDao extends BaseDao {

    private static final String COLLECTION_NAME = Constant.COLLECTION_TE_TEACHER;

    /**
     * 保存
     * @param entry
     * @return
     */
    public ObjectId saveEvaluationTeacher(EvaluationTeacherEntry entry){
        save(MongoFacroty.getAppDB(), COLLECTION_NAME, entry.getBaseEntry());
        return entry.getID();
    }

    /**
     * 查询老师信息
     * @param teacherId
     * @param fields
     * @return
     */
    public EvaluationTeacherEntry getEvaluationTeacherEntryByTeacherId(ObjectId teacherId, DBObject fields){
        DBObject query = new BasicDBObject("tid", teacherId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), COLLECTION_NAME, query, fields);
        if(dbObject != null){
            return new EvaluationTeacherEntry((BasicDBObject)dbObject);
        }
        return null;
    }

    /**
     * 查找老师信息
     * @param teacherIds
     * @param fields
     * @return
     */
    public List<EvaluationTeacherEntry> getEvaluationTeacherEntryByTeacherIds(List<ObjectId> teacherIds, DBObject fields){
        List<EvaluationTeacherEntry> teacherEntries = new ArrayList<EvaluationTeacherEntry>();
        DBObject query = new BasicDBObject("tid", new BasicDBObject(Constant.MONGO_IN, teacherIds));
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), COLLECTION_NAME, query, fields);
        if(dbObjects != null && dbObjects.size() > 0){
            for(DBObject dbObject : dbObjects){
                teacherEntries.add(new EvaluationTeacherEntry((BasicDBObject)dbObject));
            }
        }
        return teacherEntries;
    }

    /**
     * 更新个人陈述
     * @param teacherId
     * @param statement
     */
    public void updateStatement(ObjectId teacherId, String statement){
        DBObject query = new BasicDBObject("tid", teacherId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("stat", statement));
        update(MongoFacroty.getAppDB(), COLLECTION_NAME, query, updateValue);
    }

    /**
     * 更新实证资料
     * @param teacherId
     * @param evidence
     */
    public void updateEvidence(ObjectId teacherId, String evidence){
        DBObject query = new BasicDBObject("tid", teacherId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("evi", evidence));
        update(MongoFacroty.getAppDB(), COLLECTION_NAME, query, updateValue);
    }

}
