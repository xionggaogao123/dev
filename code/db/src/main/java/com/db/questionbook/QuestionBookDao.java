package com.db.questionbook;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.questionbook.QuestionBookEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/9/30.
 */
public class QuestionBookDao extends BaseDao{
    //添加错题
    public ObjectId addEntry(QuestionBookEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_QUESTION_BOOK, entry.getBaseEntry());
        return entry.getID();
    }

    //删除错题
    public void delEntry(ObjectId id){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isr",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_QUESTION_BOOK, query,updateValue);
    }
    //多条件组合查询列表
    public List<QuestionBookEntry> getQuestionList(String gradeId,String subjectId,String questionTypeId,String testId,int type,int page,int pageSize,String keyword,ObjectId userId) {
        BasicDBObject query = new BasicDBObject()
                .append("typ", type)
                .append("uid",userId)
                .append("isr", 0); // 未删除
        if(gradeId != null && !gradeId.equals("")){
            query.append("gid",gradeId);
        }
        if(subjectId != null && !subjectId.equals("")){
            query.append("sid",subjectId);
        }
        if(questionTypeId != null && !questionTypeId.equals("")){
            query.append("qid",questionTypeId);
        }
        if(testId != null && !testId.equals("")){
            query.append("tid",testId);
        }
        if(keyword != null && !keyword.equals("")){
            query.append("dec", MongoUtils.buildRegex(keyword));
        }
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_QUESTION_BOOK,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC, page, pageSize);
        List<QuestionBookEntry> entryList = new ArrayList<QuestionBookEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new QuestionBookEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
//new BasicDBObject(Constant.MONGO_GT, 0))
    public List<QuestionBookEntry> getReviewList(ObjectId userId,long time) {
        BasicDBObject query = new BasicDBObject()
                .append("uid", userId)
                .append("ptm", new BasicDBObject(Constant.MONGO_LT, time))
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_QUESTION_BOOK,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<QuestionBookEntry> entryList = new ArrayList<QuestionBookEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new QuestionBookEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    //修改复习题
    public void updateQuestionBook(ObjectId id,long time,int level){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("ptm",time));
        updateValue.append(Constant.MONGO_SET,new BasicDBObject("lev",level));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_QUESTION_BOOK, query, updateValue);
    }

    //已学会
    public void changeQuestionBook(ObjectId id){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("typ",2));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_QUESTION_BOOK, query, updateValue);
    }
}
