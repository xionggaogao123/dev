package com.db.controlphone;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.DBObject;
import com.sys.constants.Constant;

import java.util.List;

/**
 * Created by James on 2017/11/9.
 */
public class ControlAppResultDao extends BaseDao {
    /**
     * 批量保存
     *
     * @param list
     */
    public void addBatch(List<DBObject> list) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_APP_RESULT, list);
    }
}
