package com.db.indexPage;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.indexPage.IndexPageEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/9/29.
 */
public class IndexPageDao extends BaseDao {

    //添加临时记录
    public ObjectId addEntry(IndexPageEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_INDEX_PAGE, entry.getBaseEntry());
        return entry.getID();
    }
    public IndexPageEntry getEntry(ObjectId id){
        BasicDBObject query=new BasicDBObject("tid",id).append("isr",Constant.ZERO);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_INDEX_PAGE,query,Constant.FIELDS);
        if(null!=dbObject){
            return new IndexPageEntry((BasicDBObject) dbObject);
        }else {
            return null;
        }
    }

    //删除作业
    public void delAllEntry(){
        BasicDBObject query = new BasicDBObject();
        query.append("typ",Constant.EIGHT);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isr",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_INDEX_PAGE, query,updateValue);
    }

    //删除作业
    public void delEntry(ObjectId id){
        BasicDBObject query = new BasicDBObject();
        query.append("tid",id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isr",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_INDEX_PAGE, query,updateValue);
    }
    //查询首页显示列表
    public List<IndexPageEntry> getPageList(List<ObjectId> olist,ObjectId userId,int page,int pageSize){
        List<IndexPageEntry> entryList=new ArrayList<IndexPageEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("cid",new BasicDBObject(Constant.MONGO_IN,olist))
                //.append("uid",new BasicDBObject(Constant.MONGO_NE,userId))
                .append("isr", Constant.ZERO);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_INDEX_PAGE,query,
                Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new IndexPageEntry((BasicDBObject) obj));
            }
        }
        return entryList;



    }

    //查询首页显示列表
    public List<IndexPageEntry> getPageList2(List<ObjectId> olist,ObjectId userId,int page,int pageSize,List<Integer> integerList){
        List<IndexPageEntry> entryList=new ArrayList<IndexPageEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("cid",new BasicDBObject(Constant.MONGO_IN,olist))
                        //.append("uid",new BasicDBObject(Constant.MONGO_NE,userId))
                .append("isr", Constant.ZERO);
        query.append("typ",new BasicDBObject(Constant.MONGO_IN,integerList));
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_INDEX_PAGE,query,
                Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new IndexPageEntry((BasicDBObject) obj));
            }
        }
        return entryList;



    }

    //查询首页显示列表
    public List<ObjectId> getSixPageList(List<ObjectId> olist,ObjectId userId,int page,int pageSize,int type,List<ObjectId> userIds){
        List<ObjectId> entryList=new ArrayList<ObjectId>();
        BasicDBObject query=new BasicDBObject()
                //.append("uid",new BasicDBObject(Constant.MONGO_NE,userId))
                .append("isr", Constant.ZERO);
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject("typ",type).append("cid", new BasicDBObject(Constant.MONGO_IN, olist)));
        values.add(new BasicDBObject("typ", Constant.NINE).append("rlt", new BasicDBObject(Constant.MONGO_IN, userIds)));
        query.append(Constant.MONGO_OR,values);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_INDEX_PAGE,query,
                Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new IndexPageEntry((BasicDBObject) obj).getContactId());
            }
        }
        return entryList;



    }

    //查询首页显示列表
    public List<ObjectId> getNewPageList(List<ObjectId> olist,ObjectId userId,int page,int pageSize,int type,List<ObjectId> userIds,int role){
        List<ObjectId> entryList=new ArrayList<ObjectId>();
        BasicDBObject query=new BasicDBObject()
                        //.append("uid",new BasicDBObject(Constant.MONGO_NE,userId))
                .append("isr", Constant.ZERO);
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject("typ",type).append("cid", new BasicDBObject(Constant.MONGO_IN, olist)));
        values.add(new BasicDBObject("typ", Constant.TEN).append("rlt", new BasicDBObject(Constant.MONGO_IN, olist)).append("olt", role));
        values.add(new BasicDBObject("typ", Constant.TEN).append("uid", userId));
        values.add(new BasicDBObject("typ", Constant.NINE).append("rlt", new BasicDBObject(Constant.MONGO_IN, userIds)));
        query.append(Constant.MONGO_OR,values);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_INDEX_PAGE,query,
                Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new IndexPageEntry((BasicDBObject) obj).getContactId());
            }
        }
        return entryList;



    }

    //查询首页显示列表
    public List<ObjectId> getEightPageList(List<ObjectId> olist,ObjectId userId,int page,int pageSize,int type,List<ObjectId> userIds,int role){
        List<ObjectId> entryList=new ArrayList<ObjectId>();
        BasicDBObject query=new BasicDBObject()
                //.append("uid",new BasicDBObject(Constant.MONGO_NE,userId))
                .append("isr", Constant.ZERO);
        BasicDBList values = new BasicDBList();
        //通知
        values.add(new BasicDBObject("typ",type).append("cid", new BasicDBObject(Constant.MONGO_IN, olist)));
        //投票
        values.add(new BasicDBObject("typ", Constant.TEN).append("rlt", new BasicDBObject(Constant.MONGO_IN, olist)).append("olt", role));
        values.add(new BasicDBObject("typ", Constant.TEN).append("uid", userId));
        //成绩单
        values.add(new BasicDBObject("typ", Constant.NINE).append("rlt", new BasicDBObject(Constant.MONGO_IN, userIds)));
        values.add(new BasicDBObject("typ", Constant.NINE).append("uid", userId));
        //作业
        values.add(new BasicDBObject("typ",11).append("cid", new BasicDBObject(Constant.MONGO_IN, olist)));
        //火热分享
        values.add(new BasicDBObject("typ",12).append("cid", new BasicDBObject(Constant.MONGO_IN, olist)));
        //选课
        values.add(new BasicDBObject("typ",13).append("rlt", new BasicDBObject(Constant.MONGO_IN, olist)));
        query.append(Constant.MONGO_OR,values);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_INDEX_PAGE,query,
                Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new IndexPageEntry((BasicDBObject) obj).getContactId());
            }
        }
        return entryList;



    }

    public List<IndexPageEntry> getSystemPageList(List<ObjectId> olist,ObjectId userId,int page,int pageSize){
        List<IndexPageEntry> entryList=new ArrayList<IndexPageEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("cid",new BasicDBObject(Constant.MONGO_IN,olist))
                        //.append("uid",new BasicDBObject(Constant.MONGO_NE,userId))
                .append("isr", Constant.ZERO);
        query.append("typ",Constant.FOUR);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_INDEX_PAGE,query,
                Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new IndexPageEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    public ObjectId getNewSystemPageList(ObjectId userId,int page,int pageSize){
        BasicDBObject query=new BasicDBObject()
                .append("cid",userId)
                .append("isr", Constant.ZERO);
        query.append("typ",Constant.FOUR);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_INDEX_PAGE,query,
                Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        ObjectId syId = null;
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                syId = new IndexPageEntry((BasicDBObject) obj).getContactId();
            }
        }
        return syId;
    }

    public int countPageList(List<ObjectId> olist,ObjectId userId){
        BasicDBObject query=new BasicDBObject()
                .append("cid",new BasicDBObject(Constant.MONGO_IN,olist))
                //.append("uid", new BasicDBObject(Constant.MONGO_NE, userId))
                .append("isr", Constant.ZERO);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_INDEX_PAGE,query);
    }

    public int countPageList2(List<ObjectId> olist,ObjectId userId,List<Integer> integerList){
        BasicDBObject query=new BasicDBObject()
                .append("cid",new BasicDBObject(Constant.MONGO_IN,olist))
                        //.append("uid", new BasicDBObject(Constant.MONGO_NE, userId))
                .append("isr", Constant.ZERO);
        query.append("typ",new BasicDBObject(Constant.MONGO_IN,integerList));
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_INDEX_PAGE,query);
    }

    public int countNewPageList(List<ObjectId> olist,ObjectId userId,int type,List<ObjectId> userIds,int role){
        BasicDBObject query=new BasicDBObject()
                        //.append("uid", new BasicDBObject(Constant.MONGO_NE, userId))
                .append("isr", Constant.ZERO);
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject("typ",type).append("cid", new BasicDBObject(Constant.MONGO_IN, olist)));
        values.add(new BasicDBObject("typ", Constant.TEN).append("rlt", new BasicDBObject(Constant.MONGO_IN, olist)).append("olt", role));
        values.add(new BasicDBObject("typ", Constant.TEN).append("uid", userId));
        values.add(new BasicDBObject("typ", Constant.NINE).append("rlt", new BasicDBObject(Constant.MONGO_IN, userIds)));
        query.append(Constant.MONGO_OR,values);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_INDEX_PAGE,query);
    }

    public int countEightPageList(List<ObjectId> olist,ObjectId userId,int type,List<ObjectId> userIds,int role){
        BasicDBObject query=new BasicDBObject()
                //.append("uid", new BasicDBObject(Constant.MONGO_NE, userId))
                .append("isr", Constant.ZERO);
        BasicDBList values = new BasicDBList();
        //通知
        values.add(new BasicDBObject("typ",type).append("cid", new BasicDBObject(Constant.MONGO_IN, olist)));
        //投票
        values.add(new BasicDBObject("typ", Constant.TEN).append("rlt", new BasicDBObject(Constant.MONGO_IN, olist)).append("olt", role));
        values.add(new BasicDBObject("typ", Constant.TEN).append("uid", userId));
        //成绩单
        values.add(new BasicDBObject("typ", Constant.NINE).append("rlt", new BasicDBObject(Constant.MONGO_IN, userIds)));
        values.add(new BasicDBObject("typ", Constant.NINE).append("uid", userId));
        //作业
        values.add(new BasicDBObject("typ",11).append("cid", new BasicDBObject(Constant.MONGO_IN, olist)));
        //火热分享
        values.add(new BasicDBObject("typ",12).append("cid", new BasicDBObject(Constant.MONGO_IN, olist)));
        //选课
        values.add(new BasicDBObject("typ",13).append("rlt", new BasicDBObject(Constant.MONGO_IN, olist)));
        query.append(Constant.MONGO_OR,values);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_INDEX_PAGE,query);
    }

    public int countSixPageList(List<ObjectId> olist,ObjectId userId,int type,List<ObjectId> userIds){
        BasicDBObject query=new BasicDBObject()
                //.append("uid", new BasicDBObject(Constant.MONGO_NE, userId))
                .append("isr", Constant.ZERO);
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject("typ",type).append("cid", new BasicDBObject(Constant.MONGO_IN, olist)));
        values.add(new BasicDBObject("typ", Constant.NINE).append("rlt", new BasicDBObject(Constant.MONGO_IN, userIds)));
        query.append(Constant.MONGO_OR,values);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_INDEX_PAGE,query);
    }

}
