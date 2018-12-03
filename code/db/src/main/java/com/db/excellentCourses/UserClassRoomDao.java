package com.db.excellentCourses;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.excellentCourses.UserClassRoomEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018-11-26.
 */
public class UserClassRoomDao extends BaseDao {

    //添加用户课程直播间信息
    public String addEntry(UserClassRoomEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_CLASS_ROOM, entry.getBaseEntry());
        return entry.getID().toString();
    }

    public UserClassRoomEntry getEntry(ObjectId userId,ObjectId cid){
        BasicDBObject query=new BasicDBObject("uid",userId).append("cid",cid);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_CLASS_ROOM,query,Constant.FIELDS);
        if(null!=dbObject){
            return new UserClassRoomEntry((BasicDBObject) dbObject);
        }else {
            return null;
        }
    }

    public boolean booleanEntry(String roomId){
        BasicDBObject query=new BasicDBObject("rid",roomId);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_CLASS_ROOM,query,Constant.FIELDS);
        if(null!=dbObject){
            return true;
        }else {
            return false;
        }
    }
}
