package com.db.indexPage;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.indexPage.WebHomePageEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/11/17.
 */
public class WebHomePageDao extends BaseDao{

    public void saveWebHomeEntry(WebHomePageEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_WEB_HOME_PAGE_RECORD,entry.getBaseEntry());
    }


    /**
     *
     * @param communityIds
     * @param receiveIds
     * @param type
     * @param subjectId
     * @param startTime
     * @param endTime
     * @param status
     * @param userId
     * @return
     */
    public int countMyReceivedHomePageEntries(List<ObjectId> communityIds,
                                              List<ObjectId> receiveIds,
                                              int type,
                                              ObjectId subjectId,
                                              long startTime,
                                              long endTime,
                                              int status,
                                              ObjectId userId){
        BasicDBObject query=getMyReceivedQueryCondition(communityIds, receiveIds, type, subjectId, startTime, endTime, status, userId);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_WEB_HOME_PAGE_RECORD,query);
    }

    public WebHomePageEntry getWebHomePageEntry(ObjectId groupExamDetailId){
        BasicDBObject query=new BasicDBObject()
                .append("cti",groupExamDetailId)
                .append("ty",Constant.FIVE);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_WEB_HOME_PAGE_RECORD,query,Constant.FIELDS);
        if(null!=dbObject){
            return new WebHomePageEntry(dbObject);
        }else{
            return null;
        }
    }

    public BasicDBObject getMyReceivedQueryCondition(List<ObjectId> communityIds,
                                                     List<ObjectId> receiveIds,
                                                     int type,
                                                     ObjectId subjectId,
                                                     long startTime,
                                                     long endTime,
                                                     int status,
                                                     ObjectId userId){
        BasicDBObject query=new BasicDBObject();
        if(type==-1){
            BasicDBList values = new BasicDBList();
            List<Integer> integers = new ArrayList<Integer>();
            integers.add(Constant.ONE);
            integers.add(Constant.TWO);
            BasicDBObject query1=new BasicDBObject("ty",new BasicDBObject(Constant.MONGO_IN,integers))
                    .append("uid",new BasicDBObject(Constant.MONGO_NE,userId));
            query1.append("cid",new BasicDBObject(Constant.MONGO_IN,communityIds));
            values.add(query1);
            BasicDBObject query2=new BasicDBObject("ty",Constant.THREE)
                    .append("uid",new BasicDBObject(Constant.MONGO_NE,userId));
            query2.append("rid",new BasicDBObject(Constant.MONGO_IN,receiveIds));
            values.add(query2);
            query.put(Constant.MONGO_OR, values);
        }else{
            query.append("ty",type).append("uid",new BasicDBObject(Constant.MONGO_NE,userId));
            if(type==Constant.ONE||type==Constant.TWO){
                query.append("cid",new BasicDBObject(Constant.MONGO_IN,communityIds));
            }else{
                query.append("rid",new BasicDBObject(Constant.MONGO_IN,receiveIds));
            }
        }
        if(subjectId!=null){
            query.append("sid",subjectId);
        }
        if(startTime!=0L&&endTime!=0L){
            query.append("ti",new BasicDBObject(Constant.MONGO_GTE,startTime).append(Constant.MONGO_LTE,endTime));
        }
        if(status!=-1){
            query.append("st",status);
        }
        return query;
    }

    /**
     * 我收到的首页消息
     * @param communityIds
     * @param receiveIds
     * @param type
     * @param subjectId
     * @param startTime
     * @param endTime
     * @param status
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    public List<WebHomePageEntry> getMyReceivedHomePageEntries(
            List<ObjectId> communityIds,
            List<ObjectId> receiveIds,
            int type,
            ObjectId subjectId,
            long startTime,
            long endTime,
            int status,
            ObjectId userId,
            int page,int pageSize
    ){
        List<WebHomePageEntry> entries =new ArrayList<WebHomePageEntry>();
        BasicDBObject query=getMyReceivedQueryCondition(communityIds, receiveIds, type, subjectId, startTime, endTime, status, userId);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_WEB_HOME_PAGE_RECORD,
                query,Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new WebHomePageEntry(dbObject));
            }
        }
        return entries;
    }


    public int countMySendHomePageEntries(ObjectId communityId, int type,
                                          ObjectId subjectId,
                                          long startTime,
                                          long endTime,
                                          int status,
                                          ObjectId userId){
        BasicDBObject query=getMySendQueryCondtion(communityId, type, subjectId, startTime, endTime, status, userId);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_WEB_HOME_PAGE_RECORD,query);
    }


    public BasicDBObject getMySendQueryCondtion(ObjectId communityId, int type,
                                                ObjectId subjectId,
                                                long startTime,
                                                long endTime,
                                                int status,
                                                ObjectId userId){
        BasicDBObject query=new BasicDBObject()
                .append("uid",userId);
        if(communityId!=null){
            query.append("cid",communityId);
        }
        if(type!=-1){
            if(type==2){
                List<Integer> types=new ArrayList<Integer>();
                types.add(Constant.TWO);
                types.add(Constant.FOUR);
                query.append("ty", new BasicDBObject(Constant.MONGO_IN,types));
            }else {
                query.append("ty", type);
            }
        }else{
            List<Integer> types=new ArrayList<Integer>();
            types.add(Constant.ONE);
            types.add(Constant.TWO);
            types.add(Constant.FOUR);
            types.add(Constant.FIVE);
            query.append("ty", new BasicDBObject(Constant.MONGO_IN,types));
        }
        if(subjectId!=null){
            query.append("sid",subjectId);
        }
        if(startTime!=0L&&endTime!=0L){
            query.append("ti",new BasicDBObject(Constant.MONGO_GTE,startTime).append(Constant.MONGO_LTE,endTime));
        }
        if(status!=-1){
            query.append("st",status);
        }
        return query;
    }

    /**
     * 我发出的首页消息
     * @param communityId
     * @param type
     * @param subjectId
     * @param startTime
     * @param endTime
     * @param status
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    public List<WebHomePageEntry> getMySendHomePageEntries(
            ObjectId communityId,
            int type,
            ObjectId subjectId,
            long startTime,
            long endTime,
            int status,
            ObjectId userId,
            int page,int pageSize){
        List<WebHomePageEntry> entries =new ArrayList<WebHomePageEntry>();
        BasicDBObject query=getMySendQueryCondtion(communityId, type, subjectId, startTime, endTime, status, userId);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_WEB_HOME_PAGE_RECORD,
                query,Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new WebHomePageEntry(dbObject));
            }
        }
        return entries;
    }


}
