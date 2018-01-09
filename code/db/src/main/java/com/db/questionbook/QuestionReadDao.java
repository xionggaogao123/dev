package com.db.questionbook;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.questionbook.QuestionReadEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018/1/8.
 */
public class QuestionReadDao extends BaseDao {

    //添加记录
    public ObjectId addEntry(QuestionReadEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_READ_BOOK, entry.getBaseEntry());
        return entry.getID();
    }
    //修改记录数
    public void delEntry(ObjectId id){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("unr",Constant.ZERO));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_READ_BOOK, query,updateValue);
    }

    //批量查询
    public List<QuestionReadEntry> getReviewList(List<ObjectId> userIds,ObjectId parentId,int type) {
        BasicDBObject query = new BasicDBObject()
                .append("pid",parentId)
                .append("uid",new BasicDBObject(Constant.MONGO_IN,userIds))
                .append("typ",type)
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_READ_BOOK,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<QuestionReadEntry> entryList = new ArrayList<QuestionReadEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new QuestionReadEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
}
