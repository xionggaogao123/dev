package com.db.reportCard;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.reportCard.GroupExamDetailEntry;
import com.pojo.reportCard.MultiGroupExamDetailEntry;
import com.sys.constants.Constant;

public class MultiGroupExamDetailDao extends BaseDao {

    /**
     * 
     *〈简述〉保存
     *〈详细描述〉
     * @author Administrator
     * @param examDetailEntry
     * @return
     */
    public ObjectId saveGroupExamDetailEntry(MultiGroupExamDetailEntry examDetailEntry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_MULTI_REPORT_CARD_GROUP_EXAM_DETAIL,examDetailEntry.getBaseEntry());
        return examDetailEntry.getID();
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
                .append(Constant.MONGO_SET,new BasicDBObject("isr",status));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_MULTI_REPORT_CARD_GROUP_EXAM_DETAIL,query,updateValue);
    }
    
    /**
     * 更新状态(是否生成成绩单)
     * @param id
     * @param status
     */
    public void updateGroupExamDetailEntry1(ObjectId id,int status){
        BasicDBObject query=new BasicDBObject()
                .append(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject()
                .append(Constant.MONGO_SET,new BasicDBObject("st",status));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_MULTI_REPORT_CARD_GROUP_EXAM_DETAIL,query,updateValue);
    }
    
    public List<MultiGroupExamDetailEntry> getMappingDatas(List<ObjectId> cid, int page,int pageSize){
        List<MultiGroupExamDetailEntry> entries=new ArrayList<MultiGroupExamDetailEntry>();
        BasicDBObject query = new BasicDBObject("isr", Constant.ZERO);
        if (Constant.ZERO != cid.size()) {
            query.append("cmId", new BasicDBObject(Constant.MONGO_IN, cid));
        }
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(),
                Constant.COLLECTION_MULTI_REPORT_CARD_GROUP_EXAM_DETAIL,query,
                Constant.FIELDS,new BasicDBObject("ti", -1),(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new MultiGroupExamDetailEntry(dbObject));
            }
        }
        return entries;
    }
    
    public MultiGroupExamDetailEntry getEntry(ObjectId cid){
        BasicDBObject query = new BasicDBObject("isr", Constant.ZERO);
   
        query.append("_id", cid);
        
        DBObject dbObject=findOne(MongoFacroty.getAppDB(),
                Constant.COLLECTION_MULTI_REPORT_CARD_GROUP_EXAM_DETAIL,query,
                Constant.FIELDS);
        
        if(null!=dbObject){
            return new MultiGroupExamDetailEntry(dbObject);
        }else{
            return null;
        }
    }
    
    
    public Integer getMappingDatas(List<ObjectId> cid){
       
        BasicDBObject query = new BasicDBObject("isr", Constant.ZERO);
        if (Constant.ZERO != cid.size()) {
            query.append("cmId", new BasicDBObject(Constant.MONGO_IN, cid));
        }
        return count(MongoFacroty.getAppDB(),
                Constant.COLLECTION_MULTI_REPORT_CARD_GROUP_EXAM_DETAIL,query);
        
    }
}
