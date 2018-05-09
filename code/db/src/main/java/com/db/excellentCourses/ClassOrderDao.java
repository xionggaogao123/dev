package com.db.excellentCourses;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.excellentCourses.ClassOrderEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    //首页订单查询
    public  List<ClassOrderEntry> getEntry(ObjectId userId){
        List<ClassOrderEntry> entryList=new ArrayList<ClassOrderEntry>();
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(0);
        BasicDBObject query=new BasicDBObject("uid",userId).append("isBuy",1).append("typ", new BasicDBObject(Constant.MONGO_IN, list)).append("isr", Constant.ZERO);
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
        BasicDBObject query=new BasicDBObject("uid",userId).append("isBuy",1).append("typ",new BasicDBObject(Constant.MONGO_IN,list)).append("isr", Constant.ZERO);
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
        query.append("cid",contactId).append("isr", Constant.ZERO);
        query.append("typ",new BasicDBObject(Constant.MONGO_IN,list));
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
    public  List<ObjectId> getEntryIdList(ObjectId userId,ObjectId contactId){
        List<ObjectId> entryList=new ArrayList<ObjectId>();
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(0);
        BasicDBObject query=new BasicDBObject("uid",userId).append("cid",contactId).append("typ", new BasicDBObject(Constant.MONGO_IN, list)).append("isr",Constant.ZERO);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASS_ORDER, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ClassOrderEntry((BasicDBObject) obj).getParentId());
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
}
