package com.db.fcommunity;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.fcommunity.LatestGroupDynamicEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/2/15.
 */
public class LatestGroupDynamicDao extends BaseDao {

    public void saveItem(LatestGroupDynamicEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FPRUM_LATEST_GROUP_DYNAMIC,entry.getBaseEntry());
    }

    public LatestGroupDynamicEntry getLatestInfo(ObjectId communityId){
        BasicDBObject query=new BasicDBObject().append("cid",communityId).append("ir",Constant.ZERO);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FPRUM_LATEST_GROUP_DYNAMIC,query,Constant.FIELDS);
        if(null!=dbObject){
            return new LatestGroupDynamicEntry(dbObject);
        }else{
            return null;
        }
    }

    public void pushRead(ObjectId id,ObjectId userId){
        BasicDBObject query=new BasicDBObject().append(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_PUSH,new BasicDBObject("rl",userId));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FPRUM_LATEST_GROUP_DYNAMIC,query,updateValue);
    }
}
