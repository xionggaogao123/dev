package com.db.backstage;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.pojo.backstage.UserLogResultEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by scott on 2017/11/29.
 */
public class UserLogResultDao extends BaseDao {

    public void saveUserLogEntry(UserLogResultEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_LOG_RESULT, entry.getBaseEntry());
    }

    public void removeItemByUserId(ObjectId userId) {
        BasicDBObject query = new BasicDBObject()
                .append("uid", userId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_LOG_RESULT, query);
    }

    public List<UserLogResultEntry> getEntries(){
        List<UserLogResultEntry> entries = new ArrayList<UserLogResultEntry>();
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_LOG_RESULT,
                new BasicDBObject(),Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new UserLogResultEntry(dbObject));
            }
        }
        return entries;
    }


    public UserLogResultEntry getEntryById(ObjectId id) {
        BasicDBObject query = new BasicDBObject()
                .append(Constant.ID, id);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_LOG_RESULT,
                query, Constant.FIELDS);
        if (null != dbObject) {
            return new UserLogResultEntry(dbObject);
        } else {
            return null;
        }
    }

    public UserLogResultEntry getEntryByUserId(ObjectId userId) {
        BasicDBObject query = new BasicDBObject()
                .append("uid", userId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_LOG_RESULT,
                query, Constant.FIELDS);
        if (null != dbObject) {
            return new UserLogResultEntry(dbObject);
        } else {
            return null;
        }
    }

    /**
     * 按userId分组 返回Map<userId,List<userLogResultEntryRoleId>>
     * @param userIds
     * @return
     */
    public Map<ObjectId,List<ObjectId>> getEntriesGroupByUserId(List<ObjectId> userIds) {
        BasicDBObject query = new BasicDBObject()
                .append("uid", new BasicDBObject(Constant.MONGO_IN,userIds));
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_LOG_RESULT,
                query, Constant.FIELDS);
        List<UserLogResultEntry> userLogResultEntrys = new ArrayList<UserLogResultEntry>();
        for (DBObject dbo : dbObjects) {
            userLogResultEntrys.add(new UserLogResultEntry(dbo));
        }

        //开始封装返回的数据
        Map<ObjectId,List<ObjectId>> result = new HashMap<ObjectId, List<ObjectId>>();
        for (ObjectId userId : userIds){
            List<ObjectId> userLogResultEntryRoleIds = new ArrayList<ObjectId>();
            for (UserLogResultEntry entry : userLogResultEntrys){
                if (userId.equals(entry.getUserId())){
                    userLogResultEntryRoleIds.add(entry.getRoleId());
                }
            }
            result.put(userId,userLogResultEntryRoleIds);
        }

        return result;
    }

    public List<UserLogResultEntry> getLogsByUserId(ObjectId userId) {
        List<UserLogResultEntry> entries = new ArrayList<UserLogResultEntry>();
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_LOG_RESULT,
                new BasicDBObject().append("uid", userId),Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new UserLogResultEntry(dbObject));
            }
        }
        return entries;
    }

    public List<UserLogResultEntry> getEntryByRoleId(ObjectId roleId) {
        List<UserLogResultEntry> entries = new ArrayList<UserLogResultEntry>();
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_LOG_RESULT,
                new BasicDBObject().append("roleId", roleId).append("ir",Constant.ZERO),Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new UserLogResultEntry(dbObject));
            }
        }
        return entries;
    }

    public List<UserLogResultEntry> getEntryByRoleIdList(List<ObjectId> settingEntryIdList) {
        List<UserLogResultEntry> entries = new ArrayList<UserLogResultEntry>();
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_LOG_RESULT,
                new BasicDBObject().append("roleId", new BasicDBObject(Constant.MONGO_IN,settingEntryIdList)).append("ir",Constant.ZERO),Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new UserLogResultEntry(dbObject));
            }
        }
        return entries;
    }

    public List<UserLogResultEntry> getEntryPageByRoleId(ObjectId roleId, int page, int pageSize) {
        List<UserLogResultEntry> entries = new ArrayList<UserLogResultEntry>();
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_LOG_RESULT,
                new BasicDBObject().append("roleId", roleId).append("ir",Constant.ZERO), Constant.FIELDS, new BasicDBObject(Constant.ID, Constant.DESC), (page - 1) * pageSize, pageSize);
        if (null != dbObjectList && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                entries.add(new UserLogResultEntry(dbObject));
            }
        }
        return entries;
    }

    public List<UserLogResultEntry> getEntryPageByRoleIdList(List<ObjectId> settingEntryIdList, int page, int pageSize) {
        List<UserLogResultEntry> entries = new ArrayList<UserLogResultEntry>();
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_LOG_RESULT,
                new BasicDBObject().append("roleId", new BasicDBObject(Constant.MONGO_IN,settingEntryIdList)).append("ir",Constant.ZERO), Constant.FIELDS, new BasicDBObject(Constant.ID, Constant.DESC), (page - 1) * pageSize, pageSize);
        if (null != dbObjectList && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                entries.add(new UserLogResultEntry(dbObject));
            }
        }
        return entries;
    }

    public String delAdminJurisdiction(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject updateParam = new BasicDBObject();
        updateParam.append("ir",Constant.ONE);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, updateParam);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_LOG_RESULT,query,updateValue);
        return id.toString();
    }
}
