package com.db.smalllesson;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.smalllesson.SmallLessonEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by James on 2017/9/27.
 */
public class SmallLessonDao extends BaseDao {
    //用户的所有课程列表（分页）
    public List<SmallLessonEntry> getLessonPageList(ObjectId userId,int page,int pageSize) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("uid",userId);
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_SMALL_LESSON, query, Constant.FIELDS,new BasicDBObject("dtm",Constant.DESC),(page - 1) * pageSize, pageSize);
        List<SmallLessonEntry> retList =new ArrayList<SmallLessonEntry>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new SmallLessonEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }
    //用户的所有课程列表
    public List<SmallLessonEntry> getLessonList(ObjectId userId) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("uid",userId);
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_SMALL_LESSON, query, Constant.FIELDS,new BasicDBObject("dtm",Constant.DESC));
        List<SmallLessonEntry> retList =new ArrayList<SmallLessonEntry>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new SmallLessonEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    public List<SmallLessonEntry> getTeacherLessonList(List<ObjectId> userIds,long startTime,long endTime) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("uid",new BasicDBObject(Constant.MONGO_IN,userIds));
        BasicDBList dblist =new BasicDBList();
        dblist.add(new BasicDBObject("ctm", new BasicDBObject(Constant.MONGO_GTE, startTime)));
        dblist.add(new BasicDBObject("ctm", new BasicDBObject(Constant.MONGO_LT, endTime)));
        query.append(Constant.MONGO_AND,dblist);
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_SMALL_LESSON, query, Constant.FIELDS,new BasicDBObject("dtm",Constant.DESC));
        List<SmallLessonEntry> retList =new ArrayList<SmallLessonEntry>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new SmallLessonEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    /**
     * getTeacherLessonList 方法 去掉时间筛选
     * @param userIds
     * @return
     */
    public List<SmallLessonEntry> getTeacherLessonListNotTimeRange(List<ObjectId> userIds) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("uid",new BasicDBObject(Constant.MONGO_IN,userIds));
