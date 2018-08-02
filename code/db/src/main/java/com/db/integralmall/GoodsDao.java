package com.db.integralmall;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.integralmall.GoodsEntry;
import com.sys.constants.Constant;
import com.sys.utils.StringUtil;

public class GoodsDao extends BaseDao {

    public ObjectId addEntry(GoodsEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEGRAL_GOODS, entry.getBaseEntry());
        return entry.getID();
    }
    
    /**
     * 更新申述内容、申述状态
     */
    public void updateGoodsTimes(ObjectId goodId,long times) {
        DBObject query = new BasicDBObject(Constant.ID, goodId);
        BasicDBObject updateValue=new BasicDBObject()
            .append(Constant.MONGO_SET,new BasicDBObject("times",times));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEGRAL_GOODS,query,updateValue);
    }
    
    /**
     * 删除
     */
    public void updateIsr(ObjectId goodId) {
        DBObject query = new BasicDBObject(Constant.ID, goodId);
        BasicDBObject updateValue=new BasicDBObject()
            .append(Constant.MONGO_SET,new BasicDBObject("isr",1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEGRAL_GOODS,query,updateValue);
    }
    
    public void updateGood(ObjectId id, String avatar, String pic,String label, String name, int cost, String description) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject updateValue=new BasicDBObject()
            .append(Constant.MONGO_SET,new BasicDBObject("avat",avatar).append("pic",pic).append("lab", label).append("name", name).append("cost", cost).append("descrip", description));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEGRAL_GOODS,query,updateValue);
    }
    
    /**
     * 更新商品图片
     */
    public void updateGoodsAvatar(ObjectId goodId,String avatar) {
        DBObject query = new BasicDBObject(Constant.ID, goodId);
        BasicDBObject updateValue=new BasicDBObject()
            .append(Constant.MONGO_SET,new BasicDBObject("avat",avatar));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEGRAL_GOODS,query,updateValue);
    }
    
    public GoodsEntry getEntry(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        query.append("isr",Constant.ZERO);
        DBObject obj =
            findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEGRAL_GOODS, query, Constant.FIELDS);
        if (obj != null) {
            return new GoodsEntry((BasicDBObject) obj);
        }
        return null;
    }
    
    public GoodsEntry getEntryById(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        DBObject obj =
            findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEGRAL_GOODS, query, Constant.FIELDS);
        if (obj != null) {
            return new GoodsEntry((BasicDBObject) obj);
        }
        return null;
    }
    
    public List<GoodsEntry> getGoodsList(String name,int page,int pageSize) {
        List<GoodsEntry> entries = new ArrayList<GoodsEntry>();
        
        BasicDBObject query=new BasicDBObject().append("isr", 0);
        if (StringUtils.isNotBlank(name)) {
            query.append("name", new BasicDBObject(Constant.MONGO_REGEX,name));
        }
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEGRAL_GOODS,
            query,Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new GoodsEntry(dbObject));
            }
        }
        return entries;
    }
    
    public int getGoodsListAll(String name) {

        BasicDBObject query=new BasicDBObject().append("isr", 0);
        if (StringUtils.isNotBlank(name)) {
            query.append("name", new BasicDBObject(Constant.MONGO_REGEX,name));
        }
        int i =count(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEGRAL_GOODS,
            query);
       
        return i;
    }
}
