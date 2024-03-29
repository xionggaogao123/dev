package com.db.excellentCourses;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.excellentCourses.ClassOrderEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * Created by James on 2018-04-26.
 */
public class ClassOrderDao extends BaseDao {

    /**
     * 批量保存
     *
     * @param list
     */
    public void addBatch(List<DBObject> list) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_ORDER, list);
    }

    //添加课程
    public String addEntry(ClassOrderEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_ORDER, entry.getBaseEntry());
        return entry.getID().toString();
    }
    //删除
    public void updateBindEntry(ObjectId userId,ObjectId mainUserId){
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(0);
        BasicDBObject query=new BasicDBObject("uid",mainUserId).append("isb",1).append("typ", new BasicDBObject(Constant.MONGO_IN, list)).append("isr", Constant.ZERO);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("uid",userId));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_ORDER, query,updateValue);
    }
    //首页订单查询
    public  List<ClassOrderEntry> getEntry(ObjectId userId){
        List<ClassOrderEntry> entryList=new ArrayList<ClassOrderEntry>();
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(0);
        BasicDBObject query=new BasicDBObject("uid",userId).append("isb",1).append("typ", new BasicDBObject(Constant.MONGO_IN, list)).append("isr", Constant.ZERO);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_ORDER, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ClassOrderEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    //orderId
    public  List<ClassOrderEntry> getOrderEntry(String orderId){
        List<ClassOrderEntry> entryList=new ArrayList<ClassOrderEntry>();
        BasicDBObject query=new BasicDBObject("oid",orderId).append("isb", 0).append("isr", Constant.ZERO);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_ORDER, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ClassOrderEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    //首页订单查询
    public  List<ObjectId> getObjectIdEntry(ObjectId userId){
        List<ObjectId> entryList=new ArrayList<ObjectId>();
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(0);
        BasicDBObject query=new BasicDBObject("uid",userId).append("isb",1).append("typ",new BasicDBObject(Constant.MONGO_IN,list)).append("isr", Constant.ZERO);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_ORDER, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ClassOrderEntry((BasicDBObject) obj).getParentId());
            }
        }
        return entryList;
    }

    //首页订单查询
    public  List<ObjectId> getOwnEntry(List<ObjectId> parentIds,ObjectId userId){
        List<ObjectId> entryList=new ArrayList<ObjectId>();
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(0);
        BasicDBObject query=new BasicDBObject("pid",new BasicDBObject(Constant.MONGO_IN,parentIds)).append("typ",new BasicDBObject(Constant.MONGO_IN,list));
        query.append("uid",userId).append("isr", Constant.ZERO);
        query.append("isb",Constant.ONE);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_ORDER, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ClassOrderEntry((BasicDBObject) obj).getParentId());
            }
        }
        return entryList;
    }

    //课程订单用户查询
    public  Set<ObjectId> getUserIdEntry(ObjectId contactId){
        Set<ObjectId> entryList=new HashSet<ObjectId>();
        BasicDBObject query=new BasicDBObject();
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(0);
        query.append("cid",contactId).append("isr", Constant.ZERO).append("isb", Constant.ONE);
        query.append("typ",new BasicDBObject(Constant.MONGO_IN,list));
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_ORDER, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ClassOrderEntry((BasicDBObject) obj).getUserId());
            }
        }
        return entryList;
    }

    public  Map<ObjectId,ClassOrderEntry> getMapEntry(ObjectId contactId){
        Map<ObjectId,ClassOrderEntry> entryList=new HashMap<ObjectId, ClassOrderEntry>();
        BasicDBObject query=new BasicDBObject();
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(0);
        query.append("cid",contactId).append("isr", Constant.ZERO).append("isb", Constant.ONE);
        query.append("typ",new BasicDBObject(Constant.MONGO_IN,list));
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_ORDER, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                ClassOrderEntry classOrderEntry = new ClassOrderEntry((BasicDBObject) obj);
                entryList.put(classOrderEntry.getUserId(), classOrderEntry);
            }
        }
        return entryList;
    }

    //课程订单用户查询
    public  List<ClassOrderEntry> getCoursesUserList(ObjectId contactId){
        List<ClassOrderEntry> entryList=new ArrayList<ClassOrderEntry>();
        BasicDBObject query=new BasicDBObject();
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(0);
        query.append("cid",contactId).append("isr", Constant.ZERO).append("isb", Constant.ONE);
        query.append("typ",new BasicDBObject(Constant.MONGO_IN,list));
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_ORDER, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ClassOrderEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    //所有订单
    public  List<ClassOrderEntry> getCoursesUserList(List<ObjectId> userIds){
        List<ClassOrderEntry> entryList=new ArrayList<ClassOrderEntry>();
        BasicDBObject query=new BasicDBObject();
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(0);
        query.append("uid",new BasicDBObject(Constant.MONGO_IN,userIds)).append("isr", Constant.ZERO).append("isb",Constant.ONE);
        query.append("typ",new BasicDBObject(Constant.MONGO_IN,list));
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_ORDER, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ClassOrderEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
    //课程订单用户查询
    public  List<ClassOrderEntry> getAllUserList(ObjectId parentId){
        List<ClassOrderEntry> entryList=new ArrayList<ClassOrderEntry>();
        BasicDBObject query=new BasicDBObject();
        List<Integer> list = new ArrayList<Integer>();
       // list.add(1);//未进入
        query.append("pid",parentId).append("isr", Constant.ZERO).append("isb", Constant.ONE).append("typ",Constant.ONE);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_ORDER, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ClassOrderEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    //用户订单查询
    public ClassOrderEntry getEntry(ObjectId parentId,ObjectId contactId,ObjectId userId){
        BasicDBObject query=new BasicDBObject().append("isr",Constant.ZERO).append("isb",Constant.ONE);
        query.append("pid",parentId).append("cid",contactId).append("uid",userId);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_ORDER,query,Constant.FIELDS);
        if(null!=dbObject){
            return new ClassOrderEntry((BasicDBObject) dbObject);
        }else {
            return null;
        }
    }

    //首页订单查询
    public  List<ObjectId> getEntryIdList(ObjectId userId,ObjectId contactId){
        List<ObjectId> entryList=new ArrayList<ObjectId>();
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(0);
        BasicDBObject query=new BasicDBObject("uid",userId).append("isb",Constant.ONE).append("cid", contactId).append("typ", new BasicDBObject(Constant.MONGO_IN, list)).append("isr",Constant.ZERO);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_ORDER, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ClassOrderEntry((BasicDBObject) obj).getParentId());
            }
        }
        return entryList;
    }

    public  List<ClassOrderEntry> getAllEntryIdList(ObjectId userId,ObjectId contactId){
        List<ClassOrderEntry> entryList=new ArrayList<ClassOrderEntry>();
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(0);
        BasicDBObject query=new BasicDBObject("uid",userId).append("isb",Constant.ONE).append("cid", contactId).append("typ", new BasicDBObject(Constant.MONGO_IN, list)).append("isr",Constant.ZERO);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_ORDER, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ClassOrderEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    //查询首页显示列表
    public List<ClassOrderEntry> getPageList(List<ObjectId> olist,ObjectId userId,int page,int pageSize){
        List<ClassOrderEntry> entryList=new ArrayList<ClassOrderEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("cid",new BasicDBObject(Constant.MONGO_IN,olist))
                        //.append("uid",new BasicDBObject(Constant.MONGO_NE,userId))
                .append("isr", Constant.ZERO);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_ORDER,query,
                Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ClassOrderEntry((BasicDBObject) obj));
            }
        }
        return entryList;



    }

    //查询首页显示列表
    public Map<ObjectId,ClassOrderEntry> getEntryList(ObjectId userId,ObjectId contactId){
        Map<ObjectId,ClassOrderEntry> map = new HashMap<ObjectId, ClassOrderEntry>();
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(0);
        BasicDBObject query=new BasicDBObject("uid",userId).append("isb",Constant.ONE).append("cid", contactId).append("typ", new BasicDBObject(Constant.MONGO_IN, list)).append("isr", Constant.ZERO);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_ORDER, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                ClassOrderEntry classOrderEntry = new ClassOrderEntry((BasicDBObject) obj);
                map.put(classOrderEntry.getParentId(),classOrderEntry);
            }
        }
        return map;
    }

    public List<ClassOrderEntry> getAllEntryMap(ObjectId contactId){
        List<ClassOrderEntry> classOrderEntryList = new ArrayList<ClassOrderEntry>();
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(0);
        BasicDBObject query=new BasicDBObject().append("isb",Constant.ONE).append("cid", contactId).append("typ", new BasicDBObject(Constant.MONGO_IN, list)).append("isr", Constant.ZERO);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_ORDER, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                ClassOrderEntry classOrderEntry = new ClassOrderEntry((BasicDBObject) obj);
                classOrderEntryList.add(classOrderEntry);
            }
        }
        return classOrderEntryList;
    }

    //批量删除
    public void delEntry(ObjectId parentId,ObjectId userId){
        BasicDBObject query = new BasicDBObject("pid",parentId).append("uid",userId);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isr",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_ORDER, query,updateValue);
    }
    //批量删除个人订单
    public void delOrderEntry(ObjectId contactId,ObjectId userId){
        BasicDBObject query = new BasicDBObject("cid",contactId).append("uid",userId);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isr",Constant.ONE).append("typ",Constant.FIVE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_ORDER, query,updateValue);
    }

    //批量退课
    public void updateEntry(ObjectId contactId,ObjectId userId){
        BasicDBObject query = new BasicDBObject("cid",contactId).append("uid",userId);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("typ",Constant.THREE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_ORDER, query,updateValue);
    }

    //批量退课 ----退课了
    public void updateEntry(List<ObjectId> classIds){
        BasicDBObject query = new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_IN,classIds));
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("typ",Constant.THREE).append("isr",Constant.ZERO).append("isb",Constant.ZERO));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_ORDER, query,updateValue);
    }

    //上课去
    public void updateToEntry(ObjectId id,long time){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("typ",Constant.ZERO).append("gtm",time));//使用中
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_ORDER, query,updateValue);
    }

    //批量退课
    public void updateEntry(List<ObjectId> classIds,ObjectId userId){
        BasicDBObject query = new BasicDBObject("pid",new BasicDBObject(Constant.MONGO_IN,classIds)).append("uid",userId);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("typ",Constant.THREE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_ORDER, query,updateValue);
    }

    //批量修改已购买
    public void updateEntryToBuy(List<ObjectId> objectIds){
        BasicDBObject query = new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_IN,objectIds));
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isb",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_ORDER, query,updateValue);
    }
}
