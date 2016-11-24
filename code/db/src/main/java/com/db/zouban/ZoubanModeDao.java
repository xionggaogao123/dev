package com.db.zouban;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.IdNameValuePair;
import com.pojo.app.NameValuePair;
import com.pojo.utils.MongoUtils;
import com.pojo.zouban.ZoubanModeEntry;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by wangkaidong on 2016/7/13.
 * <p/>
 * 走班模式配置
 */
public class ZoubanModeDao extends BaseDao {

    /**
     * 新增
     *
     * @param zoubanModeEntry
     * @return
     */
    public ObjectId add(ZoubanModeEntry zoubanModeEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_MODE, zoubanModeEntry.getBaseEntry());
        return zoubanModeEntry.getID();
    }

    /**
     * 更新学校
     *
     * @param id
     * @param mode
     */
    public void updateSchool(ObjectId id, List<NameValuePair> mode) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject updateValue = new BasicDBObject("md", MongoUtils.convert(MongoUtils.fetchDBObjectList(mode)));
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, updateValue);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_MODE, query, update);
    }

    /**
     * 更新年级走班模式
     *
     * @param id
     * @param gradeId
     * @param mode
     */
    public void updateGrade(ObjectId id, ObjectId gradeId, int mode) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id).append("gl.id", gradeId);
        BasicDBObject updateValue = new BasicDBObject("gl.$.v", mode);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, updateValue);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_MODE, query, update);
    }

    /**
     * 删除学校
     *
     * @param id
     */
    public void delete(ObjectId id) {
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_MODE, new BasicDBObject(Constant.ID, id));
    }

    /**
     * 查询总数，分页用
     *
     * @return
     */
    public int count() {
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_MODE, Constant.QUERY);
    }

    /**
     * 分页查询
     *
     * @param skip
     * @param limit
     * @return
     */
    public List<ZoubanModeEntry> findZoubanModeList(int skip, int limit) {
        List<ZoubanModeEntry> entryList = new ArrayList<ZoubanModeEntry>();
        List<DBObject> dboList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_MODE, Constant.QUERY, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, skip, limit);
        if (dboList != null) {
            for (DBObject dbo : dboList) {
                entryList.add(new ZoubanModeEntry((BasicDBObject) dbo));
            }
        }
        return entryList;
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public ZoubanModeEntry findZoubanMode(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_MODE, query, Constant.FIELDS);
        if (dbObject == null) {
            return null;
        }
        return new ZoubanModeEntry((BasicDBObject) dbObject);
    }

    /**
     * 获取学校走班模式
     *
     * @param schoolId
     * @return
     */
    public ZoubanModeEntry findSchoolMode(ObjectId schoolId) {
        BasicDBObject query = new BasicDBObject("sid", schoolId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_MODE, query, Constant.FIELDS);
        if (dbObject == null) {
            return null;
        }
        return new ZoubanModeEntry((BasicDBObject) dbObject);
    }

    /**
     * 获取年级走班模式
     *
     * @param schoolId
     * @param gradeId
     * @return
     */
    public int findZoubanMode(ObjectId schoolId, ObjectId gradeId) {
        BasicDBObject query = new BasicDBObject("sid", schoolId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_MODE, query, Constant.FIELDS);
        if (dbObject == null) {
            return -1;
        }
        ZoubanModeEntry zoubanModeEntry = new ZoubanModeEntry((BasicDBObject) dbObject);
        List<IdNameValuePair> gradeList = zoubanModeEntry.getGradeList();
        for (IdNameValuePair inv : gradeList) {
            if (inv.getId().equals(gradeId)) {
                int mode = 0;
                try {
                    mode = (Integer) inv.getValue();
                } catch (ClassCastException e) {
                    mode = Integer.parseInt((String) inv.getValue());
                }
                return mode;
            }
        }
        return 0;
    }

    /**
     * 根据学校名称模糊查询
     *
     * @param key
     * @param skip
     * @param limit
     * @return
     */
    public List<ZoubanModeEntry> findZoubanModeList(String key, int skip, int limit) {
        BasicDBObject query = new BasicDBObject();
        if (!StringUtils.isEmpty(key)) {
            query.append("snm", MongoUtils.buildRegex(key));
        }
        List<ZoubanModeEntry> entryList = new ArrayList<ZoubanModeEntry>();
        List<DBObject> dboList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_MODE, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, skip, limit);
        if (dboList != null) {
            for (DBObject dbo : dboList) {
                entryList.add(new ZoubanModeEntry((BasicDBObject) dbo));
            }
        }
        return entryList;
    }

}
