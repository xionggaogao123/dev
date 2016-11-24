package com.db.examresult;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.examregional.ExamSummaryEntry;
import com.sys.constants.Constant;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fl on 2015/10/27.
 */
public class ExamSummaryDao extends BaseDao {
    public List<ExamSummaryEntry> findExamSummaryList(ObjectId jointExamId) {
        DBObject query = new BasicDBObject("aid", jointExamId);
        List<DBObject> dbList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAMSUMMARY, query, Constant.FIELDS);
        if (dbList != null) {
            List<ExamSummaryEntry> list = new ArrayList<ExamSummaryEntry>();
            for (DBObject dbo : dbList) {
                list.add(new ExamSummaryEntry((BasicDBObject) dbo));
            }
            return list;
        }
        return null;
    }

    public void update(ExamSummaryEntry examSummaryEntry) {
        DBObject query = new BasicDBObject(Constant.ID, examSummaryEntry.getID());
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, examSummaryEntry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAMSUMMARY, query, updateValue);
    }
}
