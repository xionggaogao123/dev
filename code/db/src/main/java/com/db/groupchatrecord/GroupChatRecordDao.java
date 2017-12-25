package com.db.groupchatrecord;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.groupchatrecord.GroupChatRecordEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/12/25.
 */
public class GroupChatRecordDao extends BaseDao{

    public void saveGroupRecordEntry(GroupChatRecordEntry recordEntry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_GROUP_CHAT_RECORD,recordEntry.getBaseEntry());
    }


    public List<GroupChatRecordEntry> getChatRecords(ObjectId userId,ObjectId groupId,
                                                     String timeStr,
                                                     int page,int pageSize){
        List<GroupChatRecordEntry>  entries = new ArrayList<GroupChatRecordEntry>();
        BasicDBObject query = new BasicDBObject()
                .append("uid",userId)
                .append("sd",timeStr)
                .append("gid",groupId)
                .append("ir",Constant.ZERO);
        List<DBObject> dbObjectList =find(MongoFacroty.getAppDB(),Constant.COLLECTION_JXM_GROUP_CHAT_RECORD,
                query,Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new GroupChatRecordEntry(dbObject));
            }
        }
        return entries;
    }



}
