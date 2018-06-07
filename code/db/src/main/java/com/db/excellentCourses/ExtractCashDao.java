package com.db.excellentCourses;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.pojo.excellentCourses.ExtractCashEntry;
import com.sys.constants.Constant;

/**
 * Created by James on 2018-06-06.
 */
public class ExtractCashDao extends BaseDao {

    //添加提现申请
    public String addEntry(ExtractCashEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_EXTRACT_CASH, entry.getBaseEntry());
        return entry.getID().toString();
    }
}
