package com.db.operation;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.operation.AppRecordResultEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/11/10.
 */
public class AppRecordResultDao extends BaseDao {
    /**
     * 批量保存
     *
     * @param list
     */
    public void addBatch(List<DBObject> list) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_RECORD_RESULT, list);
    }

    public List<ObjectId> getEntryList(ObjectId userId,long dateTime) {
        BasicDBObject query = new BasicDBObject()
                .append("dtm",dateTime)
                .append("uid",userId)
                .append("isl",2)
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_RECORD_RESULT,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<ObjectId> entryList = new ArrayList<ObjectId>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppRecordResultEntry((BasicDBObject) obj).getParentId());
            }
        }
        return entryList;
    }

    public List<AppRecordResultEntry> getEntryListByParentId(ObjectId parentId) {
        BasicDBObject query = new BasicDBObject()
                .append("pid",parentId)
                .append("isl", 2)
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_RECORD_RESULT,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<AppRecordResultEntry> entryList = new ArrayList<AppRecordResultEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppRecordResultEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
}
