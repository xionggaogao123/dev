package com.db.appactivity;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.appactivity.AppActivityUserEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/12/27.
 */
public class AppActivityUserDao extends BaseDao{

    public void saveEntry(AppActivityUserEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_APP_ACTIVITY_USER,entry.getBaseEntry());
    }

    public void saveEntries(List<AppActivityUserEntry> entries){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_APP_ACTIVITY_USER, MongoUtils.fetchDBObjectList(entries));
    }

    public void removeActivityData(ObjectId activityId){
        BasicDBObject query =new BasicDBObject()
                .append("ai",activityId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_APP_ACTIVITY_USER,query);
    }

    public void removeEntry(ObjectId activityId,
                            ObjectId userId){
        BasicDBObject query =new BasicDBObject()
                .append("ai",activityId)
                .append("uid",userId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_APP_ACTIVITY_USER,query);
    }

    public List<AppActivityUserEntry> getEntries(ObjectId activityId){
        BasicDBObject query =new BasicDBObject()
                .append("ai",activityId);
        List<AppActivityUserEntry> entries =new ArrayList<AppActivityUserEntry>();
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_APP_ACTIVITY_USER,query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                entries.add(new AppActivityUserEntry(dbObject));
            }
        }
        return entries;
    }

    public AppActivityUserEntry getEntry(ObjectId activityId,
                                         ObjectId userId){
        BasicDBObject query =new BasicDBObject()
                .append("ai",activityId)
                .append("uid",userId);
        DBObject dbObject =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_APP_ACTIVITY_USER,query,Constant.FIELDS);
        if(null!=dbObject){
            return new AppActivityUserEntry(dbObject);
        }else {
            return null;
        }
    }
}
