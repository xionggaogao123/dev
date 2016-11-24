package com.db.overallquality;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.overallquality.OverallQualityGoodsEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * Created by guojing on 2016/8/25.
 */
public class OverallQualityGoodsDao extends BaseDao {
    /**
     * 添加
     * @param e
     * @return
     */
    public ObjectId addOverallQualityGoodsEntry(OverallQualityGoodsEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_OVERALL_QUALITY_GOODS, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 查询
     * @param schoolId
     * @return
     */
    public List<OverallQualityGoodsEntry> searchOverallQualityGoodsEntryList(ObjectId schoolId) {
        BasicDBObject query =new BasicDBObject("si", schoolId);
        query.append("st", Constant.ZERO);
        BasicDBObject sort =new BasicDBObject(Constant.ID,Constant.DESC);
        List<DBObject> dbos =find(MongoFacroty.getAppDB(), Constant.COLLECTION_OVERALL_QUALITY_GOODS, query, Constant.FIELDS, sort);
        List<OverallQualityGoodsEntry> list=new ArrayList<OverallQualityGoodsEntry>();
        if(null!=dbos && !dbos.isEmpty()) {
            for (DBObject dbo : dbos) {
                list.add(new OverallQualityGoodsEntry((BasicDBObject) dbo));
            }
        }
        return list;
    }

    /**
     * 查询
     * @param id
     * @return
     */
    public OverallQualityGoodsEntry searchOverallQualityGoodsEntry(ObjectId id) {
        BasicDBObject query =new BasicDBObject(Constant.ID, id);
        query.append("st", Constant.ZERO);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_OVERALL_QUALITY_GOODS, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new OverallQualityGoodsEntry((BasicDBObject)dbo);
        }
        return null;
    }

    /**
     * 修改
     * @param entry
     */
    public void updateOverallQualityGoodsEntry(OverallQualityGoodsEntry entry) {
        BasicDBObject query =new BasicDBObject(Constant.ID, entry.getID());
        query.append("st", Constant.ZERO);
        BasicDBObject updateValue =new BasicDBObject(Constant.MONGO_SET, entry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_OVERALL_QUALITY_GOODS, query, updateValue);
    }

    /**
     * 删除
     * @param id
     */
    public void deleteOverallQualityGoodsEntry(ObjectId id) {
        BasicDBObject query =new BasicDBObject(Constant.ID, id);
        BasicDBObject updateValue =new BasicDBObject(Constant.MONGO_SET,  new BasicDBObject("st", Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_OVERALL_QUALITY_GOODS, query, updateValue);
    }

    public Map<ObjectId, OverallQualityGoodsEntry> getOverallQualityGoodsEntryMap(Collection<ObjectId> ids, DBObject fields) {
        Map<ObjectId, OverallQualityGoodsEntry> retMap =new HashMap<ObjectId, OverallQualityGoodsEntry>();
        BasicDBObject query =new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_IN,ids));
        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_OVERALL_QUALITY_GOODS, query, fields);
        if(null!=list && !list.isEmpty())
        {
            OverallQualityGoodsEntry e;
            for(DBObject dbo:list)
            {
                e=new OverallQualityGoodsEntry((BasicDBObject)dbo);
                retMap.put(e.getID(), e);
            }
        }
        return retMap;
    }
}

