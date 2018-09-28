package com.db.operation;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.operation.AppNewOperationEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/8/25.
 */
public class AppNewOperationDao extends BaseDao {
    //添加
    public String addEntry(AppNewOperationEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_TOPIC_COMMENT, entry.getBaseEntry());
        return entry.getID().toString();
    }
    public AppNewOperationEntry getEntry(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        query.append("isr",Constant.ZERO);
        DBObject obj =
                findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_TOPIC_COMMENT, query, Constant.FIELDS);
        if (obj != null) {
            return new AppNewOperationEntry((BasicDBObject) obj);
        }
        return null;
    }
    public void updEntry(AppNewOperationEntry e) {
        BasicDBObject query=new BasicDBObject(Constant.ID,e.getID());
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,e.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_TOPIC_COMMENT, query,updateValue);
    }
    //删除评论
    public void delAppOperationEntry(ObjectId id){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isr",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_TOPIC_COMMENT, query,updateValue);
    }

    public void updateHotlist(ObjectId id,int type){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("rol",type));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_TOPIC_COMMENT, query,updateValue);
    }


    //超级话题
    public List<AppNewOperationEntry> getEntryListByParentId(List<ObjectId> contactIds,int role,int page,int pageSize) {
        BasicDBObject query = new BasicDBObject()
                .append("cid",new BasicDBObject(Constant.MONGO_IN,contactIds))
                .append("rol",role)
                .append("lev", 1)//一级
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_TOPIC_COMMENT,
                        query, Constant.FIELDS,
                        new BasicDBObject("zc",-1), (page - 1) * pageSize, pageSize);
        List<AppNewOperationEntry> entryList = new ArrayList<AppNewOperationEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppNewOperationEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
    
  //超级话题
    public List<AppNewOperationEntry> getEntryListByParentId(List<ObjectId> contactIds,int page,int pageSize) {
        BasicDBObject query = new BasicDBObject()
                .append("cid",new BasicDBObject(Constant.MONGO_IN,contactIds))
                .append("lev", 1)//一级
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_TOPIC_COMMENT,
                        query, Constant.FIELDS,
                        new BasicDBObject("zc",-1), (page - 1) * pageSize, pageSize);
        List<AppNewOperationEntry> entryList = new ArrayList<AppNewOperationEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppNewOperationEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    //超级话题 所有
    public List<AppNewOperationEntry> getAllEntryListByParentId(List<ObjectId> contactIds,int page,int pageSize) {
        BasicDBObject query = new BasicDBObject()
                .append("cid", new BasicDBObject(Constant.MONGO_IN, contactIds))
                .append("lev", 1)//一级
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_TOPIC_COMMENT,
                        query, Constant.FIELDS,
                        new BasicDBObject("zc",-1), (page - 1) * pageSize, pageSize);
        List<AppNewOperationEntry> entryList = new ArrayList<AppNewOperationEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppNewOperationEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    //老师评论列表查询
    public List<AppNewOperationEntry> getEntryListByParentId2(ObjectId contactId,ObjectId userId,int page,int pageSize) {
        BasicDBObject query = new BasicDBObject()
                .append("cid", contactId)
                .append("uid", userId)
                .append("lev", 1)//一级
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_TOPIC_COMMENT,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC, (page - 1) * pageSize, pageSize);
        List<AppNewOperationEntry> entryList = new ArrayList<AppNewOperationEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppNewOperationEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
    //老师评论列表查询
    public List<AppNewOperationEntry> getEntryListByParentIdByStu(ObjectId contactId,List<ObjectId> userIds,int role,int page,int pageSize) {
        BasicDBObject query = new BasicDBObject()
                .append("cid",contactId)
                .append("rol",role)
                .append("uid",new BasicDBObject(Constant.MONGO_IN,userIds))
                .append("lev", 1)//一级
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_TOPIC_COMMENT,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC, (page - 1) * pageSize, pageSize);
        List<AppNewOperationEntry> entryList = new ArrayList<AppNewOperationEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppNewOperationEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    public List<AppNewOperationEntry> getEntryListByParentId2(ObjectId contactId,int role) {
        BasicDBObject query = new BasicDBObject()
                .append("cid",contactId)
                .append("rol",role)
                .append("lev", 1)//一级
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_TOPIC_COMMENT,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<AppNewOperationEntry> entryList = new ArrayList<AppNewOperationEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppNewOperationEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
    public List<AppNewOperationEntry> getEntryListByUserIdAndId(ObjectId userId,ObjectId contactId,int role) {
        BasicDBObject query = new BasicDBObject()
                .append("cid",contactId)
                .append("rol",role)
                .append("uid",userId)
                .append("lev", 1)//一级
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_TOPIC_COMMENT,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<AppNewOperationEntry> entryList = new ArrayList<AppNewOperationEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppNewOperationEntry((BasicDBObject) obj));
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
                        Constant.COLLECTION_TOPIC_COMMENT,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<ObjectId> entryList = new ArrayList<ObjectId>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                AppNewOperationEntry appOperationEntry = new AppNewOperationEntry((BasicDBObject) obj);
                entryList.add(appOperationEntry.getUserId());
            }
        }
        return entryList;
    }
    /**
     * 符合搜索条件的对象个数
     * @return
     */
    public int getEntryListByParentIdNum(List<ObjectId> contactIds,int role) {
        BasicDBObject query = new BasicDBObject()
                .append("cid",new BasicDBObject(Constant.MONGO_IN,contactIds))
                .append("rol", role)
                .append("lev", 1)
                .append("isr", 0);
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_TOPIC_COMMENT,
                        query);
        return count;
    }
    
    /**
     * 符合搜索条件的对象个数
     * @return
     */
    public int getEntryListByParentIdNum(List<ObjectId> contactIds) {
        BasicDBObject query = new BasicDBObject()
                .append("cid",new BasicDBObject(Constant.MONGO_IN,contactIds))
                .append("lev", 1)
                .append("isr", 0);
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_TOPIC_COMMENT,
                        query);
        return count;
    }

    public int getAllEntryListByParentIdNum(List<ObjectId> contactIds) {
        BasicDBObject query = new BasicDBObject()
                .append("cid",new BasicDBObject(Constant.MONGO_IN,contactIds))
                .append("lev", 1)
                .append("isr", 0);
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_TOPIC_COMMENT,
                        query);
        return count;
    }

    //查询所有已提交的数量
    public int countStudentLoadTimes(ObjectId contactId, int role) {
        BasicDBObject query = new BasicDBObject()
                .append("cid",contactId)
                .append("rol", role)
                .append("lev", 1)
                .append("isr",Constant.ZERO); // 未删除
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_TOPIC_COMMENT,
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
                        Constant.COLLECTION_TOPIC_COMMENT,
                        query);
        return count;
    }

    //家长、学生评论列表查询
    public List<AppNewOperationEntry> getEntryListByUserId(ObjectId userId,int role,ObjectId contactId,int page,int pageSize) {
        BasicDBObject query = new BasicDBObject()
                .append("uid",userId)
                .append("cid", contactId)
                .append("rol",role)
                .append("lev", Constant.ONE)//一级
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_TOPIC_COMMENT,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC, (page - 1) * pageSize, pageSize);
        List<AppNewOperationEntry> entryList = new ArrayList<AppNewOperationEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppNewOperationEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
    //二级评论列表查询
    public List<AppNewOperationEntry> getSecondList(List<ObjectId> parentIds) {
        BasicDBObject query = new BasicDBObject()
                .append("pid", new BasicDBObject(Constant.MONGO_IN,parentIds))
                .append("lev",Constant.TWO)//二级
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_TOPIC_COMMENT,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<AppNewOperationEntry> entryList = new ArrayList<AppNewOperationEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppNewOperationEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    public List<AppNewOperationEntry> getSecondListByParentId(ObjectId parentId) {
        BasicDBObject query = new BasicDBObject()
                .append("pid",parentId)
                .append("lev",Constant.TWO)//二级
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_TOPIC_COMMENT,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<AppNewOperationEntry> entryList = new ArrayList<AppNewOperationEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppNewOperationEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
    //查询某个用户是否多次提交
    public List<AppNewOperationEntry> getAppOperationListById(ObjectId contactId,ObjectId userId) {
        BasicDBObject query = new BasicDBObject()
                .append("cid",contactId)
                .append("uid",userId)
                .append("lev",Constant.ONE)//一级
                .append("rol",Constant.THREE)//作业类型
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_TOPIC_COMMENT,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<AppNewOperationEntry> entryList = new ArrayList<AppNewOperationEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppNewOperationEntry((BasicDBObject) obj));
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
                        Constant.COLLECTION_TOPIC_COMMENT,
                        query);
        return count;
    }
}
