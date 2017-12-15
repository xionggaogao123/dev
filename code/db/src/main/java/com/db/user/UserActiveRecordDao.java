package com.db.user;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.user.UserActiveRecordEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/12/15.
 */
public class UserActiveRecordDao extends BaseDao{

    public void saveEntry(UserActiveRecordEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_ACTIVE_RECORD,entry.getBaseEntry());
    }

    public void updateActiveTime(ObjectId userId,long updateTime){
        BasicDBObject query = new BasicDBObject()
                .append("uid",userId);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("upt",updateTime));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_ACTIVE_RECORD,query,updateValue);
    }

    public UserActiveRecordEntry getEntryByUserId(ObjectId userId){
        BasicDBObject query = new BasicDBObject()
                .append("uid",userId);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_ACTIVE_RECORD,query,Constant.FIELDS);
        if(null!=dbObject){
            return new UserActiveRecordEntry(dbObject);
        }else {
            return null;
        }
    }
}
