package com.db.repertory;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.repertory.CoursewareEntry;
import com.pojo.repertory.RepertoryProperty;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

/**
 * 资源-课件DAO
 *
 * @author cxy
 *         2015-9-15 21:35:02
 */
public class CoursewareDao extends BaseDao {
    /**
     * 新增一个课件
     *
     * @param e
     * @return
     */
    public ObjectId addCourseware(CoursewareEntry e) {
        save(MongoFacroty.getResDB(), Constant.COLLECTION_COURSWARE, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 根据传入的数据字典ID和属性分类种类查询相应的课件
     *
     * @return
     */
    private List<CoursewareEntry> getCoursewares(String resourceDictionaryId, String propertyType, int isSaved, ObjectId educationBureauId) {
        BasicDBObject query = new BasicDBObject();
        query.append("ir", Constant.ZERO).append("is", isSaved).append("edid", educationBureauId);
        if (!"".equals(resourceDictionaryId)) {
            query.append("props." + propertyType + ".id", resourceDictionaryId);
        }

        DBObject orderBy = new BasicDBObject("ts", Constant.DESC);
        List<DBObject> dbObjects = find(MongoFacroty.getResDB(), Constant.COLLECTION_COURSWARE, query, Constant.FIELDS, orderBy);
        List<CoursewareEntry> resultList = new ArrayList<CoursewareEntry>();
        for (DBObject dbObject : dbObjects) {
            CoursewareEntry entry = new CoursewareEntry((BasicDBObject) dbObject);
            resultList.add(entry);
        }
        return resultList;
    }

    /**
     * 根据数据字典ID在教材版本中查询未入库信息
     *
     * @param resourceDictionaryId
     * @param propertyType
     * @return
     */
    public List<CoursewareEntry> getCoursewaresNotSavedByIdInVersion(String resourceDictionaryId, ObjectId educationBureauId) {
        return getCoursewares(resourceDictionaryId, "ver", 0, educationBureauId);
    }

    /**
     * 根据数据字典ID在教材版本中查询已入库信息
     *
     * @param resourceDictionaryId
     * @param propertyType
     * @return
     */
    public List<CoursewareEntry> getCoursewaresSavedByIdInVersion(String resourceDictionaryId, ObjectId educationBureauId) {
        return getCoursewares(resourceDictionaryId, "ver", 1, educationBureauId);
    }

    /**
     * 根据数据字典ID在知识点中查询未入库信息
     *
     * @param resourceDictionaryId
     * @param propertyType
     * @return
     */
    public List<CoursewareEntry> getCoursewaresNotSavedByIdInKnowledge(String resourceDictionaryId, ObjectId educationBureauId) {
        return getCoursewares(resourceDictionaryId, "kno", 0, educationBureauId);
    }

    /**
     * 根据数据字典ID在知识点中查询已入库信息
     *
     * @param resourceDictionaryId
     * @param propertyType
     * @return
     */
    public List<CoursewareEntry> getCoursewaresSavedByIdInKnowledge(String resourceDictionaryId, ObjectId educationBureauId) {
        return getCoursewares(resourceDictionaryId, "kno", 1, educationBureauId);
    }

    /**
     * 删除一条
     *
     * @param id
     */
    public void deleteCourseware(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject().append("ir", 1));
        update(MongoFacroty.getResDB(), Constant.COLLECTION_COURSWARE, query, updateValue);
    }

    /**
     * 忽略一条
     *
     * @param id
     */
    public void ignoreCourseware(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject().append("iig", 1));
        update(MongoFacroty.getResDB(), Constant.COLLECTION_COURSWARE, query, updateValue);
    }

    /**
     * 根据Id查询一个
     *
     * @param id
     * @return
     */
    public CoursewareEntry getCoursewareEntry(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbo = findOne(MongoFacroty.getResDB(), Constant.COLLECTION_COURSWARE, query, Constant.FIELDS);
        if (null != dbo) {
            return new CoursewareEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * 把一条改成入库状态
     *
     * @param id
     */
    public void saveToGit(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject().append("is", 1));
        update(MongoFacroty.getResDB(), Constant.COLLECTION_COURSWARE, query, updateValue);
    }

    /**
     * 根据ID更新一条Courseware
     */
    public void updateCourseware(ObjectId id, String coverId, ObjectId fileId, List<RepertoryProperty> properties) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateObj = new BasicDBObject().append("props", MongoUtils.convert(MongoUtils.fetchDBObjectList(properties))).append("coid", coverId).append("fid", fileId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, updateObj);
        update(MongoFacroty.getResDB(), Constant.COLLECTION_COURSWARE, query, updateValue);
    }


    /**
     * 根据传入的数据字典ID和属性分类种类查询前端需要的课件
     *
     * @return
     */
    public List<CoursewareEntry> getCoursewaresForCloud(String resourceDictionaryId, String propertyType, ObjectId educationBureauId) {
        BasicDBObject query = new BasicDBObject();
        query.append("ir", Constant.ZERO).append("edid", educationBureauId);
        if (!"".equals(resourceDictionaryId)) {
            query.append("props." + propertyType + ".id", resourceDictionaryId);
        }

        DBObject orderBy = new BasicDBObject("ts", Constant.DESC);
        List<DBObject> dbObjects = find(MongoFacroty.getResDB(), Constant.COLLECTION_COURSWARE, query, Constant.FIELDS, orderBy);
        List<CoursewareEntry> resultList = new ArrayList<CoursewareEntry>();
        for (DBObject dbObject : dbObjects) {
            CoursewareEntry entry = new CoursewareEntry((BasicDBObject) dbObject);
            resultList.add(entry);
        }
        return resultList;
    }

}
