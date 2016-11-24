package com.db.school;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.school.NavigationEntry;
import com.sys.constants.Constant;

/**
 * 导航操作
 *
 * @author fourer
 */
public class NavigationDao extends BaseDao {

    /**
     * 增加
     *
     * @param e
     * @return
     */
    public ObjectId addNavigationEntry(NavigationEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NAV, e.getBaseEntry());
        return e.getID();
    }


    /**
     * 详情
     *
     * @param id
     * @return
     */
    public NavigationEntry getNavigationEntry(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NAV, query, Constant.FIELDS);
        if (null != dbo) {
            return new NavigationEntry((BasicDBObject) dbo);
        }
        return null;
    }


}
