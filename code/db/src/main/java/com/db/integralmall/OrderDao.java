package com.db.integralmall;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.integralmall.GoodsEntry;
import com.pojo.integralmall.OrderEntry;
import com.sys.constants.Constant;

public class OrderDao extends BaseDao {

    public ObjectId addEntry(OrderEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEGRAL_ORDER, entry.getBaseEntry());
        return entry.getID();
    }
    
    public OrderEntry getEntry(ObjectId id) {
        BasicDBObject query = new BasicDBObject("_id", id);
        query.append("isr",Constant.ZERO);
        DBObject obj =
            findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEGRAL_ORDER, query, Constant.FIELDS);
        if (obj != null) {
           return new OrderEntry((BasicDBObject) obj);
        }
        return null;
    }
    
    public List<OrderEntry> getOrderListByUserId(int page,int pageSize, ObjectId userId) {
        List<OrderEntry> entries = new ArrayList<OrderEntry>();
        BasicDBObject query=new BasicDBObject("uid",userId);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEGRAL_ORDER,
            query,Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new OrderEntry(dbObject));
            }
        }
        return entries;
    }
    
    public List<OrderEntry> getOrderListByUserIdTotal(ObjectId userId) {
        List<OrderEntry> entries = new ArrayList<OrderEntry>();
        BasicDBObject query=new BasicDBObject("uid",userId);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEGRAL_ORDER,
            query,Constant.FIELDS,Constant.MONGO_SORTBY_DESC);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new OrderEntry(dbObject));
            }
        }
        return entries;
    }
    
    public List<OrderEntry> getOrderList(int page,int pageSize) {
        List<OrderEntry> entries = new ArrayList<OrderEntry>();
        BasicDBObject query=new BasicDBObject();
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEGRAL_ORDER,
            query,Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new OrderEntry(dbObject));
            }
        }
        return entries;
    }
    
    public List<OrderEntry> getOrderListAll() {
        List<OrderEntry> entries = new ArrayList<OrderEntry>();
        BasicDBObject query=new BasicDBObject();
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEGRAL_ORDER,
            query,Constant.FIELDS,Constant.MONGO_SORTBY_DESC);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new OrderEntry(dbObject));
            }
        }
        return entries;
    }
    
    /**
     * 更新申述内容、申述状态
     */
    public void updateOrderState(ObjectId orderId, String stateReason) {
        DBObject query = new BasicDBObject(Constant.ID, orderId);
        BasicDBObject updateValue=new BasicDBObject()
            .append(Constant.MONGO_SET,new BasicDBObject("sRea",stateReason).append("ist",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEGRAL_ORDER,query,updateValue);
    }
    
    /**
     * 更新物流信息
     */
    public void updateEx(ObjectId orderId, String xcompanyNo, String expressNo) {
        DBObject query = new BasicDBObject(Constant.ID, orderId);
        BasicDBObject updateValue=new BasicDBObject()
            .append(Constant.MONGO_SET,new BasicDBObject("ecNo",xcompanyNo).append("epNo",expressNo));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEGRAL_ORDER,query,updateValue);
    }
}
