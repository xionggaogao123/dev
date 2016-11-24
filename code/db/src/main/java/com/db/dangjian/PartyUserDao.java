package com.db.dangjian;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.dangjian.PartyUser;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by fl on 2016/6/14.
 */
public class PartyUserDao extends BaseDao {

    public ObjectId addPartyUser(PartyUser partyUser){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_PARTYUSER, partyUser.getBaseEntry());
        return partyUser.getID();
    }

    public void updatePartyUser(int isPartyMember, int isCenterMember, int isPartySecretary, ObjectId id){
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ispm", isPartyMember).append("iscm", isCenterMember).append("isps", isPartySecretary));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_PARTYUSER, query, updateValue);
    }

    public void removePartyUser(ObjectId userId){
        DBObject query = new BasicDBObject("uid", userId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_PARTYUSER, query);
    }

    public PartyUser getPartyUser(ObjectId userId){
        BasicDBObject query = new BasicDBObject("uid", userId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_PARTYUSER, query, Constant.FIELDS);
        if(dbObject != null){
            return new PartyUser((BasicDBObject)dbObject);
        }
        return null;
    }

    public List<PartyUser> getPartyUsers(Collection<ObjectId> userIds){
        BasicDBObject query = new BasicDBObject("uid", new BasicDBObject(Constant.MONGO_IN, userIds));
       return getList(query, Constant.FIELDS);
    }

    private List<PartyUser> getList(DBObject query, DBObject fields){
        List<PartyUser> partyUsers = new ArrayList<PartyUser>();
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_PARTYUSER, query, fields);
        if(dbObjects != null && dbObjects.size() > 0){
            for(DBObject dbObject : dbObjects){
                partyUsers.add(new PartyUser((BasicDBObject)dbObject));
            }
        }
        return partyUsers;
    }


    //
}
