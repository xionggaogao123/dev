package com.db.examscoreanalysis;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.examscoreanalysis.ScoreSummaryEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fl on 2016/8/12.
 */
public class ScoreSummaryDao extends BaseDao {

    /**
     * 新增或更新
     * @param scoreSummaryEntry
     * @return
     */
    public ObjectId saveScoreSummary(ScoreSummaryEntry scoreSummaryEntry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORE_SUMMARY, scoreSummaryEntry.getBaseEntry());
        return scoreSummaryEntry.getID();
    }

    /**
     * 批量插入
     * @param scoreSummaryEntry
     */
    public void insertScoreSummaries(List<ScoreSummaryEntry> scoreSummaryEntry){
        List<DBObject> dbObjects = MongoUtils.fetchDBObjectList(scoreSummaryEntry);
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORE_SUMMARY, dbObjects);
    }

    /**
     * 列表
     * @param examId 考试id
     * @param subjectId null表示所有学科  否则为具体学科
     * @param type  -1 全部  1 班级层面  2 年级层面
     * @param fields
     * @return
     */
    public List<ScoreSummaryEntry> getScoreSummaryByExamIdSubjectId(ObjectId examId, ObjectId subjectId, int type, DBObject fields){
        BasicDBObject query = new BasicDBObject("exid", examId);
        if(subjectId != null){
            query.append("subid", subjectId);
        }
        if(type > 0){
            query.append("ty", type);
        }
        return getList(query, fields);
    }



    private List<ScoreSummaryEntry> getList(DBObject query, DBObject fields){
        List<ScoreSummaryEntry> scoreSummaryEntries = new ArrayList<ScoreSummaryEntry>();
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORE_SUMMARY, query, fields);
        if(list != null && !list.isEmpty()){
            for(DBObject object : list){
                scoreSummaryEntries.add(new ScoreSummaryEntry((BasicDBObject)object));
            }
        }
        return scoreSummaryEntries;
    }

    public void removeByExamId(ObjectId examId){
        DBObject query = new BasicDBObject("exid", examId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORE_SUMMARY, query);
    }



}
