package com.db.excellentCourses;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.excellentCourses.RechargeResultEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018-05-10.
 */
public class RechargeResultDao extends BaseDao {
    public String saveEntry(RechargeResultEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_RECHARGE_RESULT,entry.getBaseEntry());
        return entry.getID().toString();
    }

    public RechargeResultEntry getEntry(ObjectId id){
        BasicDBObject query=new BasicDBObject(Constant.ID,id).append("isr",Constant.ZERO);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_RECHARGE_RESULT,query,Constant.FIELDS);
        if(null!=dbObject){
            return new RechargeResultEntry((BasicDBObject) dbObject);
        }else {
            return null;
        }
    }
}
