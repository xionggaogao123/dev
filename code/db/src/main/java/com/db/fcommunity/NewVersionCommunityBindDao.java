package com.db.fcommunity;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.fcommunity.NewVersionCommunityBindEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.*;

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

    public void updateStudentNumberAndThirdNameNono(ObjectId communityId,
                                                ObjectId mainUserId,
                                                ObjectId userId,
                                                String thirdName){
        BasicDBObject query=new BasicDBObject()
                .append("cid",communityId)
                .append("muid",mainUserId)
                .append("uid",userId);
        BasicDBObject updateValue=new BasicDBObject()
                .append(Constant.MONGO_SET,new BasicDBObject("tn",thirdName));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_COMMUNITY_BIND,query,updateValue);
    }
    
    public void updateUserIdByBindId(ObjectId bindId,ObjectId userId){
            BasicDBObject query=new BasicDBObject()
                    .append(Constant.ID,bindId);
            BasicDBObject updateValue=new BasicDBObject()
                    .append(Constant.MONGO_SET,new BasicDBObject("uid",userId));
            update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_COMMUNITY_BIND,query,updateValue);
        }
    
    
    
    public void updateStudentNumberAndThirdName(ObjectId communityId,
                                                ObjectId mainUserId,
                                                ObjectId userId,
                                                String studentNumber,
                                                String thirdName){
        BasicDBObject query=new BasicDBObject()
                .append("cid",communityId)
                .append("muid",mainUserId)
                .append("uid",userId);
        BasicDBObject updateValue=new BasicDBObject()
                .append(Constant.MONGO_SET,new BasicDBObject("tn",thirdName).append("nm",studentNumber));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_COMMUNITY_BIND,query,updateValue);
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


    public int countAllStudentBindEntries(ObjectId userId){
        BasicDBObject query=new BasicDBObject()
                .append("uid",userId)
                .append("ir", Constant.ZERO);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_COMMUNITY_BIND,query);
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

    public List<ObjectId> getAllStudentBindEntries(ObjectId communityId,List<ObjectId> userIds){
        List<ObjectId> entries=new ArrayList<ObjectId>();
        BasicDBObject query=new BasicDBObject()
                .append("cid",communityId)
                .append("uid",new BasicDBObject(Constant.MONGO_IN,userIds))
                .append("ir", Constant.ZERO);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_COMMUNITY_BIND,query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new NewVersionCommunityBindEntry(dbObject).getMainUserId());
            }
        }
        return entries;
    }


    public int countStudentIdListByCommunityId(ObjectId communityId){
        BasicDBObject query=new BasicDBObject()
                .append("cid",communityId)
                .append("ir", Constant.ZERO);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_COMMUNITY_BIND,query);
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


    public List<ObjectId> getParentIdListByCommunityId(ObjectId communityId){
        List<ObjectId> entries=new ArrayList<ObjectId>();
        BasicDBObject query=new BasicDBObject()
                .append("cid",communityId)
                .append("ir", Constant.ZERO);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_COMMUNITY_BIND,query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new NewVersionCommunityBindEntry(dbObject).getMainUserId());
            }
        }
        return entries;
    }


    public List<NewVersionCommunityBindEntry>  getBindEntries(ObjectId communityId, ObjectId mainUserId){
        List<NewVersionCommunityBindEntry> entries = new ArrayList<NewVersionCommunityBindEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("ir", Constant.ZERO)
                .append("muid",mainUserId);
        if(null!=communityId){
            query.append("cid",communityId);
        }

        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_COMMUNITY_BIND,query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                NewVersionCommunityBindEntry entry=new NewVersionCommunityBindEntry(dbObject);
                entries.add(entry);
            }
        }
        return entries;
    }


    public Map<ObjectId,NewVersionCommunityBindEntry> getCommunityBindMap(ObjectId communityId, ObjectId mainUserId){
        Map<ObjectId,NewVersionCommunityBindEntry> map=new HashMap<ObjectId, NewVersionCommunityBindEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("ir", Constant.ZERO)
                .append("muid",mainUserId);
        if(null!=communityId){
            query.append("cid",communityId);
        }
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_COMMUNITY_BIND,query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                NewVersionCommunityBindEntry entry=new NewVersionCommunityBindEntry(dbObject);
                map.put(entry.getUserId(),entry);
            }
        }
        return map;
    }

    public Map<ObjectId,Set<ObjectId>>  getUserEntryMapByUserId(List<ObjectId> userIds){
        Map<ObjectId,Set<ObjectId>> result = new HashMap<ObjectId, Set<ObjectId>>();
        BasicDBObject query = new BasicDBObject()
                .append("uid",new BasicDBObject(Constant.MONGO_IN,userIds))
                .append("ir", Constant.ZERO);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_COMMUNITY_BIND,query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                NewVersionCommunityBindEntry entry=new NewVersionCommunityBindEntry(dbObject);
                ObjectId userId=entry.getUserId();
                ObjectId communityId=entry.getCommunityId();
                if(null!=result.get(userId)){
                    Set<ObjectId> communityIds = result.get(userId);
                    communityIds.add(communityId);
                    result.put(userId,communityIds);
                }else{
                    Set<ObjectId> communityIds = new HashSet<ObjectId>();
                    communityIds.add(communityId);
                    result.put(userId,communityIds);
                }
            }
        }
        return result;
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

    /**
     * 获取家长绑定的社区
     * @return
     */
    public List<NewVersionCommunityBindEntry> getPageList(int page,int pageSize){
        List<NewVersionCommunityBindEntry> entries=new ArrayList<NewVersionCommunityBindEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("ir", Constant.ZERO);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_COMMUNITY_BIND,query,
                Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1) * pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new NewVersionCommunityBindEntry(dbObject));
            }
        }
        return entries;
    }
    public void removeSaoNewVersionCommunity(ObjectId communityId,
                                          ObjectId mainUserId,ObjectId userId){
        BasicDBObject query=new BasicDBObject()
                .append("cid",communityId)
                .append("uid",userId)
                .append("muid",mainUserId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("ir",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_COMMUNITY_BIND,query,updateValue);
    }

    public void removeNewVersionCommunity(ObjectId communityId,
                                          ObjectId mainUserId){
        BasicDBObject query=new BasicDBObject()
                .append("cid",communityId)
                .append("muid",mainUserId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("ir",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_COMMUNITY_BIND,query,updateValue);
    }
    //批量删除
    public void removeNewVersionCommunityList(ObjectId communityId,
                                          List<ObjectId> mainUserIds){
        BasicDBObject query=new BasicDBObject()
                .append("cid",communityId)
                .append("muid",new BasicDBObject(Constant.MONGO_IN,mainUserIds));
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


    public NewVersionCommunityBindEntry getVirtualBindEntry(ObjectId communityId,
                                                            String thirdName,
                                                            String userNumber){
        BasicDBObject query = new BasicDBObject("cid",communityId)
                .append("tn",thirdName)
                .append("nm",userNumber)
                .append("ir",Constant.ZERO);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_COMMUNITY_BIND,
                query,Constant.FIELDS);
        if(null!=dbObject){
            return new NewVersionCommunityBindEntry(dbObject);
        }else{
            return null;
        }
    }
    
    public NewVersionCommunityBindEntry getEntryById(ObjectId id){
        BasicDBObject query = new BasicDBObject(Constant.ID,id)
                .append("ir",Constant.ZERO);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_COMMUNITY_BIND,
                query,Constant.FIELDS);
        if(null!=dbObject){
            return new NewVersionCommunityBindEntry(dbObject);
        }else{
            return null;
        }
    }


    public void updateCommunityBindStatus(ObjectId communityId,
                                          ObjectId userId){
        BasicDBObject query=new BasicDBObject()
                .append("cid",communityId)
                .append("ir", Constant.ZERO)
                .append("uid", userId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("ir",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_COMMUNITY_BIND,
                query,updateValue);
    }


    public NewVersionCommunityBindEntry getEntry(String thirdName,
                                                 ObjectId communityId,
                                                 ObjectId mainUserId){
        BasicDBObject query=new BasicDBObject()
                .append("cid",communityId)
                .append("muid", mainUserId)
                .append("tn",thirdName);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_COMMUNITY_BIND,
                query,Constant.FIELDS);
        if(null!=dbObject){
            return new NewVersionCommunityBindEntry(dbObject);
        }else{
            return null;
        }
    }

    public NewVersionCommunityBindEntry getEntry(ObjectId bindId){
        BasicDBObject query=new BasicDBObject()
                .append(Constant.ID, bindId);
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

    public List<ObjectId> getEntries(ObjectId mainUserId,
                                     ObjectId userId){
        Set<ObjectId> communityIds = new HashSet<ObjectId>();
        BasicDBObject query=new BasicDBObject()
                .append("muid", mainUserId)
                .append("ir", Constant.ZERO)
                .append("uid", userId);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_COMMUNITY_BIND,
                query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                NewVersionCommunityBindEntry entry = new NewVersionCommunityBindEntry(dbObject);
                communityIds.add(entry.getCommunityId());
            }
        }
        return new ArrayList<ObjectId>(communityIds);
    }
}