//        BasicDBList dblist =new BasicDBList();
//        dblist.add(new BasicDBObject("ctm", new BasicDBObject(Constant.MONGO_GTE, startTime)));
//        dblist.add(new BasicDBObject("ctm", new BasicDBObject(Constant.MONGO_LT, endTime)));
//        query.append(Constant.MONGO_AND,dblist);
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_SMALL_LESSON, query, Constant.FIELDS,new BasicDBObject("dtm",Constant.DESC));
        List<SmallLessonEntry> retList =new ArrayList<SmallLessonEntry>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new SmallLessonEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }
    /**
     * 修改
     */
    public void updEntry(SmallLessonEntry e) {
        BasicDBObject query=new BasicDBObject(Constant.ID,e.getID());
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,e.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SMALL_LESSON, query,updateValue);
    }
    public SmallLessonEntry getEntry(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        query.append("isr",Constant.ZERO);
        DBObject obj =
                findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_SMALL_LESSON, query, Constant.FIELDS);
        if (obj != null) {
            return new SmallLessonEntry((BasicDBObject) obj);
        }
        return null;
    }

    public List<SmallLessonEntry> getEntryList(long time) {
        BasicDBObject query = new BasicDBObject();
        query.append("isr",Constant.ZERO);
        query.append("typ",0);
        query.append("ctm",new BasicDBObject(Constant.MONGO_LT, time));
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_SMALL_LESSON, query, Constant.FIELDS,new BasicDBObject("dtm",Constant.DESC));
        List<SmallLessonEntry> retList =new ArrayList<SmallLessonEntry>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new SmallLessonEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }
    public SmallLessonEntry getActiveEntry(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        query.append("isr",Constant.ZERO);
        query.append("typ",0);
        DBObject obj =
                findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_SMALL_LESSON, query, Constant.FIELDS);
        if (obj != null) {
            return new SmallLessonEntry((BasicDBObject) obj);
        }
        return null;
    }
    public SmallLessonEntry getEntryByUserId(ObjectId id) {
        BasicDBObject query = new BasicDBObject();
        query.append("isr",Constant.ZERO);
        query.append("uid",id);
        query.append("typ",0);
        DBObject obj =
                findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_SMALL_LESSON, query, Constant.FIELDS);
        if (obj != null) {
            return new SmallLessonEntry((BasicDBObject) obj);
        }
        return null;
    }
    public SmallLessonEntry getEntryByCode(String code) {
        BasicDBObject query = new BasicDBObject();
        query.append("isr",Constant.ZERO);
        query.append("cod",code);
        query.append("typ",0);
        DBObject obj =
                findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_SMALL_LESSON, query, Constant.FIELDS);
        if (obj != null) {
            return new SmallLessonEntry((BasicDBObject) obj);
        }
        return null;
    }
    public SmallLessonEntry getEntry2(ObjectId userId) {
        
        
        BasicDBObject query = new BasicDBObject();
        query.append("isr",Constant.ZERO);
        query.append("uid",new BasicDBObject(Constant.MONGO_IN, userId));
        query.append("typ",0);
        DBObject obj =
                findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_SMALL_LESSON, query, Constant.FIELDS);
        if (obj != null) {
            return new SmallLessonEntry((BasicDBObject) obj);
        }
        return null;
        
    }
    
    public List<ObjectId> getEntrySize(List<ObjectId> userId, Long startTime, Long endTime) {
        List<ObjectId> l= new ArrayList<ObjectId>();
        BasicDBObject query = new BasicDBObject();
        BasicDBObject query1 = new BasicDBObject();
        BasicDBObject query2 = new BasicDBObject();
        BasicDBList values = new BasicDBList();
        query1.append("isr",Constant.ZERO);
        query1.append("uid",new BasicDBObject(Constant.MONGO_IN, userId));
        if (startTime != null && startTime != 0l) {
            query1.append("dtm", new BasicDBObject(Constant.MONGO_GTE, startTime));
        }
        if (endTime != null && endTime != 0l) {
            query2.append("dtm", new BasicDBObject(Constant.MONGO_LT, endTime));
        }
        values.add(query1);
        values.add(query2);
        query.put(Constant.MONGO_AND, values);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_SMALL_LESSON, query);
        for (DBObject d : list) {
            l.add(new SmallLessonEntry(d).getUserId());
        }
        return l;
    }
    
    public List<SmallLessonEntry> getEntrySize1(List<ObjectId> userId, Long startTime, Long endTime) {
        List<SmallLessonEntry> l= new ArrayList<SmallLessonEntry>();
        BasicDBObject query = new BasicDBObject();
        BasicDBObject query1 = new BasicDBObject();
        BasicDBObject query2 = new BasicDBObject();
        BasicDBList values = new BasicDBList();
        query1.append("isr",Constant.ZERO);
        query1.append("uid",new BasicDBObject(Constant.MONGO_IN, userId));
        if (startTime != null && startTime != 0l) {
            query1.append("dtm", new BasicDBObject(Constant.MONGO_GTE, startTime));
        }
        if (endTime != null && endTime != 0l) {
            query2.append("dtm", new BasicDBObject(Constant.MONGO_LT, endTime));
        }
        values.add(query1);
        values.add(query2);
        query.put(Constant.MONGO_AND, values);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_SMALL_LESSON, query);
        for (DBObject d : list) {
            l.add(new SmallLessonEntry(d));
        }
        return l;
    }
    
    public List<SmallLessonEntry> getEntrySize2(ObjectId userId, Long startTime, Long endTime) {
        List<SmallLessonEntry> l= new ArrayList<SmallLessonEntry>();
        BasicDBObject query = new BasicDBObject();
        BasicDBObject query1 = new BasicDBObject();
        BasicDBObject query2 = new BasicDBObject();
        BasicDBList values = new BasicDBList();
        query1.append("isr",Constant.ZERO);
        query1.append("uid",userId);
        if (startTime != null && startTime != 0l) {
            query1.append("dtm", new BasicDBObject(Constant.MONGO_GTE, startTime));
        }
        if (endTime != null && endTime != 0l) {
            query2.append("dtm", new BasicDBObject(Constant.MONGO_LT, endTime));
        }
        values.add(query1);
        values.add(query2);
        query.put(Constant.MONGO_AND, values);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_SMALL_LESSON, query);
        for (DBObject d : list) {
            l.add(new SmallLessonEntry(d));
        }
        return l;
    }
    
    public List<BasicDBObject> getEntryByUserId(List<ObjectId> userId, Long startTime, Long endTime) {
        List<SmallLessonEntry> l= new ArrayList<SmallLessonEntry>();
        BasicDBObject query = new BasicDBObject();
        BasicDBObject query1 = new BasicDBObject();
        BasicDBObject query2 = new BasicDBObject();
        BasicDBList values = new BasicDBList();
        query1.append("isr",Constant.ZERO);
        query1.append("uid",new BasicDBObject(Constant.MONGO_IN, userId));
        if (startTime != null && startTime != 0l) {
            query1.append("dtm", new BasicDBObject(Constant.MONGO_GTE, startTime));
        }
        if (endTime != null && endTime != 0l) {
            query2.append("dtm", new BasicDBObject(Constant.MONGO_LT, endTime));
        }
        values.add(query1);
        values.add(query2);
        query.put(Constant.MONGO_AND, values);
        DBObject query4 = new BasicDBObject(Constant.MONGO_MATCH, query);
        BasicDBObject group = new BasicDBObject();
        group.put("uid", "$uid");
        BasicDBObject select = new BasicDBObject("_id", group);
        select.put("count", new BasicDBObject("$sum", 1));
        DBObject groupP = new BasicDBObject("$group", select);
        AggregationOutput output;
        List<BasicDBObject> retList = new ArrayList<BasicDBObject>();
        try {
            output = aggregate(MongoFacroty.getAppDB(), Constant.COLLECTION_SMALL_LESSON, query4, groupP);
            Iterator<DBObject> iter = output.results().iterator();
            BasicDBObject dbob;
            while (iter.hasNext()) {
                dbob = (BasicDBObject) iter.next();
                retList.add(dbob);
            }
        } catch (Exception e) {

        }
        return retList;
    }

    public void updateSmallLessonEntry(ObjectId id,String name){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("nam",name));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SMALL_LESSON, query,updateValue);
    }
    public void delSmallLessonEntry(ObjectId id){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isr",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SMALL_LESSON, query,updateValue);
    }


    public void delSmallLessonEntryList(List<ObjectId> ids){
        BasicDBObject query = new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_IN,ids));
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("typ",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SMALL_LESSON, query,updateValue);
    }
    public void getTimeLoading(ObjectId id,long time){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("ctm",time));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SMALL_LESSON, query,updateValue);
    }

    public ObjectId addEntry(SmallLessonEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_SMALL_LESSON, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 符合搜索条件的对象个数
     * @return
     */
    public int getNumber(ObjectId userId) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("uid",userId);
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_SMALL_LESSON,
                        query);
        return count;
    }
}
