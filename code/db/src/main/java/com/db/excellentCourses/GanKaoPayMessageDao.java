package com.db.excellentCourses;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.excellentCourses.GanKaoPayMessageEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-09-19.
 */
public class GanKaoPayMessageDao extends BaseDao {
    //添加提醒
    public String addEntry(GanKaoPayMessageEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_GAN_KAO_PAY, entry.getBaseEntry());
        return entry.getID().toString();
    }

    //查询未发送通知进行再次发送
    public List<GanKaoPayMessageEntry> getEntryList(long time){
        List<GanKaoPayMessageEntry> entryList=new ArrayList<GanKaoPayMessageEntry>();
        BasicDBObject query=new BasicDBObject().append("isr", Constant.ZERO).append("sta",Constant.ZERO);
        query.append("etm",new BasicDBObject(Constant.MONGO_LT,time));
        BasicDBObject orderquery = new BasicDBObject();
        orderquery.append("ctm",-1);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_GAN_KAO_PAY, query,
                Constant.FIELDS,orderquery);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new GanKaoPayMessageEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }


    //改变状态
    public void updateEntry(ObjectId id,int status,long time){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("sta",status).append("btm",time));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXCELLENT_COURSES, query,updateValue);
    }

    //改变状态
    public void updateEntry2(ObjectId id,int status){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("sta",status));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXCELLENT_COURSES, query,updateValue);
    }

    //改变状态
    public void updateTime(ObjectId id,int type,long time){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("typ",type).append("tim",time));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXCELLENT_COURSES, query,updateValue);
    }
}
