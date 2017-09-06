package com.db.newVersionGrade;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.newVersionGrade.NewVersionGradeEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

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
                .append("uid", userId)
                .append("isr", Constant.ZERO)
                .append("ye", year);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_GRADE,
                query, Constant.FIELDS);
        if(null!=dbObject){
            return new NewVersionGradeEntry((BasicDBObject) dbObject);
        }else {
            return null;
        }
    }

    public int getMaxGradeType(ObjectId userId){
        BasicDBObject query = new BasicDBObject()
                .append("uid", userId)
                .append("isr", Constant.ZERO);
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_NEW_VERSION_GRADE,
                        query, Constant.FIELDS,
                        new BasicDBObject("gt", Constant.DESC));
        List<NewVersionGradeEntry> entryList = new ArrayList<NewVersionGradeEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new NewVersionGradeEntry((BasicDBObject) obj));
            }
        }
        if(entryList.size()>0){
            return entryList.get(0).getGradeType();
        }else{
            return 0;
        }
    }
}
