package com.db.teachermanage;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.teachermanage.ResumeEntry;
import com.pojo.teachermanage.TitleEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang_xinxin on 2016/3/7.
 */
public class TitleDao extends BaseDao {
    /**
     * 增加教师职称信息
     *
     * @param e
     * @return
     */
    public ObjectId addTitleEntry(TitleEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_TITLE, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 删除教师职称信息
     *
     * @param userId
     */
    public void removeTitleEntry(ObjectId userId) {
        DBObject query = new BasicDBObject("ui", userId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_TITLE, query);
    }

    /**
     * 获取教师职称信息
     *
     * @param userId
     * @param fields
     * @return
     */
    public List<TitleEntry> getTitleList(ObjectId userId, DBObject fields) {
        BasicDBObject query = new BasicDBObject("ui", userId);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_TITLE, query, fields);
        List<TitleEntry> titleEntryList = new ArrayList<TitleEntry>();
        for (DBObject dbObject : list) {
            TitleEntry resumeEntry = new TitleEntry((BasicDBObject) dbObject);
            titleEntryList.add(resumeEntry);
        }
        return titleEntryList;
    }
}
