package com.db.backstage;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.backstage.SchoolControlTimeEntry;
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

    public List<SchoolControlTimeEntry> getMoreSchoolControlSettingList(List<ObjectId> schoolIds) {
        BasicDBObject query = new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("schoolId",new BasicDBObject(Constant.MONGO_IN,schoolIds));

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

    /**
     * 校管控列表查询
     * 系统默认管控设置列表展示
     */
    public List<SchoolControlTimeEntry> getDefaultSchoolControlSettingList() {
        BasicDBObject query = new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("schoolId",null);

        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(),Constant.COLLECTION_SCHOOL_CONTROL_TIME,query);
        List<SchoolControlTimeEntry> entryList = new ArrayList<SchoolControlTimeEntry>();
        for (DBObject dbObject : dbObjectList){
            if (dbObject != null){
                entryList.add(new SchoolControlTimeEntry(dbObject));
            }
        }
        return entryList;
    }

    /**
     * 新增学校增加默认校管控时间
     * @param schoolControlBasicDBObjectList
     */
    public void saveNewSchoolAddControlTime(List<DBObject> schoolControlBasicDBObjectList) {
        save(MongoFacroty.getAppDB(),Constant.COLLECTION_SCHOOL_CONTROL_TIME,schoolControlBasicDBObjectList);
    }

    /**
     * 获取已经设置过校管控时间的学校Id集合
     * @return
     */
    public List<ObjectId> getSchoolControlIdList() {
        BasicDBObject query = new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("schoolId", new BasicDBObject(Constant.MONGO_NE,null));

        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(),Constant.COLLECTION_SCHOOL_CONTROL_TIME,query);
        List<ObjectId> schoolControlIdList = new ArrayList<ObjectId>();
        for (DBObject dbObject : dbObjectList){
            if (dbObject != null){
                if (new SchoolControlTimeEntry(dbObject).getSchoolId() != null && !schoolControlIdList.contains(new ObjectId(new SchoolControlTimeEntry(dbObject).getSchoolId()))) {
                    schoolControlIdList.add(new ObjectId(new SchoolControlTimeEntry(dbObject).getSchoolId()));
                }
            }
        }
        return schoolControlIdList;
    }
}
