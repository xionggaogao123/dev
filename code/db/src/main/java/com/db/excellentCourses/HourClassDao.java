package com.db.excellentCourses;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.excellentCourses.HourClassEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-04-26.
 */
public class HourClassDao extends BaseDao {

    /**
     * 批量保存
     *
     * @param list
     */
    public void addBatch(List<DBObject> list) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_HOUR_CLASS, list);
    }

    //添加课时
    public String addEntry(HourClassEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_HOUR_CLASS, entry.getBaseEntry());
        return entry.getID().toString();
    }

    //删除
    public void delEntry(ObjectId parentId,ObjectId userId){
        BasicDBObject query = new BasicDBObject("pid",parentId).append("uid",userId);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isr",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_HOUR_CLASS, query,updateValue);
    }

    //删除
    public void delOneEntry(ObjectId id){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isr",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_HOUR_CLASS, query,updateValue);
    }


    //删除
    public void sortEntry(ObjectId id,int order){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("ord",order));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_HOUR_CLASS, query,updateValue);
    }

    //修改价格
    public void updatePriceEntry(ObjectId id,double price){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("cnp",price));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_HOUR_CLASS, query,updateValue);
    }

    //修改课程时间
    public void updateClassTime(ObjectId id,long startTime,long date){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("stm",startTime).append("dtm",date));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_HOUR_CLASS, query,updateValue);
    }

    //批量修改课程定价
    public void updateNewPrice(ObjectId id,List<ObjectId> hourClassIds,double price){
        BasicDBObject query = new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_IN,hourClassIds));
        query.append("pid",id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("cnp",price));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_HOUR_CLASS, query,updateValue);
    }


    public HourClassEntry getEntry(ObjectId id){
        BasicDBObject query=new BasicDBObject(Constant.ID,id).append("isr",Constant.ZERO);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_HOUR_CLASS,query,Constant.FIELDS);
        if(null!=dbObject){
            return new HourClassEntry((BasicDBObject) dbObject);
        }else {
            return null;
        }
    }

    //查询课时
    public List<HourClassEntry> getEntryList(ObjectId parentId){
        List<HourClassEntry> entryList=new ArrayList<HourClassEntry>();
        BasicDBObject query=new BasicDBObject().append("pid", parentId).append("isr", Constant.ZERO);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_HOUR_CLASS, query,
                Constant.FIELDS, new BasicDBObject("ord",Constant.ASC));
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new HourClassEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    //查询课时
    public List<ObjectId> getObjectIdList(ObjectId parentId){
        List<ObjectId> entryList=new ArrayList<ObjectId>();
        BasicDBObject query=new BasicDBObject().append("pid", parentId).append("isr", Constant.ZERO);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_HOUR_CLASS, query,
                Constant.FIELDS, new BasicDBObject("ord",Constant.ASC));
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                HourClassEntry hourClassEntry = new HourClassEntry((BasicDBObject) obj);
                entryList.add(hourClassEntry.getID());
            }
        }
        return entryList;
    }

    public List<HourClassEntry> getEntryListByStartTime(ObjectId parentId){
        List<HourClassEntry> entryList=new ArrayList<HourClassEntry>();
        BasicDBObject query=new BasicDBObject().append("pid", parentId).append("isr", Constant.ZERO);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_HOUR_CLASS, query,
                Constant.FIELDS, new BasicDBObject("stm",Constant.ASC));
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new HourClassEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    //用户的所有课程列表
    public List<HourClassEntry> getIsNewObjectId(long startTime,long endTime) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        BasicDBList dblist =new BasicDBList();
        dblist.add(new BasicDBObject("stm", new BasicDBObject(Constant.MONGO_GTE, startTime)));
        dblist.add(new BasicDBObject("stm", new BasicDBObject(Constant.MONGO_LT, endTime)));
        query.append(Constant.MONGO_AND,dblist);
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_HOUR_CLASS, query, Constant.FIELDS);
        List<HourClassEntry> retList =new ArrayList<HourClassEntry>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new HourClassEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }


    //查询订单课时
    public List<HourClassEntry> getEntryList(List<ObjectId> ids){
        List<HourClassEntry> entryList=new ArrayList<HourClassEntry>();
        BasicDBObject query=new BasicDBObject().append(Constant.ID, new BasicDBObject(Constant.MONGO_IN,ids)).append("isr", Constant.ZERO);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_HOUR_CLASS, query,
                Constant.FIELDS, new BasicDBObject("ord", Constant.ASC));
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new HourClassEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    //查询订单课时
    public List<HourClassEntry> getParentEntryList(List<ObjectId> courseIds){
        List<HourClassEntry> entryList=new ArrayList<HourClassEntry>();
        BasicDBObject query=new BasicDBObject().append("pid", new BasicDBObject(Constant.MONGO_IN,courseIds)).append("isr", Constant.ZERO);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_HOUR_CLASS, query,
                Constant.FIELDS, new BasicDBObject("ord", Constant.ASC));
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new HourClassEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
    
    public Integer countHourClass(List<ObjectId> parentIds,long startTime, long endTime) {
        
        
        BasicDBObject query = new BasicDBObject();       
        BasicDBList values = new BasicDBList();
        
        values.add(new BasicDBObject().append("stm",  new BasicDBObject(Constant.MONGO_GTE, startTime)).append("pid", new BasicDBObject(Constant.MONGO_IN, parentIds)));
        values.add(new BasicDBObject().append("stm",  new BasicDBObject(Constant.MONGO_LT, endTime)));
        query.put(Constant.MONGO_AND, values);
  
        
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_HOUR_CLASS, query);
    }
}
