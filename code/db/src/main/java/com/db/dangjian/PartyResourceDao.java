package com.db.dangjian;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.dangjian.PartyResourceEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fl on 2016/3/23.
 */
public class PartyResourceDao extends BaseDao{

    public ObjectId addPartyResource(PartyResourceEntry partyResourceEntry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_PARTYRESOURCE, partyResourceEntry.getBaseEntry());
        return partyResourceEntry.getID();
    }

    public PartyResourceEntry getPartyResourceEntry(ObjectId id) throws Exception{
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_PARTYRESOURCE, query, Constant.FIELDS);
        if(dbObject != null){
            return new PartyResourceEntry((BasicDBObject)dbObject);
        } else {
            throw new Exception("资源不存在");
        }
    }

    public List<PartyResourceEntry> getPartyResourceEntrys(List<ObjectId> directoryIds, String term, ObjectId userId, int skip, int limit){
        BasicDBObject query = new BasicDBObject().append("di", new BasicDBObject(Constant.MONGO_IN, directoryIds)).append("st", 0);
        if(!term.equals("ALLTERM")){
            query.append("tm", term);
        }
        if(userId != null){
            query.append("ui", userId);
        }
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_PARTYRESOURCE, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, skip, limit);
        List<PartyResourceEntry> partyResourceEntries = new ArrayList<PartyResourceEntry>();
        if(dbObjects != null){
            for(DBObject dbObject : dbObjects){
                partyResourceEntries.add(new PartyResourceEntry((BasicDBObject)dbObject));
            }
        }
        return partyResourceEntries;
    }

    public int countResources(List<ObjectId> directoryIds, String term, ObjectId userId){
        BasicDBObject query = new BasicDBObject().append("di", new BasicDBObject(Constant.MONGO_IN, directoryIds)).append("st", 0);
        if(!term.equals("ALLTERM")){
            query.append("tm", term);
        }
        if(userId != null){
            query.append("ui", userId);
        }
        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_PARTYRESOURCE, query);
        return count;
    }
}
