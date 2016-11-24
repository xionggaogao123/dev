package com.db.teacherevaluation;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.teacherevaluation.SettingEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fl on 2016/4/21.
 */
public class SettingDao extends BaseDao {

    private static final String COLLECTION_NAME = Constant.COLLECTION_TE_SETTING;

    public ObjectId addSetting(SettingEntry entry){
        save(MongoFacroty.getAppDB(), COLLECTION_NAME, entry.getBaseEntry());
        return entry.getID();
    }


    public SettingEntry getSettingEntry(ObjectId evaluationId){
        DBObject query = new BasicDBObject("evid", evaluationId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), COLLECTION_NAME, query, Constant.FIELDS);
        if(dbObject != null) {
            return new SettingEntry((BasicDBObject) dbObject);
        }
        return null;
    }

    /**
     * 获取还未到评价打分时间的评价项目
     * @param schoolId
     * @param time
     * @return
     */
    public List<ObjectId> getEvaluationIdsByTime(ObjectId schoolId, long time){
        List<ObjectId> list = new ArrayList<ObjectId>();
        DBObject query = new BasicDBObject("si", schoolId).append("etb", new BasicDBObject(Constant.MONGO_GT, time));
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), COLLECTION_NAME, query, new BasicDBObject("evid", 1));
        if(dbObjects != null && dbObjects.size() > 0){
            for(DBObject dbObject : dbObjects){
                SettingEntry settingEntry = new SettingEntry((BasicDBObject)dbObject);
                list.add(settingEntry.getEvaluationId());
            }
        }
        return list;
    }

    @Deprecated
    public void updateEvaluationId(ObjectId schoolId, String year, ObjectId evaluationId){
        DBObject query = new BasicDBObject("si", schoolId).append("y", year);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("evid", evaluationId));
        update(MongoFacroty.getAppDB(), COLLECTION_NAME, query, updateValue);
    }


}
