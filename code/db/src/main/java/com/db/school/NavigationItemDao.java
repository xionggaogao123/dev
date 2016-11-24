package com.db.school;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.school.NavigationItemEntry;
import com.sys.constants.Constant;

/**
 * 导航条目 操作
 *
 * @author fourer
 */
public class NavigationItemDao extends BaseDao {

    /**
     * 增加
     *
     * @param e
     * @return
     */
    public ObjectId addNavigationItemEntry(NavigationItemEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NAV_ITEM, e.getBaseEntry());
        return e.getID();
    }


    /**
     * 详情
     *
     * @param id
     * @return
     */
    public NavigationItemEntry getNavigationItemEntry(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NAV_ITEM, query, Constant.FIELDS);
        if (null != dbo) {
            return new NavigationItemEntry((BasicDBObject) dbo);
        }
        return null;
    }


    /**
     * 列表
     *
     * @param ids
     * @return
     */
    public List<NavigationItemEntry> getNavigationItemEntrys(Collection<ObjectId> ids) {
        List<NavigationItemEntry> retList = new ArrayList<NavigationItemEntry>();
        DBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids));
        List<DBObject> dbos = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NAV_ITEM, query, Constant.FIELDS);
        if (null != dbos) {
            for (DBObject dbo : dbos) {
                retList.add(new NavigationItemEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }
}
