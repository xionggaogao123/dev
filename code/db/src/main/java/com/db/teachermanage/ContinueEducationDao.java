package com.db.teachermanage;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.teachermanage.ContinueEducationEntry;
import com.pojo.teachermanage.EducationEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang_xinxin on 2016/3/7.
 */
public class ContinueEducationDao extends BaseDao {
    /**
     * 增加继续教育信息
     *
     * @param e
     * @return
     */
    public ObjectId addContinueEducationEntry(ContinueEducationEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTINUEEDUCATION, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 删除继续教育信息
     *
     * @param userId
     */
    public void removeContinueEducationEntry(ObjectId userId) {
        DBObject query = new BasicDBObject("ui", userId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTINUEEDUCATION, query);
    }

    /**
     * 获取继续教育信息
     *
     * @param userId
     * @param fields
     * @return
     */
    public List<ContinueEducationEntry> getContinueEducationList(ObjectId userId, DBObject fields) {
        BasicDBObject query = new BasicDBObject("ui", userId);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTINUEEDUCATION, query, fields);
        List<ContinueEducationEntry> continueEducationEntryList = new ArrayList<ContinueEducationEntry>();
        for (DBObject dbObject : list) {
            ContinueEducationEntry continueEducationEntry = new ContinueEducationEntry((BasicDBObject) dbObject);
            continueEducationEntryList.add(continueEducationEntry);
        }
        return continueEducationEntryList;
    }

    /**
     * 获取继续教育信息
     *
     * @param projectId
     * @param fields
     * @return
     */
    public List<ContinueEducationEntry> getContinueEducationListById(String projectId, int type, DBObject fields) {
        BasicDBObject query = new BasicDBObject("course", projectId);
        DBObject db = new BasicDBObject("record", -1);
        if (type == 1) {
            db = new BasicDBObject("record", 1);
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTINUEEDUCATION, query, fields, db);
        List<ContinueEducationEntry> continueEducationEntryList = new ArrayList<ContinueEducationEntry>();
        for (DBObject dbObject : list) {
            ContinueEducationEntry continueEducationEntry = new ContinueEducationEntry((BasicDBObject) dbObject);
            continueEducationEntryList.add(continueEducationEntry);
        }
        return continueEducationEntryList;
    }
}
