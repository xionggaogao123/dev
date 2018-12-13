package com.db.integral;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.integral.IntegralSufferEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<ObjectId,Integer> selectContentList(List<ObjectId> userIds) {
        BasicDBObject query = new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("uid",new BasicDBObject(Constant.MONGO_IN,userIds));
        List<DBObject> dboList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEGRAL_SUFFER, query, Constant.FIELDS,Constant.MONGO_SORTBY_DESC);
        Map<ObjectId,Integer> map = new HashMap<ObjectId, Integer>();
        if (null != dboList && !dboList.isEmpty()) {
            for (DBObject dbo : dboList) {
                IntegralSufferEntry entry = new IntegralSufferEntry((BasicDBObject) dbo);
                map.put(entry.getUserId(),entry.getScore());
            }
        }
        return map;
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
