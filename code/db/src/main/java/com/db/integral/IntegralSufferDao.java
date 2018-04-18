package com.db.integral;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.integral.IntegralSufferEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018-04-16.
 */
public class IntegralSufferDao extends BaseDao {

    //添加
    public ObjectId addEntry(IntegralSufferEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEGRAL_SUFFER, entry.getBaseEntry());
        return entry.getID();
    }

    public IntegralSufferEntry getEntry(ObjectId userId) {
        BasicDBObject query = new BasicDBObject("uid",userId);
        query.append("isr",Constant.ZERO);
        DBObject obj =
                findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEGRAL_SUFFER, query, Constant.FIELDS);
        if (obj != null) {
            return new IntegralSufferEntry((BasicDBObject) obj);
        }
        return null;
    }

    /**
     * 修改
     */
    public void updEntry(IntegralSufferEntry e) {
        BasicDBObject query=new BasicDBObject(Constant.ID,e.getID());
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,e.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEGRAL_SUFFER, query,updateValue);
    }
}
