package com.db.questionbook;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.questionbook.QuestionTagsEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/12/8.
 */
public class QuestionTagsDao extends BaseDao {

    //添加标签
    public ObjectId addEntry(QuestionTagsEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_QUESTION_TAGS, entry.getBaseEntry());
        return entry.getID();
    }
    //查询
    public QuestionTagsEntry getEntryById(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        query.append("isr",Constant.ZERO);
        DBObject obj =
                findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_QUESTION_TAGS, query, Constant.FIELDS);
        if (obj != null) {
            return new QuestionTagsEntry((BasicDBObject) obj);
        }
        return null;
    }

    public void delEntryById(ObjectId id){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isr",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_QUESTION_TAGS, query,updateValue);
    }

    public List<QuestionTagsEntry> getReviewList(ObjectId userId) {
        BasicDBObject query = new BasicDBObject()
                .append("uid",userId)
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_QUESTION_TAGS,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<QuestionTagsEntry> entryList = new ArrayList<QuestionTagsEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new QuestionTagsEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    //删除
    public void delEntry(ObjectId id){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isr",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_QUESTION_TAGS, query,updateValue);
    }
}
