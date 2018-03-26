package com.db.controlphone;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.controlphone.ControlVersionEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018/3/22.
 */
public class ControlVersionDao extends BaseDao {

    public List<ControlVersionEntry> getCommunityVersionList(ObjectId communityId){
        BasicDBObject query = new BasicDBObject();
        query.append("cid",communityId).append("isr",Constant.ZERO);
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_CONTROL_VERSION,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<ControlVersionEntry> entryList = new ArrayList<ControlVersionEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ControlVersionEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    //个人单查询
    public ControlVersionEntry getEntry(ObjectId userId,int type) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO).append("uid", userId).append("typ",type);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_VERSION, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new ControlVersionEntry((BasicDBObject)dbo);
        }
        return null;
    }

    //社群单查询
    public ControlVersionEntry getEntry(ObjectId communityId,ObjectId userId,int type) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO).append("uid", userId).append("typ",type);
        if(communityId!=null){
            query.append("cid",communityId);
        }
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_VERSION, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new ControlVersionEntry((BasicDBObject)dbo);
        }
        return null;
    }

    //添加
    public String addEntry(ControlVersionEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_VERSION, entry.getBaseEntry());
        return entry.getID().toString() ;
    }
}
