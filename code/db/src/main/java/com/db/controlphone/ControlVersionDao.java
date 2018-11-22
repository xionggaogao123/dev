package com.db.controlphone;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.controlphone.ControlVersionEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018/3/22.
 */
public class ControlVersionDao extends BaseDao {

    //上线修改
    public void updateEntry(ObjectId userId,long time,int status,String channelId){
        BasicDBObject query = new BasicDBObject("uid",userId).append("typ", Constant.ONE);
        //query.append("tim",new BasicDBObject(Constant.MONGO_LT,time));
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("tim",time).append("hid", channelId).append("sta",status));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_VERSION, query,updateValue);
    }
    //下线修改
    public void updateNewEntry(ObjectId userId,long time,int status,String channelId){
        BasicDBObject query = new BasicDBObject("uid", userId).append("typ",Constant.ONE);
        query.append("hid",channelId);
        //query.append("tim",new BasicDBObject(Constant.MONGO_LT,time));
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("tim",time).append("sta",status));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_VERSION, query,updateValue);
    }

    public List<ControlVersionEntry> getCommunityVersionList(ObjectId communityId){
        BasicDBObject query = new BasicDBObject();
        query.append("cid",communityId).append("isr",Constant.ZERO).append("typ",2);
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_CONTROL_VERSION,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC,0,10);
        List<ControlVersionEntry> entryList = new ArrayList<ControlVersionEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ControlVersionEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    public List<ControlVersionEntry> getAllStudentVersionList(List<ObjectId> userIds,long current){
        BasicDBObject query = new BasicDBObject();
        query.append("uid",new BasicDBObject(Constant.MONGO_IN,userIds)).append("isr",Constant.ZERO).append("dtm",new BasicDBObject(Constant.MONGO_GT,current));
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_CONTROL_VERSION,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<ControlVersionEntry> entryList = new ArrayList<ControlVersionEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ControlVersionEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
    public List<ControlVersionEntry> getNewAllStudentVersionList(List<ObjectId> userIds){
        BasicDBObject query = new BasicDBObject();
        query.append("uid",new BasicDBObject(Constant.MONGO_IN,userIds)).append("isr",Constant.ZERO);
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_CONTROL_VERSION,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<ControlVersionEntry> entryList = new ArrayList<ControlVersionEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ControlVersionEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }


    //个人单查询
    public ControlVersionEntry getEntry(ObjectId userId,int type) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO).append("uid", userId).append("typ",type);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_VERSION, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new ControlVersionEntry((BasicDBObject)dbo);
        }
        return null;
    }

    //家长单查询
    public ControlVersionEntry getEntryForParent(ObjectId userId,ObjectId sonId,int type) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO).append("uid", userId).append("cid",sonId).append("typ",type);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_VERSION, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new ControlVersionEntry((BasicDBObject)dbo);
        }
        return null;
    }

    //社群单查询
    public ControlVersionEntry getEntry(ObjectId communityId,ObjectId userId,int type) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO).append("uid", userId).append("typ",type);
        if(communityId!=null){
            query.append("cid",communityId);
        }
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_VERSION, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new ControlVersionEntry((BasicDBObject)dbo);
        }
        return null;
    }

    //社群单查询
    public ControlVersionEntry getEntryByCommunityId(ObjectId communityId,int type) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO).append("cid", communityId).append("typ",type);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_VERSION, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new ControlVersionEntry((BasicDBObject)dbo);
        }
        return null;
    }

    //添加
    public String addEntry(ControlVersionEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_VERSION, entry.getBaseEntry());
        return entry.getID().toString() ;
    }
}
