package com.db.newVersionGrade;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.newVersionGrade.NewVersionSubjectEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/10/10.
 */
public class NewVersionSubjectDao extends BaseDao{

    public void saveNewVersionSubjectEntry(NewVersionSubjectEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_SUBJECT,entry.getBaseEntry());
    }

    public NewVersionSubjectEntry getEntryByUserId(ObjectId userId){
        BasicDBObject query=new BasicDBObject()
                .append("uid",userId);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_SUBJECT,query,Constant.FIELDS);
        if(null!=dbObject){
            return new NewVersionSubjectEntry(dbObject);
        }else{
            return null;
        }
    }

    public NewVersionSubjectEntry getAllEntryByUserId(){
        BasicDBObject query=new BasicDBObject();
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_SUBJECT,query,Constant.FIELDS);
        if(null!=dbObject){
            return new NewVersionSubjectEntry(dbObject);
        }else{
            return null;
        }
    }
}
