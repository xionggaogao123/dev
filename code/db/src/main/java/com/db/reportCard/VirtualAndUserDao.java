package com.db.reportCard;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.reportCard.VirtualAndUserEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-03-29.
 */
public class VirtualAndUserDao extends BaseDao {

    //添加
    public String addEntry(VirtualAndUserEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_VIRTUAL_CONNECT, entry.getBaseEntry());
        return entry.getID().toString() ;
    }

    //单查询
    public VirtualAndUserEntry getEntry(ObjectId virtualId,ObjectId communityId) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO) .append("cid", communityId) .append("vid", virtualId);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_VIRTUAL_CONNECT, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new VirtualAndUserEntry((BasicDBObject)dbo);
        }
        return null;
    }

    //单查询
    public VirtualAndUserEntry getUserEntry(ObjectId userId,ObjectId communityId) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO) .append("cid", communityId) .append("uid", userId);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_VIRTUAL_CONNECT, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new VirtualAndUserEntry((BasicDBObject)dbo);
        }
        return null;
    }

    //删除
    public void delEntry(ObjectId userId,ObjectId communityId){
        BasicDBObject query = new BasicDBObject("uid",userId);
        query.append("cid",communityId);
        BasicDBObject updateValue = new BasicDBObject()
                .append(Constant.MONGO_SET,
                        new BasicDBObject("isr", Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_VIRTUAL_CONNECT, query, updateValue);
    }

    //查找
    public List<ObjectId> getEntryListByCommunityId(List<ObjectId> objectIds) {
        BasicDBObject query = new BasicDBObject()
                .append("uid",new BasicDBObject(Constant.MONGO_IN,objectIds))
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_VIRTUAL_CONNECT,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<ObjectId> entryList = new ArrayList<ObjectId>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new VirtualAndUserEntry((BasicDBObject) obj).getVirtualId());
            }
        }
        return entryList;
    }

    //查找
    public List<ObjectId> getViEntryListByCommunityId(List<ObjectId> objectIds) {
        BasicDBObject query = new BasicDBObject()
                .append("vid",new BasicDBObject(Constant.MONGO_IN,objectIds))
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_VIRTUAL_CONNECT,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<ObjectId> entryList = new ArrayList<ObjectId>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new VirtualAndUserEntry((BasicDBObject) obj).getUserId());
            }
        }
        return entryList;
    }

    //查找
    public List<VirtualAndUserEntry> getUserIdList(List<ObjectId> objectIds) {
        BasicDBObject query = new BasicDBObject()
                .append("vid",new BasicDBObject(Constant.MONGO_IN,objectIds))
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_VIRTUAL_CONNECT,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<VirtualAndUserEntry> entryList = new ArrayList<VirtualAndUserEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new VirtualAndUserEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
}
