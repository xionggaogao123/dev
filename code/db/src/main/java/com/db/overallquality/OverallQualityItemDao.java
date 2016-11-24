package com.db.overallquality;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.overallquality.OverallQualityItemEntry;
import com.pojo.utils.DeleteState;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guojing on 2016/8/4.
 */
public class OverallQualityItemDao extends BaseDao {
    /**
     * 添加项目
     * @param e
     * @return
     */
    public ObjectId addOverallQualityItemEntry(OverallQualityItemEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_OVERALL_QUALITY_ITEM, e.getBaseEntry());
        return e.getID();
    }

    public int getQualityItemCount(String id, ObjectId schoolId, String itemName) {
        BasicDBObject query =new BasicDBObject("si", schoolId);
        query.append("st", DeleteState.NORMAL.getState());
        if(id!=null&&!"".equals(id)){
            BasicDBList dblist =new BasicDBList();
            dblist.add(new BasicDBObject(Constant.ID, new ObjectId(id)));
            dblist.add(new BasicDBObject("nm", itemName));
            query.append(Constant.MONGO_OR,dblist);
        }else{
            query.append("nm", itemName);
        }
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_OVERALL_QUALITY_ITEM, query);
    }

    public List<OverallQualityItemEntry> searchQualityItemEntryList(ObjectId schoolId) {
        BasicDBObject query =new BasicDBObject("si", schoolId);
        query.append("st", DeleteState.NORMAL.getState());
        BasicDBObject sort =new BasicDBObject(Constant.ID,Constant.DESC);
        List<DBObject> dbos =find(MongoFacroty.getAppDB(), Constant.COLLECTION_OVERALL_QUALITY_ITEM, query, Constant.FIELDS, sort);
        List<OverallQualityItemEntry> list=new ArrayList<OverallQualityItemEntry>();
        if(null!=dbos && !dbos.isEmpty()) {
            for (DBObject dbo : dbos) {
                list.add(new OverallQualityItemEntry((BasicDBObject) dbo));
            }
        }
        return list;
    }

    public void delQualityItem(ObjectId id) {
        BasicDBObject query =new BasicDBObject(Constant.ID, id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_OVERALL_QUALITY_ITEM, query);
    }

    public OverallQualityItemEntry getQualityItem(ObjectId id) {
        BasicDBObject query =new BasicDBObject(Constant.ID, id);
        DBObject dbo=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_OVERALL_QUALITY_ITEM, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new OverallQualityItemEntry((BasicDBObject)dbo);
        }
        return null;
    }

    public void updOverallQualityItemEntry(OverallQualityItemEntry e) {
        BasicDBObject query =new BasicDBObject(Constant.ID, e.getID());
        BasicDBObject updateValue =new BasicDBObject(Constant.MONGO_SET, e.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_OVERALL_QUALITY_ITEM, query, updateValue);
    }

    public Map<ObjectId, OverallQualityItemEntry> searchQualityItemEntryMap(ObjectId schoolId) {
        BasicDBObject query =new BasicDBObject("si", schoolId);
        query.append("st", DeleteState.NORMAL.getState());
        BasicDBObject sort =new BasicDBObject(Constant.ID,Constant.DESC);
        List<DBObject> dbos =find(MongoFacroty.getAppDB(), Constant.COLLECTION_OVERALL_QUALITY_ITEM, query, Constant.FIELDS, sort);
        Map<ObjectId, OverallQualityItemEntry> map=new HashMap<ObjectId, OverallQualityItemEntry>();
        if(null!=dbos && !dbos.isEmpty()) {
            OverallQualityItemEntry entry;
            for (DBObject dbo : dbos) {
                entry = new OverallQualityItemEntry((BasicDBObject) dbo);
                map.put(entry.getID(), entry);
            }
        }
        return map;
    }
}
