package com.db.controlphone;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.controlphone.ControlAppSchoolResultEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
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

    //删除作业
    public void delEntry(ObjectId id){
        BasicDBObject query = new BasicDBObject();
        query.append(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isr",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_APP_SCHOOL_RESULT, query,updateValue);
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
    //查找社区推荐应用列表
    public List<ControlAppSchoolResultEntry> getEntryList(ObjectId appId,ObjectId communityId,List<Integer> types) {
        BasicDBObject query = new BasicDBObject()
                .append("osr",new BasicDBObject(Constant.MONGO_IN,types))
                .append("cid", communityId)
                .append("aid", appId)
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_CONTROL_APP_SCHOOL_RESULT,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<ControlAppSchoolResultEntry> entryList = new ArrayList<ControlAppSchoolResultEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ControlAppSchoolResultEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    //单查询
    public ControlAppSchoolResultEntry getHomeEntry(int type,ObjectId communityId,ObjectId appId,int week) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO) .append("cid", communityId) .append("aid", appId).append("typ",type).append("osr",week);
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

    //查找社区应用使用情况列表
    public Map<String,ControlAppSchoolResultEntry> getAllEntryListByCommunityId(List<ObjectId> appIds,List<ObjectId> communityIds) {
        BasicDBObject query = new BasicDBObject()
                .append("cid",new BasicDBObject(Constant.MONGO_IN,communityIds))
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
                    ControlAppSchoolResultEntry controlAppSchoolResultEntry1=  map.get(controlAppSchoolResultEntry.getAppId().toString() + "*" + controlAppSchoolResultEntry.getType() + "*");
                    if(controlAppSchoolResultEntry1==null){
                        map.put(controlAppSchoolResultEntry.getAppId().toString()+"*"+controlAppSchoolResultEntry.getType()+"*",controlAppSchoolResultEntry);
                    }else{
                        if(controlAppSchoolResultEntry.getSaveTime()>controlAppSchoolResultEntry1.getSaveTime()){
                            map.put(controlAppSchoolResultEntry.getAppId().toString()+"*"+controlAppSchoolResultEntry.getType()+"*",controlAppSchoolResultEntry);
                        }
                    }

                }else{
                    if(controlAppSchoolResultEntry.getOutSchoolRule()==0){
                        ControlAppSchoolResultEntry controlAppSchoolResultEntry1=  map.get(controlAppSchoolResultEntry.getAppId().toString()+"*"+controlAppSchoolResultEntry.getType()+"*"+controlAppSchoolResultEntry.getDateTime());
                        if(controlAppSchoolResultEntry1==null){
                            map.put(controlAppSchoolResultEntry.getAppId().toString()+"*"+controlAppSchoolResultEntry.getType()+"*"+controlAppSchoolResultEntry.getDateTime(),controlAppSchoolResultEntry);
                        }else{
                            if(controlAppSchoolResultEntry.getSaveTime()>controlAppSchoolResultEntry1.getSaveTime()){
                                map.put(controlAppSchoolResultEntry.getAppId().toString()+"*"+controlAppSchoolResultEntry.getType()+"*"+controlAppSchoolResultEntry.getDateTime(),controlAppSchoolResultEntry);
                            }

                        }

                    }else{
                        ControlAppSchoolResultEntry controlAppSchoolResultEntry1=  map.get(controlAppSchoolResultEntry.getAppId().toString()+"*"+controlAppSchoolResultEntry.getType()+"*"+controlAppSchoolResultEntry.getOutSchoolRule());
                        if(controlAppSchoolResultEntry1==null){
                            map.put(controlAppSchoolResultEntry.getAppId().toString()+"*"+controlAppSchoolResultEntry.getType()+"*"+controlAppSchoolResultEntry.getOutSchoolRule(),controlAppSchoolResultEntry);
                        }else {
                            if (controlAppSchoolResultEntry.getSaveTime() > controlAppSchoolResultEntry1.getSaveTime()) {
                                map.put(controlAppSchoolResultEntry.getAppId().toString() + "*" + controlAppSchoolResultEntry.getType() + "*" + controlAppSchoolResultEntry.getOutSchoolRule(), controlAppSchoolResultEntry);
                            }
                        }

                    }

                }

            }
        }
        return map;
    }
}
