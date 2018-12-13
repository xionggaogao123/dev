package com.db.extendedcourse;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.DBObject;
import com.pojo.extendedcourse.ExtendedCourseClassEntry;
import com.sys.constants.Constant;

import java.util.List;

/**
 * Created by James on 2018-12-10.
 */
public class ExtendedCourseClassDao extends BaseDao {
    public void saveEntry(ExtendedCourseClassEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_EXTENDED_COURSE_CLASS,entry.getBaseEntry());
    }

    public void addBatch(List<DBObject> list) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_EXTENDED_COURSE_CLASS, list);
    }
}
