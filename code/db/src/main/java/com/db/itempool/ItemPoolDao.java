package com.db.itempool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.exercise.ExerciseItemType;
import com.pojo.itempool.ItemPoolEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;


/**
 * 题库操作类
 * index:gtys_sty_lel_ty_oty_sc_clty_psbs1_psbs2_grs
 * {"gtys":1,"sty":1,"lel":1,"ty":1,"oty":1,"sc":1,"clty":1,"psbs1":1,"psbs2":1,"grs":1}
 *
 * @author fourer
 */
public class ItemPoolDao extends BaseDao {

    private String getDBName(Boolean isCloud) {
        if (isCloud) {
            return Constant.COLLECTION_ITEM_POOL;
        }
        return Constant.COLLECTION_USER_ITEM_POOL;
    }

    /**
     * 增加
     *
     * @param e
     * @return
     */
    public ObjectId addItemPoolEntry(ItemPoolEntry e, Boolean isCloud) {
        save(MongoFacroty.getResDB(), getDBName(isCloud), e.getBaseEntry());
        return e.getID();
    }

    /**
     * 增加多个
     *
     * @param
     * @return
     */
    public void addItemPoolEntrys(List<ItemPoolEntry> list, Boolean isCloud) {
        save(MongoFacroty.getResDB(), getDBName(isCloud), MongoUtils.fetchDBObjectList(list));
    }


    /**
     * 详情
     *
     * @param id
     * @return
     */
    public ItemPoolEntry getItemPoolEntry(ObjectId id, Boolean isCloud) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbo = findOne(MongoFacroty.getResDB(), getDBName(isCloud), query, Constant.FIELDS);
        if (null != dbo) {
            return new ItemPoolEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * 更新
     *
     * @param id
     * @param field
     * @param value
     * @throws IllegalParamException
     */
    public void update(ObjectId id, String field, Object value) throws IllegalParamException {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject valueDBO = new BasicDBObject(field, value);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, valueDBO);
        update(MongoFacroty.getResDB(), Constant.COLLECTION_ITEM_POOL, query, updateValue);
    }


    /**
     * 根据知识点查询个数
     *
     * @param kws
     * @param level
     * @return result like this:[{"_id":1,"count":100}]
     */
    public List<BasicDBObject> count(Collection<ObjectId> kws, int level) {
        List<BasicDBObject> retList = new ArrayList<BasicDBObject>();

        DBObject dbo = new BasicDBObject("scs", new BasicDBObject(Constant.MONGO_IN, kws));
        if (level != -1) {
            dbo.put("lel", level);
        }
        DBObject query = new BasicDBObject(Constant.MONGO_MATCH, dbo);
        DBObject group = new BasicDBObject(Constant.MONGO_GROUP, new BasicDBObject(Constant.ID, "$oty").append("count", new BasicDBObject(Constant.MONGO_SUM, 1)));
        AggregationOutput output;
        try {
            output = aggregate(MongoFacroty.getResDB(), Constant.COLLECTION_ITEM_POOL, query, group);
            Iterator<DBObject> iter = output.results().iterator();
            BasicDBObject dbob;
            while (iter.hasNext()) {
                dbob = (BasicDBObject) iter.next();
                retList.add(dbob);
            }
        } catch (Exception e) {

        }
        return retList;
    }

