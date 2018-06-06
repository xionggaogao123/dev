package com.db.excellentCourses;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.excellentCourses.AccountOrderEntry;
import com.sys.constants.Constant;

/**
 * Created by James on 2018-06-05.
 */
public class AccountOrderDao  extends BaseDao{

    //添加支付订单
    public String addEntry(AccountOrderEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_ACCOUNT_ORDER, entry.getBaseEntry());
        return entry.getID().toString();
    }


    public AccountOrderEntry getEntry(String orderId){
        BasicDBObject query=new BasicDBObject("oid",orderId).append("isr",Constant.ZERO);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_ACCOUNT_ORDER,query,Constant.FIELDS);
        if(null!=dbObject){
            return new AccountOrderEntry((BasicDBObject) dbObject);
        }else {
            return null;
        }
    }
}
