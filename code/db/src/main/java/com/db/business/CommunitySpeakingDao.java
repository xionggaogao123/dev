package com.db.business;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.business.CommunitySpeakingEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-07-02.
 */
public class CommunitySpeakingDao extends BaseDao {
    public String addEntry(CommunitySpeakingEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_COMMUNITY_BANNING, entry.getBaseEntry());
        return entry.getID().toString() ;
    }

    //单查询
    public CommunitySpeakingEntry getEntry(ObjectId userId,ObjectId groupId) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO) .append("uid", userId).append("gid",groupId);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_COMMUNITY_BANNING, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new CommunitySpeakingEntry((BasicDBObject)dbo);
        }
        return null;
    }

    //修改解析内容和图片
    public void updateEntry(ObjectId userId,ObjectId groupId){
        BasicDBObject query=new BasicDBObject();
        query.append("isr", Constant.ZERO) .append("uid", userId).append("gid",groupId);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isr",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_COMMUNITY_BANNING, query, updateValue);
    }

    public List<String> getPageList(ObjectId groupId) {
        long current = System.currentTimeMillis();
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("gid",groupId);
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_COMMUNITY_BANNING, query);
        List<String> retList =new ArrayList<String>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                CommunitySpeakingEntry communitySpeakingEntry = new CommunitySpeakingEntry((BasicDBObject)dbo);
                if(communitySpeakingEntry.getEndTime()>current) {//未结束
                    retList.add(communitySpeakingEntry.getUserId().toString());
                }
            }
        }
        return retList;
    }
}
