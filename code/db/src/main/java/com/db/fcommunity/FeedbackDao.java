package com.db.fcommunity;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.fcommunity.FeedbackEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/3/21.
 */
public class FeedbackDao extends BaseDao {

    public void addFeedbackEntry(FeedbackEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FEEDBACK_CONTENT,entry.getBaseEntry());
    }

    public void removeFeedbackInfo(ObjectId id){
        BasicDBObject query=new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("ir",Constant.ONE));
        update(MongoFacroty.getAppDB(),Constant.COLLECTION_FEEDBACK_CONTENT,query,updateValue);
    }

    public List<FeedbackEntry> getEntries(int page,int pageSize){
        List<FeedbackEntry> entries=new ArrayList<FeedbackEntry>();
        BasicDBObject query=new BasicDBObject("ir",Constant.ZERO);
        List<DBObject> dbObjects=find(MongoFacroty.getAppDB(),Constant.COLLECTION_FEEDBACK_CONTENT,query,Constant.FIELDS,
                Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjects&&!dbObjects.isEmpty()){
            for(DBObject dbObject:dbObjects){
                entries.add(new FeedbackEntry(dbObject));
            }
        }
        return entries;
    }

    public int countEntries(){
        BasicDBObject query=new BasicDBObject("ir",Constant.ZERO);
        return count(MongoFacroty.getAppDB(),Constant.COLLECTION_FEEDBACK_CONTENT,query);
    }

}
