package com.db.operation;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.operation.ParentChildConnectionEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-07-26.
 */
public class ParentChildConnectionDao extends BaseDao {
    /**
     * 保存
     * @param entry
     */
    public ObjectId saveAppNoticeEntry(ParentChildConnectionEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_PARENT_CHILD_CONNECTION,entry.getBaseEntry());
        return entry.getID();
    }

    public void updateEntry(ObjectId id,String name){
        BasicDBObject query = new BasicDBObject("uid",id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("una",name));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_PARENT_CHILD_CONNECTION, query,updateValue);
    }

    public void delEntry(ObjectId id){
        BasicDBObject query = new BasicDBObject("uid",id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isr",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_PARENT_CHILD_CONNECTION, query,updateValue);
    }

    public ParentChildConnectionEntry getEntryByById(ObjectId parentId,String name,ObjectId communityId) {
        BasicDBObject query = new BasicDBObject("pid",parentId);
        query.append("una",name);
        query.append("cid",communityId);
        query.append("isr",Constant.ZERO);
        DBObject obj =
                findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_PARENT_CHILD_CONNECTION, query, Constant.FIELDS);
        if (obj != null) {
            return new ParentChildConnectionEntry((BasicDBObject) obj);
        }
        return null;
    }

    public List<ParentChildConnectionEntry> getEntry(ObjectId parentId) {
        BasicDBObject query = new BasicDBObject();
        query.append("pid",parentId);
        query.append("isr",Constant.ZERO);
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_PARENT_CHILD_CONNECTION,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<ParentChildConnectionEntry> entryList = new ArrayList<ParentChildConnectionEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ParentChildConnectionEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    public List<ParentChildConnectionEntry> getEntryByList(ObjectId communityId) {
        BasicDBObject query = new BasicDBObject();
        query.append("cid",communityId);
        query.append("isr",Constant.ZERO);
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_PARENT_CHILD_CONNECTION,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<ParentChildConnectionEntry> entryList = new ArrayList<ParentChildConnectionEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ParentChildConnectionEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }


    public List<ParentChildConnectionEntry> getOneEntry(ObjectId parentId,ObjectId communityId) {
        BasicDBObject query = new BasicDBObject();
        query.append("pid",parentId);
        query.append("cid",communityId);
        query.append("isr",Constant.ZERO);
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_PARENT_CHILD_CONNECTION,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<ParentChildConnectionEntry> entryList = new ArrayList<ParentChildConnectionEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ParentChildConnectionEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }



}
