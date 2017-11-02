package com.db.smalllesson;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.smalllesson.SmallLessonEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
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
        query.append("uid",userId);
        query.append("typ",0);
        DBObject obj =
                findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_SMALL_LESSON, query, Constant.FIELDS);
        if (obj != null) {
            return new SmallLessonEntry((BasicDBObject) obj);
        }
        return null;
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
