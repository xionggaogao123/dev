package com.db.forum;

import java.util.ArrayList;
import java.util.List;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.forum.FLogEntry;
import com.sys.constants.Constant;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

/**
 * Created by admin on 2016/7/19.
 */
public class FLogDao extends BaseDao {

    /**
     * 新增日志
     */
    public ObjectId addFLog(FLogEntry fLogEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_LOG, fLogEntry.getBaseEntry());
        return fLogEntry.getID();
    }

    /**
     * 根据板块页面名称（改用户）查询访问量
     *
     * @param actionName
     * @return
     */
    public int getCountByActionName(String actionName, ObjectId personId) {
        BasicDBObject query = new BasicDBObject();
        if (StringUtils.isNotBlank(actionName)) {
            query.append("an", actionName);
        }
        if (personId != null) {
            query.append("psid", personId);
        }
        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_LOG, query);
        return count;
    }


    /**
     * 每天5：30触发，统计前一天用户行为
     *
     * @return
     */
    public List<FLogEntry> getFLogEntrys(int skip, int limit) {
        List<FLogEntry> retList = new ArrayList<FLogEntry>(limit);

        long nowTime = System.currentTimeMillis();

        long endTime = nowTime - 1000 * 60 * 330;

        long startTime = endTime - Constant.MS_IN_DAY;

        DBObject query = new BasicDBObject("ti", new BasicDBObject(Constant.MONGO_GTE, startTime).append(Constant.MONGO_LTE, endTime));

        List<DBObject> dboList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_LOG, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, skip, limit);

        if (null != dboList && !dboList.isEmpty()) {
            for (DBObject dbo : dboList) {
                retList.add(new FLogEntry((BasicDBObject) dbo));
            }
        }

        return retList;
    }
}
