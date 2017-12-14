package com.pojo.user;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/8/28.
 * {
 *     muid:mainUserId
 *     uid:userId
 *     rl:relation
 *     regionId
 *     regionAreaId
 *     schoolName
 * }
 */
public class NewVersionBindRelationEntry extends BaseDBObject {

    public NewVersionBindRelationEntry(){


    }
    public NewVersionBindRelationEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public NewVersionBindRelationEntry(ObjectId mainUserId,
                                       ObjectId userId,
                                       int relation,
                                       String provinceName,
                                       String regionName,
                                       String regionAreaName,
                                       String schoolName,
                                       String personalSignature
                                       ){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("muid",mainUserId)
                .append("uid",userId)
                .append("rl", relation)
                .append("pn",provinceName)
                .append("rd",regionName)
                .append("ra",regionAreaName)
                .append("sn",schoolName)
                .append("pst",personalSignature)
                .append("ir",Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    public void setRemove(int remove){
        setSimpleValue("ir",remove);
    }

    public void setPersonalSignature(String personalSignature){
        setSimpleValue("pst",personalSignature);
    }

    public String getPersonalSignature(){
        return getSimpleStringValue("pst");
    }

    public void setRegionAreaName(String regionAreaName){
        setSimpleValue("ra",regionAreaName);
    }

    public String getRegionAreaName(){
        return getSimpleStringValue("ra");
    }

    public void setRegionName(String regionName){
        setSimpleValue("rd",regionName);
    }

    public String getRegionName(){
        return getSimpleStringValue("rd");
    }

    public void setProvinceName(String provinceName){
        setSimpleValue("pn",provinceName);
    }

    public String getProvinceName(){
        return getSimpleStringValue("pn");
    }

    public void setSchoolName(String schoolName){
        setSimpleValue("sn",schoolName);
    }

    public String getSchoolName(){
        return getSimpleStringValue("sn");
    }

    public int getRelation(){
        return getSimpleIntegerValue("rl");
    }

    public void setRelation(int relation){
        setSimpleValue("rl",relation);
    }

    public ObjectId getMainUserId(){
        return getSimpleObjecIDValue("muid");
    }

    public void setMainUserId(ObjectId mainUserId){
        setSimpleValue("muid",mainUserId);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }


}
