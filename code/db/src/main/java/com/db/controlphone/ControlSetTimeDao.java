package com.db.controlphone;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.pojo.controlphone.ControlSetTimeEntry;
import com.sys.constants.Constant;

/**
 * Created by James on 2017/11/20.
 */
public class ControlSetTimeDao extends BaseDao{
    //添加
    public String addEntry(ControlSetTimeEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_SET_TIME, entry.getBaseEntry());
        return entry.getID().toString() ;
    }
}
