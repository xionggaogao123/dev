package com.db.reportCard;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.reportCard.VirtualUserEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by scott on 2017/11/16.
 */
public class VirtualUserDao extends BaseDao{

    public void saveVirualEntry(VirtualUserEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_VIRTUAL_USER,entry.getBaseEntry());
    }

    public void saveEntries(List<VirtualUserEntry> entries){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_VIRTUAL_USER, MongoUtils.fetchDBObjectList(entries));
    }

    public void editVirtualUserItem(ObjectId itemId,
                                    String userName,
                                    String userNumber){
        BasicDBObject query=new BasicDBObject(Constant.ID,itemId);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject().append("unm",userName)
                        .append("un",userNumber));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_VIRTUAL_USER,query,updateValue);
    }


    public VirtualUserEntry findById(ObjectId itemId){
        BasicDBObject query=new BasicDBObject(Constant.ID,itemId);
        DBObject dbObject =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_VIRTUAL_USER,query,Constant.FIELDS);
        if(null!=dbObject){
            return new VirtualUserEntry(dbObject);
        }else{
            return null;
        }
    }

    public VirtualUserEntry findByNames(ObjectId communityId,String userName,String number){
        BasicDBObject query=new BasicDBObject();
        query.append("cid",communityId);
        query.append("unm",userName);
        query.append("un",number);
        query.append("ir",0);
        DBObject dbObject =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_VIRTUAL_USER,query,Constant.FIELDS);
        if(null!=dbObject){
            return new VirtualUserEntry(dbObject);
        }else{
            return null;
        }
    }
    
    public VirtualUserEntry findByNamesOnly(ObjectId communityId,String userName){
        BasicDBObject query=new BasicDBObject();
        query.append("cid",communityId);
        query.append("unm",userName);
        query.append("ir",0);
        DBObject dbObject =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_VIRTUAL_USER,query,Constant.FIELDS);
        if(null!=dbObject){
            return new VirtualUserEntry(dbObject);
        }else{
            return null;
        }
    }

    public void removeItemById(ObjectId itemId){
        BasicDBObject query=new BasicDBObject(Constant.ID,itemId);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("ir",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_VIRTUAL_USER,query,updateValue);
    }

    public void removeOldData(ObjectId communityId){
        BasicDBObject query=new BasicDBObject()
                .append("cid",communityId)
                .append("ir",Constant.ZERO);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("ir",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_VIRTUAL_USER,query,updateValue);
    }

    public VirtualUserEntry getVirtualUserByUserId(ObjectId userId){
        BasicDBObject query=new BasicDBObject()
                .append("uid",userId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_VIRTUAL_USER,query,Constant.FIELDS);
        if(null!=dbObject){
            return new VirtualUserEntry(dbObject);
        }else{
            return null;
        }
    }
    
    //获取实时名单
    public VirtualUserEntry getIrVirtualUserByUserId(ObjectId cid,ObjectId userId){
        BasicDBObject query=new BasicDBObject().append("cid", cid)
                .append("uid",userId).append("ir",Constant.ZERO);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_VIRTUAL_USER,query,Constant.FIELDS);
        if(null!=dbObject){
            return new VirtualUserEntry(dbObject);
        }else{
            return null;
        }
    }

    public Map<ObjectId,VirtualUserEntry> getVirtualUserMap(List<ObjectId> userIds){
        Map<ObjectId,VirtualUserEntry> virtualUserEntryMap = new HashMap<ObjectId, VirtualUserEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("uid",new BasicDBObject(Constant.MONGO_IN,userIds));
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_VIRTUAL_USER,
                query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                VirtualUserEntry entry=new VirtualUserEntry(dbObject);
                virtualUserEntryMap.put(entry.getUserId(),entry);
            }
        }
        return virtualUserEntryMap;
    }

    public List<VirtualUserEntry> getAllVirtualUsers(ObjectId communityId){
        List<VirtualUserEntry> entries=new ArrayList<VirtualUserEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("ir",Constant.ZERO)
                .append("cid",communityId);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_VIRTUAL_USER,
                query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new VirtualUserEntry(dbObject));
            }
        }
        return entries;
    }
    
    public List<VirtualUserEntry> getAllVirtualUsersNew(ObjectId communityId,String userName){
        List<VirtualUserEntry> entries=new ArrayList<VirtualUserEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("ir",Constant.ZERO)
                .append("cid",communityId)
                .append("unm", userName);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_VIRTUAL_USER,
                query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new VirtualUserEntry(dbObject));
            }
        }
        return entries;
    }

    public int countAllVirtualUsers(ObjectId communityId){
        BasicDBObject query=new BasicDBObject()
                .append("ir",Constant.ZERO)
                .append("cid",communityId);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_VIRTUAL_USER,
                query);
    }

    public List<VirtualUserEntry> getAllVirtualUsers(ObjectId communityId,int page,int pageSize){
        List<VirtualUserEntry> entries=new ArrayList<VirtualUserEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("ir",Constant.ZERO)
                .append("cid",communityId);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_VIRTUAL_USER,
                query,Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new VirtualUserEntry(dbObject));
            }
        }
        return entries;
    }
}
