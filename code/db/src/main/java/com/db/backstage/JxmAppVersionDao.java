package com.db.backstage;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.backstage.JxmAppVersionEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/11/27.
 */
public class JxmAppVersionDao extends BaseDao {

    //添加
    public String addEntry(JxmAppVersionEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_APP_VERSION, entry.getBaseEntry());
        return entry.getID().toString() ;
    }

    public void updEntry(JxmAppVersionEntry e) {
        BasicDBObject query=new BasicDBObject(Constant.ID,e.getID());
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,e.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_APP_VERSION, query,updateValue);
    }
    //用户的所有课程列表
    public List<JxmAppVersionEntry> getIsNewObjectId() {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_APP_VERSION, query, Constant.FIELDS);
        List<JxmAppVersionEntry> retList =new ArrayList<JxmAppVersionEntry>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new JxmAppVersionEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    public List<JxmAppVersionEntry> getNewObjectId(int type) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO).append("typ",type);
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_APP_VERSION, query, Constant.FIELDS);
        List<JxmAppVersionEntry> retList =new ArrayList<JxmAppVersionEntry>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new JxmAppVersionEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    public List<JxmAppVersionEntry> getListByName(String name) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO).append("nam",name);
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_APP_VERSION, query, Constant.FIELDS);
        List<JxmAppVersionEntry> retList =new ArrayList<JxmAppVersionEntry>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new JxmAppVersionEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    //包名查询
    public List<JxmAppVersionEntry> getListByAppPackName(List<String> names) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO).append("nam",new BasicDBObject(Constant.MONGO_IN,names));
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_APP_VERSION, query, Constant.FIELDS);
        List<JxmAppVersionEntry> retList =new ArrayList<JxmAppVersionEntry>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new JxmAppVersionEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }


    public JxmAppVersionEntry getEntry(String packageName) {
        BasicDBObject query = new BasicDBObject("nam",packageName);
        query.append("isr", Constant.ZERO);
        DBObject obj =
                findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_APP_VERSION, query, Constant.FIELDS);
        if (obj != null) {
            return new JxmAppVersionEntry((BasicDBObject) obj);
        }
        return null;
    }

    public JxmAppVersionEntry getEntryByType(String packageName,int type) {
        BasicDBObject query = new BasicDBObject("nam",packageName).append("typ",type);
        query.append("isr", Constant.ZERO);
        DBObject obj =
                findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_APP_VERSION, query, Constant.FIELDS);
        if (obj != null) {
            return new JxmAppVersionEntry((BasicDBObject) obj);
        }
        return null;
    }

    public void removeById(ObjectId id){
        BasicDBObject query=new BasicDBObject(Constant.ID,id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_APP_VERSION,query);
    }


}
