package com.db.groupcollectmoney;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.pojo.groupcollectmoney.GroupCollectMoneyEntry;
import com.sys.constants.Constant;

/**
 * Created by scott on 2018/1/10.
 */
public class GroupCollectMoneyDao extends BaseDao{

    public void saveCollectMoney(GroupCollectMoneyEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_GROUP_COLLECT_MONEY,entry.getBaseEntry());
    }




}
