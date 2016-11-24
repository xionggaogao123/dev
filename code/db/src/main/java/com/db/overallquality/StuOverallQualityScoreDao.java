package com.db.overallquality;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.overallquality.StuOverallQualityScoreEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guojing on 2016/8/24.
 */
public class StuOverallQualityScoreDao extends BaseDao {

    /**
     * 添加
     * @param e
     * @return
     */
    public ObjectId addStuOverallQualityScoreEntry(StuOverallQualityScoreEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_STU_OVERALL_QUALITY_SCORE, e.getBaseEntry());
        return e.getID();
    }


    /**
     * 查询
     * @param userId
     * @return
     */
    public StuOverallQualityScoreEntry searchStuOverallQualityScoreEntryByUserId(ObjectId userId) {
        BasicDBObject query =new BasicDBObject("ui", userId);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_STU_OVERALL_QUALITY_SCORE, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new StuOverallQualityScoreEntry((BasicDBObject) dbo);
        }
        return null;
    }

    public void updateStuOverallQualityScoreEntry(StuOverallQualityScoreEntry e) {
        BasicDBObject query =new BasicDBObject(Constant.ID, e.getID());
        BasicDBObject updateValue =new BasicDBObject(Constant.MONGO_SET, e.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_STU_OVERALL_QUALITY_SCORE, query, updateValue);
    }

    public Map<ObjectId, StuOverallQualityScoreEntry> searchStuOverallQualityScoreEntryMap(Collection<ObjectId> uids) {
        Map<ObjectId, StuOverallQualityScoreEntry> retMap =new HashMap<ObjectId, StuOverallQualityScoreEntry>();
        BasicDBObject query =new BasicDBObject("ui",new BasicDBObject(Constant.MONGO_IN, uids));
        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_STU_OVERALL_QUALITY_SCORE, query, Constant.FIELDS);
        if(null!=list && !list.isEmpty())
        {
            StuOverallQualityScoreEntry e;
            for(DBObject dbo:list)
            {
                e=new StuOverallQualityScoreEntry((BasicDBObject)dbo);
                retMap.put(e.getUserId(), e);
            }
        }
        return retMap;
    }
}
