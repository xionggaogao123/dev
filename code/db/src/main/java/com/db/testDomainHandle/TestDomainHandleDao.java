package com.db.testDomainHandle;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class TestDomainHandleDao extends BaseDao {


    public int getDBObjectCount(String dbName, String tableName, String selField, String childField) {
        BasicDBObject query = new BasicDBObject();
        if(StringUtils.isBlank(childField)){
            query.append(selField, MongoUtils.buildRegex("http"));
        }else{
            BasicDBList list =new BasicDBList();
            list.add(new BasicDBObject(selField, MongoUtils.buildRegex("http")));
            list.add(new BasicDBObject(selField+"."+childField, MongoUtils.buildRegex("http")));
            query.append(Constant.MONGO_OR, list);
        }
        DB db = null;
        if("k6kt_new".equals(dbName)){
            db = MongoFacroty.getAppDB();
        }
        if("res".equals(dbName)){
            db = MongoFacroty.getResDB();
        }
        if("cloud".equals(dbName)){
            db = MongoFacroty.getCloudAppDB();
        }
        if(db!=null) {
            return count(db, tableName, query);
        }else{
            return 0;
        }
    }

    public List<DBObject> getDBObjectList (String dbName, String tableName, String selField, String childField, int skip, int limit)
    {
        BasicDBObject query = new BasicDBObject();
        if(StringUtils.isBlank(childField)){
            query.append(selField, MongoUtils.buildRegex("http"));
        }else{
            BasicDBList list =new BasicDBList();
            list.add(new BasicDBObject(selField, MongoUtils.buildRegex("http")));
            list.add(new BasicDBObject(selField+"."+childField, MongoUtils.buildRegex("http")));
            query.append(Constant.MONGO_OR, list);
        }
        BasicDBObject fields = new BasicDBObject(selField,1);
        DB db = null;
        if("k6kt_new".equals(dbName)){
            db = MongoFacroty.getAppDB();
        }
        if("res".equals(dbName)){
            db = MongoFacroty.getResDB();
        }
        if("cloud".equals(dbName)){
            db = MongoFacroty.getCloudAppDB();
        }
        if(db!=null) {
            List<DBObject> list = find(db, tableName, query, fields, Constant.MONGO_SORTBY_DESC, skip, limit);
            return list;
        }else{
            return new ArrayList<DBObject>();
        }
    }

    public void updField(String dbName, String tableName, ObjectId id, String selField, List<DBObject> fieldObjs) {
        DB db = null;
        if("k6kt_new".equals(dbName)){
            db = MongoFacroty.getAppDB();
        }
        if("res".equals(dbName)){
            db = MongoFacroty.getResDB();
        }
        if("cloud".equals(dbName)){
            db = MongoFacroty.getCloudAppDB();
        }
        if(db!=null) {
            DBObject query =new BasicDBObject(Constant.ID,id);
            DBObject updateValue=new BasicDBObject(Constant.MONGO_SET, new BasicDBObject(selField, fieldObjs));
            update(db, tableName, query, updateValue);
        }
    }

    public void updField(String dbName, String tableName, ObjectId id, String selField, String value) {
        DB db = null;
        if("k6kt_new".equals(dbName)){
            db = MongoFacroty.getAppDB();
        }
        if("res".equals(dbName)){
            db = MongoFacroty.getResDB();
        }
        if("cloud".equals(dbName)){
            db = MongoFacroty.getCloudAppDB();
        }
        if(db!=null) {
            DBObject query =new BasicDBObject(Constant.ID,id);
            DBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject(selField, value));
            update(db, tableName, query, updateValue);
        }
    }
}
