package com.db.lancustom;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.lancustom.MonetaryOrdersEntry;
import com.sys.constants.Constant;
import com.sys.utils.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Auther: taotao.chan
 * @Date: 2018/8/7 18:39
 * @Description:
 */
public class MonetaryOrdersDao extends BaseDao {

    public String addEntry(MonetaryOrdersEntry ordersEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_MONETARY_ORDER, ordersEntry.getBaseEntry());
        return ordersEntry.getID().toString();
    }

    public void updateOrder(Map map) {
        DBObject query = new BasicDBObject(Constant.ID, new ObjectId(map.get("orderId").toString()));
        BasicDBObject updateParam = new BasicDBObject();
        if (map.get("userId") != null) {
            updateParam.append("uid",new ObjectId(map.get("userId").toString()));
        }
        if (map.get("goodId") != null) {
            updateParam.append("goodId",new ObjectId(map.get("goodId").toString()));
        }
        if (map.get("addressId") != null) {
            updateParam.append("addressId",new ObjectId(map.get("addressId").toString()));
        }
        if (map.get("goodNum") != null) {
            updateParam.append("goodNum",map.get("goodNum"));
        }
        if (map.get("style") != null) {
            updateParam.append("style",map.get("style"));
        }
        if (map.get("money") != null) {
            updateParam.append("money",map.get("money"));
        }
        if (map.get("excompanyNo") != null) {
            updateParam.append("excompanyNo",map.get("excompanyNo"));
        }
        if (map.get("expressNo") != null) {
            updateParam.append("expressNo",map.get("expressNo"));
        }
        if (map.get("orderTimeStr") != null) {
            updateParam.append("orderTimeStr",map.get("orderTimeStr"));
        }
        if (map.get("status") != null) {
            updateParam.append("status",map.get("status"));
        }
        if (map.get("payOrderTimeStr") != null) {
            updateParam.append("payOrderTimeStr",map.get("payOrderTimeStr"));
        }
        if (map.get("payMethod") != null) {
            updateParam.append("payMethod",map.get("payMethod"));
        }
        if (map.get("tradeNo") != null) {
            updateParam.append("tradeNo",map.get("tradeNo"));
        }
        if (map.get("isState") != null) {
            updateParam.append("isState",map.get("isState"));
        }
        if (map.get("stateReason") != null) {
            updateParam.append("stateReason",map.get("stateReason"));
        }
        if (map.get("isAcceptance") != null) {
            updateParam.append("isAcceptance",map.get("isAcceptance"));
        }
        if (map.get("acceptanceStr") != null) {
            updateParam.append("acceptanceStr",map.get("acceptanceStr"));
        }
        if (map.get("isr") != null) {
            updateParam.append("isr",map.get("isr"));
        }
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, updateParam);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_MONETARY_ORDER,query,updateValue);
    }

    public List<MonetaryOrdersEntry> getMonetaryOrderList(Map map) {
        int page = Integer.parseInt(map.get("page").toString());
        int pageSize = Integer.parseInt(map.get("pageSize").toString());
        List<MonetaryOrdersEntry> entries = new ArrayList<MonetaryOrdersEntry>();

        BasicDBObject query = new BasicDBObject().append("isr", 0);
        //已经购买状态 status 为 "1"
        query.append("status","1") ;
        if (StringUtils.isNotBlank(map.get("userId").toString())) {
            query.append("uid",new ObjectId(map.get("userId").toString())) ;
        }
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_MONETARY_ORDER,
                query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if (null != dbObjectList && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                entries.add(new MonetaryOrdersEntry(dbObject));
            }
        }
        return entries;
    }

    public MonetaryOrdersEntry getEntryById(ObjectId orderId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, orderId);
        DBObject obj =
                findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_MONETARY_ORDER, query, Constant.FIELDS);
        if (obj != null) {
            return new MonetaryOrdersEntry((BasicDBObject) obj);
        }
        return null;
    }

    public List<MonetaryOrdersEntry> getOrderList(String orderNo, int page, int pageSize) {
        List<MonetaryOrdersEntry> entries = new ArrayList<MonetaryOrdersEntry>();

        BasicDBObject query = new BasicDBObject().append("isr", 0);
        if (StringUtils.isNotBlank(orderNo)) {
            query.append("orderNo",orderNo) ;
        }
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_MONETARY_ORDER,
                query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if (null != dbObjectList && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                entries.add(new MonetaryOrdersEntry(dbObject));
            }
        }
        return entries;
    }

    public List<MonetaryOrdersEntry> getOrderListAll(String orderNo) {
        List<MonetaryOrdersEntry> entries = new ArrayList<MonetaryOrdersEntry>();

        BasicDBObject query = new BasicDBObject().append("isr", 0);
        if (StringUtils.isNotBlank(orderNo)) {
            query.append("orderNo",orderNo) ;
        }
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_MONETARY_ORDER,
                query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if (null != dbObjectList && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                entries.add(new MonetaryOrdersEntry(dbObject));
            }
        }
        return entries;
    }

    public int getMonetaryPersonalOrderListCount(Map<String,Object> pMap) {
        BasicDBObject query = new BasicDBObject().append("isr", 0);
        //已经购买状态 status 为 "1"
        query.append("status","1") ;
        if (StringUtils.isNotBlank(pMap.get("userId").toString())) {
            query.append("uid",new ObjectId(pMap.get("userId").toString())) ;
        }
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_MONETARY_ORDER,
                query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        return dbObjectList.size();
    }
}
