package com.db.controlphone;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.controlphone.ControlAppSchoolResultEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by James on 2018-09-27.
 */
public class ControlAppSchoolResultDao extends BaseDao {

    //添加
    public String addEntry(ControlAppSchoolResultEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_APP_SCHOOL_RESULT, entry.getBaseEntry());
        return entry.getID().toString() ;
    }
    //单查询
    public ControlAppSchoolResultEntry getEntry(int type,ObjectId communityId,ObjectId appId) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO) .append("cid", communityId) .append("aid", appId).append("typ",type);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_APP_SCHOOL_RESULT, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new ControlAppSchoolResultEntry((BasicDBObject)dbo);
        }
        return null;
    }

    //查找社区应用使用情况列表
    public Map<String,ControlAppSchoolResultEntry> getEntryListByCommunityId(List<ObjectId> appIds,ObjectId communityId) {
        BasicDBObject query = new BasicDBObject()
                .append("cid",communityId)
                .append("aid",new BasicDBObject(Constant.MONGO_IN,appIds))
                .append("isr", 0); // 未删除


        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_CONTROL_APP_SCHOOL_RESULT,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        Map<String,ControlAppSchoolResultEntry> map = new HashMap<String, ControlAppSchoolResultEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                ControlAppSchoolResultEntry controlAppSchoolResultEntry = new ControlAppSchoolResultEntry((BasicDBObject) obj);
                if(controlAppSchoolResultEntry.getType()==1){//管控内
                    map.put(controlAppSchoolResultEntry.getAppId().toString()+"*"+controlAppSchoolResultEntry.getType()+"*",controlAppSchoolResultEntry);
                }else{
                    if(controlAppSchoolResultEntry.getOutSchoolRule()==0){
                        map.put(controlAppSchoolResultEntry.getAppId().toString()+"*"+controlAppSchoolResultEntry.getType()+"*"+controlAppSchoolResultEntry.getDateTime(),controlAppSchoolResultEntry);
                    }else{
                        map.put(controlAppSchoolResultEntry.getAppId().toString()+"*"+controlAppSchoolResultEntry.getType()+"*"+controlAppSchoolResultEntry.getOutSchoolRule(),controlAppSchoolResultEntry);
                    }

                }

            }
        }
        return map;
    }
}
