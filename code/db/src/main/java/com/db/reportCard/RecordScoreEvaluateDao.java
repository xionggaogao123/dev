package com.db.reportCard;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.reportCard.RecordScoreEvaluateEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/9/30.
 */
public class RecordScoreEvaluateDao  extends BaseDao{

    public void saveRecordScoreEvaluateEntry(RecordScoreEvaluateEntry evaluateEntry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_RECORD_SCORE_EVALUATE,evaluateEntry.getBaseEntry());
    }

    public RecordScoreEvaluateEntry getEntryById(ObjectId groupExamDetailId){
        BasicDBObject query=new BasicDBObject()
                .append("eid",groupExamDetailId);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_RECORD_SCORE_EVALUATE,query,Constant.FIELDS);
        if(null!=dbObject){
            return new RecordScoreEvaluateEntry(dbObject);
        }else {
            return null;
        }
    }
}
