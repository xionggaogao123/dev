package com.db.backstage;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.backstage.PushMessageEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-06-28.
 */
public class PushMessageDao extends BaseDao {
    //添加
    public String addEntry(PushMessageEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_PUSH_MESSAGE, entry.getBaseEntry());
        return entry.getID().toString() ;
    }

    //删除作业
    public void delEntry(ObjectId id){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("sta",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_PUSH_MESSAGE, query,updateValue);
    }

    //删除作业
    public void delOneEntry(ObjectId id){
        BasicDBObject query = new BasicDBObject("cid",id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isr",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_PUSH_MESSAGE, query,updateValue);
    }

    //修改提醒
    public void updateEntry(ObjectId id,long time,long date){
        BasicDBObject query = new BasicDBObject("cid",id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("sta",Constant.ZERO).append("stm",time).append("dtm",date));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_PUSH_MESSAGE, query,updateValue);
    }
    public boolean isNotHave(ObjectId contactId) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO) .append("cid", contactId);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_PUSH_MESSAGE, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return false;
        }
        return true;
    }

    public List<PushMessageEntry> selectList(long dateTime,int status,long currentTime) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("sta", status).append("dtm", dateTime);
        query.append("stm",new BasicDBObject(Constant.MONGO_LTE,currentTime));
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_PUSH_MESSAGE,query, Constant.FIELDS,
                Constant.MONGO_SORTBY_DESC);
        List<PushMessageEntry> retList =new ArrayList<PushMessageEntry>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new PushMessageEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

}
