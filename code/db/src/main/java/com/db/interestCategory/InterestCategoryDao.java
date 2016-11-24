package com.db.interestCategory;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.interestCategory.InterestCategoryEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fl on 2015/11/17.
 */
public class InterestCategoryDao extends BaseDao {

    /**
     * 新增
     * @param interestCategoryEntry
     */
    public void addInterestCategory(InterestCategoryEntry interestCategoryEntry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_INTERESTCATEGORY_NAME, interestCategoryEntry.getBaseEntry());
    }

    public void removeInterestCategory(ObjectId interestCategoryId){
        DBObject query = new BasicDBObject(Constant.ID, interestCategoryId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_INTERESTCATEGORY_NAME, query);
    }

    public void updateInterestCategory(ObjectId interestCategoryId, String name){
        DBObject query = new BasicDBObject(Constant.ID, interestCategoryId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("nm", name));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_INTERESTCATEGORY_NAME, query, updateValue);
    }

    public List<InterestCategoryEntry> findInterestCategory(ObjectId schoolId){
        DBObject query = new BasicDBObject("sid", schoolId);
        List<DBObject> basicDBList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_INTERESTCATEGORY_NAME, query, Constant.FIELDS);
        if(basicDBList != null){
            List<InterestCategoryEntry> list = new ArrayList<InterestCategoryEntry>();
            for(DBObject dbObject : basicDBList) {
                list.add(new InterestCategoryEntry((BasicDBObject)dbObject));
            }
            return list;
        }
        return null;
    }
}
