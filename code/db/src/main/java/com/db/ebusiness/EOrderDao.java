package com.db.ebusiness;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBList;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.FieldValuePair;
import com.pojo.ebusiness.EOrderEntry;
import com.pojo.emarket.OrderState;
import com.sys.constants.Constant;

/**
 * 电子商务订单
 *
 * @author fourer
 */
public class EOrderDao extends BaseDao {

    /**
     * 添加
     *
     * @param e
     * @return
     */
    public ObjectId addEOrderEntry(EOrderEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_ORDERS, e.getBaseEntry());
        return e.getID();
    }


    /**
     * 更新多个字段值
     *
     * @param id
     * @param pairs
     */
    public void update(ObjectId id, FieldValuePair... pairs) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject valueDBO = new BasicDBObject();
        for (FieldValuePair pair : pairs) {
            valueDBO.append(pair.getField(), pair.getValue());
        }
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, valueDBO);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_ORDERS, query, updateValue);
    }

    /**
     * 更新快递信息
     *
     * @param orderId
     * @param goodsId
     * @param kindIds
     * @param expressNo
     * @param expressComNo
     */
    public void updateExpress(ObjectId orderId, ObjectId goodsId, List<ObjectId> kindIds, String expressNo, String expressComNo) {
        BasicDBObject query = new BasicDBObject(Constant.ID, orderId).append("ogs.egi", goodsId).append("ogs.kis", MongoUtils.convert(kindIds));
        BasicDBObject updateData = new BasicDBObject("ogs.$.en", expressNo).append("ogs.$.ecn", expressComNo);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, updateData);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_ORDERS, query, updateValue);
    }


    /**
     * 详情
     *
     * @param id
     * @return
     */
    public EOrderEntry getEOrderEntry(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_ORDERS, query, Constant.FIELDS);
        if (dbo != null) {
            EOrderEntry eOrderEntry = new EOrderEntry((BasicDBObject) dbo);
            return eOrderEntry;
        }
        return null;
    }


    /**
     * 查询
     *
     * @param ui
     * @param skip
     * @param limit
     * @return
     */
    public List<EOrderEntry> getEOrderEntrys(ObjectId ui, int state, int skip, int limit, boolean isParent) {
        BasicDBObject query = new BasicDBObject();
        if (null != ui) {
            if (isParent) {
                BasicDBList list = new BasicDBList();
                list.add(new BasicDBObject("ui", ui));
                list.add(new BasicDBObject("pid", ui));
                query.append(Constant.MONGO_OR, list);
            } else {
                query.append("ui", ui);
            }

        }
        if (state > 0) {
            query.append("st", state);
        } else {
            query.append("st", new BasicDBObject(Constant.MONGO_NE, OrderState.DELETE.getType()));
        }
        List<EOrderEntry> retList = new ArrayList<EOrderEntry>(limit);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_ORDERS, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, skip, limit);
        if (dbObjectList != null && dbObjectList.size() > 0) {
            for (DBObject dbo : dbObjectList) {
                EOrderEntry userEntry = new EOrderEntry((BasicDBObject) dbo);
                retList.add(userEntry);
            }
        }
        return retList;
    }

    /**
     * 查询订单个数
     *
     * @param ui
     * @param state
     * @return
     */
    public int getEOrderEntrysCount(ObjectId ui, int state, boolean isParent) {
        BasicDBObject query = new BasicDBObject();
        if (null != ui) {
            if (isParent) {
                BasicDBList list = new BasicDBList();
                list.add(new BasicDBObject("ui", ui));
                list.add(new BasicDBObject("pid", ui));
                query.append(Constant.MONGO_OR, list);
            } else {
                query.append("ui", ui);
            }
        }
        if (state > 0) {
            query.append("st", state);
        } else {
            query.append("st", new BasicDBObject(Constant.MONGO_NE, OrderState.DELETE.getType()));
        }
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_ORDERS, query);
    }


    /**
     * 删除
     *
     * @param id
     * @param userId
     */
    public void removeOrder(ObjectId id, ObjectId userId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id).append("ui", userId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("st", OrderState.DELETE.getType()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_ORDERS, query, updateValue);
    }
}
