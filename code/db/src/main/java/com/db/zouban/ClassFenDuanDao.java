package com.db.zouban;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.utils.MongoUtils;
import com.pojo.zouban.ClassFengDuanEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiangm on 2016/4/26.
 */
public class ClassFenDuanDao extends BaseDao {
    //添加分段
    public void addFenduan(ClassFengDuanEntry classFengDuanEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FENGDUAN, classFengDuanEntry.getBaseEntry());
    }

    //修改分段
    public void updateFenDuan(ObjectId id, ClassFengDuanEntry entry) {
        BasicDBObject basicDBObject = new BasicDBObject(Constant.ID, id);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("xkid", entry.getXuanKeId())
                        .append("group", entry.getGroup())
                        .append("grpnm", entry.getGroupName())
                        .append("cids", entry.getClassIds()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FENGDUAN, basicDBObject, updateValue);
    }

    /**
     * 删除分段
     *
     * @param id
     */
    public void deleteFenduan(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_FENGDUAN, query);
    }

    public List<ClassFengDuanEntry> findClassFenDuanList(ObjectId xuankeId) {
        BasicDBObject query = new BasicDBObject("xkid", xuankeId);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FENGDUAN, query, new BasicDBObject());
        List<ClassFengDuanEntry> fengDuanEntries = new ArrayList<ClassFengDuanEntry>();
        if (list != null && !list.isEmpty()) {
            for (DBObject dbObject : list) {
                fengDuanEntries.add(new ClassFengDuanEntry((BasicDBObject) dbObject));
            }
        }
        return fengDuanEntries;
    }

    public List<ClassFengDuanEntry> findByIds(List<ObjectId> ids) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FENGDUAN, query, new BasicDBObject());
        List<ClassFengDuanEntry> fengDuanEntries = new ArrayList<ClassFengDuanEntry>();
        if (list != null && !list.isEmpty()) {
            for (DBObject dbObject : list) {
                fengDuanEntries.add(new ClassFengDuanEntry((BasicDBObject) dbObject));
            }
        }
        return fengDuanEntries;
    }
}
