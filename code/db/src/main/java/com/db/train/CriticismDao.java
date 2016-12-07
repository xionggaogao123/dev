package com.db.train;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.train.CriticismEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/12/6.
 */
public class CriticismDao extends BaseDao {

    public void saveOrUpdate(CriticismEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_TRAIN_COMMENT,entry.getBaseEntry());
    }

    public List<CriticismEntry> getCriticismEntries(ObjectId instituteId,int page,int pageSize){
        List<CriticismEntry> entries=new ArrayList<CriticismEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("sid",instituteId);
        BasicDBObject order=new BasicDBObject()
                .append(Constant.ID,-1)
                .append("sc",-1);
        List<DBObject> dbObjects=find(MongoFacroty.getAppDB(), Constant.COLLECTION_TRAIN_COMMENT,query,Constant.FIELDS,order,(page-1)*pageSize,pageSize);
        if(null!=dbObjects&&!dbObjects.isEmpty()){
            for(DBObject dbObject:dbObjects){
                entries.add(new CriticismEntry((BasicDBObject)dbObject));
            }
        }
        return entries;
    }

    public int countCriticismEntries(ObjectId instituteId){
        BasicDBObject query=new BasicDBObject()
                .append("sid",instituteId);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_TRAIN_COMMENT,query);
    }

    public CriticismEntry getEntry(ObjectId instituteId,ObjectId userId){
        BasicDBObject query=new BasicDBObject()
                .append("sid",instituteId)
                .append("uid",userId)
                .append("ir",0);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_TRAIN_COMMENT,query);
        if(null!=dbObject){
            return new CriticismEntry((BasicDBObject)dbObject);
        }else{
            return null;
        }
    }
}
