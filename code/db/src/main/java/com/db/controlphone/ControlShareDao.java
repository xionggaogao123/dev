package com.db.controlphone;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.controlphone.ControlShareEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-09-17.
 */
public class ControlShareDao extends BaseDao {
    public ControlShareEntry getEntry(ObjectId shareId) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO).append("hid",shareId);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_SHARE, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new ControlShareEntry((BasicDBObject)dbo);
        }
        return null;
    }

    //添加
    public String addEntry(ControlShareEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_SHARE, entry.getBaseEntry());
        return entry.getID().toString() ;
    }

    //修改
    public void deleteEntry(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue = new BasicDBObject()
                .append(Constant.MONGO_SET,
                        new BasicDBObject("isr", Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_SHARE, query, updateValue);
    }

    //查找
    public List<ControlShareEntry> getEntryList(ObjectId userId,ObjectId sonId) {
        BasicDBObject query = new BasicDBObject()
                .append("uid",userId)
                .append("sid",sonId)
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_CONTROL_SHARE,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<ControlShareEntry> entryList = new ArrayList<ControlShareEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ControlShareEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
}