    /**
     * 根据id查询，返回map
     *
     * @param ids
     * @param fields
     * @return
     */
    public Map<ObjectId, ItemPoolEntry> getItemPoolEntryMap(Collection<ObjectId> ids, DBObject fields, Boolean isCloud) {
        Map<ObjectId, ItemPoolEntry> retMap = new HashMap<ObjectId, ItemPoolEntry>();
        DBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids));

        List<DBObject> list = find(MongoFacroty.getResDB(), getDBName(isCloud), query, fields);

        if (null != list) {
            ItemPoolEntry e;
            for (DBObject dbo : list) {
                e = new ItemPoolEntry((BasicDBObject) dbo);
                retMap.put(e.getID(), e);
            }
        }
        return retMap;
    }

    public List<ItemPoolEntry> getItemPoolEntryList(Collection<ObjectId> ids, int level, List<Integer> itemType, int skip, int limit, DBObject fields) {
        List<ItemPoolEntry> entryList = new ArrayList<ItemPoolEntry>();
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids));
        if (level > -1) {
            query.append("lel", level);
        }
        if (null != itemType && !itemType.isEmpty()) {
            query.append("oty", new BasicDBObject(Constant.MONGO_IN, itemType));
        }

        List<DBObject> list = find(MongoFacroty.getResDB(), Constant.COLLECTION_ITEM_POOL, query, fields, new BasicDBObject(), skip, limit);

        if (null != list) {
            ItemPoolEntry e;
            for (DBObject dbo : list) {
                e = new ItemPoolEntry((BasicDBObject) dbo);
                entryList.add(e);
            }
        }
        return entryList;
    }

    /**
     * @param gtys        学段
     * @param subjectType 学科
     * @param level       级别
     * @param itemType    题目类型
     * @param scs         知识面
     * @param psbs        知识点
     * @param order
     * @param field
     * @param skip
     * @param limit
     * @return
     * @throws Exception
     */
    public List<ItemPoolEntry> getItemPoolEntryList(ObjectId gtys, ObjectId subjectType, int level,
                                                    List<ObjectId> itemType, List<ObjectId> scs, List<ObjectId> psbs, List<Integer> origItemType, List<ObjectId> owns, String regular, DBObject order, DBObject field, int skip, int limit, Boolean isCloud) throws Exception {
        BasicDBObject query = buildQuery(gtys, subjectType, level, itemType, scs, psbs, origItemType, owns, regular);

        List<ItemPoolEntry> retList = new ArrayList<ItemPoolEntry>();

        List<DBObject> list = find(MongoFacroty.getResDB(), getDBName(isCloud), query, field, order, skip, limit);

        if (null != list) {
            for (DBObject dbo : list) {
                retList.add(new ItemPoolEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }

    public int countItemPoolEntryList(ObjectId gtys, ObjectId subjectType, int level,
                                      List<ObjectId> itemType, List<ObjectId> scs, List<ObjectId> psbs, List<Integer> origItemType, List<ObjectId> owns, Boolean isCloud) throws Exception {
        BasicDBObject query = buildQuery(gtys, subjectType, level, itemType, scs, psbs, origItemType, owns, null);

        List<ItemPoolEntry> retList = new ArrayList<ItemPoolEntry>();

        int count = count(MongoFacroty.getResDB(), getDBName(isCloud), query);

        return count;
    }


    /**
     * @param itemType      题目类型
     * @param scs           知识面
     * @param psbs          单元
     * @param donedItemList
     * @return
     * @throws Exception
     */
    public List<ObjectId> getItemPoolIdsList(List<ObjectId> itemType, List<ObjectId> scs, List<ObjectId> psbs, List<Integer> origItemType, List<ObjectId> donedItemList, List<ObjectId> owns) throws Exception {

        BasicDBObject query = buildQuery(null, null, -1, itemType, scs, psbs, origItemType, owns, null);
        if (null != donedItemList && donedItemList.size() > 0) {
            query.append(Constant.ID, new BasicDBObject(Constant.MONGO_NOTIN, donedItemList));
        }
        List<ObjectId> retList = new ArrayList<ObjectId>();

        List<DBObject> list = find(MongoFacroty.getResDB(), Constant.COLLECTION_ITEM_POOL, query, new BasicDBObject(Constant.ID, 1), Constant.MONGO_SORTBY_DESC, 0, 100);

        if (null != list) {
            for (DBObject dbo : list) {
                retList.add((ObjectId) dbo.get(Constant.ID));
            }
        }
        return retList;
    }


    /**
     * @param gtys        学段
     * @param subjectType 学科
     * @param level       级别
     * @param itemType    题目类型
     * @param scs         知识面
     * @param count
     * @return
     * @throws Exception
     */
    public Map<ExerciseItemType, List<ObjectId>> getItemList(ObjectId gtys, ObjectId subjectType, int level,
                                                             List<ObjectId> itemType, List<ObjectId> scs, List<ObjectId> psbs, List<Integer> origItemType, int count) throws Exception {
        Map<com.pojo.exercise.ExerciseItemType, List<ObjectId>> retMap = new HashMap<ExerciseItemType, List<ObjectId>>();
        BasicDBObject query = buildQuery(gtys, subjectType, level, itemType, scs, psbs, origItemType, null, null);
        List<DBObject> list = find(MongoFacroty.getResDB(), Constant.COLLECTION_ITEM_POOL, query, new BasicDBObject("oty", 1), Constant.MONGO_SORTBY_DESC, Constant.ZERO, count);
        if (null != list && list.size() > 0) {
            ItemPoolEntry e = null;
            for (DBObject dbo : list) {
                e = new ItemPoolEntry((BasicDBObject) dbo);
                ExerciseItemType eType = ExerciseItemType.getExerciseItemType(e.getOrigType());
                if (!retMap.containsKey(eType)) {
                    retMap.put(eType, new ArrayList<ObjectId>());
                }
                retMap.get(eType).add(e.getID());
            }
        }
        return retMap;
    }


    /**
     * 查询条件
     *
     * @param gtys         学段
     * @param subjectType  学科
     * @param level        级别
     * @param itemType     题目类型
     * @param scs          知识面
     * @param psbs         知识点
     * @param origItemType 单元
     * @param owns         课程
     * @return
     * @throws Exception
     */
    private BasicDBObject buildQuery(ObjectId gtys, ObjectId subjectType, int level,
                                     List<ObjectId> itemType, List<ObjectId> scs, List<ObjectId> psbs, List<Integer> origItemType, List<ObjectId> owns, String regular) throws Exception {
        BasicDBObject query = new BasicDBObject();

        if (null != gtys) {
            query.append("gtys", gtys);
        }
        if (null != subjectType) {
            query.append("sty", subjectType);
        }
        if (level > -1) {
            query.append("lel", level);
        }
        if (null != itemType && !itemType.isEmpty()) {
            query.append("ty", new BasicDBObject(Constant.MONGO_IN, itemType));
        }
        if (null != origItemType && !origItemType.isEmpty()) {
            query.append("oty", new BasicDBObject(Constant.MONGO_IN, origItemType));
        }
        if (null != scs && !scs.isEmpty()) {
            query.append("scs", new BasicDBObject(Constant.MONGO_IN, scs));
        }

        if (null != psbs && !psbs.isEmpty()) {
            query.append("psbs", new BasicDBObject(Constant.MONGO_IN, psbs));
        }

        if (null != owns && !owns.isEmpty()) {
            query.append("ows", new BasicDBObject(Constant.MONGO_IN, owns));
        }

        if (null != regular && !regular.equals("")) {
            query.append("qu", MongoUtils.buildRegex(regular));
        }

        if (query.isEmpty())
            throw new Exception();
        return query;
    }


    @Deprecated
    public List<ItemPoolEntry> getItemPoolEntry(int skip, int limit) {
        List<DBObject> list = find(MongoFacroty.getResDB(), Constant.COLLECTION_ITEM_POOL, new BasicDBObject(), Constant.FIELDS, Constant.MONGO_SORTBY_ASC, skip, limit);
        List<ItemPoolEntry> schoolEntryList = new ArrayList<ItemPoolEntry>();
        for (DBObject dbObject : list) {
            ItemPoolEntry schoolEntry = new ItemPoolEntry((BasicDBObject) dbObject);
            schoolEntryList.add(schoolEntry);
        }
        return schoolEntryList;
    }

    /**
     * 删除
     *
     * @param id      题目id
     * @param ownerId 用户id
     * @param isCloud 是否云题库
     */
    public void remove(ObjectId id, ObjectId ownerId, Boolean isCloud) {
        DBObject query = new BasicDBObject(Constant.ID, id).append("ows", ownerId);
        remove(MongoFacroty.getResDB(), getDBName(isCloud), query);
    }


}
