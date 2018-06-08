package com.db.excellentCourses;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.excellentCourses.UserAccountEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018-05-30.
 */
public class UserAccountDao extends BaseDao {
    //添加账户
    public String addEntry(UserAccountEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_ACCOUNT, entry.getBaseEntry());
        return entry.getID().toString();
    }

    //删除账户
    public void deleteEntry(ObjectId userId){
        BasicDBObject query = new BasicDBObject();
        query.append("uid",userId);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isr",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_ACCOUNT, query,updateValue);
    }


    //查询
    public UserAccountEntry getEntry(ObjectId userId){
        BasicDBObject query=new BasicDBObject("uid",userId).append("isr",Constant.ZERO);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_ACCOUNT,query,Constant.FIELDS);
        if(null!=dbObject){
            return new UserAccountEntry((BasicDBObject) dbObject);
        }else {
            return null;
        }
    }


}
