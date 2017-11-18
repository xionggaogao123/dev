package com.db.controlphone;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
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
    //添加
    public String addEntry(ControlAppResultEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_APP_RESULT, entry.getBaseEntry());
        return entry.getID().toString() ;
    }
    //用户的所有课程列表
    public List<ObjectId> getIsNewObjectId(ObjectId userId,long dateTime) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("isn", Constant.ZERO);
        query.append("dtm", dateTime);
        query.append("uid",userId);
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

    public List<ControlAppResultEntry> getIsNewEntryList(ObjectId userId,long dateTime) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("isn", Constant.ZERO);
        query.append("dtm", dateTime);
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

    //学生的实时使用时间
    public int getAllTime(ObjectId userId) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("isn", Constant.ZERO);
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

    /**
     * 修改
     */
    public void updEntry(List<ObjectId> ids) {
        BasicDBObject query = new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_IN,ids));
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isn",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_APP_RESULT, query,updateValue);
    }
}
