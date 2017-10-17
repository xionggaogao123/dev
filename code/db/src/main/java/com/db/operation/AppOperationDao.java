package com.db.operation;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.operation.AppOperationEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/8/25.
 */
public class AppOperationDao extends BaseDao {
    //添加
    public String addEntry(AppOperationEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_OPERATION, entry.getBaseEntry());
        return entry.getID().toString();
    }
    //删除评论
    public void delAppOperationEntry(ObjectId id){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isr",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_OPERATION, query,updateValue);
    }


    //老师评论列表查询
    public List<AppOperationEntry> getEntryListByParentId(ObjectId contactId,int role,int page,int pageSize) {
        BasicDBObject query = new BasicDBObject()
                .append("cid",contactId)
                .append("rol",role)
                .append("lev", 1)//一级
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_OPERATION,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC, (page - 1) * pageSize, pageSize);
        List<AppOperationEntry> entryList = new ArrayList<AppOperationEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppOperationEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
    /**
     * 符合搜索条件的对象个数
     * @return
     */
    public int getEntryListByParentIdNum(ObjectId contactId,int role) {
        BasicDBObject query = new BasicDBObject()
                .append("cid",contactId)
                .append("rol", role)
                .append("lev", 1)
                .append("isr", 0);
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_OPERATION,
                        query);
        return count;
    }
    //查询所有已提交的数量
    public int countStudentLoadTimes(ObjectId contactId, int role) {
        BasicDBObject query = new BasicDBObject()
                .append("cid",contactId)
                .append("rol", role)
                .append("isr",Constant.ZERO); // 未删除
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_OPERATION,
                        query);
        return count;
    }

    //家长、学生评论列表查询
    public List<AppOperationEntry> getEntryListByUserId(ObjectId userId,int role,ObjectId contactId,int page,int pageSize) {
        BasicDBObject query = new BasicDBObject()
                .append("uid",userId)
                .append("cid", contactId)
                .append("rol",role)
                .append("lev", Constant.ONE)//一级
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_OPERATION,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC, (page - 1) * pageSize, pageSize);
        List<AppOperationEntry> entryList = new ArrayList<AppOperationEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppOperationEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
    //二级评论列表查询
    public List<AppOperationEntry> getSecondList(List<ObjectId> parentIds) {
        BasicDBObject query = new BasicDBObject()
                .append("pid", new BasicDBObject(Constant.MONGO_IN,parentIds))
                .append("lev",Constant.TWO)//二级
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_OPERATION,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<AppOperationEntry> entryList = new ArrayList<AppOperationEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppOperationEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    public List<AppOperationEntry> getSecondListByParentId(ObjectId parentId,int page,int pageSize) {
        BasicDBObject query = new BasicDBObject()
                .append("pid",parentId)
                .append("lev",Constant.TWO)//二级
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_OPERATION,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC, (page - 1) * pageSize, pageSize);
        List<AppOperationEntry> entryList = new ArrayList<AppOperationEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppOperationEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    /**
     * 查询作业所有回复的数量
     * @param
     * @return
     */
    public int getEntryCount(ObjectId parentId) {
        BasicDBObject query = new BasicDBObject()
                .append("pid",parentId)
                .append("lev", Constant.TWO)//二级
                .append("isr", 0); // 未删除
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_OPERATION,
                        query);
        return count;
    }
}
