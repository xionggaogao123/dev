package com.db.operation;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.operation.AppOperationEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by James on 2017/8/25.
 */
public class AppOperationDao extends BaseDao {
    //添加
    public String addEntry(AppOperationEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_OPERATION, entry.getBaseEntry());
        return entry.getID().toString();
    }
    public AppOperationEntry getEntry(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        query.append("isr",Constant.ZERO);
        DBObject obj =
                findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_OPERATION, query, Constant.FIELDS);
        if (obj != null) {
            return new AppOperationEntry((BasicDBObject) obj);
        }
        return null;
    }
    public void updEntry(AppOperationEntry e) {
        BasicDBObject query=new BasicDBObject(Constant.ID,e.getID());
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,e.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_OPERATION, query,updateValue);
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

    //老师评论列表查询
    public List<AppOperationEntry> getEntryListByParentId2(ObjectId contactId,int role,ObjectId userId,int page,int pageSize) {
        BasicDBObject query = new BasicDBObject()
                .append("cid",contactId)
                .append("rol",role)
                .append("uid", userId)
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
    //老师评论列表查询
    public List<AppOperationEntry> getEntryListByParentIdByStu(ObjectId contactId,List<ObjectId> userIds,int role,int page,int pageSize) {
        BasicDBObject query = new BasicDBObject()
                .append("cid",contactId)
                .append("rol",role)
                .append("uid",new BasicDBObject(Constant.MONGO_IN,userIds))
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

    public List<AppOperationEntry> getReadStudentOperation(ObjectId contactId,List<ObjectId> userIds,int role,int page,int pageSize) {
        BasicDBObject query = new BasicDBObject()
                .append("cid",contactId)
                .append("rol",role)
                .append("red",Constant.ONE)
                .append("uid", new BasicDBObject(Constant.MONGO_IN, userIds))
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

    public List<AppOperationEntry> getUnReadStudentOperation(ObjectId contactId,List<ObjectId> userIds,int role,int page,int pageSize) {
        BasicDBObject query = new BasicDBObject()
                .append("cid",contactId)
                .append("rol",role)
                .append("red",new BasicDBObject(Constant.MONGO_NE,Constant.ONE))
                .append("uid", new BasicDBObject(Constant.MONGO_IN, userIds))
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


    //所有已提交作业
    public List<AppOperationEntry> getAllStudentOperationList(ObjectId contactId,List<ObjectId> userIds,int role) {
        BasicDBObject query = new BasicDBObject()
                .append("cid",contactId)
                .append("rol",role)
                .append("uid",new BasicDBObject(Constant.MONGO_IN,userIds))
                .append("lev", 1)//一级
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

    public List<AppOperationEntry> getEntryListByParentId2(ObjectId contactId,int role) {
        BasicDBObject query = new BasicDBObject()
                .append("cid",contactId)
                .append("rol",role)
                .append("lev", 1)//一级
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
    public List<AppOperationEntry> getEntryListByUserIdAndId(ObjectId userId,ObjectId contactId,int role) {
        BasicDBObject query = new BasicDBObject()
                .append("cid",contactId)
                .append("rol",role)
                .append("uid",userId)
                .append("lev", 1)//一级
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

    /**
     * 查询当前作业某些孩子的提交情况
     * @param contactId
     * @param role
     * @return
     */
    public List<ObjectId> getEntryMap(List<ObjectId> userIds,ObjectId contactId,int role) {
        BasicDBObject query = new BasicDBObject()
                .append("cid",contactId)
                .append("rol",role)
                .append("uid",new BasicDBObject(Constant.MONGO_IN,userIds))
                .append("lev", 1)//一级
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_OPERATION,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<ObjectId> entryList = new ArrayList<ObjectId>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                AppOperationEntry appOperationEntry = new AppOperationEntry((BasicDBObject) obj);
                entryList.add(appOperationEntry.getUserId());
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
                .append("cid", contactId)
                .append("rol", role)
                .append("lev", 1)
                .append("isr",Constant.ZERO); // 未删除
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_OPERATION,
                        query);
        return count;
    }

    //查询所有已提交的数量
    public int countStudentLoadTimesFor(ObjectId contactId,List<ObjectId> userIds, int role) {
        BasicDBObject query = new BasicDBObject()
                .append("cid",contactId)
                .append("uid",new BasicDBObject(Constant.MONGO_IN,userIds))
                .append("rol", role)
                .append("lev", 1)
                .append("isr",Constant.ZERO); // 未删除
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_OPERATION,
                        query);
        return count;
    }

    public int countReadStudentLoadTimesFor(ObjectId contactId,List<ObjectId> userIds, int role) {
        BasicDBObject query = new BasicDBObject()
                .append("cid",contactId)
                .append("red",Constant.ONE)
                .append("uid", new BasicDBObject(Constant.MONGO_IN, userIds))
                .append("rol", role)
                .append("lev", 1)
                .append("isr",Constant.ZERO); // 未删除
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_OPERATION,
                        query);
        return count;
    }

    public int countunReadStudentLoadTimesFor(ObjectId contactId,List<ObjectId> userIds, int role) {
        BasicDBObject query = new BasicDBObject()
                .append("cid",contactId)
                .append("red",new BasicDBObject(Constant.MONGO_NE,Constant.ONE))
                .append("uid", new BasicDBObject(Constant.MONGO_IN, userIds))
                .append("rol", role)
                .append("lev", 1)
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

    public List<AppOperationEntry> getSecondListByParentId(ObjectId parentId) {
        BasicDBObject query = new BasicDBObject()
                .append("pid",parentId)
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
    //查询某个用户是否多次提交
    public List<AppOperationEntry> getAppOperationListById(ObjectId contactId,ObjectId userId) {
        BasicDBObject query = new BasicDBObject()
                .append("cid",contactId)
                .append("uid",userId)
                .append("lev",Constant.ONE)//一级
                .append("rol",Constant.THREE)//作业类型
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

    /**
     * 获取待阅留言反馈
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    public Map<String,Object> getFeedbackMsgByReadyRead(ObjectId userId, int page, int pageSize) {
        Map<String,Object> result = new HashMap<String, Object>();

        BasicDBObject query = new BasicDBObject()
                .append("rol",5)//留言反馈
                .append("red", new BasicDBObject(Constant.MONGO_NE,1))//未读
                .append("lev", Constant.ONE)//一级
                .append("isr", 0); // 未删除

        BasicDBObject queryAll = new BasicDBObject()
                .append("rol",5)//留言反馈
                .append("red", new BasicDBObject(Constant.MONGO_NE,1))//未读
                .append("lev", Constant.ONE)//一级
                .append("isr", 0); // 未删除

        if (null != userId){
            query.append("uid",userId);
        }
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
        result.put("entries", entryList);
        int count = count(MongoFacroty.getAppDB(),Constant.COLLECTION_APP_OPERATION,query);
        result.put("count", count);
        int countAll = count(MongoFacroty.getAppDB(),Constant.COLLECTION_APP_OPERATION,queryAll);
        result.put("countAll", countAll);
        return result;
    }

    /**
     * 获取已阅留言反馈
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    public Map<String,Object> getFeedbackMsgByAlreadyRead(ObjectId userId, int page, int pageSize) {
        Map<String,Object> result = new HashMap<String, Object>();

        BasicDBObject query = new BasicDBObject()
                .append("rol",5)//留言反馈
                .append("red", 1)//已读
                .append("lev", Constant.ONE)//一级
                .append("isr", 0); // 未删除

        BasicDBObject queryAll = new BasicDBObject()
                .append("rol",5)//留言反馈
                .append("red", 1)//已读
                .append("lev", Constant.ONE)//一级
                .append("isr", 0); // 未删除

        if (null != userId){
            query.append("uid",userId);
        }
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
        result.put("entries", entryList);
        int count = count(MongoFacroty.getAppDB(),Constant.COLLECTION_APP_OPERATION,query);
        result.put("count", count);

        int countAll = count(MongoFacroty.getAppDB(),Constant.COLLECTION_APP_OPERATION,queryAll);
        result.put("countAll", countAll);
        return result;
    }

    //已读评论
    public void updateAppOperationEntryToRead(ObjectId id){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("red",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_OPERATION, query,updateValue);
    }

    /**
     * 查看回复
     * @param parentId
     * @return
     */
    public AppOperationEntry getAnswerEntryByParentId(String parentId) {
        BasicDBObject query = new BasicDBObject()
                .append("rol",5)//留言反馈
                .append("pid",new ObjectId(parentId))//找父问题的答案
                .append("lev", Constant.TWO)//二级
                .append("isr", 0); // 未删除
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_OPERATION, query);
        if (null != dbObject){
            return new AppOperationEntry((BasicDBObject) dbObject);
        }else{
            return null;
        }
    }
}
