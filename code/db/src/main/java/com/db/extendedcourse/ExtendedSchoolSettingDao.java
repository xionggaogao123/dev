package com.db.extendedcourse;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.extendedcourse.ExtendedSchoolSettingEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018-12-06.
 *
 * 拓展课学校设置
 *
 */
public class ExtendedSchoolSettingDao extends BaseDao {
    public void saveEntry(ExtendedSchoolSettingEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_EXTENDED_SCHOOL_SETTING,entry.getBaseEntry());
    }


    public ExtendedSchoolSettingEntry getEntryBySchoolId(ObjectId schoolId){
        BasicDBObject query = new BasicDBObject();
        query.append("isr",Constant.ZERO);
        query.append("sid",schoolId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_EXTENDED_SCHOOL_SETTING,query,Constant.FIELDS);
        if(dbObject==null){
            return null;
        }else{
            return new ExtendedSchoolSettingEntry((BasicDBObject) dbObject);
        }
    }
}
