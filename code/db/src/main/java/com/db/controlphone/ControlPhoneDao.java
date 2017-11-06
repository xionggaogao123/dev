package com.db.controlphone;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.controlphone.ControlPhoneEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/11/3.
 */
public class ControlPhoneDao extends BaseDao {
    //添加
    public String addEntry(ControlPhoneEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_PHONE, entry.getBaseEntry());
        return entry.getID().toString() ;
    }
    //修改
    public void updateEntry(String name,String phone,ObjectId id){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue = new BasicDBObject()
                .append(Constant.MONGO_SET,
                        new BasicDBObject("nam", name).append("pho",phone));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_PHONE, query, updateValue);
    }
    //删除
    public void delEntry(ObjectId id){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue = new BasicDBObject()
                .append(Constant.MONGO_SET,
                        new BasicDBObject("isr", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_PHONE, query, updateValue);
    }
    public List<ControlPhoneEntry> getEntryListByparentIdAndUserId(ObjectId parentId,ObjectId userId) {
        BasicDBObject query = new BasicDBObject()
                .append("pid",parentId)
                .append("uid",userId)
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_CONTROL_PHONE,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<ControlPhoneEntry> entryList = new ArrayList<ControlPhoneEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ControlPhoneEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

}