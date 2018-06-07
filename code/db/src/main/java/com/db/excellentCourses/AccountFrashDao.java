package com.db.excellentCourses;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.excellentCourses.AccountFrashEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018-06-06.
 */
public class AccountFrashDao extends BaseDao {
    //添加账户
    public String addEntry(AccountFrashEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_ACCOUNT_FRASH, entry.getBaseEntry());
        return entry.getID().toString();
    }

    public AccountFrashEntry getEntry(ObjectId userId){
        BasicDBObject query=new BasicDBObject("uid",userId).append("isr",Constant.ZERO);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_ACCOUNT_FRASH,query,Constant.FIELDS);
        if(null!=dbObject){
            return new AccountFrashEntry((BasicDBObject) dbObject);
        }else {
            return null;
        }
    }


    //修改
    public void updateEntry(ObjectId id,double price){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("acc",price));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ACCOUNT_FRASH, query,updateValue);
    }


}
