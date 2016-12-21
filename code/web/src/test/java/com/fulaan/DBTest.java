package com.fulaan;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.Code;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by moslpc on 2016/12/20.
 */
public class DBTest extends BaseDao{

    public static void main(String[] args) {


        ObjectId d = ObjectId.get();
        long t = d.getTime();
//        DBTest dbTest = new DBTest();
//
//        Map<String,Object> map = new HashMap<String,Object>();
//        Map<String,Object> map2 = new HashMap<String,Object>();
//        map2.put("key","value");
//        map.put("key",map2);
//
//        List<DBObject> dbs = dbTest.find(MongoFacroty.getAppDB(),"mosl.users",new BasicDBObject());
//        dbs.size();
//        dbs.size();
//        Pattern
//        List<DBObject> dbs = dbTest.find(MongoFacroty.getAppDB(),"mosl.users",new BasicDBObject("time",new BasicDBObject("$lt",new Date())));
//        dbs.size();
//
//        Date date = (Date)dbs.get(0).get("time");
//        long time = date.getTime();
//        date.getTime();
    }
}
