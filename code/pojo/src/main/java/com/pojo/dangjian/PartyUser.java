package com.pojo.dangjian;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/** 党建成员
 * Created by fl on 2016/6/14.
 * uid : userId  用户id
 * sid : schoolId 学校id
 * ispm: isPartyMember  是否党员
 * iscm: isCenterMember 是否中心组成员
 * isps: isPartySecretary 是否党支书
 */
public class PartyUser extends BaseDBObject {

    public PartyUser(){}

    public PartyUser(BasicDBObject baseEntry){
        setBaseEntry(baseEntry);
    }

    public PartyUser(ObjectId userId, ObjectId schoolId, int isPartyMember, int isCenterMember, int isPartySecretary){
        BasicDBObject baseEntry = new BasicDBObject()
                .append("uid", userId)
                .append("sid", schoolId)
                .append("ispm", isPartyMember)
                .append("iscm", isCenterMember)
                .append("isps", isPartySecretary)
                ;
        setBaseEntry(baseEntry);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid", userId);
    }

    public ObjectId getSchoolId(){
        return getSimpleObjecIDValue("sid");
    }

    public void setSchoolId(ObjectId schoolId){
        setSimpleValue("sid", schoolId);
    }

    public int getIsPartyMember(){
        return getSimpleIntegerValue("ispm");
    }

    public void setIsPartyMember(int isPartyMember){
        setSimpleValue("ispm", isPartyMember);
    }

    public int getIsCenterMember(){
        return getSimpleIntegerValue("iscm");
    }

    public void setIsCenterMember(int isCenterMember){
        setSimpleValue("iscm", isCenterMember);
    }

    public int getIsPartySecretary(){
        return getSimpleIntegerValue("isps");
    }

    public void setIsPartySecretary(int isPartySecretary){
        setSimpleValue("isps", isPartySecretary);
    }

    //
}
