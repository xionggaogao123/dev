package com.db.registration;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.registration.QualityEntry;
import com.sys.constants.Constant;

/**
 * 素质教育项目Dao
 *
 * @author cxy
 *         2015-11-25 14:37:17
 */
public class QualityDao extends BaseDao {
    /**
     * 添加素质教育项目
     *
     * @param e
     * @return
     */
    public ObjectId addQualityEntry(QualityEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_QUALITY, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 删除一条素质教育项目
     *
     * @param id
     */
    public void deleteQualityEntry(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_QUALITY, query);
    }

    /**
     * 根据ID更新一条素质教育项目信息
     */
    public void updateQualityEntry(ObjectId id, String name) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject()
                        .append("nm", name));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_QUALITY, query, updateValue);

    }

    /**
     * 通过schoolId查询素质教育项目信息
     *
     * @param schoolId
     */
    public List<QualityEntry> queryQuality(ObjectId schoolId) {
        List<QualityEntry> retList = new ArrayList<QualityEntry>();
        DBObject query = new BasicDBObject("sid", schoolId);
        DBObject orderBy = new BasicDBObject("ti", Constant.ASC);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_QUALITY, query, Constant.FIELDS, orderBy);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new QualityEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }

}
