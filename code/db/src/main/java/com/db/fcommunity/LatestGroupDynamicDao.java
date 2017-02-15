package com.db.fcommunity;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.fcommunity.LatestGroupDynamicEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by scott on 2017/2/15.
 */
public class LatestGroupDynamicDao extends BaseDao {

    public void saveItem(LatestGroupDynamicEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FPRUM_LATEST_GROUP_DYNAMIC,entry.getBaseEntry());
    }

    public LatestGroupDynamicEntry getLatestInfo(ObjectId communityId){
        BasicDBObject query=new BasicDBObject().append("cid",communityId).append("ir",Constant.ZERO);
        List<DBObject> dbObject=find(MongoFacroty.getAppDB(), Constant.COLLECTION_FPRUM_LATEST_GROUP_DYNAMIC,query,Constant.FIELDS,Constant.MONGO_SORTBY_DESC,0,1);
        if(null!=dbObject&&!dbObject.isEmpty()){
            if(dbObject.size()>0) {
                return new LatestGroupDynamicEntry(dbObject.get(0));
            }else{
                return null;
            }
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
