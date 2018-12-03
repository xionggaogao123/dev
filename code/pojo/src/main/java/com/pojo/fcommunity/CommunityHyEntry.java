package com.pojo.fcommunity;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;

public class CommunityHyEntry extends BaseDBObject {

    public CommunityHyEntry(DBObject dbo) {
        setBaseEntry((BasicDBObject) dbo);
    }
    
    public CommunityHyEntry(ObjectId _id, ObjectId communityId) {
        BasicDBObject dbo = new BasicDBObject()
            .append(Constant.ID,_id)
            .append("cmid", communityId)
            .append("ti", System.currentTimeMillis());
        setBaseEntry(dbo);
    }
    
    public ObjectId getCommunityId() {
        if(getBaseEntry().containsKey("cmid")){
          return getSimpleObjecIDValue("cmid");
        }
        return null;
      }
    
}
