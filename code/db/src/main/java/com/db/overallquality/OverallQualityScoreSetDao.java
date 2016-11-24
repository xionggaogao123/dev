package com.db.overallquality;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.overallquality.OverallQualityScoreSetEntry;
import com.pojo.utils.DeleteState;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * Created by guojing on 2016/8/4.
 */
public class OverallQualityScoreSetDao extends BaseDao {
    /**
     * 添加项目分值
     * @param e
     * @return
     */
    public ObjectId addOverallQualityScoreSetEntry(OverallQualityScoreSetEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_OVERALL_QUALITY_SCORE_SET, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 查询项目分值
     * @param schoolId
     * @return
     */
    public List<OverallQualityScoreSetEntry> getQualityScoreEntryList(ObjectId schoolId) {
        BasicDBObject query =new BasicDBObject("si", schoolId);
        query.append("st", DeleteState.NORMAL.getState());
        BasicDBObject sort =new BasicDBObject(Constant.ID,Constant.DESC);
        List<DBObject> dbos =find(MongoFacroty.getAppDB(), Constant.COLLECTION_OVERALL_QUALITY_SCORE_SET, query, Constant.FIELDS, sort);
        List<OverallQualityScoreSetEntry> list=new ArrayList<OverallQualityScoreSetEntry>();
        if(null!=dbos && !dbos.isEmpty()) {
            for (DBObject dbo : dbos) {
                list.add(new OverallQualityScoreSetEntry((BasicDBObject) dbo));
            }
        }
        return list;
    }

    /**
     * 根据id集合查询多个用户map
     * @param ids
     * @return
     */
    public Map<ObjectId, OverallQualityScoreSetEntry> getOverallQualityScoreMap(Collection<ObjectId> ids)
    {
        Map<ObjectId, OverallQualityScoreSetEntry> retMap =new HashMap<ObjectId, OverallQualityScoreSetEntry>();
        BasicDBObject query =new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_IN,ids)).append("st", DeleteState.NORMAL.getState());
        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_OVERALL_QUALITY_SCORE_SET, query, Constant.FIELDS);
        if(null!=list && !list.isEmpty())
        {
            OverallQualityScoreSetEntry e;
            for(DBObject dbo:list)
            {
                e=new OverallQualityScoreSetEntry((BasicDBObject)dbo);
                retMap.put(e.getID(), e);
            }
        }
        return retMap;
    }

    public OverallQualityScoreSetEntry getOverallQualityScoreById(ObjectId id) {
        BasicDBObject query =new BasicDBObject(Constant.ID, id);
        DBObject dbo=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_OVERALL_QUALITY_SCORE_SET, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new OverallQualityScoreSetEntry((BasicDBObject) dbo);
        }
        return null;
    }
}
