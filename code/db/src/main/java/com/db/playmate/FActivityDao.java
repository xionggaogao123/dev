package com.db.playmate;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.playmate.FASignEntry;
import com.pojo.playmate.FActivityEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moslpc on 2016/11/30.
 * 找活动 Dao层
 */
public class FActivityDao extends BaseDao {

    public void save(FActivityEntry fActivityEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_ACTIVITY, fActivityEntry.getBaseEntry());
    }

    public List<FActivityEntry> findByPage(BasicDBObject query, int page, int pageSize) {
        List<FActivityEntry> activityEntries = new ArrayList<FActivityEntry>();
        BasicDBObject orderBy = new BasicDBObject("st", -1);
        List<DBObject> dbos = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_ACTIVITY, query, Constant.FIELDS, orderBy, (page - 1) * pageSize, pageSize);
        for (DBObject dbo : dbos) {
            activityEntries.add(new FActivityEntry(dbo));
        }
        return activityEntries;
    }

    public BasicDBObject buildQuery(double lon, double lat, int maxDistance) {
        BasicDBObject query = new BasicDBObject();
        if (lon != 0 && lat != 0) {
            query.append("loc", MongoUtils.buildGeometry(lon, lat, maxDistance));
        }
        return query;
    }

    public int coutByQuery(BasicDBObject query) {
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_ACTIVITY, query);
    }

    public boolean isUserSignActivity(ObjectId acid, ObjectId uid) {
        BasicDBObject query = new BasicDBObject("acid", acid).append("uid", uid);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORM_SIGN_ACTIVITY_SHEET, query) == 1;
    }

    public void save(FASignEntry signEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FORM_SIGN_ACTIVITY_SHEET, signEntry.getBaseEntry());
    }

    public int countSignUser(ObjectId acid) {
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORM_SIGN_ACTIVITY_SHEET, new BasicDBObject("acid", acid));
    }

    public int countUserSignActivity(ObjectId userId) {
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORM_SIGN_ACTIVITY_SHEET, new BasicDBObject("uid", userId));
    }

    public List<FASignEntry> get20SignEntry(ObjectId acid) {
        BasicDBObject query = new BasicDBObject("acid", acid);
        BasicDBObject orderBy = new BasicDBObject(Constant.ID, -1);
        List<DBObject> dbos = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORM_SIGN_ACTIVITY_SHEET, query, Constant.FIELDS, orderBy, 0, 20);
        List<FASignEntry> signEntries = new ArrayList<FASignEntry>();
        for (DBObject dbo : dbos) {
            signEntries.add(new FASignEntry(dbo));
        }
        return signEntries;
    }


    public int countPublishActivity(ObjectId userId) {
        BasicDBObject query = new BasicDBObject("uid", userId);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_ACTIVITY, query);
    }

    public List<FActivityEntry> getPublishedActivity(ObjectId userId, int page, int pageSize) {
        List<FActivityEntry> activityEntries = new ArrayList<FActivityEntry>();
        BasicDBObject query = new BasicDBObject("uid", userId);
        BasicDBObject orderBy = new BasicDBObject(Constant.ID, Constant.DESC);
        List<DBObject> dbos = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_ACTIVITY, query, Constant.FIELDS, orderBy, (page - 1) * pageSize, pageSize);
        for (DBObject dbo : dbos) {
            activityEntries.add(new FActivityEntry(dbo));
        }
        return activityEntries;
    }

    public List<FActivityEntry> getActivityByIds(List<ObjectId> ids) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids));
        List<DBObject> dbos = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_ACTIVITY, query, Constant.FIELDS);
        List<FActivityEntry> activityEntries = new ArrayList<FActivityEntry>();
        for (DBObject dbo : dbos) {
            activityEntries.add(new FActivityEntry(dbo));
        }
        return activityEntries;
    }

    public List<FActivityEntry> getSignedActivity(ObjectId userId, int page, int pageSize) {
        List<FASignEntry> signEntries = new ArrayList<FASignEntry>();
        BasicDBObject query = new BasicDBObject("uid", userId);
        BasicDBObject orderBy = new BasicDBObject(Constant.ID, Constant.DESC);
        List<DBObject> dbos = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORM_SIGN_ACTIVITY_SHEET, query, Constant.FIELDS, orderBy, (page - 1) * pageSize, pageSize);
        for (DBObject dbo : dbos) {
            signEntries.add(new FASignEntry(dbo));
        }
        List<ObjectId> ids = new ArrayList<ObjectId>();
        for (FASignEntry signEntry : signEntries) {
            ids.add(signEntry.getAcid());
        }
        return getActivityByIds(ids);
    }

    public FActivityEntry getActivityById(ObjectId acid) {
        BasicDBObject query = new BasicDBObject(Constant.ID, acid);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_ACTIVITY, query);
        return dbo == null ? null : new FActivityEntry(dbo);
    }

    public int countUserAttendActivity(ObjectId userId) {
        BasicDBObject query = new BasicDBObject("uid", userId).append(Constant.MONGO_GTE, new BasicDBObject("acti", System.currentTimeMillis()));
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORM_SIGN_ACTIVITY_SHEET, query);
    }

    /**
     * 获取已经参加过的活动
     *
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    public List<FActivityEntry> getAttendedActivity(ObjectId userId, int page, int pageSize) {
        List<FASignEntry> signEntries = new ArrayList<FASignEntry>();
        BasicDBObject query = new BasicDBObject("uid", userId).append(Constant.MONGO_GTE, new BasicDBObject("acti", System.currentTimeMillis()));
        BasicDBObject orderBy = new BasicDBObject("acid", Constant.DESC);
        List<DBObject> dbos = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORM_SIGN_ACTIVITY_SHEET, query, Constant.FIELDS, orderBy, (page - 1) * pageSize, pageSize);
        for (DBObject dbo : dbos) {
            signEntries.add(new FASignEntry(dbo));
        }
        List<ObjectId> ids = new ArrayList<ObjectId>();
        for (FASignEntry signEntry : signEntries) {
            ids.add(signEntry.getAcid());
        }
        return getActivityByIds(ids);
    }

    /**
     * 取消报名
     *
     * @param acid
     * @param userId
     */
    public void cancelSignActivity(ObjectId acid, ObjectId userId) {
        BasicDBObject query = new BasicDBObject("acid", acid).append("uid", userId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_FORM_SIGN_ACTIVITY_SHEET, query);
    }
}
