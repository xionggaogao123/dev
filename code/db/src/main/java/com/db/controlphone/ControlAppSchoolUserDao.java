package com.db.controlphone;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.controlphone.ControlAppSchoolUserEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018-09-27.
 */
public class ControlAppSchoolUserDao extends BaseDao {

    //添加
    public String addEntry(ControlAppSchoolUserEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_APP_SCHOOL_USER, entry.getBaseEntry());
        return entry.getID().toString() ;
    }
    //单查询
    public ControlAppSchoolUserEntry getEntry(ObjectId userId,ObjectId communityId) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO) .append("cid", communityId) .append("uid", userId);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_APP_SCHOOL_USER, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new ControlAppSchoolUserEntry((BasicDBObject)dbo);
        }
        return null;
    }
}
