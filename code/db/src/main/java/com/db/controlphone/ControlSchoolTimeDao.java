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
    public ControlSchoolTimeEntry getOtherEntry(String dateTime) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO) .append("dtn", dateTime);
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

}
