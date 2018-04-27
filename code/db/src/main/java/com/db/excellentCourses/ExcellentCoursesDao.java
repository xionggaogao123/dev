package com.db.excellentCourses;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.excellentCourses.ExcellentCoursesEntry;
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

    //删除
    public void finishEntry(ObjectId id,int type){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("sta",type));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXCELLENT_COURSES, query,updateValue);
    }
    public ExcellentCoursesEntry getEntry(ObjectId id){
        BasicDBObject query=new BasicDBObject(Constant.ID,id);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_EXCELLENT_COURSES,query,Constant.FIELDS);
        if(null!=dbObject){
            return new ExcellentCoursesEntry((BasicDBObject) dbObject);
        }else {
            return null;
        }
    }

    //首页订单查询
    public List<ExcellentCoursesEntry> getEntryList(List<ObjectId> objectIds){
        List<ExcellentCoursesEntry> entryList=new ArrayList<ExcellentCoursesEntry>();
        BasicDBObject query=new BasicDBObject().append("cis",new BasicDBObject(Constant.MONGO_IN,objectIds));
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
    public List<ExcellentCoursesEntry> getEntryListById(List<ObjectId> objectIds){
        List<ExcellentCoursesEntry> entryList=new ArrayList<ExcellentCoursesEntry>();
        BasicDBObject query=new BasicDBObject().append(Constant.ID,new BasicDBObject(Constant.MONGO_IN,objectIds));
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXCELLENT_COURSES, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ExcellentCoursesEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

}
