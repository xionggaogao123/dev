package com.db.controlphone;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.controlphone.ControlAppSchoolEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by James on 2018-09-27.
 */
public class ControlAppSchoolDao extends BaseDao {

    //添加
    public String addEntry(ControlAppSchoolEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_APP_SCHOOL, entry.getBaseEntry());
        return entry.getID().toString() ;
    }
    //单查询
    public ControlAppSchoolEntry getEntry(ObjectId appId) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO) .append("aid", appId);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_APP_SCHOOL, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new ControlAppSchoolEntry((BasicDBObject)dbo);
        }
        return null;
    }
    //查找默认应用使用情况列表
    public Map<String,ControlAppSchoolEntry> getEntryList(List<ObjectId> appIds) {
        BasicDBObject query = new BasicDBObject()
                .append("aid", new BasicDBObject(Constant.MONGO_IN, appIds))
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_CONTROL_APP_SCHOOL,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        Map<String,ControlAppSchoolEntry> map = new HashMap<String, ControlAppSchoolEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                ControlAppSchoolEntry controlAppSchoolEntry = new ControlAppSchoolEntry((BasicDBObject) obj);
                map.put(controlAppSchoolEntry.getAppId().toString() + "*" + controlAppSchoolEntry.getType(), controlAppSchoolEntry);
            }
        }
        return map;
    }
}
