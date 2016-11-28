package com.db.fcommunity;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.fcommunity.AppBannerEntry;
import com.pojo.fcommunity.Banner;
import com.pojo.fcommunity.BannerEntity;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jerry on 2016/9/6.
 */
public class BannerDao extends BaseDao {

    //app 端banner
    public void save(AppBannerEntry entity) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_App_Banners, entity.getBaseEntry());
    }

    public List<AppBannerEntry> get() {
        List<AppBannerEntry> entities = new ArrayList<AppBannerEntry>();
        BasicDBObject query = new BasicDBObject();
        List<DBObject> dbos = find(MongoFacroty.getAppDB(), Constant.COLLECTION_App_Banners, query, Constant.FIELDS);
        for (DBObject dbo : dbos) {
            entities.add(new AppBannerEntry((BasicDBObject) dbo));
        }
        return entities;
    }

    public void deleteAppBanner(String id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new ObjectId(id));
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_App_Banners, query);
    }

    public void updateAppStatus(String id, int status) {
        DBObject query = new BasicDBObject(Constant.ID, new ObjectId(id));
        DBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ss", status));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_App_Banners, query, update);
    }

    public int getAppBannerCount(int status) {
        DBObject query = new BasicDBObject("ss", status);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_App_Banners, query);
    }

    // pc 端banner
    public List<Banner> getBanner(int status) {
        BasicDBObject query = new BasicDBObject("ss", status);
        return query(query);
    }

    public List<Banner> getBanners() {
        BasicDBObject query = new BasicDBObject();
        return query(query);
    }

    private List<Banner> query(BasicDBObject query) {
        List<Banner> entities = new ArrayList<Banner>();
        List<DBObject> dbos = find(MongoFacroty.getAppDB(), Constant.COLLECTION_Banners, query, Constant.FIELDS);
        for (DBObject dbo : dbos) {
            Banner banner = new Banner(new BannerEntity((BasicDBObject) dbo));
            entities.add(banner);
        }
        return entities;
    }

    public void save(Banner banner) {
        BannerEntity entity = new BannerEntity.Builder()
                .setName(banner.getName())
                .setCreateTime(banner.getCreateTime())
                .setImageUrl(banner.getImageUrl())
                .setStatus(banner.getStatus())
                .setSubTitle(banner.getSubTitle())
                .setTargetUrl(banner.getTargetUrl())
                .setTargetId(banner.getTargetId()).build();

        save(MongoFacroty.getAppDB(), Constant.COLLECTION_Banners, entity.getBaseEntry());
    }

    public int countBanner(int status) {
        DBObject query;
        if (status == -1) {
            query = new BasicDBObject();
        } else {
            query = new BasicDBObject("ss", status);
        }
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_Banners, query);
    }

    public void save(BannerEntity entity) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_Banners, entity.getBaseEntry());
    }

    public void delete(String id) {
        DBObject query = new BasicDBObject(Constant.ID, new ObjectId(id));
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_Banners, query);
    }

    public void updateStatus(String id, int status) {
        DBObject query = new BasicDBObject(Constant.ID, new ObjectId(id));
        DBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ss", status));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_Banners, query, update);
    }

    public int getCount(int status) {
        DBObject query = new BasicDBObject("ss", status);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_Banners, query);
    }


}
