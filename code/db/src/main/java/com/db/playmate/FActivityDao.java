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


}
