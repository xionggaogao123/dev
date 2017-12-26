package com.db.groupchatrecord;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.groupchatrecord.RecordChatPersonalEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/12/26.
 */
public class RecordChatPersonalDao extends BaseDao{

    public void saveRecordEntry(RecordChatPersonalEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_RECORD_CHAT_PERSONAL,entry.getBaseEntry());
    }


    public int countChatEntries(ObjectId userId){
        BasicDBObject query = new BasicDBObject()
                .append("uid",userId);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_RECORD_CHAT_PERSONAL,query);
    }

    public List<RecordChatPersonalEntry> getChatEntries(ObjectId userId,
                                                        int page,
                                                        int pageSize){
        List<RecordChatPersonalEntry> entries = new ArrayList<RecordChatPersonalEntry>();
        BasicDBObject query = new BasicDBObject()
                .append("uid",userId);
        BasicDBObject order = new BasicDBObject("uti",-1);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_RECORD_CHAT_PERSONAL,query,
                Constant.FIELDS,order,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new RecordChatPersonalEntry(dbObject));
            }
        }
        return entries;
    }


    public RecordChatPersonalEntry getChatEntry(ObjectId userId,
                                                ObjectId receiveId){
        BasicDBObject query = new BasicDBObject()
                .append("uid",userId)
                .append("rid",receiveId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_RECORD_CHAT_PERSONAL,query,Constant.FIELDS);
        if(null!=dbObject){
            return new RecordChatPersonalEntry(dbObject);
        }else{
            return null;
        }
    }
}
