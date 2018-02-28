package com.db.jiaschool;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.jiaschool.SchoolAppEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018/2/6.
 */
public class SchoolAppDao extends BaseDao {

    //添加标签
    public ObjectId addEntry(SchoolAppEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_BOOK_APP, entry.getBaseEntry());
        return entry.getID();
    }

    public void updEntry(SchoolAppEntry e) {
        BasicDBObject query=new BasicDBObject(Constant.ID,e.getID());
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,e.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_BOOK_APP, query,updateValue);
    }

    //查询
    public SchoolAppEntry getEntryById(ObjectId schoolId) {
        BasicDBObject query = new BasicDBObject("sid",schoolId);
        query.append("isr",Constant.ZERO);
        DBObject obj =
                findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_BOOK_APP, query, Constant.FIELDS);
        if (obj != null) {
            return new SchoolAppEntry((BasicDBObject) obj);
        }
        return null;
    }

    public List<SchoolAppEntry> getList(List<ObjectId> objectIds) {
        BasicDBObject query = new BasicDBObject()
                .append("isr", 0); // 未删除
        query.append("sid",new BasicDBObject(Constant.MONGO_IN,objectIds));
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_SCHOOL_BOOK_APP,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<SchoolAppEntry> entryList = new ArrayList<SchoolAppEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new SchoolAppEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
}
