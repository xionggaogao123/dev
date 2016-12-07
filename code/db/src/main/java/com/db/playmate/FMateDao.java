package com.db.playmate;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.playmate.FMateEntry;
import com.pojo.user.UserTag;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moslpc on 2016/11/30.
 * 找玩伴 Dao层
 */
public class FMateDao extends BaseDao {

    public ObjectId save(FMateEntry mateEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MATE_SEEKMATE, mateEntry.getBaseEntry());
        return mateEntry.getID();
    }


    /**
     * 分页
     *
     * @param page
     * @param pageSize
     * @return
     */
    public List<FMateEntry> findByPage(BasicDBObject query, int page, int pageSize) {
        List<FMateEntry> fMateEntries = new ArrayList<FMateEntry>();
        BasicDBObject orderBy = new BasicDBObject();
        List<DBObject> dbos = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MATE_SEEKMATE, query, Constant.FIELDS, orderBy, (page - 1) * pageSize, pageSize);
        for (DBObject dbo : dbos) {
            fMateEntries.add(new FMateEntry(dbo));
        }
        return fMateEntries;
    }

    public BasicDBObject buildQuery(double lon, double lat, List<Integer> tags,int aged, int ons, int maxDistance) {
        BasicDBObject query = new BasicDBObject();
        if (lon != 0 && lat != 0) {
            List<Double> locs = new ArrayList<Double>();
            locs.add(lon);
            locs.add(lat);
            BasicDBObject geometry = new BasicDBObject("type", "Point")
                    .append("coordinates", locs);
            query.append("loc", new BasicDBObject("$near", new BasicDBObject("$geometry", geometry).append("$maxDistance", maxDistance)));
        }
        if (null != tags && tags.size() > 0) {
            query.append("tag", new BasicDBObject("$in", tags));
        }
        if (aged != -1) {
            query.append("aged", aged);
        }
        if (ons != -1) {
            query.append("ons", ons);
        }
        return query;
    }

    /**
     * count
     *
     * @return
     */
    public int countByPage(BasicDBObject query) {
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MATE_SEEKMATE, query);
    }

    public boolean isExist(ObjectId userId) {
        BasicDBObject query = new BasicDBObject("uid", userId);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MATE_SEEKMATE, query) >= 1;
    }

    public void updateAged(ObjectId userId, int aged) {
        BasicDBObject query = new BasicDBObject("uid", userId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("aged", aged));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MATE_SEEKMATE, query, update);
    }

    /**
     * 更新位置
     *
     * @param userId
     * @param lat
     * @param lon
     */
    public void upateUserLocation(ObjectId userId, double lat, double lon) {
        BasicDBObject query = new BasicDBObject("uid", userId);
        List<Double> locations = new ArrayList<Double>();
        locations.add(lat);
        locations.add(lon);
        BasicDBObject gemObject = new BasicDBObject("type", "Point").append("coordinates", locations);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("loc", gemObject));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MATE_SEEKMATE, query, update);
    }

    public FMateEntry getCoordinates(ObjectId userId) {
        BasicDBObject query = new BasicDBObject("uid", userId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MATE_SEEKMATE, query, Constant.FIELDS);
        return dbo == null ? null : new FMateEntry(dbo);
    }

    /**
     * 更新用户标签
     *
     * @param userId
     * @param tags
     */
    public void updateUserTags(ObjectId userId, List<Integer> tags) {
        BasicDBObject query = new BasicDBObject("uid", userId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("tag", tags));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MATE_SEEKMATE, query, update);
    }

    /**
     * 更新用户标签
     *
     * @param userId
     * @param tag
     */
    public void pushUserTag(ObjectId userId, int tag) {
        BasicDBObject query = new BasicDBObject("uid", userId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_PUSH, new BasicDBObject("tag", tag));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MATE_SEEKMATE, query, update);
    }

    /**
     * 更新用户标签
     *
     * @param userId
     * @param tag
     */
    public void pullUserTag(ObjectId userId, int tag) {
        BasicDBObject query = new BasicDBObject("uid", userId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("tag", tag));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MATE_SEEKMATE, query, update);
    }


    /**
     * 更新爱好
     *
     * @param userId
     * @param hobbys
     */
    public void updateUserHobbys(ObjectId userId, List<String> hobbys) {
        BasicDBObject query = new BasicDBObject("uid", userId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("hob", hobbys));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MATE_SEEKMATE, query, update);
    }

    /**
     * 更新可在线时间段
     * @param userId
     * @param ons
     */
    public void updateUserOns(ObjectId userId, int ons) {
        BasicDBObject query = new BasicDBObject("uid", userId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ons", ons));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MATE_SEEKMATE, query, update);
    }
}
