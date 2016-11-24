package com.db.overtime;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.overtime.OverTimeEntry;
import com.pojo.overtime.OverTimeModelEntry;
import com.sys.constants.Constant;
import com.sys.utils.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wang_xinxin on 2016/7/14.
 */
public class OverTimeDao extends BaseDao {

    /**
     *
     * @param overTimeEntry
     * @return
     */
    public ObjectId addJiaBanInfo(OverTimeEntry overTimeEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_OVERTIME_NAME,overTimeEntry.getBaseEntry());
        return overTimeEntry.getID();

    }

    /**
     *
     * @param stime
     * @param etime
     * @param users
     * @param userId
     * @return
     */
    public List<OverTimeEntry> selJiaBanList(long stime, long etime, List<ObjectId> users, ObjectId userId,int type,int index) {
        List<OverTimeEntry> retList = new ArrayList<OverTimeEntry>();
        BasicDBObject query =new BasicDBObject("ir",Constant.ZERO);
        if (users!=null && users.size()!=0) {
            query.append("juid",new BasicDBObject(Constant.MONGO_IN, users));
        }
        if (type==1) {
            query.append("apuid",userId);
        } else if (type==2) {
            if (userId!=null) {
                query.append("apuid",userId);
            }
            query.append("type",2);
        } else if (type==3) {
            query.append("atuid",userId);
            if (index!=0) {
                query.append("type",index);
            } else {
            query.append("type",new BasicDBObject(Constant.MONGO_GTE,1));
            }
        }
        BasicDBList dblist =new BasicDBList();
        if(stime>0){
            dblist.add(new BasicDBObject("date", new BasicDBObject(Constant.MONGO_GTE, stime)));
        }
        if(etime>0) {
            dblist.add(new BasicDBObject("date", new BasicDBObject(Constant.MONGO_LTE, etime)));
        }
        if(dblist.size()>0){
            query.append(Constant.MONGO_AND,dblist);
        }
        List<DBObject> list =new ArrayList<DBObject>();
        list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_OVERTIME_NAME, query, null, new BasicDBObject("date",-1));
        for(DBObject dbo:list)
        {
            retList.add(new OverTimeEntry((BasicDBObject)dbo));
        }
        return retList;
    }

    /**
     *
     * @param id
     */
    public void delJiaBanInfo(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("ir",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_OVERTIME_NAME, query, updateValue);
    }

    /**
     *
     * @param id
     */
    public void submitJiaBan(ObjectId id,int type) {
        BasicDBObject query = new BasicDBObject(Constant.ID,id).append("ir",Constant.ZERO);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("type",type));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_OVERTIME_NAME, query, updateValue);
    }

    /**
     *
     * @param id
     * @return
     */
    public OverTimeEntry selSingleOverTime(ObjectId id) {
        BasicDBObject query =new BasicDBObject(Constant.ID,id);
        DBObject dbo =  findOne(MongoFacroty.getAppDB(),Constant.COLLECTION_OVERTIME_NAME,query,null);
        if(null!=dbo)
        {
            return new OverTimeEntry((BasicDBObject)dbo);
        }
        return null;
    }

    /**
     *
     * @param overTimeId
     * @param jbUserId
     * @param strToLongTime
     * @param startTime
     * @param endTime
     * @param cause
     * @param shUserId
     */
    public void updateJiaBanInfo(ObjectId overTimeId, ObjectId jbUserId, Long strToLongTime, String startTime, String endTime, String cause, ObjectId shUserId) {
        BasicDBObject query = new BasicDBObject(Constant.ID,overTimeId).append("ir",Constant.ZERO);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("juid",jbUserId)
        .append("date",strToLongTime)
        .append("st",startTime)
        .append("et",endTime)
        .append("cause",cause)
        .append("atuid",shUserId));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_OVERTIME_NAME, query, updateValue);
    }

    /**
     *
     * @param overTimeModelEntry
     */
    public ObjectId saveModelInfo(OverTimeModelEntry overTimeModelEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_OVERTIME_MODEL_NAME,overTimeModelEntry.getBaseEntry());
        return overTimeModelEntry.getID();
    }

    /**
     *
     * @param userId
     * @return
     */
    public List<OverTimeModelEntry> selJiaBanModelList(ObjectId userId) {
        List<OverTimeModelEntry> retList = new ArrayList<OverTimeModelEntry>();
        BasicDBObject query =new BasicDBObject("apuid",userId).append("ir",Constant.ZERO);
        List<DBObject> list =new ArrayList<DBObject>();
        list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_OVERTIME_MODEL_NAME, query, null, Constant.MONGO_SORTBY_DESC);
        for(DBObject dbo:list)
        {
            retList.add(new OverTimeModelEntry((BasicDBObject)dbo));
        }
        return retList;
    }

    /**
     *
     * @param id
     */
    public void delOverTimeModel(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("ir",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_OVERTIME_MODEL_NAME, query, updateValue);
    }

    /**
     *
     * @param id
     * @param modelName
     */
    public void updateModelInfo(ObjectId id, String modelName) {
        BasicDBObject query = new BasicDBObject(Constant.ID,id).append("ir",Constant.ZERO);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("mn",modelName));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_OVERTIME_MODEL_NAME, query, updateValue);
    }

    /**
     *
     * @param id
     * @param salary
     */
    public void updateSalaryById(ObjectId id, double salary) {
        BasicDBObject query = new BasicDBObject(Constant.ID,id).append("ir",Constant.ZERO);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("pay",salary));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_OVERTIME_NAME, query, updateValue);
    }

    /**
     *
     * @param type
     * @param id
     */
    public void updateCheckTime(int type, ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID,id).append("ir",Constant.ZERO);
        BasicDBObject updateData = new BasicDBObject();
        if (type==1) {
            updateData.append("it",new Date().getTime());
        } else {
            updateData.append("ot",new Date().getTime());
        }
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,
                updateData);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_OVERTIME_NAME, query, updateValue);
    }

    /**
     *
     * @param id
     * @param log
     * @param basicDBObjects
     */
    public void addOverTimeLog(ObjectId id, String log, List<BasicDBObject> basicDBObjects) {
        BasicDBObject query = new BasicDBObject(Constant.ID,id).append("ir",Constant.ZERO);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("con",log).append("dcl", basicDBObjects));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_OVERTIME_NAME, query, updateValue);
    }

    /**
     *
     * @param id
     * @param type
     */
    public void updOverTimeType(ObjectId id, int type) {
        BasicDBObject query = new BasicDBObject(Constant.ID,id).append("ir",Constant.ZERO);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("type",type));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_OVERTIME_NAME, query, updateValue);
    }

    /**
     *
     * @param overTimeId
     * @param userId
     */
    public void updShUserById(ObjectId overTimeId,ObjectId userId) {
        BasicDBObject query = new BasicDBObject(Constant.ID,overTimeId).append("ir",Constant.ZERO);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("atuid",userId));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_OVERTIME_NAME, query, updateValue);
    }

    /**
     *
     * @param stime
     * @param etime
     * @param userId
     * @return
     */
    public List<OverTimeEntry> selMyJiaBanSalary(long stime, long etime, ObjectId userId) {
        List<OverTimeEntry> retList = new ArrayList<OverTimeEntry>();
        BasicDBObject query =new BasicDBObject("ir",Constant.ZERO).append("juid",userId).append("type",2);
        BasicDBList dblist =new BasicDBList();
        if(stime>0){
            dblist.add(new BasicDBObject("date", new BasicDBObject(Constant.MONGO_GTE, stime)));
        }
        if(etime>0) {
            dblist.add(new BasicDBObject("date", new BasicDBObject(Constant.MONGO_LTE, etime)));
        }
        if(dblist.size()>0){
            query.append(Constant.MONGO_AND,dblist);
        }
        List<DBObject> list =new ArrayList<DBObject>();
        list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_OVERTIME_NAME, query, null, Constant.MONGO_SORTBY_ASC);
        for(DBObject dbo:list)
        {
            retList.add(new OverTimeEntry((BasicDBObject)dbo));
        }
        return retList;
    }

    /**
     * 检查模板名是否重复
     * @param schoolId
     * @param modelName
     * @param userId
     * @return
     */
    public int checkModelName(ObjectId schoolId, String modelName, ObjectId userId,String modelId) {
        BasicDBObject query=new BasicDBObject("si", schoolId).append("mn",modelName).append("apuid",userId).append("ir", Constant.ZERO);
        if (!StringUtils.isEmpty(modelId)) {
            query.append(Constant.ID,new BasicDBObject(Constant.MONGO_NE,new ObjectId(modelId)));
        }
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_OVERTIME_MODEL_NAME, query);
    }

    /**
     * 检查是否重复发布
     * @param schoolId
     * @param userId
     * @param date
     * @return
     */
    public List<OverTimeEntry> selOverTimeByDate(ObjectId schoolId, ObjectId userId, long date,String overTimeId) {
        List<OverTimeEntry> retList = new ArrayList<OverTimeEntry>();
        BasicDBObject query =new BasicDBObject("ir",Constant.ZERO).append("juid",userId).append("si",schoolId).append("date",date);
        if (!StringUtils.isEmpty(overTimeId)) {
            query.append(Constant.ID,new BasicDBObject(Constant.MONGO_NE,new ObjectId(overTimeId)));
        }
        List<DBObject> list =new ArrayList<DBObject>();
        list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_OVERTIME_NAME, query, null, Constant.MONGO_SORTBY_ASC);
        for(DBObject dbo:list)
        {
            retList.add(new OverTimeEntry((BasicDBObject)dbo));
        }
        return retList;
    }
}
