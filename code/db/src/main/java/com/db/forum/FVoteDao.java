package com.db.forum;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.forum.FMissionEntry;
import com.pojo.forum.FVoteEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by admin on 2016/7/13.
 */
public class FVoteDao extends BaseDao {

    /**
     * 新增&更新
     */
    public ObjectId saveOrUpdate(FVoteEntry fVoteEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_VOTE, fVoteEntry.getBaseEntry());
        return fVoteEntry.getID();
    }

    /**
     * 批量添加
     */
    public Boolean addFVoteList(List<FVoteEntry> fVoteEntryList) {
        List<DBObject> dbObjects = MongoUtils.fetchDBObjectList(fVoteEntryList);
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_VOTE, dbObjects);
        return true;
    }

    /**
     * 查询某人是否投票了
     *
     * @param voteId
     * @param userId
     * @return
     */
    public FVoteEntry findFVote(ObjectId voteId, ObjectId userId) {
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_VOTE, new BasicDBObject("vid", voteId).append("uid", userId), Constant.FIELDS);
        if (dbObject != null) {
            return new FVoteEntry((BasicDBObject) dbObject);
        } else {
            return null;
        }
    }

    /**
     * 查询该贴投票人数
     *
     * @param voteId
     * @return
     */
    public int getFVoteCount(ObjectId voteId) {
        BasicDBObject query = new BasicDBObject();
        if (voteId != null) {
            query.append("vid", voteId);
        }

        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_VOTE, query);
        return count;
    }

    /**
     * 查询该帖投票信息
     */

    public List<FVoteEntry> getFVoteList(ObjectId voteId) {

        BasicDBObject query = new BasicDBObject();
        if (voteId != null) {
            query.append("vid", voteId);
        }
        List<FVoteEntry> retList = new ArrayList<FVoteEntry>();
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_VOTE, query, Constant.FIELDS);


        for (DBObject dbObject : dbObjectList) {
            retList.add(new FVoteEntry((BasicDBObject) dbObject));
        }
        return retList;
    }

    /**
     * 获取投票人数
     */
    public int getFVoteListCount(ObjectId voteId) {

        BasicDBObject query = new BasicDBObject();
        if (voteId != null) {
            query.append("vid", voteId);
        }
//        List<FVoteEntry> fVoteEntryList = new ArrayList<FVoteEntry>();
        Set<ObjectId> userIds=new HashSet<ObjectId>();
//        List<FVoteEntry> retList = new ArrayList<FVoteEntry>();
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_VOTE, query, Constant.FIELDS);


        for (DBObject dbObject : dbObjectList) {
            FVoteEntry entry=new FVoteEntry((BasicDBObject) dbObject);
            userIds.add(entry.getUserId());
//            retList.add(new FVoteEntry((BasicDBObject) dbObject));
        }
        return userIds.size();


//        if (retList.size() > 0) {
//            fVoteEntryList.add(retList.get(0));
//            for (FVoteEntry fVoteEntry : retList) {
//                boolean flag = true;
//                for (FVoteEntry item : fVoteEntryList) {
//                    if (item.getUserId().equals(fVoteEntry.getUserId())) {
//                        flag = false;
//                        break;
//                    }
//                }
//                if (flag) {
//                    fVoteEntryList.add(fVoteEntry);
//                }
//            }
//        }
//        return fVoteEntryList.size();
    }
}
