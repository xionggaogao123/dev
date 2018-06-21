package com.db.business;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.business.BanningSpeakingEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018-06-21.
 */
public class BanningSpeakingDao extends BaseDao {

    public String addEntry(BanningSpeakingEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_MODULE_BANNING, entry.getBaseEntry());
        return entry.getID().toString() ;
    }

    //单查询
    public BanningSpeakingEntry getEntry(ObjectId userId,int modultType) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO) .append("uid", userId).append("mty",modultType);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_MODULE_BANNING, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new BanningSpeakingEntry((BasicDBObject)dbo);
        }
        return null;
    }

    //修改解析内容和图片
    public void updateEntry(ObjectId userId,int modultType){
        BasicDBObject query=new BasicDBObject();
        query.append("isr", Constant.ZERO) .append("uid", userId).append("mty",modultType);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isr",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_MODULE_BANNING, query, updateValue);
    }

}
