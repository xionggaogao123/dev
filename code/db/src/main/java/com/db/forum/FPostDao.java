package com.db.forum;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.forum.FPostEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by admin on 2016/5/31.
 */
public class FPostDao extends BaseDao {

    /**
     * 添加帖子
     * @param e
     * @return
     */
    public ObjectId addFPost(FPostEntry e){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_POST, e.getBaseEntry());
        return e.getID();
    }


    /**
     * 获取帖子（分页）
     * @param fields
     * @param sort
     * @param cream
     * @param postSectionId
     * @param classify
     * @param skip
     * @param gtTime
     * @param limit
     * @return
     */
    public List<FPostEntry> getFPostEntries(DBObject fields, DBObject sort,int cream,ObjectId postSectionId,
                                            ObjectId personId,int classify,
                                            long gtTime,int skip,int limit){
        List<FPostEntry> retList=new ArrayList<FPostEntry>();

        BasicDBObject query = new BasicDBObject("pstid",postSectionId);
        if(classify!=-1){
            query.append("clf",classify);
        }
        if(gtTime!=0){
            query.append("ti",new BasicDBObject(Constant.MONGO_GT, gtTime));
            query.append("ti",new BasicDBObject(Constant.MONGO_LT, System.currentTimeMillis()));
        }
        if(cream!=-1){
            query.append("cr",cream);
        }
        if(personId!=null){
            query.append("psid",personId);
        }
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_POST, query, fields, sort, skip, limit);

        for(DBObject dbObject : dbObjectList){
            retList.add(new FPostEntry((BasicDBObject)dbObject));
        }
        return  retList;
    }

    /**
     * 查帖子数量
     * @param cream
     * @param postSectionId
     * @param classify
     * @param gtTime
     * @return
     */
    public int  getFPostEntriesCount(int cream,ObjectId postSectionId,ObjectId personId,int classify,
                                            long gtTime){

        BasicDBObject query = new BasicDBObject("pstid",postSectionId);
        if(classify!=-1){
            query.append("clf",classify);
        }
        if(gtTime!=0){
            query.append("ti",new BasicDBObject(Constant.MONGO_GT, gtTime));
            query.append("ti",new BasicDBObject(Constant.MONGO_LT, System.currentTimeMillis()));
        }
        if(cream!=-1){
            query.append("cr",cream);
        }
        if(personId!=null){
            query.append("psid",personId);
        }
        int count= count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_POST, query);


        return  count;
    }


    /**
     * 帖子详情
     * @param id
     * @return
     */
    public FPostEntry getFPostEntry(ObjectId id){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(),Constant.COLLECTION_FORUM_POST,query,Constant.FIELDS);
        if(null!=dbo)
        {
            return new FPostEntry((BasicDBObject)dbo);
        }
        return null;
    }

    /**
     * 删除帖子
     * @param Id
     */
    public void deletePost(ObjectId Id){
        BasicDBObject query =new BasicDBObject(Constant.ID, Id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_POST,query);
    }

    /**
     * 获取板块的主题数（发帖数/帖子数）
     * @param postSectionId
     * @return
     */
    public int getThemeCount(ObjectId postSectionId,String name){
        BasicDBObject query =new BasicDBObject();
        if(postSectionId != null){
            query.append("pstid", postSectionId);
        }
        if(!"".equals(name)){
            query.append("nm", name);
        }
        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_POST, query);
        return count;
    }

    /**
     * 获取板块的总浏览数（每个帖子的浏览数相加）
     * @param postSectionId
     * @return
     */
    public int getTotalScanCount(ObjectId postSectionId,String name){
        BasicDBObject query =new BasicDBObject();
        int scanCount=0;

        if(postSectionId != null){
            query.append("pstid", postSectionId);
        }
        if(!"".equals(name)){
            query.append("nm", name);
        }
        List<DBObject> dbo=find(MongoFacroty.getAppDB(),Constant.COLLECTION_FORUM_POST,query,Constant.FIELDS);
        for(DBObject dbObject : dbo){
            FPostEntry item=new FPostEntry((BasicDBObject)dbObject);
            scanCount+=item.getScanCount();
        }
        return scanCount;
    }

    /**
     * 获取板块的总评论数（每个帖子的评论数相加）
     * @param postSectionId
     * @return
     */
    public int getTotalCommentCount(ObjectId postSectionId,String name){
        BasicDBObject query =new BasicDBObject();
        int scanCount=0;

        if(postSectionId != null){
            query.append("pstid", postSectionId);
        }
        if(!"".equals(name)){
            query.append("nm", name);
        }
        List<DBObject> dbo=find(MongoFacroty.getAppDB(),Constant.COLLECTION_FORUM_POST,query,Constant.FIELDS);
        for(DBObject dbObject : dbo){
            FPostEntry item=new FPostEntry((BasicDBObject)dbObject);
            scanCount+=item.getCommentCount();
        }
        return scanCount;
    }



}
