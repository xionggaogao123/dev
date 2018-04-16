package com.db.integral;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.pojo.integral.IntegralSufferEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018-04-16.
 */
public class IntegralSufferDao extends BaseDao {

    //添加临时记录
    public ObjectId addEntry(IntegralSufferEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_INTEGRAL_SUFFER, entry.getBaseEntry());
        return entry.getID();
    }
}
