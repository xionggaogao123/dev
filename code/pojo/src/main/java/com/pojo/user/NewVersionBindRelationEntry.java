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

    public NewVersionBindRelationEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public NewVersionBindRelationEntry(ObjectId mainUserId,
                                       ObjectId userId,
                                       String relation,
                                       ObjectId regionId,
                                       ObjectId regionAreaId,
                                       String schoolName
                                       ){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("muid",mainUserId)
                .append("uid",userId)
                .append("rl", relation)
                .append("rd",regionId)
                .append("ra",regionAreaId)
                .append("sn",schoolName)
                .append("ir",Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    public void setSchoolName(String schoolName){
        setSimpleValue("sn",schoolName);
    }

    public String getSchoolName(){
        return getSimpleStringValue("sn");
    }

    public ObjectId getRegionId(){
        return getSimpleObjecIDValue("rd");
    }

    public void setRegionId(ObjectId regionId){
        setSimpleValue("rd",regionId);
    }

    public ObjectId getRegionAreaId(){
        return getSimpleObjecIDValue("ra");
    }

    public void setRegionAreaId(ObjectId regionAreaId){
        setSimpleValue("ra",regionAreaId);
    }

    public String getRelation(){
        return getSimpleStringValue("rl");
    }

    public void setRelation(String relation){
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
