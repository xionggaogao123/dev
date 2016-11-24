package com.db.ebusiness;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.ebusiness.EExpressTemplateEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 运费模板Dao
 * Created by Wangkaidong on 2016/3/9.
 */
public class EExpressTemplateDao extends BaseDao {
    /**
     * 查询全部(分页)
     */
    public List<EExpressTemplateEntry> getEntryList(int page, int pageSize) {
        List<EExpressTemplateEntry> retList = new ArrayList<EExpressTemplateEntry>();
        List<DBObject> dbos = find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXPRESS_TEMPLATE_NAME, Constant.QUERY, Constant.FIELDS, Constant.MONGO_SORTBY_ASC, page, pageSize * (page - 1));
        if (null != dbos && dbos.size() > 0) {
            for (DBObject dbo : dbos) {
                retList.add(new EExpressTemplateEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }

    /**
     * 查询模板总数
     */
    public int getTotal() {
        BasicDBObject query = new BasicDBObject();
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_EXPRESS_TEMPLATE_NAME, query);
    }

    /**
     * 根据ID查询
     */
    public EExpressTemplateEntry getEntryById(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_EXPRESS_TEMPLATE_NAME, query, Constant.FIELDS);
        return new EExpressTemplateEntry((BasicDBObject) dbo);
    }


    /**
     * 新增
     */
    public ObjectId add(EExpressTemplateEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_EXPRESS_TEMPLATE_NAME, entry.getBaseEntry());
        return entry.getID();
    }

    /**
     * 更新
     */
    public ObjectId update(EExpressTemplateEntry entry) {
        DBObject query = new BasicDBObject(Constant.ID, entry.getID());
        DBObject update = new BasicDBObject().append("nm", entry.getName()).append("dts", MongoUtils.convert(MongoUtils.fetchDBObjectList(entry.getDetails())));
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, update);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXPRESS_TEMPLATE_NAME, query, updateValue);
        return entry.getID();
    }

    /**
     * 删除
     */
    public void delete(ObjectId id) {
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_EXPRESS_TEMPLATE_NAME, new BasicDBObject(Constant.ID, id));
    }

    /**
     * 查询全部(不分页)
     */
    public List<EExpressTemplateEntry> getEntryList() {
        List<EExpressTemplateEntry> retList = new ArrayList<EExpressTemplateEntry>();
        List<DBObject> dbos = find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXPRESS_TEMPLATE_NAME, Constant.QUERY, Constant.FIELDS, Constant.MONGO_SORTBY_ASC);
        if (null != dbos && dbos.size() > 0) {
            for (DBObject dbo : dbos) {
                retList.add(new EExpressTemplateEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }
}
