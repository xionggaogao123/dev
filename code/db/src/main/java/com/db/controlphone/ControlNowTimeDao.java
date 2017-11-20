package com.db.controlphone;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.controlphone.ControlNowTimeEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by James on 2017/11/17.
 */
public class ControlNowTimeDao extends BaseDao {

    //添加
    public String addEntry(ControlNowTimeEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_NOW_TIME, entry.getBaseEntry());
        return entry.getID().toString() ;
    }

    //单查询
    public ControlNowTimeEntry getEntry(int week) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO) .append("wek", week);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_NOW_TIME, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new ControlNowTimeEntry((BasicDBObject)dbo);
        }
        return null;
    }
public static void main(String[] args){
    int i = 1;
    System.out.println(i);
}
    //单查询
    public ControlNowTimeEntry getOtherEntry2(String dateTime,ObjectId userId) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO).append("cid",userId).append("dtm",dateTime);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_NOW_TIME, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new ControlNowTimeEntry((BasicDBObject)dbo);
        }
        return null;
    }

    public ControlNowTimeEntry getOtherEntry(String dateTime,ObjectId userId) {
        BasicDBObject query = new BasicDBObject();
        query.append("isr", Constant.ZERO).append("cid", userId).append("dtm",dateTime);
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_CONTROL_NOW_TIME,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        ControlNowTimeEntry entryList = null;
        if (dbList != null && !dbList.isEmpty()) {
            DBObject obj = dbList.get(0);
            entryList = new ControlNowTimeEntry((BasicDBObject)obj);
        }
        return entryList;
    }
    //删除作业
    public void deleteControlTime(ObjectId communityId,String dateTime){
        BasicDBObject query = new BasicDBObject("cid",communityId);
        query.append("dtm",dateTime);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isr",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_NOW_TIME, query,updateValue);
    }
}
