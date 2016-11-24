package com.db.lesson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.lesson.DirEntry;
import com.sys.constants.Constant;

/**
 * 目录操作
 * index:ow_pi_ty_so
 * {"ow":1,"pi":1,"ty":1,"so":1}
 *
 * @author fourer
 */
public class DirDao extends BaseDao {

    /**
     * 增加一个目录
     *
     * @param e
     * @return
     */
    public ObjectId addDirEntry(DirEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_DIR_NAME, e.getBaseEntry());
        return e.getID();
    }


    /**
     * 详情
     *
     * @param id
     * @return
     */
    public DirEntry getDirEntry(ObjectId id, ObjectId ownId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        if (null != ownId) {
            query.put("ow", ownId);
        }
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_DIR_NAME, query, Constant.FIELDS);
        if (null != dbo) {
            return (new DirEntry((BasicDBObject) dbo));
        }
        return null;
    }


    /**
     * 根据拥有者和父目录查询
     *
     * @param owerID
     * @param parentId
     * @return
     */
    public List<DirEntry> getDirEntryList(ObjectId owerID, ObjectId parentId) {
        List<DirEntry> retList = new ArrayList<DirEntry>();
        DBObject query = new BasicDBObject();
        if (null != owerID) {
            query.put("ow", owerID);
        }
        if (null != parentId) {
            query.put("pi", parentId);
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_DIR_NAME, query, Constant.FIELDS);
        if (null != list && list.size() > 0) {
            for (DBObject dbo : list) {
                retList.add(new DirEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }


    /**
     * 根据拥有者和父目录查询
     *
     * @param owerID
     * @param parentId
     * @return
     */
    public List<DirEntry> getDirEntryList(Collection<ObjectId> owerIDs, DBObject fields, int dirtype) {
        List<DirEntry> retList = new ArrayList<DirEntry>();
        DBObject query = new BasicDBObject("ow", new BasicDBObject(Constant.MONGO_IN, owerIDs));
        if (dirtype != -1) {
            query.put("ty", dirtype);
        }

        DBObject orderBy = new BasicDBObject("so", Constant.ASC);


        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_DIR_NAME, query, fields, orderBy);
        if (null != list && list.size() > 0) {
            for (DBObject dbo : list) {
                retList.add(new DirEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }


    /**
     * 根据拥有者和父目录查询
     *
     * @param owerID
     * @param parentId
     * @return
     */
    public List<DirEntry> getDirEntryListByIds(Collection<ObjectId> iDs, DBObject fields) {
        List<DirEntry> retList = new ArrayList<DirEntry>();
        DBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, iDs));

        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_DIR_NAME, query, fields);
        if (null != list && list.size() > 0) {
            for (DBObject dbo : list) {
                retList.add(new DirEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }

    /**
     * 根据ownId删除
     *
     * @param ownId---tcsubject的_id
     */
    public void deleteByOwnId(ObjectId ownId) {
        BasicDBObject query = new BasicDBObject("ow", ownId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_DIR_NAME, query);
    }


    /**
     * 删除多个目录
     *
     * @param ids
     */
    public void removeDirs(Collection<ObjectId> ids) {
        DBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids));
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_DIR_NAME, query);
    }

    /**
     * 更新字段值
     *
     * @param dirId
     * @param field
     * @param value
     */
    public void update(ObjectId dirId, String field, Object value) {
        BasicDBObject query = new BasicDBObject(Constant.ID, dirId);
        BasicDBObject valueDBO = new BasicDBObject(field, value);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, valueDBO);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_DIR_NAME, query, updateValue);
    }


    public void updateTopClassDirByOwnerId(ObjectId ownerId, String field, Object value) {
        BasicDBObject query = new BasicDBObject("ow", ownerId).append("pi", null);
        BasicDBObject valueDBO = new BasicDBObject(field, value);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, valueDBO);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_DIR_NAME, query, updateValue);
    }

    /**
     * 移动dir
     *
     * @param id
     * @param parentId
     * @param order
     */
    public void move(ObjectId id, ObjectId parentId, int order) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("pi", parentId).append("so", order));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_DIR_NAME, query, updateValue);
    }
}
