package com.db.controlphone;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.controlphone.ControlAppUserEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2017/11/17.
 */
public class ControlAppUserDao extends BaseDao {
    //添加
    public String addEntry(ControlAppUserEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_PARENT_APP, entry.getBaseEntry());
        return entry.getID().toString() ;
    }
    //修改
    public void updEntry(ControlAppUserEntry e) {
        BasicDBObject query=new BasicDBObject(Constant.ID,e.getID());
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,e.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_PARENT_APP, query,updateValue);
    }

    //单查询
    public ControlAppUserEntry getEntry(ObjectId parentId,ObjectId userId) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO) .append("pid", parentId).append("uid", userId);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_PARENT_APP, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new ControlAppUserEntry((BasicDBObject)dbo);
        }
        return null;
    }

}
