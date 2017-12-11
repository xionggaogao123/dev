package com.db.user;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.appnotice.GenerateUserCodeEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

import java.util.List;

/**
 * Created by scott on 2017/12/11.
 */
public class GenerateUserCodeDao extends BaseDao{

    public void saveEntry(GenerateUserCodeEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_GENERATE_USER_CODE,entry.getBaseEntry());
    }

    public void saveEntries(List<GenerateUserCodeEntry> entries){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_GENERATE_USER_CODE, MongoUtils.fetchDBObjectList(entries));
    }

    public GenerateUserCodeEntry findLastEntry(){
        BasicDBObject order=new BasicDBObject("sei",-1);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_GENERATE_USER_CODE,
                new BasicDBObject(),Constant.FIELDS,order,0,1);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            GenerateUserCodeEntry entry=new GenerateUserCodeEntry(dbObjectList.get(0));
            return entry;
        }else {
            return null;
        }
    }

    public long findEntryByLast(){
        long result=500499L;
        BasicDBObject order=new BasicDBObject("sei",-1);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_GENERATE_USER_CODE,
                new BasicDBObject(),Constant.FIELDS,order,0,1);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            GenerateUserCodeEntry entry=new GenerateUserCodeEntry(dbObjectList.get(0));
            result=entry.getSeqId();
        }
        return result;
    }

    public GenerateUserCodeEntry getCodeEntry() {
        BasicDBObject query = new BasicDBObject()
                .append("ir", Constant.ZERO)
                .append("rn", new BasicDBObject(Constant.MONGO_GTE, Math.random()));
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ir", Constant.ONE));
        DBObject dbo = findAndModifed(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_GENERATE_USER_CODE, query, update);
        if(null!=dbo) {
            return new GenerateUserCodeEntry(dbo);
        }else {
            return null;
        }
    }


    public void updateStatus(long code){
        BasicDBObject query = new BasicDBObject()
                .append("sei",code);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ir", Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_GENERATE_USER_CODE, query, update);
    }
}
