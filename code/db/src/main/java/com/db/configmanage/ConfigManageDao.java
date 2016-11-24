package com.db.configmanage;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.configmanage.ConfigManageEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 配置管理
 *
 * @author guo
 */
public class ConfigManageDao extends BaseDao {

    /**
     * 添加配置管理信息
     *
     * @param e
     * @return
     */
    public ObjectId addConfigManageEntry(ConfigManageEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_SYSTEM_CONFIG, e.getBaseEntry());
        return e.getID();
    }


    /**
     * 查询配置管理信息
     *
     * @param code
     */
    public ConfigManageEntry getConfigManageEntry(int code) {
        BasicDBObject query = new BasicDBObject("cd", code);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_SYSTEM_CONFIG, query, Constant.FIELDS);
        if (null != dbo) {
            return new ConfigManageEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * 获取全部配置福安里信息
     *
     * @return
     */
    public List<ConfigManageEntry> selAllConfigManageEntry() {
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_SYSTEM_CONFIG, new BasicDBObject(), Constant.FIELDS);
        List<ConfigManageEntry> list = new ArrayList<ConfigManageEntry>();
        for (DBObject dbObject : dbObjects) {
            list.add(new ConfigManageEntry((BasicDBObject) dbObject));
        }
        return list;
    }

    /**
     * 修改配置管理信息
     *
     * @param code  用户id
     * @param value 宠物新名称
     */
    public void updateConfigManageValue(int code, String value) {
        DBObject query = new BasicDBObject("cd", code);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("vl", value));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SYSTEM_CONFIG, query, updateValue);
    }
}
