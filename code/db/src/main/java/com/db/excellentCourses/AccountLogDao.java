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

    public List<AccountLogEntry> getAllMemberBySchoolId(ObjectId userId,String contactId,int page,int pageSize) {
        BasicDBObject query = new BasicDBObject()
                .append("isr", 0); // 未删除
        query.append("uid",userId);
        if(contactId!=null && !contactId.equals("")){
            query.append("cid",new ObjectId(contactId));
        }
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_ACCOUNT_LOG,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC,(page - 1) * pageSize, pageSize);
        List<AccountLogEntry> entryList = new ArrayList<AccountLogEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AccountLogEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    public int countAllMemberBySchoolId(ObjectId userId,String contactId){
        BasicDBObject query = new BasicDBObject();
        query.append("isr",0);
        query.append("uid",userId);
        if(contactId!=null && !contactId.equals("")){
            query.append("cid",new ObjectId(contactId));
        }
        int count = count(MongoFacroty.getAppDB(),Constant.COLLECTION_ACCOUNT_LOG,query);
        return count;
    }
}
