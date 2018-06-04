package com.db.referenceData;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.referenceData.ReferenceDataEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-04-23.
 */
public class ReferenceDataDao extends BaseDao {

    /**
     * 批量保存
     *
     * @param list
     */
    public void addBatch(List<DBObject> list) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_REFERENCE_DATA, list);
    }

    public ReferenceDataEntry getEntry(ObjectId id){
        BasicDBObject query=new BasicDBObject(Constant.ID,id);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_REFERENCE_DATA,query,Constant.FIELDS);
        if(null!=dbObject){
            return new ReferenceDataEntry((BasicDBObject) dbObject);
        }else {
            return null;
        }
    }

    public String addEntry(ReferenceDataEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_REFERENCE_DATA,entry.getBaseEntry());
        return entry.getID().toString();
    }


    //老师所有作业列表查询
    public List<ReferenceDataEntry> selectDateListPage(String keyword,int page,int pageSize,int type,List<ObjectId> communityIds) {
        BasicDBObject query = new BasicDBObject().append("isr", 0); // 未删除
        if(type!=0){
            query.append("typ",type);
        }
        if(keyword!=null && !keyword.equals("")){
            query.append("tit", MongoUtils.buildRegex(keyword));
        }
        query.append("cid",new BasicDBObject(Constant.MONGO_IN,communityIds));
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_REFERENCE_DATA,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC, (page - 1) * pageSize, pageSize);
        List<ReferenceDataEntry> entryList = new ArrayList<ReferenceDataEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ReferenceDataEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
    
    public List<ReferenceDataEntry> selectDateListPage(String keyword,int page,int pageSize,int type,List<ObjectId> communityIds,ObjectId userId, int receiveType, String communityId) {
        BasicDBObject query = new BasicDBObject().append("isr", 0); // 未删除
        if(type!=0){
            query.append("typ",type);
        }
        if(StringUtils.isNotBlank(communityId)) {
            query.append("cid", new ObjectId(communityId));
        } else {
            query.append("cid",new BasicDBObject(Constant.MONGO_IN,communityIds));
        }
        if(keyword!=null && !keyword.equals("")){
            query.append("tit", MongoUtils.buildRegex(keyword));
        }
        if (receiveType == 1) {
            
        } else if (receiveType == 3) {
            query.append("uid", userId);
        } else if (receiveType == 2) {
            query.append("uid", new BasicDBObject(Constant.MONGO_NE,userId));
        }
        
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_REFERENCE_DATA,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC, (page - 1) * pageSize, pageSize);
        List<ReferenceDataEntry> entryList = new ArrayList<ReferenceDataEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ReferenceDataEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    public int countDateListPage(String keyword,int type,List<ObjectId> communityIds) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        if(type!=0){
            query.append("typ",type);
        }
        if(keyword!=null && !keyword.equals("")){
            query.append("tit", MongoUtils.buildRegex(keyword));
        }
        query.append("cid",new BasicDBObject(Constant.MONGO_IN,communityIds));
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_REFERENCE_DATA,
                        query);
        return count;
    }
    
    public int countDateListPage(String keyword,int type,List<ObjectId> communityIds,ObjectId userId, int receiveType, String communityId) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        if(type!=0){
            query.append("typ",type);
        }
        if (StringUtils.isNotBlank(communityId)) {
            query.append("cid", new ObjectId(communityId));
        } else {
            query.append("cid",new BasicDBObject(Constant.MONGO_IN,communityIds));
        }
        if(keyword!=null && !keyword.equals("")){
            query.append("tit", MongoUtils.buildRegex(keyword));
        }
        if (receiveType == 1) {
            
        } else if (receiveType == 3) {
            query.append("uid", userId);
        } else if (receiveType == 2) {
            query.append("uid", new BasicDBObject(Constant.MONGO_NE,userId));
        }
        
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_REFERENCE_DATA,
                        query);
        return count;
    }

    public void delEntry(ObjectId id){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isr",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_REFERENCE_DATA, query,updateValue);
    }

    public void delNewEntry(ObjectId id){
        BasicDBObject query = new BasicDBObject("sid",id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isr",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_REFERENCE_DATA, query,updateValue);
    }
}
