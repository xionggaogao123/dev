package com.db.excellentCourses;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.excellentCourses.BackOrderEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-08-28.
 */
public class BackOrderDao  extends BaseDao {

    //添加课程
    public String addEntry(BackOrderEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_BACK_ORDER, entry.getBaseEntry());
        return entry.getID().toString();
    }


    public List<BackOrderEntry> getTuiOrderList(ObjectId id,int status,int page,int pageSize){
        List<BackOrderEntry> entryList=new ArrayList<BackOrderEntry>();
        BasicDBObject query=new BasicDBObject().append("isr",0);
        query.append("cid",id).append("sta",status);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_BACK_ORDER, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new BackOrderEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    public int countTuiOrderList(ObjectId id,int status) {
        BasicDBObject query=new BasicDBObject().append("isr",0);
        query.append("cid",id).append("sta",status);
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_BACK_ORDER,
                        query);
        return count;
    }
}
