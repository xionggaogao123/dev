package com.db.achievement;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.achievement.AchievementEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 科研成果持久化
 * Created by Caocui on 2015/8/26.
 */
public class AchievementDao extends BaseDao {

    /**
     * 发布科研成果记录
     *
     * @param achievementEntry
     */
    public ObjectId add(AchievementEntry achievementEntry) {
        this.save(MongoFacroty.getCloudAppDB(), Constant.COLLECTION_ACHIEVEMENT, achievementEntry.getBaseEntry());
        return achievementEntry.getID();
    }

    /**
     * 获取符合条件的科研成果记录总数
     *
     * @param type     查询条件值
     * @param key      查询条件列
     * @param pageNo   页码
     * @param pageSize 每页记录数
     * @return
     */

    public List<AchievementEntry> findAchievementEntry(int type, String key, int pageNo, int pageSize) {
        BasicDBObject query = new BasicDBObject();
        if (type != 0) {
            query.append("at", type);
        }
        if (!StringUtils.isEmpty(key)) {
            query.append("ana", MongoUtils.buildRegex(key));
        }
        List<DBObject> dbObjects = find(MongoFacroty.getCloudAppDB(), Constant.COLLECTION_ACHIEVEMENT,
                query, Constant.FIELDS,
                new BasicDBObject("pt", Constant.DESC), (pageNo - Constant.ONE) * pageSize, pageSize);
        List<AchievementEntry> resultList = new ArrayList<AchievementEntry>();
        for (DBObject dbObject : dbObjects) {
            resultList.add(new AchievementEntry((BasicDBObject) dbObject));
        }
        return resultList;
    }

    /**
     * 获取符合条件的科研成果记录总数
     *
     * @param type 查询条件值
     * @param key  查询条件列
     * @return
     */
    public int count(int type, String key) {
        BasicDBObject query = new BasicDBObject();
        if (type != 0) {
            query.append("at", type);
        }
        if (!StringUtils.isEmpty(key)) {
            query.append("ana", MongoUtils.buildRegex(key));
        }
        return this.count(MongoFacroty.getCloudAppDB(), Constant.COLLECTION_ACHIEVEMENT, query);
    }

    /**
     * 根据ID获取对应的对象息
     *
     * @param id
     * @return
     */
    public AchievementEntry getAchievementEntry(ObjectId id) {
        DBObject object = this.findOne(MongoFacroty.getCloudAppDB(),
                Constant.COLLECTION_ACHIEVEMENT,
                new BasicDBObject().append(Constant.ID, id),
                Constant.FIELDS);
        return object == null ? null : new AchievementEntry((BasicDBObject) object);
    }

}
