package com.db.excellentCourses;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.excellentCourses.CoursesRoomEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018-05-09.
 */
public class CoursesRoomDao extends BaseDao {

    public String addEntry( CoursesRoomEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSES_ROOM,entry.getBaseEntry());
        return entry.getID().toString();
    }

    public CoursesRoomEntry getEntry(ObjectId id){
        BasicDBObject query=new BasicDBObject("cid",id).append("isr",Constant.ZERO);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSES_ROOM,query,Constant.FIELDS);
        if(null!=dbObject){
            return new CoursesRoomEntry((BasicDBObject) dbObject);
        }else {
            return null;
        }
    }

    public CoursesRoomEntry getRoomEntry(String roomId){
        BasicDBObject query=new BasicDBObject("rid",roomId).append("isr",Constant.ZERO);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSES_ROOM,query,Constant.FIELDS);
        if(null!=dbObject){
            return new CoursesRoomEntry((BasicDBObject) dbObject);
        }else {
            return null;
        }
    }
}
