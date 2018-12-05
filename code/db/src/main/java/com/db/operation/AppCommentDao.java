package com.db.operation;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.operation.AppCommentEntry;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

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
        query.append("aid", userId);
        List<Integer> ilist = new ArrayList<Integer>();
        ilist.add(1);
        ilist.add(0);
        query.append("sta", new BasicDBObject(Constant.MONGO_IN, ilist));
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_COMMENT,
                        query);
        return count;
    }
    public int getWebNumber(ObjectId userId,String communityId,String subjectId) {
        List<Integer> ilist = new ArrayList<Integer>();
        ilist.add(1);
        ilist.add(0);
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("aid", userId).append("sta", new BasicDBObject(Constant.MONGO_IN, ilist));
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

    //家长日作业列表查询//收到的
    public List<AppCommentEntry> selectAllPageDateList(List<ObjectId> userIds,int page,int pageSize) {
        List<Integer> ilist = new ArrayList<Integer>();
        ilist.add(1);
        ilist.add(0);
        BasicDBObject query = new BasicDBObject()
                .append("rid",new BasicDBObject(Constant.MONGO_IN,userIds))
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
    public List<AppCommentEntry> selectWebAllDatePageList(List<ObjectId> userIds,String communityId,String subjectId,int page,int pageSize) {
        List<Integer> ilist = new ArrayList<Integer>();
        ilist.add(1);
        ilist.add(0);
        BasicDBObject query = new BasicDBObject()
                .append("sta", new BasicDBObject(Constant.MONGO_IN, ilist))
                .append("isr", 0); // 未删除
        if(subjectId != null && !subjectId.equals("")){
            query.append("sid",new ObjectId(subjectId));
        }
        if(communityId!=null && !communityId.equals("")){
            query.append("rid",new ObjectId(communityId));
        }else{
            query.append("rid",new BasicDBObject(Constant.MONGO_IN, userIds));
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

    public List<AppCommentEntry> selectWebAllKeyPageList(List<ObjectId> userIds,String communityId,String subjectId,int page,int pageSize,String keyword) {
        List<Integer> ilist = new ArrayList<Integer>();
        ilist.add(1);
        ilist.add(0);
        BasicDBObject query = new BasicDBObject()
                .append("sta", new BasicDBObject(Constant.MONGO_IN, ilist))
                .append("isr", 0); // 未删除
        if(subjectId != null && !subjectId.equals("")){
            query.append("sid",new ObjectId(subjectId));
        }
        if(communityId!=null && !communityId.equals("")){
            query.append("rid",new ObjectId(communityId));
        }else{
            query.append("rid",new BasicDBObject(Constant.MONGO_IN, userIds));
        }

        if(StringUtils.isNotBlank(keyword)){
            BasicDBList list = new BasicDBList();
            Pattern pattern = Pattern.compile("^.*" + keyword + ".*$", Pattern.CASE_INSENSITIVE);
            list.add(new BasicDBObject().append("des", new BasicDBObject(Constant.MONGO_REGEX, pattern)));
            list.add(new BasicDBObject().append("tit", new BasicDBObject(Constant.MONGO_REGEX, pattern)));
            query.append(Constant.MONGO_OR, list);
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

    public int getWebAllDatePageNumber(List<ObjectId> userIds,String communityId,String subjectId) {
        List<Integer> ilist = new ArrayList<Integer>();
        ilist.add(1);
        ilist.add(0);
        BasicDBObject query = new BasicDBObject()
                .append("sta", new BasicDBObject(Constant.MONGO_IN, ilist))
                .append("isr", 0); // 未删除
        if(subjectId != null && !subjectId.equals("")){
            query.append("sid",new ObjectId(subjectId));
        }
        if(communityId!=null && !communityId.equals("")){
            query.append("rid",new ObjectId(communityId));
        }else{
            query.append("rid",new BasicDBObject(Constant.MONGO_IN, userIds));
        }
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_COMMENT,
                        query);
        return count;
    }

    public int getWebAllKeyPageNumber(List<ObjectId> userIds,String communityId,String subjectId,String keyword) {
        List<Integer> ilist = new ArrayList<Integer>();
        ilist.add(1);
        ilist.add(0);
        BasicDBObject query = new BasicDBObject()
                .append("sta", new BasicDBObject(Constant.MONGO_IN, ilist))
                .append("isr", 0); // 未删除
        if(subjectId != null && !subjectId.equals("")){
            query.append("sid",new ObjectId(subjectId));
        }
        if(communityId!=null && !communityId.equals("")){
            query.append("rid",new ObjectId(communityId));
        }else{
            query.append("rid",new BasicDBObject(Constant.MONGO_IN, userIds));
        }

        if(StringUtils.isNotBlank(keyword)){
            BasicDBList list = new BasicDBList();
            Pattern pattern = Pattern.compile("^.*" + keyword + ".*$", Pattern.CASE_INSENSITIVE);
            list.add(new BasicDBObject().append("des", new BasicDBObject(Constant.MONGO_REGEX, pattern)));
            list.add(new BasicDBObject().append("tit", new BasicDBObject(Constant.MONGO_REGEX, pattern)));
            query.append(Constant.MONGO_OR, list);
        }

        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_COMMENT,
                        query);
        return count;
    }
    
    
    public int getWebAllDatePageNumberByTime(List<ObjectId> communityIds,String subjectId, Long timeStart,Long timeEnd) {
        List<Integer> ilist = new ArrayList<Integer>();
        ilist.add(1);
        ilist.add(0);
        BasicDBObject query = new BasicDBObject();
        BasicDBList values = new BasicDBList();
        BasicDBObject query1 = new BasicDBObject().append("sta", new BasicDBObject(Constant.MONGO_IN, ilist))
                .append("isr", 0); // 未删除
        if(subjectId != null && !subjectId.equals("")){
            query1.append("sid",new ObjectId(subjectId));
        }
        
        query1.append("rid",new BasicDBObject(Constant.MONGO_IN, communityIds));
        
        if (timeStart != null && timeStart != 0l) {
            query1.append("ctm", new BasicDBObject(Constant.MONGO_GTE, timeStart));
        }
        values.add(query1);
        BasicDBObject query2 = new BasicDBObject();
        if (timeEnd != null && timeEnd != 0l) {
            query2.append("ctm", new BasicDBObject(Constant.MONGO_LT, timeEnd));
        }
        values.add(query2);
        query.put(Constant.MONGO_AND, values);
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_COMMENT,
                        query);
        return count;
    }
    
    public List<AppCommentEntry> getWebAllDatePageByTime(List<ObjectId> communityIds,String subjectId, Long timeStart,Long timeEnd) {
        List<Integer> ilist = new ArrayList<Integer>();
        ilist.add(1);
        ilist.add(0);
        BasicDBObject query = new BasicDBObject();
        BasicDBList values = new BasicDBList();
        BasicDBObject query1 = new BasicDBObject().append("sta", new BasicDBObject(Constant.MONGO_IN, ilist))
                .append("isr", 0); // 未删除
        if(subjectId != null && !subjectId.equals("")){
            query1.append("sid",new ObjectId(subjectId));
        }
        
        query1.append("rid",new BasicDBObject(Constant.MONGO_IN, communityIds));
        
        if (timeStart != null && timeStart != 0l) {
            query1.append("ctm", new BasicDBObject(Constant.MONGO_GTE, timeStart));
        }
        values.add(query1);
        BasicDBObject query2 = new BasicDBObject();
        if (timeEnd != null && timeEnd != 0l) {
            query2.append("ctm", new BasicDBObject(Constant.MONGO_LT, timeEnd));
        }
        values.add(query2);
        query.put(Constant.MONGO_AND, values);
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_COMMENT,
                        query);
        List<AppCommentEntry> entryList = new ArrayList<AppCommentEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppCommentEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
    

    
    public List<AppCommentEntry> getWebAllDatePageByTimePage(List<ObjectId> communityIds,String subjectId, Long timeStart,Long timeEnd,int page,int pageSize) {
        List<Integer> ilist = new ArrayList<Integer>();
        ilist.add(1);
        ilist.add(0);
        BasicDBObject query = new BasicDBObject();
        BasicDBList values = new BasicDBList();
        BasicDBObject query1 = new BasicDBObject().append("sta", new BasicDBObject(Constant.MONGO_IN, ilist))
                .append("isr", 0); // 未删除
        if(subjectId != null && !subjectId.equals("")){
            query1.append("sid",new ObjectId(subjectId));
        }
        
        query1.append("rid",new BasicDBObject(Constant.MONGO_IN, communityIds));
        
        if (timeStart != null && timeStart != 0l) {
            query1.append("ctm", new BasicDBObject(Constant.MONGO_GTE, timeStart));
        }
        values.add(query1);
        BasicDBObject query2 = new BasicDBObject();
        if (timeEnd != null && timeEnd != 0l) {
            query2.append("ctm", new BasicDBObject(Constant.MONGO_LT, timeEnd));
        }
        values.add(query2);
        query.put(Constant.MONGO_AND, values);
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_COMMENT,
                        query,Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC,(page - 1) * pageSize, pageSize);
        List<AppCommentEntry> entryList = new ArrayList<AppCommentEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppCommentEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
    
    public List<AppCommentEntry> getWebAllDatePageByTimePage(List<ObjectId> communityIds,String subjectId, String aid,Long timeStart,Long timeEnd,int page,int pageSize) {
        List<Integer> ilist = new ArrayList<Integer>();
        ilist.add(1);
        ilist.add(0);
        BasicDBObject query = new BasicDBObject();
        BasicDBList values = new BasicDBList();
        BasicDBObject query1 = new BasicDBObject().append("sta", new BasicDBObject(Constant.MONGO_IN, ilist))
                .append("isr", 0); // 未删除
        if(subjectId != null && !subjectId.equals("")){
            query1.append("sid",new ObjectId(subjectId));
        }
        
        query1.append("rid",new BasicDBObject(Constant.MONGO_IN, communityIds));
        
        if (timeStart != null && timeStart != 0l) {
            query1.append("ctm", new BasicDBObject(Constant.MONGO_GTE, timeStart));
        }
        values.add(query1);
        BasicDBObject query2 = new BasicDBObject();
        if (timeEnd != null && timeEnd != 0l) {
            query2.append("ctm", new BasicDBObject(Constant.MONGO_LT, timeEnd));
        }
        values.add(query2);
        query.put(Constant.MONGO_AND, values);
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_COMMENT,
                        query,Constant.FIELDS,
                        new BasicDBObject("ctm", -1),(page - 1) * pageSize, pageSize);
        List<AppCommentEntry> entryList = new ArrayList<AppCommentEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppCommentEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
    
    /**
     * 根据用户名和学科分组查询作业数量
     *
     * @param kws
     * @param level
     * @return result like this:[{"_id":1,"count":100}]
     */
    public List<BasicDBObject> count(List<ObjectId> communityIds,String subjectId, Long timeStart,Long timeEnd,int page,int pageSize) {
        List<Integer> ilist = new ArrayList<Integer>();
        ilist.add(1);
        ilist.add(0);
        BasicDBObject query = new BasicDBObject();
        BasicDBList values = new BasicDBList();
        BasicDBObject query1 = new BasicDBObject().append("sta", new BasicDBObject(Constant.MONGO_IN, ilist))
                .append("isr", 0); // 未删除
        if(subjectId != null && !subjectId.equals("")){
            query1.append("sid",new ObjectId(subjectId));
        }
        
        query1.append("rid",new BasicDBObject(Constant.MONGO_IN, communityIds));
        
        if (timeStart != null && timeStart != 0l) {
            query1.append("ctm", new BasicDBObject(Constant.MONGO_GTE, timeStart));
        }
        values.add(query1);
        BasicDBObject query2 = new BasicDBObject();
        if (timeEnd != null && timeEnd != 0l) {
            query2.append("ctm", new BasicDBObject(Constant.MONGO_LT, timeEnd));
        }
        values.add(query2);
        query.put(Constant.MONGO_AND, values);
        DBObject query4 = new BasicDBObject(Constant.MONGO_MATCH, query);
        BasicDBObject group = new BasicDBObject();
        group.put("aid", "$aid");
        group.put("sid", "$sid");
        BasicDBObject select = new BasicDBObject("_id", group);
        select.put("count", new BasicDBObject("$sum", 1));
        DBObject groupP = new BasicDBObject("$group", select);
        //DBObject group = new BasicDBObject(Constant.MONGO_GROUP, new BasicDBObject("aid", "$aid").append("count", new BasicDBObject(Constant.MONGO_SUM, 1)));
        AggregationOutput output;
        List<BasicDBObject> retList = new ArrayList<BasicDBObject>();
        try {
            output = aggregate(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_COMMENT, query4, groupP);
            Iterator<DBObject> iter = output.results().iterator();
            BasicDBObject dbob;
            while (iter.hasNext()) {
                dbob = (BasicDBObject) iter.next();
                retList.add(dbob);
            }
        } catch (Exception e) {

        }
        return retList;
    }
    
    /**
     * 根据用户名和学科分组查询作业数量
     *
     * @param kws
     * @param level
     * @return result like this:[{"_id":1,"count":100}]
     */
    public List<BasicDBObject> count1(List<ObjectId> communityIds,String subjectId, Long timeStart,Long timeEnd,int page,int pageSize) {
        List<Integer> ilist = new ArrayList<Integer>();
        ilist.add(1);
        ilist.add(0);
        BasicDBObject query = new BasicDBObject();
        BasicDBList values = new BasicDBList();
        BasicDBObject query1 = new BasicDBObject().append("sta", new BasicDBObject(Constant.MONGO_IN, ilist))
                .append("isr", 0); // 未删除
        if(subjectId != null && !subjectId.equals("")){
            query1.append("sid",new ObjectId(subjectId));
        }
        
        query1.append("rid",new BasicDBObject(Constant.MONGO_IN, communityIds));
        
        if (timeStart != null && timeStart != 0l) {
            query1.append("ctm", new BasicDBObject(Constant.MONGO_GTE, timeStart));
        }
        values.add(query1);
        BasicDBObject query2 = new BasicDBObject();
        if (timeEnd != null && timeEnd != 0l) {
            query2.append("ctm", new BasicDBObject(Constant.MONGO_LT, timeEnd));
        }
        values.add(query2);
        query.put(Constant.MONGO_AND, values);
        DBObject query4 = new BasicDBObject(Constant.MONGO_MATCH, query);
        BasicDBObject group = new BasicDBObject();
        group.put("rid", "$rid");
        BasicDBObject select = new BasicDBObject("_id", group);
        select.put("count", new BasicDBObject("$sum", 1));
        DBObject groupP = new BasicDBObject("$group", select);
        //DBObject group = new BasicDBObject(Constant.MONGO_GROUP, new BasicDBObject("aid", "$aid").append("count", new BasicDBObject(Constant.MONGO_SUM, 1)));
        AggregationOutput output;
        List<BasicDBObject> retList = new ArrayList<BasicDBObject>();
        try {
            output = aggregate(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_COMMENT, query4, groupP);
            Iterator<DBObject> iter = output.results().iterator();
            BasicDBObject dbob;
            while (iter.hasNext()) {
                dbob = (BasicDBObject) iter.next();
                retList.add(dbob);
            }
        } catch (Exception e) {

        }
        return retList;
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
                .append("aid", new BasicDBObject(Constant.MONGO_NE, adminId))
                .append("sta", new BasicDBObject(Constant.MONGO_IN,ilist))
                .append("isr", 0); // 未删除
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
    public int getNewPageNumber(List<ObjectId> userIds) {
        List<Integer> ilist = new ArrayList<Integer>();
        ilist.add(1);
        ilist.add(0);
        BasicDBObject query = new BasicDBObject()
                .append("rid",new BasicDBObject(Constant.MONGO_IN,userIds))
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
