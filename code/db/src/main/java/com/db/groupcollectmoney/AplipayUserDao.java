package com.db.groupcollectmoney;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.pojo.groupcollectmoney.AplipayUserEntry;
import com.sys.constants.Constant;

/**
 * Created by scott on 2018/1/17.
 */
public class AplipayUserDao extends BaseDao{

    public void saveAplipayUserEntry(AplipayUserEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_APLIPAY_USER,entry.getBaseEntry());
    }
}
