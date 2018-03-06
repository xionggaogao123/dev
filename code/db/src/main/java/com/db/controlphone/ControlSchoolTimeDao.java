package com.db.controlphone;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.controlphone.ControlSchoolTimeEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/11/16.
 */
public class ControlSchoolTimeDao extends BaseDao {
    //添加
    public String addEntry(ControlSchoolTimeEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_SCHOOL_TIME, entry.getBaseEntry());
        return entry.getID().toString() ;
    }

    //删除作业
    public void delAppCommentEntry(ObjectId id){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isr",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_SCHOOL_TIME, query,updateValue);
    }

    //单查询
    public ControlSchoolTimeEntry getEntry(int week) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO) .append("wek", week);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_SCHOOL_TIME, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new ControlSchoolTimeEntry((BasicDBObject)dbo);
        }
        return null;
    }

    //学校单查询
    public ControlSchoolTimeEntry getcommunityEntry(int week,ObjectId schoolId) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.TWO) .append("wek", week).append("pid",schoolId);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_SCHOOL_TIME, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new ControlSchoolTimeEntry((BasicDBObject)dbo);
        }
        return null;
    }

    //单查询
    public ControlSchoolTimeEntry getEntryById(ObjectId id) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO) .append(Constant.ID, id);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_SCHOOL_TIME, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new ControlSchoolTimeEntry((BasicDBObject)dbo);
        }
        return null;
    }

    //学校单查询
    public ControlSchoolTimeEntry getCommunityEntryById(ObjectId id) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.TWO) .append(Constant.ID, id);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_SCHOOL_TIME, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new ControlSchoolTimeEntry((BasicDBObject)dbo);
        }
        return null;
    }
    public ControlSchoolTimeEntry getOtherEntry(String dateTime) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO) .append("dtm", dateTime);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_SCHOOL_TIME, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new ControlSchoolTimeEntry((BasicDBObject)dbo);
        }
        return null;
    }

    public ControlSchoolTimeEntry getOtherCommunityEntry(String dateTime,ObjectId schoolId) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.TWO) .append("dtm", dateTime).append("pid",schoolId);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_SCHOOL_TIME, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new ControlSchoolTimeEntry((BasicDBObject)dbo);
        }
        return null;
    }
    //修改
    public void updEntry(ControlSchoolTimeEntry e) {
        BasicDBObject query=new BasicDBObject(Constant.ID,e.getID());
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,e.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_SCHOOL_TIME, query,updateValue);
    }
    //单查询
    public ControlSchoolTimeEntry getOtherEntry(int type,String dateTime) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO) .append("typ", type).append("dtm",dateTime);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_SCHOOL_TIME, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new ControlSchoolTimeEntry((BasicDBObject)dbo);
        }
        return null;
    }
    public ControlSchoolTimeEntry getOtherEntry2(int type,String dateTime) {
        BasicDBObject query = new BasicDBObject();
        query.append("isr", Constant.ZERO) .append("typ", type).append("dtm", dateTime);
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_CONTROL_SCHOOL_TIME,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        ControlSchoolTimeEntry entryList = null;
        if (dbList != null && !dbList.isEmpty()) {
            DBObject obj = dbList.get(0);
            entryList = new ControlSchoolTimeEntry((BasicDBObject)obj);
        }
        return entryList;
    }

    /**
     * 查询默认周常配置
     * @return
     */
    public List<ControlSchoolTimeEntry> getSchoolMoEntryList() {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO).append("typ",Constant.ONE);
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_SCHOOL_TIME, query, Constant.FIELDS);
        List<ControlSchoolTimeEntry> retList =new ArrayList<ControlSchoolTimeEntry>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new ControlSchoolTimeEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }
    public List<ControlSchoolTimeEntry> getAllEntryList() {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_SCHOOL_TIME, query, Constant.FIELDS);
        List<ControlSchoolTimeEntry> retList =new ArrayList<ControlSchoolTimeEntry>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new ControlSchoolTimeEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    public List<ControlSchoolTimeEntry> getAllSchoolEntryList(List<ObjectId> schoolIds) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.TWO);//学校类型
        query.append("pid",new BasicDBObject(Constant.MONGO_IN,schoolIds));
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_SCHOOL_TIME, query, Constant.FIELDS);
        List<ControlSchoolTimeEntry> retList =new ArrayList<ControlSchoolTimeEntry>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new ControlSchoolTimeEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    public List<ControlSchoolTimeEntry> getOneSchoolEntryList(ObjectId schoolId) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.TWO);//学校类型
        query.append("pid",schoolId);
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_SCHOOL_TIME, query, Constant.FIELDS);
        List<ControlSchoolTimeEntry> retList =new ArrayList<ControlSchoolTimeEntry>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new ControlSchoolTimeEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    public List<ControlSchoolTimeEntry> getAllDuringList() {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO).append("typ", Constant.THREE);
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_SCHOOL_TIME, query, Constant.FIELDS);
        List<ControlSchoolTimeEntry> retList =new ArrayList<ControlSchoolTimeEntry>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new ControlSchoolTimeEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

}
