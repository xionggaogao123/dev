package com.db.excellentCourses;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.excellentCourses.UserCCLoinEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-09-06.
 */
public class UserCCLoinDao extends BaseDao {
    //添加收藏
    public String addEntry(UserCCLoinEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CC_LOGIN, entry.getBaseEntry());
        return entry.getID().toString();
    }

    /**
     * 批量保存
     *
     * @param list
     */
    public void addBatch(List<DBObject> list) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CC_LOGIN, list);
    }


    public  List<UserCCLoinEntry> getAllEntryIdList(ObjectId userId,ObjectId contactId){
        List<UserCCLoinEntry> entryList=new ArrayList<UserCCLoinEntry>();
        BasicDBObject query=new BasicDBObject("uid",userId);
        query.append("cid",contactId);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_CC_LOGIN, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new UserCCLoinEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

}
