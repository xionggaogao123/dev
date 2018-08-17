package com.db.excellentCourses;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.excellentCourses.AccountOrderEntry;
import com.sys.constants.Constant;

import java.util.List;

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
        BasicDBObject query=new BasicDBObject("oid",orderId).append("sta",Constant.ZERO).append("isr", Constant.ZERO);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_ACCOUNT_ORDER,query,Constant.FIELDS);
        if(null!=dbObject){
            return new AccountOrderEntry((BasicDBObject) dbObject);
        }else {
            return null;
        }
    }

    public String getEntryListByOrderId(List<String> orderIds) {
        BasicDBObject query = new BasicDBObject()
                .append("oid", new BasicDBObject(Constant.MONGO_IN,orderIds))
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_ACCOUNT_ORDER,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        StringBuffer sb = new StringBuffer();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                AccountOrderEntry accountOrderEntry = new AccountOrderEntry((BasicDBObject) obj);
                sb.append(accountOrderEntry.getOrder());
            }
        }
        return sb.toString();
    }

    public AccountOrderEntry getNotEntry(String orderId){
        BasicDBObject query=new BasicDBObject("oid",orderId).append("sta",Constant.ONE).append("isr", Constant.ZERO);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_ACCOUNT_ORDER,query,Constant.FIELDS);
        if(null!=dbObject){
            return new AccountOrderEntry((BasicDBObject) dbObject);
        }else {
            return null;
        }
    }
}
