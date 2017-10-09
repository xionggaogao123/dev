package com.db.reportCard;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.reportCard.GroupExamUserRecordEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
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

    public GroupExamUserRecordEntry getGroupExamUserRecordEntry(ObjectId id){
        BasicDBObject query=new BasicDBObject()
                .append(Constant.ID,id);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_EXAM_USER_RECORD, query,Constant.FIELDS);
        if(null!=dbObject){
            return new GroupExamUserRecordEntry(dbObject);
        }else{
            return null;
        }
    }


    /**
     * 查询查询录入成绩的学生名单
     * @param groupExamDetailId
     * @return
     */
    public List<GroupExamUserRecordEntry> getExamUserRecordEntries(ObjectId groupExamDetailId){
        List<Integer> status=new ArrayList<Integer>();
        status.add(Constant.ZERO);
        status.add(Constant.TWO);
        status.add(Constant.THREE);
        BasicDBObject query=new BasicDBObject()
                .append("eid",groupExamDetailId)
                .append("ir", Constant.ZERO);
        List<GroupExamUserRecordEntry> entries=new ArrayList<GroupExamUserRecordEntry>();
        List<DBObject> dbObjects=find(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_EXAM_USER_RECORD,
                query,Constant.FIELDS);
        if(null!=dbObjects&&!dbObjects.isEmpty()){
            for(DBObject dbObject:dbObjects){
                entries.add(new GroupExamUserRecordEntry(dbObject));
            }
        }
        return entries;
    }


    /**
     * 学生接收到的成绩单列表
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    public List<GroupExamUserRecordEntry> getStudentReceivedEntries(ObjectId userId,
                                                             int page,int pageSize){
        List<Integer> status=new ArrayList<Integer>();
        status.add(Constant.ZERO);
        status.add(Constant.TWO);
        status.add(Constant.THREE);
        List<GroupExamUserRecordEntry> entries=new ArrayList<GroupExamUserRecordEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("uid",userId)
                .append("st",new BasicDBObject(Constant.MONGO_IN,status))
                .append("ir", Constant.ZERO);
        List<DBObject> dbObjects=find(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_EXAM_USER_RECORD,
                query,Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjects&&!dbObjects.isEmpty()){
            for(DBObject dbObject:dbObjects){
                entries.add(new GroupExamUserRecordEntry(dbObject));
            }
        }
        return entries;
    }

    /**
     * 家长接收到的成绩单列表
     * @param mainUserId
     * @param userIds
     * @param page
     * @param pageSize
     * @return
     */
    public List<GroupExamUserRecordEntry> getParentReceivedEntries(ObjectId mainUserId,
                                                               List<ObjectId> userIds,
                                                               int page,int pageSize){
        List<Integer> status=new ArrayList<Integer>();
        status.add(Constant.ZERO);
        status.add(Constant.TWO);
        status.add(Constant.THREE);
        List<GroupExamUserRecordEntry> entries=new ArrayList<GroupExamUserRecordEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("muid",new BasicDBObject(Constant.MONGO_NE,mainUserId))
                .append("uid",new BasicDBObject(Constant.MONGO_IN,userIds))
                .append("st",new BasicDBObject(Constant.MONGO_IN,status))
                .append("ir", Constant.ZERO);
        List<DBObject> dbObjects=find(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_EXAM_USER_RECORD,
                query,Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjects&&!dbObjects.isEmpty()){
            for(DBObject dbObject:dbObjects){
                entries.add(new GroupExamUserRecordEntry(dbObject));
            }
        }
        return entries;
    }


    /**
     * 更新状态
     * @param groupExamDetailId
     */
    public void updateStatus(ObjectId groupExamDetailId){
        BasicDBObject query=new BasicDBObject()
                .append("eid",groupExamDetailId);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("ir", Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_GROUP_EXAM_DETAIL,query,updateValue);
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
     *
     * @param groupExamDetailId
     * @param status
     */
    public void updateGroupExamDetailStatus(ObjectId groupExamDetailId,
                                            int status){
        BasicDBObject query=new BasicDBObject()
                .append("eid",groupExamDetailId);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("st",status));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_GROUP_EXAM_DETAIL,query,updateValue);
    }


    public void updateGroupExamUserRecordScore(ObjectId id,
                                               double score,
                                               int scoreLevel,
                                               int rank
                                               ){
        BasicDBObject query=new BasicDBObject()
                .append(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject()
                .append(Constant.MONGO_SET,new BasicDBObject("sc",score).append("scl",scoreLevel).append("rk",rank));
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
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("st",Constant.THREE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_GROUP_EXAM_DETAIL,query,updateValue);
    }

    public GroupExamUserRecordEntry getUserRecordEntry(ObjectId groupExamDetailId,
                                             ObjectId userId){
        BasicDBObject query=new BasicDBObject()
                .append("eid",groupExamDetailId)
                .append("st",Constant.THREE)
                .append("uid",userId);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_GROUP_EXAM_DETAIL,query,Constant.FIELDS);
        if(null!=dbObject){
            return new GroupExamUserRecordEntry(dbObject);
        }else{
            return null;
        }
    }
}
