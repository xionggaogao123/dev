package com.db.business;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.business.VersionOpenEntry;
import com.sys.constants.Constant;

/**
 * Created by James on 2018-06-01.
 */
public class VersionOpenDao extends BaseDao {
    public String addEntry(VersionOpenEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_VERSION_OPEN, entry.getBaseEntry());
        return entry.getID().toString() ;
    }

    //单查询
    public VersionOpenEntry getEntry(String moduleName) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO) .append("mnm", moduleName);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_VERSION_OPEN, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new VersionOpenEntry((BasicDBObject)dbo);
        }
        return null;
    }
}
