package com.db.ebusiness;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.ebusiness.EGoodsCommentEntry;
import com.sys.constants.Constant;


/**
 * 电子商务商品评论
 *
 * @author fourer
 */
public class EGoodsCommentDao extends BaseDao {

    /**
     * 添加
     *
     * @param e
     * @return
     */
    public ObjectId addEGoodsCommentEntry(EGoodsCommentEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_GOODS_COMMENT, e.getBaseEntry());
        return e.getID();
    }


    /**
     * 查询
     *
     * @param ui      可以为null
     * @param goodsId
     * @param skip
     * @param limit
     * @return
     */
    public List<EGoodsCommentEntry> getEGoodsCommentEntrys(ObjectId ui, ObjectId goodsId, int onlyImg, int skip, int limit) {
        BasicDBObject query = new BasicDBObject("egi", goodsId);
        if (null != ui) {
            query.append("ui", ui);
        }
        if (1 == onlyImg) {
            query.append("ims", new BasicDBObject("$not", new BasicDBObject("$size", 0)));
        }
        List<EGoodsCommentEntry> retList = new ArrayList<EGoodsCommentEntry>(limit);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_GOODS_COMMENT, query, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, skip, limit);
        if (dbObjectList != null && dbObjectList.size() > 0) {
            for (DBObject dbo : dbObjectList) {
                EGoodsCommentEntry userEntry = new EGoodsCommentEntry((BasicDBObject) dbo);
                retList.add(userEntry);
            }
        }

        return retList;
    }

    /**
     * 评论数量
     *
     * @param userId
     * @param goodsId
     * @return
     */
    public int getCommentsCount(ObjectId userId, ObjectId goodsId) {
        BasicDBObject query = new BasicDBObject("egi", goodsId);
        if (null != userId) {
            query.append("ui", userId);
        }
        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_GOODS_COMMENT, query);
        return count;
    }
}
