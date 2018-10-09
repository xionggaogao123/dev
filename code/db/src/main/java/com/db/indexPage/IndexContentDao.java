package com.db.indexPage;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.indexPage.IndexContentEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-09-04.
 */
public class IndexContentDao extends BaseDao {
    //添加临时记录
    public ObjectId addEntry(IndexContentEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_INDEX_CONTENT, entry.getBaseEntry());
        return entry.getID();
    }

    //删除作业
    public void delAllEntry(){
        BasicDBObject query = new BasicDBObject();
        query.append("cty",Constant.ONE);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("ir",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_INDEX_CONTENT, query,updateValue);
    }

    //删除作业
    public void updateEntry(ObjectId id,int count){
        BasicDBObject query=new BasicDBObject()
                .append("cid",id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("act",count));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_INDEX_CONTENT, query,updateValue);
    }
    //同步修改已阅
    public void updateAllEntry(ObjectId id,int count,List<ObjectId> readList){
        BasicDBObject query=new BasicDBObject()
                .append("cid",id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("act",count).append("rlt",readList));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_INDEX_CONTENT, query,updateValue);
    }

    public IndexContentEntry getEntry(ObjectId id){
        BasicDBObject query=new BasicDBObject()
                .append("cid",id);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_INDEX_CONTENT,query,Constant.FIELDS);
        if(null!=dbObject){
            return new IndexContentEntry(dbObject);
        }else{
            return null;
        }
    }

    /**
     * 已阅读
     * @param userId
     * @param id
     */
    public void pushReadList(ObjectId userId,ObjectId id){
        BasicDBObject query=new BasicDBObject()
                .append("cid",id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_PUSH,new BasicDBObject("rlt",userId));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_INDEX_CONTENT,query,updateValue);
    }

    public List<IndexContentEntry> getPageList(List<ObjectId> contactId){
        List<IndexContentEntry> entryList=new ArrayList<IndexContentEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("cid",new BasicDBObject(Constant.MONGO_IN,contactId))
                .append("ir", Constant.ZERO);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_INDEX_CONTENT,query,
                Constant.FIELDS,Constant.MONGO_SORTBY_DESC);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new IndexContentEntry((BasicDBObject) obj));
            }
        }
        return entryList;



    }
}
