package com.db.integralmall;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.integralmall.AddressEntry;
import com.pojo.integralmall.GoodsEntry;
import com.sys.constants.Constant;

public class AddressDao extends BaseDao {

    public ObjectId addEntry(AddressEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEGRAL_ADDRESS, entry.getBaseEntry());
        return entry.getID();
    }
    
    public AddressEntry getEntry(ObjectId userId) {
        BasicDBObject query = new BasicDBObject("uid", userId);
        query.append("isr",Constant.ZERO);
        DBObject obj =
            findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEGRAL_ADDRESS, query, Constant.FIELDS);
        if (obj != null) {
            return new AddressEntry((BasicDBObject) obj);
        }
        return null;
    }
    
    public AddressEntry getEntryById(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        DBObject obj =
            findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEGRAL_ADDRESS, query, Constant.FIELDS);
        if (obj != null) {
            new AddressEntry((BasicDBObject) obj);
        }
        return null;
    }
    
    /**
     * 删除旧地址
     */
    public void updateAddressState(ObjectId userId) {
        DBObject query = new BasicDBObject("uid", userId);
        BasicDBObject updateValue=new BasicDBObject()
            .append(Constant.MONGO_SET,new BasicDBObject("isr",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEGRAL_ADDRESS,query,updateValue);
    }
}
