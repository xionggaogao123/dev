package com.db.activity;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.activity.ActInvitationEntry;
import com.pojo.activity.enums.ActIvtStatus;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by Hao on 2015/3/9.
 */
public class ActInvitationDao extends BaseDao {
    public void updateInvitationStatus(ObjectId objectId, int ordinal) {
        BasicDBObject query = new BasicDBObject(Constant.ID, objectId);
        BasicDBObject update = new BasicDBObject("st", ordinal);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, update);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVITY_INVITATION_NAME, query, updateValue);
    }

    public ActInvitationEntry findInvitationById(ObjectId objectId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, objectId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVITY_INVITATION_NAME, query, Constant.FIELDS);
        if (dbObject == null) return null;
        return new ActInvitationEntry((BasicDBObject) dbObject);
    }

    public void deleteActInvitationById(ObjectId objectId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, objectId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVITY_INVITATION_NAME, query);
    }

    public ObjectId insertActInvitation(ActInvitationEntry actInvitation) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVITY_INVITATION_NAME, actInvitation.getBaseEntry());
        return actInvitation.getID();
    }

    public ActInvitationEntry findInvitation(ObjectId actId, ObjectId userId) {
        BasicDBObject query = new BasicDBObject("aid", actId).append("gid", userId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(),
                Constant.COLLECTION_ACTIVITY_INVITATION_NAME, query, Constant.FIELDS);
        if (dbObject == null) return null;
        return new ActInvitationEntry((BasicDBObject) dbObject);
    }

    public int selectInvitationCount(ObjectId userId, ActIvtStatus invite) {
        BasicDBList basicDBList = new BasicDBList();
        basicDBList.add(new BasicDBObject("gid", userId));
        basicDBList.add(new BasicDBObject("st", invite.getState()));
        BasicDBObject query = new BasicDBObject(Constant.MONGO_AND, basicDBList);
        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVITY_INVITATION_NAME, query);
        return count;
    }

    /**
     * 批量删除
     * add by miaoqiang
     *
     * @param ids
     */
    public void deleteByActivityIds(List<ObjectId> ids) {
        BasicDBObject query = new BasicDBObject();
        query.append("aid", new BasicDBObject(Constant.MONGO_IN, ids));
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVITY_INVITATION_NAME, query);
    }
}
