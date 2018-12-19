package com.pojo.reportCard;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;


public class MultiGroupExamDetailEntry extends BaseDBObject{

    public MultiGroupExamDetailEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public MultiGroupExamDetailEntry(ObjectId groupId,
                                List<ObjectId> communityId,
                                String communityName,
                                int recordScoreType,
                                ObjectId userId,
                                String examName,
                                String subjectId,
                                long examTime,
                                String grade
                                ){
        BasicDBObject basicDBObject=new BasicDBObject()


                .append("gid",groupId)
                .append("uid",userId)
                .append("rt",recordScoreType)
                .append("en",examName)
                .append("cmId",communityId)
                .append("cName", communityName)
                .append("et",examTime)
                .append("grade",grade)
                .append("ti",System.currentTimeMillis())
                .append("sids",subjectId)
                .append("st",Constant.ZERO)
                .append("isr",Constant.ZERO);

        setBaseEntry(basicDBObject);
    }
    
   

   

    public void setSubmitTime(long submitTime){
        setSimpleValue("ti",submitTime);
    }

    public long getSubmitTime(){
        return getSimpleLongValue("ti");
    }

    public void setExamTime(long examTime){
        setSimpleValue("et",examTime);
    }

    public long getExamTime(){
        return getSimpleLongValue("et");
    }

    public void setStatus(int status){
        setSimpleValue("st",status);
    }

    public int getStatus(){
        return getSimpleIntegerValue("st");
    }


    public void setCName(String cName){
        setSimpleValue("cName",cName);
    }

    public String getCName(){
        return getSimpleStringValue("cName");
    }
   
    public void setGrade(String grade){
        setSimpleValue("grade",grade);
    }

    public String getGrade(){
        return getSimpleStringValue("grade");
    }
  

    public void setExamName(String examName){
        setSimpleValue("en",examName);
    }

    public String getExamName(){
        return getSimpleStringValue("en");
    }
    
    public void setSubjectIds(String subjectIds){
        setSimpleValue("sids",subjectIds);
    }

    public String getSubjectIds(){
        return getSimpleStringValue("sids");
    }

    public void setRecordScoreType(int recordScoreType){
        setSimpleValue("rt",recordScoreType);
    }

    public int getRecordScoreType(){
        return getSimpleIntegerValue("rt");
    }

   

    public List<ObjectId> getCommunityId() {
        List<ObjectId> resultList = new ArrayList<ObjectId>();
        BasicDBList list = (BasicDBList) getSimpleObjectValue("cmId");
        if (null != list && !list.isEmpty()) {
            for (Object o : list) {
                resultList.add((ObjectId) o);
            }
        }
        return resultList;
    }

    public void setGroupId(ObjectId groupId){
        setSimpleValue("gid",groupId);
    }

    public ObjectId getGroupId(){
        return getSimpleObjecIDValue("gid");
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

}
