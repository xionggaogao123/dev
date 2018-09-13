package com.db.business;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.business.ModuleTimeEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-06-07.
 */
public class ModuleTimeDao extends BaseDao {

    public void addEntry(ObjectId userId,int type,ObjectId communityId){
        ModuleTimeEntry entry = new ModuleTimeEntry(userId,type,communityId);
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_MODULE_TIME, entry.getBaseEntry());
        entry = null;
    }

    public List<ModuleTimeEntry> getEntryList(List<ObjectId> userIds,List<ObjectId> communityIds,long startTime,long endTime,List<Integer> integers) {
        communityIds.add(null);//兼容老数据
        BasicDBObject query = new BasicDBObject()
                .append("mty",new BasicDBObject(Constant.MONGO_IN,integers))
                .append("uid", new BasicDBObject(Constant.MONGO_IN, userIds))
                .append("cid", new BasicDBObject(Constant.MONGO_IN,communityIds))
                .append("isr", 0); // 未删除
        BasicDBList dblist =new BasicDBList();
        dblist.add(new BasicDBObject("ctm", new BasicDBObject(Constant.MONGO_GTE, startTime)));
        dblist.add(new BasicDBObject("ctm", new BasicDBObject(Constant.MONGO_LT, endTime)));
        query.append(Constant.MONGO_AND,dblist);
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_MODULE_TIME,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<ModuleTimeEntry> entryList = new ArrayList<ModuleTimeEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ModuleTimeEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    /**
     * getEntryList 方法 去掉时间范围
     * @param userIds
     * @param communityIds
     * @param integers
     * @return
     */
    public List<ModuleTimeEntry> getEntryListNotTimeRange(List<ObjectId> userIds,List<ObjectId> communityIds,List<Integer> integers) {
        communityIds.add(null);//兼容老数据
        BasicDBObject query = new BasicDBObject()
                .append("mty",new BasicDBObject(Constant.MONGO_IN,integers))
                .append("uid", new BasicDBObject(Constant.MONGO_IN, userIds))
                .append("cid", new BasicDBObject(Constant.MONGO_IN,communityIds))
                .append("isr", 0); // 未删除
//        BasicDBList dblist =new BasicDBList();
//        dblist.add(new BasicDBObject("ctm", new BasicDBObject(Constant.MONGO_GTE, startTime)));
//        dblist.add(new BasicDBObject("ctm", new BasicDBObject(Constant.MONGO_LT, endTime)));
//        query.append(Constant.MONGO_AND,dblist);
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_MODULE_TIME,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<ModuleTimeEntry> entryList = new ArrayList<ModuleTimeEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ModuleTimeEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

}
