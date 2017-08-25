package com.db.operation;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.operation.AppOperationEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/8/25.
 */
public class AppOperationDao extends BaseDao {
    //添加
    public String addEntry(AppOperationEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_OPERATION, entry.getBaseEntry());
        return entry.getID().toString();
    }

    //老师评论列表查询
    public List<AppOperationEntry> getEntryListByParentId(ObjectId parentId) {
        BasicDBObject query = new BasicDBObject()
                .append("pid",parentId)
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_OPERATION,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<AppOperationEntry> entryList = new ArrayList<AppOperationEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppOperationEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
    //家长评论列表查询
    public List<AppOperationEntry> getEntryListByUserId(ObjectId userId,ObjectId id) {
        BasicDBObject query = new BasicDBObject()
                .append("uid",userId)
                .append("pid",id)
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_OPERATION,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<AppOperationEntry> entryList = new ArrayList<AppOperationEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppOperationEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    /**
     * 查询作业所有回复的数量
     * @param
     * @return
     */
    public int getEntryCount(ObjectId parentId) {
        BasicDBObject query = new BasicDBObject()
                .append("pid",parentId)
                .append("isr", 0); // 未删除
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_OPERATION,
                        query);
        return count;
    }
}
