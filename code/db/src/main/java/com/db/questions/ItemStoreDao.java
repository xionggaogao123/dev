package com.db.questions;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.questions.ItemProperty;
import com.pojo.questions.ItemStoreEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

/**
 * @author Zoukai
 */

public class ItemStoreDao extends BaseDao {

    /**
     * 根据id查询一个
     *
     * @return
     */
    public ItemStoreEntry getItemStoreEntry(ObjectId id) {

        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbo = findOne(MongoFacroty.getCloudAppDB(), Constant.COLLECTION_ITEM__STORE, query, Constant.FIELDS);
        if (null != dbo) {
            return new ItemStoreEntry((BasicDBObject) dbo);
        }
        return null;
    }


    /*
     * 全查询
     */
    public List<ItemStoreEntry> fandAll(String questionTopic) {
        List<ItemStoreEntry> resultList = new ArrayList<ItemStoreEntry>();
        BasicDBObject query = null;
        DBObject orderBy = new BasicDBObject("pti", Constant.DESC);
        if (!"全部".equals(questionTopic)) {
            query = new BasicDBObject().append("qt", questionTopic);
            List<DBObject> dbObjects = find(MongoFacroty.getCloudAppDB(), Constant.COLLECTION_ITEM__STORE, query, Constant.FIELDS, orderBy);
            for (DBObject dbObject : dbObjects) {
                ItemStoreEntry entry = new ItemStoreEntry((BasicDBObject) dbObject);
                resultList.add(entry);
            }

        }
        if ("全部".equals(questionTopic)) {
            query = new BasicDBObject();
            List<DBObject> dbObjects = find(MongoFacroty.getCloudAppDB(), Constant.COLLECTION_ITEM__STORE, query, Constant.FIELDS, orderBy);

            for (DBObject dbObject : dbObjects) {
                ItemStoreEntry entry = new ItemStoreEntry((BasicDBObject) dbObject);
                resultList.add(entry);
            }

        }

        return resultList;

    }

    //增加新题
    public boolean addQuestions(ItemStoreEntry ques) {
        save(MongoFacroty.getCloudAppDB(), Constant.COLLECTION_ITEM__STORE, ques.getBaseEntry());
        return true;
    }

    /**
     * 根据传入的数据字典ID和属性分类种类查询相应的问题
     *
     * @return
     */
    public List<ItemStoreEntry> getItem(String id, String propertyType, String questionTopic, int isSaved) {
        List<ItemStoreEntry> resultList = new ArrayList<ItemStoreEntry>();
        BasicDBObject query = null;
        DBObject orderBy = new BasicDBObject("pti", Constant.DESC);
        if (!StringUtils.isBlank(propertyType) && !"全部".equals(questionTopic)) {
            query = new BasicDBObject().append("pops." + propertyType + ".id", id).append("qt", questionTopic).append("ir", Constant.ZERO).append("is", isSaved);
            ;
            List<DBObject> dbObjects = find(MongoFacroty.getCloudAppDB(), Constant.COLLECTION_ITEM__STORE, query, Constant.FIELDS, orderBy);
            for (DBObject dbObject : dbObjects) {
                ItemStoreEntry entry = new ItemStoreEntry((BasicDBObject) dbObject);
                resultList.add(entry);
            }

        }
        if ("全部".equals(questionTopic)) {
            query = new BasicDBObject().append("pops." + propertyType + ".id", id).append("ir", Constant.ZERO).append("is", isSaved);
            ;
            List<DBObject> dbObjects = find(MongoFacroty.getCloudAppDB(), Constant.COLLECTION_ITEM__STORE, query, Constant.FIELDS, orderBy);

            for (DBObject dbObject : dbObjects) {
                ItemStoreEntry entry = new ItemStoreEntry((BasicDBObject) dbObject);
                resultList.add(entry);
            }

        }

        return resultList;

    }


    /**
     * 根据数据字典ID在同步教材版本中查询未入库信息
     *
     * @param resourceDictionaryId
     * @param propertyType
     * @return
     */
    public List<ItemStoreEntry> getItemNotSavedByIdInVersion(String resourceDictionaryId, String questionTopic) {
        return getItem(resourceDictionaryId, "tcv", questionTopic, 0);
    }

    /**
     * 根据数据字典ID在教材版本中查询已入库信息
     *
     * @param resourceDictionaryId
     * @param propertyType
     * @return
     */
    public List<ItemStoreEntry> getItemSavedByIdInVersion(String resourceDictionaryId, String questionTopic) {
        return getItem(resourceDictionaryId, "tcv", questionTopic, 1);
    }

    /**
     * 根据数据字典ID在综合知识点中查询未入库信息
     *
     * @param resourceDictionaryId
     * @param propertyType
     * @return
     */
    public List<ItemStoreEntry> getItemNotSavedByIdInKnowledge(String resourceDictionaryId, String questionTopic) {
        return getItem(resourceDictionaryId, "kpn", questionTopic, 0);
    }

    /**
     * 根据数据字典ID在综合知识点中查询已入库信息
     *
     * @param resourceDictionaryId
     * @param propertyType
     * @return
     */
    public List<ItemStoreEntry> getItemSavedByIdInKnowledge(String resourceDictionaryId, String questionTopic) {
        return getItem(resourceDictionaryId, "kpn", questionTopic, 1);

    }

    /**
     * 删除题目
     *
     * @param _id
     */
    public void removeItem(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        remove(MongoFacroty.getCloudAppDB(), Constant.COLLECTION_ITEM__STORE, query);
    }

    //更新
    public void updateStatus(ObjectId id, int status) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject updateObj = new BasicDBObject("is", status);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, updateObj);
        update(MongoFacroty.getCloudAppDB(), Constant.COLLECTION_ITEM__STORE, query, updateValue);
    }

    /**
     * 更新题目
     *
     * @param _id
     */
    public void updateItem(ObjectId id, String questionTopic, String contentOfQuestion, String rightAnswer,
                           String answerAnalysis, List<ItemProperty> properties) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);

        DBObject updateObj = new BasicDBObject("qt", questionTopic).append("coq", contentOfQuestion)
                .append("ria", rightAnswer)
                .append("ani", answerAnalysis)
                .append("pops", MongoUtils.convert(MongoUtils.fetchDBObjectList(properties)));
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, updateObj);

        update(MongoFacroty.getCloudAppDB(), Constant.COLLECTION_ITEM__STORE, query, updateValue);
    }
}
