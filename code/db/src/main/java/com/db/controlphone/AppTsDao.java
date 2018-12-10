package com.db.controlphone;

import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.controlphone.AppTsEntry;
import com.pojo.controlphone.ControlAppEntry;
import com.sys.constants.Constant;

public class AppTsDao extends BaseDao{

  //添加
    public String addEntry(AppTsEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_APP_TS, entry.getBaseEntry());
        return entry.getID().toString() ;
    }
    
    
    public int tsCount(List<ObjectId> communityIds,Long startTime, Long endTime) {
        BasicDBObject query = new BasicDBObject();
        BasicDBObject query1 = new BasicDBObject();
        BasicDBObject query2 = new BasicDBObject();
        query1.append("cmid",new BasicDBObject(Constant.MONGO_IN, communityIds));
        BasicDBList values = new BasicDBList();
        if (startTime != null && startTime != 0l) {
            query1.append("ti",  new BasicDBObject(Constant.MONGO_GTE, startTime));
        }
        if (endTime != null && endTime != 0l) {
            query2.append("ti",  new BasicDBObject(Constant.MONGO_LT, endTime));
        }
        values.add(query1);
        values.add(query2);
        query.put(Constant.MONGO_AND, values);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_APP_TS, query);
    }
}
