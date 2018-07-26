package com.db.business;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.business.UserAgreementEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018-07-26.
 */
public class UserAgreementDao extends BaseDao {
    public String addEntry(UserAgreementEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_AGREEMENT, entry.getBaseEntry());
        return entry.getID().toString() ;
    }


    //单查询
    public UserAgreementEntry getEntry(ObjectId userId) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO) .append("uid", userId);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_AGREEMENT, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new UserAgreementEntry((BasicDBObject)dbo);
        }
        return null;
    }
}
