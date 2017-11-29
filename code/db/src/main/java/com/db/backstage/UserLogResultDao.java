package com.db.backstage;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.backstage.UserLogResultEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/11/29.
 */
public class UserLogResultDao extends BaseDao{

     public void saveUserLogEntry(UserLogResultEntry entry){
         save(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_LOG_RESULT,entry.getBaseEntry());
     }



     public UserLogResultEntry getEntryById(ObjectId id){
         BasicDBObject query=new BasicDBObject()
                 .append(Constant.ID,id);
         DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_LOG_RESULT,
                 query,Constant.FIELDS);
         if(null!=dbObject){
             return new UserLogResultEntry(dbObject);
         }else {
             return null;
         }
     }

     public UserLogResultEntry getEntryByUserId(ObjectId userId){
         BasicDBObject query=new BasicDBObject()
                 .append("uid",userId);
         DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_LOG_RESULT,
                 query,Constant.FIELDS);
         if(null!=dbObject){
             return new UserLogResultEntry(dbObject);
         }else {
             return null;
         }
     }
}
