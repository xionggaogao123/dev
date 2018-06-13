package com.db.jiaschool;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.jiaschool.SchoolPersionEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-06-11.
 *
 */
public class SchoolPersionDao extends BaseDao {
    //添加用户角色
    public ObjectId addEntry(SchoolPersionEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_PERSON, entry.getBaseEntry());
        return entry.getID();
    }

    //查询
    public SchoolPersionEntry getEntryById(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        query.append("isr",Constant.ZERO);
        DBObject obj =
                findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_PERSON, query, Constant.FIELDS);
        if (obj != null) {
            return new SchoolPersionEntry((BasicDBObject) obj);
        }
        return null;
    }

    //查询
    public SchoolPersionEntry getEntry(ObjectId userId,ObjectId schoolId) {
        BasicDBObject query = new BasicDBObject();
        query.append("isr",Constant.ZERO);
        query.append("uid",userId);
        query.append("sid",schoolId);
        DBObject obj =
                findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_PERSON, query, Constant.FIELDS);
        if (obj != null) {
            return new SchoolPersionEntry((BasicDBObject) obj);
        }
        return null;
    }

    //删除
    public void delEntry(ObjectId id){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isr",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_PERSON, query,updateValue);
    }
    //查询
    public List<SchoolPersionEntry> getOneRoleList(ObjectId userId) {
        BasicDBObject query = new BasicDBObject()
                .append("isr", 0); // 未删除
        query.append("uid",userId);
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_SCHOOL_PERSON,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<SchoolPersionEntry> entryList = new ArrayList<SchoolPersionEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new SchoolPersionEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }


    public List<SchoolPersionEntry> getAllMemberBySchoolId(ObjectId schoolId,int page,int pageSize) {
        BasicDBObject query = new BasicDBObject()
                .append("isr", 0); // 未删除
       query.append("sid",schoolId);
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_SCHOOL_PERSON,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC,(page - 1) * pageSize, pageSize);
        List<SchoolPersionEntry> entryList = new ArrayList<SchoolPersionEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new SchoolPersionEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    public int countAllMemberBySchoolId(ObjectId schoolId){
        BasicDBObject query = new BasicDBObject();
        query.append("isr",0);
        query.append("sid",schoolId);
        int count = count(MongoFacroty.getAppDB(),Constant.COLLECTION_SCHOOL_PERSON,query);
        return count;
    }
}
