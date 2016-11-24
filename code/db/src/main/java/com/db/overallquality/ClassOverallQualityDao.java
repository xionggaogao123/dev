package com.db.overallquality;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.overallquality.ClassOverallQualityEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojing on 2016/8/23.
 */
public class ClassOverallQualityDao extends BaseDao {
    /**
     * 添加
     * @param e
     * @return
     */
    public ObjectId addClassOverallQualityEntry(ClassOverallQualityEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_OVERALL_QUALITY, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 查询
     * @param gradeId
     * @param classId
     * @param dateStart
     * @param dateEnd
     * @return
     */
    public List<ClassOverallQualityEntry> searchClassOverallQualityEntryList(ObjectId gradeId, ObjectId classId, long dateStart, long dateEnd) {
        BasicDBObject query =new BasicDBObject("gi", gradeId);
        query.append("ci", classId);
        BasicDBList dblist =new BasicDBList();
        if(dateStart>0){
            dblist.add(new BasicDBObject("cd", new BasicDBObject(Constant.MONGO_GTE, dateStart)));
        }
        if(dateEnd>0) {
            dblist.add(new BasicDBObject("cd", new BasicDBObject(Constant.MONGO_LTE, dateEnd)));
        }
        if(dblist.size()>0){
            query.append(Constant.MONGO_AND,dblist);
        }

        BasicDBObject sort =new BasicDBObject(Constant.ID,Constant.DESC);
        List<DBObject> dbos =find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_OVERALL_QUALITY, query, Constant.FIELDS, sort);
        List<ClassOverallQualityEntry> list=new ArrayList<ClassOverallQualityEntry>();
        if(null!=dbos && !dbos.isEmpty()) {
            for (DBObject dbo : dbos) {
                list.add(new ClassOverallQualityEntry((BasicDBObject) dbo));
            }
        }
        return list;
    }

    /**
     * 查询
     * @param id
     * @return
     */
    public ClassOverallQualityEntry searchClassOverallQualityEntry(ObjectId id) {
        BasicDBObject query =new BasicDBObject(Constant.ID, id);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_OVERALL_QUALITY, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new ClassOverallQualityEntry((BasicDBObject)dbo);
        }
        return null;
    }

    /**
     * 修改
     * @param entry
     */
    public void updateClassOverallQualityEntry(ClassOverallQualityEntry entry) {
        BasicDBObject query =new BasicDBObject(Constant.ID, entry.getID());
        BasicDBObject updateValue =new BasicDBObject(Constant.MONGO_SET, entry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_OVERALL_QUALITY, query, updateValue);
    }
}
