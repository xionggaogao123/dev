package com.db.lancustom;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;


import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.integralmall.AddressEntry;
import com.pojo.integralmall.OrderEntry;
import com.pojo.lancustom.MobileReturnEntry;
import com.sys.constants.Constant;

public class mobileReturnDao extends BaseDao {

    public ObjectId addEntry(MobileReturnEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_MOBILE_RETURN, entry.getBaseEntry());
        return entry.getID();
    }
    
    public MobileReturnEntry getEntry(ObjectId id) {
        BasicDBObject query = new BasicDBObject("_id", id);
        query.append("isr",Constant.ZERO);
        DBObject obj =
            findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_MOBILE_RETURN, query, Constant.FIELDS);
        if (obj != null) {
           return new MobileReturnEntry((BasicDBObject) obj);
        }
        return null;
    }
    
    public List<MobileReturnEntry> getList(int page,int pageSize, ObjectId userId) {
        List<MobileReturnEntry> entries = new ArrayList<MobileReturnEntry>();
        BasicDBObject query=new BasicDBObject("uid",userId);
        query.append("isr",Constant.ZERO);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_MOBILE_RETURN,
            query,Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new MobileReturnEntry(dbObject));
            }
        }
        return entries;
    }
    
    public List<MobileReturnEntry> getList(ObjectId userId) {
        List<MobileReturnEntry> entries = new ArrayList<MobileReturnEntry>();
        BasicDBObject query=new BasicDBObject("uid",userId);
        query.append("isr",Constant.ZERO);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_MOBILE_RETURN,
            query,Constant.FIELDS,Constant.MONGO_SORTBY_DESC);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new MobileReturnEntry(dbObject));
            }
        }
        return entries;
    }
    
    public List<MobileReturnEntry> getListAll(int page,int pageSize) {
        List<MobileReturnEntry> entries = new ArrayList<MobileReturnEntry>();
        BasicDBObject query=new BasicDBObject();
        query.append("isr",Constant.ZERO);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_MOBILE_RETURN,
            query,Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new MobileReturnEntry(dbObject));
            }
        }
        return entries;
    }
    
    public List<MobileReturnEntry> getListAll() {
        List<MobileReturnEntry> entries = new ArrayList<MobileReturnEntry>();
        BasicDBObject query=new BasicDBObject();
        query.append("isr",Constant.ZERO);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_MOBILE_RETURN,
            query,Constant.FIELDS,Constant.MONGO_SORTBY_DESC);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new MobileReturnEntry(dbObject));
            }
        }
        return entries;
    }
    
    /**
     * 更新物流信息
     */
    public void updateEx(ObjectId orderId, String xcompanyNo, String expressNo) {
        DBObject query = new BasicDBObject(Constant.ID, orderId);
        BasicDBObject updateValue=new BasicDBObject()
            .append(Constant.MONGO_SET,new BasicDBObject("excompanyNo",xcompanyNo).append("expressNo",expressNo));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_MOBILE_RETURN,query,updateValue);
    }
    
    /**
     * 更新状态
     */
    public void handleState(ObjectId goodId, int state) {
        DBObject query = new BasicDBObject(Constant.ID, goodId);
        BasicDBObject updateValue=new BasicDBObject()
            .append(Constant.MONGO_SET,new BasicDBObject("sta",state));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_MOBILE_RETURN,query,updateValue);
    }
    
    
}
