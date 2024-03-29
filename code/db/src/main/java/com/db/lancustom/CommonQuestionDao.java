package com.db.lancustom;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.integralmall.AddressEntry;
import com.pojo.lancustom.CommonQuestionEntry;
import com.sys.constants.Constant;

public class CommonQuestionDao extends BaseDao {
    
    public ObjectId addEntry(CommonQuestionEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_COMMON_QUESTION, entry.getBaseEntry());
        return entry.getID();
    }
    
    public void updateQuestion(ObjectId id, String question, String answer, int type, String classes) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject updateValue=new BasicDBObject()
            .append(Constant.MONGO_SET,new BasicDBObject("question",question).append("answer",answer).append("type", type).append("classes", classes));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_COMMON_QUESTION,query,updateValue);
    }
    
    /**
     * 删除
     */
    public void updateIsr(ObjectId goodId) {
        DBObject query = new BasicDBObject(Constant.ID, goodId);
        BasicDBObject updateValue=new BasicDBObject()
            .append(Constant.MONGO_SET,new BasicDBObject("isr",1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_COMMON_QUESTION,query,updateValue);
    }

    public List<CommonQuestionEntry> getCommonQuestion(String pid, String name,int page,int pageSize) {
        List<CommonQuestionEntry> entries = new ArrayList<CommonQuestionEntry>();
        
        BasicDBObject query=new BasicDBObject().append("isr", 0);
        if (StringUtils.isNotBlank(pid)) {
            query.append("pid", new ObjectId(pid));
        } else {
            query.append("pid", new BasicDBObject(Constant.MONGO_EXIST,false));
        }
        if (StringUtils.isNotBlank(name)) {
            query.append("question", new BasicDBObject(Constant.MONGO_REGEX,name));
        }
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_COMMON_QUESTION,
            query,Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new CommonQuestionEntry(dbObject));
            }
        }
        return entries;
    }
    
    public List<CommonQuestionEntry> getCommonQuestion(int type, String name,int page,int pageSize) {
        List<CommonQuestionEntry> entries = new ArrayList<CommonQuestionEntry>();
        
        BasicDBObject query=new BasicDBObject().append("isr", 0);
        if (type != 0) {
            query.append("type", type);
        }
        
        
        if (StringUtils.isNotBlank(name)) {
            query.append("question", new BasicDBObject(Constant.MONGO_REGEX,name));
        }
        query.append("pid", new BasicDBObject(Constant.MONGO_EXIST,true));
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_COMMON_QUESTION,
            query,Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new CommonQuestionEntry(dbObject));
            }
        }
        return entries;
    }
    
    public List<CommonQuestionEntry> getCommonQuestionCount(String pid, String name) {
        List<CommonQuestionEntry> entries = new ArrayList<CommonQuestionEntry>();
        
        BasicDBObject query=new BasicDBObject().append("isr", 0);
        if (StringUtils.isNotBlank(pid)) {
            query.append("pid", new ObjectId(pid));
        } else {
            query.append("pid", new BasicDBObject(Constant.MONGO_EXIST,false));
        }
        if (StringUtils.isNotBlank(name)) {
            query.append("question", new BasicDBObject(Constant.MONGO_REGEX,name));
        }
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_COMMON_QUESTION,
            query,Constant.FIELDS,Constant.MONGO_SORTBY_DESC);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new CommonQuestionEntry(dbObject));
            }
        }
        return entries;
    }
    
    public List<CommonQuestionEntry> getCommonQuestionCountByType(int type, String name) {
        List<CommonQuestionEntry> entries = new ArrayList<CommonQuestionEntry>();
        
        BasicDBObject query=new BasicDBObject().append("isr", 0);
        if (type != 0) {
            query.append("type", type);
        }
        
       
        if (StringUtils.isNotBlank(name)) {
            query.append("question", new BasicDBObject(Constant.MONGO_REGEX,name));
        }
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_COMMON_QUESTION,
            query,Constant.FIELDS,Constant.MONGO_SORTBY_DESC);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new CommonQuestionEntry(dbObject));
            }
        }
        return entries;
    }


    /**
     * 修复数据 获取所有数据
     * @return
     */
    public List<CommonQuestionEntry> getAllCommonQuestion() {
        List<CommonQuestionEntry> entries = new ArrayList<CommonQuestionEntry>();
        BasicDBObject query=new BasicDBObject().append("isr", 0);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_COMMON_QUESTION,
                query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new CommonQuestionEntry(dbObject));
            }
        }
        return entries;
    }
    /**
     * 修复数据 查找是类型的数据
     * @return
     */
    public List<CommonQuestionEntry> getAllTypeCommonQuestion() {
        List<CommonQuestionEntry> entries = new ArrayList<CommonQuestionEntry>();
        BasicDBObject query=new BasicDBObject().append("isr", 0);
//        query.append("pid", new BasicDBObject(Constant.MONGO_NE,null));
        query.append("pid", new BasicDBObject(Constant.MONGO_EXIST,false));
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_COMMON_QUESTION,
                query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new CommonQuestionEntry(dbObject));
            }
        }
        return entries;
    }

    /**
     * 新版修复删除
     */
    public void updateRemoveAllIsr(List<ObjectId> ids) {
        DBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids));
        BasicDBObject updateValue=new BasicDBObject()
                .append(Constant.MONGO_SET,new BasicDBObject("isr",2));//新版修复删除
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_COMMON_QUESTION,query,updateValue);
    }

    /**
     * 插入集合
     * @param dbObjects
     */
    public void insertAll(List<DBObject> dbObjects) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_COMMON_QUESTION, dbObjects);
    }
}
