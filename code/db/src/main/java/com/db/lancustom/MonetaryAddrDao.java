package com.db.lancustom;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.lancustom.MonetaryAddrEntry;
import com.pojo.lancustom.MonetaryGoodsEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: taotao.chan
 * @Date: 2018/8/7 11:35
 * @Description:
 */
public class MonetaryAddrDao extends BaseDao {
    /**
     * 新增地址
     * @param entry
     */
    public void addEntry(MonetaryAddrEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_MONETARY_ADDRESS, entry.getBaseEntry());
    }

    /**
     * 更新地址 逻辑删除 当前用户的地址
     * @param objectId
     * @param area
     * @param detail
     * @param name
     * @param telphone
     * @param userId
     */
    public void updateMonetaryAddr(ObjectId objectId, String area, String detail, String name, String telphone, String userId) {
        DBObject query = new BasicDBObject("uid", new ObjectId(userId));
        BasicDBObject updateValue=new BasicDBObject()
                .append(Constant.MONGO_SET,new BasicDBObject("isr",1)/*.append("detail",detail).append("telphone", telphone)
                        .append("userId", userId).append("name", name)*/);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_MONETARY_ADDRESS,query,updateValue);
    }

    public List<MonetaryAddrEntry> getUserAddrsList(ObjectId userId) {
        List<MonetaryAddrEntry> entries = new ArrayList<MonetaryAddrEntry>();
        BasicDBObject query=new BasicDBObject().append("isr", 0).append("uid",userId);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(),Constant.COLLECTION_MONETARY_ADDRESS,query,Constant.FIELDS,Constant.MONGO_SORTBY_DESC);
        if (null!=dbObjectList&&!dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList){
                entries.add(new MonetaryAddrEntry(dbObject));
            }
        }
        return entries;
    }

    public MonetaryAddrEntry getEntryById(ObjectId Id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, Id);
        DBObject obj =
                findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_MONETARY_ADDRESS, query, Constant.FIELDS);
        if (obj != null) {
            return new MonetaryAddrEntry((BasicDBObject) obj);
        }
        return null;
    }

    public MonetaryAddrEntry getEntryByUid(ObjectId userId) {
        BasicDBObject query = new BasicDBObject("uid", userId).append("isr", 0);
        DBObject obj =
                findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_MONETARY_ADDRESS, query, Constant.FIELDS);
        if (obj != null) {
            return new MonetaryAddrEntry((BasicDBObject) obj);
        }
        return null;
    }
}
