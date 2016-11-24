package com.db.customized;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.customized.RecordVideoEntry;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/8/23.
 */
public class RecordVideoDao extends BaseDao {

    public ObjectId add(RecordVideoEntry recordVideoEntry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_RECORD_VIDEO, recordVideoEntry.getBaseEntry());
        return recordVideoEntry.getID();
    }

    public void removeById(ObjectId Id){
        BasicDBObject query=new BasicDBObject(Constant.ID,Id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_RECORD_VIDEO, query);
    }

    public void logicRemove(ObjectId Id){
        BasicDBObject query=new BasicDBObject(Constant.ID,Id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("ir",1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_RECORD_VIDEO, query,updateValue);
    }

    public List<RecordVideoEntry> testAll(){
        List<RecordVideoEntry> retList =new ArrayList<RecordVideoEntry>();
        List<DBObject> dbos =find(MongoFacroty.getAppDB(), Constant.COLLECTION_RECORD_VIDEO,new BasicDBObject(), Constant.FIELDS,new BasicDBObject("_id",-1));
        if(null!=dbos && dbos.size()>0)
        {
            for(DBObject dbo:dbos)
            {
                retList.add( new RecordVideoEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }


    public List<RecordVideoEntry> getRecordVideoList(String date,String app,int skip,int limit){
        List<RecordVideoEntry> retList =new ArrayList<RecordVideoEntry>();
        BasicDBObject query =new BasicDBObject("ir",0);
        if(StringUtils.isNotBlank(date)){
            query.append("ti", date);
        }
        if(StringUtils.isNotBlank(app)){
            query.append("app",app);
        }

        List<DBObject> dbos =find(MongoFacroty.getAppDB(), Constant.COLLECTION_RECORD_VIDEO,query, Constant.FIELDS,new BasicDBObject("_id",-1),skip,limit);
        if(null!=dbos && dbos.size()>0)
        {
            for(DBObject dbo:dbos)
            {
                retList.add( new RecordVideoEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    public int countRecordVideoList(String date,String app){

        BasicDBObject query =new BasicDBObject("ir",0);
        if(StringUtils.isNotBlank(date)){
            query.append("ti", date);
        }
        if(StringUtils.isNotBlank(date)){
            query.append("app",app);
        }

        int count =count(MongoFacroty.getAppDB(), Constant.COLLECTION_RECORD_VIDEO,query);

        return count;
    }

}
