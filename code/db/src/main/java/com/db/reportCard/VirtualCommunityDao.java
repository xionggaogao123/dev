package com.db.reportCard;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.reportCard.VirtualCommunityEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by scott on 2017/11/24.
 */
public class VirtualCommunityDao extends BaseDao{

    public void saveVirtualCommunity(VirtualCommunityEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_VIRTUAL_COMMUNITY,entry.getBaseEntry());
    }

    public VirtualCommunityEntry findntryByCommunityId(ObjectId communityId){
        BasicDBObject query=new BasicDBObject()
                .append("cid",communityId);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_VIRTUAL_COMMUNITY,query,Constant.FIELDS);
        if(null!=dbObject){
            return new VirtualCommunityEntry(dbObject);
        }else {
            return null;
        }
    }


    public Map<ObjectId,VirtualCommunityEntry> getVirtualMap(List<ObjectId> communityIds){
        Map<ObjectId,VirtualCommunityEntry>
                map=new HashMap<ObjectId, VirtualCommunityEntry>();
        BasicDBObject query=new BasicDBObject("cid",new BasicDBObject(Constant.MONGO_IN,communityIds));
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_VIRTUAL_COMMUNITY,
                query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                VirtualCommunityEntry
                        communityEntry=new VirtualCommunityEntry(dbObject);
                map.put(communityEntry.getCommunityId(),communityEntry);
            }
        }
        return map;
    }
}
