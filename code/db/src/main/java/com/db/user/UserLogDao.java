package com.db.user;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.pojo.user.UserLogEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by wang_xinxin on 2016/5/4.
 */
public class UserLogDao extends BaseDao {
    /**
     * 添加log
     *
     * @param e
     * @return
     */
    public ObjectId addUserLogLog(UserLogEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_LOG_NAME, e.getBaseEntry());
        return e.getID();
    }
}
