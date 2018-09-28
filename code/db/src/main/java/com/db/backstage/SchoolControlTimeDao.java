package com.db.backstage;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.backstage.SchoolControlTimeEntry;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Auther: taotao.chan
 * @Date: 2018-9-27 15:19:00
 * @Description:
 */
public class SchoolControlTimeDao extends BaseDao {
    public String saveSchoolControlSetting(SchoolControlTimeEntry schoolControlEntry) {
        save(MongoFacroty.getAppDB(),Constant.COLLECTION_SCHOOL_CONTROL_TIME,schoolControlEntry.getBaseEntry());
        return schoolControlEntry.getID().toString();
    }

    public List<SchoolControlTimeEntry> getEachSchoolControlSettingList(ObjectId schoolId) {
        BasicDBObject query = new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("schoolId",schoolId);

        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(),Constant.COLLECTION_SCHOOL_CONTROL_TIME,query);
        List<SchoolControlTimeEntry> entryList = new ArrayList<SchoolControlTimeEntry>();
        for (DBObject dbObject : dbObjectList){
            if (dbObject != null){
                entryList.add(new SchoolControlTimeEntry(dbObject));
            }
        }
        return entryList;
    }

    public String delSchoolControlSetting(Map map) {
        BasicDBObject query = new BasicDBObject();
        query.append(Constant.ID, new ObjectId(map.get("id").toString()));

        BasicDBObject updateParam = new BasicDBObject();
        updateParam.append("isr",Constant.ONE);

        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, updateParam);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCHOOL_CONTROL_TIME,query,updateValue);
        return map.get("id").toString();
    }
}
