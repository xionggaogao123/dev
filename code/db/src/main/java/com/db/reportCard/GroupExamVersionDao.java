package com.db.reportCard;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.reportCard.GroupExamVersionEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/10/18.
 */
public class GroupExamVersionDao extends BaseDao{

    public void saveGroupExamVersionEntry(GroupExamVersionEntry examVersionEntry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_EXAM_VERSION,examVersionEntry.getBaseEntry());
    }

    public void updateVersionByGroupExamDetailId(ObjectId groupExamDetailId,
                                                 long version){
        BasicDBObject query=new BasicDBObject()
                .append("eid",groupExamDetailId);
        BasicDBObject updateValue=new BasicDBObject()
                .append(Constant.MONGO_SET,new BasicDBObject("v",version));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_EXAM_VERSION,query,updateValue);
    }

    public void increaseVersion(ObjectId groupExamDetailId){
        BasicDBObject query=new BasicDBObject()
                .append("eid",groupExamDetailId);
        BasicDBObject updateValue=new BasicDBObject()
                .append(Constant.MONGO_INC,new BasicDBObject("v",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_EXAM_VERSION,query,updateValue);
    }

    public GroupExamVersionEntry getVersionByGroupExamDetailId(ObjectId groupExamDetailId){
        BasicDBObject query=new BasicDBObject()
                .append("eid",groupExamDetailId);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_EXAM_VERSION,query,Constant.FIELDS);
        if(null!=dbObject){
            return new GroupExamVersionEntry(dbObject);
        }else {
            return null;
        }
    }
}
