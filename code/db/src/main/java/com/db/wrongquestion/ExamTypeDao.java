package com.db.wrongquestion;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.reportCard.ExamTypeEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/10/16.
 */
public class ExamTypeDao extends BaseDao {

    /**
     * 增加
     * @param e
     * @return
     */
    public ObjectId addExamTypeEntry(ExamTypeEntry e)
    {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAM_TYPE, e.getBaseEntry());
        return e.getID();
    }
    /**
     * 根据类型查找
     */
    public List<ExamTypeEntry> getList(){
        BasicDBObject query = new BasicDBObject()
                .append("ir", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_EXAM_TYPE,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_ASC);
        List<ExamTypeEntry> entryList = new ArrayList<ExamTypeEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ExamTypeEntry((BasicDBObject) obj));
            }
        }
        return entryList;

    }

}
