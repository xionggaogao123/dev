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

    //签到
    public void updateEntry(ObjectId parentId){
        BasicDBObject query = new BasicDBObject();
        query.append("pid",parentId);
        BasicDBObject updateValue = new BasicDBObject()
                .append(Constant.MONGO_SET,
                        new BasicDBObject("isl", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_RECORD, query, updateValue);
    }

    //查询已签到列表
    public List<AppRecordEntry> getEntryListByParentId(ObjectId parentId,int type) {
        BasicDBObject query = new BasicDBObject()
                .append("pid",parentId)
                .append("typ",type)
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
