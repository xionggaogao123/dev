package com.db.communityValidate;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.fcommunity.ValidateInfoEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/12/26.
 */
public class ValidateInfoDao extends BaseDao {

    public void saveOrUpdate(ValidateInfoEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_VALIDATE_INFO,entry.getBaseEntry());
    }

    public void batchSaveInfo(List<ValidateInfoEntry> entries){
        List<DBObject> list= MongoUtils.fetchDBObjectList(entries);
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_VALIDATE_INFO,list);
    }

    public int countValidateInfos(ObjectId reviewId){
        BasicDBObject query=new BasicDBObject()
                .append("rw",reviewId)
                .append("ir",0);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_VALIDATE_INFO,query);
    }

    public List<ValidateInfoEntry> getValidateInfos(ObjectId reviewId,int page,int pageSize){
        List<ValidateInfoEntry> entries=new ArrayList<ValidateInfoEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("rw",reviewId)
                .append("ir",0);

        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_VALIDATE_INFO,query,Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=list&&!list.isEmpty()){
            for(DBObject dbObject:list){
                entries.add(new ValidateInfoEntry((BasicDBObject)dbObject));
            }

        }
        return entries;
    }


    public List<ValidateInfoEntry> getEntries(ObjectId userId,ObjectId communityId,ObjectId reviewKeyId){
        List<ValidateInfoEntry> entries=new ArrayList<ValidateInfoEntry>();
        BasicDBObject query=new BasicDBObject("uid",userId).append("cmId",communityId).append("rei",reviewKeyId);
        List<DBObject> dbObjects=find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_VALIDATE_INFO,query);
        if(null!=dbObjects&&!dbObjects.isEmpty()){
            for(DBObject dbObject:dbObjects){
                entries.add(new ValidateInfoEntry((BasicDBObject)dbObject));
            }
        }
        return entries;
    }

    /**
     * 更新权限状态
     * @param communityId
     */
    public void updateAuthority(ObjectId reviewedId,ObjectId communityId,int authority){
        BasicDBObject query=new BasicDBObject("rw",reviewedId).append("ty",1).append("cmId",communityId);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("aut",authority));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_VALIDATE_INFO,query,updateValue);
    }


    //先查询数据看是否审核了
    public ValidateInfoEntry getEntry(ObjectId userId,ObjectId reviewKeyId,ObjectId communityId){
        BasicDBObject query=new BasicDBObject("uid",userId).append("cmId",communityId).append("rei",reviewKeyId);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_VALIDATE_INFO,query);
        if(null!=dbObject){
            return new ValidateInfoEntry((BasicDBObject) dbObject);
        }else{
            return null;
        }
    }

    /**
     * 最心一条消息
     * @param communityId
     * @return
     */
    public ValidateInfoEntry getNewsInfo(ObjectId communityId,ObjectId userId){
        BasicDBObject query=new BasicDBObject("cmId",communityId).append("rw",userId).append("st",0).append("ty",1);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_VALIDATE_INFO,query);
        if(null!=dbObject){
            return new ValidateInfoEntry((BasicDBObject) dbObject);
        }else{
            return null;
        }
    }

    public ValidateInfoEntry getIsApplyEntry(ObjectId userId,ObjectId communityId){
        BasicDBObject query=new BasicDBObject("uid",userId).append("cmId",communityId).append("st",0);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_VALIDATE_INFO,query);
        if(null!=dbObject){
            return new ValidateInfoEntry((BasicDBObject) dbObject);
        }else{
            return null;
        }
    }


    /**
     * 获取未处理的申请人信息
     * @return
     */
    public int getApplyCount(ObjectId userId,int type){
        BasicDBObject query=new BasicDBObject("rw",userId).append("ty",type).append("st",0);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_VALIDATE_INFO,query);
    }

    public int getApprovedCount(ObjectId userId,int type){
        BasicDBObject query=new BasicDBObject("rw",userId).append("ty",type).append("st",0).append("aut",0);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_VALIDATE_INFO,query);
    }



    public ValidateInfoEntry getApplyEntry(ObjectId userId,ObjectId communityId,ObjectId reviewKeyId){
        BasicDBObject query=new BasicDBObject("rw",userId).append("cmId",communityId).append("rei",reviewKeyId);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_VALIDATE_INFO,query);
        if(null!=dbObject){
            return new ValidateInfoEntry((BasicDBObject) dbObject);
        }else{
            return null;
        }
    }
}
