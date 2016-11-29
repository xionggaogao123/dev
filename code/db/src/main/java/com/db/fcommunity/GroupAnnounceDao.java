package com.db.fcommunity;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.fcommunity.GroupAnnounceEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moslpc on 2016/11/8.
 */
public class GroupAnnounceDao extends BaseDao {

    public void save(GroupAnnounceEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_GROUPANNOUNCE, entry.getBaseEntry());
    }

    public GroupAnnounceEntry getEarlyOne(ObjectId groupId) {
        BasicDBObject query = new BasicDBObject()
                .append("grid", groupId);
        BasicDBObject sort = new BasicDBObject(Constant.ID, -1);
        List<DBObject> dbos = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_GROUPANNOUNCE, query, Constant.FIELDS, sort, 0, 1);
        if (dbos != null && dbos.size() > 0) {
            return new GroupAnnounceEntry(dbos.get(0));
        }
        return null;
    }

    public List<GroupAnnounceEntry> getByPage(ObjectId groupId, int page, int paeSize) {
        List<GroupAnnounceEntry> groupAnnounceEntries = new ArrayList<GroupAnnounceEntry>();
        BasicDBObject query = new BasicDBObject()
                .append("grid", groupId);
        BasicDBObject orderBy = new BasicDBObject()
                .append(Constant.ID, -1);
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_GROUPANNOUNCE, query, Constant.FIELDS, orderBy, (page - 1) * paeSize, paeSize);
        for (DBObject dbo : dbObjects) {
            groupAnnounceEntries.add(new GroupAnnounceEntry(dbo));
        }
        return groupAnnounceEntries;
    }

    public int count(ObjectId groupId) {
        BasicDBObject query = new BasicDBObject()
                .append("grid", groupId)
                .append("r", 0);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_GROUPANNOUNCE, query);
    }


}