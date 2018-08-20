package com.db.lancustom;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.lancustom.MonetaryGoodsEntry;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: taotao.chan
 * @Date: 2018/8/6 16:50
 * @Description: 商品DAO
 */
public class MonetaryGoodsDao extends BaseDao {

    public ObjectId addEntry(MonetaryGoodsEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_MONETARY_GOODS, entry.getBaseEntry());
        return entry.getID();
    }

    /**
     * 新增或更新商品
     * @param objectId
     * @param avatar
     * @param description
     * @param label
     * @param money
     * @param name
     * @param pic
     * @param style
     */
    public void updateGoods(ObjectId objectId, String avatar, String description, String label, Double money, String name, String pic, String style) {
        DBObject query = new BasicDBObject(Constant.ID, objectId);
        BasicDBObject updateValue=new BasicDBObject()
                .append(Constant.MONGO_SET,new BasicDBObject("avatar",avatar).append("desc",description).append("label", label)
                        .append("money", money).append("name", name).append("pic", pic).append("style", style));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_MONETARY_GOODS,query,updateValue);
    }

    /**
     * 获取当前页的商品列表
     * @param name
     * @param page
     * @param pageSize
     * @return
     */
    public List<MonetaryGoodsEntry> getMonetaryGoodsList(String name, int page, int pageSize) {
        List<MonetaryGoodsEntry> entries = new ArrayList<MonetaryGoodsEntry>();

        BasicDBObject query = new BasicDBObject().append("isr", 0);
        if (StringUtils.isNotBlank(name)) {
            query.append("name", new BasicDBObject(Constant.MONGO_REGEX, name));
        }
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_MONETARY_GOODS,
                query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, (page - 1) * pageSize, pageSize);
        if (null != dbObjectList && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                entries.add(new MonetaryGoodsEntry(dbObject));
            }
        }
        return entries;
    }

    /**
     * 获取列表条数
     * @param name
     * @return
     */
    public int getMonetaryGoodsCount(String name) {
        BasicDBObject query=new BasicDBObject().append("isr", 0);
        if (StringUtils.isNotBlank(name)) {
            query.append("name", new BasicDBObject(Constant.MONGO_REGEX,name));
        }
        int count =count(MongoFacroty.getAppDB(), Constant.COLLECTION_MONETARY_GOODS,
                query);

        return count;
    }

    public void updateIsr(ObjectId goodId) {
        DBObject query = new BasicDBObject(Constant.ID, goodId);
        BasicDBObject updateValue=new BasicDBObject()
                .append(Constant.MONGO_SET,new BasicDBObject("isr",1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_MONETARY_GOODS,query,updateValue);
    }

    public MonetaryGoodsEntry getEntryById(ObjectId goodId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, goodId);
        DBObject obj =
                findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_MONETARY_GOODS, query, Constant.FIELDS);
        if (obj != null) {
            return new MonetaryGoodsEntry((BasicDBObject) obj);
        }
        return null;
    }
}
