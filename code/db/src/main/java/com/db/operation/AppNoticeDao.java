package com.db.operation;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.appnotice.AppNoticeEntry;
import com.pojo.operation.AppCommentEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by scott on 2017/9/22.
 */
public class AppNoticeDao extends BaseDao{

    /**
     * 保存
     * @param entry
     */
    public ObjectId saveAppNoticeEntry(AppNoticeEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,entry.getBaseEntry());
        return entry.getID();
    }

    public void removeAppNoticeEntry(ObjectId noticeId){
        BasicDBObject query=new BasicDBObject(Constant.ID,noticeId);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("ir",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,query,updateValue);
    }

    /**
     * 保存几条信息
     * @param entries
     */
    public void saveAppNoticeEntries(List<AppNoticeEntry> entries){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE, MongoUtils.fetchDBObjectList(entries));
    }

    public int countMyAppNotices(String title, ObjectId communityId,
                                 ObjectId subjectId,
                                 List<ObjectId> groupIds,
                                 ObjectId userId){
        BasicDBObject query=getMyAppNoticesConditionSec(title,communityId,subjectId,groupIds,userId);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,query);
    }

    public List<AppNoticeEntry> getMyAppNotices(String title, ObjectId communityId,
                                                ObjectId subjectId,
                                                List<ObjectId> groupIds,
                                                ObjectId userId,
                                                int page,
                                                int pageSize){
        List<AppNoticeEntry> entries=new ArrayList<AppNoticeEntry>();
        BasicDBObject query=getMyAppNoticesConditionSec(title,communityId,subjectId,groupIds,userId);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,query,
                Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new AppNoticeEntry(dbObject));
            }
        }
        return entries;
    }
    
    public List<AppNoticeEntry> getAppNoticesByCmId(ObjectId communityId, Long timeStart, Long timeEnd
                                                ){
        List<AppNoticeEntry> entries=new ArrayList<AppNoticeEntry>();
        BasicDBList values = new BasicDBList();
        BasicDBObject query = new BasicDBObject();
        BasicDBObject query1= new BasicDBObject("cmId", communityId).append("ir",Constant.ZERO);
        if (timeStart != null && timeStart != 0l) {
            query1.append("ti", new BasicDBObject(Constant.MONGO_GTE, timeStart));
        }
        
        BasicDBObject query2 = new BasicDBObject();
        if (timeEnd != null && timeEnd != 0l) {
            query2.append("ti", new BasicDBObject(Constant.MONGO_LT, timeEnd));
        }
        values.add(query1);
        values.add(query2);
        query.put(Constant.MONGO_AND, values);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,query,
                Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new AppNoticeEntry(dbObject));
            }
        }
        return entries;
    }
    
    public List<AppNoticeEntry> getAppNoticesByUId(ObjectId communityId,ObjectId subjectId, Long timeStart, Long timeEnd
        ){
        List<AppNoticeEntry> entries=new ArrayList<AppNoticeEntry>();
        BasicDBList values = new BasicDBList();
        BasicDBObject query = new BasicDBObject();
        BasicDBObject query1= new BasicDBObject("uid", communityId).append("sid",subjectId).append("ir",Constant.ZERO);
        if (timeStart != null && timeStart != 0l) {
            query1.append("ti", new BasicDBObject(Constant.MONGO_GTE, timeStart));
        }
        
        BasicDBObject query2 = new BasicDBObject();
        if (timeEnd != null && timeEnd != 0l) {
            query2.append("ti", new BasicDBObject(Constant.MONGO_LT, timeEnd));
        }
        values.add(query1);
        values.add(query2);
        query.put(Constant.MONGO_AND, values);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,query,
                Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new AppNoticeEntry(dbObject));
            }
        }
        return entries;
}

    public List<AppNoticeEntry> getMyAppNoticeList(int page,int pageSize){
        List<AppNoticeEntry> entries=new ArrayList<AppNoticeEntry>();
        BasicDBObject query=new BasicDBObject();
        query.append("ir",Constant.ZERO);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,query,
                Constant.FIELDS,Constant.MONGO_SORTBY_ASC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new AppNoticeEntry(dbObject));
            }
        }
        return entries;
    }
    
    public BasicDBObject getMyAppNoticesConditionSec(String title, ObjectId communityId,
                                                  ObjectId subjectId,
                                                  List<ObjectId> groupIds,
                                                  ObjectId userId){
        BasicDBObject query=new BasicDBObject()
                .append("ir",Constant.ZERO);
        if(StringUtils.isNotBlank(title)){
            Pattern pattern = Pattern.compile("^.*" + title + ".*$", Pattern.CASE_INSENSITIVE);
            query.append("tl",new BasicDBObject(Constant.MONGO_REGEX, pattern));
        }
        if(null!=communityId){
            query.append("cmId",communityId);
        }
        if(null!=subjectId){
            query.append("sid",subjectId);
        }
        BasicDBList values = new BasicDBList();
        BasicDBObject query1=new BasicDBObject("uid",userId);
        values.add(query1);
        List<Integer> watchPermissions=new ArrayList<Integer>();
        watchPermissions.add(Constant.ONE);
        watchPermissions.add(Constant.THREE);
        BasicDBObject query2=new BasicDBObject()
                .append("uid",new BasicDBObject(Constant.MONGO_NE,userId))
                .append("wp",new BasicDBObject(Constant.MONGO_IN,watchPermissions));
        if(null==communityId){
            query2.append("gi",new BasicDBObject(Constant.MONGO_IN,groupIds));
        }
        values.add(query2);
        query.put(Constant.MONGO_OR,values);
        return query;
    }


    public BasicDBObject getMyAppNoticesCondition(ObjectId communityId,
                                                  ObjectId subjectId,
                                                  List<ObjectId> groupIds,
                                                  ObjectId userId){
        BasicDBObject query=new BasicDBObject()
                .append("ir",Constant.ZERO);
        if(null!=communityId){
            query.append("cmId",communityId);
        }
        if(null!=subjectId){
            query.append("sid",subjectId);
        }
        BasicDBList values = new BasicDBList();
        BasicDBObject query1=new BasicDBObject("uid",userId);
        values.add(query1);
        List<Integer> watchPermissions=new ArrayList<Integer>();
        watchPermissions.add(Constant.ONE);
        watchPermissions.add(Constant.THREE);
        BasicDBObject query2=new BasicDBObject()
                .append("uid",new BasicDBObject(Constant.MONGO_NE,userId))
                .append("wp",new BasicDBObject(Constant.MONGO_IN,watchPermissions));
        if(null==communityId){
            query2.append("gi",new BasicDBObject(Constant.MONGO_IN,groupIds));
        }
        values.add(query2);
        query.put(Constant.MONGO_OR,values);
        return query;
    }


    public List<AppNoticeEntry> getMySendAppNoticeEntries(String title, ObjectId communityId,
                                                          ObjectId subjectId,
                                                          ObjectId userId,int page,int pageSize){
        List<AppNoticeEntry> entries=new ArrayList<AppNoticeEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("uid",userId)
                .append("ir",Constant.ZERO);
        if(StringUtils.isNotBlank(title)){
            Pattern pattern = Pattern.compile("^.*" + title + ".*$", Pattern.CASE_INSENSITIVE);
            query.append("tl",new BasicDBObject(Constant.MONGO_REGEX, pattern));
        }
        if(null!=communityId){
            query.append("cmId",communityId);
        }
        if(null!=subjectId){
            query.append("sid",subjectId);
        }
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,query,
                Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new AppNoticeEntry(dbObject));
            }
        }
        return entries;
    }

    /**
     * 查询我已发送的通知
     * @param userId
     * @return
     */
    public List<AppNoticeEntry> getMySendAppNoticeEntries(ObjectId userId,int page,int pageSize){
        List<AppNoticeEntry> entries=new ArrayList<AppNoticeEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("uid",userId)
                .append("ir",Constant.ZERO);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,query,
                Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new AppNoticeEntry(dbObject));
            }
        }
        return entries;
    }

    public int countMySendAppNoticeEntries(ObjectId userId){
        BasicDBObject query=new BasicDBObject()
                .append("uid",userId)
                .append("ir",Constant.ZERO);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,query);
    }

    public int countMySendAppNoticeEntries(
                                           String title,
               ObjectId communityId,
               ObjectId subjectId,
               ObjectId userId){
        BasicDBObject query=new BasicDBObject()
                .append("uid",userId)
                .append("ir",Constant.ZERO);
        if(StringUtils.isNotBlank(title)){
            Pattern pattern = Pattern.compile("^.*" + title + ".*$", Pattern.CASE_INSENSITIVE);
            query.append("tl",new BasicDBObject(Constant.MONGO_REGEX, pattern));
        }
        if(null!=communityId){
            query.append("cmId",communityId);
        }
        if(null!=subjectId){
            query.append("sid",subjectId);
        }
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,query);
    }


    public int countMyReceivedAppNoticeEntries(List<ObjectId> groupIds, ObjectId userId){
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,
                getMyReceivedAppNoticeQueryCondition(groupIds, userId));
    }

    public int countMyReceivedAppNoticeEntries(String title, ObjectId communityId,ObjectId subjectId,List<ObjectId> groupIds, ObjectId userId){
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,
                getMyReceivedAppNoticeQueryConditionSec(title,communityId,subjectId,groupIds, userId));
    }
    
    public int countAppNoticeEntries(List<ObjectId> communityIds,Long startTime, Long endTime){
        BasicDBObject query = new BasicDBObject();
        BasicDBObject query1 = new BasicDBObject();
        BasicDBObject query2 = new BasicDBObject();
        query1.append("ir",Constant.ZERO).append("cmId",new BasicDBObject(Constant.MONGO_IN, communityIds));
        BasicDBList values = new BasicDBList();
        if (startTime != null && startTime != 0l) {
            query1.append("ti", new BasicDBObject(Constant.MONGO_GTE, startTime));
        }
        if (endTime != null && endTime != 0l) {
            query2.append("ti", new BasicDBObject(Constant.MONGO_LT, endTime));
        }
        values.add(query1);
        values.add(query2);
        query.put(Constant.MONGO_AND, values);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,
            query);
    }
    
    /**
     * 根据用户名和学科分组查询作业数量
     *
     * @param kws
     * @param level
     * @return result like this:[{"_id":1,"count":100}]
     */
    public List<BasicDBObject> count(List<ObjectId> communityIds,String subjectId, Long timeStart,Long timeEnd,int page,int pageSize) {
   
        BasicDBObject query = new BasicDBObject();
        BasicDBList values = new BasicDBList();
        BasicDBObject query1 = new BasicDBObject().append("ir", 0); // 未删除
        if(subjectId != null && !subjectId.equals("")){
            query1.append("sid",new ObjectId(subjectId));
        }
        
        query1.append("cmId",new BasicDBObject(Constant.MONGO_IN, communityIds));
        
        if (timeStart != null && timeStart != 0l) {
            query1.append("ti", new BasicDBObject(Constant.MONGO_GTE, timeStart));
        }
        values.add(query1);
        BasicDBObject query2 = new BasicDBObject();
        if (timeEnd != null && timeEnd != 0l) {
            query2.append("ti", new BasicDBObject(Constant.MONGO_LT, timeEnd));
        }
        values.add(query2);
        query.put(Constant.MONGO_AND, values);
        DBObject query4 = new BasicDBObject(Constant.MONGO_MATCH, query);
        BasicDBObject group = new BasicDBObject();
        group.put("uid", "$uid");
        group.put("sid", "$sid");
        BasicDBObject select = new BasicDBObject("_id", group);
        select.put("count", new BasicDBObject("$sum", 1));
        DBObject groupP = new BasicDBObject("$group", select);
        //DBObject group = new BasicDBObject(Constant.MONGO_GROUP, new BasicDBObject("aid", "$aid").append("count", new BasicDBObject(Constant.MONGO_SUM, 1)));
        AggregationOutput output;
        List<BasicDBObject> retList = new ArrayList<BasicDBObject>();
        try {
            output = aggregate(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE, query4, groupP);
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
     * 根据班级分组查询作业数量
     *
     * @param kws
     * @param level
     * @return result like this:[{"_id":1,"count":100}]
     */
    public List<BasicDBObject> count1(List<ObjectId> communityIds,String subjectId, Long timeStart,Long timeEnd,int page,int pageSize) {

        BasicDBObject query = new BasicDBObject();
        BasicDBList values = new BasicDBList();
        BasicDBObject query1 = new BasicDBObject().append("ir", 0); // 未删除
        if(subjectId != null && !subjectId.equals("")){
            query1.append("sid",new ObjectId(subjectId));
        }
        
        query1.append("cmId",new BasicDBObject(Constant.MONGO_IN, communityIds));
        
        if (timeStart != null && timeStart != 0l) {
            query1.append("ti", new BasicDBObject(Constant.MONGO_GTE, timeStart));
        }
        values.add(query1);
        BasicDBObject query2 = new BasicDBObject();
        if (timeEnd != null && timeEnd != 0l) {
            query2.append("ti", new BasicDBObject(Constant.MONGO_LT, timeEnd));
        }
        values.add(query2);
        query.put(Constant.MONGO_AND, values);
        DBObject query4 = new BasicDBObject(Constant.MONGO_MATCH, query);
        BasicDBObject group = new BasicDBObject();
        group.put("cmId", "$cmId");
        BasicDBObject select = new BasicDBObject("_id", group);
        select.put("count", new BasicDBObject("$sum", 1));
        DBObject groupP = new BasicDBObject("$group", select);
        //DBObject group = new BasicDBObject(Constant.MONGO_GROUP, new BasicDBObject("aid", "$aid").append("count", new BasicDBObject(Constant.MONGO_SUM, 1)));
        AggregationOutput output;
        List<BasicDBObject> retList = new ArrayList<BasicDBObject>();
        try {
            output = aggregate(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE, query4, groupP);
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
    
    public List<AppNoticeEntry> getWebAllDatePageByTimePage(ObjectId communityIds,String subjectId, String aid,Long timeStart,Long timeEnd,int page,int pageSize) {
        BasicDBObject query = new BasicDBObject();
        BasicDBList values = new BasicDBList();
        BasicDBObject query1 = new BasicDBObject().append("ir", 0); // 未删除
        if(subjectId != null && !subjectId.equals("")){
            query1.append("sid",new ObjectId(subjectId));
        }
        
        query1.append("cmId",communityIds);
        
        if (timeStart != null && timeStart != 0l) {
            query1.append("ti", new BasicDBObject(Constant.MONGO_GTE, timeStart));
        }
        values.add(query1);
        BasicDBObject query2 = new BasicDBObject();
        if (timeEnd != null && timeEnd != 0l) {
            query2.append("ti", new BasicDBObject(Constant.MONGO_LT, timeEnd));
        }
        values.add(query2);
        query.put(Constant.MONGO_AND, values);
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_NEW_VERSION_APP_NOTICE,
                        query,Constant.FIELDS,
                        new BasicDBObject("ti", -1),(page - 1) * pageSize, pageSize);
        List<AppNoticeEntry> entryList = new ArrayList<AppNoticeEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppNoticeEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
    
    public List<AppNoticeEntry> getWebAllDatePageByTimePage(List<ObjectId> communityIds,String subjectId, String aid,Long timeStart,Long timeEnd,int page,int pageSize) {
  
        BasicDBObject query = new BasicDBObject();
        BasicDBList values = new BasicDBList();
        BasicDBObject query1 = new BasicDBObject().append("ir", 0); // 未删除
        if(subjectId != null && !subjectId.equals("")){
            query1.append("sid",new ObjectId(subjectId));
        }
        if (StringUtils.isNotBlank(aid)) {
            query1.append("uid", new ObjectId(aid));
        }
        
        query1.append("cmId",new BasicDBObject(Constant.MONGO_IN, communityIds));
        
        if (timeStart != null && timeStart != 0l) {
            query1.append("ti", new BasicDBObject(Constant.MONGO_GTE, timeStart));
        }
        values.add(query1);
        BasicDBObject query2 = new BasicDBObject();
        if (timeEnd != null && timeEnd != 0l) {
            query2.append("ti", new BasicDBObject(Constant.MONGO_LT, timeEnd));
        }
        values.add(query2);
        query.put(Constant.MONGO_AND, values);
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_NEW_VERSION_APP_NOTICE,
                        query,Constant.FIELDS,
                        new BasicDBObject("ti", -1),(page - 1) * pageSize, pageSize);
        List<AppNoticeEntry> entryList = new ArrayList<AppNoticeEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppNoticeEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }


    public BasicDBObject getMyReceivedAppNoticeQueryCondition(List<ObjectId> groupIds, ObjectId userId){
        List<Integer> watchPermissions=new ArrayList<Integer>();
        watchPermissions.add(Constant.ONE);
        watchPermissions.add(Constant.THREE);
        BasicDBObject query=new BasicDBObject()
                .append("gi",new BasicDBObject(Constant.MONGO_IN,groupIds))
                .append("uid",new BasicDBObject(Constant.MONGO_NE,userId))
                .append("wp",new BasicDBObject(Constant.MONGO_IN,watchPermissions))
                .append("ir",Constant.ZERO);
        return query;
    }

    public BasicDBObject getMyReceivedAppNoticeQueryCondition(
            ObjectId communityId,
            ObjectId subjectId,
            List<ObjectId> groupIds, ObjectId userId){
        List<Integer> watchPermissions=new ArrayList<Integer>();
        watchPermissions.add(Constant.ONE);
        watchPermissions.add(Constant.THREE);
        BasicDBObject query=new BasicDBObject()
                .append("uid",new BasicDBObject(Constant.MONGO_NE,userId))
                .append("wp",new BasicDBObject(Constant.MONGO_IN,watchPermissions))
                .append("ir",Constant.ZERO);
        if(null==communityId){
            query.append("gi",new BasicDBObject(Constant.MONGO_IN,groupIds));
        }else{
            query.append("cmId",communityId);
        }
        if(null!=subjectId){
            query.append("sid",subjectId);
        }
        return query;
    }

    public List<AppNoticeEntry> getMyReceivedAppNoticeEntries(
            String title,
            ObjectId communityId,
            ObjectId subjectId,
            List<ObjectId> groupIds,int page,int pageSize,
            ObjectId userId){
        List<AppNoticeEntry> entries=new ArrayList<AppNoticeEntry>();
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,
                getMyReceivedAppNoticeQueryConditionSec(title, communityId,subjectId,groupIds, userId),
                Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new AppNoticeEntry(dbObject));
            }
        }
        return entries;
    }
    
    public BasicDBObject getMyReceivedAppNoticeQueryConditionSec(
                                                                 String title,
          ObjectId communityId,
          ObjectId subjectId,
          List<ObjectId> groupIds, ObjectId userId){
      List<Integer> watchPermissions=new ArrayList<Integer>();
      watchPermissions.add(Constant.ONE);
      watchPermissions.add(Constant.THREE);
      BasicDBObject query=new BasicDBObject()
              .append("uid",new BasicDBObject(Constant.MONGO_NE,userId))
              .append("wp",new BasicDBObject(Constant.MONGO_IN,watchPermissions))
              .append("ir",Constant.ZERO);
      if(StringUtils.isNotBlank(title)){
          Pattern pattern = Pattern.compile("^.*" + title + ".*$", Pattern.CASE_INSENSITIVE);
          query.append("tl",new BasicDBObject(Constant.MONGO_REGEX, pattern));
      }
      if(null==communityId){
          query.append("gi",new BasicDBObject(Constant.MONGO_IN,groupIds));
      }else{
          query.append("cmId",communityId);
      }
      if(null!=subjectId){
          query.append("sid",subjectId);
      }
      return query;
  }

    /**
     * 获取我接收到的通知
     * @param groupIds
     * @return
     */
    public List<AppNoticeEntry> getMyReceivedAppNoticeEntries(List<ObjectId> groupIds,int page,int pageSize,
            ObjectId userId){
        List<AppNoticeEntry> entries=new ArrayList<AppNoticeEntry>();
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,
                getMyReceivedAppNoticeQueryCondition(groupIds, userId),
                Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new AppNoticeEntry(dbObject));
            }
        }
        return entries;
    }

    /**
     * 获取我接收到的通知
     * @param communityIds
     * @return
     */
    public List<AppNoticeEntry> getRoleList(List<ObjectId> communityIds,int page,int pageSize,
                                                              String userName){
        BasicDBObject query=new BasicDBObject()
                .append("cmId", new BasicDBObject(Constant.MONGO_IN, communityIds))
                .append("wp", Constant.ONE)
                .append("ir",Constant.ZERO);
        if(userName!=null && !userName.equals("")){
             query.append("un",userName);
        }
        List<AppNoticeEntry> entries=new ArrayList<AppNoticeEntry>();

        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,
                query,
                Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new AppNoticeEntry(dbObject));
            }
        }
        return entries;
    }

    public int countRoleList(List<ObjectId> communityIds,String userName){
        BasicDBObject query=new BasicDBObject()
                .append("cmId", new BasicDBObject(Constant.MONGO_IN, communityIds))
                .append("wp", Constant.ONE)
                .append("ir",Constant.ZERO);
        if(userName!=null && !userName.equals("")){
            query.append("un",userName);
        }
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,
                query);
    }
    /**
     * 根据idlist查询通知信息
     * @return
     */
    public List<AppNoticeEntry> getAppNoticeEntriesByIds(List<ObjectId> ids){
        List<AppNoticeEntry> entries=new ArrayList<AppNoticeEntry>();
        BasicDBObject query=new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_IN,ids))
                .append("ir", Constant.ZERO);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,query,
                Constant.FIELDS,Constant.MONGO_SORTBY_DESC);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new AppNoticeEntry(dbObject));
            }
        }
        return entries;
    }

    /**
     * 已阅读
     * @param userId
     * @param id
     */
    public void pushReadList(ObjectId userId,ObjectId id){
        BasicDBObject query=new BasicDBObject()
                .append(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_PUSH,new BasicDBObject("rl",userId));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,query,updateValue);
    }

    public void updateCommentCount(ObjectId id){
        BasicDBObject query=new BasicDBObject()
                .append(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_INC,new BasicDBObject("cc",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,query,updateValue);
    }


    /**
     * 查询entry
     * @param id
     */
    public AppNoticeEntry getAppNoticeEntry(ObjectId id){
        BasicDBObject query=new BasicDBObject()
                .append(Constant.ID,id);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,query,Constant.FIELDS);
        if(null!=dbObject){
            return new AppNoticeEntry(dbObject);
        }else{
            return null;
        }
    }
    public void updEntry(AppNoticeEntry e) {
        BasicDBObject query=new BasicDBObject(Constant.ID,e.getID());
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,e.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE, query,updateValue);
    }

    public int countMyReceivedAppNoticeEntriesForStudent(List<ObjectId> groupIds){
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,
                getQueryMyReceivedAppNoticeEntriesForStudentCondition(groupIds));
    }

    public BasicDBObject getQueryMyReceivedAppNoticeEntriesForStudentCondition(List<ObjectId> groupIds){
        List<Integer> watchPermissions=new ArrayList<Integer>();
        watchPermissions.add(Constant.TWO);
        watchPermissions.add(Constant.THREE);
        BasicDBObject query=new BasicDBObject()
                .append("gi",new BasicDBObject(Constant.MONGO_IN,groupIds))
                .append("wp",new BasicDBObject(Constant.MONGO_IN,watchPermissions))
                .append("ir",Constant.ZERO);
        return query;
    }


    public List<AppNoticeEntry> getMyReceivedAppNoticeEntriesForStudent(List<ObjectId> groupIds,int page,int pageSize){
        List<AppNoticeEntry> entries=new ArrayList<AppNoticeEntry>();

        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,
                getQueryMyReceivedAppNoticeEntriesForStudentCondition(groupIds),
                Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new AppNoticeEntry(dbObject));
            }
        }
        return entries;
    }



    public List<AppNoticeEntry> searchAppNotice(String keyWord,int page,int pageSize){
        List<AppNoticeEntry> entries=new ArrayList<AppNoticeEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("ir",Constant.ZERO);
        BasicDBList list = new BasicDBList();
        if(StringUtils.isNotBlank(keyWord)){
            Pattern pattern = Pattern.compile("^.*" + keyWord + ".*$", Pattern.CASE_INSENSITIVE);
            list.add(new BasicDBObject().append("gn", new BasicDBObject(Constant.MONGO_REGEX, pattern)));
            list.add(new BasicDBObject().append("un", new BasicDBObject(Constant.MONGO_REGEX, pattern)));
            list.add(new BasicDBObject().append("su", new BasicDBObject(Constant.MONGO_REGEX, pattern)));
        }
        query.append(Constant.MONGO_OR, list);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,query,
                Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new AppNoticeEntry(dbObject));
            }
        }
        return entries;
    }

    
    

}
