package com.db.user;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.user.RecordUserUnbindEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * Created by scott on 2018/1/5.
 */
public class RecordUserUnbindDao extends BaseDao{

    public void saveEntry(RecordUserUnbindEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_RECORD_USER_UNBIND,entry.getBaseEntry());
    }


    public void saveEntries(List<RecordUserUnbindEntry> entries){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_RECORD_USER_UNBIND, MongoUtils.fetchDBObjectList(entries));
    }


    public void removeUnBindId(ObjectId id){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_RECORD_USER_UNBIND,query);
    }


    public List<RecordUserUnbindEntry> getEntriesByMainUserId(ObjectId mainUserId){
        List<RecordUserUnbindEntry> entries = new ArrayList<RecordUserUnbindEntry>();
        BasicDBObject query = new BasicDBObject()
                .append("muid",mainUserId);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_RECORD_USER_UNBIND,query,
                Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new RecordUserUnbindEntry(dbObject));
            }
        }
        return entries;
    }


    public void removeOldData(ObjectId mainUserId,ObjectId communityId){
        BasicDBObject query = new BasicDBObject()
                .append("cmId",communityId)
                .append("muid",mainUserId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_RECORD_USER_UNBIND,query);
    }


    public List<ObjectId> getUserIdsByCondition(ObjectId communityId,
                                                ObjectId mainUserId){
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        BasicDBObject query = new BasicDBObject()
                .append("muid",mainUserId)
                .append("cmId",communityId);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_RECORD_USER_UNBIND,query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                RecordUserUnbindEntry unbindEntry = new RecordUserUnbindEntry(dbObject);
                userIds.add(unbindEntry.getUserId());
            }
        }
        return userIds;
    }


    public void removeEntry(ObjectId communityId,
                            ObjectId mainUserId,
                            ObjectId userId,
                            String userKey){
        BasicDBObject query = new BasicDBObject()
                .append("muid",mainUserId)
                .append("cmId",communityId)
                .append("uid",userId)
                .append("uk",userKey);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_RECORD_USER_UNBIND,query);
    }

    public List<ObjectId> getAlreadyTransferUserIds(ObjectId mainUserId,
                                                    List<ObjectId> userIds){
        Set<ObjectId> set = new HashSet<ObjectId>();
        BasicDBObject query = new BasicDBObject()
                .append("muid",mainUserId)
                .append("uid",new BasicDBObject(Constant.MONGO_IN,userIds));
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_RECORD_USER_UNBIND,query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                RecordUserUnbindEntry unbindEntry = new RecordUserUnbindEntry(dbObject);
                set.add(unbindEntry.getUserId());
            }
        }
        return new ArrayList<ObjectId>(set);
    }



    public RecordUserUnbindEntry getEntry(String userKey){
        BasicDBObject query = new BasicDBObject()
                .append("uk",userKey);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_RECORD_USER_UNBIND,query,Constant.FIELDS);
        if(null!=dbObject){
            return new RecordUserUnbindEntry(dbObject);
        }else{
            return null;
        }
    }

    public Map<String,RecordUserUnbindEntry> getRecordUnbindMap(List<String> userKeys){
        Map<String,RecordUserUnbindEntry> recordUserUnbindEntryMap = new HashMap<String, RecordUserUnbindEntry>();
        BasicDBObject query = new BasicDBObject()
                .append("uk",new BasicDBObject(Constant.MONGO_IN,userKeys));
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_RECORD_USER_UNBIND,query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                RecordUserUnbindEntry entry = new RecordUserUnbindEntry(dbObject);
                recordUserUnbindEntryMap.put(entry.getUserKey(), entry);
            }
        }
        return recordUserUnbindEntryMap;
    }


    public RecordUserUnbindEntry getEntry(ObjectId mainUserId,
                                          ObjectId userId,
                                          String userKey){
        BasicDBObject query = new BasicDBObject()
                .append("muid",mainUserId)
                .append("uid",userId)
                .append("uk",userKey);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_RECORD_USER_UNBIND,query,Constant.FIELDS);
        if(null!=dbObject){
            return new RecordUserUnbindEntry(dbObject);
        }else{
            return null;
        }
    }
}
