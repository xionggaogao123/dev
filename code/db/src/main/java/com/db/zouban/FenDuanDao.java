package com.db.zouban;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.zouban.ClassFengDuanEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * Created by wangkaidong on 2016/6/22.
 */
public class FenDuanDao extends BaseDao {
    /**
     * 添加分段
     *
     * @param classFengDuanEntry
     */
    public void addFenDuan(ClassFengDuanEntry classFengDuanEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FENGDUAN, classFengDuanEntry.getBaseEntry());
    }

    /**
     * 更新分段
     * @param entry
     */
    public void updateFenDuan(ClassFengDuanEntry entry) {
        BasicDBObject query = new BasicDBObject(Constant.ID, entry.getID());
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("xkid", entry.getXuanKeId())
                        .append("group", entry.getGroup())
                        .append("grpnm", entry.getGroupName())
                        .append("cids", entry.getClassIds()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FENGDUAN, query, updateValue);
    }

    /**
     * 删除分段
     *
     * @param xuanKeId
     */
    public void removeFenDuan(ObjectId xuanKeId) {
        DBObject query = new BasicDBObject("xkid", xuanKeId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_FENGDUAN, query);
    }

    /**
     * 根据选课id获取分段情况
     *
     * @param xuankeId
     * @return
     */
    public List<ClassFengDuanEntry> getClassFenduanList(ObjectId xuankeId) {
        BasicDBObject query = new BasicDBObject("xkid", xuankeId);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FENGDUAN, query, Constant.FIELDS);
        List<ClassFengDuanEntry> list = new ArrayList<ClassFengDuanEntry>();
        for (DBObject entry : dbObjectList) {
            list.add(new ClassFengDuanEntry((BasicDBObject) entry));
        }
        return list;
    }

    /**
     * 根据id获取分段
     *
     * @param ids
     * @return
     */
    public List<ClassFengDuanEntry> findFenDuanByIds(List<ObjectId> ids) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FENGDUAN, query, Constant.FIELDS);
        List<ClassFengDuanEntry> fengDuanEntries = new ArrayList<ClassFengDuanEntry>();
        if (list != null && !list.isEmpty()) {
            for (DBObject dbObject : list) {
                fengDuanEntries.add(new ClassFengDuanEntry((BasicDBObject) dbObject));
            }
        }
        return fengDuanEntries;
    }

    /**
     * 根据id获取分段
     *
     * @param groupId
     * @return
     */
    public ClassFengDuanEntry findFenDuanById(ObjectId groupId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, groupId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FENGDUAN, query, null);
        if (null != dbo) {
            return new ClassFengDuanEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * 分段列表
     *
     * @param groupIds
     * @param fields
     * @return
     */
    public Map<ObjectId, ClassFengDuanEntry> findGroupEntryMap(Collection<ObjectId> groupIds, DBObject fields) {
        DBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, groupIds));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FENGDUAN, query, fields);
        Map<ObjectId, ClassFengDuanEntry> retMap = new HashMap<ObjectId, ClassFengDuanEntry>();
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                ClassFengDuanEntry e = new ClassFengDuanEntry((BasicDBObject) dbo);
                retMap.put(e.getID(), e);
            }
        }
        return retMap;
    }


    /**
     * 添加班级到分段
     *
     * @param groupId
     * @param classId
     */
    public void addFenDuanClass(ObjectId groupId, ObjectId classId){
        BasicDBObject query = new BasicDBObject(Constant.ID, groupId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_ADDTOSET, new BasicDBObject("cids", classId));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FENGDUAN, query, updateValue);
    }

    /**
     * 从分段删除班级
     *
     * @param groupId
     * @param classId
     */
    public void removeFenDuanClass(ObjectId groupId, ObjectId classId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, groupId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("cids", classId));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FENGDUAN, query, updateValue);

    }


}
