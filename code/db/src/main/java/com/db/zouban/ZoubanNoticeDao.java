package com.db.zouban;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.zouban.ZoubanNotice;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiangm on 2015/11/9.
 */
public class ZoubanNoticeDao extends BaseDao {

    /**
     * 获取走班通知列表
     *
     * @param term
     * @param schoolId
     * @param gradeId
     * @param type
     * @return
     */
    public List<ZoubanNotice> findNoticeList(String term, ObjectId schoolId, ObjectId gradeId, int type, int page, int pageSize) {
        List<ZoubanNotice> zoubanNoticeList = new ArrayList<ZoubanNotice>();
        BasicDBObject query = new BasicDBObject();
        query.append("te", term);
        query.append("sid", schoolId);
        query.append("gid", gradeId);
        query.append("df", Constant.ZERO);
        int skip = pageSize * (page - 1);
        if (type != -1)
            query.append("ty", type);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_NOTICE, query, Constant.FIELDS, new BasicDBObject(Constant.ID, -1), skip, pageSize);
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                zoubanNoticeList.add(new ZoubanNotice((BasicDBObject) dbObject));
            }
        }
        return zoubanNoticeList;
    }

    /**
     * 统计共有多少条走班通知
     *
     * @param term
     * @param schoolId
     * @param gradeId
     * @param type
     * @return
     */
    public int countNotice(String term, ObjectId schoolId, ObjectId gradeId, int type) {
        BasicDBObject query = new BasicDBObject();
        query.append("te", term);
        query.append("sid", schoolId);
        query.append("gid", gradeId);
        query.append("df", Constant.ZERO);
        if (type != -1)
            query.append("ty", type);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_NOTICE, query);
    }

    /**
     * 获取走班详情
     *
     * @param id
     * @return
     */
    public ZoubanNotice findNoticeById(ObjectId id) {
        BasicDBObject query = new BasicDBObject()
                .append(Constant.ID, id)
                .append("df", Constant.ZERO);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_NOTICE, query, Constant.FIELDS);
        if (dbObject != null) {
            return new ZoubanNotice((BasicDBObject) dbObject);
        }
        return null;
    }

    /**
     * 新建调课通知
     *
     * @param zoubanNotice
     */
    public void addNotice(ZoubanNotice zoubanNotice) {
        zoubanNotice.setDeleteFlag(0);
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_NOTICE, zoubanNotice.getBaseEntry());
    }

    /**
     * 删除调课通知，修改delete标志位
     *
     * @param id
     */
    public void deleteZoubanNotice(ObjectId id) {
        BasicDBObject query = new BasicDBObject()
                .append(Constant.ID, id);
        BasicDBObject updateValue = new BasicDBObject()
                .append(Constant.MONGO_SET, new BasicDBObject("df", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_NOTICE, query, updateValue);
    }

    /**
     * 获取和本班相关的通知
     *
     * @param term
     * @param schoolId
     * @param gradeId
     * @param classId
     * @return
     */
    public List<ZoubanNotice> findNoticeListByClass(String term, ObjectId schoolId, ObjectId gradeId, ObjectId classId) {
        List<ZoubanNotice> zoubanNoticeList = new ArrayList<ZoubanNotice>();
        BasicDBObject query = new BasicDBObject();
        query.append("te", term);
        query.append("sid", schoolId);
        query.append("gid", gradeId);
        query.append("cl", classId);
        query.append("df", Constant.ZERO);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_NOTICE, query, Constant.FIELDS, new BasicDBObject(Constant.ID, -1));
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                zoubanNoticeList.add(new ZoubanNotice((BasicDBObject) dbObject));
            }
        }
        return zoubanNoticeList;
    }

    /**
     * 获取和本年级相关的通知
     *
     * @param term
     * @param schoolId
     * @param gradeId
     * @return
     */
    public List<ZoubanNotice> findNoticeListByGrade(String term, ObjectId schoolId, ObjectId gradeId) {
        List<ZoubanNotice> zoubanNoticeList = new ArrayList<ZoubanNotice>();
        BasicDBObject query = new BasicDBObject();
        query.append("te", term);
        query.append("sid", schoolId);
        query.append("gid", gradeId);
        query.append("df", Constant.ZERO);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_NOTICE, query, Constant.FIELDS, new BasicDBObject(Constant.ID, -1));
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                zoubanNoticeList.add(new ZoubanNotice((BasicDBObject) dbObject));
            }
        }
        return zoubanNoticeList;
    }

    /**
     * 删除通知
     *
     * @param term
     * @param gradeId
     */
    public void deleteAllNotice(String term, ObjectId gradeId) {
        BasicDBObject query = new BasicDBObject()
                .append("te", term)
                .append("gid", gradeId);
        BasicDBObject updateValue = new BasicDBObject()
                .append(Constant.MONGO_SET, new BasicDBObject("df", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_NOTICE, query, updateValue);
    }
}
