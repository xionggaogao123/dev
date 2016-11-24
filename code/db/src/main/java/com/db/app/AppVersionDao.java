package com.db.app;

import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.AppVersionEntry;
import com.sys.constants.Constant;

/**
 * 版本信息
 *
 * @author fourer
 */
public class AppVersionDao extends BaseDao {

    /**
     * 增加一个地区
     *
     * @param e
     * @return
     */
    public ObjectId addAppVersion(AppVersionEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_VERSION, e.getBaseEntry());
        return e.getID();
    }


    /**
     * 得到最近的版本号
     *
     * @return
     */
    public AppVersionEntry getRecentlyVersion(int client) {
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_VERSION, new BasicDBObject("c", client), Constant.FIELDS, Constant.MONGO_SORTBY_DESC, Constant.ZERO, Constant.ONE);
        if (null != list && list.size() == Constant.ONE) {
            DBObject dbo = list.get(0);
            return new AppVersionEntry((BasicDBObject) dbo);
        }
        return null;
    }

}
