package com.db.pet;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.FieldValuePair;
import com.pojo.pet.PetInfo;
import com.pojo.pet.UserPetEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by qinbo on 15/3/13.
 */
public class UserPetDao extends BaseDao{

    /**
     * 增加用户宠物信息
     * @param e 用户宠物entry
     * @return
     */
    public ObjectId addUserPetEntry(UserPetEntry e)
    {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_PET, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 给用户在增加一个宠物
     * @param petInfo 宠物entry
     * @return
     */
    public void addPetEntry2User(ObjectId userId,PetInfo petInfo)
    {
        DBObject query =new BasicDBObject("uid",userId);
        DBObject updateValue =new BasicDBObject(Constant.MONGO_PUSH,new BasicDBObject("pets", petInfo.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_PET, query, updateValue);
    }

    /**
     * 用户在删除一个宠物
     * @param id
     * @param petInfo
     */
    public void delPetEntry2User(ObjectId id, PetInfo petInfo) {
        BasicDBObject query=new BasicDBObject(Constant.ID,id);
        BasicDBObject update=new BasicDBObject(Constant.MONGO_PULL,new BasicDBObject("pets",petInfo.getBaseEntry()));
        update(MongoFacroty.getAppDB(),Constant.COLLECTION_USER_PET,query,update);
    }

    /** 根据id查找某个用户宠物信息
     * @param userId 要查找的用户id
     * @return
     */
    public UserPetEntry findUserPet(ObjectId userId)
    {
        BasicDBObject query =new BasicDBObject("uid",userId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_PET,query,Constant.FIELDS);
        if(null!=dbo)
        {
            return new UserPetEntry((BasicDBObject)dbo);
        }
        return null;
    }

    /**
     * 更新用户宠物信息
     * @param userId 用户的id
     * @param pairs 要更新的键值对
     */
    public void update(ObjectId userId,FieldValuePair... pairs )
    {
        BasicDBObject query =new BasicDBObject("uid",userId);
        BasicDBObject valueDBO=new BasicDBObject();
        for(FieldValuePair pair:pairs)
        {
            valueDBO.append(pair.getField(), pair.getValue());
        }
        valueDBO.append(Constant.FIELD_SYN, Constant.SYN_YES_NEED);
        BasicDBObject updateValue =new BasicDBObject(Constant.MONGO_SET,valueDBO);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_PET,query,updateValue);
    }

    /**移除某个用户
     * @param UserId 要移除用户id
     */
    public void remove(ObjectId UserId)
    {
        DBObject query = new BasicDBObject("uid",UserId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_PET,query);
    }

    /**查询用户有多少个宠物
     * @param UserId 用户id
     */
    public int selPetCount(ObjectId UserId) {
        UserPetEntry userPet=findUserPet(UserId);
        if(userPet==null){
            return 0;
        }else{
            if(null!=userPet.getPetInfos()&&!userPet.getPetInfos().isEmpty()){
                return userPet.getPetInfos().size();
            }else{
                return 0;
            }
        }
    }

    /**修改宠物名称
     * @param userid 用户id
     * @param petEntryId  用户与宠物关联id
     * @param newPetName 宠物新名称
     */
    public void updatePetName(ObjectId userid, ObjectId petEntryId, String newPetName) {
        DBObject query =new BasicDBObject("uid",userid).append("pets.id",petEntryId);
        DBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("pets.$.pnm",newPetName));
        update(MongoFacroty.getAppDB(),Constant.COLLECTION_USER_PET,query,updateValue);
    }

    /**查询宠物
     * @param userid 用户id
     * @param selecttype  宠物是否选中
     */
    public UserPetEntry selPetByParam(ObjectId userid, int selecttype) {
        /*DBObject query =new BasicDBObject("uid",userid).append("pets.st",selecttype);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_PET, query, new BasicDBObject("pets.$",1).append("uid",1));
        if(null!=dbo)
        {
            return new UserPetEntry((BasicDBObject)dbo);
        }
        return null;*/

        DBObject matchDBO=new BasicDBObject(Constant.MONGO_MATCH,new BasicDBObject("uid",userid));
        DBObject projectDBO =new BasicDBObject(Constant.MONGO_PROJECT,new BasicDBObject("uid",1).append("pets", 1));
        DBObject unbindDBO =new BasicDBObject(Constant.MONGO_UNWIND,"$pets");
        DBObject matchDBO1=new BasicDBObject(Constant.MONGO_MATCH,new BasicDBObject("pets.st",selecttype));
        List<PetInfo> petList =new ArrayList<PetInfo>();
        AggregationOutput output;
        try {
            output = aggregate(MongoFacroty.getAppDB(),Constant.COLLECTION_USER_PET,matchDBO,projectDBO,unbindDBO,matchDBO1);
            Iterator<DBObject> iter=output.results().iterator();
            BasicDBObject userPetEntry;
            BasicDBObject petInfo;
            while(iter.hasNext()) {
                userPetEntry=(BasicDBObject)iter.next();
                petInfo=(BasicDBObject)userPetEntry.get("pets");
                petList.add(new PetInfo(petInfo));
            }
        } catch (Exception e) {
        }
        return new UserPetEntry(userid, petList);
    }

