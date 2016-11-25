package com.db.fcommunity;

import com.db.base.BaseDao;
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
        List<DBObject> list = find(getDB(), getCollection(), query, Constant.FIELDS);
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
        save(getDB(), getCollection(), entry.getBaseEntry());
    }

}
