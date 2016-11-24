package com.db.zouban;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.zouban.XuankeConfEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;


/**
 * 选课配置
 * Created by wang_xinxin on 2015/9/22.
 */
public class XuanKeConfDao extends BaseDao {


    /**
     * 添加走班选课
     *
     * @param xuankeEntry
     * @return
     */
    public ObjectId addXuanKeConf(XuankeConfEntry xuankeEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_XUANKE_CONF, xuankeEntry.getBaseEntry());
        return xuankeEntry.getID();
    }


    /**
     * 走班选课列表
     *
     * @param term
     * @param gradeId
     * @return
     */
    public XuankeConfEntry findXuanKeConf(String term, ObjectId gradeId) {
        BasicDBObject query = new BasicDBObject("te", term).append("gid", gradeId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_XUANKE_CONF, query, null);
        if (null != dbo) {
            return new XuankeConfEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * 通过Id查询走班选课
     *
     * @param xuanKeId
     * @return
     */
    public XuankeConfEntry findXuanKeConfByXuanKeId(ObjectId xuanKeId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, xuanKeId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_XUANKE_CONF, query, null);
        if (null != dbo) {
            return new XuankeConfEntry((BasicDBObject) dbo);
        }
        return null;
    }



    /**
     * 发布、取消发布
     *
     * @param id
     * @param isRelease
     */
    public void isRelease(ObjectId id, int isRelease) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject value = new BasicDBObject("isrels", isRelease);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, value);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_XUANKE_CONF, query, updateValue);
    }

    /**
     * 更新选课时间
     *
     * @param xuankeId
     * @param startDate
     * @param endDate
     */
    public void updateXuanKeConf(ObjectId xuankeId, long startDate, long endDate) {
        DBObject query = new BasicDBObject(Constant.ID, xuankeId);
        DBObject value = new BasicDBObject()
                .append("stadt", startDate)
                .append("eddt", endDate);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, value);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_XUANKE_CONF, query, updateValue);
    }


    /**
     * 更新选课说明
     * @param xuankeId
     * @param info
     */
    public void updateXuanKeInfo(ObjectId xuankeId, String info) {
        DBObject query = new BasicDBObject(Constant.ID, xuankeId);
        DBObject value = new BasicDBObject("info", info);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, value);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_XUANKE_CONF, query, updateValue);
    }



}
