package com.db.questionbook;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.questionbook.QuestionWebTestEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/12/12.
 */
public class QuestionWebTestDao extends BaseDao {

    //添加标签
    public ObjectId addEntry(QuestionWebTestEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_QUESTION_WEB_TEST, entry.getBaseEntry());
        return entry.getID();
    }

    //查询
    public QuestionWebTestEntry getEntryById(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        query.append("isr",Constant.ZERO);
        DBObject obj =
                findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_QUESTION_WEB_TEST, query, Constant.FIELDS);
        if (obj != null) {
            return new QuestionWebTestEntry((BasicDBObject) obj);
        }
        return null;
    }
    public void updEntry(QuestionWebTestEntry e) {
        BasicDBObject query=new BasicDBObject(Constant.ID,e.getID());
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,e.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_QUESTION_WEB_TEST, query,updateValue);
    }

    //多条件组合查询列表
    public List<QuestionWebTestEntry> getQuestionList(ObjectId userId,int page,int pageSize) {
        BasicDBObject query = new BasicDBObject()
                .append("uid", userId)
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_QUESTION_WEB_TEST,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC,(page - 1) * pageSize, pageSize);
        List<QuestionWebTestEntry> entryList = new ArrayList<QuestionWebTestEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new QuestionWebTestEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    /**
     * 符合搜索条件的对象个数
     * @return
     */
    public int getReviewListCount(ObjectId userId) {
        BasicDBObject query = new BasicDBObject()
                .append("uid", userId)
                .append("isr", 0); // 未删除
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_QUESTION_WEB_TEST,
                        query);
        return count;
    }

    public int getNameCount(String name) {
        BasicDBObject query = new BasicDBObject()
                .append("isr", 0); // 未删除
        if(name != null && !name.equals("")){
            query.append("tit", MongoUtils.buildRegex(name));
        }
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_QUESTION_WEB_TEST,
                        query);
        return count;
    }
}
