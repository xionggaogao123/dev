package com.db.withdrawCash;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.emarket.WithDrawEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojing on 2015/6/4.
 */
public class WithdrawCashDao extends BaseDao {

    public ObjectId addWithdrawCash(WithDrawEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_WITHDRAW_CASH_NAME, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 提现记录
     *
     * @return
     */
    public List<WithDrawEntry> selWithDrawEntryList(List<String> userids, ObjectId dslId, ObjectId delId, int page, int pageSize) {
        List<WithDrawEntry> retList = new ArrayList<WithDrawEntry>();
        BasicDBObject query = new BasicDBObject();
        if (userids != null && userids.size() != 0) {
            query.append("uid", new BasicDBObject(Constant.MONGO_IN, userids));
        }
        BasicDBList dblist = new BasicDBList();
        if (dslId != null) {
            dblist.add(new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_GTE, dslId)));
        }
        if (delId != null) {
            dblist.add(new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_LTE, delId)));
        }
        if (dblist.size() > 0) {
            query.append(Constant.MONGO_AND, dblist);
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_WITHDRAW_CASH_NAME, query, null, new BasicDBObject("_id", -1), page, pageSize);
        for (DBObject dbo : list) {
            retList.add(new WithDrawEntry((BasicDBObject) dbo));
        }
        return retList;
    }

    /**
     * @return
     */
    public int withDrawEntryCount(List<String> userids, ObjectId dslId, ObjectId delId) {
        BasicDBObject query = new BasicDBObject();
        if (userids != null && userids.size() != 0) {
            query.append("uid", new BasicDBObject(Constant.MONGO_IN, userids));
        }
        BasicDBList dblist = new BasicDBList();
        if (dslId != null) {
            dblist.add(new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_GTE, dslId)));
        }
        if (delId != null) {
            dblist.add(new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_LTE, delId)));
        }
        if (dblist.size() > 0) {
            query.append(Constant.MONGO_AND, dblist);
        }
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_WITHDRAW_CASH_NAME, query);
    }


    /**
     * @param userids
     * @param dslId
     * @param delId
     * @return
     */
    public List<WithDrawEntry> selWithDrawEntryList(List<String> userids, ObjectId dslId, ObjectId delId) {
        List<WithDrawEntry> retList = new ArrayList<WithDrawEntry>();
        BasicDBObject query = new BasicDBObject();
        if (userids != null && userids.size() != 0) {
            query.append("uid", new BasicDBObject(Constant.MONGO_IN, userids));
        }
        BasicDBList dblist = new BasicDBList();
        if (dslId != null) {
            dblist.add(new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_GTE, dslId)));
        }
        if (delId != null) {
            dblist.add(new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_LTE, delId)));
        }
        if (dblist.size() > 0) {
            query.append(Constant.MONGO_AND, dblist);
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_WITHDRAW_CASH_NAME, query, null, new BasicDBObject("_id", -1));
        for (DBObject dbo : list) {
            retList.add(new WithDrawEntry((BasicDBObject) dbo));
        }
        return retList;
    }
}
