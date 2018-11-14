package com.db.appvote;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.appvote.AppVoteOptionEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-10-29.
 */
public class AppVoteOptionDao extends BaseDao {
    public void saveAppVote(AppVoteOptionEntry appVoteOptionEntry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_VOTE_OPTION,appVoteOptionEntry.getBaseEntry());
    }

    public List<AppVoteOptionEntry> getOneVoteList(ObjectId voteId){
        List<AppVoteOptionEntry> entryList=new ArrayList<AppVoteOptionEntry>();
        BasicDBObject query=new BasicDBObject().append("isr", 0).append("vid", voteId).append("sel",Constant.ONE);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_VOTE_OPTION, query,
                Constant.FIELDS, new BasicDBObject("ord",1));
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppVoteOptionEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    public List<AppVoteOptionEntry> getAllOneVoteList(ObjectId voteId){
        List<AppVoteOptionEntry> entryList=new ArrayList<AppVoteOptionEntry>();
        BasicDBObject query=new BasicDBObject().append("isr", 0).append("vid", voteId);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_VOTE_OPTION, query,
                Constant.FIELDS, new BasicDBObject("ord",1));
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppVoteOptionEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    public List<AppVoteOptionEntry> getSelectOneVoteList(ObjectId voteId){
        List<AppVoteOptionEntry> entryList=new ArrayList<AppVoteOptionEntry>();
        BasicDBObject query=new BasicDBObject().append("isr", 0).append("vid",voteId).append("sel",Constant.ONE);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_VOTE_OPTION, query,
                Constant.FIELDS, new BasicDBObject("ord",1));
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppVoteOptionEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    //已成为的选项
    public int countSelectOneVoteList(ObjectId voteId) {
        BasicDBObject query=new BasicDBObject().append("isr", 0).append("vid",voteId).append("sel",Constant.ONE);
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_VOTE_OPTION,
                        query);
        return count;
    }



    //修改已选
    public void updateEntry(ObjectId voteId,List<ObjectId> ids,int select){
        BasicDBObject query = new BasicDBObject().append("isr",Constant.ZERO);
        query.append("vid",voteId);
        query.append(Constant.ID,new BasicDBObject(Constant.MONGO_IN,ids));
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("sel",select));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_VOTE_OPTION, query,updateValue);
    }

    //修改已选
    public void updateOneEntry(ObjectId voteId,ObjectId id,int select,long order){
        BasicDBObject query = new BasicDBObject().append("isr",Constant.ZERO);
        query.append("vid",voteId);
        query.append(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("sel",select).append("ord",order));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_VOTE_OPTION, query,updateValue);
    }


    //修改已选
    public void delEntry(ObjectId id){
        BasicDBObject query = new BasicDBObject().append("isr",Constant.ZERO).append(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isr",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_VOTE_OPTION, query,updateValue);
    }

    //删除投票选项
    public void delAllEntry(ObjectId voteId){
        BasicDBObject query = new BasicDBObject().append("isr",Constant.ZERO).append("vid",voteId);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isr",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_VOTE_OPTION, query,updateValue);
    }

    public AppVoteOptionEntry getEntry(ObjectId id,ObjectId userId){
        BasicDBObject query=new BasicDBObject()
                .append("vid",id).append("uid",userId).append("isr",Constant.ZERO);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_VOTE_OPTION,query,Constant.FIELDS);
        if(null!=dbObject){
            return new AppVoteOptionEntry((BasicDBObject)dbObject);
        }else{
            return null;
        }
    }

    public AppVoteOptionEntry getOneEntry(ObjectId id){
        BasicDBObject query=new BasicDBObject()
                .append(Constant.ID, id).append("isr",Constant.ZERO);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_VOTE_OPTION,query,Constant.FIELDS);
        if(null!=dbObject){
            return new AppVoteOptionEntry((BasicDBObject)dbObject);
        }else{
            return null;
        }
    }

    //选择一个待选项
    public AppVoteOptionEntry getNowOneOption(ObjectId voteId){
        List<AppVoteOptionEntry> entryList=new ArrayList<AppVoteOptionEntry>();
        BasicDBObject query=new BasicDBObject().append("isr", 0).append("vid",voteId).append("sel",Constant.ZERO);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_VOTE_OPTION, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppVoteOptionEntry((BasicDBObject) obj));
            }
        }
        if(entryList.size()>0){
            return entryList.get(0);
        }else{
            return null;
        }
    }


}
