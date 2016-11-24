package com.db.resources;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.resources.ResourceEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.exceptions.ResultTooManyException;

/**
 * 资源操作类
 *
 * @author fourer
 */
public class CloudResourceDao extends BaseDao {

    /**
     * 添加
     *
     * @param e
     * @return
     */
    public ObjectId addResource(ResourceEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CLOUD_RESOURCES, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 更改字段值
     *
     * @param id
     * @param field
     * @param value
     * @throws IllegalParamException
     */
    public void update(ObjectId id, String field, Object value) throws IllegalParamException {
        if (field.equalsIgnoreCase("nm") || field.equalsIgnoreCase("lng") || field.equalsIgnoreCase("vsty")) {
            throw new IllegalParamException();
        }
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject(field, value));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CLOUD_RESOURCES, query, updateValue);
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public ResourceEntry getResourceEntryById(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CLOUD_RESOURCES, query, Constant.FIELDS);
        if (null != dbo) {
            return new ResourceEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * 根据persistentid查询
     *
     * @param persistentid
     * @return
     */
    public ResourceEntry getResourceEntryByPersistentId(String persistentid) {
        DBObject query = new BasicDBObject("pid", persistentid);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CLOUD_RESOURCES, query, Constant.FIELDS);
        if (null != dbo) {
            return new ResourceEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * 根据ID集合查询，并且返回map形式
     *
     * @param col
     * @param fields
     * @return
     */
    public Map<ObjectId, ResourceEntry> getResourceEntryMap(Collection<ObjectId> col, DBObject fields) {
        Map<ObjectId, ResourceEntry> map = new HashMap<ObjectId, ResourceEntry>();
        DBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, col));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLOUD_RESOURCES, query, fields);
        if (null != list && !list.isEmpty()) {
            ResourceEntry e;
            for (DBObject dbo : list) {
                e = new ResourceEntry((BasicDBObject) dbo);
                map.put(e.getID(), e);
            }
        }
        return map;
    }


    /**
     * 查找资源
     *
     * @param type     资源类型；不为-1时生效
     * @param name
     * @param userId
     * @param schoolId
     * @param scs      知识点
     * @param psbs     章节
     * @param skip
     * @param limit
     * @return
     * @throws ResultTooManyException
     */
    public List<ResourceEntry> getgetResourceEntryList(int type, String name, ObjectId userId, ObjectId schoolId, Collection<ObjectId> scs, Collection<ObjectId> psbs, int skip, int limit) throws ResultTooManyException {
        BasicDBObject query = new BasicDBObject();
        if (type != -1) {
            query.append("ty", type);
        }
        if (StringUtils.isNotBlank(name)) {
            query.append("nm", MongoUtils.buildRegex(name));
        }
        if (null != userId) {
            query.append("ui", userId);
        }
        if (null != schoolId) {
            query.append("si", schoolId);
        }
        if (null != scs && !scs.isEmpty()) {
            query.append("scs", new BasicDBObject(Constant.MONGO_IN, scs));
        }
        if (null != psbs && !psbs.isEmpty()) {
            query.append("psbs", new BasicDBObject(Constant.MONGO_IN, psbs));
        }

        if (query.isEmpty())
            throw new ResultTooManyException();

        List<ResourceEntry> retList = new ArrayList<ResourceEntry>();
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLOUD_RESOURCES, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, skip, limit);

        if (null != list && !list.isEmpty()) {
            ResourceEntry e;
            for (DBObject dbo : list) {
                e = new ResourceEntry((BasicDBObject) dbo);
                retList.add(e);
            }
        }
        return retList;

    }

    /**
     * 忽略一条
     *
     * @param id
     */
    public void ignoreResource(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject().append("iig", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CLOUD_RESOURCES, query, updateValue);
    }

    /**
     * 删除一条
     *
     * @param id
     */
    public void removeById(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_CLOUD_RESOURCES, query);
    }

}
