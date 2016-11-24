package com.db.ebusiness;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.ebusiness.EGoodsCollection;
import com.pojo.ebusiness.EGoodsHistory;
import com.pojo.ebusiness.EGoodsLogEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fl on 2016/1/28.
 */
public class EGoodsLogDao extends BaseDao {

    /**
     * 新增浏览记录/收藏记录
     */
    public ObjectId add(EGoodsLogEntry eGoodsLogEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_GOODSLOG, eGoodsLogEntry.getBaseEntry());
        return eGoodsLogEntry.getID();
    }

    /**
     * 通过id获取时间
     */
    private String getDate(EGoodsLogEntry entry) {
        Date time = new Date(entry.getID().getTime());
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdFormat.format(time);
        return date;
    }

    /**
     * 查询浏览记录
     *
     * @return 商品id列表
     */
    public List<EGoodsHistory> getLogs(ObjectId userId) {
        BasicDBObject query = new BasicDBObject("uid", userId).append("ty", 1);
        List<DBObject> logs = find(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_GOODSLOG, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        List<EGoodsHistory> historyList = new ArrayList<EGoodsHistory>();

        for (DBObject dbo : logs) {
            EGoodsLogEntry log = new EGoodsLogEntry((BasicDBObject) dbo);
            EGoodsHistory history = new EGoodsHistory();
            history.setId(log.getID().toString());
            history.setDate(getDate(log));
            history.setGoodsId(log.getGoodsId().toString());
            historyList.add(history);
        }
        return historyList;
    }

    /**
     * 查询收藏记录
     *
     * @return 商品id列表
     */
    public List<EGoodsCollection> getCollections(ObjectId userId) {
        BasicDBObject query = new BasicDBObject("uid", userId).append("ty", 2);
        List<DBObject> logs = find(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_GOODSLOG, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        List<EGoodsCollection> collectionList = new ArrayList<EGoodsCollection>();

        for (DBObject dbo : logs) {
            EGoodsLogEntry log = new EGoodsLogEntry((BasicDBObject) dbo);
            EGoodsCollection collection = new EGoodsCollection(log);
            collectionList.add(collection);
        }
        return collectionList;
    }


    /**
     * 判断是否已加入浏览记录
     */
    public boolean isAddLog(ObjectId userId, ObjectId goodsId, String createTime) {
        BasicDBObject query = new BasicDBObject("uid", userId).append("gid", goodsId).append("ty", 1);
        List<DBObject> log = find(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_GOODSLOG, query, Constant.FIELDS);
        List<String> dateList = new ArrayList<String>();
        for (DBObject dbObject : log) {
            EGoodsLogEntry entry = new EGoodsLogEntry((BasicDBObject) dbObject);
            dateList.add(getDate(entry));
        }
        if (dateList.contains(createTime)) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否已收藏
     */
    public boolean isCollected(ObjectId userId, ObjectId goodsId) {
        BasicDBObject query = new BasicDBObject("uid", userId).append("gid", goodsId).append("ty", 2);
        DBObject log = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_GOODSLOG, query, Constant.FIELDS);
        if (log != null) {
            return true;
        }
        return false;
    }

    /**
     * 根据id删除浏览/收藏记录
     */
    public void delete(ObjectId logId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, logId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_GOODSLOG, query);
    }

}
