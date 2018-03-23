package com.db.reportCard;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.reportCard.GroupExamDetailEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by scott on 2017/9/30.
 */
public class GroupExamDetailDao extends BaseDao{

    public ObjectId saveGroupExamDetailEntry(GroupExamDetailEntry examDetailEntry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_GROUP_EXAM_DETAIL,examDetailEntry.getBaseEntry());
        return examDetailEntry.getID();
    }

    public List<GroupExamDetailEntry> getMappingDatas(int page,int pageSize){
        List<GroupExamDetailEntry> entries=new ArrayList<GroupExamDetailEntry>();
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(),
                Constant.COLLECTION_REPORT_CARD_GROUP_EXAM_DETAIL,new BasicDBObject(),
                Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new GroupExamDetailEntry(dbObject));
            }
        }
        return entries;
    }


    public GroupExamDetailEntry getGroupExamDetailEntry(ObjectId id){
        BasicDBObject query=new BasicDBObject()
                .append(Constant.ID,id);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_GROUP_EXAM_DETAIL,query,Constant.FIELDS);
        if(null!=dbObject){
            return new GroupExamDetailEntry(dbObject);
        }else{
            return null;
        }
    }

    public void updateSignCount(ObjectId groupExamDetailId,int signCount){
        BasicDBObject query=new BasicDBObject(Constant.ID,groupExamDetailId);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("sc",signCount));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_GROUP_EXAM_DETAIL,
                query,updateValue);
    }
    
    public void updateShowType(ObjectId groupExamDetailId,int showType) {
        BasicDBObject query=new BasicDBObject(Constant.ID,groupExamDetailId);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("sw",showType));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_GROUP_EXAM_DETAIL,
                query,updateValue);
    }

    public int countMySendGroupExamDetailEntries(
            ObjectId subjectId,ObjectId examTypeId,int status,
            ObjectId userId){
        BasicDBObject query=new BasicDBObject()
                .append("uid",userId);
        if(null!=subjectId){
            query.append("sid",subjectId);
        }
        if(null!=examTypeId){
            query.append("etp",examTypeId);
        }
        List<Integer> statuses=new ArrayList<Integer>();
        if(status==-1){
            statuses.add(Constant.ZERO);
            statuses.add(Constant.TWO);
        }else{
            statuses.add(status);
        }
        query.append("st",new BasicDBObject(Constant.MONGO_IN,statuses));
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_GROUP_EXAM_DETAIL,
                query);
    }
    /**
     * 获取我发送的成绩单列表
     * @param userId
     * @return
     */
    public List<GroupExamDetailEntry> getMySendGroupExamDetailEntries(
            ObjectId subjectId,ObjectId examTypeId,int status,
            ObjectId userId,
            int page,int pageSize){
        List<GroupExamDetailEntry> entries=new ArrayList<GroupExamDetailEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("uid",userId);
        if(null!=subjectId){
            query.append("sid",subjectId);
        }
        if(null!=examTypeId){
            query.append("etp",examTypeId);
        }
        List<Integer> statuses=new ArrayList<Integer>();
        if(status==-1){
            statuses.add(Constant.ZERO);
            statuses.add(Constant.TWO);
        }else{
            statuses.add(status);
        }
        query.append("st",new BasicDBObject(Constant.MONGO_IN,statuses));
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


    public void updateSignedCount(ObjectId id){
        BasicDBObject query=new BasicDBObject()
                .append(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject()
                .append(Constant.MONGO_INC,new BasicDBObject("sec",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_GROUP_EXAM_DETAIL,query,updateValue);
    }

    /**
     * 删除这次的考次
     * @param id
     */
    public void removeGroupExamDetailEntry(ObjectId id){
        BasicDBObject query=new BasicDBObject()
                .append(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject()
                .append(Constant.MONGO_SET,new BasicDBObject("st", Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_GROUP_EXAM_DETAIL,query,updateValue);
    }


    public Map<ObjectId,GroupExamDetailEntry>  getGroupExamDetailMap(List<ObjectId> groupExamIds){
        Map<ObjectId,GroupExamDetailEntry> entryMap=new HashMap<ObjectId, GroupExamDetailEntry>();
        BasicDBObject query=new BasicDBObject()
                .append(Constant.ID,new BasicDBObject(Constant.MONGO_IN,groupExamIds));
        List<DBObject> dbObjects=find(MongoFacroty.getAppDB(), Constant.COLLECTION_REPORT_CARD_GROUP_EXAM_DETAIL,query,Constant.FIELDS);
        if(null!=dbObjects&&!dbObjects.isEmpty()){
            for(DBObject dbObject:dbObjects){
                GroupExamDetailEntry examDetailEntry=new GroupExamDetailEntry(dbObject);
                entryMap.put(examDetailEntry.getID(),examDetailEntry);
            }
        }
        return entryMap;
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
