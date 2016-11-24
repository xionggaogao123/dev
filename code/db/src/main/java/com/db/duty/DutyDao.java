package com.db.duty;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.duty.*;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wang_xinxin on 2016/6/28.
 */
public class DutyDao extends BaseDao {


    /**
     * 获取值班设定
     * @param schoolId
     * @return
     */
    public DutySetEntry selDutySetInfo(ObjectId schoolId,int year,int week) {
        BasicDBObject query =new BasicDBObject("si",schoolId).append("year",year).append("week",week).append("ir", Constant.ZERO);
        DBObject dbo =  findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTYSET_NAME,query,null);
        if(null!=dbo)
        {
            return new DutySetEntry((BasicDBObject)dbo);
        }
        return null;
    }
    /**
     * 获取值班设定
     * @param schoolId
     * @return
     */
    public List<DutySetEntry> selDutySetInfo(ObjectId schoolId) {
        List<DutySetEntry> retList = new ArrayList<DutySetEntry>();
        BasicDBObject query =new BasicDBObject("si",schoolId).append("ir", Constant.ZERO);
        List<DBObject> list =new ArrayList<DBObject>();
        list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTYSET_NAME, query, null);
        for(DBObject dbo:list)
        {
            retList.add(new DutySetEntry((BasicDBObject)dbo));
        }
        return retList;
    }

    /**
     *
     * @param dutySetEntry
     */
    public ObjectId addDutySetInfo(DutySetEntry dutySetEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTYSET_NAME,dutySetEntry.getBaseEntry());
        return dutySetEntry.getID();
    }

    /**
     *
     * @param schoolId
     * @param type
     * @param num
     */
    public void updateDutySetTime(ObjectId schoolId, int type, int num,int year,int week) {
        BasicDBObject query = new BasicDBObject("si",schoolId).append("year",year).append("week",week).append("ir", Constant.ZERO);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("type",type).append("num",num));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTYSET_NAME, query, updateValue);
    }

    /**
     *  @param schoolId
     * @param userId
     */
    public void addDutyUser(ObjectId schoolId, ObjectId userId,int year,int week) {
        BasicDBObject query = new BasicDBObject("si",schoolId).append("year",year).append("week",week).append("ir", Constant.ZERO);
        DBObject updateValue =new BasicDBObject(Constant.MONGO_PUSH,new BasicDBObject("uids",userId));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTYSET_NAME, query, updateValue);
    }

    /**
     *
     * @param schoolId
     * @param userId
     */
    public void missDutyUser(ObjectId schoolId, ObjectId userId,int year,int week) {
        BasicDBObject query = new BasicDBObject("si",schoolId).append("year",year).append("week",week).append("ir", Constant.ZERO);
        DBObject updateValue =new BasicDBObject(Constant.MONGO_PULL,new BasicDBObject("uids",userId));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTYSET_NAME, query, updateValue);
    }

    /**
     *
     * @param dutyId
     * @return
     */
    public List<DutyTimeEntry> selDutyTimeInfo(ObjectId dutyId) {
        List<DutyTimeEntry> retList = new ArrayList<DutyTimeEntry>();
        BasicDBObject query =new BasicDBObject("dtid",dutyId).append("ir",Constant.ZERO);
        List<DBObject> list =new ArrayList<DBObject>();
        list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTYSETTIME_NAME, query, null, Constant.MONGO_SORTBY_ASC);
        for(DBObject dbo:list)
        {
            retList.add(new DutyTimeEntry((BasicDBObject)dbo));
        }
        return retList;
    }
    /**
     *
     * @param dutyId
     * @return
     */
    public List<DutyTimeEntry> selDutyTimeLogInfo(ObjectId dutyId) {
        List<DutyTimeEntry> retList = new ArrayList<DutyTimeEntry>();
        BasicDBObject query =new BasicDBObject("dtid",dutyId);
        List<DBObject> list =new ArrayList<DBObject>();
        list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTYSETTIME_NAME, query, null, Constant.MONGO_SORTBY_ASC);
        for(DBObject dbo:list)
        {
            retList.add(new DutyTimeEntry((BasicDBObject)dbo));
        }
        return retList;
    }

    /**
     *
     * @param dutyTimeEntry
     * @return
     */
    public ObjectId addDutyTimeInfo(DutyTimeEntry dutyTimeEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTYSETTIME_NAME,dutyTimeEntry.getBaseEntry());
        return dutyTimeEntry.getID();

    }

    /**
     *
     * @param dutyTimeId
     * @param timeDesc
     * @param startTime
     * @param endTime
     */
    public void updateDutyTimeInfo(String dutyTimeId, String timeDesc, String startTime, String endTime) {
        BasicDBObject query = new BasicDBObject(Constant.ID,new ObjectId(dutyTimeId)).append("ir",Constant.ZERO);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("tdesc",timeDesc).append("st",startTime).append("et",endTime));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTYSETTIME_NAME, query, updateValue);
    }

    /**
     *
     * @param dutyTimeId
     */
    public void delDutyTimeInfo(String dutyTimeId) {
        BasicDBObject query = new BasicDBObject(Constant.ID,new ObjectId(dutyTimeId));
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("ir",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTYSETTIME_NAME, query, updateValue);
    }

    /**
     *
     * @param dutyId
     * @return
     */
    public List<DutyProjectEntry> selDutyProjectInfo(ObjectId dutyId,int index) {
        List<DutyProjectEntry> retList = new ArrayList<DutyProjectEntry>();
        BasicDBObject query =new BasicDBObject("dtid",dutyId).append("ir", Constant.ZERO);
        if (index!=2) {
            query.append("idx",index);
        }
        List<DBObject> list =new ArrayList<DBObject>();
        list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTYSETPROJECT_NAME, query, null, Constant.MONGO_SORTBY_DESC);
        for(DBObject dbo:list)
        {
            retList.add(new DutyProjectEntry((BasicDBObject)dbo));
        }
        return retList;
    }

    /**
     *
     * @param dutyId
     * @return
     */
    public List<DutyProjectEntry> selDutyProjectLogInfo(ObjectId dutyId,int index) {
        List<DutyProjectEntry> retList = new ArrayList<DutyProjectEntry>();
        BasicDBObject query =new BasicDBObject("dtid",dutyId);
        if (index!=2) {
            query.append("idx",index);
        }
        List<DBObject> list =new ArrayList<DBObject>();
        list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTYSETPROJECT_NAME, query, null, Constant.MONGO_SORTBY_DESC);
        for(DBObject dbo:list)
        {
            retList.add(new DutyProjectEntry((BasicDBObject)dbo));
        }
        return retList;
    }

    /**
     *
     * @param dutyProjectEntry
     */
    public ObjectId addDutyProject(DutyProjectEntry dutyProjectEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTYSETPROJECT_NAME,dutyProjectEntry.getBaseEntry());
        return dutyProjectEntry.getID();
    }

    /**
     *
     * @param dutyProjectId
     * @param orgDutyProjectId
     * @param content
     */
    public void updateDutyProject(String dutyProjectId, String orgDutyProjectId, String content) {
        BasicDBObject query = new BasicDBObject(Constant.ID,new ObjectId(dutyProjectId));
        BasicDBObject data = new BasicDBObject("con",content);
        if (StringUtils.isEmpty(orgDutyProjectId)) {
            data.append("index",1);
        } else {
            data.append("orgid",orgDutyProjectId);
            data.append("index",2);
        }
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,data);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTYSETPROJECT_NAME, query, updateValue);
    }

    /**
     *
     * @param dutyProjectId
     */
    public void delDutyProject(String dutyProjectId) {
        BasicDBObject query = new BasicDBObject(Constant.ID,new ObjectId(dutyProjectId));
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("ir",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTYSETPROJECT_NAME, query, updateValue);
    }

    /**
     *
     * @param schoolId
     */
    public List<DutyEntry> selDutyInfo(ObjectId schoolId,int index,int year) {
        List<DutyEntry> retList = new ArrayList<DutyEntry>();
        BasicDBObject query =new BasicDBObject("si",schoolId).append("idx",index).append("year",year).append("ir", Constant.ZERO);
        List<DBObject> list =new ArrayList<DBObject>();
        list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTY_NAME, query, null, Constant.MONGO_SORTBY_ASC);
        for(DBObject dbo:list)
        {
            retList.add(new DutyEntry((BasicDBObject)dbo));
        }
        return retList;
    }

    /**
     *
     * @param schoolId
     * @return
     */
    public int selDutyCount(ObjectId schoolId,int year,int week) {
        BasicDBObject query =new BasicDBObject("si",schoolId).append("year",year).append("idx", week).append("ir", Constant.ZERO);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTY_NAME, query);
    }

    /**
     *
     * @param dutyEntry
     * @return
     */
    public ObjectId addDutyInfo(DutyEntry dutyEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTY_NAME,dutyEntry.getBaseEntry());
        return dutyEntry.getID();
    }

    /**
     *
     * @param dutyModelEntry
     * @return
     */
    public ObjectId addModel(DutyModelEntry dutyModelEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTY_MODEL_NAME,dutyModelEntry.getBaseEntry());
        return dutyModelEntry.getID();
    }

    /**
     *
     * @param id
     * @return
     */
    public DutyModelEntry selDutyModel(ObjectId id) {
        BasicDBObject query =new BasicDBObject(Constant.ID,id);
        DBObject dbo =  findOne(MongoFacroty.getAppDB(),Constant.COLLECTION_DUTY_MODEL_NAME,query,null);
        if(null!=dbo)
        {
            return new DutyModelEntry((BasicDBObject)dbo);
        }
        return null;
    }

    /**
     *
     * @param id
     */
    public void updateDutyInfo(ObjectId id, ObjectId dutyTimeId,ObjectId dutyProjectId) {
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("pid",dutyProjectId)
//                .append("uids",duty.getUserIds())
                .append("dtid", dutyTimeId));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTY_NAME, query, updateValue);
    }

    /**
     *
     * @param modelId
     * @param modelName
     */
    public void updateDutyModel(ObjectId modelId, String modelName) {
        BasicDBObject query = new BasicDBObject(Constant.ID,modelId);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("mnm",modelName));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTY_MODEL_NAME, query, updateValue);
    }

    /**
     *
     * @param modelId
     */
    public void delDutyModel(ObjectId modelId) {
        BasicDBObject query = new BasicDBObject(Constant.ID,modelId);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("ir",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTY_MODEL_NAME, query, updateValue);
    }

    /**
     *
     * @param schoolId
     * @return
     */
    public List<DutyModelEntry> selDutyModelInfo(ObjectId schoolId) {
        List<DutyModelEntry> retList = new ArrayList<DutyModelEntry>();
        BasicDBObject query =new BasicDBObject("si",schoolId).append("ir", Constant.ZERO);
        List<DBObject> list =new ArrayList<DBObject>();
        list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTY_MODEL_NAME, query, null, Constant.MONGO_SORTBY_DESC);
        for(DBObject dbo:list)
        {
            retList.add(new DutyModelEntry((BasicDBObject)dbo));
        }
        return retList;
    }

    /**
     *
     * @param id
     * @param index
     * @return
     */
    public List<DutyProjectEntry> selDutyProjectCount(String id, int index) {
        List<DutyProjectEntry> retList = new ArrayList<DutyProjectEntry>();
        BasicDBObject query =new BasicDBObject("orgid",id).append("idx",index).append("ir", Constant.ZERO);
        List<DBObject> list =new ArrayList<DBObject>();
        list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTYSETPROJECT_NAME, query, null, Constant.MONGO_SORTBY_DESC);
        for(DBObject dbo:list)
        {
            retList.add(new DutyProjectEntry((BasicDBObject)dbo));
        }
        return retList;
    }

    /**
     *
     * @param projectId
     * @return
     */
    public DutyProjectEntry selDutyProjectById(ObjectId projectId) {
        List<DutyProjectEntry> retList = new ArrayList<DutyProjectEntry>();
        BasicDBObject query =new BasicDBObject(Constant.ID,projectId).append("ir", Constant.ZERO);
        DBObject dbo=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTYSETPROJECT_NAME, query,null);
        if(null!=dbo)
        {
            return new DutyProjectEntry((BasicDBObject)dbo);
        }
        return null;
    }

    /**
     *
     * @param dutyId
     * @return
     */
    public List<DutyUserEntry> selDutyUserList(ObjectId dutyId) {
        List<DutyUserEntry> retList = new ArrayList<DutyUserEntry>();
        BasicDBObject query =new BasicDBObject("dtid",dutyId).append("ir", Constant.ZERO);
        List<DBObject> list =new ArrayList<DBObject>();
        list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTY_USER_NAME, query, null, Constant.MONGO_SORTBY_DESC);
        for(DBObject dbo:list)
        {
            retList.add(new DutyUserEntry((BasicDBObject)dbo));
        }
        return retList;
    }

    /**
     *
     * @param schoolId
     * @param index
     * @param num
     * @return
     */
    public DutyEntry selSingleDuty(ObjectId schoolId, int index, String num,int year) {
        BasicDBObject query =new BasicDBObject("si",schoolId).append("idx",index).append("year",year).append("num", num).append("ir", Constant.ZERO);
        DBObject dbo =  findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTY_NAME, query, null);
        if(null!=dbo)
        {
            return new DutyEntry((BasicDBObject)dbo);
        }
        return null;
    }

    /**
     *
     * @param dutyId
     * @return
     */
    public DutyEntry selDutyInfoById(ObjectId dutyId) {
        BasicDBObject query =new BasicDBObject(Constant.ID,dutyId).append("ir", Constant.ZERO);
        DBObject dbo =  findOne(MongoFacroty.getAppDB(),Constant.COLLECTION_DUTY_NAME,query,null);
        if(null!=dbo)
        {
            return new DutyEntry((BasicDBObject)dbo);
        }
        return null;
    }

    /**
     *
     * @param dutyTimeId
     * @return
     */
    public DutyTimeEntry selDutyTimeById(ObjectId dutyTimeId) {
        BasicDBObject query =new BasicDBObject(Constant.ID,dutyTimeId);
        DBObject dbo =  findOne(MongoFacroty.getAppDB(),Constant.COLLECTION_DUTYSETTIME_NAME,query,null);
        if(null!=dbo)
        {
            return new DutyTimeEntry((BasicDBObject)dbo);
        }
        return null;
    }

    /**
     *
     * @param orgId
     * @return
     */
    public List<DutyProjectEntry> selProjectByOrgId(String orgId) {
        List<DutyProjectEntry> retList = new ArrayList<DutyProjectEntry>();
        BasicDBObject query =new BasicDBObject("orgid",orgId).append("ir", Constant.ZERO);
        List<DBObject> list =new ArrayList<DBObject>();
        list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTYSETPROJECT_NAME, query, null, Constant.MONGO_SORTBY_DESC);
        for(DBObject dbo:list)
        {
            retList.add(new DutyProjectEntry((BasicDBObject)dbo));
        }
        return retList;
    }

    /**
     *
     * @param id
     * @param dutyProject
     */
    public void updateDutyProject(ObjectId id, String dutyProject) {
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("pid",new ObjectId(dutyProject)));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTY_NAME, query, updateValue);
    }

    /**
     *
     * @param dutyUserEntry
     */
    public ObjectId addDutyUserEntry(DutyUserEntry dutyUserEntry) {

        save(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTY_USER_NAME,dutyUserEntry.getBaseEntry());
        return dutyUserEntry.getID();
    }

    /**
     *
     * @param dutyId
     */
    public void delDutyUserEntry(ObjectId dutyId) {
        BasicDBObject query = new BasicDBObject("dtid",dutyId);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("ir",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTY_USER_NAME, query, updateValue);
    }

    /**
     *
     * @param dutyIds
     * @param userId
     * @return
     */
    public List<DutyUserEntry> selDutyUserByUserId(List<ObjectId> dutyIds, ObjectId userId) {
        List<DutyUserEntry> retList = new ArrayList<DutyUserEntry>();
        BasicDBObject query =new BasicDBObject("dtid",new BasicDBObject(Constant.MONGO_IN,dutyIds)).append("ui",userId).append("ir", Constant.ZERO);
        List<DBObject> list =new ArrayList<DBObject>();
        list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTY_USER_NAME, query, null, Constant.MONGO_SORTBY_ASC);
        for(DBObject dbo:list)
        {
            retList.add(new DutyUserEntry((BasicDBObject)dbo));
        }
        return retList;
    }

    /**
     *
     * @param dutyId
     * @param userId
     */
    public DutyShiftEntry selDutyShiftDetail(ObjectId dutyId, ObjectId userId) {
        BasicDBObject query =new BasicDBObject("dtid",dutyId).append("ui",userId).append("ir", Constant.ZERO);
        DBObject dbo =  findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTY_SHIFT_NAME, query, null);
        if(null!=dbo)
        {
            return new DutyShiftEntry((BasicDBObject)dbo);
        }
        return null;
    }

    /**
     *
     * @param dutyShiftEntry
     * @return
     */
    public ObjectId addDutyShiftInfo(DutyShiftEntry dutyShiftEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTY_SHIFT_NAME,dutyShiftEntry.getBaseEntry());
        return dutyShiftEntry.getID();
    }

    /**
     *
     * @param id
     * @param cause
     */
    public void updateDutyShiftInfo(ObjectId id, String cause) {
        BasicDBObject query = new BasicDBObject(Constant.ID,id).append("ir",Constant.ZERO);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("cause",cause).append("type",0));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTY_SHIFT_NAME, query, updateValue);
    }

    /**
     *
     * @param type
     * @param dutyId
     * @param userId
     */
    public void updateDutyUser(int type, String dutyId, ObjectId userId,String ip) {
        BasicDBObject query = new BasicDBObject("dtid",new ObjectId(dutyId)).append("ui",userId);
        BasicDBObject queryUpdate = new BasicDBObject();
        if (type==1) {
            queryUpdate.append("type",1).append("it",new Date().getTime()).append("ip",ip);
        }else if (type==2){
            queryUpdate.append("type",2).append("ot", new Date().getTime());
        }
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,queryUpdate);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTY_USER_NAME, query, updateValue);
    }

    /**
     *
     * @param stime
     * @param etime
     * @param dutyTimeId
     * @param schoolId
     * @return
     */
    public List<DutyEntry> selDutyByDate(long stime, long etime, String dutyTimeId, ObjectId schoolId) {
        List<DutyEntry> retList = new ArrayList<DutyEntry>();
        BasicDBObject query =new BasicDBObject("si",schoolId).append("ir", Constant.ZERO);
        if (!StringUtils.isEmpty(dutyTimeId)) {
            query.append("dtid", new ObjectId(dutyTimeId));
        }
        BasicDBList dblist =new BasicDBList();
        if(stime>0){
            dblist.add(new BasicDBObject("dt", new BasicDBObject(Constant.MONGO_GTE, stime)));
        }
        if(etime>0) {
            dblist.add(new BasicDBObject("dt", new BasicDBObject(Constant.MONGO_LTE, etime)));
        }
        if(dblist.size()>0){
            query.append(Constant.MONGO_AND,dblist);
        }
        List<DBObject> list =new ArrayList<DBObject>();
        list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTY_NAME, query, null, Constant.MONGO_SORTBY_ASC);
        for(DBObject dbo:list)
        {
            retList.add(new DutyEntry((BasicDBObject)dbo));
        }
        return retList;
    }

    /**
     *
     * @param dutyIds
     * @param users
     * @return
     */
    public List<DutyShiftEntry> selDutyShiftByUserIds(List<ObjectId> dutyIds, List<ObjectId> users) {
        List<DutyShiftEntry> retList = new ArrayList<DutyShiftEntry>();
        BasicDBObject query =new BasicDBObject("ir",Constant.ZERO);
        if (dutyIds!=null && dutyIds.size()!=0) {
            query.append("dtid", new BasicDBObject(Constant.MONGO_IN, dutyIds));
        }
        if (users!=null && users.size()!=0) {
            query.append("ui",new BasicDBObject(Constant.MONGO_IN, users));
        }
        List<DBObject> list =new ArrayList<DBObject>();
        list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTY_SHIFT_NAME, query, null, Constant.MONGO_SORTBY_ASC);
        for(DBObject dbo:list)
        {
            retList.add(new DutyShiftEntry((BasicDBObject)dbo));
        }
        return retList;
    }

    /**
     *
     * @param dutyShiftId
     * @param type
     */
    public void isTongGuo(String dutyShiftId, int type) {
        BasicDBObject query = new BasicDBObject(Constant.ID,new ObjectId(dutyShiftId)).append("ir",Constant.ZERO);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("type",type));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTY_SHIFT_NAME, query, updateValue);
    }

    /**
     *
     * @param stime
     * @param etime
     * @param dutyProjectId
     * @param schoolId
     * @return
     */
    public List<DutyEntry> selDutyByProjectId(long stime, long etime, String dutyProjectId, ObjectId schoolId) {
        List<DutyEntry> retList = new ArrayList<DutyEntry>();
        BasicDBObject query =new BasicDBObject("si",schoolId).append("ir", Constant.ZERO);
        if (!StringUtils.isEmpty(dutyProjectId)) {
            query.append("pid",new ObjectId(dutyProjectId));
        }
        BasicDBList dblist =new BasicDBList();
        if(stime>0){
            dblist.add(new BasicDBObject("dt", new BasicDBObject(Constant.MONGO_GTE, stime)));
        }
        if(etime>0) {
            dblist.add(new BasicDBObject("dt", new BasicDBObject(Constant.MONGO_LTE, etime)));
        }
        if(dblist.size()>0){
            query.append(Constant.MONGO_AND,dblist);
        }
        List<DBObject> list =new ArrayList<DBObject>();
        list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTY_NAME, query, null, Constant.MONGO_SORTBY_ASC);
        for(DBObject dbo:list)
        {
            retList.add(new DutyEntry((BasicDBObject)dbo));
        }
        return retList;
    }

    /**
     * 通过userIds查询值班
     * @param dutyIds
     * @param users
     * @return
     */
    public List<DutyUserEntry> selDutyUserByUserIds(List<ObjectId> dutyIds, List<ObjectId> users) {
        List<DutyUserEntry> retList = new ArrayList<DutyUserEntry>();
        BasicDBObject query =new BasicDBObject("ir",Constant.ZERO);
        if (dutyIds!=null && dutyIds.size()!=0) {
            query.append("dtid",new BasicDBObject(Constant.MONGO_IN, dutyIds));
        }
        if (users!=null && users.size()!=0) {
            query.append("ui",new BasicDBObject(Constant.MONGO_IN, users));
        }
        query.append("type",new BasicDBObject(Constant.MONGO_NE,0));
        List<DBObject> list =new ArrayList<DBObject>();
        list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTY_USER_NAME, query, null, Constant.MONGO_SORTBY_ASC);
        for(DBObject dbo:list)
        {
            retList.add(new DutyUserEntry((BasicDBObject)dbo));
        }
        return retList;
    }

    /**
     * 通过userIds查询值班分页查询
     * @param dutyIds
     * @param users
     * @return
     */
    public List<DutyUserEntry> selDutyUserByUserIds(List<ObjectId> dutyIds, List<ObjectId> users,int skip,int limit) {
        List<DutyUserEntry> retList = new ArrayList<DutyUserEntry>();
        BasicDBObject query =new BasicDBObject("ir",Constant.ZERO);
        if (dutyIds!=null && dutyIds.size()!=0) {
            query.append("dtid",new BasicDBObject(Constant.MONGO_IN, dutyIds));
        }
        if (users!=null && users.size()!=0) {
            query.append("ui",new BasicDBObject(Constant.MONGO_IN, users));
        }
        query.append("type",new BasicDBObject(Constant.MONGO_NE,0));
        List<DBObject> list =new ArrayList<DBObject>();
        list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTY_USER_NAME, query, null, Constant.MONGO_SORTBY_ASC,skip,limit);
        for(DBObject dbo:list)
        {
            retList.add(new DutyUserEntry((BasicDBObject)dbo));
        }
        return retList;
    }

    /**
     *
     * @param id
     * @param sarlay
     */
    public void updateSarlary(ObjectId id, double sarlay) {
        BasicDBObject query = new BasicDBObject(Constant.ID,id).append("ir",Constant.ZERO);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("pay",sarlay));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTY_USER_NAME, query, updateValue);
    }

    /**
     *
     * @param dutyShiftId
     * @param type
     */
    public void updDutyShiftInfo(ObjectId dutyShiftId, int type) {
        BasicDBObject query = new BasicDBObject(Constant.ID,dutyShiftId).append("ir",Constant.ZERO);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("type",type));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTY_SHIFT_NAME, query, updateValue);
    }

    /**
     *
     * @param id
     * @return
     */
    public DutyShiftEntry selDutyShiftById(ObjectId id) {
        BasicDBObject query =new BasicDBObject(Constant.ID,id).append("ir", Constant.ZERO);
        DBObject dbo =  findOne(MongoFacroty.getAppDB(),Constant.COLLECTION_DUTY_SHIFT_NAME,query,null);
        if(null!=dbo)
        {
            return new DutyShiftEntry((BasicDBObject)dbo);
        }
        return null;
    }

    /**
     *
     * @param dutyId
     * @param orgUserId
     * @param userId
     */
    public void updateUserIdDutyInfo(ObjectId dutyId, ObjectId orgUserId,ObjectId userId) {
        BasicDBObject query = new BasicDBObject("dtid",dutyId).append("ui",orgUserId).append("ir",Constant.ZERO);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("ui",userId));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTY_USER_NAME, query, updateValue);
    }

    /**
     *
     * @param id
     * @param log
     * @param basicDBObjects
     */
    public void addDutyLog(ObjectId id,ObjectId userId, String log, List<BasicDBObject> basicDBObjects) {
        BasicDBObject query = new BasicDBObject("dtid",id).append("ui",userId).append("ir", Constant.ZERO);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("con",log).append("dcl", basicDBObjects));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTY_USER_NAME, query, updateValue);
    }

    /**
     *
     * @param dutyUserId
     */
    public DutyUserEntry selDutyUserById(ObjectId dutyUserId,ObjectId userId) {
        BasicDBObject query =new BasicDBObject("dtid",dutyUserId).append("ui",userId).append("ir", Constant.ZERO);
        DBObject dbo =  findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTY_USER_NAME,query,null);
        if(null!=dbo)
        {
            return new DutyUserEntry((BasicDBObject)dbo);
        }
        return null;
    }

    /**
     *
     * @param modelEntry
     * @return
     */
    public ObjectId addDutyModelDetail(ModelEntry modelEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTY_MODEL_DETAIL_NAME,modelEntry.getBaseEntry());
        return modelEntry.getID();
    }

    public List<ModelEntry> selDutyModelDetail(ObjectId id) {
        List<ModelEntry> retList = new ArrayList<ModelEntry>();
        BasicDBObject query =new BasicDBObject("ir",Constant.ZERO).append("dmid", id);
        List<DBObject> list =new ArrayList<DBObject>();
        list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTY_MODEL_DETAIL_NAME, query, null, Constant.MONGO_SORTBY_ASC);
        for(DBObject dbo:list)
        {
            retList.add(new ModelEntry((BasicDBObject)dbo));
        }
        return retList;
    }

    /**
     *
     * @param schoolId
     * @param modelName
     * @return
     */
    public int checkModelNameCount(ObjectId schoolId, String modelName,String modelId) {
        BasicDBObject query =new BasicDBObject("si",schoolId).append("mnm",modelName).append("ir",Constant.ZERO);
        if (!StringUtils.isEmpty(modelId)) {
            query.append(Constant.ID,new BasicDBObject(Constant.MONGO_NE,new ObjectId(modelId)));
        }
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTY_MODEL_NAME, query);
    }

    /**
     * 删除值班列表所对应的值班时段
     * @param dutyTimeId
     * @return
     */
    public void delDutyInfoByDutyTimeId(ObjectId dutyTimeId,int week,int year) {
        BasicDBObject query = new BasicDBObject("dtid",dutyTimeId).append("idx",week).append("year",year);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("ir",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTY_NAME, query, updateValue);
    }

    /**
     * 设置ip
     * @param schoolId
     * @param ip
     */
    public void updateDutySetIp(ObjectId schoolId, String ip,int year,int week) {
        BasicDBObject query = new BasicDBObject("si",schoolId).append("year",year).append("week",week).append("ir", Constant.ZERO);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("ip",ip));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTYSET_NAME, query, updateValue);
    }

    /**
     *
     * @param projectId
     */
    public void delDutyInfoByProjectId(String projectId) {
        BasicDBObject query = new BasicDBObject("pid",new ObjectId(projectId));
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("dtid",null).append("pid",null));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTY_NAME, query, updateValue);
    }


    /**
     *  timeId获取值班
     * @param timeId
     * @return
     */
    public List<DutyEntry> selDutyInfoByTimeId(ObjectId timeId) {
        List<DutyEntry> retList = new ArrayList<DutyEntry>();
        BasicDBObject query =new BasicDBObject("dtid",timeId).append("ir", Constant.ZERO);
        List<DBObject> list =new ArrayList<DBObject>();
        list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTY_NAME, query, null, Constant.MONGO_SORTBY_ASC);
        for(DBObject dbo:list)
        {
            retList.add(new DutyEntry((BasicDBObject)dbo));
        }
        return retList;
    }

    /**
     *  更新编号
     * @param schoolId
     * @param week
     * @param year
     * @param index
     */
    public void updateDutyInfoByNum(ObjectId schoolId, int week, int year, String index,String num) {
        BasicDBObject query = new BasicDBObject("si",schoolId).append("idx",week).append("year",year).append("num",index);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("num",num));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTY_NAME, query, updateValue);
    }

    /**
     * 删除人员
     * @param dutyId
     * @param userId
     */
    public void delDutyUserEntryByUserId(ObjectId dutyId, String userId) {
        BasicDBObject query = new BasicDBObject("dtid",dutyId).append("ui", new ObjectId(userId));
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("ir",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTY_USER_NAME, query, updateValue);
    }

    /**
     * 獲取攝製INFO
     * @param dutySetId
     * @return
     */
    public DutySetEntry selDutySetById(ObjectId dutySetId) {
        BasicDBObject query =new BasicDBObject(Constant.ID,dutySetId).append("ir", Constant.ZERO);
        DBObject dbo =  findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTYSET_NAME,query,null);
        if(null!=dbo)
        {
            return new DutySetEntry((BasicDBObject)dbo);
        }
        return null;

    }

    /**
     *  跟新值班設置
     * @param id
     * @param entry
     */
    public void updateDutySetInfo(ObjectId id, DutySetEntry entry) {
        BasicDBObject query = new BasicDBObject(Constant.ID,id).append("ir", Constant.ZERO);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("type",entry.getType()).append("num",entry.getNum()).append("ip",entry.getIp()).append("uids",entry.getUserIds()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTYSET_NAME, query, updateValue);

    }

    /**
     *
     * @param dutySetId
     */
    public void delDutyProjectBySetId(ObjectId dutySetId) {
        BasicDBObject query = new BasicDBObject("dtid",dutySetId);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("ir",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTYSETPROJECT_NAME, query, updateValue);
    }

    /**
     *
     * @param dutySetId
     */
    public void delDutyTimeBySetId(ObjectId dutySetId) {
        BasicDBObject query = new BasicDBObject("dtid",dutySetId);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("ir",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTYSETTIME_NAME, query, updateValue);
    }

    /**
     * 检查值班项目是否重名
     * @param orgDutyProjectId
     * @param index
     * @param content
     * @param dutyProjectId
     * @param dutyId
     * @return
     */
    public int checkDutyProjectName(String orgDutyProjectId, int index, String content, String dutyProjectId, String dutyId) {
        BasicDBObject query =new BasicDBObject("dtid",new ObjectId(dutyId)).append("con",content).append("idx",index).append("ir", Constant.ZERO);
        if (index==0) {
            if (!StringUtils.isEmpty(orgDutyProjectId)) {
                query.append("orgid",new BasicDBObject(Constant.MONGO_NE,orgDutyProjectId));
            }
        } else if (index==1) {
            if (!StringUtils.isEmpty(dutyProjectId)) {
                query.append(Constant.ID,new BasicDBObject(Constant.MONGO_NE,new ObjectId(dutyProjectId)));
            }
        }
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTYSETPROJECT_NAME, query);
    }

    /**
     * 编辑值班说明
     * @param schoolId
     * @param explain
     */
    public void addDutyExplain(ObjectId schoolId,int year,int week, String explain) {
        BasicDBObject query = new BasicDBObject("si",schoolId).append("year",year).append("week",week).append("ir", Constant.ZERO);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("exp",explain));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTYSET_NAME, query, updateValue);
    }

    /**
     *  查询值班数量
     * @param dutyIds
     * @param userIds
     * @return
     */
    public int selDutyUserByUserIdsCount(List<ObjectId> dutyIds, List<ObjectId> userIds) {
        BasicDBObject query =new BasicDBObject("ir",Constant.ZERO);
        if (dutyIds!=null && dutyIds.size()!=0) {
            query.append("dtid",new BasicDBObject(Constant.MONGO_IN, dutyIds));
        }
        if (userIds!=null && userIds.size()!=0) {
            query.append("ui",new BasicDBObject(Constant.MONGO_IN, userIds));
        }
        query.append("type",new BasicDBObject(Constant.MONGO_NE,0));
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_DUTY_USER_NAME, query);

    }
}
