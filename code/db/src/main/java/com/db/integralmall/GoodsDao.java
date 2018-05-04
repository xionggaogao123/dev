package com.db.integralmall;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.integralmall.GoodsEntry;
import com.pojo.reportCard.GroupExamDetailEntry;
import com.sys.constants.Constant;

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
    
    public GoodsEntry getEntry(ObjectId id) {
        BasicDBObject query = new BasicDBObject("_id", id);
        query.append("isr",Constant.ZERO);
        DBObject obj =
            findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEGRAL_GOODS, query, Constant.FIELDS);
        if (obj != null) {
            new GoodsEntry((BasicDBObject) obj);
        }
        return null;
    }
    
    public List<GoodsEntry> getGoodsList(int page,int pageSize) {
        List<GoodsEntry> entries = new ArrayList<GoodsEntry>();
        BasicDBObject query=new BasicDBObject();
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEGRAL_GOODS,
            query,Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new GoodsEntry(dbObject));
            }
        }
        return entries;
    }
}
