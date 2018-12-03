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
    
    
    public int tsCount(List<ObjectId> communityIds,long startTime, long endTime) {
        BasicDBObject query = new BasicDBObject();
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject().append("ti",  new BasicDBObject(Constant.MONGO_GTE, startTime)).append("cmid",new BasicDBObject(Constant.MONGO_IN, communityIds)));
        values.add(new BasicDBObject().append("ti",  new BasicDBObject(Constant.MONGO_LT, endTime)));
        query.put(Constant.MONGO_AND, values);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_APP_TS, query);
    }
}
