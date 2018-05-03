package com.db.excellentCourses;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.excellentCourses.UserBehaviorEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018-04-27.
 */
public class UserBehaviorDao extends BaseDao {
    //添加收藏
    public String addEntry(UserBehaviorEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_BEHAVIOR, entry.getBaseEntry());
        return entry.getID().toString();
    }

    public UserBehaviorEntry getEntry(ObjectId userId){
        BasicDBObject query=new BasicDBObject("uid",userId).append("isr",Constant.ZERO);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_BEHAVIOR,query,Constant.FIELDS);
        if(null!=dbObject){
            return new UserBehaviorEntry((BasicDBObject) dbObject);
        }else {
            return null;
        }
    }
}
