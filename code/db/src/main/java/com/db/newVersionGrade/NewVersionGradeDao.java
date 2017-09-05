package com.db.newVersionGrade;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.newVersionGrade.NewVersionGradeEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/9/5.
 */
public class NewVersionGradeDao extends BaseDao{

    public void saveNewVersionGradeEntry(NewVersionGradeEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_GRADE,entry.getBaseEntry());
    }

    public NewVersionGradeEntry getEntryByCondition(ObjectId userId,
                                                    String year){
        BasicDBObject query = new BasicDBObject()
                .append("uid",userId)
                .append("ye",year);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_GRADE,
                query,Constant.FIELDS);
        if(null!=dbObject){
            return new NewVersionGradeEntry(dbObject);
        }else {
            return null;
        }
    }
}
