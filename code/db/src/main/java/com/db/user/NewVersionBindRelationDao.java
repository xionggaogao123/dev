package com.db.user;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.user.NewVersionBindRelationEntry;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/8/28.
 */
public class NewVersionBindRelationDao extends BaseDao{

    public ObjectId saveNewVersionBindEntry(NewVersionBindRelationEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_BIND_RELATION,entry.getBaseEntry());
        return entry.getID();
    }

    public void updatePersonalSignature(ObjectId bindId,String personalSignature){
        BasicDBObject query=new BasicDBObject(Constant.ID,bindId);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("pst",personalSignature));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_BIND_RELATION,query,updateValue);
    }

    public void supplementNewVersionInfo(ObjectId bindId,
                                      String provinceName,
                                      String regionName,
                                      String regionAreaName,
                                      String schoolName,
                                      int relation){
        BasicDBObject query=new BasicDBObject(Constant.ID,bindId);
        BasicDBObject updates = new BasicDBObject();
        if(StringUtils.isNotEmpty(provinceName)){
            updates.append("pn",provinceName);
        }
        if(StringUtils.isNotEmpty(regionName)){
            updates.append("rd",regionName);
        }
        if(StringUtils.isNotEmpty(regionAreaName)){
            updates.append("ra",regionAreaName);
        }
        if(relation!=-1){
            updates .append("rl", relation);
        }
        if(StringUtils.isNotEmpty(schoolName)){
            updates.append("sn",schoolName);
        }
        if(updates.size()>0) {
            BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, updates);
            update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_BIND_RELATION, query, updateValue);
        }
    }


    public NewVersionBindRelationEntry getBindRelationEntry(ObjectId mainUserId,ObjectId userId){
        BasicDBObject query = new BasicDBObject()
                .append("muid",mainUserId)
                .append("uid",userId);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_BIND_RELATION,query,Constant.FIELDS);
        if(null!=dbObject){
            return new NewVersionBindRelationEntry(dbObject);
        }else{
            return null;
        }
    }
    /**
     *
     * @param userId
     * @return
     */
    public NewVersionBindRelationEntry getEntryByUserId(ObjectId userId){
        BasicDBObject query = new BasicDBObject()
                .append("uid",userId);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_BIND_RELATION,query,Constant.FIELDS);
        if(null!=dbObject){
            return new NewVersionBindRelationEntry(dbObject);
        }else{
            return null;
        }
    }

    public NewVersionBindRelationEntry getBindEntry(ObjectId userId){
        BasicDBObject query = new BasicDBObject()
                .append("uid",userId)
                .append("ir",Constant.ZERO);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_BIND_RELATION,query,Constant.FIELDS);
        if(null!=dbObject){
            return new NewVersionBindRelationEntry(dbObject);
        }else{
            return null;
        }
    }

    public NewVersionBindRelationEntry getEntry(ObjectId bindId){
        BasicDBObject query = new BasicDBObject()
                .append(Constant.ID,bindId);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_BIND_RELATION,query,Constant.FIELDS);
        if(null!=dbObject){
            return new NewVersionBindRelationEntry(dbObject);
        }else{
            return null;
        }
    }

    //父id
    public List<NewVersionBindRelationEntry> getEntriesByMainUserId(
            ObjectId mainUserId
    ){
        List<NewVersionBindRelationEntry> entries
                =new ArrayList<NewVersionBindRelationEntry>();
        BasicDBObject query = new BasicDBObject()
                .append("muid",mainUserId)
                .append("ir",Constant.ZERO);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_BIND_RELATION,
                query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new NewVersionBindRelationEntry(dbObject));
            }
        }
        return entries;
    }

    public List<ObjectId> getIdsByMainUserId(
            ObjectId mainUserId
    ){
        List<ObjectId> entries
                =new ArrayList<ObjectId>();
        BasicDBObject query = new BasicDBObject()
                .append("muid",mainUserId)
                .append("ir",Constant.ZERO);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_BIND_RELATION,
                query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new NewVersionBindRelationEntry(dbObject).getUserId());
            }
        }
        return entries;
    }

    //子id
    public List<NewVersionBindRelationEntry> getEntriesByUserId(
            ObjectId userId
    ){
        List<NewVersionBindRelationEntry> entries
                =new ArrayList<NewVersionBindRelationEntry>();
        BasicDBObject query = new BasicDBObject()
                .append("uid",userId)
                .append("ir",Constant.ZERO);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_BIND_RELATION,
                query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new NewVersionBindRelationEntry(dbObject));
            }
        }
        return entries;
    }

    public void delNewVersionEntry(ObjectId parentId,ObjectId studentId){
        BasicDBObject query = new BasicDBObject();
        query.append("muid",parentId);
        query.append("uid",studentId);
        BasicDBObject updateValue = new BasicDBObject()
                .append(Constant.MONGO_SET,
                        new BasicDBObject("ir", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_BIND_RELATION, query, updateValue);
    }


    public List<NewVersionBindRelationEntry> getEntries(int page,int pageSize){
        List<NewVersionBindRelationEntry> entries
                =new ArrayList<NewVersionBindRelationEntry>();
        BasicDBObject query = new BasicDBObject()
                .append("ir",Constant.ZERO);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_BIND_RELATION,
                query,Constant.FIELDS,Constant.MONGO_SORTBY_ASC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new NewVersionBindRelationEntry(dbObject));
            }
        }
        return entries;
    }
}
