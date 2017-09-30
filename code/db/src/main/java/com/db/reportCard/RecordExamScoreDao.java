package com.db.reportCard;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.reportCard.RecordExamScoreEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/9/30.
 */
public class RecordExamScoreDao extends BaseDao{

    /**
     * 保存一条信息
     * @param examScoreEntry
     */
    public void saveRecordExamScore(RecordExamScoreEntry examScoreEntry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_RECORD_EXAM_SCORE,examScoreEntry.getBaseEntry());
    }


    /**
     * 保存列表信息
     * @param entryList
     */
    public void saveEntries(List<RecordExamScoreEntry> entryList){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_RECORD_EXAM_SCORE, MongoUtils.fetchDBObjectList(entryList));
    }

    /**
     * 编辑时用到的这次考试的所有学生成绩
     * @param groupExamDetailId
     * @return
     */
    public List<RecordExamScoreEntry> getRecordEntries(ObjectId groupExamDetailId){
        List<RecordExamScoreEntry> entries=new ArrayList<RecordExamScoreEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("eid",groupExamDetailId);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_RECORD_EXAM_SCORE,query);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new RecordExamScoreEntry(dbObject));
            }
        }
        return entries;
    }

    /**
     * 保存前，把前面的成绩删除掉
     * @param groupExamDetailId
     */
    public void removeAllRecordExamScore(ObjectId groupExamDetailId){
        BasicDBObject query=new BasicDBObject()
                .append("eid",groupExamDetailId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_RECORD_EXAM_SCORE,query);
    }

    /**
     * 获取某人在这次考试中所得到的的成绩
     * @param groupExamDetailId
     * @param userId
     * @return
     */
    public RecordExamScoreEntry getEntry(ObjectId groupExamDetailId,
                                         ObjectId userId){
        BasicDBObject query=new BasicDBObject()
                .append("eid",groupExamDetailId)
                .append("uid",userId);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_RECORD_EXAM_SCORE,query,Constant.FIELDS);
        if(null!=dbObject){
            return new RecordExamScoreEntry(dbObject);
        }else{
            return null;
        }
    }
}
