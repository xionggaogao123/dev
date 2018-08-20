package com.db.reportCard;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.integralmall.OrderEntry;
import com.pojo.reportCard.GroupExamUserRecordEntry;
import com.pojo.reportCard.ScoreRepresentEntry;
import com.sys.constants.Constant;

public class ScoreRepresentDao extends BaseDao {

    public ObjectId saveScoreRepresent(ScoreRepresentEntry scorePresentEntry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORE_REPRESENT,scorePresentEntry.getBaseEntry());
        return scorePresentEntry.getID();
    }
    
    /**
     * 删除
     */
    public void updateIsr(ObjectId goodId) {
        DBObject query = new BasicDBObject(Constant.ID, goodId);
        BasicDBObject updateValue=new BasicDBObject()
            .append(Constant.MONGO_SET,new BasicDBObject("ir",1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORE_REPRESENT,query,updateValue);
    }
    
    /**
     * 删除
     */
    public void updateIsrOne(ObjectId groupExamDetailId, ObjectId subjectId) {
        DBObject query = new BasicDBObject("groupExamDetailId", groupExamDetailId).append("subjectId", subjectId);
        BasicDBObject updateValue=new BasicDBObject()
            .append(Constant.MONGO_SET,new BasicDBObject("ir",1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORE_REPRESENT,query,updateValue);
    }
    
    /**
     * 删除
     */
    public void updateIsr(ObjectId groupExamDetailId, ObjectId subjectId) {
        DBObject query = new BasicDBObject("groupExamDetailId", groupExamDetailId).append("subjectId", subjectId);
        BasicDBObject updateValue=new BasicDBObject()
            .append(Constant.MONGO_SET,new BasicDBObject("ir",0));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORE_REPRESENT,query,updateValue);
    }
    
    /**
     * 
     *〈简述〉根据考试id、课程id查所有满足的
     *〈详细描述〉
     * @author Administrator
     * @param groupExamDetailId
     * @param subjectId
     * @return
     */
    public List<ScoreRepresentEntry> getScoreRepresentAll(ObjectId groupExamDetailId, ObjectId subjectId) {
        List<ScoreRepresentEntry> entries = new ArrayList<ScoreRepresentEntry>();
        BasicDBObject query=new BasicDBObject();
        query.append("ir",Constant.ZERO);
       
        query.append("groupExamDetailId", groupExamDetailId);
        query.append("subjectId", subjectId);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORE_REPRESENT,
            query,Constant.FIELDS,Constant.MONGO_SORTBY_DESC);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new ScoreRepresentEntry(dbObject));
            }
        }
        return entries;
    }
    
    public List<ScoreRepresentEntry> getScoreRepresentAll(ObjectId groupExamDetailId) {
        List<ScoreRepresentEntry> entries = new ArrayList<ScoreRepresentEntry>();
        BasicDBObject query=new BasicDBObject();
        query.append("ir",Constant.ZERO);
        query.append("groupExamDetailId", groupExamDetailId);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORE_REPRESENT,
            query,Constant.FIELDS,new BasicDBObject("sort", 1));
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new ScoreRepresentEntry(dbObject));
            }
        }
        return entries;
    }
    
    /**
     * 
     *〈简述〉所有未删除的分数代表
     *〈详细描述〉
     * @author Administrator
     * @param groupExamDetailId
     * @return
     */
    public List<ScoreRepresentEntry> getScoreRepresentWithgid(ObjectId groupExamDetailId) {
        List<ScoreRepresentEntry> entries = new ArrayList<ScoreRepresentEntry>();
        BasicDBObject query=new BasicDBObject();
        query.append("ir",Constant.ZERO);
       
        query.append("groupExamDetailId", groupExamDetailId);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORE_REPRESENT,
            query,Constant.FIELDS,Constant.MONGO_SORTBY_DESC);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new ScoreRepresentEntry(dbObject));
            }
        }
        return entries;
    }
    
    /**
     * 
     *〈简述〉除了总分的分数代表
     *〈详细描述〉
     * @author Administrator
     * @param groupExamDetailId
     * @return
     */
    public List<ScoreRepresentEntry> getScoreRepresentWithgidnM(ObjectId groupExamDetailId) {
        List<ScoreRepresentEntry> entries = new ArrayList<ScoreRepresentEntry>();
        BasicDBObject query=new BasicDBObject();
        query.append("ir",Constant.ZERO);
        query.append("subjectId", new BasicDBObject(Constant.MONGO_EXIST, true));
        query.append("groupExamDetailId", groupExamDetailId);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORE_REPRESENT,
            query,Constant.FIELDS,Constant.MONGO_SORTBY_DESC);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new ScoreRepresentEntry(dbObject));
            }
        }
        return entries;
    }
    
    /**
     * 
     *〈简述〉删除总分代表
     *〈详细描述〉
     * @author Administrator
     * @param groupExamDetailId
     * @return
     */
    public void updateScoreRepresentWithgidoM(ObjectId groupExamDetailId) {

        BasicDBObject query=new BasicDBObject();
        query.append("ir",Constant.ZERO);
        query.append("subjectId", new BasicDBObject(Constant.MONGO_EXIST, false));
        query.append("groupExamDetailId", groupExamDetailId);
        BasicDBObject updateValue=new BasicDBObject()
            .append(Constant.MONGO_SET,new BasicDBObject("ir",1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORE_REPRESENT,query,updateValue);


    }
    
    
    public  int getScoreRepresentCount(ObjectId groupExamDetailId, ObjectId subjectId) {
        BasicDBObject query=new BasicDBObject();
        query.append("ir",Constant.ZERO);
       
        query.append("groupExamDetailId", groupExamDetailId);
        query.append("subjectId", subjectId);
        int i =count(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORE_REPRESENT,
            query);
        
        return i;
    }
    
    public  int getScoreRepresentTrueCount(ObjectId groupExamDetailId, ObjectId subjectId) {
        BasicDBObject query=new BasicDBObject();
       
        query.append("groupExamDetailId", groupExamDetailId);
        query.append("subjectId", subjectId);
        int i =count(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORE_REPRESENT,
            query);
        
        return i;
    }
    
    
}
