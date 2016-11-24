package com.db.lesson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.FieldValuePair;
import com.pojo.emarket.Comment;
import com.pojo.lesson.LessonEntry;
import com.pojo.lesson.LessonType;
import com.pojo.lesson.LessonWare;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;


/**
 * 课程操作类
 * <p>
 * index:ui_di_si_ir_vis
 * {"ui":1,"di":1,"si":1,"ir":1,"vis":1}
 *
 * @author fourer
 */
public class LessonDao extends BaseDao {

    private static final BasicDBObject ORDER_BY = new BasicDBObject("lut", Constant.DESC);


    public ObjectId addLessonEntry(LessonEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_LESSON_NAME, e.getBaseEntry());
        return e.getID();
    }

    public void addLessonEntrys(List<LessonEntry> es) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_LESSON_NAME, MongoUtils.fetchDBObjectList(es));
    }

    /**
     * 删除
     *
     * @param id
     * @param ui
     */
    public void removeLessonEntry(ObjectId id, ObjectId ui) {
        DBObject query = new BasicDBObject(Constant.ID, id).append("ui", ui);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ir", Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_LESSON_NAME, query, updateValue);
    }

    /**
     * @param id
     * @param field ui和di不允许更新
     * @param value
     * @throws IllegalParamException
     */
    public void update(ObjectId id, String field, Object value) throws IllegalParamException {
        if (StringUtils.isBlank(field) || "ui".equals(field) || "di".equals(field))
            throw new IllegalParamException();
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject valueDBO = new BasicDBObject(field, value);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, valueDBO);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_LESSON_NAME, query, updateValue);
    }


    /**
     * 增加课程的推送次数
     *
     * @param id
     */
    public void increasePushCount(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_INC, new BasicDBObject("pc", Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_LESSON_NAME, query, updateValue);
    }


    /**
     * 更新多个字段值
     *
     * @param id
     * @param pairs
     * @throws IllegalParamException
     */
    public void update(Collection<ObjectId> ids, FieldValuePair... pairs) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids));
        BasicDBObject valueDBO = new BasicDBObject();
        for (FieldValuePair pair : pairs) {
            valueDBO.append(pair.getField(), pair.getValue());
        }
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, valueDBO);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_LESSON_NAME, query, updateValue);
    }

    /**
     * 自增某个字段值
     *
     * @param id
     * @param field
     * @param count
     * @throws IllegalParamException
     */
    public void increase(ObjectId id, String field, int count) throws IllegalParamException {
        if (!"vc".equals(field) && !"ec".equals(field) && !"dc".equals(field)) {
            throw new IllegalParamException();
        }
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_INC, new BasicDBObject(field, count)).append(Constant.MONGO_SET, new BasicDBObject("lut", System.currentTimeMillis()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_LESSON_NAME, query, updateValue);
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public LessonEntry getLessonEntry(ObjectId id, DBObject fields) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_LESSON_NAME, query, fields);
        if (null != dbo) {
            return new LessonEntry((BasicDBObject) dbo);
        }
        return null;
    }


    /**
     * 根据源ID和用户查询课程
     *
     * @param dirs
     * @param fields
     * @return
     */
    public Map<ObjectId, LessonEntry> getLessonEntryMap(ObjectId sourceId, ObjectId userId, DBObject fields) {
        DBObject query = new BasicDBObject("si", sourceId).append("ui", userId).append("ir", Constant.ZERO);
        return query(fields, query);
    }

    /**
     * 根据类型目录查询
     *
     * @param dirs
     * @param fields
     * @return
     */
    public Map<ObjectId, LessonEntry> getLessonEntryMap(Collection<ObjectId> dirs, DBObject fields) {
        DBObject query = new BasicDBObject("di", new BasicDBObject(Constant.MONGO_IN, dirs)).append("ir", Constant.ZERO);
        return query(fields, query);
    }


    public Map<ObjectId, LessonEntry> getLessonEntryMap(ObjectId dirs, DBObject fields) {
        DBObject query = new BasicDBObject("di", dirs).append("ir", Constant.ZERO);
        return query(fields, query);
    }


    /**
     * 判断要给课程是否属于dirs
     *
     * @param id
     * @param dirs
     * @return
     */
    public boolean isExists(ObjectId id, Collection<ObjectId> dirs) {
        DBObject query = new BasicDBObject("di", new BasicDBObject(Constant.MONGO_IN, dirs)).append(Constant.ID, id);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_LESSON_NAME, query) >= Constant.ONE;
    }

    /**
     * 根据类型目录查询
     *
     * @param dirs
     * @param fields
     * @return
     */
    public int count(Collection<ObjectId> dirs) {
        DBObject query = new BasicDBObject("di", new BasicDBObject(Constant.MONGO_IN, dirs)).append("ir", Constant.ZERO);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_LESSON_NAME, query);
    }


    /**
     * 根据ID集合 查询
     *
     * @param dirs
     * @param fields
     * @return
     */
    public Map<ObjectId, LessonEntry> getLessonEntryMapByIDs(Collection<ObjectId> ids, DBObject fields) {
        DBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids));
        return query(fields, query);
    }


    /**
     * 根据目录删除
     *
     * @param ids
     */
    public void deleteByDirs(Collection<ObjectId> dirs) {
        DBObject query = new BasicDBObject("di", new BasicDBObject(Constant.MONGO_IN, dirs));
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ir", Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_LESSON_NAME, query, updateValue);
    }


    /**
     * 根据ID删除
     *
     * @param ids
     */
    public void deleteByIds(Collection<ObjectId> ids) {
        DBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids));
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ir", Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_LESSON_NAME, query, updateValue);
    }

    /**
     * 添加一个课件
     *
     * @param id
     * @param ware
     */
    public void addLessonWare(ObjectId id, LessonWare ware) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PUSH, new BasicDBObject("dcl", ware.getBaseEntry()))
                .append(Constant.MONGO_INC, new BasicDBObject("dc", Constant.ONE))
                .append(Constant.MONGO_SET, new BasicDBObject("lut", System.currentTimeMillis()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_LESSON_NAME, query, updateValue);

    }


    /**
     * 添加一个课件
     *
     * @param id
     * @param ware
     */
    public void addLessonWare(Collection<ObjectId> ids, LessonWare ware) {
        DBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids));
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PUSH, new BasicDBObject("dcl", ware.getBaseEntry()))
                .append(Constant.MONGO_INC, new BasicDBObject("dc", Constant.ONE))
                .append(Constant.MONGO_SET, new BasicDBObject("lut", System.currentTimeMillis()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_LESSON_NAME, query, updateValue);

    }

    /**
     * 删除一个课件
     *
     * @param id
     * @param ware
     * @param type 1：视频 2课后练习
     */
    public void removeWare(ObjectId id, LessonWare ware) {
        DBObject query = new BasicDBObject(Constant.ID, id);

        BasicDBObject updateValue = new BasicDBObject().append(Constant.MONGO_SET, new BasicDBObject("lut", System.currentTimeMillis()));

        updateValue.append(Constant.MONGO_PULL, new BasicDBObject("dcl", ware.getBaseEntry()))
                .append(Constant.MONGO_INC, new BasicDBObject("dc", Constant.NEGATIVE_ONE));

        update(MongoFacroty.getAppDB(), Constant.COLLECTION_LESSON_NAME, query, updateValue);
    }


    /**
     * 删除一个视频
     *
     * @param id
     * @param ware
     */
    public void removeVideo(ObjectId id, ObjectId videoId) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("vis", videoId))
                .append(Constant.MONGO_INC, new BasicDBObject("vc", Constant.NEGATIVE_ONE))
                .append(Constant.MONGO_SET, new BasicDBObject("lut", System.currentTimeMillis()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_LESSON_NAME, query, updateValue);
    }


    /**
     * 添加一个视频
     *
     * @param id
     * @param ware
     */
    public void addVideo(ObjectId id, ObjectId videoId) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_ADDTOSET, new BasicDBObject("vis", videoId))
                .append(Constant.MONGO_INC, new BasicDBObject("vc", Constant.ONE))
                .append(Constant.MONGO_SET, new BasicDBObject("lut", System.currentTimeMillis()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_LESSON_NAME, query, updateValue);
    }


    /**
     * 给多个课程添加视频
     *
     * @param id
     * @param ware
     */
    public void addVideo(Collection<ObjectId> ids, ObjectId videoId) {
        DBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids));
        DBObject updateValue = new BasicDBObject(Constant.MONGO_ADDTOSET, new BasicDBObject("vis", videoId))
                .append(Constant.MONGO_INC, new BasicDBObject("vc", Constant.ONE))
                .append(Constant.MONGO_SET, new BasicDBObject("lut", System.currentTimeMillis()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_LESSON_NAME, query, updateValue);
    }


    /**
     * 删除最早的评论
     *
     * @param id
     */
    public void deleteComment(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_POP, new BasicDBObject("coms", Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_LESSON_NAME, query, updateValue);
    }

    /**
     * 删除用户一条评论
     *
     * @param lessonId
     * @param userId
     * @param time
     */
    public void deleteComment(ObjectId lessonId, ObjectId userId, long time) {
        DBObject query = new BasicDBObject(Constant.ID, lessonId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("coms", new BasicDBObject("ui", userId).append("t", time)));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_LESSON_NAME, query, updateValue);
    }


    /**
     * 增加一个评论
     *
     * @param id
     * @param comment
     */
    public void addComment(ObjectId id, Comment comment) {
        DBObject dbo = new BasicDBObject(Constant.ID, id);
        BasicDBList list = new BasicDBList();
        list.add(comment.getBaseEntry());
        BasicDBObject operDBO = new BasicDBObject(Constant.MONGO_EACH, list).append(Constant.MONGO_SORT, new BasicDBObject("t", -1));
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PUSH, new BasicDBObject("coms", operDBO))
                .append(Constant.MONGO_INC, new BasicDBObject("comc", Constant.ONE));
        ;
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_LESSON_NAME, dbo, updateValue);
    }


    /**
     * 得到课程,带有评论
     *
     * @param id
     * @param skip
     * @param limit
     * @return
     */
    public LessonEntry getLessonEntry(ObjectId id, int skip, int limit) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBList list = new BasicDBList();
        list.add(skip);
        list.add(limit);
        DBObject fields = new BasicDBObject("coms", new BasicDBObject(Constant.MONGO_SLICE, list));
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_LESSON_NAME, query, fields);
        if (null != dbo) {
            LessonEntry e = new LessonEntry((BasicDBObject) dbo);
            return e;
        }
        return null;
    }


    private Map<ObjectId, LessonEntry> query(DBObject fields, DBObject query) {
        Map<ObjectId, LessonEntry> retMap = new HashMap<ObjectId, LessonEntry>();
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_LESSON_NAME, query, fields, ORDER_BY);
        if (null != list && !list.isEmpty()) {
            LessonEntry e = null;
            for (DBObject dbo : list) {
                e = new LessonEntry((BasicDBObject) dbo);
                retMap.put(e.getID(), e);
            }
        }
        return retMap;
    }

    /*
    *
    * 查询课程
    * */
    public List<LessonEntry> findLessonByVideoIds(Collection<ObjectId> set) {
        if (set == null) return new ArrayList<LessonEntry>();
//        BasicDBObject query=new BasicDBObject("vis",new BasicDBObject(Constant.MONGO_IN,set)).append("ir", Constant.ZERO);
        BasicDBObject query = new BasicDBObject("vis", new BasicDBObject(Constant.MONGO_IN, set));
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_LESSON_NAME, query, Constant.FIELDS);
        List<LessonEntry> lessonEntries = new ArrayList<LessonEntry>();
        if (dbObjectList != null) {
            for (DBObject dbObject : dbObjectList) {
                LessonEntry lessonEntry = new LessonEntry((BasicDBObject) dbObject);
                lessonEntries.add(lessonEntry);
            }
        }
        return lessonEntries;
    }

    /**
     * 获取用户备课空间、班级课程上传数目
     *
     * @param dirids
     * @param lessonType
     * @param dsl
     * @param del
     */
    public int selLessonCount(List<ObjectId> dirids, int lessonType, long dsl, long del) {
        BasicDBObject query = new BasicDBObject("di", new BasicDBObject(Constant.MONGO_IN, dirids)).append("ir", Constant.ZERO);

        BasicDBList dblist = new BasicDBList();
        if (dsl > 0) {
            dblist.add(new BasicDBObject("lut", new BasicDBObject(Constant.MONGO_GTE, dsl)));
        }
        if (del > 0) {
            dblist.add(new BasicDBObject("lut", new BasicDBObject(Constant.MONGO_LTE, del)));
        }
        if (dblist.size() > 0) {
            query.append(Constant.MONGO_AND, dblist);
        }

        query.append("ty", lessonType);

        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_LESSON_NAME, query);
    }

    public List<LessonEntry> getLessonEntryByParamList(List<ObjectId> dirids, int type, long dsl, long del, int skip, int limit, DBObject fields, String orderBy) {
        List<LessonEntry> retList = new ArrayList<LessonEntry>();
        BasicDBObject query = new BasicDBObject("di", new BasicDBObject(Constant.MONGO_IN, dirids)).append("ir", Constant.ZERO);

        BasicDBList dblist = new BasicDBList();
        if (dsl > 0) {
            dblist.add(new BasicDBObject("lut", new BasicDBObject(Constant.MONGO_GTE, dsl)));
        }
        if (del > 0) {
            dblist.add(new BasicDBObject("lut", new BasicDBObject(Constant.MONGO_LTE, del)));
        }
        if (dblist.size() > 0) {
            query.append(Constant.MONGO_AND, dblist);
        }
        query.append("ty", type);

        BasicDBObject sort = null;
        if (!"".equals(orderBy)) {
            sort = new BasicDBObject(orderBy, Constant.DESC);
        } else {
            sort = new BasicDBObject("lut", Constant.DESC);
        }
        List<DBObject> list = new ArrayList<DBObject>();
        if (skip >= 0 && limit > 0) {
            list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_LESSON_NAME, query, fields, sort, skip, limit);
        } else {
            list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_LESSON_NAME, query, fields, sort);
        }

        for (DBObject dbo : list) {
            retList.add(new LessonEntry((BasicDBObject) dbo));
        }
        return retList;
    }

    public LessonEntry getLessonEntryBySrcId(ObjectId id, DBObject fields) {
        BasicDBObject query = new BasicDBObject("si", id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_LESSON_NAME, query, fields);
        if (null != dbo) {
            return new LessonEntry((BasicDBObject) dbo);
        }
        return null;
    }

    public LessonEntry getlesson(String lessonId, DBObject fields) {
        BasicDBObject query = new BasicDBObject("si", new ObjectId(lessonId));
        query.append("ty", LessonType.EMARKET_LESSON.getType());
        query.append("ir", 0);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_LESSON_NAME, query, null);
        if (null != dbo) {
            return new LessonEntry((BasicDBObject) dbo);
        }
        return null;

    }

    /**
     * 获取课程列表
     *
     * @param lessons
     * @return
     */
    public List<LessonEntry> selLessonList(List<ObjectId> lessons) {
        List<LessonEntry> retList = new ArrayList<LessonEntry>();
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, lessons)).append("ir", Constant.ZERO);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_LESSON_NAME, query, null);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new LessonEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }

    /**
     * 删除
     *
     * @param id
     */
    public void removeLessonEntry(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ir", Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_LESSON_NAME, query, updateValue);
    }

    /**
     * 查找所有的班级课程，用于转换成作业
     *
     * @return
     */
    public List<LessonEntry> findAllCLassLesson() {
        DBObject query = new BasicDBObject("ty", 2);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_LESSON_NAME, query, Constant.FIELDS);

        if (null != list && !list.isEmpty()) {
            List<LessonEntry> examResultEntries = new ArrayList<LessonEntry>();
            for (DBObject dbo : list) {
                LessonEntry e = new LessonEntry((BasicDBObject) dbo);
                examResultEntries.add(e);
            }
            return examResultEntries;
        }
        return null;

    }
}
