package com.pojo.business;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by James on 2018-05-15.
 * 运营用户权限表
 * id
 * userId             运营用户               uid
 * List<String>       用户权限（）           rlt   0 普通
 * List<ObjectId>     生效社群               clt
 * type               权限类型               typ
 * oldAvatar        用户原头像                              ota
 * newAvatar        用户新头像                              nta
 *
 */
public class BusinessRoleEntry extends BaseDBObject {

    public BusinessRoleEntry(){

    }

    public BusinessRoleEntry(BasicDBObject object){
        super(object);
    }

    //添加构造
    public BusinessRoleEntry(
            ObjectId userId,
            int type,
            List<ObjectId> communityIdList,
            List<String> roleType
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("uid", userId)
                .append("typ", type)
                .append("clt", communityIdList)
                .append("rlt", roleType)
                .append("ctm",new Date().getTime())
                .append("isr", 0);
        setBaseEntry(dbObject);
    }
    
  //修改构造
    public BusinessRoleEntry(
            ObjectId userId,
            int type,
            List<ObjectId> communityIdList,
            List<String> roleType,
            String oldAvatar,
            String newAvatar
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("uid", userId)
                .append("typ", type)
                .append("ota",oldAvatar)
                .append("nta",newAvatar)
                .append("clt", communityIdList)
                .append("rlt", roleType)
                .append("ctm",new Date().getTime())
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public BusinessRoleEntry(
            ObjectId id,
            ObjectId userId,
            int type,
            List<ObjectId> communityIdList,
            List<String> roleType
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("uid", userId)
                .append("typ", type)
                .append("clt", communityIdList)
                .append("rlt", roleType)
                .append("ctm", new Date().getTime())
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }
    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }


    public int getType(){
        return getSimpleIntegerValue("typ");
    }

    public void setType(int type){
        setSimpleValue("typ",type);
    }


    public String getOldAvatar(){
        return getSimpleStringValue("ota");
    }

    public void setOldAvatar(String oldAvatar){
        setSimpleValue("ota",oldAvatar);
    }

    public String getNewAvatar(){
        return getSimpleStringValue("nta");
    }

    public void setNewAvatar(String newAvatar){
        setSimpleValue("nta",newAvatar);
    }



    public void setCommunityIdList(List<ObjectId> communityIdList){
        setSimpleValue("clt", MongoUtils.convert(communityIdList));
    }

    public List<ObjectId> getCommunityIdList(){
        ArrayList<ObjectId> communityIdList = new ArrayList<ObjectId>();
        BasicDBList dbList = (BasicDBList) getSimpleObjectValue("clt");
        if(dbList != null && !dbList.isEmpty()){
            for (Object obj : dbList) {
                communityIdList.add((ObjectId)obj);
            }
        }
        return communityIdList;
    }


    public void setRoleType(List<String> roleType){
        setSimpleValue("rlt",roleType);
    }

    public List<String> getRoleType(){
        @SuppressWarnings("rawtypes")
        List roleType =(List)getSimpleObjectValue("rlt");
        return roleType;
    }



    public long getCreateTime(){
        return getSimpleLongValue("ctm");
    }

    public void setCreateTime(long createTime){
        setSimpleValue("ctm",createTime);
    }



    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }





}
