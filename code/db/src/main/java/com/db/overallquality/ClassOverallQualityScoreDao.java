package com.db.overallquality;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.overallquality.ClassOverallQualityScoreEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojing on 2016/8/24.
 */
public class ClassOverallQualityScoreDao extends BaseDao {

    /**
     * 添加
     * @param e
     * @return
     */
    public ObjectId addClassOverallQualityScoreEntry(ClassOverallQualityScoreEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_OVERALL_QUALITY_SCORE, e.getBaseEntry());
        return e.getID();
    }


    /**
     * 查询
     * @param classId
     * @return
     */
    public ClassOverallQualityScoreEntry searchClassOverallQualityScoreEntryByClassId(ObjectId classId) {
        BasicDBObject query =new BasicDBObject("ci", classId);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_OVERALL_QUALITY_SCORE, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new ClassOverallQualityScoreEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * 查询
     * @param gradeId
     * @return
     */
    public List<ClassOverallQualityScoreEntry> searchClassOverallQualityScoreEntryByGradeId(ObjectId gradeId, int skip, int limit) {
        BasicDBObject query =new BasicDBObject("gi", gradeId);
        List<DBObject> dbos =new ArrayList<DBObject>();
        if(skip>=0 && limit>0)
        {
            BasicDBObject sort =new BasicDBObject("ts", Constant.DESC).append(Constant.ID, Constant.ASC);
            dbos = find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_OVERALL_QUALITY_SCORE, query, Constant.FIELDS, sort, skip, limit);
        }
        else
        {
            BasicDBObject sort =new BasicDBObject(Constant.ID, Constant.DESC);
            dbos = find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_OVERALL_QUALITY_SCORE, query, Constant.FIELDS, sort);
        }

        List<ClassOverallQualityScoreEntry> list=new ArrayList<ClassOverallQualityScoreEntry>();
        if(null!=dbos && !dbos.isEmpty()) {
            for (DBObject dbo : dbos) {
                list.add(new ClassOverallQualityScoreEntry((BasicDBObject) dbo));
            }
        }
        return list;
    }

    public void updateClassOverallQualityScoreEntry(ClassOverallQualityScoreEntry e) {
        BasicDBObject query =new BasicDBObject(Constant.ID, e.getID());
        BasicDBObject updateValue =new BasicDBObject(Constant.MONGO_SET, e.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_OVERALL_QUALITY_SCORE, query, updateValue);
    }
}
