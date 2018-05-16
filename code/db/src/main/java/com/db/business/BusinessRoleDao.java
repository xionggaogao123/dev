package com.db.business;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.business.BusinessRoleEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-05-15.
 */
public class BusinessRoleDao extends BaseDao {


    public String addEntry(BusinessRoleEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_BUSINESS_ROLE, entry.getBaseEntry());
        return entry.getID().toString() ;
    }

    public void updEntry(BusinessRoleEntry e) {
        BasicDBObject query=new BasicDBObject(Constant.ID,e.getID());
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,e.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_BUSINESS_ROLE, query,updateValue);
    }
    //修改解析内容和图片
    public void updateEntry(List<ObjectId> subjectIdList,ObjectId userId){
        BasicDBObject query=new BasicDBObject("uid",userId);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("sut",subjectIdList));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_BUSINESS_ROLE, query, updateValue);
    }

    //单查询
    public BusinessRoleEntry getEntry(ObjectId userId) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO) .append("uid", userId);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_BUSINESS_ROLE, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new BusinessRoleEntry((BasicDBObject)dbo);
        }
        return null;
    }
    public List<BusinessRoleEntry> getPageList() {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_BUSINESS_ROLE, query, Constant.FIELDS,new BasicDBObject("ctm",Constant.DESC));
        List<BusinessRoleEntry> retList =new ArrayList<BusinessRoleEntry>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new BusinessRoleEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    public List<BusinessRoleEntry> getPageList(int page,int pageSize) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_BUSINESS_ROLE, query, Constant.FIELDS,new BasicDBObject("ctm",Constant.DESC),(page - 1) * pageSize, pageSize);
        List<BusinessRoleEntry> retList =new ArrayList<BusinessRoleEntry>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new BusinessRoleEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    public int getReviewListCount() {
        BasicDBObject query = new BasicDBObject()
                .append("isr", 0); // 未删除
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_BUSINESS_ROLE,
                        query);
        return count;
    }
}
