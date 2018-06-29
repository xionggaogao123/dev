package com.db.appactivity;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.appactivity.AppActivityEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/12/27.
 */
public class AppActivityDao extends BaseDao {

    public void saveEntry(AppActivityEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_APP_ACTIVITY,entry.getBaseEntry());
    }


    public void saveEntries(List<AppActivityEntry> entries){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_APP_ACTIVITY, MongoUtils.fetchDBObjectList(entries));
    }


    public void partInActivity(ObjectId activityId){
        BasicDBObject query = new BasicDBObject(Constant.ID,activityId);
        BasicDBObject updateValue= new BasicDBObject(Constant.MONGO_INC,new BasicDBObject("ptc",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_APP_ACTIVITY,query,updateValue);
    }


    public AppActivityEntry getEntryById(ObjectId activityId){
        BasicDBObject query = new BasicDBObject(Constant.ID,activityId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_APP_ACTIVITY,query,Constant.FIELDS);
        if(null!=dbObject){
            return new AppActivityEntry(dbObject);
        }else {
            return null;
        }
    }


    public void popActivity(ObjectId activityId){
        BasicDBObject query = new BasicDBObject(Constant.ID,activityId);
        BasicDBObject updateValue= new BasicDBObject(Constant.MONGO_INC,new BasicDBObject("ptc",Constant.NEGATIVE_ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_APP_ACTIVITY,query,updateValue);
    }

    public void removeActivity(ObjectId activityId){
        BasicDBObject query = new BasicDBObject(Constant.ID,activityId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("ir",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_APP_ACTIVITY,query,updateValue);
    }


    public void removeById(ObjectId activityId){
        BasicDBObject query = new BasicDBObject(Constant.ID,activityId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_APP_ACTIVITY,query);
    }

    public BasicDBObject getGatherQueryCondition(ObjectId userId,
                                                 List<ObjectId> groupIds){
        List<Integer> visiblePermission = new ArrayList<Integer>();
        visiblePermission.add(Constant.ONE);
        visiblePermission.add(Constant.THREE);
        BasicDBObject query = new BasicDBObject();
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject("uid",userId));
        values.add(new BasicDBObject("uid",new BasicDBObject(Constant.MONGO_NE,userId))
                .append("gid",new BasicDBObject(Constant.MONGO_IN,groupIds))
                .append("vp",new BasicDBObject(Constant.MONGO_IN,visiblePermission)));
        query.append(Constant.MONGO_OR,values);
        query.append("ir", Constant.ZERO);
        return query;
    }

    public int countGatherActivities(ObjectId userId,
                                     List<ObjectId> groupIds){
        BasicDBObject query = getGatherQueryCondition(userId,groupIds);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_APP_ACTIVITY,query);
    }

    public BasicDBObject getStudentReceivedCondition(List<ObjectId> groupIds){
        List<Integer> visiblePermission = new ArrayList<Integer>();
        visiblePermission.add(Constant.TWO);
        visiblePermission.add(Constant.THREE);
        BasicDBObject query=new BasicDBObject()
                .append("gid",new BasicDBObject(Constant.MONGO_IN,groupIds))
                .append("vp",new BasicDBObject(Constant.MONGO_IN,visiblePermission))
                .append("ir", Constant.ZERO);
        return query;
    }


    public List<AppActivityEntry> getStudentActivities(List<ObjectId> groupIds,
                                                       int page,
                                                       int pageSize){
        List<AppActivityEntry> entries = new ArrayList<AppActivityEntry>();
        BasicDBObject query = getStudentReceivedCondition(groupIds);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_APP_ACTIVITY,query,
                Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new AppActivityEntry(dbObject));
            }
        }
        return entries;
    }

    public List<AppActivityEntry> getEntriesById(List<ObjectId> ids){
        List<AppActivityEntry> entries = new ArrayList<AppActivityEntry>();
        BasicDBObject query = new BasicDBObject();
        query.append("ir",Constant.ZERO);
        query.append(Constant.ID,new BasicDBObject(Constant.MONGO_IN,ids));
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_APP_ACTIVITY,query,
                Constant.FIELDS,Constant.MONGO_SORTBY_DESC);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new AppActivityEntry(dbObject));
            }
        }
        return entries;
    }

    public int countStudentActivities(List<ObjectId> groupIds){
        BasicDBObject query = getStudentReceivedCondition(groupIds);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_APP_ACTIVITY,query);
    }


    public List<AppActivityEntry> getGatherActivities(ObjectId userId,
                                                      List<ObjectId> groupIds,
                                                      int page,
                                                      int pageSize){
        List<AppActivityEntry> entries = new ArrayList<AppActivityEntry>();
        BasicDBObject query = getGatherQueryCondition(userId,groupIds);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_APP_ACTIVITY,query,
                Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new AppActivityEntry(dbObject));
            }
        }
        return entries;
    }

}
