package com.db.jiaschool;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.jiaschool.HomeSchoolEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018/2/1.
 */
public class HomeSchoolDao extends BaseDao {

    //添加标签
    public ObjectId addEntry(HomeSchoolEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_HOME_SCHOOL, entry.getBaseEntry());
        return entry.getID();
    }
    //查询
    public HomeSchoolEntry getEntryById(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        query.append("isr",Constant.ZERO);
        DBObject obj =
                findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_HOME_SCHOOL, query, Constant.FIELDS);
        if (obj != null) {
            return new HomeSchoolEntry((BasicDBObject) obj);
        }
        return null;
    }

    //查询
    public HomeSchoolEntry getEntryBySchoolId(ObjectId schoolId) {
        BasicDBObject query = new BasicDBObject(Constant.ID,schoolId);
        query.append("isr",Constant.ZERO);
        DBObject obj =
                findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_HOME_SCHOOL, query, Constant.FIELDS);
        if (obj != null) {
            return new HomeSchoolEntry((BasicDBObject) obj);
        }
        return null;
    }

    public void delEntryById(ObjectId id){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isr",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_HOME_SCHOOL, query,updateValue);
    }

    public List<HomeSchoolEntry> getReviewList(int sortType,int page,int pageSize,String keyword) {
        BasicDBObject query = new BasicDBObject()
                .append("isr", 0); // 未删除
        if(sortType !=0){
            query.append("sty",sortType);
        }
        if(keyword != null && !keyword.equals("")){
            query.append("nm", MongoUtils.buildRegex(keyword));
        }
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_HOME_SCHOOL,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC,(page - 1) * pageSize, pageSize);
        List<HomeSchoolEntry> entryList = new ArrayList<HomeSchoolEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new HomeSchoolEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    public List<HomeSchoolEntry> getAllList(int page,int pageSize,String keyword) {
        BasicDBObject query = new BasicDBObject()
                .append("isr", 0); // 未删除
        if(keyword != null && !keyword.equals("")){
            query.append("nm", MongoUtils.buildRegex(keyword));
        }
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_HOME_SCHOOL,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC,(page - 1) * pageSize, pageSize);
        List<HomeSchoolEntry> entryList = new ArrayList<HomeSchoolEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new HomeSchoolEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    public List<HomeSchoolEntry> getSchoolList() {
        BasicDBObject query = new BasicDBObject()
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_HOME_SCHOOL,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<HomeSchoolEntry> entryList = new ArrayList<HomeSchoolEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new HomeSchoolEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    public List<ObjectId> getSchoolObjectList(List<ObjectId> objectIds) {
        BasicDBObject query = new BasicDBObject()
                .append("isr", 0); // 未删除
        query.append(Constant.ID,new BasicDBObject(Constant.MONGO_IN,objectIds));
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_HOME_SCHOOL,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<ObjectId> entryList = new ArrayList<ObjectId>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                HomeSchoolEntry homeSchoolEntry =new HomeSchoolEntry((BasicDBObject) obj);
                entryList.add(homeSchoolEntry.getID());
            }
        }
        return entryList;
    }
    //查询所有已提交的数量
    public int getSortCount() {
        BasicDBObject query = new BasicDBObject();
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_HOME_SCHOOL,
                        query);
        return count;
    }
    //查询所有已提交的数量
    public int getListCount(int sortType,int page,int pageSize,String keyword) {
        BasicDBObject query = new BasicDBObject()
                .append("isr", 0); // 未删除
        if(sortType !=0){
            query.append("sty",sortType);
        }
        if(keyword != null && !keyword.equals("")){
            query.append("nm", MongoUtils.buildRegex(keyword));
        }
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_HOME_SCHOOL,
                        query);
        return count;
    }


    public int getAllListCount(int page,int pageSize,String keyword) {
        BasicDBObject query = new BasicDBObject()
                .append("isr", 0); // 未删除
        if(keyword != null && !keyword.equals("")){
            query.append("nm", MongoUtils.buildRegex(keyword));
        }
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_HOME_SCHOOL,
                        query);
        return count;
    }
    //删除
    public void delEntry(ObjectId id){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isr",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_HOME_SCHOOL, query,updateValue);
    }
}
