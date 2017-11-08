package com.db.controlphone;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.pojo.controlphone.ControlMapEntry;
import com.sys.constants.Constant;

/**
 * Created by James on 2017/11/7.
 */
public class ControlMapDao extends BaseDao {

    //添加
    public String addEntry(ControlMapEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_MAP, entry.getBaseEntry());
        return entry.getID().toString() ;
    }

}
