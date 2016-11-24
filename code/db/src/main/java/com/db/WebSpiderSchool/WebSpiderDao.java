package com.db.WebSpiderSchool;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.WebSpiderSchool.WebSpiderSchool;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by qiangm on 2016/3/16.
 */
public class WebSpiderDao extends BaseDao {
    public static String tableName = "webSpiderSchool";

    /**
     * 保存
     *
     * @param webSpiderSchool
     */
    public void save(WebSpiderSchool webSpiderSchool) {
        save(MongoFacroty.getAppDB(), tableName, webSpiderSchool.getBaseEntry());
    }

    /**
     * 统计是否有存在
     *
     * @param schoolName
     * @return
     */
    public int count(String schoolName, ObjectId city) {
        BasicDBObject query = new BasicDBObject("sn", schoolName).append("ci", city);
        return count(MongoFacroty.getAppDB(), tableName, query);
    }

    /**
     * 统计是否有存在
     *
     * @param schoolName
     * @return
     */
    public int count(String schoolName) {
        BasicDBObject query = new BasicDBObject("sn", schoolName);
        return count(MongoFacroty.getAppDB(), tableName, query);
    }

    /**
     * 增加多个学校
     *
     * @param rs
     */
    public void addRegionEntrys(Collection<WebSpiderSchool> rs) {
        List<DBObject> list = MongoUtils.fetchDBObjectList(rs);
        if (null != list && !list.isEmpty()) {
            save(MongoFacroty.getAppDB(), tableName, list);
        }
    }

    /**
     * 查询学校
     *
     * @param provinceId
     * @param cityId
     * @param countyId
     * @return
     */
    public List<WebSpiderSchool> getSchoolValueList(String provinceId, String cityId, String countyId) {
        List<WebSpiderSchool> list = new ArrayList<WebSpiderSchool>();
        BasicDBObject query = new BasicDBObject();
        if (StringUtils.isNotBlank(provinceId)) {
            query.append("pr", new ObjectId(provinceId));
        }
        if (StringUtils.isNotBlank(cityId)) {
            query.append("ci", new ObjectId(cityId));
        }
        if (StringUtils.isNotBlank(countyId)) {
            query.append("co", new ObjectId(countyId));
        }
        BasicDBObject sort = new BasicDBObject("test", Constant.DESC);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), tableName, query, Constant.FIELDS, sort);
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                list.add(new WebSpiderSchool((BasicDBObject) dbObject));
            }
        }
        return list;

    }

    /**
     * 获取全部学校
     *
     * @return
     */
    public List<WebSpiderSchool> findAll() {
        List<WebSpiderSchool> list = new ArrayList<WebSpiderSchool>();
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), tableName, new BasicDBObject(), new BasicDBObject());
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                list.add(new WebSpiderSchool((BasicDBObject) dbObject));
            }
        }
        return list;
    }

    /**
     * 修改学校id
     *
     * @param id
     * @param schoolId
     */
    public void updateSchoolId(ObjectId id, ObjectId schoolId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("sid", schoolId));
        update(MongoFacroty.getAppDB(), tableName, query, updateValue);
    }

    /**
     * 修改学校类别
     *
     * @param oldType
     * @param newType
     */
    public void updateSchoolType(int oldType, int newType) {
        BasicDBObject query = new BasicDBObject("st", oldType);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("st", newType));
        update(MongoFacroty.getAppDB(), tableName, query, updateValue);
    }

    public void setTestSchool(String school) {
        BasicDBObject query = new BasicDBObject("sn", school);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("test", 1));
        update(MongoFacroty.getAppDB(), tableName, query, updateValue);
    }

    public void setAllTest() {
        BasicDBObject query = new BasicDBObject();
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("test", 0));
        update(MongoFacroty.getAppDB(), tableName, query, updateValue);
    }
}
