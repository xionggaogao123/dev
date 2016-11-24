package com.db.examresult;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.examregional.RegionalExamEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by fl on 2015/10/27.
 */
public class JointExamDao extends BaseDao {
    /**
     * @param id
     * @return
     */
    public RegionalExamEntry find(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_JOINTEXAM, query, Constant.FIELDS);
        if (dbo != null)
            return new RegionalExamEntry((BasicDBObject) dbo);
        return null;
    }

    public void update(RegionalExamEntry jointExamEntry) {
        DBObject query = new BasicDBObject(Constant.ID, jointExamEntry.getID());
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, jointExamEntry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_JOINTEXAM, query, updateValue);
    }

    public void update(ObjectId id, BasicDBObject value) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, value);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_JOINTEXAM, query, updateValue);
    }
}
