package com.db.excellentCourses;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.excellentCourses.UserClassDescEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by James on 2018-09-06.
 */
public class UserClassDescDao extends BaseDao {
    //添加收藏
    public String addEntry(UserClassDescEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_DESC, entry.getBaseEntry());
        return entry.getID().toString();
    }

    /**
     * 批量保存
     *
     * @param list
     */
    public void addBatch(List<DBObject> list) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_DESC, list);
    }

    //首页订单查询
    public Set<ObjectId> getEntryIdList(List<ObjectId> objectIds,ObjectId contactId){
        Set<ObjectId> entryList=new HashSet<ObjectId>();
        BasicDBObject query=new BasicDBObject("isr",Constant.ZERO);
        query.append("pid",new BasicDBObject(Constant.MONGO_IN,objectIds));
        query.append("cid",contactId);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_DESC, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new UserClassDescEntry((BasicDBObject) obj).getParentId());
            }
        }
        return entryList;
    }
}
