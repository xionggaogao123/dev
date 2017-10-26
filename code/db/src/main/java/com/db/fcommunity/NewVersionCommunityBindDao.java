package com.db.fcommunity;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.fcommunity.NewVersionCommunityBindEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by scott on 2017/9/13.
 */
public class NewVersionCommunityBindDao extends BaseDao{

    public void saveEntry(NewVersionCommunityBindEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_COMMUNITY_BIND,entry.getBaseEntry());
    }

    public void saveEntries(List<NewVersionCommunityBindEntry> entryList){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_COMMUNITY_BIND, MongoUtils.fetchDBObjectList(entryList));
    }

    public void updateThirdName(ObjectId communityId,
                             ObjectId mainUserId,
                             ObjectId userId,
                             String thirdName
    ){
        BasicDBObject query=new BasicDBObject()
                .append("cid",communityId)
                .append("muid",mainUserId)
                .append("uid",userId);
        BasicDBObject updateValue=new BasicDBObject()
                .append(Constant.MONGO_SET,new BasicDBObject("tn",thirdName));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_COMMUNITY_BIND,query,updateValue);
    }

    public void updateNumber(ObjectId communityId,
                             ObjectId mainUserId,
                             ObjectId userId,
                             String studentNumber){
        BasicDBObject query=new BasicDBObject()
                .append("cid",communityId)
                .append("muid",mainUserId)
                .append("uid",userId);
        BasicDBObject updateValue=new BasicDBObject()
                .append(Constant.MONGO_SET,new BasicDBObject("nm",studentNumber));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_COMMUNITY_BIND,query,updateValue);
    }

    public void updateEntryStatus(ObjectId id){
        BasicDBObject query=new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject( "ir", Constant.ZERO));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_COMMUNITY_BIND,query,updateValue);
    }

    public List<NewVersionCommunityBindEntry> getAllStudentBindEntries(ObjectId userId){
        List<NewVersionCommunityBindEntry> entries=new ArrayList<NewVersionCommunityBindEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("uid",userId)
                .append("ir", Constant.ZERO);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_COMMUNITY_BIND,query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new NewVersionCommunityBindEntry(dbObject));
            }
        }
        return entries;
    }
    public List<NewVersionCommunityBindEntry> getStudentIdListByCommunityId(ObjectId communityId){
        List<NewVersionCommunityBindEntry> entries=new ArrayList<NewVersionCommunityBindEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("cid",communityId)
                .append("ir", Constant.ZERO);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_COMMUNITY_BIND,query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new NewVersionCommunityBindEntry(dbObject));
            }
        }
        return entries;
    }
    public List<ObjectId> getStudentListByCommunityId(ObjectId communityId){
        List<ObjectId> entries=new ArrayList<ObjectId>();
        BasicDBObject query=new BasicDBObject()
                .append("cid",communityId)
                .append("ir", Constant.ZERO);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_COMMUNITY_BIND,query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new NewVersionCommunityBindEntry(dbObject).getUserId());
            }
        }
        return entries;
    }


    public Map<ObjectId,NewVersionCommunityBindEntry> getCommunityBindMap(ObjectId communityId, ObjectId mainUserId){
        Map<ObjectId,NewVersionCommunityBindEntry> map=new HashMap<ObjectId, NewVersionCommunityBindEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("cid",communityId)
                .append("ir", Constant.ZERO)
                .append("muid",mainUserId);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_COMMUNITY_BIND,query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                NewVersionCommunityBindEntry entry=new NewVersionCommunityBindEntry(dbObject);
                map.put(entry.getUserId(),entry);
            }
        }
        return map;
    }


    public Map<ObjectId,NewVersionCommunityBindEntry> getUserEntryMapByCondition(
         ObjectId communityId,List<ObjectId> userIds
    ){
        Map<ObjectId,NewVersionCommunityBindEntry>
                bindUserMap=new HashMap<ObjectId, NewVersionCommunityBindEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("cid",communityId)
                .append("uid",new BasicDBObject(Constant.MONGO_IN,userIds))
                .append("ir", Constant.ZERO);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_COMMUNITY_BIND,query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                NewVersionCommunityBindEntry
                        entry=new NewVersionCommunityBindEntry(dbObject);
                bindUserMap.put(entry.getUserId(),entry);
            }
        }
        return bindUserMap;
    }

    /**
     * 获取家长绑定的社区
     * @return
     */
    public List<NewVersionCommunityBindEntry> getEntriesByMainUserId(ObjectId mainUserId){
        List<NewVersionCommunityBindEntry> entries=new ArrayList<NewVersionCommunityBindEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("ir", Constant.ZERO)
                .append("muid",mainUserId);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_COMMUNITY_BIND,query,
                Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new NewVersionCommunityBindEntry(dbObject));
            }
        }
        return entries;
    }


    public void removeNewVersionCommunity(ObjectId communityId,
                                          ObjectId mainUserId){
        BasicDBObject query=new BasicDBObject()
                .append("cid",communityId)
                .append("muid",mainUserId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("ir",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_COMMUNITY_BIND,query,updateValue);
    }


    public void removeNewVersionCommunityBindRelation(ObjectId mainUserId,
                                                      ObjectId userId){
        BasicDBObject query = new BasicDBObject()
                .append("muid",mainUserId)
                .append("uid",userId);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("ir", Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_COMMUNITY_BIND,query,updateValue);
    }

    public NewVersionCommunityBindEntry getEntry(ObjectId communityId,
                                                 ObjectId userId){
        BasicDBObject query=new BasicDBObject()
                .append("cid",communityId)
                .append("uid",userId);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_COMMUNITY_BIND,
                query,Constant.FIELDS);
        if(null!=dbObject){
            return new NewVersionCommunityBindEntry(dbObject);
        }else{
            return null;
        }
    }

    public NewVersionCommunityBindEntry getEntry(ObjectId communityId,
                                                 ObjectId mainUserId,
                                                 ObjectId userId){
        BasicDBObject query=new BasicDBObject()
                .append("cid",communityId)
                .append("muid", mainUserId)
                .append("uid", userId);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_COMMUNITY_BIND,
                query,Constant.FIELDS);
        if(null!=dbObject){
            return new NewVersionCommunityBindEntry(dbObject);
        }else{
            return null;
        }
    }
}
