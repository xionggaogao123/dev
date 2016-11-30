package com.db.playmate;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.playmate.FMateEntry;
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
     * @param lat
     * @param lon
     * @param tags
     * @param hobbys
     * @param aged
     * @param ons
     * @param page
     * @param pageSize
     * @return
     */
    public List<FMateEntry> findByPage(double lon, double lat, List<String> tags, List<String> hobbys, int aged, int ons, int page, int pageSize) {
        List<FMateEntry> fMateEntries = new ArrayList<FMateEntry>();
        List<Double> locs = new ArrayList<Double>();
        locs.add(lon);
        locs.add(lat);
        List<Object> list = new ArrayList<Object>();
        list.add(locs);
        list.add(1000);
        BasicDBObject query = new BasicDBObject("loc", new BasicDBObject(Constant.MONGO_GEOWITHIN, new BasicDBObject("$centerSphere", list)));
        if (null != tags && tags.size() > 0) {
            query.append("tag", new BasicDBObject("$all", tags));
        }
        if (null != hobbys && hobbys.size() > 0) {
            query.append("hob", new BasicDBObject("$all", hobbys));
        }
        if (aged != -1) {
            query.append("aged", aged);
        }
        if (ons != -1) {
            query.append("ons", ons);
        }
        BasicDBObject orderBy = new BasicDBObject("ti", -1);
        List<DBObject> dbos = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MATE_SEEKMATE, query, Constant.FIELDS, orderBy, (page - 1) * pageSize, pageSize);
        for (DBObject dbo : dbos) {
            fMateEntries.add(new FMateEntry(dbo));
        }
        return fMateEntries;
    }

    /**
     * count
     * @param lat
     * @param lon
     * @param tags
     * @param hobbys
     * @param aged
     * @param ons
     * @return
     */
    public int countByPage(double lat, double lon, List<String> tags, List<String> hobbys, int aged, int ons) {
        List<Double> locs = new ArrayList<Double>();
        locs.add(lat);
        locs.add(lon);
        List<Object> list = new ArrayList<Object>();
        list.add(locs);
        list.add(1000);
        BasicDBObject query = new BasicDBObject("loc", new BasicDBObject(Constant.MONGO_GEOWITHIN, new BasicDBObject("$centerSphere", list)));
        if (null != tags && tags.size() > 0) {
            query.append("tag", new BasicDBObject("$all", tags));
        }
        if (null != hobbys && hobbys.size() > 0) {
            query.append("hob", new BasicDBObject("$all", hobbys));
        }
        if (aged != -1) {
            query.append("aged", aged);
        }
        if (ons != -1) {
            query.append("ons", ons);
        }
        return count(MongoFacroty.getAppDB(),Constant.COLLECTION_FORUM_MATE_SEEKMATE,query);
    }

    /**
     * 更新位置
     * @param userId
     * @param lat
     * @param lon
     */
    public void upateUserLocation(ObjectId userId, double lat, double lon) {
        BasicDBObject query = new BasicDBObject("uid", userId);
        List<Double> locations = new ArrayList<Double>();
        locations.add(lat);
        locations.add(lon);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("loc", locations));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MATE_SEEKMATE, query, update);
    }

    /**
     * 更新用户标签
     * @param userId
     * @param tags
     */
    public void updateUserTags(ObjectId userId, List<String> tags) {
        BasicDBObject query = new BasicDBObject("uid", userId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("tag", tags));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MATE_SEEKMATE, query, update);
    }

    /**
     * 更新爱好
     * @param userId
     * @param hobbys
     */
    public void updateUserHobbys(ObjectId userId, List<String> hobbys) {
        BasicDBObject query = new BasicDBObject("uid", userId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("hob", hobbys));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MATE_SEEKMATE, query, update);
    }

    public static void main(String[] args) {


    }

}
