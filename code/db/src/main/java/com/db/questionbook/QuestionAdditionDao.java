package com.db.questionbook;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.questionbook.QuestionAdditionEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/9/30.
 */
public class QuestionAdditionDao extends BaseDao {
    //添加解析
     public ObjectId addEntry(QuestionAdditionEntry entry){
         save(MongoFacroty.getAppDB(), Constant.COLLECTION_QUESTION_ADDITION, entry.getBaseEntry());
         return entry.getID();
     }
    public QuestionAdditionEntry getEntry(ObjectId parentId,int type,int level) {
        BasicDBObject query = new BasicDBObject();
        query.append("isr",Constant.ZERO);
        query.append("pid",parentId);
        query.append("aty",type);
        query.append("lev",level);
        DBObject obj =
                findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_QUESTION_ADDITION, query, Constant.FIELDS);
        if (obj != null) {
            return new QuestionAdditionEntry((BasicDBObject) obj);
        }
        return null;
    }
    //修改解析内容和图片
    public void updateEntry(QuestionAdditionEntry e){
        BasicDBObject query=new BasicDBObject(Constant.ID,e.getID());
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("con",e.getContent()));
        updateValue.append(Constant.MONGO_SET,new BasicDBObject("anl",e.getAnswerList()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_QUESTION_ADDITION, query, updateValue);
    }
    //根据错题查询相关解析
    public List<QuestionAdditionEntry> getListByParentId(ObjectId parentId) {
        BasicDBObject query = new BasicDBObject()
                .append("pid", parentId)
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_QUESTION_ADDITION,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<QuestionAdditionEntry> entryList = new ArrayList<QuestionAdditionEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new QuestionAdditionEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
    public List<QuestionAdditionEntry> getListByParentIdList(List<ObjectId> parentIds) {
        BasicDBObject query = new BasicDBObject()
                .append("pid", new BasicDBObject(Constant.MONGO_IN, parentIds))
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_QUESTION_ADDITION,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<QuestionAdditionEntry> entryList = new ArrayList<QuestionAdditionEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new QuestionAdditionEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
    //删除
    public void delEntry(ObjectId id){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isr",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_QUESTION_ADDITION, query,updateValue);
    }
    //添加解析
    /*public ObjectId addEntry(QuestionBookEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_QUESTION_BOOK, entry.getBaseEntry());
        return entry.getID();
    }*/
}
