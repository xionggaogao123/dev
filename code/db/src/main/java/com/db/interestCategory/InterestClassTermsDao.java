package com.db.interestCategory;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.IdNameValuePair;
import com.pojo.interestCategory.InterestClassTermsEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by fl on 2016/1/25.
 */
public class InterestClassTermsDao extends BaseDao {

    /**
     * 添加
     *
     * @param entry
     * @return
     */
    public ObjectId add(InterestClassTermsEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_INTERESTTERMS_NAME, entry.getBaseEntry());
        return entry.getID();
    }

    /**
     * 根据学校id查找
     *
     * @param schoolId
     * @return
     */
    public InterestClassTermsEntry findInterestClassTermsEntryBySchoolId(ObjectId schoolId) {
        BasicDBObject query = new BasicDBObject("sid", schoolId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_INTERESTTERMS_NAME, query, Constant.FIELDS);
        if (dbObject != null) {
            return new InterestClassTermsEntry((BasicDBObject) dbObject);
        } else {
            return null;
        }
    }

    /**
     * 增加一个学期
     *
     * @param schoolId
     * @param term
     */
    public void updateTerms(ObjectId schoolId, IdNameValuePair term) {
        DBObject query = new BasicDBObject("sid", schoolId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PUSH, new BasicDBObject("tms", term.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_INTERESTTERMS_NAME, query, updateValue);
    }
}
