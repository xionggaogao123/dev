package com.db.user;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.user.RecordUserUnbindEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by scott on 2018/1/5.
 */
public class RecordUserUnbindDao extends BaseDao{

    public void saveEntry(RecordUserUnbindEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_RECORD_USER_UNBIND,entry.getBaseEntry());
    }


    public void removeEntry(ObjectId mainUserId,
                            ObjectId userId,
                            String userKey){
        BasicDBObject query = new BasicDBObject()
                .append("muid",mainUserId)
                .append("uid",userId)
                .append("uk",userKey);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_RECORD_USER_UNBIND,query);
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
