package com.db.business;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.business.BusinessManageEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018/1/15.
 */
public class BusinessManageDao extends BaseDao {

    public String addEntry(BusinessManageEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_BUSINESS_MANAGE, entry.getBaseEntry());
        return entry.getID().toString() ;
    }

    public void updEntry(BusinessManageEntry e) {
        BasicDBObject query=new BasicDBObject(Constant.ID,e.getID());
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,e.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_BUSINESS_MANAGE, query,updateValue);
    }

    //单查询
    public BusinessManageEntry getEntry(ObjectId userId) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO) .append("uid", userId);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_BUSINESS_MANAGE, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new BusinessManageEntry((BasicDBObject)dbo);
        }
        return null;
    }
    public List<BusinessManageEntry> getPageList(String str,int page,int pageSize) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        if(str!=null && !str.equals("")){
            query.append("uid",new ObjectId(str));
        }
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_BUSINESS_MANAGE, query, Constant.FIELDS,new BasicDBObject("ctm",Constant.DESC),(page - 1) * pageSize, pageSize);
        List<BusinessManageEntry> retList =new ArrayList<BusinessManageEntry>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new BusinessManageEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    public List<BusinessManageEntry> getPageList(int page,int pageSize) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_BUSINESS_MANAGE, query, Constant.FIELDS,new BasicDBObject("ctm",Constant.DESC),(page - 1) * pageSize, pageSize);
        List<BusinessManageEntry> retList =new ArrayList<BusinessManageEntry>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new BusinessManageEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    public int getReviewListCount() {
        BasicDBObject query = new BasicDBObject()
                .append("isr", 0); // 未删除
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_BUSINESS_MANAGE,
                        query);
        return count;
    }
}
