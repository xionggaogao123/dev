package com.db.teachermanage;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.teachermanage.EducationEntry;
import com.pojo.teachermanage.PartTimeEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang_xinxin on 2016/3/7.
 */
public class PartTimeDao extends BaseDao {
    /**
     * 增加社会兼职信息
     *
     * @param e
     * @return
     */
    public ObjectId addPartTimeEntry(PartTimeEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_PARTTIME, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 删除社会兼职信息
     *
     * @param userId
     */
    public void removePartTimeEntry(ObjectId userId) {
        DBObject query = new BasicDBObject("ui", userId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_PARTTIME, query);
    }

    /**
     * 获取社会兼职信息
     *
     * @param userId
     * @param fields
     * @return
     */
    public List<PartTimeEntry> getPartTimeList(ObjectId userId, DBObject fields) {
        BasicDBObject query = new BasicDBObject("ui", userId);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_PARTTIME, query, fields);
        List<PartTimeEntry> partTimeEntryList = new ArrayList<PartTimeEntry>();
        for (DBObject dbObject : list) {
            PartTimeEntry partTimeEntry = new PartTimeEntry((BasicDBObject) dbObject);
            partTimeEntryList.add(partTimeEntry);
        }
        return partTimeEntryList;
    }
}
