package com.db.cloudlesson;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.cloudlesson.CloudLessonEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.exceptions.ResultTooManyException;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * 云课程
 *
 * @author fourer
 */
public class CloudLessonDao extends BaseDao {

    /**
     * 添加
     *
     * @param e
     * @return
     */
    public ObjectId addCloudLessonEntry(CloudLessonEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CLOUDLESSON_NAME, e.getBaseEntry());
        return e.getID();
    }


    /**
     * 查找云课程数量
     *
     * @param subject
     * @param grades
     * @param CloudLessonType 云课程类型
     * @return
     */
    public int countCloudLessonEntry(int subject, List<Integer> grades, ObjectId CloudLessonType, String searchName) {
        BasicDBObject query = buildQuery(subject, grades, CloudLessonType, searchName);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_CLOUDLESSON_NAME, query);
    }


    /**
     * 云课程详情
     *
     * @param id
     * @return
     */
    public CloudLessonEntry getCloudLessonEntry(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CLOUDLESSON_NAME, query, Constant.FIELDS);
        if (null != dbo) {
            return new CloudLessonEntry((BasicDBObject) dbo);
        }
        return null;
    }


    /**
     * 更新云课程年级,仅仅用于更新云资源年级
     *
     * @param cid
     * @param grade
     */
    @Deprecated
    public void addGrade(ObjectId cid, int grade) {
        BasicDBObject query = new BasicDBObject(Constant.ID, cid);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_ADDTOSET, new BasicDBObject("ccgts", grade));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CLOUDLESSON_NAME, query, updateValue);
    }

    /**
     * 仅仅为了掩饰增加知识点和章节目录
     *
     * @param cid
     * @param field
     * @param id
     */
    @Deprecated
    public void update(ObjectId cid, String field, ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, cid);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_ADDTOSET, new BasicDBObject(field, id));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CLOUDLESSON_NAME, query, updateValue);
    }


    /**
     * 查找云课程
     *
     * @param subject
     * @param grades
     * @param CloudLessonType 云课程类型
     * @param fields
     * @param skip
     * @param limit
     * @return
     * @throws ResultTooManyException
     */
    public List<CloudLessonEntry> getCloudLessonEntryList(int subject, List<Integer> grades, ObjectId CloudLessonType, String searchName, DBObject fields, int skip, int limit) throws ResultTooManyException {
        List<CloudLessonEntry> retList = new ArrayList<CloudLessonEntry>();
        BasicDBObject query = buildQuery(subject, grades, CloudLessonType, searchName);

        if (query.isEmpty() && limit > 100) {
            throw new ResultTooManyException();
        }

        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLOUDLESSON_NAME, query, fields, Constant.MONGO_SORTBY_DESC, skip, limit);
        if (null != list && list.size() > 0) {
            for (DBObject dbo : list) {
                retList.add(new CloudLessonEntry((BasicDBObject) dbo));
            }
        }

        return retList;
    }

    /**
     * 查找云课程 ,返回map，key为视频ID
     *
     * @param subject
     * @param grades
     * @param CloudLessonType
     * @param fields
     * @param skip
     * @param limit
     * @return
     * @throws ResultTooManyException
     */
    public Map<ObjectId, List<CloudLessonEntry>> getCloudLessonEntryMap(int subject, List<Integer> grades, ObjectId CloudLessonType, String searchName, DBObject fields, int skip, int limit) throws ResultTooManyException {
        Map<ObjectId, List<CloudLessonEntry>> retMap = new HashMap<ObjectId, List<CloudLessonEntry>>();
        List<CloudLessonEntry> list = getCloudLessonEntryList(subject, grades, CloudLessonType, searchName, fields, skip, limit);
        if (null != list && list.size() > 0) {
            for (CloudLessonEntry cce : list) {
                for (ObjectId vid : cce.getVideoIds()) {
                    if (!retMap.containsKey(vid)) {
                        retMap.put(vid, new ArrayList<CloudLessonEntry>());
                    }
                    retMap.get(vid).add(cce);
                }
            }
        }
        return retMap;
    }


    private BasicDBObject buildQuery(int subject, List<Integer> grades,
                                     ObjectId CloudClassType, String searchName) {
        BasicDBObject query = new BasicDBObject();

        if (subject > Constant.NEGATIVE_ONE) {
            query.append("sub", subject);
        }
        if (null != grades && !grades.isEmpty()) {
            query.append("ccgts", new BasicDBObject(Constant.MONGO_IN, grades));
        }
        if (null != CloudClassType) {
            query.append("cctys", CloudClassType);
        }
        if (StringUtils.isNotBlank(searchName)) {
            query.append("nm", MongoUtils.buildRegex(searchName));
        }

        return query;
    }

    public Set<ObjectId> getAllCloudLessonIdList() {
        Set<ObjectId> set = new HashSet<ObjectId>();
        BasicDBObject query = new BasicDBObject();
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLOUDLESSON_NAME, query, new BasicDBObject("vis", 1));
        if (null != list && !list.isEmpty()) {
            CloudLessonEntry e;
            for (DBObject dbo : list) {
                e = new CloudLessonEntry((BasicDBObject) dbo);
                if (e.getVideoIds() != null) {
                    set.addAll(e.getVideoIds());
                }
            }
        }
        return set;
    }


    public List<CloudLessonEntry> getCloudLessonEntry(int skip, int limit) {
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLOUDLESSON_NAME, new BasicDBObject(), Constant.FIELDS, Constant.MONGO_SORTBY_ASC, skip, limit);
        List<CloudLessonEntry> schoolEntryList = new ArrayList<CloudLessonEntry>();
        for (DBObject dbObject : list) {
            CloudLessonEntry schoolEntry = new CloudLessonEntry((BasicDBObject) dbObject);
            schoolEntryList.add(schoolEntry);
        }
        return schoolEntryList;
    }

    @Deprecated
    public List<ObjectId> getIds() {
        List<ObjectId> retList = new ArrayList<ObjectId>();
        BasicDBObject query = new BasicDBObject("sub", 2);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLOUDLESSON_NAME, query, new BasicDBObject("_id", 1), Constant.MONGO_SORTBY_ASC, 0, 200);
        if (null != list && !list.isEmpty()) {
            CloudLessonEntry e;
            for (DBObject dbo : list) {
                e = new CloudLessonEntry((BasicDBObject) dbo);
                if (e.getVideoIds() != null) {
                    retList.add(e.getID());
                }
            }
        }
        return retList;
    }

    @Deprecated

    public CloudLessonEntry getCloudLessonEntryByVideoid(ObjectId videoId) {
        BasicDBObject query = new BasicDBObject("vis", videoId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CLOUDLESSON_NAME, query, Constant.FIELDS);
        if (null != dbo) {
            return new CloudLessonEntry((BasicDBObject) dbo);
        }
        return null;
    }
}
