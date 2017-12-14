package com.db.user;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.user.MakeOutUserRelationEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/12/14.
 */
public class MakeOutUserRelationDao extends BaseDao{

    public void saveEntry(MakeOutUserRelationEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_MAKE_OUT_USER_RELATION,entry.getBaseEntry());
    }


    public void removeByParentId(ObjectId parentId){
        BasicDBObject query = new BasicDBObject("pid",parentId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_MAKE_OUT_USER_RELATION,query);
    }

    public void removeItemById(ObjectId id){
        BasicDBObject query=new BasicDBObject(Constant.ID,id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_MAKE_OUT_USER_RELATION,query);
    }

    public MakeOutUserRelationEntry getRelationEntry(ObjectId parentId,String userKey){
        BasicDBObject query = new BasicDBObject("pid",parentId)
                .append("uk", userKey);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_MAKE_OUT_USER_RELATION,query,Constant.FIELDS);
        if(null!=dbObject){
            return new MakeOutUserRelationEntry(dbObject);
        }else {
            return null;
        }
    }

    public  List<MakeOutUserRelationEntry> getRelationEntriesByParentIds(List<ObjectId> parentIds){
        BasicDBObject query = new BasicDBObject("pid",new BasicDBObject(Constant.MONGO_IN,parentIds));
        List<MakeOutUserRelationEntry> relationEntries =new ArrayList<MakeOutUserRelationEntry>();
        List<DBObject> dbObjectList= find(MongoFacroty.getAppDB(), Constant.COLLECTION_MAKE_OUT_USER_RELATION,query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                relationEntries.add(new MakeOutUserRelationEntry(dbObject));
            }
        }
        return relationEntries;
    }



    public List<MakeOutUserRelationEntry> getEntries(ObjectId parentId){
        BasicDBObject query = new BasicDBObject("pid",parentId);
        List<MakeOutUserRelationEntry> relationEntries =new ArrayList<MakeOutUserRelationEntry>();
        List<DBObject> dbObjectList= find(MongoFacroty.getAppDB(), Constant.COLLECTION_MAKE_OUT_USER_RELATION,query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                relationEntries.add(new MakeOutUserRelationEntry(dbObject));
            }
        }
        return relationEntries;
    }
}
