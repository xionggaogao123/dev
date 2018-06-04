package com.db.excellentCourses;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.excellentCourses.AccountLogEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-06-04.
 */
public class AccountLogDao extends BaseDao {


    //添加日志
    public String addEntry(AccountLogEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_ACCOUNT_LOG, entry.getBaseEntry());
        return entry.getID().toString();
    }

    //首页订单查询
    public List<AccountLogEntry> getEntryList(ObjectId userId){
        List<AccountLogEntry> entryList=new ArrayList<AccountLogEntry>();
        BasicDBObject query=new BasicDBObject("uid",userId).append("isr", Constant.ZERO);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_ACCOUNT_LOG, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AccountLogEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
}
