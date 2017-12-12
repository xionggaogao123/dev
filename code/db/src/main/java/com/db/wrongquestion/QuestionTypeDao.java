package com.db.wrongquestion;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.wrongquestion.QuestionTypeEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/10/12.
 */
public class QuestionTypeDao extends BaseDao {
    /**
     * 增加
     * @param e
     * @return
     */
    public ObjectId addQuestionTypeEntry(QuestionTypeEntry e)
    {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_QUESTION_TYPE, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 根据类型查找
     */
    public List<QuestionTypeEntry> getList(ObjectId id,String ename){
        BasicDBObject query = new BasicDBObject()
                .append("sid",id)
                .append("ena", ename)
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_QUESTION_TYPE,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_ASC);
        List<QuestionTypeEntry> entryList = new ArrayList<QuestionTypeEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new QuestionTypeEntry((BasicDBObject) obj));
            }
        }
        return entryList;

    }

    /**
     * 根据类型查找
     */
    public List<QuestionTypeEntry> getTypeList(){
        BasicDBObject query = new BasicDBObject()
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_QUESTION_TYPE,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_ASC);
        List<QuestionTypeEntry> entryList = new ArrayList<QuestionTypeEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new QuestionTypeEntry((BasicDBObject) obj));
            }
        }
        return entryList;

    }
}
