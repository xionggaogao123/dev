package com.db.forum;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.forum.FMissionEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by admin on 2016/7/5.
 */
public class FMissionDao extends BaseDao {


    /**
     * 新增&更新签到
     */
    public ObjectId saveOrUpdate(FMissionEntry fMissionEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MISSION, fMissionEntry.getBaseEntry());
        return fMissionEntry.getID();
    }


    /**
     * 查询某人的签到记录
     *
     * @param personId
     */
    public FMissionEntry findMissionByPersonId(ObjectId personId) {
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MISSION, new BasicDBObject("psid", personId), Constant.FIELDS);
        if (dbObject != null) {
            return new FMissionEntry((BasicDBObject) dbObject);
        } else {
            return null;
        }
    }

    /**
     * 查询今天的任务情况
     *
     * @param personId
     * @param date
     * @return
     */
    public FMissionEntry findTodayMissionByUserId(ObjectId personId, String date) {
        BasicDBObject query = new BasicDBObject();
        if (null != personId) {
            query.append("psid", personId);
        }
        if (null != date && !"".equals(date)) {
            query.append("ti", date);
        }
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MISSION, query, Constant.FIELDS);
        if (dbObject != null) {
            return new FMissionEntry((BasicDBObject) dbObject);
        } else {
            return null;
        }
    }
}
