package com.db.fcommunity;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.fcommunity.FLoginLogEntry;
import com.sys.constants.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jerry on 2016/9/21.
 * 登录日志 Dao层
 */
public class LoginLogDao extends BaseDao {


    public List<FLoginLogEntry> getLoginLog(long start, long end) {

        BasicDBObject query = new BasicDBObject()
                .append("ti", new BasicDBObject(Constant.MONGO_LTE, end).append(Constant.MONGO_GTE, start));
        List<DBObject> list = find(MongoFacroty.getAppDB(), getCollection(), query, Constant.FIELDS);
        List<FLoginLogEntry> loginLogEntries = new ArrayList<FLoginLogEntry>();
        for (DBObject dbo : list) {
            loginLogEntries.add(new FLoginLogEntry(dbo));
        }
        return loginLogEntries;
    }

    private String getCollection() {
        return Constant.COLLECTION_FORUM_LOGIN_LOG;
    }

    public void save(FLoginLogEntry entry) {
        save(MongoFacroty.getAppDB(), getCollection(), entry.getBaseEntry());
    }

    public int selectWeekNumber(String userName) {
        BasicDBObject query =new BasicDBObject();
        query.append("nm", userName);
        BasicDBList dblist =new BasicDBList();
        long time = System.currentTimeMillis();
        long startTime = time-7l*24l*60l*60l*1000l;
        long endTime =time;
        dblist.add(new BasicDBObject("ti", new BasicDBObject(Constant.MONGO_GTE, startTime)));
        dblist.add(new BasicDBObject("ti", new BasicDBObject(Constant.MONGO_LT, endTime)));
        query.append(Constant.MONGO_AND,dblist);

        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_FORUM_LOGIN_LOG,
                        query);
        return count;
    }
    public int selectMonthNumber(String userName) {
        BasicDBObject query =new BasicDBObject();
        query.append("nm", userName);
        BasicDBList dblist =new BasicDBList();
        long time = System.currentTimeMillis();
        long startTime = time-30l*24l*60l*60l*1000l;
        long endTime =time;
        dblist.add(new BasicDBObject("ti", new BasicDBObject(Constant.MONGO_GTE, startTime)));
        dblist.add(new BasicDBObject("ti", new BasicDBObject(Constant.MONGO_LT, endTime)));
        query.append(Constant.MONGO_AND,dblist);
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_FORUM_LOGIN_LOG,
                        query);
        return count;
    }

    public FLoginLogEntry getEntry(String userName) {
        BasicDBObject query = new BasicDBObject("nm",userName);
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_LOGIN_LOG, query, Constant.FIELDS,new BasicDBObject("ti",Constant.DESC));
        if(null!=dboList && !dboList.isEmpty())
        {
             return new FLoginLogEntry(dboList.get(0));
        }
        return null;
    }
    public static void main(String[] args){
        long time = System.currentTimeMillis();
        System.out.println(time);
        long during = 90l*24l*60l*60l*1000l;
        System.out.println(during);
        long startTime = time- during;
        System.out.println(startTime);
    }
    //答题列表
    public List<FLoginLogEntry> getUserResultList() {
        BasicDBObject query =new BasicDBObject();
        long time = System.currentTimeMillis();
        long startTime = time-90l*24l*60l*60l*1000l;
        query.append("ti",new BasicDBObject(Constant.MONGO_GT,startTime));
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_LOGIN_LOG, query, Constant.FIELDS,new BasicDBObject("ti",Constant.DESC));
        List<FLoginLogEntry> retList =new ArrayList<FLoginLogEntry>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new FLoginLogEntry(dbo));
            }
        }
        return retList;
    }
}
