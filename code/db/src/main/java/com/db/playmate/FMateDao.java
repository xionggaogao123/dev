package com.db.playmate;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
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
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_MATE_SEEKMATE,mateEntry.getBaseEntry());
        return mateEntry.getID();
    }

    public List<FMateEntry> findByPage(double lat,double lon,List<String> tags,List<String> hobbys) {
        BasicDBObject query = new BasicDBObject();
        return null;
    }

    public void upateUserLocation(ObjectId userId,double lat,double lon) {
        BasicDBObject query = new BasicDBObject("uid",userId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("loc",new BasicDBObject("lat",lat).append("lon",lon)));
        update(MongoFacroty.getAppDB(),Constant.COLLECTION_FORUM_MATE_SEEKMATE,query,update);
    }

    public void updateUserTags(ObjectId userId,List<String> tags) {
        BasicDBObject query = new BasicDBObject("uid",userId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("tag",tags));
        update(MongoFacroty.getAppDB(),Constant.COLLECTION_FORUM_MATE_SEEKMATE,query,update);
    }

    public void updateUserHobbys(ObjectId userId,List<String> hobbys) {
        BasicDBObject query = new BasicDBObject("uid",userId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("hob",hobbys));
        update(MongoFacroty.getAppDB(),Constant.COLLECTION_FORUM_MATE_SEEKMATE,query,update);
    }

    public static void main(String[] args) {

//        ObjectId _id = new ObjectId();
        FMateDao fMateDao = new FMateDao();
//        fMateDao.save(new FMateEntry(_id,new ObjectId("579ec252de04cb4774f8d517")));

//        fMateDao.upateUserLocation(new ObjectId("579ec252de04cb4774f8d517"),40.0,40.0);

        List<String> tags = new ArrayList<String>();
        tags.add("学习");
        tags.add("二次元");
        fMateDao.updateUserTags(new ObjectId("579ec252de04cb4774f8d517"),tags);

    }

}
