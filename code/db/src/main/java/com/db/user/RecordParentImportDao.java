package com.db.user;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.user.RecordParentImportEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2018/1/5.
 */
public class RecordParentImportDao extends BaseDao{

    public void saveRecordParentImport(RecordParentImportEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_RECORD_PARENT_IMPORT,entry.getBaseEntry());
    }


    public void removeById(ObjectId id){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_RECORD_PARENT_IMPORT,query);
    }

    public void removeEntry(ObjectId parentId,
                            String userKey,
                            String nickName){
        BasicDBObject query = new BasicDBObject()
                .append("pid",parentId)
                .append("nnm",nickName)
                .append("uk",userKey);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_RECORD_PARENT_IMPORT,query);
    }


    public RecordParentImportEntry getEntry(ObjectId id){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_RECORD_PARENT_IMPORT,query,Constant.FIELDS);
        if(null!=dbObject){
            return new RecordParentImportEntry(dbObject);
        }else{
            return null;
        }
    }


    public List<RecordParentImportEntry> getEntries(ObjectId parentId){
        List<RecordParentImportEntry> entries = new ArrayList<RecordParentImportEntry>();
        BasicDBObject query = new BasicDBObject()
                .append("pid",parentId);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_RECORD_PARENT_IMPORT,query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new RecordParentImportEntry(dbObject));
            }
        }
        return entries;
    }

    public RecordParentImportEntry getEntry(ObjectId parentId,
                                            String userKey){
        BasicDBObject query = new BasicDBObject()
                .append("pid",parentId)
                .append("uk",userKey);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_RECORD_PARENT_IMPORT,query,
                Constant.FIELDS);
        if(null!=dbObject){
            return new RecordParentImportEntry(dbObject);
        }else {
            return null;
        }
    }
}
