package com.db.backstage;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.backstage.UserLogResultEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

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
                new BasicDBObject().append("roleId", roleId),Constant.FIELDS);
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
                new BasicDBObject().append("roleId", roleId), Constant.FIELDS, new BasicDBObject(Constant.ID, Constant.DESC), (page - 1) * pageSize, pageSize);
        if (null != dbObjectList && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                entries.add(new UserLogResultEntry(dbObject));
            }
        }
        return entries;
    }
}
