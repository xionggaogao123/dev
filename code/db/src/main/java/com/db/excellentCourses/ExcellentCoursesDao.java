package com.db.excellentCourses;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.excellentCourses.ExcellentCoursesEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-04-26.
 */
public class ExcellentCoursesDao extends BaseDao {
    //添加课程
    public String addEntry(ExcellentCoursesEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_EXCELLENT_COURSES, entry.getBaseEntry());
        return entry.getID().toString();
    }
    /**
     * 修改
     */
    public void updEntry(ExcellentCoursesEntry e) {
        BasicDBObject query=new BasicDBObject(Constant.ID,e.getID());
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,e.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXCELLENT_COURSES, query,updateValue);
    }

    //删除
    public void delEntry(ObjectId id){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isr",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXCELLENT_COURSES, query,updateValue);
    }

    //拒绝课程
    public void juEntry(ObjectId id){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("sta",Constant.THREE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXCELLENT_COURSES, query,updateValue);
    }

    //设为轮播课
    public void setLunEntry(ObjectId id,int status){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("top",status));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXCELLENT_COURSES, query,updateValue);
    }

    //删除
    public void finishEntry(ObjectId id,int type,long time){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("sta",type).append("ctm",time));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXCELLENT_COURSES, query,updateValue);
    }
    public ExcellentCoursesEntry getEntry(ObjectId id){
        BasicDBObject query=new BasicDBObject(Constant.ID,id).append("isr",Constant.ZERO);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_EXCELLENT_COURSES,query,Constant.FIELDS);
        if(null!=dbObject){
            return new ExcellentCoursesEntry((BasicDBObject) dbObject);
        }else {
            return null;
        }
    }

    //首页订单查询
    public List<ExcellentCoursesEntry> getEntryList(List<ObjectId> objectIds,long time){
        List<ExcellentCoursesEntry> entryList=new ArrayList<ExcellentCoursesEntry>();
        BasicDBObject query=new BasicDBObject().append("clt",new BasicDBObject(Constant.MONGO_IN,objectIds)).append("isr",0).append("sta",2);
        query.append("etm",new BasicDBObject(Constant.MONGO_GTE,time));
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXCELLENT_COURSES, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ExcellentCoursesEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    //首页过期订单查询
    public List<ExcellentCoursesEntry> getOldEntryList(List<ObjectId> objectIds,long time){
        List<ExcellentCoursesEntry> entryList=new ArrayList<ExcellentCoursesEntry>();
        BasicDBObject query=new BasicDBObject().append("clt",new BasicDBObject(Constant.MONGO_IN,objectIds)).append("isr",0).append("sta",2);
        query.append("etm",new BasicDBObject(Constant.MONGO_LT,time));
        BasicDBObject orderquery = new BasicDBObject();
        orderquery.append("ctm",-1);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXCELLENT_COURSES, query,
                Constant.FIELDS,orderquery);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ExcellentCoursesEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    //首页订单查询
    public List<ExcellentCoursesEntry> getEntryListById(List<ObjectId> objectIds,long time){
        List<ExcellentCoursesEntry> entryList=new ArrayList<ExcellentCoursesEntry>();
        BasicDBObject query=new BasicDBObject().append(Constant.ID,new BasicDBObject(Constant.MONGO_IN,objectIds)).append("isr", 0).append("sta",2);
        query.append("etm",new BasicDBObject(Constant.MONGO_GTE,time));
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXCELLENT_COURSES, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ExcellentCoursesEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    //首页订单查询
    public List<ExcellentCoursesEntry> getMyEntryListById(List<ObjectId> objectIds){
        List<ExcellentCoursesEntry> entryList=new ArrayList<ExcellentCoursesEntry>();
        BasicDBObject query=new BasicDBObject().append(Constant.ID,new BasicDBObject(Constant.MONGO_IN,objectIds)).append("isr", 0).append("sta",2);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXCELLENT_COURSES, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ExcellentCoursesEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    //首页订单查询
    public List<ExcellentCoursesEntry> getMyBuyEntryListById(List<ObjectId> objectIds,int page,int pageSize){
        List<ExcellentCoursesEntry> entryList=new ArrayList<ExcellentCoursesEntry>();
        BasicDBObject query=new BasicDBObject().append(Constant.ID,new BasicDBObject(Constant.MONGO_IN,objectIds)).append("isr", 0).append("sta",2);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXCELLENT_COURSES, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC,(page - 1) * pageSize, pageSize);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ExcellentCoursesEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
    public int countMyBuyEntryListById(List<ObjectId> objectIds){
        BasicDBObject query=new BasicDBObject().append(Constant.ID,new BasicDBObject(Constant.MONGO_IN,objectIds)).append("isr", 0).append("sta",2);
        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_EXCELLENT_COURSES, query);
        return count;
    }

    public List<ExcellentCoursesEntry> getEntryListById4(List<ObjectId> objectIds){
        List<ExcellentCoursesEntry> entryList=new ArrayList<ExcellentCoursesEntry>();
        BasicDBObject query=new BasicDBObject().append(Constant.ID,new BasicDBObject(Constant.MONGO_IN,objectIds)).append("isr", 0).append("sta",2);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXCELLENT_COURSES, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ExcellentCoursesEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    //首页订单查询
    public List<ExcellentCoursesEntry> getOtherEntryListById(List<ObjectId> objectIds){
        List<ExcellentCoursesEntry> entryList=new ArrayList<ExcellentCoursesEntry>();
        BasicDBObject query=new BasicDBObject().append(Constant.ID,new BasicDBObject(Constant.MONGO_IN,objectIds)).append("isr", 0).append("sta",2);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXCELLENT_COURSES, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ExcellentCoursesEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
    //首页订单查询
    public List<ExcellentCoursesEntry> getOldEntryListById(List<ObjectId> objectIds,long time){
        List<ExcellentCoursesEntry> entryList=new ArrayList<ExcellentCoursesEntry>();
        BasicDBObject query=new BasicDBObject().append(Constant.ID,new BasicDBObject(Constant.MONGO_IN,objectIds)).append("isr", 0).append("sta",2);
        query.append("etm",new BasicDBObject(Constant.MONGO_LT,time));
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXCELLENT_COURSES, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ExcellentCoursesEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    //首页订单查询
    public List<ExcellentCoursesEntry> getMyKeyClassList(String keyword,long current){
        List<ExcellentCoursesEntry> entryList=new ArrayList<ExcellentCoursesEntry>();
        BasicDBObject query=new BasicDBObject().append("isr",0).append("sta", 2).append("ope",Constant.ONE);
        BasicDBList values = new BasicDBList();
        if(keyword!=null && !keyword.equals("")){
            values.add(new BasicDBObject("tit",  MongoUtils.buildRegex(keyword)));
            values.add(new BasicDBObject("unm",  MongoUtils.buildRegex(keyword)));
            query.put(Constant.MONGO_OR, values);
        }
        query.append("etm",new BasicDBObject(Constant.MONGO_GT,current));
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXCELLENT_COURSES, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ExcellentCoursesEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }


    //课程中心
    public List<ExcellentCoursesEntry> getAllEntryList(String subjectId,int priceType,int persionType,int timeType,int page,int pageSize,long current,List<ObjectId> objectIdList){
        List<ExcellentCoursesEntry> entryList=new ArrayList<ExcellentCoursesEntry>();
        BasicDBObject query=new BasicDBObject().append("isr",0).append("sta",Constant.TWO);
        if(subjectId!=null && !subjectId.equals("")){
            query.append("sid", new ObjectId(subjectId));
        }
        BasicDBObject orderQuery=new BasicDBObject();
        if(priceType!=0){
            orderQuery.append("npc", priceType);
        }
        if(persionType!=0){
            orderQuery.append("stn", persionType);
        }
        if(timeType!=0){
            orderQuery.append("ctm",timeType);
        }
        if(priceType==0&&persionType==0&&timeType==0){
            orderQuery.append("ope",1);
            orderQuery.append("ctm",-1);
        }
        query.append("etm",new BasicDBObject(Constant.MONGO_GT,current));
        //query.append("ope", Constant.ONE);//公开课
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject("ope", Constant.ONE));
        values.add(new BasicDBObject("clt",new BasicDBObject(Constant.MONGO_IN,objectIdList)));
        query.put(Constant.MONGO_OR, values);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXCELLENT_COURSES, query,
                Constant.FIELDS, orderQuery,(page-1)*pageSize,pageSize);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ExcellentCoursesEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    //轮播列表
    public List<ExcellentCoursesEntry> getLunList(int page,int pageSize,long current){
        List<ExcellentCoursesEntry> entryList=new ArrayList<ExcellentCoursesEntry>();
        BasicDBObject query=new BasicDBObject().append("isr",0).append("sta",Constant.TWO).append("top",Constant.ONE);
        query.append("etm",new BasicDBObject(Constant.MONGO_GT,current));//过期
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXCELLENT_COURSES, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ExcellentCoursesEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    //轮播列表
    public List<ExcellentCoursesEntry> getOldLunList(int page,int pageSize){
        List<ExcellentCoursesEntry> entryList=new ArrayList<ExcellentCoursesEntry>();
        BasicDBObject query=new BasicDBObject().append("isr",0).append("sta",Constant.TWO).append("top",Constant.ONE);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXCELLENT_COURSES, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ExcellentCoursesEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    public int countLunList(long current){
        BasicDBObject query = new BasicDBObject();
        query.append("etm",new BasicDBObject(Constant.MONGO_LT,current));//过期
        query.append("isr",0).append("sta",Constant.TWO).append("top",Constant.ONE);
        int count = count(MongoFacroty.getAppDB(),Constant.COLLECTION_EXCELLENT_COURSES, query);
        return count;
    }

    public int countOldLunList(){
        BasicDBObject query = new BasicDBObject();
        query.append("isr",0).append("sta",Constant.TWO).append("top",Constant.ONE);
        int count = count(MongoFacroty.getAppDB(),Constant.COLLECTION_EXCELLENT_COURSES, query);
        return count;
    }

    public List<ExcellentCoursesEntry> getAllWebEntryList(String subjectId,String name,int page,int pageSize){
        List<ExcellentCoursesEntry> entryList=new ArrayList<ExcellentCoursesEntry>();
        BasicDBObject query=new BasicDBObject().append("isr",0);
        if(subjectId!=null && !subjectId.equals("")){
            query.append("sid", new ObjectId(subjectId));
        }
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        query.append("sta",new BasicDBObject(Constant.MONGO_IN,list));
        if(name!=null && !name.equals("")){
            BasicDBList values = new BasicDBList();
            values.add(new BasicDBObject("tit",  MongoUtils.buildRegex(name)));
            values.add(new BasicDBObject("unm",  MongoUtils.buildRegex(name)));
            query.put(Constant.MONGO_OR, values);
        }
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXCELLENT_COURSES, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ExcellentCoursesEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    /**
     * 符合搜索条件的对象个数
     * @return
     */
    public int selectAllWebEntryList(String subjectId,String name) {
        BasicDBObject query=new BasicDBObject().append("isr",0);
        if(subjectId!=null && !subjectId.equals("")){
            query.append("sid", new ObjectId(subjectId));
        }
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        query.append("sta",new BasicDBObject(Constant.MONGO_IN,list));
        if(name!=null && !name.equals("")){
            BasicDBList values = new BasicDBList();
            values.add(new BasicDBObject("tit",  MongoUtils.buildRegex(name)));
            values.add(new BasicDBObject("unm",  MongoUtils.buildRegex(name)));
            query.put(Constant.MONGO_OR, values);
        }
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_EXCELLENT_COURSES,
                        query);
        return count;
    }

    //我的课程
    public List<ExcellentCoursesEntry> getMyExcellentCourses(ObjectId userId,int page,int pageSize){
        List<ExcellentCoursesEntry> entryList=new ArrayList<ExcellentCoursesEntry>();
        BasicDBObject query=new BasicDBObject().append("isr", 0);
        query.append("uid",userId);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXCELLENT_COURSES, query,
                Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ExcellentCoursesEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    //我的课程 （包含打包课）
    public List<ExcellentCoursesEntry> getMyAndNewExcellentCourses(ObjectId userId,int page,int pageSize){
        List<ExcellentCoursesEntry> entryList=new ArrayList<ExcellentCoursesEntry>();
        BasicDBObject query=new BasicDBObject().append("isr", 0);
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject("uid",userId));
        values.add(new BasicDBObject("tlt",userId));
        query.append(Constant.MONGO_OR,values);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXCELLENT_COURSES, query,
                Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ExcellentCoursesEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
    /**
     * 我的课程符合搜索条件的对象个数
     * @return
     */
    public int selectMyCount(ObjectId userId) {
        BasicDBObject query=new BasicDBObject().append("isr", 0);
        query.append("uid",userId);
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_EXCELLENT_COURSES,
                        query);
        return count;
    }

    /**
     * 我的课程符合搜索条件的对象个数（打包课）
     * @return
     */
    public int selectMyNewCount(ObjectId userId) {
        BasicDBObject query=new BasicDBObject().append("isr", 0);
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject("uid",userId));
        values.add(new BasicDBObject("tlt",userId));
        query.append(Constant.MONGO_OR,values);
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_EXCELLENT_COURSES,
                        query);
        return count;
    }


    /**
     * 符合搜索条件的对象个数
     * @return
     */
    public int selectCount(String subjectId,long current,List<ObjectId> objectIdList) {
        BasicDBObject query =new BasicDBObject("isr",0).append("sta",2);
        if(subjectId!=null && !subjectId.equals("")){
            query.append("sid",new ObjectId(subjectId
            ));
        }
        query.append("etm",new BasicDBObject(Constant.MONGO_GT,current));
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject("ope", Constant.ONE));
        values.add(new BasicDBObject("clt",new BasicDBObject(Constant.MONGO_IN,objectIdList)));
        query.put(Constant.MONGO_OR, values);
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_EXCELLENT_COURSES,
                        query);
        return count;
    }


}
