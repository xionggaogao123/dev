package com.db.zouban;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.zouban.ZoubanConfig;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiangm on 2016/3/14.
 */
public class ZoubanConfigDao extends BaseDao {
    /**
     * 添加
     *
     * @param zoubanConfig
     */
    public void addConfig(ZoubanConfig zoubanConfig) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_CONFIG, zoubanConfig.getBaseEntry());
    }

    /**
     * 删除走班配置
     *
     * @param id
     */
    public void removeConfig(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_CONFIG, query);
    }

    /**
     * 统计总数
     *
     * @return
     */
    public int countConfig() {
        BasicDBObject query = new BasicDBObject();
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_CONFIG, query);
    }

    /**
     * 更新数据
     *
     * @param id
     * @param schoolId
     * @param mode
     */
    public void updateConfig(ObjectId id, ObjectId schoolId, int mode) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("sid", schoolId)
                .append("zbm", mode));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_CONFIG, query, updateValue);
    }

    /**
     * 分页获取配置
     *
     * @param page
     * @param pageSize
     * @return
     */
    public List<ZoubanConfig> findConfig(int page, int pageSize) {
        List<ZoubanConfig> zoubanConfigs = new ArrayList<ZoubanConfig>();
        int skip = pageSize * (page - 1);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_CONFIG, new BasicDBObject(), new BasicDBObject(), new BasicDBObject(),
                skip, pageSize);
        if (list != null && !list.isEmpty()) {
            for (DBObject dbObject : list) {
                zoubanConfigs.add(new ZoubanConfig((BasicDBObject) dbObject));
            }
        }
        return zoubanConfigs;
    }

    /**
     * 根据学校id获取走班模式
     *
     * @param schoolId
     * @return
     */
    public int getModeBySchoolId(ObjectId schoolId) {
        BasicDBObject query = new BasicDBObject("sid", schoolId);
        DBObject object = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_CONFIG, query, new BasicDBObject());
        if (object != null)
            return new ZoubanConfig((BasicDBObject) object).getZoubanMode();
        return 0;
    }

    /**
     * 根据学校id统计，主要用于判断是否已经有存在
     *
     * @param schoolId
     * @return
     */
    public int countBySchoolId(ObjectId schoolId) {
        BasicDBObject query = new BasicDBObject("sid", schoolId);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_CONFIG, query);
    }
}
