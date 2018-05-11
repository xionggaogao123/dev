package com.db.excellentCourses;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.excellentCourses.RechargeResultEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-05-10.
 */
public class RechargeResultDao extends BaseDao {
    public String saveEntry(RechargeResultEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_RECHARGE_RESULT,entry.getBaseEntry());
        return entry.getID().toString();
    }

    public RechargeResultEntry getEntry(ObjectId id){
        BasicDBObject query=new BasicDBObject(Constant.ID,id).append("isr",Constant.ZERO);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_RECHARGE_RESULT,query,Constant.FIELDS);
        if(null!=dbObject){
            return new RechargeResultEntry((BasicDBObject) dbObject);
        }else {
            return null;
        }
    }

    //课程中心
    public List<RechargeResultEntry> getAllEntryList(List<ObjectId> objectIds ,int page,int pageSize){
        List<RechargeResultEntry> entryList=new ArrayList<RechargeResultEntry>();
        BasicDBObject query=new BasicDBObject().append("isr",0);
        query.append("uid",new BasicDBObject(Constant.MONGO_IN,objectIds));
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_RECHARGE_RESULT, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new RechargeResultEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    /**
     * 符合搜索条件的对象个数
     * @return
     */
    public int selectMyCount(List<ObjectId> objectIds) {
        BasicDBObject query=new BasicDBObject().append("isr",0);
        query.append("uid",new BasicDBObject(Constant.MONGO_IN,objectIds));
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_RECHARGE_RESULT,
                        query);
        return count;
    }


}
