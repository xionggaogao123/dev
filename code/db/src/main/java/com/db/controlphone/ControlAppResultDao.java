package com.db.controlphone;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.controlphone.ControlAppResultEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/11/9.
 */
public class ControlAppResultDao extends BaseDao {
    /**
     * 批量保存
     *
     * @param list
     */
    public void addBatch(List<DBObject> list) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_APP_RESULT, list);
    }

    /**
     * 批量保存缓存表
     *
     * @param list
     */
    public void addLinBatch(List<DBObject> list) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_APP_RESULT_CURRENT, list);
    }

    /**
     * 定时删除缓存表
     * @return
     */
    public void drop(){
        dropCollection(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_APP_RESULT_CURRENT);
    }



    //添加
    public String addEntry(ControlAppResultEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_APP_RESULT, entry.getBaseEntry());
        return entry.getID().toString() ;
    }
    //用户的所有课程列表
    public List<ObjectId> getIsNewObjectId(ObjectId userId,long startTime,long endTime) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("isn", Constant.ZERO);
        BasicDBList dblist =new BasicDBList();
        dblist.add(new BasicDBObject("dtm", new BasicDBObject(Constant.MONGO_GTE, startTime)));
        dblist.add(new BasicDBObject("dtm", new BasicDBObject(Constant.MONGO_LT, endTime)));
        query.append(Constant.MONGO_AND,dblist);
        query.append("uid", userId);
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_APP_RESULT, query, Constant.FIELDS);
        List<ObjectId> retList =new ArrayList<ObjectId>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new ControlAppResultEntry((BasicDBObject)dbo).getID());
            }
        }
        return retList;
    }

    public List<ControlAppResultEntry> getIsNewEntryList(ObjectId userId,long startTime,long endTime) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("isn", Constant.ZERO);
        BasicDBList dblist =new BasicDBList();
        dblist.add(new BasicDBObject("dtm", new BasicDBObject(Constant.MONGO_GTE, startTime)));
        dblist.add(new BasicDBObject("dtm", new BasicDBObject(Constant.MONGO_LT, endTime)));
        query.append(Constant.MONGO_AND,dblist);
        query.append("uid",userId);
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_APP_RESULT, query, Constant.FIELDS,new BasicDBObject("utm",Constant.DESC));
        List<ControlAppResultEntry> retList =new ArrayList<ControlAppResultEntry>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new ControlAppResultEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    public List<ControlAppResultEntry> getNewNewEntryList(ObjectId userId,ObjectId parentId,long dateTime) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("uid",userId);
        query.append("pid",parentId);
        query.append("dtm",dateTime);
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_APP_RESULT, query, Constant.FIELDS,new BasicDBObject("utm",Constant.DESC));
        List<ControlAppResultEntry> retList =new ArrayList<ControlAppResultEntry>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new ControlAppResultEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    public List<ControlAppResultEntry> getLinNewNewEntryList(ObjectId userId,ObjectId parentId,long dateTime) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("uid",userId);
        query.append("pid",parentId);
        query.append("dtm",dateTime);
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_APP_RESULT_CURRENT, query, Constant.FIELDS,new BasicDBObject("utm",Constant.DESC));
        List<ControlAppResultEntry> retList =new ArrayList<ControlAppResultEntry>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new ControlAppResultEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    public List<ControlAppResultEntry> getBlackIsNewEntryList(List<String> oids,ObjectId userId,long startTime,long endTime) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("isn", Constant.ZERO);
        BasicDBList dblist =new BasicDBList();
        dblist.add(new BasicDBObject("dtm", new BasicDBObject(Constant.MONGO_GTE, startTime)));
        dblist.add(new BasicDBObject("dtm", new BasicDBObject(Constant.MONGO_LT, endTime)));
        query.append(Constant.MONGO_AND, dblist);
        query.append("uid",userId);
        query.append("pnm",new BasicDBObject(Constant.MONGO_IN,oids));
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_APP_RESULT, query, Constant.FIELDS,new BasicDBObject("utm",Constant.DESC));
        List<ControlAppResultEntry> retList =new ArrayList<ControlAppResultEntry>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new ControlAppResultEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    //学生的实时使用时间
    public int getAllTime(ObjectId userId,long dateTime) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("isn", Constant.ZERO);
        query.append("dtm", dateTime);
        query.append("uid",userId);
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_APP_RESULT, query, Constant.FIELDS);
        int retList = 0;
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList += (new ControlAppResultEntry((BasicDBObject)dbo).getUseTime());
            }
        }
        return retList;
    }

    public long getUserAllTime(ObjectId userId,long startTime,long endTime) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("isn", Constant.ZERO);
        BasicDBList dblist =new BasicDBList();
        dblist.add(new BasicDBObject("dtm", new BasicDBObject(Constant.MONGO_GTE, startTime)));
        dblist.add(new BasicDBObject("dtm", new BasicDBObject(Constant.MONGO_LT, endTime)));
        query.append(Constant.MONGO_AND,dblist);
        query.append("uid",userId);
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_APP_RESULT, query, Constant.FIELDS);
        long retList = 0l;
        if(null!=dboList && !dboList.isEmpty())
        {
          /*  for(DBObject dbo:dboList)
            {
                retList = (new ControlAppResultEntry((BasicDBObject)dbo).getAddiction());
            }*/
            DBObject dbo = dboList.get(0);
            retList = new ControlAppResultEntry((BasicDBObject)dbo).getAddiction();
        }
        return retList;
    }

    /**
     * 修改
     */
    public void updEntry(List<ObjectId> ids) {
        BasicDBObject query = new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_IN,ids));
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isn",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_APP_RESULT, query,updateValue);
    }
    /**
     * 删除
     */
    public void delEntry(List<ObjectId> ids) {
        BasicDBObject query = new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_IN,ids));
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_APP_RESULT, query);
    }

    /**
     * 删除
     */
    public void delEntryList() {
        BasicDBObject query = new BasicDBObject("isn",Constant.ONE);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_APP_RESULT, query);
    }
}
