package com.db.appvote;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.appvote.AppNewVoteEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-10-29.
 */
public class AppNewVoteDao extends BaseDao {

    public void saveAppVote(AppNewVoteEntry appNewVoteEntry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_NEW_VOTE,appNewVoteEntry.getBaseEntry());
    }


    public List<AppNewVoteEntry> getVoteList(ObjectId userId,String keyword,List<ObjectId> communityIds,int page,int pageSize,int role){
        List<AppNewVoteEntry> entryList=new ArrayList<AppNewVoteEntry>();
        BasicDBObject query=new BasicDBObject().append("isr", 0);
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject("aty",  role));
        values.add(new BasicDBObject("vtl",  role));
        values.add(new BasicDBObject("uid",  userId));
        query.put(Constant.MONGO_OR, values);
        if(keyword!=null && !keyword.equals("")){
            query.append("tit",MongoUtils.buildRegex(keyword));
        }
        query.append("clt",new BasicDBObject(Constant.MONGO_IN,communityIds));
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_NEW_VOTE, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new AppNewVoteEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    public int countVoteList(ObjectId userId,String keyword,List<ObjectId> communityIds,int role) {
        BasicDBObject query=new BasicDBObject().append("isr", 0);
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject("aty",  role));
        values.add(new BasicDBObject("vtl",  role));
        values.add(new BasicDBObject("uid",  userId));
        query.put(Constant.MONGO_OR, values);
        if(keyword!=null && !keyword.equals("")){
            query.append("tit", MongoUtils.buildRegex(keyword));
        }
        query.append("clt",new BasicDBObject(Constant.MONGO_IN,communityIds));
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_APP_NEW_VOTE,
                        query);
        return count;
    }


    public AppNewVoteEntry getEntry(ObjectId id){
        BasicDBObject query=new BasicDBObject()
                .append(Constant.ID,id).append("isr",Constant.ZERO);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_NEW_VOTE,query,Constant.FIELDS);
        if(null!=dbObject){
            return new AppNewVoteEntry((BasicDBObject)dbObject);
        }else{
            return null;
        }
    }

    //修改已选
    public void delEntry(ObjectId id){
        BasicDBObject query = new BasicDBObject().append("isr",Constant.ZERO).append(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isr",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_NEW_VOTE, query,updateValue);
    }


}
