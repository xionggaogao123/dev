package com.db.wrongquestion;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.wrongquestion.SubjectClassEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/9/6.
 */
public class SubjectClassDao extends BaseDao {

    /**
     * 增加
     * @param e
     * @return
     */
    public ObjectId addSubjectEntry(SubjectClassEntry e)
    {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_SUBJECT_CLASS, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 查询所有的科目列表
     */
    public List<SubjectClassEntry> getList(){
        BasicDBObject query = new BasicDBObject()
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_SUBJECT_CLASS,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_ASC);
        List<SubjectClassEntry> entryList = new ArrayList<SubjectClassEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new SubjectClassEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
}
