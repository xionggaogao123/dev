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

/**
 * Created by scott on 2017/9/29.
 * {
 *     groupId:群组Id
 *     communityId:社区Id
 *     examType:考试类型
 *     recordScoreType:考分类型
 *     userId:创建人Id
 *     examName:考试名称
 *     subjectId:学科Id
 *     maxScore:满分
 *     qualifyScore:合格分
 *     excellentScore:优秀分
 *     status:状态 0副本状态 1保存（待发送） 2已发送
 * }
 */
public class GroupExamDetailEntry extends BaseDBObject{

    public GroupExamDetailEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public GroupExamDetailEntry(ObjectId groupId,
                                ObjectId communityId,
                                int examType,
                                int recordScoreType,
                                ObjectId userId,
                                String examName,
                                ObjectId subjectId,
                                int maxScore,
                                int qualifyScore,
                                int excellentScore,
                                long examTime
                                ){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("ms",maxScore)
                .append("qs",qualifyScore)
                .append("es",excellentScore)
                .append("gid",groupId)
                .append("uid",userId)
                .append("et",examType)
                .append("rt",recordScoreType)
                .append("en",examName)
                .append("cmId",communityId)
                .append("et",examTime)
                .append("ti",System.currentTimeMillis())
                .append("sid",subjectId)
                .append("st",Constant.ZERO)
                .append("ir", Constant.ZERO);
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

    public void setExcellentScore(int excellentScore){
        setSimpleValue("es",excellentScore);
    }

    public int getExcellentScore(){
        return getSimpleIntegerValue("es");
    }

    public void setQualifyScore(int qualifyScore){
        setSimpleValue("qs",qualifyScore);
    }

    public int getQualifyScore(){
        return getSimpleIntegerValue("qs");
    }

    public void setMaxScore(int maxScore){
        setSimpleValue("ms",maxScore);
    }

    public int getMaxScore(){
        return getSimpleIntegerValue("ms");
    }

    public void setExamName(String examName){
        setSimpleValue("en",examName);
    }

    public String getExamName(){
        return getSimpleStringValue("en");
    }

    public void setRecordScoreType(int recordScoreType){
        setSimpleValue("rt",recordScoreType);
    }

    public int getRecordScoreType(){
        return getSimpleIntegerValue("rt");
    }

    public void setExamType(int examType){
        setSimpleValue("et",examType);
    }

    public int getExamType(){
        return getSimpleIntegerValue("et");
    }

    public void setCommunityId(ObjectId communityId){
        setSimpleValue("cmId",communityId);
    }

    public ObjectId getCommunityId(){
        return getSimpleObjecIDValue("cmId");
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

    public ObjectId getSubjectId(){
        return getSimpleObjecIDValue("sid");
    }

    public void setSubjectId(ObjectId subjectId){
        setSimpleValue("sid",subjectId);
    }
}
