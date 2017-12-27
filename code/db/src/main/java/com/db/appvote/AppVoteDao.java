package com.db.appvote;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.appvote.AppVoteEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/11/6.
 */
public class AppVoteDao extends BaseDao{

    public void saveAppVote(AppVoteEntry appVoteEntry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_VOTE,appVoteEntry.getBaseEntry());
    }

    public void saveEntries(List<AppVoteEntry> entries){
        save(MongoFacroty.getAppDB(),Constant.COLLECTION_NEW_VERSION_APP_VOTE, MongoUtils.fetchDBObjectList(entries));
    }


    public BasicDBObject getStudentReceivedCondition(List<ObjectId> groupIds){
        List<Integer> visiblePermission=new ArrayList<Integer>();
        visiblePermission.add(Constant.TWO);
        visiblePermission.add(Constant.THREE);
        BasicDBObject query=new BasicDBObject()
                .append("gid",new BasicDBObject(Constant.MONGO_IN,groupIds))
                .append("vp",new BasicDBObject(Constant.MONGO_IN,visiblePermission))
                .append("ir", Constant.ZERO);
        return query;
    }

    public int countStudentReceivedEntries(List<ObjectId> groupIds){
        BasicDBObject query=getStudentReceivedCondition(groupIds);
        return count(MongoFacroty.getAppDB(),Constant.COLLECTION_NEW_VERSION_APP_VOTE,query);
    }

    public List<AppVoteEntry> getStudentReceivedEntries(List<ObjectId> groupIds,int page,int pageSize){
        List<AppVoteEntry> entries=new ArrayList<AppVoteEntry>();
        BasicDBObject query=getStudentReceivedCondition(groupIds);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(),Constant.COLLECTION_NEW_VERSION_APP_VOTE,query,
                Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new AppVoteEntry(dbObject));
            }
        }
        return entries;
    }

    public BasicDBObject getReceivedCondition(List<ObjectId> groupIds,
                                              ObjectId userId){
        List<Integer> visiblePermission=new ArrayList<Integer>();
        visiblePermission.add(Constant.ONE);
        visiblePermission.add(Constant.THREE);
        BasicDBObject query=new BasicDBObject()
                .append("uid",new BasicDBObject(Constant.MONGO_NE,userId))
                .append("gid",new BasicDBObject(Constant.MONGO_IN,groupIds))
                .append("vp",new BasicDBObject(Constant.MONGO_IN,visiblePermission))
                .append("ir", Constant.ZERO);
        return query;
    }

    public int countMyReceivedAppVoteEntries(List<ObjectId> groupIds,
                                             ObjectId userId){
        BasicDBObject query=getReceivedCondition(groupIds, userId);
        return count(MongoFacroty.getAppDB(),Constant.COLLECTION_NEW_VERSION_APP_VOTE,query);
    }


    public List<AppVoteEntry> getMyReceivedAppVoteEntries(List<ObjectId> groupIds,
                                                          ObjectId userId,int page,int pageSize){
        List<AppVoteEntry> entries=new ArrayList<AppVoteEntry>();
        BasicDBObject query=getReceivedCondition(groupIds, userId);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(),Constant.COLLECTION_NEW_VERSION_APP_VOTE,query,
                Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new AppVoteEntry(dbObject));
            }
        }
        return entries;
    }

    public int countMySendAppVoteEntries(ObjectId userId){
        BasicDBObject query=new BasicDBObject()
                .append("uid",userId)
                .append("ir", Constant.ZERO);
        return count(MongoFacroty.getAppDB(),Constant.COLLECTION_NEW_VERSION_APP_VOTE,query);
    }


    public BasicDBObject getGatherCondition(List<ObjectId> groupIds,
                                            ObjectId userId){
        List<Integer> visiblePermission=new ArrayList<Integer>();
        visiblePermission.add(Constant.ONE);
        visiblePermission.add(Constant.THREE);
        BasicDBObject query = new BasicDBObject();
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject("uid",userId));
        values.add(new BasicDBObject("uid",new BasicDBObject(Constant.MONGO_NE,userId))
                .append("vp",new BasicDBObject(Constant.MONGO_IN,visiblePermission))
                .append("gid",new BasicDBObject(Constant.MONGO_IN,groupIds)));
        query.append(Constant.MONGO_OR,values);
        return query;
    }

    public int countGatherAppVotes(ObjectId userId,List<ObjectId> groupIds){
        BasicDBObject query=getGatherCondition(groupIds,userId);
        return count(MongoFacroty.getAppDB(),Constant.COLLECTION_NEW_VERSION_APP_VOTE,query);
    }

    public List<AppVoteEntry> getGatherAppVoteEntries(ObjectId userId,List<ObjectId> groupIds,int page,int pageSize){
        List<AppVoteEntry> entries=new ArrayList<AppVoteEntry>();
        BasicDBObject query=getGatherCondition(groupIds,userId);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(),Constant.COLLECTION_NEW_VERSION_APP_VOTE,query,
                Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new AppVoteEntry(dbObject));
            }
        }
        return entries;
    }


    public List<AppVoteEntry> getMySendAppVoteEntries(ObjectId userId,int page,int pageSize){
        List<AppVoteEntry> entries=new ArrayList<AppVoteEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("uid",userId)
                .append("ir", Constant.ZERO);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(),Constant.COLLECTION_NEW_VERSION_APP_VOTE,query,
                Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new AppVoteEntry(dbObject));
            }
        }
        return entries;
    }
}
