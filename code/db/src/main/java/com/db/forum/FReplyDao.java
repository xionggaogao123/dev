package com.db.forum;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.forum.FReplyEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/5/31.
 */
public class FReplyDao extends BaseDao {

    /**
     * 添加回帖
     * @param e
     * @return
     */
    public ObjectId addFReply(FReplyEntry e){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_REPLY, e.getBaseEntry());
        ObjectId postId=e.getPostId();
        BasicDBObject query =new BasicDBObject(Constant.ID,postId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("upt", System.currentTimeMillis()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_POST,query, updateValue);
        return e.getID();
    }


    /**
     * 获取回帖（分页）
     * @param fields
     * @param sort
     * @param postSectionId
     * @param postId
     * @param skip
     * @param limit
     * @return
     */
    public List<FReplyEntry> getFReplyEntries(DBObject fields, DBObject sort,ObjectId postSectionId,ObjectId postId,
                                              ObjectId personId,int skip,int limit){
        List<FReplyEntry> retList=new ArrayList<FReplyEntry>();

        BasicDBObject query = new BasicDBObject("pstid",postSectionId);
        if(postId != null){
            query.append("ptid", postId);
        }
        if(personId != null){
            query.append("psid",personId);
        }
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_REPLY, query, fields, sort, skip, limit);

        for(DBObject dbObject : dbObjectList){
            retList.add(new FReplyEntry((BasicDBObject)dbObject));
        }
        return  retList;
    }

    /**
     * 查帖子数量
     * @param postSectionId,postId
     * @return
     */
    public int  getFReplyEntriesCount(ObjectId postSectionId,ObjectId postId,ObjectId personId){

        BasicDBObject query = new BasicDBObject("pstid",postSectionId);
        if(postId != null){
            query.append("ptid", postId);
        }
        if(personId !=null){
            query.append("psid", postId);
        }
        int count= count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_REPLY, query);

        return  count;
    }


    /**
     * 删除帖子
     * */
    public void removeFReply(ObjectId id){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        remove(MongoFacroty.getAppDB(),Constant.COLLECTION_FORUM_REPLY,query);
    }


    /**
     * 回帖详情
     * @param id
     * @return
     */
    public FReplyEntry getFReplyEntry(ObjectId id){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(),Constant.COLLECTION_FORUM_REPLY,query,Constant.FIELDS);
        if(null!=dbo)
        {
            return new FReplyEntry((BasicDBObject)dbo);
        }
        return null;
    }

    /**
     * 删除回帖
     * @param Id
     */
    public void deleteFRply(ObjectId Id){
        BasicDBObject query =new BasicDBObject(Constant.ID, Id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_REPLY,query);
    }

    /**
     * 获取板块的总帖数
     * @param postSectionId
     * @return
     */
    public int getFReplyCount(ObjectId postSectionId,String name){
        BasicDBObject query =new BasicDBObject();
        if(postSectionId != null){
            query.append("pstid", postSectionId);
        }
        if(!"".equals(name)){
            query.append("nm", name);
        }
        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_REPLY, query);
        return count;
    }
}
