package com.db.teacherevaluation;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.teacherevaluation.EvaluationItemEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by fl on 2016/4/22.
 */
public class EvaluationItemDao extends BaseDao {

    private static final String COLLECTION_NAME = Constant.COLLECTION_TE_ITEM;

    /**
     * 添加一条记录
     * @param evaluationItemEntry
     * @return
     */
    public ObjectId addEvaluationItem(EvaluationItemEntry evaluationItemEntry){
        save(MongoFacroty.getAppDB(), COLLECTION_NAME, evaluationItemEntry.getBaseEntry());
        return evaluationItemEntry.getID();
    }

    /**
     * 获取评价详情
     * @param itemId
     * @param fields
     * @return
     */
    public EvaluationItemEntry getEvaluationItem(ObjectId itemId, DBObject fields){
        DBObject query = new BasicDBObject(Constant.ID, itemId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), COLLECTION_NAME, query, fields);
        if(dbObject != null){
            return new EvaluationItemEntry((BasicDBObject)dbObject);
        }
        return null;
    }

    public EvaluationItemEntry getEvaluationItem(ObjectId teacherId, ObjectId evaluationId, DBObject fields){
        DBObject query = new BasicDBObject("tid", teacherId).append("evid", evaluationId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), COLLECTION_NAME, query, fields);
        if(dbObject != null){
            return new EvaluationItemEntry((BasicDBObject)dbObject);
        }
        return null;
    }

    public List<EvaluationItemEntry> getEvaluationItems(Collection<ObjectId> teacherIds, ObjectId evaluationId, DBObject fields){
        List<EvaluationItemEntry> evaluationItemEntries = new ArrayList<EvaluationItemEntry>();
        DBObject query = new BasicDBObject("tid", new BasicDBObject(Constant.MONGO_IN, teacherIds)).append("evid", evaluationId);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), COLLECTION_NAME, query, fields, new BasicDBObject("fs", -1));
        if(dbObject != null){
            for(DBObject o : dbObject){
                evaluationItemEntries.add(new EvaluationItemEntry((BasicDBObject)o));
            }
        }
        return evaluationItemEntries;
    }

    public List<EvaluationItemEntry> getEvaluationItems(ObjectId evaluationId, DBObject fields){
        List<EvaluationItemEntry> evaluationItemEntries = new ArrayList<EvaluationItemEntry>();
        DBObject query = new BasicDBObject("evid", evaluationId);
        DBObject orderBy = new BasicDBObject("rk", 1);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), COLLECTION_NAME, query, fields, orderBy);
        if(dbObject != null){
            for(DBObject o : dbObject){
                evaluationItemEntries.add(new EvaluationItemEntry((BasicDBObject)o));
            }
        }
        return evaluationItemEntries;
    }

    public List<EvaluationItemEntry> getEvaluationItems(ObjectId schoolId, ObjectId teacherId, DBObject fields){
        List<EvaluationItemEntry> evaluationItemEntries = new ArrayList<EvaluationItemEntry>();
        DBObject query = new BasicDBObject("si", schoolId).append("tid", teacherId);
        DBObject orderBy = new BasicDBObject("y", -1);
        List<DBObject> dbObject = find(MongoFacroty.getAppDB(), COLLECTION_NAME, query, fields, orderBy);
        if(dbObject != null){
            for(DBObject o : dbObject){
                evaluationItemEntries.add(new EvaluationItemEntry((BasicDBObject)o));
            }
        }
        return evaluationItemEntries;
    }

    /**
     * 更新指定数据
     * @param teacherId
     * @param evaluationId
     * @param value
     */
    public void updateEvaluationItem(ObjectId teacherId, ObjectId evaluationId, DBObject value){
        DBObject query = new BasicDBObject("tid", teacherId).append("evid", evaluationId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, value);
        update(MongoFacroty.getAppDB(), COLLECTION_NAME, query, updateValue);
    }

    /**
     * 清空量化成绩、互评成绩、考核打分
     * @param evaluationId
     */
    public void restEvaluationItemScore(ObjectId evaluationId){
        DBObject query = new BasicDBObject().append("evid", evaluationId);
        List<EvaluationItemEntry.ElementScore> elementScores = new ArrayList<EvaluationItemEntry.ElementScore>();
        BasicDBList dbList = MongoUtils.convert(MongoUtils.fetchDBObjectList(elementScores));
        DBObject updateValue = new BasicDBObject("lhs", dbList)
                .append("hps", dbList)
                .append("lds", dbList)
                .append("gps", dbList)
                .append("flh", 0)
                .append("fhp", 0)
                .append("fld", 0)
                .append("fgp", 0)
                .append("fs", 0)
                .append("fss", 0)
                .append("rk", 0);
        update(MongoFacroty.getAppDB(), COLLECTION_NAME, query, new BasicDBObject(Constant.MONGO_SET, updateValue));
    }

    public void removeItem(ObjectId evaluationId, ObjectId teacherId){
        DBObject query = new BasicDBObject("evid", evaluationId).append("tid", teacherId);
        remove(MongoFacroty.getAppDB(), COLLECTION_NAME, query);
    }


    @Deprecated
    public void updateEvaluationId(ObjectId schoolId, String year, ObjectId evaluationId){
        DBObject query = new BasicDBObject("si", schoolId).append("y", year);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("evid", evaluationId));
        update(MongoFacroty.getAppDB(), COLLECTION_NAME, query, updateValue);
    }




}
