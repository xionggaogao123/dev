package com.db.extendedcourse;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.extendedcourse.ExtendedCourseEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-12-07.
 */
public class ExtendedCourseDao extends BaseDao {
    public void saveEntry(ExtendedCourseEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_EXTENDED_COURSE,entry.getBaseEntry());
    }

    //查询全部
    public List<ExtendedCourseEntry> getAllEntryList(String gradeName,ObjectId schoolId,String keyword,int status,long current,int page,int pageSize){
        List<ExtendedCourseEntry> entryList=new ArrayList<ExtendedCourseEntry>();
        BasicDBObject query=new BasicDBObject();
        query.append("isr", 0);
        query.append("sid",schoolId);
        if(gradeName!=null && !gradeName.equals("")){
            query.append("glt",gradeName);
        }
        if(keyword!=null && !keyword.equals("")){
            query.append("cnm", MongoUtils.buildRegex(keyword));
        }
        //0  全部   1  报名中   2  学习中   3 已学完
        if(status==0){
            //查询全部
        }else if(status==1){
            //课程未开始
            query.append("vst", new BasicDBObject(Constant.MONGO_GT, current));
            //query.append("vet", new BasicDBObject(Constant.MONGO_GT, current));
        }else if(status==2){
            //课程进行中
            query.append("vst", new BasicDBObject(Constant.MONGO_LT, current));
            query.append("vet", new BasicDBObject(Constant.MONGO_GT, current));
        }else if(status==3){
            //课程已结束
            query.append("vet", new BasicDBObject(Constant.MONGO_LT, current));
        }
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXTENDED_COURSE, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC,(page - 1) * pageSize, pageSize);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ExtendedCourseEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
    //查询全部
    public int countAllEntryList(String gradeName,ObjectId schoolId,String keyword,int status,long current){
        BasicDBObject query=new BasicDBObject();
        query.append("isr", 0);
        query.append("sid", schoolId);
        if(gradeName!=null && !gradeName.equals("")){
            query.append("glt",gradeName);
        }
        if(keyword!=null && !keyword.equals("")){
            query.append("cnm", MongoUtils.buildRegex(keyword));
        }
        //0  全部   1  报名中   2  学习中   3 已学完
        if(status==0){
            //查询全部
        }else if(status==1){
            //课程未开始
            query.append("vst", new BasicDBObject(Constant.MONGO_GT, current));
            //query.append("vet", new BasicDBObject(Constant.MONGO_GT, current));
        }else if(status==2){
            //课程进行中
            query.append("vst", new BasicDBObject(Constant.MONGO_LT, current));
            query.append("vet", new BasicDBObject(Constant.MONGO_GT, current));
        }else if(status==3){
            //课程已结束
            query.append("vet", new BasicDBObject(Constant.MONGO_LT, current));
        }
        int count = count(MongoFacroty.getAppDB(),Constant.COLLECTION_EXTENDED_COURSE, query);
        return count;
    }


    public ExtendedCourseEntry getEntryById(ObjectId id){
        BasicDBObject query = new BasicDBObject();
        query.append("isr",Constant.ZERO);
        query.append(Constant.ID,id);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_EXTENDED_COURSE,query,Constant.FIELDS);
        if(dbObject==null){
            return null;
        }else{
            return new ExtendedCourseEntry((BasicDBObject) dbObject);
        }
    }
}
