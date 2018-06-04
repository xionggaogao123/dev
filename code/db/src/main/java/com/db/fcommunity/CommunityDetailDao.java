package com.db.fcommunity;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.fcommunity.CommunityDetailEntry;
import com.pojo.fcommunity.CommunityDetailType;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jerry on 2016/10/24.
 * 社区内容
 */
public class CommunityDetailDao extends BaseDao {

    public ObjectId save(CommunityDetailEntry detailEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_DETAIL, detailEntry.getBaseEntry());
        return detailEntry.getID();
    }

    public void delOldEntry(String id){
        BasicDBObject query = new BasicDBObject("shul",id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("r",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_DETAIL, query,updateValue);
    }

    /**
     * 获取某个社区内容(通过id)
     *
     * @param id
     * @return
     */
    public CommunityDetailEntry findByObjectId(ObjectId id) {
        BasicDBObject query = new BasicDBObject()
                .append(Constant.ID, id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_DETAIL, query);
        return dbo == null ? null : new CommunityDetailEntry(dbo);
    }

    public void updEntry(CommunityDetailEntry e) {
        BasicDBObject query=new BasicDBObject(Constant.ID,e.getID());
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,e.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_DETAIL, query,updateValue);
    }
    /**
     * 分页-获取某个社区发表内容(按时间先后排序)
     *
     * @param communityId
     * @param page
     * @param pageSize
     * @return
     */
    public List<CommunityDetailEntry> getDetailsByCommunityId(ObjectId communityId, int page, int pageSize, int order) {
        List<CommunityDetailEntry> detailEntries = new ArrayList<CommunityDetailEntry>();
        BasicDBObject query = new BasicDBObject()
                .append("cmid", communityId)
                .append("r", 0);
        BasicDBObject orderBy = new BasicDBObject()
                .append(Constant.ID, order);
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_DETAIL, query, Constant.FIELDS, orderBy, (page - 1) * pageSize, pageSize);
        for (DBObject dbo : dbObjects) {
            detailEntries.add(new CommunityDetailEntry(dbo));
        }
        return detailEntries;
    }

    /**
     * 分页-获取某个社区发表内容(按时间先后排序)
     *
     * @param communityId
     * @param page
     * @param pageSize
     * @return
     */
    public List<CommunityDetailEntry> getDetails(ObjectId communityId, int page, int pageSize, int order, CommunityDetailType type) {
        List<CommunityDetailEntry> detailEntries = new ArrayList<CommunityDetailEntry>();
        BasicDBObject query = new BasicDBObject().append("cmid", communityId)
                .append("cmty", type.getType()).append("r", 0);
        BasicDBObject orderBy = new BasicDBObject().append("tp",-1).append(Constant.ID, order);
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_DETAIL, query, Constant.FIELDS, orderBy, (page - 1) * pageSize, pageSize);
        for (DBObject dbo : dbObjects) {
            detailEntries.add(new CommunityDetailEntry(dbo));
        }
        return detailEntries;
    }
    
    public List<CommunityDetailEntry> getDetails(ObjectId communityId, int page, int pageSize, int order, CommunityDetailType type, int receiveType, ObjectId userId) {
        List<CommunityDetailEntry> detailEntries = new ArrayList<CommunityDetailEntry>();
        BasicDBObject query = new BasicDBObject().append("cmid", communityId)
                .append("cmty", type.getType()).append("r", 0);
        if (receiveType == 1) {
            
        } else if (receiveType == 3) {
            query.append("cmuid", userId);
        } else if (receiveType == 2) {
            query.append("cmuid", new BasicDBObject(Constant.MONGO_NE,userId));
        }
        BasicDBObject orderBy = new BasicDBObject().append("tp",-1).append(Constant.ID, order);
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_DETAIL, query, Constant.FIELDS, orderBy, (page - 1) * pageSize, pageSize);
        for (DBObject dbo : dbObjects) {
            detailEntries.add(new CommunityDetailEntry(dbo));
        }
        return detailEntries;
    }



    public List<CommunityDetailEntry> getDetails(List<ObjectId> communityIds, int page, int pageSize, int order, int type) {
        List<CommunityDetailEntry> detailEntries = new ArrayList<CommunityDetailEntry>();
        BasicDBObject query = new BasicDBObject().append("cmid", new BasicDBObject(Constant.MONGO_IN,communityIds))
                .append("cmty", type).append("r", 0);
        BasicDBObject orderBy = new BasicDBObject().append("tp",-1).append(Constant.ID, order);
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_DETAIL, query, Constant.FIELDS, orderBy, (page - 1) * pageSize, pageSize);
        for (DBObject dbo : dbObjects) {
            detailEntries.add(new CommunityDetailEntry(dbo));
        }
        return detailEntries;
    }
    
    public List<CommunityDetailEntry> getDetails(List<ObjectId> communityIds, int page, int pageSize, int order, int type, int receiveType, ObjectId userId) {
        List<CommunityDetailEntry> detailEntries = new ArrayList<CommunityDetailEntry>();
        BasicDBObject query = new BasicDBObject().append("cmid", new BasicDBObject(Constant.MONGO_IN,communityIds))
                .append("cmty", type).append("r", 0);
        if (receiveType == 1) {
            
        } else if (receiveType == 3) {
            query.append("cmuid", userId);
        } else if (receiveType == 2) {
            query.append("cmuid", new BasicDBObject(Constant.MONGO_NE,userId));
        }
        BasicDBObject orderBy = new BasicDBObject().append("tp",-1).append(Constant.ID, order);
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_DETAIL, query, Constant.FIELDS, orderBy, (page - 1) * pageSize, pageSize);
        for (DBObject dbo : dbObjects) {
            detailEntries.add(new CommunityDetailEntry(dbo));
        }
        return detailEntries;
    }


    public CommunityDetailEntry getLatestDetails(ObjectId communityId, int type) {
        List<CommunityDetailEntry> detailEntries = new ArrayList<CommunityDetailEntry>();
        BasicDBObject query = new BasicDBObject()
                .append("cmid", communityId)
                .append("cmty", type)
                .append("r", 0);
        BasicDBObject orderBy = new BasicDBObject()
                .append(Constant.ID, -1);
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_DETAIL, query, Constant.FIELDS, orderBy, 0, 1);
        for (DBObject dbo : dbObjects) {
            detailEntries.add(new CommunityDetailEntry(dbo));
        }
        if (detailEntries.size() > 0) {
            return detailEntries.get(0);
        }
        return null;
    }


    /**
     * 分页-获取某个社区发表内容(按时间先后排序)
     *
     * @param commIds
     * @param page
     * @param pageSize
     * @return
     */
    public List<CommunityDetailEntry> getDetailsByUserId(List<ObjectId> commIds, int page, int pageSize, int order) {
        List<CommunityDetailEntry> detailEntries = new ArrayList<CommunityDetailEntry>();
        BasicDBObject query = new BasicDBObject().append("cmid", new BasicDBObject(Constant.MONGO_IN, commIds)).append("r", 0);
        BasicDBObject orderBy = new BasicDBObject().append(Constant.ID, order);
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_DETAIL, query, Constant.FIELDS, orderBy, (page - 1) * pageSize, pageSize);
        for (DBObject dbo : dbObjects) {
            detailEntries.add(new CommunityDetailEntry(dbo));
        }
        return detailEntries;
    }


    public List<CommunityDetailEntry> getNewsByType(List<ObjectId> communityIds, CommunityDetailType type, int page, int pageSize, int order) {
        List<CommunityDetailEntry> detailEntries = new ArrayList<CommunityDetailEntry>();
        BasicDBObject query = new BasicDBObject().append("cmty", type.getType());
        if (communityIds != null && communityIds.size() > 0) {
            query.append("cmid", new BasicDBObject(Constant.MONGO_IN, communityIds));
        }
        query.append("r", 0);
        BasicDBObject orderBy = new BasicDBObject().append(Constant.ID, order);
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_DETAIL, query, Constant.FIELDS, orderBy, (page - 1) * pageSize, pageSize);
        for (DBObject dbo : dbObjects) {
            detailEntries.add(new CommunityDetailEntry(dbo));
        }
        return detailEntries;
    }

    /**
     * 返回某个社区的消息个数
     *
     * @param query
     * @return
     */
    public int count(DBObject query) {
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_DETAIL, query);
    }

    /**
     * 返回某个社区的消息个数
     *
     * @param communityId
     * @return
     */
    public int count(ObjectId communityId) {
        BasicDBObject query = new BasicDBObject("cmid", communityId).append("r", 0);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_DETAIL, query);
    }

    /**
     * 返回某个社区的消息个数
     *
     * @param communityId
     * @return
     */
    public int count(ObjectId communityId, CommunityDetailType type) {
        BasicDBObject query = new BasicDBObject("cmid", communityId).append("cmty", type.getType()).append("r", 0);
        
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_DETAIL, query);
    }
    
    public int count(ObjectId communityId, CommunityDetailType type, int receiveType, ObjectId userId) {
        BasicDBObject query = new BasicDBObject("cmid", communityId).append("cmty", type.getType()).append("r", 0);
        if (receiveType == 1) {
            
        } else if (receiveType == 3) {
            query.append("cmuid", userId);
        } else if (receiveType == 2) {
            query.append("cmuid", new BasicDBObject(Constant.MONGO_NE,userId));
        }
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_DETAIL, query);
    }

    public int count(List<ObjectId> communityIds, int type) {
        BasicDBObject query = new BasicDBObject("cmid", new BasicDBObject(Constant.MONGO_IN,communityIds)).append("cmty", type).append("r", 0);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_DETAIL, query);
    }
    
    public int count(List<ObjectId> communityIds, int type, int receiveType, ObjectId userId) {
        BasicDBObject query = new BasicDBObject("cmid", new BasicDBObject(Constant.MONGO_IN,communityIds)).append("cmty", type).append("r", 0);
if (receiveType == 1) {
            
        } else if (receiveType == 3) {
            query.append("cmuid", userId);
        } else if (receiveType == 2) {
            query.append("cmuid", new BasicDBObject(Constant.MONGO_NE,userId));
        }
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_DETAIL, query);
    }

    /**
     * 返回某个社区的消息个数
     *
     * @param commIds
     * @return
     */
    public int count(List<ObjectId> commIds) {
        BasicDBObject query = new BasicDBObject().append("cmid", new BasicDBObject(Constant.MONGO_IN, commIds)).append("r", 0);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_DETAIL, query);
    }

    /**
     * 报名参与活动
     *
     * @param communityDetailId
     * @param userId
     */
    public void enterCommunityDetail(ObjectId communityDetailId, ObjectId userId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, communityDetailId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_PUSH, new BasicDBObject("pil", userId));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_DETAIL, query, updateValue);
    }

    /**
     * 报名参与活动
     *
     * @param communityDetailId
     * @param userId
     */
    public void cancelBaoming(ObjectId communityDetailId, ObjectId userId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, communityDetailId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("pil", userId));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_DETAIL, query, updateValue);
    }

    /**
     * 删除备注
     *
     * @param communityDetailId
     * @param userId
     */
    public void deleteBaomingBeizhu(ObjectId communityDetailId, ObjectId userId) {
        BasicDBObject query = new BasicDBObject().append("cdid", communityDetailId)
                .append("uid", userId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_PARTINCONTENT, query);
    }

    /**
     * 统计某人已读的某社区的数目
     *
     * @return
     */
    public int countRead(CommunityDetailType type, ObjectId communityId, ObjectId userId) {
        BasicDBObject query = new BasicDBObject().append("cmid", communityId)
                .append("unrl", userId)
                .append("cmty", type.getType())
                .append("r", 0);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_DETAIL, query);
    }

    /**
     * 该条消息已读取
     *
     * @param detailId
     * @param userId
     */
    public void pushRead(ObjectId detailId, ObjectId userId) {
        BasicDBObject query = new BasicDBObject().append(Constant.ID, detailId);
        BasicDBObject update = new BasicDBObject().append(Constant.MONGO_PUSH, new BasicDBObject("unrl", userId));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_DETAIL, query, update);
    }


    /**
     * 根据Id删除社区详情
     *
     * @param id
     */
    public void removeCommunityDetail(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("r", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_DETAIL, query, updateValue);
    }


    /**
     * 置顶详情数据
     * @param id
     */
    public void updateCommunityDetailTop(ObjectId id,int top){
        BasicDBObject query=new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("tp", top));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_DETAIL, query, updateValue);
    }

    /**
     * 点赞功能
     */
    public void updateCommunityDetailZan(ObjectId id,ObjectId userId,int type){
        BasicDBObject query=new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject();
        if(type==1) {
            updateValue=new BasicDBObject(Constant.MONGO_PUSH, new BasicDBObject("zl", userId)).append(Constant.MONGO_INC, new BasicDBObject("zc",Constant.ONE));
        }else{
            updateValue=new BasicDBObject(Constant.MONGO_PULL,new BasicDBObject("zl",userId)).append(Constant.MONGO_INC,new BasicDBObject("zc",Constant.NEGATIVE_ONE));
        }
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_DETAIL, query, updateValue);
    }


    /**
     * 记录哪些人员删除记录
     * @param id
     * @param userId
     */
    public void recordCommunityDetailDeleteUserIds(ObjectId id,ObjectId userId){
        BasicDBObject query=new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject().append(Constant.MONGO_PUSH, new BasicDBObject("dus", userId));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_DETAIL, query, updateValue);
    }


    public List<CommunityDetailEntry> getRecordDetails(List<ObjectId> communityIds, int page, int pageSize, int order, int type,
                                                       ObjectId userId) {
        List<CommunityDetailEntry> detailEntries = new ArrayList<CommunityDetailEntry>();
        BasicDBObject query = new BasicDBObject().append("cmid", new BasicDBObject(Constant.MONGO_IN,communityIds))
                .append("cmty", type).append("r", 0);
        if(null!=userId){
            List<ObjectId> userIds=new ArrayList<ObjectId>();
            userIds.add(userId);
            query.append("dus",new BasicDBObject(Constant.MONGO_NOTIN,userIds));
        }
        BasicDBObject orderBy = new BasicDBObject().append("tp",-1).append(Constant.ID, order);
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_DETAIL, query, Constant.FIELDS, orderBy, (page - 1) * pageSize, pageSize);
        for (DBObject dbo : dbObjects) {
            detailEntries.add(new CommunityDetailEntry(dbo));
        }
        return detailEntries;
    }

    public List<CommunityDetailEntry> getDetailEntries(int type,int page,int pageSize) {
        List<CommunityDetailEntry> detailEntries = new ArrayList<CommunityDetailEntry>();
        BasicDBObject query = new BasicDBObject()
                .append("cmty", type)
                .append("r", 0);
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(),
                Constant.COLLECTION_FORUM_COMMUNITY_DETAIL, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_ASC, (page-1)*pageSize,pageSize);
        for (DBObject dbo : dbObjects) {
            detailEntries.add(new CommunityDetailEntry(dbo));
        }
        return detailEntries;
    }


}
