package com.db.extendedcourse;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.extendedcourse.ExtendedUserApplyEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-12-10.
 */
public class ExtendedUserApplyDao extends BaseDao {
    public void saveEntry(ExtendedUserApplyEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_EXTENDED_USER_APPLY,entry.getBaseEntry());
    }


    //删除
    public void delAllEntry(ObjectId courseId,ObjectId communityId,List<ObjectId> userIds){
        BasicDBObject query = new BasicDBObject();
        query.append("isr",Constant.ZERO);
        query.append("cid",courseId);
        query.append("cmid",communityId);
        query.append("uid",new BasicDBObject(Constant.MONGO_IN,userIds));
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isr",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXTENDED_USER_APPLY, query,updateValue);
    }

    //查询已成功入选用户
    public List<ObjectId> getIdsByCourseId(ObjectId courseId,ObjectId communityId){
        List<ObjectId> entryList=new ArrayList<ObjectId>();
        BasicDBObject query = new BasicDBObject();
        query.append("isr",Constant.ZERO);
        query.append("cid",courseId);
        query.append("cmid",communityId);
        query.append("sta",Constant.TWO);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXTENDED_USER_APPLY, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ExtendedUserApplyEntry((BasicDBObject) obj).getUserId());
            }
        }
        return entryList;
    }

    public List<ExtendedUserApplyEntry> getEntrysByCourseId(ObjectId courseId){
        List<ExtendedUserApplyEntry> entryList=new ArrayList<ExtendedUserApplyEntry>();
        BasicDBObject query = new BasicDBObject();
        query.append("isr",Constant.ZERO);
        query.append("cid",courseId);
        query.append("sta",Constant.TWO);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXTENDED_USER_APPLY, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ExtendedUserApplyEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    public List<ExtendedUserApplyEntry> getEntrysByCourseId2(ObjectId courseId,ObjectId communityId){
        List<ExtendedUserApplyEntry> entryList=new ArrayList<ExtendedUserApplyEntry>();
        BasicDBObject query = new BasicDBObject();
        query.append("isr",Constant.ZERO);
        query.append("cid",courseId);
        query.append("cmid",communityId);
        query.append("sta",Constant.TWO);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXTENDED_USER_APPLY, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ExtendedUserApplyEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    public int countCourseUser(ObjectId courseId,ObjectId communityId){
        BasicDBObject query = new BasicDBObject();
        query.append("isr",Constant.ZERO);
        query.append("cid",courseId);
        query.append("cmid",communityId);
        query.append("sta",Constant.TWO);
        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_EXTENDED_USER_APPLY, query);
        return count;
    }

    public List<ExtendedUserApplyEntry> getAllEntrysByCourseId(ObjectId courseId,ObjectId communityId){
        List<ExtendedUserApplyEntry> entryList=new ArrayList<ExtendedUserApplyEntry>();
        BasicDBObject query = new BasicDBObject();
        query.append("isr",Constant.ZERO);
        query.append("cid",courseId);
        query.append("cmid",communityId);
        List<Integer> integers = new ArrayList<Integer>();
        integers.add(1);
        integers.add(2);
        query.append("sta",new BasicDBObject(Constant.MONGO_IN,integers));
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXTENDED_USER_APPLY, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ExtendedUserApplyEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    //查询用户记录
    public ExtendedUserApplyEntry getEntryById(ObjectId id,ObjectId userId){
        BasicDBObject query = new BasicDBObject();
        query.append("isr",Constant.ZERO);
        query.append("uid",userId);
        query.append("cid",id);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_EXTENDED_USER_APPLY,query,Constant.FIELDS);
        if(dbObject==null){
            return null;
        }else{
            return new ExtendedUserApplyEntry((BasicDBObject) dbObject);
        }
    }
}

