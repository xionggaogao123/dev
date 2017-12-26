package com.db.groupchatrecord;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.groupchatrecord.RecordTotalChatEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by scott on 2017/12/26.
 */
public class RecordTotalChatDao extends BaseDao{

    public void saveEntry(RecordTotalChatEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_RECORD_TOTAL_CHAT,entry.getBaseEntry());
    }

    public Map<ObjectId,RecordTotalChatEntry> getChatMapByIds(List<ObjectId> userIds){
        Map<ObjectId,RecordTotalChatEntry> result = new HashMap<ObjectId, RecordTotalChatEntry>();
        BasicDBObject query =new BasicDBObject()
                .append("uid",new BasicDBObject(Constant.MONGO_IN,userIds));
        List<DBObject> dbObjectList =find(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_RECORD_TOTAL_CHAT,query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                RecordTotalChatEntry chatEntry = new RecordTotalChatEntry(dbObject);
                result.put(chatEntry.getUserId(),chatEntry);
            }
        }
        return result;
    }

    public void updateEntry(ObjectId userId){
        BasicDBObject query =new BasicDBObject()
                .append("uid",userId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_INC,new BasicDBObject("cc",1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_RECORD_TOTAL_CHAT,query,updateValue);
    }

    public RecordTotalChatEntry getEntryByUserId(ObjectId userId){
        BasicDBObject query =new BasicDBObject()
                .append("uid",userId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_RECORD_TOTAL_CHAT,query,Constant.FIELDS);
        if(null!=dbObject){
            return new RecordTotalChatEntry(dbObject);
        }else {
            return null;
        }
    }
}
