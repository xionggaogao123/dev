package com.db.excellentCourses;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.excellentCourses.CoursesOrderResultEntry;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-10-19.
 */
public class CoursesOrderResultDao extends BaseDao {
    //添加课程
    public String addEntry(CoursesOrderResultEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSES_ORDER_RESULT, entry.getBaseEntry());
        return entry.getID().toString();
    }
    /**
     * 修改
     */
    public void updEntry(CoursesOrderResultEntry e) {
        BasicDBObject query=new BasicDBObject(Constant.ID,e.getID());
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,e.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSES_ORDER_RESULT, query,updateValue);
    }

    //删除
    public void delEntry(ObjectId userId,ObjectId coursesId){
        BasicDBObject query = new BasicDBObject("cid",coursesId).append("uid",userId);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isr",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSES_ORDER_RESULT, query,updateValue);
    }



    //首页订单查询
    public List<CoursesOrderResultEntry> getEntryList(int type,String startTime,String endTime,String schoolId,String coursesId,int page,int pageSize){
        List<CoursesOrderResultEntry> entryList=new ArrayList<CoursesOrderResultEntry>();
        BasicDBObject query=new BasicDBObject().append("isr", 0).append("typ",type);
        if(startTime !=null && !startTime.equals("")&& endTime !=null && !endTime.equals("")){
            long stm = DateTimeUtils.getStrToLongTime(startTime,"yyyy-MM-dd");
            endTime = endTime + " 23:59:59";
            long etm = DateTimeUtils.getStrToLongTime(endTime,"yyyy-MM-dd HH:mm:ss");
            BasicDBList basicDBList = new BasicDBList();
            basicDBList.add(new BasicDBObject("otm",new BasicDBObject(Constant.MONGO_GTE,stm)));
            basicDBList.add(new BasicDBObject("otm",new BasicDBObject(Constant.MONGO_LTE,etm)));
            query.append(Constant.MONGO_AND,basicDBList);
        }
        if(schoolId !=null && !schoolId.equals("")){
            query.append("sid",new ObjectId(schoolId));
        }
        if(coursesId !=null && !coursesId.equals("")){
            query.append("cid",new ObjectId(coursesId));
        }
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSES_ORDER_RESULT, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC,(page - 1) * pageSize, pageSize);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new CoursesOrderResultEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    public List<CoursesOrderResultEntry> getAllEntryList(String startTime,String endTime,String schoolId,String coursesId){
        List<CoursesOrderResultEntry> entryList=new ArrayList<CoursesOrderResultEntry>();
        BasicDBObject query=new BasicDBObject().append("isr", 0);
        if(startTime !=null && !startTime.equals("")&& endTime !=null && !endTime.equals("")){
            long stm = DateTimeUtils.getStrToLongTime(startTime,"yyyy-MM-dd");
            endTime = endTime + " 23:59:59";
            long etm = DateTimeUtils.getStrToLongTime(endTime,"yyyy-MM-dd HH:mm:ss");
            BasicDBList basicDBList = new BasicDBList();
            basicDBList.add(new BasicDBObject("otm",new BasicDBObject(Constant.MONGO_GTE,stm)));
            basicDBList.add(new BasicDBObject("otm",new BasicDBObject(Constant.MONGO_LTE,etm)));
            query.append(Constant.MONGO_AND,basicDBList);
        }
        if(schoolId !=null && !schoolId.equals("")){
            query.append("sid",new ObjectId(schoolId));
        }
        if(coursesId !=null && !coursesId.equals("")){
            query.append("cid",new ObjectId(coursesId));
        }
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSES_ORDER_RESULT, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new CoursesOrderResultEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    public List<CoursesOrderResultEntry> getNewAllEntryList(int type,String startTime,String endTime,String schoolId,String coursesId){
        List<CoursesOrderResultEntry> entryList=new ArrayList<CoursesOrderResultEntry>();
        BasicDBObject query=new BasicDBObject().append("isr", 0).append("typ",type);
        if(startTime !=null && !startTime.equals("")&& endTime !=null && !endTime.equals("")){
            long stm = DateTimeUtils.getStrToLongTime(startTime,"yyyy-MM-dd");
            endTime = endTime + " 23:59:59";
            long etm = DateTimeUtils.getStrToLongTime(endTime,"yyyy-MM-dd HH:mm:ss");
            BasicDBList basicDBList = new BasicDBList();
            basicDBList.add(new BasicDBObject("otm",new BasicDBObject(Constant.MONGO_GTE,stm)));
            basicDBList.add(new BasicDBObject("otm",new BasicDBObject(Constant.MONGO_LTE,etm)));
            query.append(Constant.MONGO_AND,basicDBList);
        }
        if(schoolId !=null && !schoolId.equals("")){
            query.append("sid",new ObjectId(schoolId));
        }
        if(coursesId !=null && !coursesId.equals("")){
            query.append("cid",new ObjectId(coursesId));
        }
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSES_ORDER_RESULT, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new CoursesOrderResultEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    public int countEntryList(int type,String startTime,String endTime,String schoolId,String coursesId){
        BasicDBObject query=new BasicDBObject().append("isr", 0).append("typ",type);
        if(startTime !=null && !startTime.equals("")&& endTime !=null && !endTime.equals("")){
            long stm = DateTimeUtils.getStrToLongTime(startTime,"yyyy-MM-dd");
            endTime = endTime + " 23:59:59";
            long etm = DateTimeUtils.getStrToLongTime(endTime,"yyyy-MM-dd HH:mm:ss");
            BasicDBList basicDBList = new BasicDBList();
            basicDBList.add(new BasicDBObject("otm",new BasicDBObject(Constant.MONGO_GTE,stm)));
            basicDBList.add(new BasicDBObject("otm",new BasicDBObject(Constant.MONGO_LTE,etm)));
            query.append(Constant.MONGO_AND,basicDBList);
        }
        if(schoolId !=null && !schoolId.equals("")){
            query.append("sid",new ObjectId(schoolId));
        }
        if(coursesId !=null && !coursesId.equals("")){
            query.append("cid",new ObjectId(coursesId));
        }
        int count = count(MongoFacroty.getAppDB(),Constant.COLLECTION_COURSES_ORDER_RESULT,query);
        return count;
    }


    public List<ObjectId> getAllLsit(ObjectId schoolId){
        List<ObjectId> entryList=new ArrayList<ObjectId>();
        BasicDBObject query=new BasicDBObject().append("isr", 0);
        query.append("sid",schoolId);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSES_ORDER_RESULT, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new CoursesOrderResultEntry((BasicDBObject) obj).getCoursesId());
            }
        }
        return entryList;
    }
}
