package com.db.wrongquestion;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.wrongquestion.SubjectClassEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by James on 2017/9/6.
 */
public class SubjectClassDao extends BaseDao {

    /**
     * 增加
     * @param e
     * @return
     */
    public ObjectId addSubjectEntry(SubjectClassEntry e)
    {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_SUBJECT_CLASS, e.getBaseEntry());
        return e.getID();
    }

    public SubjectClassEntry getEntry(ObjectId id){
        BasicDBObject query=new BasicDBObject(Constant.ID,id);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_SUBJECT_CLASS,query,Constant.FIELDS);
        if(null!=dbObject){
            return new SubjectClassEntry((BasicDBObject) dbObject);
        }else {
            return null;
        }
    }

    /**
     * 查询所有的科目列表
     */
    public List<SubjectClassEntry> getList(){
        BasicDBObject query = new BasicDBObject()
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_SUBJECT_CLASS,
                        query, Constant.FIELDS,
                        new BasicDBObject("lev",Constant.ASC));
        List<SubjectClassEntry> entryList = new ArrayList<SubjectClassEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new SubjectClassEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    public Map<ObjectId,SubjectClassEntry> getSubjectClassEntryMap(List<ObjectId> objectIdList){
        Map<ObjectId,SubjectClassEntry> subjectClassEntryMap=new HashMap<ObjectId, SubjectClassEntry>();
        BasicDBObject query = new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_IN,objectIdList))
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_SUBJECT_CLASS,
                        query, Constant.FIELDS,
                        new BasicDBObject("lev",Constant.ASC));
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                SubjectClassEntry entry=new SubjectClassEntry((BasicDBObject) obj);
                subjectClassEntryMap.put(entry.getID(),entry);
            }
        }
        return subjectClassEntryMap;
    }
    public List<SubjectClassEntry> getListByList(List<ObjectId> objectIdList){
        BasicDBObject query = new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_IN,objectIdList))
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_SUBJECT_CLASS,
                        query, Constant.FIELDS,
                        new BasicDBObject("lev",Constant.ASC));
        List<SubjectClassEntry> entryList = new ArrayList<SubjectClassEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new SubjectClassEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    public String getBigListByList(List<ObjectId> objectIdList){
        BasicDBObject query = new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_IN,objectIdList))
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_SUBJECT_CLASS,
                        query, Constant.FIELDS,
                        new BasicDBObject("lev",Constant.ASC));
        StringBuffer sb = new StringBuffer();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                sb.append(new SubjectClassEntry((BasicDBObject) obj).getName());
            }
        }
        return sb.toString();
    }
}
