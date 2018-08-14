package com.db.communityValidate;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.fcommunity.ValidateGroupInfoEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-08-07.
 */
public class ValidateGroupInfoDao extends BaseDao {
    //添加临时记录
    public ObjectId addEntry(ValidateGroupInfoEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_VALIDATE_GROUP_INFO, entry.getBaseEntry());
        return entry.getID();
    }

    /**
     * 批量保存
     *
     * @param list
     */
    public void addBatch(List<DBObject> list) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_VALIDATE_GROUP_INFO, list);
    }


    public ValidateGroupInfoEntry getEntry(ObjectId id){
        BasicDBObject query=new BasicDBObject(Constant.ID,id).append("isr",Constant.ZERO);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_VALIDATE_GROUP_INFO,query,Constant.FIELDS);
        if(null!=dbObject){
            return new ValidateGroupInfoEntry((BasicDBObject) dbObject);
        }else {
            return null;
        }
    }


    //分页查询
    public List<ValidateGroupInfoEntry> getMyBuyEntryListById(ObjectId reviewedId,int page,int pageSize,int contactType){
        List<ValidateGroupInfoEntry> entryList=new ArrayList<ValidateGroupInfoEntry>();
        BasicDBObject query=new BasicDBObject().append("rw",reviewedId).append("isr", 0).append("cty",contactType);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_VALIDATE_GROUP_INFO, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC,(page - 1) * pageSize, pageSize);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ValidateGroupInfoEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    public int countMyBuyEntryListById(ObjectId reviewedId,int contactType){
        BasicDBObject query=new BasicDBObject().append("rw", reviewedId).append("isr", 0).append("cty",contactType);
        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_VALIDATE_GROUP_INFO, query);
        return count;
    }

    /**
     * 未阅数量
     * @return
     */
    public int count(ObjectId userId){
        BasicDBObject query=new BasicDBObject().append("sta", Constant.ZERO).append("isr", 0).append("rw", userId);
        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_VALIDATE_GROUP_INFO, query);
        return count;
    }

    /**
     * 修改
     */
    public void updEntry(ObjectId id,int approvedStatus,long backTime) {
        BasicDBObject query=new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("aps", approvedStatus).append("btm", backTime));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_VALIDATE_GROUP_INFO, query,updateValue);
    }
    /*
      修改全部
     */
    public void updEntry(ObjectId contactId,ObjectId reviewedId,int approvedStatus,long backTime) {
        BasicDBObject query=new BasicDBObject("cid",contactId).append("rw",reviewedId).append("isr",Constant.ZERO).append("aps",Constant.ZERO);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("aps", approvedStatus).append("btm", backTime));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_VALIDATE_GROUP_INFO, query,updateValue);
    }

    /**
     * 修改已处理
     */
    public void updStatusEntry(ObjectId userId,int status) {
        BasicDBObject query=new BasicDBObject("rw",userId).append("sta",Constant.ZERO).append("isr", 0);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("sta",status));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_VALIDATE_GROUP_INFO, query,updateValue);
    }
}
