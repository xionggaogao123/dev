package com.db.activity;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.activity.ActivityDiscuss;
import com.pojo.activity.ActivityEntry;
import com.pojo.activity.enums.ActStatus;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;

import org.bson.types.ObjectId;

import java.util.*;

/**
 * Created by yan on 2015/3/3.
 */
public class ActivityDao extends BaseDao {


    public List<ActivityEntry> selectHotActivity(ObjectId geoId, Integer page, Integer pageSize) {
        if (page == null || page < 1) page = 1;
        if (pageSize == null || pageSize < 1) pageSize = 5;
        int begin = (page - 1) * pageSize;

        List<ActivityEntry> activityEntryList = new ArrayList<ActivityEntry>();
        BasicDBObject basicDBObject = new BasicDBObject("rid", geoId);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVITY_NAME,
                basicDBObject, Constant.FIELDS, new BasicDBObject("attc", -1), begin, pageSize);
        if (dbObjectList != null) {
            for (DBObject dbObject : dbObjectList) {
                ActivityEntry activityEntry = new ActivityEntry((BasicDBObject) dbObject);
                activityEntryList.add(activityEntry);
            }
        }

        return activityEntryList;
    }

    public List<ActivityEntry> selectActivityWhereActIdIn(List<ObjectId> objectIdList) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, objectIdList));
        List<DBObject> baseList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVITY_NAME, query, Constant.FIELDS);
        List<ActivityEntry> activityEntryList = new ArrayList<ActivityEntry>();
        for (DBObject dbObject : baseList) {
            activityEntryList.add(new ActivityEntry((BasicDBObject) dbObject));
        }
        return activityEntryList;
    }

    public List<ActivityEntry> selectActivityByUserIds(List<ObjectId> objectIdList, Integer skip, Integer pageSize) {
        BasicDBObject query = new BasicDBObject("att", new BasicDBObject(Constant.MONGO_IN, objectIdList))
                .append("as", new BasicDBObject(Constant.MONGO_NE, 1));

        //List<DBObject> baseList = find(Constant.COLLECTION_ACTIVITY_NAME, query, Constant.FIELDS);
        List<DBObject> baseList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVITY_NAME, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, skip, pageSize);
        List<ActivityEntry> activityEntryList = new ArrayList<ActivityEntry>();
        for (DBObject dbObject : baseList) {
            activityEntryList.add(new ActivityEntry((BasicDBObject) dbObject));
        }
        return activityEntryList;
    }

    public int findFriendsActivityCount(List<ObjectId> userIds) {
        BasicDBObject query = new BasicDBObject("att", new BasicDBObject(Constant.MONGO_IN, userIds)).append("as", new BasicDBObject(Constant.MONGO_NE, 1));
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVITY_NAME, query);
    }

    /*
    *
    *和当前用户相关的活动 （相关是指 参加 或者发起）
    * */
    public List<ActivityEntry> activityRelation2me(ObjectId userId) {
        BasicDBObject query = new BasicDBObject();
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject("oid", userId));
        values.add(new BasicDBObject("att", userId));
        query.put(Constant.MONGO_OR, values);
        List<DBObject> dbList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVITY_NAME, query, Constant.FIELDS);
        List<ActivityEntry> activityEntryList = new ArrayList<ActivityEntry>();
        for (DBObject dbObject : dbList) {
            ActivityEntry activityEntry = new ActivityEntry((BasicDBObject) dbObject);
            activityEntryList.add(activityEntry);
        }
        return activityEntryList;
    }

    /*
    *
    *根据学校推荐活动
    * */
    public List<ActivityEntry> recommendActivityOnlySchool(ObjectId schoolId, List<ObjectId> ids, int begin, Integer pageSize) {
        BasicDBObject query = new BasicDBObject();
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject("sid", schoolId));
        values.add(new BasicDBObject("as", 0));
        values.add(new BasicDBObject("att", new BasicDBObject(Constant.MONGO_NOTIN, ids)));
        query.put(Constant.MONGO_AND, values);
        List<DBObject> objList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVITY_NAME, query, Constant.FIELDS,
                new BasicDBObject("attc", -1), begin, pageSize);
        List<ActivityEntry> activityList = new ArrayList<ActivityEntry>();
        for (DBObject dbObject : objList) {
            activityList.add(new ActivityEntry((BasicDBObject) dbObject));
        }
        return activityList;
    }

    /*
    *
    * 推荐的活动总数
    * */
    public int recommendActivityOnlySchoolCount(ObjectId schoolId, List<ObjectId> ids) {
        BasicDBObject query = new BasicDBObject();
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject("sid", schoolId));
        values.add(new BasicDBObject("as", 0));
        values.add(new BasicDBObject("att", new BasicDBObject(Constant.MONGO_NOTIN, ids)));
        query.put(Constant.MONGO_AND, values);
        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVITY_NAME, query);
        return count;
    }

    /*
    * 我发起的活动
    *
    * */
    public List<ActivityEntry> myOrganizedActivity(ObjectId userId, int begin, Integer pageSize) {
        BasicDBObject query = new BasicDBObject("oid", userId).append("as", new BasicDBObject(Constant.MONGO_NE, 1));
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVITY_NAME, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, begin, pageSize);
        List<ActivityEntry> activityEntryList = new ArrayList<ActivityEntry>();
        for (DBObject dbObject : dbObjects) {
            ActivityEntry activityEntry = new ActivityEntry((BasicDBObject) dbObject);
            activityEntryList.add(activityEntry);
        }
        return activityEntryList;
    }

    /*
    *
    *
    * */
    public int myOrganizedActivityCount(ObjectId objectId) {
        BasicDBObject query = new BasicDBObject("oid", objectId).append("as", new BasicDBObject(Constant.MONGO_NE, 1));
        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVITY_NAME, query);
        return count;
    }

    /*
    * 我参加的活动
    *
    * */
    public List<ActivityEntry> myAttendActivity(ObjectId objectId, int begin, Integer pageSize) {
        BasicDBObject query = new BasicDBObject("att", objectId).append("as", new BasicDBObject(Constant.MONGO_NE, 1));
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVITY_NAME, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, begin, pageSize);
        List<ActivityEntry> activityEntryList = new ArrayList<ActivityEntry>();
        for (DBObject dbObject : dbObjects) {
            ActivityEntry activityEntry = new ActivityEntry((BasicDBObject) dbObject);
            activityEntryList.add(activityEntry);
        }
        return activityEntryList;
    }

    public int myAttendActivityCount(ObjectId objectId) {
        BasicDBObject query = new BasicDBObject("att", objectId).append("as", new BasicDBObject(Constant.MONGO_NE, 1));
        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVITY_NAME, query);
        return count;
    }


    public int userInActivityCount(ObjectId objectId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, objectId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVITY_NAME, query, Constant.FIELDS);
        ActivityEntry activityEntry = new ActivityEntry((BasicDBObject) dbObject);
        return activityEntry.getAttendCount();
    }


    public ActivityEntry findActivityById(ObjectId objectId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, objectId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVITY_NAME, query, Constant.FIELDS);
        return new ActivityEntry((BasicDBObject) dbObject);
    }

    public void insertAttend(ObjectId actId, ObjectId userId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, actId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_PUSH, new BasicDBObject("att", userId));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVITY_NAME, query, update);
    }


    public ObjectId insertActivity(ActivityEntry activityEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVITY_NAME, activityEntry.getBaseEntry());
        return activityEntry.getID();
    }


    public void updateDiscussAndImgCount(ObjectId actId, String image) {
        BasicDBObject query = new BasicDBObject(Constant.ID, actId);
        BasicDBObject update = new BasicDBObject();
        if (image.equals(""))//无图片，不修改图片数量
        {
            update.append(Constant.MONGO_INC, new BasicDBObject("dis", 1));
        } else {
            update.append(Constant.MONGO_INC, new BasicDBObject("ic", 1).append("dis", 1));
        }
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVITY_NAME, query, update);
    }


    public void updateDiscussAndImgCount(ObjectId actId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, actId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_INC, new BasicDBObject("ic", 1).append("dis", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVITY_NAME, query, update);
    }

    public void updateActivityStatus(ObjectId actId, ActStatus status) {
        BasicDBObject query = new BasicDBObject(Constant.ID, actId);
        BasicDBObject update = new BasicDBObject("as", status.getState());
        BasicDBObject basicDBObject = new BasicDBObject(Constant.MONGO_SET, update);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVITY_NAME, query, basicDBObject);
    }

    public void quitActivity(ObjectId actId, ObjectId userId) {
        DBObject query = new BasicDBObject(Constant.ID, actId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("att", userId))
                .append(Constant.MONGO_INC, new BasicDBObject("attc", -1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVITY_NAME, query, updateValue);
    }

    public void updateAttendCount(ObjectId objectId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, objectId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_INC, new BasicDBObject("attc", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVITY_NAME, query, update);
    }

    /*
    *
    * 活动id 和出席人id查找
    *
    * */
    public ActivityEntry findActivityByIdAndAttendUserId(ObjectId activityId, ObjectId attendUserId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, activityId).append("att", attendUserId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVITY_NAME, query, Constant.FIELDS);
        if (dbObject == null) return null;
        return new ActivityEntry((BasicDBObject) dbObject);
    }

    public Map<ObjectId, ActivityEntry> findActivityMapByIds(Set<ObjectId> activityIds) {
        Map<ObjectId, ActivityEntry> retMap = new HashMap<ObjectId, ActivityEntry>();
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, activityIds));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVITY_NAME, query, Constant.FIELDS);
        if (null != list && !list.isEmpty()) {
            ActivityEntry e;
            for (DBObject dbo : list) {
                e = new ActivityEntry((BasicDBObject) dbo);
                retMap.put(e.getID(), e);
            }
        }
        return retMap;
    }

    /**
     * 批量删除活动
     * add by miaoqiang
     *
     * @param activityIds
     */
    public void deleteByActivityIds(List<ObjectId> activityIds) {
        BasicDBObject query = new BasicDBObject();
        query.append(Constant.ID, new BasicDBObject(Constant.MONGO_IN, activityIds));
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVITY_NAME, query);
    }

    /**
     * 获取所有的活动
     * add by miaoqiang
     *
     * @return
     */
    public List<ActivityEntry> getAllList() {
        BasicDBObject query = new BasicDBObject();
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVITY_NAME,
                query, Constant.FIELDS);
        List<ActivityEntry> activityEntryList = new ArrayList<ActivityEntry>();
        if (dbObjectList != null) {
            for (DBObject dbObject : dbObjectList) {
                ActivityEntry activityEntry = new ActivityEntry((BasicDBObject) dbObject);
                activityEntryList.add(activityEntry);
            }
        }

        return activityEntryList;
    }

    /**
     * 删除某一评论
     * add by miaoqiang
     *
     * @param activityId
     * @param discuss
     */
    public void deleteDiscuss(ObjectId activityId, ActivityDiscuss discuss) {
        BasicDBObject query = new BasicDBObject(Constant.ID, activityId);
        BasicDBObject updateValue = new BasicDBObject();
        updateValue.append(Constant.MONGO_PULL, new BasicDBObject("diss", discuss.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVITY_NAME, query, updateValue);
    }

    /**
     * 根据id删除评论
     *
     * @param actId
     * @param replyId
     */
    public void deleteDiscussById(ObjectId actId, ObjectId replyId, boolean hasPic) {
        BasicDBObject query = new BasicDBObject(Constant.ID, actId);

        List<BasicDBObject> deleteList = new ArrayList<BasicDBObject>();
        DBObject db = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVITY_NAME, query, Constant.FIELDS);
        ActivityEntry activityEntry = new ActivityEntry((BasicDBObject) db);
        List<ActivityDiscuss> discusses = activityEntry.getActDiscusses();
        List<Long> timeList = new ArrayList<Long>();
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        for (ActivityDiscuss activityDiscuss : discusses) {
            if (activityDiscuss.getId().equals(replyId) || activityDiscuss.getRepId() != null && activityDiscuss.getRepId().equals(replyId)) {
                deleteList.add(activityDiscuss.getBaseEntry());
                timeList.add(activityDiscuss.getDate());
                userIds.add(activityDiscuss.getUserId());
            }
        }
        int count = deleteList.size();
        BasicDBObject updateValue = new BasicDBObject();
        updateValue.append(Constant.MONGO_PULLALL, new BasicDBObject("diss", deleteList));
        if (hasPic)//图片数量减1
        {
            updateValue.append(Constant.MONGO_INC, new BasicDBObject("ic", -1).append("dis", -1 * count));
        } else {
            updateValue.append(Constant.MONGO_INC, new BasicDBObject("dis", -1 * count));
        }
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVITY_NAME, query, updateValue);

        //删除动态
        BasicDBObject query2 = new BasicDBObject();
        query2.append("rid", actId);
        query2.append("uid", new BasicDBObject(Constant.MONGO_IN, userIds));
        query2.append("ct", new BasicDBObject(Constant.MONGO_IN, timeList));
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVITY_TRACK_NAME, query2);
    }
}
