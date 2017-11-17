package com.db.controlphone;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.controlphone.ControlMapEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/11/7.
 */
public class ControlMapDao extends BaseDao {

    //添加
    public String addEntry(ControlMapEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_MAP, entry.getBaseEntry());
        return entry.getID().toString() ;
    }
    //获得最新数据
    public ControlMapEntry getEntryByParentId(ObjectId parentId,ObjectId sonId,long zero) {
        BasicDBObject query = new BasicDBObject()
                .append("pid",parentId)
                .append("uid", sonId)
                .append("dtm",new BasicDBObject(Constant.MONGO_GT,zero))
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_CONTROL_MAP,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);

        if (dbList != null && !dbList.isEmpty()) {
            return new ControlMapEntry((BasicDBObject) dbList.get(0));
        }
        return null;
    }

    public List<ControlMapEntry> getMapListEntry(ObjectId parentId,ObjectId sonId,long startTime,long endTime) {
        BasicDBObject query = new BasicDBObject()
                .append("pid",parentId)
                .append("uid", sonId)
                .append("dtm",new BasicDBObject(Constant.MONGO_GT,startTime))
                .append("dtm",new BasicDBObject(Constant.MONGO_LT,endTime))
                .append("isr", 0); // 未删除
        List<ControlMapEntry> entries = new ArrayList<ControlMapEntry>();
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_CONTROL_MAP,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);

        if (dbList != null && !dbList.isEmpty()) {
            for(DBObject dbObject : dbList){
                entries.add(new ControlMapEntry((BasicDBObject)dbObject));
            }
        }
        return entries;
    }

    public List<ControlMapEntry> getSimpleMapListEntry(ObjectId parentId,ObjectId sonId,long dateTime) {
        BasicDBObject query = new BasicDBObject()
                .append("pid", parentId)
                .append("uid", sonId)
                .append("dtm",new BasicDBObject(Constant.MONGO_GT,dateTime))
                .append("isr", 0); // 未删除
        List<ControlMapEntry> entries = new ArrayList<ControlMapEntry>();
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_CONTROL_MAP,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);

        if (dbList != null && !dbList.isEmpty()) {
            for(DBObject dbObject : dbList){
                entries.add(new ControlMapEntry((BasicDBObject)dbObject));
            }
        }
        return entries;
    }

}
