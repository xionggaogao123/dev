package com.db.user;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.pojo.user.NewVersionUserRoleEntry;
import com.sys.constants.Constant;

/**
 * Created by scott on 2017/8/23.
 */
public class NewVersionUserRoleDao extends BaseDao{

    public void saveEntry(NewVersionUserRoleEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_USER_ROLE,entry.getBaseEntry());
    }

}
