package com.db.reportCard;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.reportCard.GroupExamDetailEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/9/30.
 */
public class GroupExamDetailDao extends BaseDao{

    public void saveGroupExamDetailEntry(GroupExamDetailEntry examDetailEntry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_GROUP_EXAM_DETAIL,examDetailEntry.getBaseEntry());
    }


    /**
     * 获取我接收的成绩单列表
     * @param userId
     * @param communityIds
     * @param userIds
     * @param page
     * @param pageSize
     * @return
     */
    public List<GroupExamDetailEntry> getMyReceivedGroupExamDetailEntries(
            ObjectId userId,
            List<ObjectId> communityIds,
            List<ObjectId> userIds,
            int page,int pageSize){
        List<GroupExamDetailEntry> entries=new ArrayList<GroupExamDetailEntry>();
        List<Integer> status=new ArrayList<Integer>();
        status.add(Constant.ZERO);
        status.add(Constant.TWO);
        BasicDBObject query=new BasicDBObject()
                .append("uid",new BasicDBObject(Constant.MONGO_NE,userId))
                .append("cmId",new BasicDBObject(Constant.MONGO_IN,communityIds))
                .append("urs.uid",new BasicDBObject(Constant.MONGO_IN,userIds))
                .append("urs.st",new BasicDBObject(Constant.MONGO_IN,status))
                .append("ir",Constant.ZERO);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_GROUP_EXAM_DETAIL,
                query,Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new GroupExamDetailEntry(dbObject));
            }
        }
        return entries;
    }

    /**
     * 获取我发送的成绩单列表
     * @param userId
     * @return
     */
    public List<GroupExamDetailEntry> getMySendGroupExamDetailEntries(ObjectId userId,
                                                                      int page,int pageSize){
        List<GroupExamDetailEntry> entries=new ArrayList<GroupExamDetailEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("uid",userId)
                .append("ir", Constant.ZERO);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_GROUP_EXAM_DETAIL,
                query,Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new GroupExamDetailEntry(dbObject));
            }
        }
        return entries;
    }

    /**
     * 阅读该条信息
     * @param id
     * @param userId
     */
    public void pushSign(ObjectId id,
                         ObjectId userId){
        BasicDBObject query=new BasicDBObject()
                .append(Constant.ID,id)
                .append("urs.uid",userId);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("urs.$.st",Constant.TWO));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_GROUP_EXAM_DETAIL,query,updateValue);
    }

    /**
     * 更新状态
     * @param communityId
     * @param userId
     * @param status
     */
    public void updateGroupExamDetail(ObjectId communityId,
                                      ObjectId userId,
                                      int status){
        BasicDBObject query=new BasicDBObject()
                .append("cmId",communityId)
                .append("urs.uid",userId);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("urs.$.st",status));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_GROUP_EXAM_DETAIL,query,updateValue);
    }

    /**
     * 更新状态
     * @param id
     * @param status
     */
    public void updateGroupExamDetailEntry(ObjectId id,int status){
        BasicDBObject query=new BasicDBObject()
                .append(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject()
                .append(Constant.MONGO_SET,new BasicDBObject("st",status));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_GROUP_EXAM_DETAIL,query,updateValue);
    }

    /**
     * 查询信息
     * @param id
     * @return
     */
    public GroupExamDetailEntry getEntryById(ObjectId id){
        BasicDBObject query=new BasicDBObject()
                .append(Constant.ID,id);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_GROUP_EXAM_DETAIL,query,Constant.FIELDS);
        if(null!=dbObject){
            return new GroupExamDetailEntry(dbObject);
        }else {
            return null;
        }
    }
}
