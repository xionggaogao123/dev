package com.db.excellentCourses;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.pojo.excellentCourses.CoursesBusinessEntry;
import com.sys.constants.Constant;

/**
 * Created by James on 2018-09-18.
 */
public class CoursesBusinessDao extends BaseDao {
    //添加支付订单
    public String addEntry(CoursesBusinessEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSES_BUSINESS, entry.getBaseEntry());
        return entry.getID().toString();
    }

}
