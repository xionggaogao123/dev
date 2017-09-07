package com.db.user;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.user.NewVersionBindRelationEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/8/28.
 */
public class NewVersionBindRelationDao extends BaseDao{

    public ObjectId saveNewVersionBindEntry(NewVersionBindRelationEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_BIND_RELATION,entry.getBaseEntry());
        return entry.getID();
    }

    public NewVersionBindRelationEntry getBindEntry(ObjectId userId){
        BasicDBObject query = new BasicDBObject()
                .append("uid",userId);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_BIND_RELATION,query,Constant.FIELDS);
        if(null!=dbObject){
            return new NewVersionBindRelationEntry(dbObject);
        }else{
            return null;
        }
    }

    public NewVersionBindRelationEntry getEntry(ObjectId bindId){
        BasicDBObject query = new BasicDBObject()
                .append(Constant.ID,bindId);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_BIND_RELATION,query,Constant.FIELDS);
        if(null!=dbObject){
            return new NewVersionBindRelationEntry(dbObject);
        }else{
            return null;
        }
    }

    //父id
    public List<NewVersionBindRelationEntry> getEntriesByMainUserId(
            ObjectId mainUserId
    ){
        List<NewVersionBindRelationEntry> entries
                =new ArrayList<NewVersionBindRelationEntry>();
        BasicDBObject query = new BasicDBObject()
                .append("muid",mainUserId);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_BIND_RELATION,
                query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new NewVersionBindRelationEntry(dbObject));
            }
        }
        return entries;
    }
}
