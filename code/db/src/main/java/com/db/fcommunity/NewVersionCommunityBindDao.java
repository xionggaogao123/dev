package com.db.fcommunity;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.fcommunity.NewVersionCommunityBindEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by scott on 2017/9/13.
 */
public class NewVersionCommunityBindDao extends BaseDao{

    public void saveEntry(NewVersionCommunityBindEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_COMMUNITY_BIND,entry.getBaseEntry());
    }

    public void saveEntries(List<NewVersionCommunityBindEntry> entryList){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_COMMUNITY_BIND, MongoUtils.fetchDBObjectList(entryList));
    }

    public void updateEntryStatus(ObjectId id){
        BasicDBObject query=new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject( "ir", Constant.ZERO));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_COMMUNITY_BIND,query,updateValue);
    }



    public void removeNewVersionCommunity(ObjectId communityId,
                                          ObjectId mainUserId){
        BasicDBObject query=new BasicDBObject()
                .append("cid",communityId)
                .append("muid",mainUserId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("ir",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_COMMUNITY_BIND,query,updateValue);
    }

    public NewVersionCommunityBindEntry getEntry(ObjectId communityId,
                                                 ObjectId mainUserId,
                                                 ObjectId userId){
        BasicDBObject query=new BasicDBObject()
                .append("cid",communityId)
                .append("muid",mainUserId)
                .append("uid",userId);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_COMMUNITY_BIND,
                query,Constant.FIELDS);
        if(null!=dbObject){
            return new NewVersionCommunityBindEntry(dbObject);
        }else{
            return null;
        }
    }
}
