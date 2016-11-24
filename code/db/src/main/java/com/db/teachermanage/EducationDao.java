package com.db.teachermanage;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.teachermanage.EducationEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang_xinxin on 2016/3/7.
 */
public class EducationDao extends BaseDao {
    /**
     * 增加学历信息
     *
     * @param e
     * @return
     */
    public ObjectId addEducationEntry(EducationEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_EDUCATION, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 删除学历信息
     *
     * @param userId
     */
    public void removeEducationEntry(ObjectId userId) {
        DBObject query = new BasicDBObject("ui", userId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_EDUCATION, query);
    }

    /**
     * 获取学历信息
     *
     * @param userId
     * @param fields
     * @return
     */
    public List<EducationEntry> getEducationList(ObjectId userId, DBObject fields) {
        BasicDBObject query = new BasicDBObject("ui", userId);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_EDUCATION, query, fields);
        List<EducationEntry> educationEntryList = new ArrayList<EducationEntry>();
        for (DBObject dbObject : list) {
            EducationEntry resumeEntry = new EducationEntry((BasicDBObject) dbObject);
            educationEntryList.add(resumeEntry);
        }
        return educationEntryList;
    }
}
