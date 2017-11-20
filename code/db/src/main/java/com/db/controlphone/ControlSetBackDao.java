package com.db.controlphone;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.controlphone.ControlSetBackEntry;
import com.sys.constants.Constant;

/**
 * Created by James on 2017/11/20.
 */
public class ControlSetBackDao extends BaseDao {

    //添加
    public String addEntry(ControlSetBackEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_BACK_TIME, entry.getBaseEntry());
        return entry.getID().toString() ;
    }

    //单查询
    public ControlSetBackEntry getEntry() {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_BACK_TIME, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new ControlSetBackEntry((BasicDBObject)dbo);
        }
        return null;
    }
    //修改
    public void updEntry(ControlSetBackEntry e) {
        BasicDBObject query=new BasicDBObject(Constant.ID,e.getID());
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,e.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_BACK_TIME, query,updateValue);
    }
}
