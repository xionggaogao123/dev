package com.db.reportCard;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.reportCard.RecordLevelEvaluateEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/9/30.
 */
public class RecordLevelEvaluateDao extends BaseDao{

    public void  saveRecordLevelEvaluate(RecordLevelEvaluateEntry evaluateEntry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_RECORD_LEVEL_EVALUATE,evaluateEntry.getBaseEntry());
    }

    public RecordLevelEvaluateEntry getRecordLevelEvaluateEntry(ObjectId groupExamDetailId){
        BasicDBObject query=new BasicDBObject()
                .append("eid",groupExamDetailId);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_RECORD_LEVEL_EVALUATE,query,Constant.FIELDS);
        if(null!=dbObject){
            return new RecordLevelEvaluateEntry(dbObject);
        }else {
            return null;
        }
    }
}
