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
        BasicDBObject query=new BasicDBObject();
        if(type==-1){
            BasicDBList values = new BasicDBList();
            List<Integer> integers = new ArrayList<Integer>();
            integers.add(Constant.ONE);
            integers.add(Constant.TWO);
            BasicDBObject query1=new BasicDBObject("ty",new BasicDBObject(Constant.MONGO_IN,integers))
                    .append("uid",new BasicDBObject(Constant.MONGO_NE,userId));
            if(communityIds.size()>0){
                query1.append("cid",new BasicDBObject(Constant.MONGO_IN,communityIds));
            }
            values.add(query1);
            BasicDBObject query2=new BasicDBObject("ty",Constant.THREE)
                    .append("uid",new BasicDBObject(Constant.MONGO_NE,userId));
            if(receiveIds.size()>0){
                query2.append("rid",new BasicDBObject(Constant.MONGO_IN,receiveIds));
            }
            values.add(query2);
            query.put(Constant.MONGO_OR, values);
        }else{
            query.append("ty",type).append("uid",new BasicDBObject(Constant.MONGO_NE,userId));
            if(type==Constant.ONE||type==Constant.TWO){
                if(communityIds.size()>0){
                    query.append("cid",new BasicDBObject(Constant.MONGO_IN,communityIds));
                }
            }else{
                if(receiveIds.size()>0){
                    query.append("rid",new BasicDBObject(Constant.MONGO_IN,receiveIds));
                }
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
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_WEB_HOME_PAGE_RECORD,
                query,Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new WebHomePageEntry(dbObject));
            }
        }
        return entries;
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
        BasicDBObject query=new BasicDBObject()
                .append("uid",userId);
        if(communityId!=null){
            query.append("cid",communityId);
        }
        if(type!=-1){
            query.append("ty",type);
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
