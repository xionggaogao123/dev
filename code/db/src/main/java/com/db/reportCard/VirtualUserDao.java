package com.db.reportCard;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.reportCard.VirtualUserEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/11/16.
 */
public class VirtualUserDao extends BaseDao{

    public void saveVirualEntry(VirtualUserEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_VIRTUAL_USER,entry.getBaseEntry());
    }

    public void saveEntries(List<VirtualUserEntry> entries){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_VIRTUAL_USER, MongoUtils.fetchDBObjectList(entries));
    }

    public void removeOldData(ObjectId communityId){
        BasicDBObject query=new BasicDBObject()
                .append("cid",communityId)
                .append("ir",Constant.ZERO);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("ir",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_VIRTUAL_USER,query,updateValue);
    }

    public List<VirtualUserEntry> getAllVirtualUsers(ObjectId communityId){
        List<VirtualUserEntry> entries=new ArrayList<VirtualUserEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("ir",Constant.ZERO)
                .append("cid",communityId);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_VIRTUAL_USER,query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new VirtualUserEntry(dbObject));
            }
        }
        return entries;
    }
}
