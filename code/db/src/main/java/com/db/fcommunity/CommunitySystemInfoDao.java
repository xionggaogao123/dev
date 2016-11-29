package com.db.fcommunity;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import com.pojo.fcommunity.CommunitySystemInfoEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/11/29.
 */
public class CommunitySystemInfoDao extends BaseDao {

    /**
     * 保存或更新数据
     * @param entry
     */
    public void saveOrUpdateEntry(CommunitySystemInfoEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_SYSTEMINFO,entry.getBaseEntry());
    }


    /**
     * 分页查询列表
     * @param relationId
     * @return
     */
    public List<CommunitySystemInfoEntry> findEntriesByUserIdAndType(ObjectId relationId,int page,int pageSize){
        List<CommunitySystemInfoEntry> entries=new ArrayList<CommunitySystemInfoEntry>();
        BasicDBObject query=getQueryCondition(relationId);
        BasicDBObject order=new BasicDBObject(Constant.ID,-1);
        List<DBObject> dbObjects=find(MongoFacroty.getAppDB(),Constant.COLLECTION_FORUM_COMMUNITY_SYSTEMINFO,query,Constant.FIELDS,
                order,(page-1)*pageSize,pageSize);
        if(null!=dbObjects&&!dbObjects.isEmpty()){
            for(DBObject dbObject:dbObjects){
                entries.add(new CommunitySystemInfoEntry((BasicDBObject)dbObject));
            }
        }
        return entries;
    }

    public BasicDBObject getQueryCondition(ObjectId relationId){
        List<Integer> type=new ArrayList<Integer>();
        type.add(1);
        type.add(2);
        type.add(3);
        type.add(4);
        type.add(5);
        BasicDBObject query=new BasicDBObject().append("reid",relationId).
                append("ty",new BasicDBObject(Constant.MONGO_IN,type)).append("r",0);
        return query;
    }

    public int countEntriesByUserIdAndType(ObjectId relationId){
        BasicDBObject query=getQueryCondition(relationId);
        return count(MongoFacroty.getAppDB(),Constant.COLLECTION_FORUM_COMMUNITY_SYSTEMINFO,query);
    }



    /**
     * 删除id对应的系统消息
     */
    public void removeEntryById(ObjectId id){
        BasicDBObject query=new BasicDBObject().append(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("r",1));
        update(MongoFacroty.getAppDB(),Constant.COLLECTION_FORUM_COMMUNITY_SYSTEMINFO,query,updateValue);
    }

    /**
     * 设置未读消息为已读
     */
    public void setAllData(ObjectId relationId){
        BasicDBObject query=new BasicDBObject().append("reid",relationId).append("unr",0);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("unr",1));
        update(MongoFacroty.getAppDB(),Constant.COLLECTION_FORUM_COMMUNITY_SYSTEMINFO,query,updateValue);
    }

    public int findUnReadInfo(ObjectId relationId){
        BasicDBObject query=new BasicDBObject().append("reid",relationId).append("unr",0);
        return count(MongoFacroty.getAppDB(),Constant.COLLECTION_FORUM_COMMUNITY_SYSTEMINFO,query);
    }


    /**
     * 批量添加系统消息
     * @param userId
     * @param ids
     * @param roleStr
     * @param type
     * @param communityId
     */
    public void addBatchData(ObjectId userId,List<ObjectId> ids,String roleStr,int type,ObjectId communityId){
        List<DBObject> list=new ArrayList<DBObject>();
        for(int i=0;i<ids.size();i++){
            BasicDBObject dbo=new BasicDBObject()
                    .append("uid",userId)
                    .append("reid",ids.get(i))
                    .append("rst",roleStr)
                    .append("ty",type)
                    .append("cmid",communityId)
                    .append("ti",System.currentTimeMillis())
                    .append("unr",0)
                    .append("r",0);
            list.add(dbo);
        }
        save(MongoFacroty.getAppDB(),Constant.COLLECTION_FORUM_COMMUNITY_SYSTEMINFO,list);
    }

}
