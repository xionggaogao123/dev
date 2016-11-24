package com.db.teacherevaluation;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.IdValuePair;
import com.pojo.teacherevaluation.ElementEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fl on 2016/4/21.
 */
public class ElementDao extends BaseDao {

    private static final String COLLECTION_NAME = Constant.COLLECTION_TE_ELEMENT;

    /**
     * 考核元素或量化成绩列表
     *
     * @param schoolId
     * @param year
     * @param type
     * @return
     */
    public List<ElementEntry> getElements(ObjectId schoolId, String year, int type) {
        List<ElementEntry> retList = new ArrayList<ElementEntry>();
        DBObject query = new BasicDBObject("si", schoolId).append("y", year).append("ty", type);
        List<DBObject> list = find(MongoFacroty.getAppDB(), COLLECTION_NAME, query, Constant.FIELDS);
        if (list != null && list.size() > 0) {
            for (DBObject dbObject : list) {
                retList.add(new ElementEntry((BasicDBObject) dbObject));
            }
        }
        return retList;
    }

    /**
     * 获取考核元素或量化成绩
     *
     * @param elementId
     * @return
     */
    public ElementEntry getElement(ObjectId elementId) {
        DBObject query = new BasicDBObject(Constant.ID, elementId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), COLLECTION_NAME, query, Constant.FIELDS);
        return new ElementEntry((BasicDBObject) dbObject);
    }

    /**
     * 增加考核元素或量化成绩
     *
     * @param elementEntry
     * @return
     */
    public ObjectId addElement(ElementEntry elementEntry) {
        save(MongoFacroty.getAppDB(), COLLECTION_NAME, elementEntry.getBaseEntry());
        return elementEntry.getID();
    }

    /**
     * 删除考核元素或量化成绩
     *
     * @param elementId
     */
    public void deleteElement(ObjectId elementId) {
        DBObject query = new BasicDBObject(Constant.ID, elementId);
        remove(MongoFacroty.getAppDB(), COLLECTION_NAME, query);
    }

    /**
     * 更新考核元素或量化成绩
     *
     * @param elementId
     * @param score
     * @param name
     */
    public void updateElement(ObjectId elementId, double score, String name) {
        DBObject query = new BasicDBObject(Constant.ID, elementId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("sc", score).append("nm", name));
        update(MongoFacroty.getAppDB(), COLLECTION_NAME, query, updateValue);
    }

    /**
     * 添加考核内容
     *
     * @param elementId
     * @param content
     */
    public void addContent(ObjectId elementId, IdValuePair content) {
        DBObject query = new BasicDBObject(Constant.ID, elementId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PUSH, new BasicDBObject("con", content.getBaseEntry()));
        update(MongoFacroty.getAppDB(), COLLECTION_NAME, query, updateValue);
    }

    /**
     * 更新考核内容
     *
     * @param elementId
     * @param contentId
     * @param value
     */
    public void updateContent(ObjectId elementId, ObjectId contentId, String value) {
        DBObject query = new BasicDBObject(Constant.ID, elementId).append("con.id", contentId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("con.$.v", value));
        update(MongoFacroty.getAppDB(), COLLECTION_NAME, query, updateValue);
    }

    /**
     * 删除考核内容
     *
     * @param elementId
     * @param content
     */
    public void deleteContent(ObjectId elementId, IdValuePair content) {
        DBObject query = new BasicDBObject(Constant.ID, elementId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("con", content.getBaseEntry()));
        update(MongoFacroty.getAppDB(), COLLECTION_NAME, query, updateValue);
    }
}
