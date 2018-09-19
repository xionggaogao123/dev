package com.db.excellentCourses;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.excellentCourses.CoursesBusinessEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-09-18.
 */
public class CoursesBusinessDao extends BaseDao {
    //添加支付订单
    public String addEntry(CoursesBusinessEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSES_BUSINESS, entry.getBaseEntry());
        return entry.getID().toString();
    }

    //批量删除
    public void delEntry(ObjectId id){
        BasicDBObject query = new BasicDBObject("cid",id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isr",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSES_BUSINESS, query,updateValue);
    }

    public List<CoursesBusinessEntry> getAllEntryList(String businessName,String province,String city,int page,int pageSize){
        List<CoursesBusinessEntry> entryList=new ArrayList<CoursesBusinessEntry>();
        BasicDBObject query=new BasicDBObject().append("isr",Constant.ZERO);
        if(businessName!=null && !businessName.equals("")){
            query.append("snm", MongoUtils.buildRegex(businessName));
        }
        if(province!=null && !province.equals("")){
            query.append("pro",province);
        }
        if(city!=null && !city.equals("")){
            query.append("cit",city);
        }
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSES_BUSINESS, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new CoursesBusinessEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    public int selectMyCount(String businessName,String province,String city) {
        BasicDBObject query=new BasicDBObject().append("isr", 0);
        if(businessName!=null && !businessName.equals("")){
            query.append("snm", MongoUtils.buildRegex(businessName));
        }
        if(province!=null && !province.equals("")){
            query.append("pro",province);
        }
        if(city!=null && !city.equals("")){
            query.append("cit",city);
        }
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_COURSES_BUSINESS,
                        query);
        return count;
    }
}
