package com.db.jiaschool;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.jiaschool.SchoolFunctionEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by James on 2018-06-29.
 */
public class SchoolFunctionDao extends BaseDao {
    //添加使用
    public ObjectId addEntry(SchoolFunctionEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_FUNCTION, entry.getBaseEntry());
        return entry.getID();
    }

    //修改
    public void updateEntry(ObjectId id,int open){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("ope",open));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_FUNCTION, query,updateValue);
    }

    //查询
    public SchoolFunctionEntry getEntry(ObjectId id) {
        BasicDBObject query = new BasicDBObject();
        query.append("isr",Constant.ZERO);
        query.append(Constant.ID,id);
        DBObject obj =
                findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_FUNCTION, query, Constant.FIELDS);
        if (obj != null) {
            return new SchoolFunctionEntry((BasicDBObject) obj);
        }
        return null;
    }


    //查询
    public Map<ObjectId,Integer> getOneRoleList(List<ObjectId> schoolIds,int type) {
        BasicDBObject query = new BasicDBObject()
                .append("isr", 0); // 未删除
        query.append("sid",new BasicDBObject(Constant.MONGO_IN,schoolIds));
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_SCHOOL_FUNCTION,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
       Map<ObjectId,Integer> map = new HashMap<ObjectId, Integer>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                SchoolFunctionEntry schoolFunctionEntry = new SchoolFunctionEntry((BasicDBObject) obj);
                map.put(schoolFunctionEntry.getSchoolId(),schoolFunctionEntry.getOpen());
            }
        }
        return map;
    }
}
