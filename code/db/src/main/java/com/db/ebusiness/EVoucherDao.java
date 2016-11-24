package com.db.ebusiness;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.ebusiness.EVoucherEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.mails.MailUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fl on 2016/3/7.
 */
public class EVoucherDao extends BaseDao {

    /**
     * 添加
     *
     * @param entry
     * @return
     */
    public ObjectId add(EVoucherEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_VOUCHER, entry.getBaseEntry());
        return entry.getID();
    }

    public Boolean add(List<EVoucherEntry> eVoucherEntries) {
        List<DBObject> dbObjects = MongoUtils.fetchDBObjectList(eVoucherEntries);
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_VOUCHER, dbObjects);
        return true;
    }

    /**
     * 根据id查找抵用券
     *
     * @param voucherId
     * @return
     */
    public EVoucherEntry getEVoucherEntry(ObjectId voucherId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, voucherId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_VOUCHER, query, Constant.FIELDS);
        if (dbObject != null) {
            return new EVoucherEntry((BasicDBObject) dbObject);
        }
        return null;
    }

    /**
     * 根据id查找未使用的抵用券
     */
    public EVoucherEntry getUnUseEVoucherEntry(ObjectId voucherId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, voucherId);
        query.append("st", 0);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_VOUCHER, query, Constant.FIELDS);
        if (dbObject != null) {
            return new EVoucherEntry((BasicDBObject) dbObject);
        }
        return null;
    }

    /**
     * 根据券号查找抵用券
     *
     * @param voucherNo
     * @return
     */
    public EVoucherEntry getEVoucherEntryByNo(String voucherNo) {
        DBObject query = new BasicDBObject("num", voucherNo);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_VOUCHER, query, Constant.FIELDS);
        if (dbObject != null) {
            return new EVoucherEntry((BasicDBObject) dbObject);
        }
        return null;
    }

    /**
     * 根据用户id和抵用券状态查找抵用券
     *
     * @param userId
     * @return
     */
    public List<EVoucherEntry> getEVoucherEntrysByUserIdAndState(ObjectId userId, int state) {
        DBObject query = new BasicDBObject("uid", userId).append("st", state);
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_VOUCHER, query, Constant.FIELDS);
        List<EVoucherEntry> eVoucherEntries = new ArrayList<EVoucherEntry>();
        if (dbObjects != null) {
            for (DBObject dbObject : dbObjects) {
                eVoucherEntries.add(new EVoucherEntry((BasicDBObject) dbObject));
            }
        }
        return eVoucherEntries;
    }


    public List<EVoucherEntry> getAllEVouchers(int skip, int limit) {
        DBObject query = new BasicDBObject();
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_VOUCHER, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, skip, limit);
        List<EVoucherEntry> eVoucherEntries = new ArrayList<EVoucherEntry>();
        if (dbObjects != null) {
            for (DBObject dbObject : dbObjects) {
                eVoucherEntries.add(new EVoucherEntry((BasicDBObject) dbObject));
            }
        }
        return eVoucherEntries;
    }

    public int countEVouchers() {
        DBObject query = new BasicDBObject();
        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_VOUCHER, query);
        return count;
    }

    public int countEVouchersByUserId(ObjectId userId) {
        BasicDBObject query = new BasicDBObject("uid", userId);
        query.append("ac", 1);
        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_VOUCHER, query);
        return count;
    }

    //用于定时检查抵用券是否过期
    public void checkVoucherExpiration() {
        List<Integer> states = new ArrayList<Integer>();
        states.add(0);
        states.add(3);
        states.add(4);

        DBObject query = new BasicDBObject("st", new BasicDBObject(Constant.MONGO_IN, states)).append("et", new BasicDBObject(Constant.MONGO_LT, System.currentTimeMillis()));
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("st", 2));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_VOUCHER, query, updateValue);
    }
}
