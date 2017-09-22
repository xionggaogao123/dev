package com.db.operation;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.operation.AppRecordEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/8/25.
 */
public class AppRecordDao extends BaseDao {
    //添加
    public String addEntry(AppRecordEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_RECORD, entry.getBaseEntry());
        return entry.getID().toString();
    }

    /**\
     * 单查询
     * @param id
     * @return
     */
    public AppRecordEntry getEntry(ObjectId id) {
        BasicDBObject query =new BasicDBObject(Constant.ID, id);
        query.append("isr", Constant.ZERO);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_RECORD, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new AppRecordEntry((BasicDBObject)dbo);
        }
        return null;
    }
    //查询已签到列表
    public AppRecordEntry getEntryListByParentId3(ObjectId userId,long dateTime) {
        BasicDBObject query = new BasicDBObject();
        query.append("isr",Constant.ZERO);
        query.append("uid",userId);
        query.append("dtm",dateTime);
        DBObject obj =
                findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_RECORD, query, Constant.FIELDS);
        if (obj != null) {
            return new AppRecordEntry((BasicDBObject) obj);
        }
        return null;
    }

    //签到
    public void updateEntry(ObjectId id){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue = new BasicDBObject()
                .append(Constant.MONGO_SET,
                        new BasicDBObject("isl", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_RECORD, query, updateValue);
    }
    //签到
    public void updateEntry2(ObjectId id,long current){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue = new BasicDBObject()
                .append(Constant.MONGO_SET,
                        new BasicDBObject("ctm", current));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_RECORD, query, updateValue);
    }

    //查询已签到列表
    public List<AppRecordEntry> getEntryListByParentId(ObjectId parentId,int type) {
        BasicDBObject query = new BasicDBObject()
                .append("pid",parentId)
                .append("isl",type)
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_RECORD,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<AppRecordEntry> entryList = new ArrayList<AppRecordEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppRecordEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
    //查询已签到列表
    public List<AppRecordEntry> getEntryListByParentId2(List<ObjectId> userIds,long zero) {
        BasicDBObject query = new BasicDBObject()
                .append("uid", new BasicDBObject(Constant.MONGO_IN, userIds))
                .append("dtm",zero)
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_RECORD,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<AppRecordEntry> entryList = new ArrayList<AppRecordEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppRecordEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
}
