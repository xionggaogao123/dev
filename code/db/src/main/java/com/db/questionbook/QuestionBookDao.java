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
    //单个年级（根据type）
    public QuestionBookEntry getEntryById(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        query.append("isr",Constant.ZERO);
        DBObject obj =
                findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_QUESTION_BOOK, query, Constant.FIELDS);
        if (obj != null) {
            return new QuestionBookEntry((BasicDBObject) obj);
        }
        return null;
    }
    //添加错题
    public ObjectId addEntry(QuestionBookEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_QUESTION_BOOK, entry.getBaseEntry());
        return entry.getID();
    }
    //修改错题内容和图片
    public void updateEntry(QuestionBookEntry e){
        BasicDBObject query=new BasicDBObject(Constant.ID,e.getID());
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("dec",e.getDescription()));
        updateValue.append(Constant.MONGO_SET,new BasicDBObject("img",e.getImageList()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_QUESTION_BOOK, query, updateValue);
    }
    public void updateEntry(ObjectId id,String url){
        BasicDBObject query=new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("img",url));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_QUESTION_BOOK, query, updateValue);
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
            query.append("gid",new ObjectId(gradeId));
        }
        if(subjectId != null && !subjectId.equals("")){
            query.append("sid",new ObjectId(subjectId));
        }
        if(questionTypeId != null && !questionTypeId.equals("")){
            query.append("qid",new ObjectId(questionTypeId));
        }
        if(testId != null && !testId.equals("")){
            query.append("tid",new ObjectId(testId));
        }
        if(keyword != null && !keyword.equals("")){
            query.append("dec", MongoUtils.buildRegex(keyword));
        }
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_QUESTION_BOOK,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC,(page - 1) * pageSize, pageSize);
        List<QuestionBookEntry> entryList = new ArrayList<QuestionBookEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new QuestionBookEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
    //多条件组合查询列表
    public List<QuestionBookEntry> getAllQuestionList(String gradeId,List<ObjectId> subjectIds,String questionTypeId,String testId,int type,int page,int pageSize,String keyword,ObjectId userId) {
        BasicDBObject query = new BasicDBObject()
                .append("typ", type)
                .append("uid",userId)
                .append("isr", 0); // 未删除
        if(gradeId != null && !gradeId.equals("")){
            query.append("gid",new ObjectId(gradeId));
        }
        query.append("sid",new BasicDBObject(Constant.MONGO_IN,subjectIds));
        if(questionTypeId != null && !questionTypeId.equals("")){
            query.append("qid",new ObjectId(questionTypeId));
        }
        if(testId != null && !testId.equals("")){
            query.append("tid",new ObjectId(testId));
        }
        if(keyword != null && !keyword.equals("")){
            query.append("dec", MongoUtils.buildRegex(keyword));
        }
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_QUESTION_BOOK,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC,(page - 1) * pageSize, pageSize);
        List<QuestionBookEntry> entryList = new ArrayList<QuestionBookEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new QuestionBookEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    public List<QuestionBookEntry> getAllQuestionList(ObjectId userId) {
        BasicDBObject query = new BasicDBObject()
                .append("uid",userId)
                .append("typ",1)
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
    //查询所有已提交的数量
    public int getQuestionListCount(String gradeId,String subjectId,String questionTypeId,String testId,int type,String keyword,ObjectId userId) {
        BasicDBObject query = new BasicDBObject()
                .append("typ", type)
                .append("uid",userId)
                .append("isr", 0); // 未删除
        if(gradeId != null && !gradeId.equals("")){
            query.append("gid",new ObjectId(gradeId));
        }
        if(subjectId != null && !subjectId.equals("")){
            query.append("sid",new ObjectId(subjectId));
        }
        if(questionTypeId != null && !questionTypeId.equals("")){
            query.append("qid",new ObjectId(questionTypeId));
        }
        if(testId != null && !testId.equals("")){
            query.append("tid",new ObjectId(testId));
        }
        if(keyword != null && !keyword.equals("")){
            query.append("dec", MongoUtils.buildRegex(keyword));
        }
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_QUESTION_BOOK,
                        query);
        return count;
    }

    //查询所有数量
    public int getAllQuestionListCount(ObjectId userId) {
        BasicDBObject query = new BasicDBObject()
                .append("uid", userId)
                .append("typ",1)
                .append("isr", 0); // 未删除
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_QUESTION_BOOK,
                        query);
        return count;
    }
//new BasicDBObject(Constant.MONGO_GT, 0))
    public List<QuestionBookEntry> getReviewList(ObjectId userId,long time) {
        BasicDBObject query = new BasicDBObject()
                .append("uid", userId)
                .append("ptm", new BasicDBObject(Constant.MONGO_LT, time))
                .append("typ",1)
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
    /**
     * 符合搜索条件的对象个数
     * @return
     */
    public int getReviewListCount(ObjectId userId,long time) {
        BasicDBObject query = new BasicDBObject()
                .append("uid", userId)
                .append("ptm", new BasicDBObject(Constant.MONGO_LT, time))
                .append("typ",1)
                .append("isr", 0); // 未删除
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_QUESTION_BOOK,
                        query);
        return count;
    }


    //修改复习题
    public void updateQuestionBook(ObjectId id,long time,int level){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject update = new BasicDBObject("ptm",time);
        update.append("lev",level);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,update);
        //updateValue.append(Constant.MONGO_SET,new BasicDBObject("lev",level));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_QUESTION_BOOK, query, updateValue);
    }

    //已学会
    public void changeQuestionBook(ObjectId id,long time){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("typ",2).append("dtm",time));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_QUESTION_BOOK, query, updateValue);
    }
    //未学会
    public void changeUnQuestionBook(ObjectId id,long time,long current){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject update = new BasicDBObject("typ",1);
        update.append("lev",1);
        update.append("ptm",time);
        update.append("dtm",current);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,update);
        //updateValue.append(Constant.MONGO_SET,new BasicDBObject("lev",1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_QUESTION_BOOK, query, updateValue);
    }
}
