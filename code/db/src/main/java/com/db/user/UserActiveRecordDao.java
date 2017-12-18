package com.db.user;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.user.UserActiveRecordEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

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


    public List<UserActiveRecordEntry> getActiveRecordEntries(long startTime,long endTime,int page,int pageSize){
        List<UserActiveRecordEntry> entries = new ArrayList<UserActiveRecordEntry>();
        BasicDBObject query = new BasicDBObject()
                .append("upt",new BasicDBObject(Constant.MONGO_GTE,startTime).append(Constant.MONGO_LTE,endTime));
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_ACTIVE_RECORD,query,Constant.FIELDS,
                Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new UserActiveRecordEntry(dbObject));
            }
        }
        return entries;
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