    /**把宠物设置成未选中状态
     * @param userid 用户id
     */
    public void updatepetNotSelectType(ObjectId userid) {
        DBObject query =new BasicDBObject("uid",userid).append("pets.st",1);
        DBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("pets.$.st",0));
        update(MongoFacroty.getAppDB(),Constant.COLLECTION_USER_PET,query,updateValue);
    }

    /**把宠物设置成选中状态
     * @param userid 用户id
     * @param petEntryId 用户与宠物关联id
     */
    public void updatePetSelectType(ObjectId userid, ObjectId petEntryId) {
        DBObject query =new BasicDBObject("uid",userid).append("pets.id",petEntryId);
        DBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("pets.$.st",1));
        update(MongoFacroty.getAppDB(),Constant.COLLECTION_USER_PET,query,updateValue);
    }
    /**孵化的宠物数量
     * @param userid 用户id
     */
    public int selHatchPetCount(ObjectId userid) {
        return selAllPetByUserIdCount(userid,1);
    }

    /**修改宠物名称
     * @param userid 用户id
     * @param petEntryId  用户与宠物关联id
     */
    public UserPetEntry selHatchPetByIdCount(ObjectId userid, ObjectId petEntryId) {
        DBObject query =new BasicDBObject("uid",userid).append("pets.id",petEntryId);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_PET, query, new BasicDBObject("pets.$",1).append("uid",1));
        if(null!=dbo)
        {
            return new UserPetEntry((BasicDBObject)dbo);
        }
        return null;
    }

    /**获取用户宠物数量
     * @param userid 用户id
     * @param ishatch  宠物蛋是否孵化
     */
    public int selAllPetByUserIdCount(ObjectId userid, int ishatch) {

        UserPetEntry userPet=findUserPetByParam(userid,ishatch);
        if(userPet==null){
            return 0;
        }else{
            if(null!=userPet.getPetInfos()&&!userPet.getPetInfos().isEmpty()){
                return userPet.getPetInfos().size();
            }else{
                return 0;
            }
        }
    }

    /**获取用户宠物信息
     * @param userId 用户id
     * @param ishatch  宠物蛋是否孵化
     */
    public UserPetEntry findUserPetByParam(ObjectId userId,int ishatch)
    {
        DBObject matchDBO=new BasicDBObject(Constant.MONGO_MATCH,new BasicDBObject("uid",userId));
        DBObject projectDBO =new BasicDBObject(Constant.MONGO_PROJECT,new BasicDBObject("uid",1).append("pets", 1));
        DBObject unbindDBO =new BasicDBObject(Constant.MONGO_UNWIND,"$pets");
        DBObject matchDBO1=new BasicDBObject(Constant.MONGO_MATCH,new BasicDBObject("pets.ih",ishatch));
        List<PetInfo> petList =new ArrayList<PetInfo>();
        AggregationOutput output;
        try {
            output = aggregate(MongoFacroty.getAppDB(),Constant.COLLECTION_USER_PET,matchDBO,projectDBO,unbindDBO,matchDBO1);
            Iterator<DBObject> iter=output.results().iterator();
            BasicDBObject userPetEntry;
            BasicDBObject petInfo;
            while(iter.hasNext()) {
                userPetEntry=(BasicDBObject)iter.next();
                petInfo=(BasicDBObject)userPetEntry.get("pets");
                petList.add(new PetInfo(petInfo));
            }
        } catch (Exception e) {
        }
        return new UserPetEntry(userId, petList);
    }

    /**获取用户选中宠物的数量
     * @param userid 用户id
     */
    public int selMyPetByUserIdCount(ObjectId userid) {
        UserPetEntry userPet=selPetByParam(userid,1);
        if(userPet==null){
            return 0;
        }else{
            if(null!=userPet.getPetInfos()&&!userPet.getPetInfos().isEmpty()){
                return userPet.getPetInfos().size();
            }else{
                return 0;
            }
        }
    }

    /**修改宠物信息
     * @param userid 用户id
     * @param petInfo 宠物信息
     */
    public void updateUserPetInfo(ObjectId userid,PetInfo petInfo) {
        DBObject query =new BasicDBObject("uid",userid).append("pets.id", petInfo.getId());
        DBObject updateValue=new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("pets.$.pid", petInfo.getPetid()).
                        append("pets.$.pnm", petInfo.getPetname()).
                        append("pets.$.st", petInfo.getSelecttype()).
                        append("pets.$.ih", petInfo.getIshatch()).
                        append("pets.$.upt", petInfo.getUpdatedate()));
        update(MongoFacroty.getAppDB(),Constant.COLLECTION_USER_PET,query,updateValue);
    }
}
