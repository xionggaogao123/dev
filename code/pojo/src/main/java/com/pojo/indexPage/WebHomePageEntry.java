package com.pojo.indexPage;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/11/17.
 * type:1 作业  2 通知  3成绩单
 * communityId:群组Id
 * userId:发起者Id
 * contactId:作业或者通知/成绩单消息Id,
 * subjectId:学科Id
 * status:状态针对作业或者通知以及成绩单 0默认保存,2已发送
 * receivedId:针对成绩单接收者Id(作业和通知是null)
 * ti:发送时间
 */
public class WebHomePageEntry extends BaseDBObject{

    public WebHomePageEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public WebHomePageEntry(int type,
                            ObjectId userId,
                            ObjectId communityId,
                            ObjectId contactId,
                            ObjectId subjectId,
                            ObjectId receiveId,
                            ObjectId reportCardId,
                            ObjectId examTypeId,
                            int status){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("ty",type)
                .append("uid",userId)
                .append("cid",communityId)
                .append("cti",contactId)
                .append("rc",reportCardId)
                .append("et",examTypeId)
                .append("sid",subjectId)
                .append("rid",receiveId)
                .append("ti",System.currentTimeMillis())
                .append("st",status)
                .append("ir", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }
    
    public WebHomePageEntry(int type,
                            ObjectId userId,
                            ObjectId communityId,
                            ObjectId contactId,
                            String subjectIds,
                            ObjectId receiveId,
                            ObjectId reportCardId,
                            ObjectId examTypeId,
                            int status){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("ty",type)
                .append("uid",userId)
                .append("cid",communityId)
                .append("cti",contactId)
                .append("rc",reportCardId)
                .append("et",examTypeId)
                .append("sids",subjectIds)
                .append("rid",receiveId)
                .append("ti",System.currentTimeMillis())
                .append("st",status)
                .append("ir", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }
    
    public void setSubjectIds(String subjectIds){
        setSimpleValue("sids",subjectIds);
    }

    public String getSubjectIds(){
        return getSimpleStringValue("sids");
    }

    public void setRemove(int remove){
        setSimpleValue("ir",remove);
    }

    public void setExamTypeId(ObjectId examTypeId){
        setSimpleValue("et",examTypeId);
    }

    public ObjectId getExamTypeId(){
        return getSimpleObjecIDValue("et");
    }

    public void setReportCardId(ObjectId reportCardId){
        setSimpleValue("rc",reportCardId);
    }

    public ObjectId getReportCardId(){
        return getSimpleObjecIDValue("rc");
    }

    public void setTime(long time){
        setSimpleValue("ti",time);
    }

    public long getTime(){
        return getSimpleLongValue("ti");
    }

    public void setStatus(int status){
        setSimpleValue("st",status);
    }

    public int getStatus(){
        return getSimpleIntegerValue("st");
    }

    public void setReceiveId(ObjectId receiveId){
        setSimpleValue("rid",receiveId);
    }

    public ObjectId getReceiveId(){
        return getSimpleObjecIDValue("rid");
    }

    public void setSubjectId(ObjectId subjectId){
        setSimpleValue("sid",subjectId);
    }

    public ObjectId getSubjectId(){
        return getSimpleObjecIDValue("sid");
    }

    public void setContactId(ObjectId contactId){
        setSimpleValue("cti",contactId);
    }

    public ObjectId getContactId(){
        return getSimpleObjecIDValue("cti");
    }

    public void setCommunityId(ObjectId communityId){
        setSimpleValue("cid",communityId);
    }

    public ObjectId getCommunityId(){
        return getSimpleObjecIDValue("cid");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setType(int type){
        setSimpleValue("ty",type);
    }

    public int getType(){
        return getSimpleIntegerValue("ty");
    }
}
