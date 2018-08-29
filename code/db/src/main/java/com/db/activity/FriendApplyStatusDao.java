package com.db.activity;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.activity.FriendApplyEntry;
import com.pojo.activity.FriendStatusEntry;
import com.sys.constants.Constant;

/**
 * Created by Hao on 2015/3/9.
 */
public class FriendApplyStatusDao extends BaseDao {
	
	 public ObjectId insertFriStatus(FriendStatusEntry fEntry) {
	        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FRIENDSTATUS, fEntry.getBaseEntry());
	        return fEntry.getID();
	    }
	
    public void updFriStatus(ObjectId userId, String status) {
        BasicDBObject query = new BasicDBObject("uid", userId);
        BasicDBObject update = new BasicDBObject("status", status);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, update);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FRIENDSTATUS, query, updateValue);
    }
   
    
    public FriendStatusEntry getfriendStatus(ObjectId uId) {
        BasicDBObject query = new BasicDBObject("uid", uId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FRIENDSTATUS, query, Constant.FIELDS);
        if(dbObject == null) return null;
        return new FriendStatusEntry((BasicDBObject) dbObject);
      }
    
    
}
