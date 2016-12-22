package com.db.parentChild;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.parentChild.ParentChildActivityEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

import java.util.List;

/**
 * Created by admin on 2016/12/22.
 */
public class ParentChildActivityDao extends BaseDao{

    public void saveOrUpdate(ParentChildActivityEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_PARENT_CHILD_ACTIVITY,entry.getBaseEntry());
    }

    public void batchAddData(List<ParentChildActivityEntry> entries){
        List<DBObject> list= MongoUtils.fetchDBObjectList(entries);
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_PARENT_CHILD_ACTIVITY,list);
    }
    public ParentChildActivityEntry getEntryByCondition(String activityName,String activityTime){

        BasicDBObject query=new BasicDBObject()
                .append("acn",activityName)
                .append("act",activityTime);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(),Constant.COLLECTION_PARENT_CHILD_ACTIVITY,query);
        if(null!=dbObject){
            return new ParentChildActivityEntry((BasicDBObject)dbObject);
        }else{
            return null;
        }
    }
}
