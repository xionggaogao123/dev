package com.db.excellentCourses;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.excellentCourses.HourClassEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-04-26.
 */
public class HourClassDao extends BaseDao {

    /**
     * 批量保存
     *
     * @param list
     */
    public void addBatch(List<DBObject> list) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_HOUR_CLASS, list);
    }

    //添加课时
    public String addEntry(HourClassEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_HOUR_CLASS, entry.getBaseEntry());
        return entry.getID().toString();
    }


    //查询课时
    public List<HourClassEntry> getEntryList(ObjectId parentId){
        List<HourClassEntry> entryList=new ArrayList<HourClassEntry>();
        BasicDBObject query=new BasicDBObject().append("pid", parentId).append("isr", Constant.ZERO);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_HOUR_CLASS, query,
                Constant.FIELDS, new BasicDBObject("ord",Constant.ASC));
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new HourClassEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }


    //查询订单课时
    public List<HourClassEntry> getEntryList(List<ObjectId> ids){
        List<HourClassEntry> entryList=new ArrayList<HourClassEntry>();
        BasicDBObject query=new BasicDBObject().append(Constant.ID, ids).append("isr", Constant.ZERO);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_HOUR_CLASS, query,
                Constant.FIELDS, new BasicDBObject("ord", Constant.ASC));
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new HourClassEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
}
