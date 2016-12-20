package com.db.train;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.train.RegionCopyEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by admin on 2016/12/19.
 */
public class RegionCopyDao extends BaseDao {

    public ObjectId saveOrUpdate(RegionCopyEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_TRAIN_REGIONS_COPY,entry.getBaseEntry());
        return entry.getID();
    }

    public RegionCopyEntry getEntryByName(String name){
        BasicDBObject query=new BasicDBObject("nm",name);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_TRAIN_REGIONS_COPY,query);
        if(null!=dbObject){
            return new RegionCopyEntry((BasicDBObject)dbObject);
        }else{
            return null;
        }
    }

}
