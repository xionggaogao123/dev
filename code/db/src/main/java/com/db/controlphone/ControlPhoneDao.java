package com.db.controlphone;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
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

    public ControlPhoneEntry getEntry(String phone) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO) .append("pho", phone).append("typ",1);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_PHONE, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new ControlPhoneEntry((BasicDBObject)dbo);
        }
        return null;
    }

    public ControlPhoneEntry getEntryById(ObjectId id) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO) .append(Constant.ID, id).append("typ",1);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_PHONE, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new ControlPhoneEntry((BasicDBObject)dbo);
        }
        return null;
    }
    public ControlPhoneEntry getEntry2(ObjectId id) {
        BasicDBObject query =new BasicDBObject(Constant.ID,id);
        query.append("isr", Constant.ZERO);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_PHONE, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new ControlPhoneEntry((BasicDBObject)dbo);
        }
        return null;
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
    public List<ControlPhoneEntry> getEntryListByparentIdAndUserId2(ObjectId userId) {
        BasicDBObject query = new BasicDBObject()
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

    public List<ControlPhoneEntry> getEntryListByparentIdAndUserId3(ObjectId parentId,ObjectId userId) {
        BasicDBObject query = new BasicDBObject()
                .append("uid",userId)
                .append("pid",parentId)
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
    //优化查询
    public List<ControlPhoneEntry> getSonAllList(ObjectId parentId,ObjectId userId) {
        BasicDBObject query = new BasicDBObject()
                .append("isr", 0); // 未删除
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject("uid",userId).append("pid",parentId));
        values.add(new BasicDBObject("typ",Constant.ONE));
        query.append(Constant.MONGO_OR,values);
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

    //修改
    public void updEntry(ControlPhoneEntry e) {
        BasicDBObject query=new BasicDBObject(Constant.ID,e.getID());
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,e.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_PHONE, query,updateValue);
    }

    public List<ControlPhoneEntry> getEntryListByType() {
        BasicDBObject query = new BasicDBObject()
                .append("typ", 1)
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_CONTROL_PHONE,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_ASC);
        List<ControlPhoneEntry> entryList = new ArrayList<ControlPhoneEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ControlPhoneEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
    public List<ControlPhoneEntry> getEntryListByUserId(ObjectId userId) {
        BasicDBObject query = new BasicDBObject()
                .append("uid", userId)
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
    public List<ControlPhoneEntry> getEntryListByUserId2(ObjectId userId,int type) {
        BasicDBObject query = new BasicDBObject()
                .append("uid",userId)
                .append("typ",type)
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

    /**
     * 符合搜索条件的对象个数
     * @return
     */
    public int getNumber(ObjectId userId) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("uid",userId);
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_CONTROL_PHONE,
                        query);
        return count;
    }

    /**
     * 符合搜索条件的对象个数
     * @return
     */
    public int getParentNumber(ObjectId parentId,ObjectId userId) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("uid",userId);
        query.append("pid",parentId);
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_CONTROL_PHONE,
                        query);
        return count;
    }
}
