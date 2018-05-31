package com.db.excellentCourses;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.excellentCourses.RefundEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018-05-28.
 */
public class RefundDao extends BaseDao {
    public RefundEntry getEntry(ObjectId id){
        BasicDBObject query=new BasicDBObject(Constant.ID,id).append("isr",Constant.ZERO);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_REFUND,query,Constant.FIELDS);
        if(null!=dbObject){
            return new RefundEntry((BasicDBObject) dbObject);
        }else {
            return null;
        }
    }

    //添加课程
    public String addEntry(RefundEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_REFUND, entry.getBaseEntry());
        return entry.getID().toString();
    }
}
