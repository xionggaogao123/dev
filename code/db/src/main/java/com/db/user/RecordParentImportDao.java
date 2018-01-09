package com.db.user;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.user.RecordParentImportEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2018/1/5.
 */
public class RecordParentImportDao extends BaseDao{

    public void saveRecordParentImport(RecordParentImportEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_RECORD_PARENT_IMPORT,entry.getBaseEntry());
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
