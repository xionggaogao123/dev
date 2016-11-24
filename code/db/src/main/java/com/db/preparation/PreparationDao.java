package com.db.preparation;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.preparation.PreparationEntry;
import com.sys.constants.Constant;

/**
 * 集体备课Dao
 * 2015-8-22 16:19:09
 *
 * @author cxy
 */
public class PreparationDao extends BaseDao {
    /**
     * 添加一条集体备课信息
     *
     * @param e
     * @return
     */
    public ObjectId addPreparationEntry(PreparationEntry e) {
        save(MongoFacroty.getCloudAppDB(), Constant.COLLECTION_PREPARATION, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 根据ID查询
     *
     * @param userId
     * @return
     */
    public PreparationEntry getPreparationEntryById(ObjectId preparationId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, preparationId);
        DBObject dbo = findOne(MongoFacroty.getCloudAppDB(), Constant.COLLECTION_PREPARATION, query, Constant.FIELDS);
        if (null != dbo) {
            return new PreparationEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * 根据传入的数据字典ID和字段名查询集体备课List
     *
     * @param resourceDictionaryId
     * @param columName
     * @return
     */
    public List<PreparationEntry> getPreparationEntriesByResourceDictionaryId(String resourceDictionaryId, String columName, ObjectId ebeId) {
        BasicDBObject query = new BasicDBObject();
        query.append(columName, resourceDictionaryId).append("edid", ebeId);
        DBObject orderBy = new BasicDBObject("pt", Constant.DESC);
        List<DBObject> dbObjects = find(MongoFacroty.getCloudAppDB(), Constant.COLLECTION_PREPARATION, query, Constant.FIELDS, orderBy);
        List<PreparationEntry> resultList = new ArrayList<PreparationEntry>();
        for (DBObject dbObject : dbObjects) {
            PreparationEntry entry = new PreparationEntry((BasicDBObject) dbObject);
            resultList.add(entry);
        }
        return resultList;
    }

    /**
     * 查询所有的列表信息
     *
     * @param resourceDictionaryId
     * @param columName
     * @return
     */
    public List<PreparationEntry> getAllPreparationEntries(ObjectId ebeId) {
        BasicDBObject query = new BasicDBObject("edid", ebeId);
        DBObject orderBy = new BasicDBObject("pt", Constant.DESC);
        List<DBObject> dbObjects = find(MongoFacroty.getCloudAppDB(), Constant.COLLECTION_PREPARATION, query, Constant.FIELDS, orderBy);
        List<PreparationEntry> resultList = new ArrayList<PreparationEntry>();
        for (DBObject dbObject : dbObjects) {
            PreparationEntry entry = new PreparationEntry((BasicDBObject) dbObject);
            resultList.add(entry);
        }
        return resultList;
    }

    /**
     * 给某一个备课增加一个备选课件
     *
     * @param userId
     * @return
     */
    public void addFileBackForPreparation(ObjectId preparationId, ObjectId fileId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, preparationId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PUSH, new BasicDBObject("csb", fileId));
        update(MongoFacroty.getCloudAppDB(), Constant.COLLECTION_PREPARATION, query, updateValue);
    }

    /**
     * 给某一个备课去除一个备选课件
     *
     * @param userId
     * @return
     */
    public void delFileBackForPreparation(ObjectId preparationId, ObjectId fileId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, preparationId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("csb", fileId));
        update(MongoFacroty.getCloudAppDB(), Constant.COLLECTION_PREPARATION, query, updateValue);
    }

    /**
     * 给某一个备课增加一个课件
     *
     * @param userId
     * @return
     */
    public void addFileForPreparation(ObjectId preparationId, ObjectId fileId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, preparationId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PUSH, new BasicDBObject("cs", fileId));
        update(MongoFacroty.getCloudAppDB(), Constant.COLLECTION_PREPARATION, query, updateValue);
    }

    /**
     * 给某一个备课去除一个课件
     *
     * @param userId
     * @return
     */
    public void delFileForPreparation(ObjectId preparationId, ObjectId fileId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, preparationId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("cs", fileId));
        update(MongoFacroty.getCloudAppDB(), Constant.COLLECTION_PREPARATION, query, updateValue);
    }
}
