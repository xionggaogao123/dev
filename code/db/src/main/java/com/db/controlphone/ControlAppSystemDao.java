package com.db.controlphone;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.controlphone.ControlAppSystemEntry;
import com.sys.constants.Constant;

/**
 * Created by James on 2017/12/1.
 */
public class ControlAppSystemDao extends BaseDao {
    public ControlAppSystemEntry getEntry() {
        BasicDBObject query =new BasicDBObject();
        query.append("isr",Constant.ZERO);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_APP_SYSTEM, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new ControlAppSystemEntry((BasicDBObject)dbo);
        }
        return null;
    }

    //添加
    public String addEntry(ControlAppSystemEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_APP_SYSTEM, entry.getBaseEntry());
        return entry.getID().toString() ;
    }
    public void updEntry(ControlAppSystemEntry e) {
        BasicDBObject query=new BasicDBObject(Constant.ID,e.getID());
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,e.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_APP_SYSTEM, query,updateValue);
    }
}
