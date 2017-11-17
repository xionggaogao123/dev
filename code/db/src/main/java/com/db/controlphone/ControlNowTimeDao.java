package com.db.controlphone;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.controlphone.ControlNowTimeEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2017/11/17.
 */
public class ControlNowTimeDao extends BaseDao {

    //添加
    public String addEntry(ControlNowTimeEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_NOW_TIME, entry.getBaseEntry());
        return entry.getID().toString() ;
    }

    //单查询
    public ControlNowTimeEntry getEntry(int week) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO) .append("wek", week);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_NOW_TIME, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new ControlNowTimeEntry((BasicDBObject)dbo);
        }
        return null;
    }

    //单查询
    public ControlNowTimeEntry getOtherEntry(String dateTime,ObjectId userId) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO).append("uid",userId).append("dtm",dateTime);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_NOW_TIME, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new ControlNowTimeEntry((BasicDBObject)dbo);
        }
        return null;
    }
}
