package com.db.groupchatrecord;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
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


    public int countGroupChatRecords(ObjectId userId,ObjectId groupId){
        BasicDBObject query = new BasicDBObject()
                .append("uid",userId)
                .append("gid",groupId)
                .append("ct",Constant.ONE)
                .append("ir",Constant.ZERO);
        return  count(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_GROUP_CHAT_RECORD,query);
    }


    public int countPersonalChatRecords(ObjectId userId,
                                        ObjectId receiveId){
        BasicDBObject query = new BasicDBObject();
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject("uid",userId).append("rid",receiveId));
        values.add(new BasicDBObject("uid",receiveId).append("rid",userId));
        query.append(Constant.MONGO_OR,values);
        query.append("ct",Constant.TWO);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_GROUP_CHAT_RECORD,query);
    }

    public List<GroupChatRecordEntry> getPersonalChatRecords(ObjectId userId,
                                                             ObjectId receiveId,
                                                             int page,
                                                             int pageSize){
        List<GroupChatRecordEntry> entries = new ArrayList<GroupChatRecordEntry>();
        BasicDBObject query = new BasicDBObject();
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject("uid",userId).append("rid",receiveId));
        values.add(new BasicDBObject("uid",receiveId).append("rid",userId));
        query.put(Constant.MONGO_OR,values);
        query.append("ct",Constant.TWO);

        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(),Constant.COLLECTION_JXM_GROUP_CHAT_RECORD,
                query,Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new GroupChatRecordEntry(dbObject));
            }
        }
        return entries;
    }


    public List<GroupChatRecordEntry> getGroupChatRecords(ObjectId userId,ObjectId groupId,
                                                     int page,int pageSize){
        List<GroupChatRecordEntry>  entries = new ArrayList<GroupChatRecordEntry>();
        BasicDBObject query = new BasicDBObject()
                .append("uid",userId)
                .append("gid",groupId)
                .append("ct",Constant.ONE)
                .append("ir",Constant.ZERO);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(),Constant.COLLECTION_JXM_GROUP_CHAT_RECORD,
                query,Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new GroupChatRecordEntry(dbObject));
            }
        }
        return entries;
    }



}
