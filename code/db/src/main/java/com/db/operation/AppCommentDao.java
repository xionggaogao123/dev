package com.db.operation;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.operation.AppCommentEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/8/25.
 */
public class AppCommentDao extends BaseDao {
    //添加
    public String addEntry(AppCommentEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_COMMENT, entry.getBaseEntry());
        return entry.getID().toString();
    }
    /**
     * 修改
     */
    public void updEntry(AppCommentEntry e) {
        BasicDBObject query=new BasicDBObject(Constant.ID,e.getID());
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,e.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_COMMENT, query,updateValue);
    }
    public AppCommentEntry getEntry(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        query.append("isr",Constant.ZERO);
        DBObject obj =
                findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_COMMENT, query, Constant.FIELDS);
        if (obj != null) {
            return new AppCommentEntry((BasicDBObject) obj);
        }
        return null;
    }

    //老师当日作业列表查询
    public List<AppCommentEntry> getEntryListByUserId(ObjectId userId,long dateTime) {
        BasicDBObject query = new BasicDBObject()
                .append("aid",userId)
                .append("dtm",dateTime)
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_COMMENT,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<AppCommentEntry> entryList = new ArrayList<AppCommentEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppCommentEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    //根据idList查询作业信息
    public List<AppCommentEntry> getEntryListByIds(List<ObjectId> ids) {
        BasicDBObject query = new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_IN,ids))
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_COMMENT,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<AppCommentEntry> entryList = new ArrayList<AppCommentEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppCommentEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    //老师月作业列表查询
    public List<AppCommentEntry> selectResultList(ObjectId userId,List<Integer> monthList) {
        List<Integer> ilist = new ArrayList<Integer>();
        ilist.add(1);
        ilist.add(0);
        BasicDBObject query = new BasicDBObject()
                .append("aid",userId)
                .append("mon", new BasicDBObject(Constant.MONGO_IN, monthList))
                .append("sta", new BasicDBObject(Constant.MONGO_IN, ilist))
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_COMMENT,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<AppCommentEntry> entryList = new ArrayList<AppCommentEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppCommentEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
    //老师日作业列表查询
    public List<AppCommentEntry> selectDateList(ObjectId userId,long dateTime) {
        List<Integer> ilist = new ArrayList<Integer>();
        ilist.add(1);
        ilist.add(0);
        BasicDBObject query = new BasicDBObject()
                .append("aid",userId)
                .append("dtm",dateTime)
                .append("sta",new BasicDBObject(Constant.MONGO_IN,ilist))
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_COMMENT,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<AppCommentEntry> entryList = new ArrayList<AppCommentEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppCommentEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
    //老师所有作业列表查询
    public List<AppCommentEntry> selectDateListPage(ObjectId userId,int page,int pageSize) {
        List<Integer> ilist = new ArrayList<Integer>();
        ilist.add(1);
        ilist.add(0);
        BasicDBObject query = new BasicDBObject()
                .append("aid", userId)
                .append("sta", new BasicDBObject(Constant.MONGO_IN, ilist))
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_COMMENT,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC, (page - 1) * pageSize, pageSize);
        List<AppCommentEntry> entryList = new ArrayList<AppCommentEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppCommentEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
    //老师所有作业列表查询
    public List<AppCommentEntry> selectWebDateListPage(ObjectId userId,String communityId,String subjectId,int page,int pageSize) {
        List<Integer> ilist = new ArrayList<Integer>();
        ilist.add(1);
        ilist.add(0);
        BasicDBObject query = new BasicDBObject()
                .append("aid", userId)
                .append("sta", new BasicDBObject(Constant.MONGO_IN, ilist))
                .append("isr", 0); // 未删除
        if(communityId != null && !communityId.equals("")){
           query.append("rid",new ObjectId(communityId));
        }
        if(subjectId != null && !subjectId.equals("")){
            query.append("sid",new ObjectId(subjectId));
        }
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_COMMENT,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC, (page - 1) * pageSize, pageSize);
        List<AppCommentEntry> entryList = new ArrayList<AppCommentEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppCommentEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
    //根据社区id查询
    public List<AppCommentEntry> selectNewListByCommunityId(ObjectId communityId,int page,int pageSize) {
        List<Integer> ilist = new ArrayList<Integer>();
        ilist.add(1);
        ilist.add(0);
        BasicDBObject query = new BasicDBObject()
                .append("rid", communityId)
                .append("sta", new BasicDBObject(Constant.MONGO_IN, ilist))
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_COMMENT,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC,(page - 1) * pageSize, pageSize);
        List<AppCommentEntry> entryList = new ArrayList<AppCommentEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppCommentEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    /**
     * 符合搜索条件的对象个数
     * @return
     */
    public int selectNewListByCommunityIdNum(ObjectId communityId) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("rid", communityId);
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_COMMENT,
                        query);
        return count;
    }

    /**
     * 符合搜索条件的对象个数
     * @return
     */
    public int getNumber(ObjectId userId) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("uid", userId);
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_COMMENT,
                        query);
        return count;
    }
    public int getWebNumber(ObjectId userId,String communityId,String subjectId) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("uid", userId);
        if(communityId != null && !communityId.equals("")){
            query.append("rid",new ObjectId(communityId));
        }
        if(subjectId != null && !subjectId.equals("")){
            query.append("sid",new ObjectId(subjectId));
        }
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_COMMENT,
                        query);
        return count;
    }

    //老师日作业列表查询
    public List<AppCommentEntry> selectWillDateList(ObjectId userId) {
        BasicDBObject query = new BasicDBObject()
                .append("aid",userId)
                .append("sta",2)
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_COMMENT,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<AppCommentEntry> entryList = new ArrayList<AppCommentEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppCommentEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
    public List<AppCommentEntry> selectWebWillDateList(ObjectId userId,String communityId,String subjectId) {
        BasicDBObject query = new BasicDBObject()
                .append("aid",userId)
                .append("sta",2)
                .append("isr", 0); // 未删除
        if(communityId != null && !communityId.equals("")){
            query.append("rid",new ObjectId(communityId));
        }
        if(subjectId != null && !subjectId.equals("")){
            query.append("sid",new ObjectId(subjectId));
        }
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_COMMENT,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<AppCommentEntry> entryList = new ArrayList<AppCommentEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppCommentEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
    //家长日作业列表查询//收到的
    public List<AppCommentEntry> selectPageDateList2(List<ObjectId> userIds,ObjectId adminId,int page,int pageSize) {
        List<Integer> ilist = new ArrayList<Integer>();
        ilist.add(1);
        ilist.add(0);
        BasicDBObject query = new BasicDBObject()
                .append("rid",new BasicDBObject(Constant.MONGO_IN,userIds))
                .append("aid",new BasicDBObject(Constant.MONGO_NE,adminId))
                .append("sta", new BasicDBObject(Constant.MONGO_IN,ilist))
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_COMMENT,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC,(page - 1) * pageSize, pageSize);
        List<AppCommentEntry> entryList = new ArrayList<AppCommentEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppCommentEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    public List<AppCommentEntry> selectWebPageDateList(List<ObjectId> userIds,String subjectId,ObjectId adminId,int page,int pageSize) {
        List<Integer> ilist = new ArrayList<Integer>();
        ilist.add(1);
        ilist.add(0);
        BasicDBObject query = new BasicDBObject()
                .append("rid",new BasicDBObject(Constant.MONGO_IN,userIds))
                .append("aid",new BasicDBObject(Constant.MONGO_NE,adminId))
                .append("sta", new BasicDBObject(Constant.MONGO_IN,ilist))
                .append("isr", 0); // 未删除
        if(subjectId != null && !subjectId.equals("")){
            query.append("sid",new ObjectId(subjectId));
        }
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_COMMENT,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC,(page - 1) * pageSize, pageSize);
        List<AppCommentEntry> entryList = new ArrayList<AppCommentEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppCommentEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
    /**
     * 符合搜索条件的对象个数
     * @return
     */
    public int getPageNumber(List<ObjectId> userIds,ObjectId adminId) {
        List<Integer> ilist = new ArrayList<Integer>();
        ilist.add(1);
        ilist.add(0);
        BasicDBObject query = new BasicDBObject()
                .append("rid",new BasicDBObject(Constant.MONGO_IN,userIds))
                .append("aid",new BasicDBObject(Constant.MONGO_NE,adminId))
                .append("sta", new BasicDBObject(Constant.MONGO_IN,ilist))
                .append("isr", 0); // 未删除
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_COMMENT,
                        query);
        return count;
    }
    public int getWebPageNumber(List<ObjectId> userIds,String subjectId,ObjectId adminId) {
        List<Integer> ilist = new ArrayList<Integer>();
        ilist.add(1);
        ilist.add(0);
        BasicDBObject query = new BasicDBObject()
                .append("rid",new BasicDBObject(Constant.MONGO_IN,userIds))
                .append("aid",new BasicDBObject(Constant.MONGO_NE,adminId))
                .append("sta", new BasicDBObject(Constant.MONGO_IN,ilist))
                .append("isr", 0); // 未删除
        if(subjectId != null && !subjectId.equals("")){
            query.append("sid",new ObjectId(subjectId));
        }
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_COMMENT,
                        query);
        return count;
    }

    public List<AppCommentEntry> selectDateList2(List<ObjectId> userIds,long dateTime) {
        List<Integer> ilist = new ArrayList<Integer>();
        ilist.add(1);
        ilist.add(0);
        BasicDBObject query = new BasicDBObject()
                .append("rid",new BasicDBObject(Constant.MONGO_IN,userIds))
                .append("dtm",dateTime)
                .append("sta",new BasicDBObject(Constant.MONGO_IN,ilist))
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_COMMENT,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<AppCommentEntry> entryList = new ArrayList<AppCommentEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppCommentEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
    //家长日作业列表查询
    public List<AppCommentEntry> selectDateListMonth(List<ObjectId> cids,List<Integer> monthList) {
        List<Integer> ilist = new ArrayList<Integer>();
        ilist.add(1);
        ilist.add(0);
        BasicDBObject query = new BasicDBObject()
                .append("rid",new BasicDBObject(Constant.MONGO_IN,cids))
                .append("mon", new BasicDBObject(Constant.MONGO_IN, monthList))
                .append("sta", new BasicDBObject(Constant.MONGO_IN, ilist))
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_COMMENT,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<AppCommentEntry> entryList = new ArrayList<AppCommentEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppCommentEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
    //删除作业
    public void delAppCommentEntry(ObjectId id){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isr",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_COMMENT, query,updateValue);
    }
}
