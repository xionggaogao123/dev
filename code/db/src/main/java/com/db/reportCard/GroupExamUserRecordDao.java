package com.db.reportCard;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.pojo.reportCard.GroupExamUserRecordEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by scott on 2017/9/30.
 */
public class GroupExamUserRecordDao extends BaseDao{

    public void saveGroupExamUserRecord(GroupExamUserRecordEntry examUserRecordEntry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_EXAM_USER_RECORD,examUserRecordEntry.getBaseEntry());
    }

    public void saveEntries(List<GroupExamUserRecordEntry> entryList){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_EXAM_USER_RECORD, MongoUtils.fetchDBObjectList(entryList));
    }

    /**
     * 更新状态
     * @param communityId
     * @param userId
     * @param status
     */
    public void updateGroupExamDetailUserRecord(ObjectId communityId,
                                      ObjectId userId,
                                      int status){
        BasicDBObject query=new BasicDBObject()
                .append("cmId",communityId)
                .append("uid",userId);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("st",status));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_GROUP_EXAM_DETAIL,query,updateValue);
    }

    /**
     * 阅读该条信息
     * @param groupExamDetailId
     * @param userId
     */
    public void pushSign(ObjectId groupExamDetailId,
                         ObjectId userId){
        BasicDBObject query=new BasicDBObject()
                .append("eid",groupExamDetailId)
                .append("uid",userId);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("st",Constant.TWO));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_GROUP_EXAM_DETAIL,query,updateValue);
    }
}
