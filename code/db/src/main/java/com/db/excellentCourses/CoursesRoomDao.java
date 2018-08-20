package com.db.excellentCourses;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.excellentCourses.CoursesRoomEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<ObjectId,String> getPageList(List<ObjectId> olist){
        Map<ObjectId,String> map = new HashMap<ObjectId, String>();
        BasicDBObject query=new BasicDBObject()
                .append("cid",new BasicDBObject(Constant.MONGO_IN,olist))
                .append("isr", Constant.ZERO);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSES_ROOM,query,
                Constant.FIELDS,Constant.MONGO_SORTBY_DESC);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                CoursesRoomEntry coursesRoomEntry = new CoursesRoomEntry((BasicDBObject) obj);
                map.put(coursesRoomEntry.getContactId(),coursesRoomEntry.getRoomId());
            }
        }
        return map;



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
