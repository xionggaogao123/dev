package com.db.integral;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.integral.IntegralRecordEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-04-16.
 */
public class IntegralRecordDao extends BaseDao {
    //添加
    public ObjectId addEntry(IntegralRecordEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEGRAL_RECORD, entry.getBaseEntry());
        return entry.getID();
    }

    //老师当日作业列表查询
    public List<IntegralRecordEntry> getEntryListByUserId(ObjectId userId,long dateTime) {
        BasicDBObject query = new BasicDBObject()
                .append("uid",userId)
                .append("dtm",dateTime)
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_INTEGRAL_RECORD,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<IntegralRecordEntry> entryList = new ArrayList<IntegralRecordEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new IntegralRecordEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

}
