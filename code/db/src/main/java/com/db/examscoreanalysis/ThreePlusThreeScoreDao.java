package com.db.examscoreanalysis;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.FieldValuePair;
import com.pojo.examscoreanalysis.ThreePlusThreeScoreEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by fl on 2016/8/15.
 */
public class ThreePlusThreeScoreDao extends BaseDao {

    /**
     * 新增或更新
     * @param threePlusThreeScoreEntry
     * @return
     */
    public ObjectId save(ThreePlusThreeScoreEntry threePlusThreeScoreEntry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORES, threePlusThreeScoreEntry.getBaseEntry());
        return threePlusThreeScoreEntry.getID();
    }

    /**
     * 批量插入
     * @param scoreEntries
     */
    public void insert(Collection<ThreePlusThreeScoreEntry> scoreEntries){
        List<DBObject> list = new ArrayList<DBObject>();
        for(ThreePlusThreeScoreEntry threePlusThreeScoreEntry : scoreEntries){
            list.add(threePlusThreeScoreEntry.getBaseEntry());
        }
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORES, list);
    }

    /**
     * 更新
     * @param id
     * @param pairs
     */
    public void update(ObjectId id, FieldValuePair... pairs){
        DBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject values = new BasicDBObject();
        for(FieldValuePair pair : pairs){
            values.append(pair.getField(), pair.getValue());
        }
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, values);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORES, query, updateValue);

    }

    /**
     * 更新
     * @param examId
     * @param studentId
     * @param subjectId
     * @param pairs
     */
    public void update(ObjectId examId, ObjectId studentId, ObjectId subjectId, FieldValuePair... pairs){
        DBObject query = new BasicDBObject("exid", examId).append("stuid", studentId).append("subid", subjectId);
        BasicDBObject values = new BasicDBObject();
        for(FieldValuePair pair : pairs){
            values.append(pair.getField(), pair.getValue());
        }
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, values);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORES, query, updateValue);

    }

    /**
     * 成绩列表
     * @param examId
     * @param subjectId
     * @param fields
     * @return
     */
    public List<ThreePlusThreeScoreEntry> getScoreEntries(ObjectId examId, ObjectId subjectId, DBObject fields){
        BasicDBObject query = new BasicDBObject("exid", examId);
        if(null != subjectId){
            query.append("subid", subjectId);
        }
        return getList(query, fields);
    }

    public List<ThreePlusThreeScoreEntry> getScoreEntries(ObjectId examId, ObjectId subjectId, int skip, int limit, DBObject fields){
        List<ThreePlusThreeScoreEntry> scoreEntries = new ArrayList<ThreePlusThreeScoreEntry>();
        BasicDBObject query = new BasicDBObject("exid", examId).append("subid", subjectId);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORES, query, fields, new BasicDBObject("grk", Constant.ASC), skip, limit);
        if(list != null && !list.isEmpty()){
            for(DBObject object : list){
                scoreEntries.add(new ThreePlusThreeScoreEntry((BasicDBObject)object));
            }
        }
        return scoreEntries;
    }

    public int countScoreEntries(ObjectId examId, ObjectId subjectId){
        BasicDBObject query = new BasicDBObject("exid", examId).append("subid", subjectId);
        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORES, query);
        return count;
    }

    /**
     * 查找行政班的成绩列表
     * @param examId
     * @param adminClassId
     * @param subjectId
     * @param fields
     * @return
     */
    public List<ThreePlusThreeScoreEntry> getScoreEntriesForAdminClass(ObjectId examId, ObjectId adminClassId, ObjectId subjectId, DBObject fields){
        BasicDBObject query = new BasicDBObject("exid", examId).append("acid", adminClassId);
        if(null != subjectId){
            query.append("subid", subjectId);
        }
        return getList(query, fields);
    }

    /**
     * 查找教学班的成绩列表
     * @param examId
     * @param teachingClassId
     * @param subjectId
     * @param fields
     * @return
     */
    public List<ThreePlusThreeScoreEntry> getScoreEntriesForTeachingClass(ObjectId examId, ObjectId teachingClassId, ObjectId subjectId, DBObject fields){
        BasicDBObject query = new BasicDBObject("exid", examId).append("tcid", teachingClassId);
        if(null != subjectId){
            query.append("subid", subjectId);
        }

        return getList(query, fields);
    }


    private List<ThreePlusThreeScoreEntry> getList(DBObject query, DBObject fields){
        List<ThreePlusThreeScoreEntry> scoreEntries = new ArrayList<ThreePlusThreeScoreEntry>();
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORES, query, fields);
        if(list != null && !list.isEmpty()){
            for(DBObject object : list){
                scoreEntries.add(new ThreePlusThreeScoreEntry((BasicDBObject)object));
            }
        }
        return scoreEntries;
    }

    /**
     * 删除成绩
     * @param examId 考试id
     * @param savingSubjectIds 需要保留成绩的科目id,除此之外的全部删除
     */
    public void removeScoreEntries(ObjectId examId, Collection<ObjectId> savingSubjectIds){
        DBObject query = new BasicDBObject("exid", examId).append("subid", new BasicDBObject(Constant.MONGO_NOTIN, savingSubjectIds));
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORES, query);
    }


}
