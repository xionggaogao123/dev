package com.db.forum;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.forum.FInvitationEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by admin on 2016/7/13.
 */
public class FInvitationDao extends BaseDao {

    /**
     * 新增/更新邀请
     */
    public ObjectId saveOrUpdate(FInvitationEntry fInvitationEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_INVITATION, fInvitationEntry.getBaseEntry());
        return fInvitationEntry.getID();
    }

    /**
     * 更新邀请数
     *
     * @param id
     */
    public void updateCount(ObjectId id, ObjectId invited) {
        BasicDBObject query = new BasicDBObject("uid", id);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_INC, new BasicDBObject("ct", 1L));
        updateValue.append(Constant.MONGO_ADDTOSET, new BasicDBObject("url", invited));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_INVITATION, query, updateValue);
    }

    /**
     * 邀请数详情
     *
     * @param id
     * @return
     */
    public FInvitationEntry getFInvitation(ObjectId id) {
        BasicDBObject query = new BasicDBObject("uid", id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_INVITATION, query, Constant.FIELDS);
        if (null != dbo) {
            return new FInvitationEntry((BasicDBObject) dbo);
        }
        return null;
    }
}
