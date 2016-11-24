package com.db.forum;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.pojo.forum.FSectionEntry;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/5/31.
 */
public class FSectionDao extends BaseDao {


    public List<FSectionEntry> getFSection() {
        List<FSectionEntry> retList = new ArrayList<FSectionEntry>();
        BasicDBObject query = new BasicDBObject();
        List<DBObject> dbObjectList = find(getDB(), getCollection(), query, null);
        for (DBObject dbObject : dbObjectList) {
            retList.add(new FSectionEntry((BasicDBObject) dbObject));
        }
        return retList;
    }

    public ObjectId addFSectionEntry(FSectionEntry fSectionEntry) {
        save(getDB(), getCollection(), fSectionEntry.getBaseEntry());
        return fSectionEntry.getID();
    }

    public void updateSection(String id, String name, String snm, String mmn) {
        DBObject query = new BasicDBObject(Constant.ID, new ObjectId(id));
        BasicDBObject value = new BasicDBObject();
        if (StringUtils.isNotBlank(name)) {
            value.put("nm", name);
            value.put("mm", name);
        } else if (StringUtils.isNotBlank(snm)) {
            value.put("snm", snm);
        } else if (StringUtils.isNotBlank(mmn)) {
            value.put("mmn", mmn);
        }
        DBObject update = new BasicDBObject(Constant.MONGO_SET, value);
        update(getDB(), getCollection(), query, update);
    }


    public ObjectId updateFSectionEntry(FSectionEntry entry) {
        DBObject query = new BasicDBObject(Constant.ID, entry.getID());
        DBObject update = new BasicDBObject("nm", entry.getName());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, update);
        update(getDB(), getCollection(), query, updateValue);
        return entry.getID();
    }

    /**
     * 更新板块排序
     */
    public void updateFSectionEntrySort(ObjectId selfId, int selfSort, ObjectId anotherId, int anotherSort) {
        DBObject query1 = new BasicDBObject(Constant.ID, selfId);
        DBObject update1 = new BasicDBObject("st", selfSort);
        DBObject updateValue1 = new BasicDBObject(Constant.MONGO_SET, update1);
        update(getDB(), getCollection(), query1, updateValue1);

        DBObject query2 = new BasicDBObject(Constant.ID, anotherId);
        DBObject update2 = new BasicDBObject("st", anotherSort);
        DBObject updateValue2 = new BasicDBObject(Constant.MONGO_SET, update2);
        update(getDB(), getCollection(), query2, updateValue2);
    }

    /**
     * 更新板块首页图片
     */
    public void updateFSectionEntryImg(ObjectId id, String type, String imgUrl) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject update = new BasicDBObject(type, imgUrl);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, update);
        update(getDB(), getCollection(), query, updateValue);
    }

    /**
     * 更新回帖量和总发帖量
     *
     * @param id
     */
    public void updateReplyAndPost(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_INC, new BasicDBObject("pc", 1));
        update(getDB(), getCollection(), query, updateValue);
    }

    public void updateTotalComment(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_INC, new BasicDBObject("tcc", 1));
        update(getDB(), getCollection(), query, updateValue);
    }

    /**
     * 更新发帖量和总发帖量
     *
     * @param id
     */
    public void updateThemeAndPost(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_INC, new BasicDBObject("pc", 1));
        update(getDB(), getCollection(), query, updateValue);
    }

    public void updateThemeCount(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_INC, new BasicDBObject("tc", 1));
        update(getDB(), getCollection(), query, updateValue);
    }

    /**
     * 更新总浏览量
     */
    public void updateTotalScanCount(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_INC, new BasicDBObject("tsc", 1));
        update(getDB(), getCollection(), query, updateValue);
    }

    /**
     * 删除商品分类及其子分类
     *
     * @param fSectionEntryId
     */
    public void deleteFSectionEntry(ObjectId fSectionEntryId) {
        DBObject query = new BasicDBObject();
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject("pid", fSectionEntryId));
        values.add(new BasicDBObject(Constant.ID, fSectionEntryId));
        query.put(Constant.MONGO_OR, values);
        remove(getDB(), getCollection(), query);
    }

    /**
     * 得到板块
     *
     * @param fSectionEntryId
     * @return
     */
    public FSectionEntry getFSection(ObjectId fSectionEntryId) {
        DBObject query = new BasicDBObject(Constant.ID, fSectionEntryId);
        DBObject dbObject = findOne(getDB(), getCollection(), query, Constant.FIELDS);
        return new FSectionEntry((BasicDBObject) dbObject);
    }

    /**
     * 根据等级得到板块
     *
     * @param level
     * @return
     */
    public List<FSectionEntry> getFSectionListByLevel(int level, ObjectId id, String name) {
        BasicDBObject query = new BasicDBObject("lvl", level);
        DBObject sort = new BasicDBObject("st", 1);
        if (id != null) {
            query.append("_id", id);
        }
        if (StringUtils.isNotBlank(name)) {
            query.append("nm", name);
        }
        List<DBObject> dbObjectList = find(getDB(), getCollection(), query, Constant.FIELDS, sort);
        List<FSectionEntry> retList = new ArrayList<FSectionEntry>();
        if (dbObjectList != null && dbObjectList.size() > 0) {
            for (DBObject dbo : dbObjectList) {
                FSectionEntry fSectionEntry = new FSectionEntry((BasicDBObject) dbo);
                retList.add(fSectionEntry);
            }
        }
        return retList;
    }

    private String getCollection() {
        return Constant.COLLECTION_FORUM_SECTION;
    }

}
