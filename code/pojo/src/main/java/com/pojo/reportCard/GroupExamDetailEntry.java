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
 *     recordScoreType:考分类型 1分值计算 2A率计算
 *     userId:创建人Id
 *     examName:考试名称
 *     subjectId:学科Id
 *     maxScore:满分
 *     qualifyScore:合格分
 *     excellentScore:优秀分
 *     showType:展示类型 0：个人1：全部学生
 *     status:状态 0保存（待发送） 1表示已删除 2已发送
 *     isNew:0：旧成绩单 1：新成绩单
 *     fsShowType:分数展示类型    0.分数和等第1.分数和排名2.仅分数3.仅等第
 * }
 */
public class GroupExamDetailEntry extends BaseDBObject{

    public GroupExamDetailEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public GroupExamDetailEntry(ObjectId groupId,
                                ObjectId communityId,
                                ObjectId examType,
                                int recordScoreType,
                                ObjectId userId,
                                String examName,
                                ObjectId subjectId,
                                int maxScore,
                                int qualifyScore,
                                int excellentScore,
                                long examTime,
                                int signCount,
                                int signedCount,
                                int showType,
                                int lrType,
                                int pmType
                                ){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("ms",maxScore)
                .append("qs",qualifyScore)
                .append("es",excellentScore)
                .append("gid",groupId)
                .append("uid",userId)
                .append("etp",examType)
                .append("rt",recordScoreType)
                .append("en",examName)
                .append("cmId",communityId)
                .append("et",examTime)
                .append("ti",System.currentTimeMillis())
                .append("sid",subjectId)
                .append("sc",signCount)
                .append("sec",signedCount)
                .append("st",Constant.ZERO)
                .append("sw",showType )
                .append("lrType", lrType)
                .append("pmType", pmType)
                .append("isNew",Constant.ZERO);
        setBaseEntry(basicDBObject);
    }
    
    public GroupExamDetailEntry(ObjectId groupId,
                                ObjectId communityId,
                                ObjectId examType,
                                int recordScoreType,
                                ObjectId userId,
                                String examName,
                                String subjectIds,
                                int maxScore,
                                int qualifyScore,
                                int excellentScore,
                                long examTime,
                                int signCount,
                                int signedCount,
                                int showType,
                                int lrType,
                                int pmType
                                ){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("ms",maxScore)
                .append("qs",qualifyScore)
                .append("es",excellentScore)
                .append("gid",groupId)
                .append("uid",userId)
                .append("etp",examType)
                .append("rt",recordScoreType)
                .append("en",examName)
                .append("cmId",communityId)
                .append("et",examTime)
                .append("ti",System.currentTimeMillis())
                .append("sids",subjectIds)
                .append("sc",signCount)
                .append("sec",signedCount)
                .append("st",Constant.ZERO)
                .append("sw",showType )
                .append("lrType", lrType)
                .append("pmType", pmType)
                .append("isNew",Constant.ONE);
        setBaseEntry(basicDBObject);
    }

    public void setSignedCount(int signedCount){
        setSimpleValue("sec",signedCount);
    }

    public int getSignedCount(){
        return getSimpleIntegerValue("sec");
    }

    public void setSignCount(int signCount){
        setSimpleValue("sc",signCount);
    }

    public int getSignCount(){
        return getSimpleIntegerValue("sc");
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

    public void setShowType(int showType){
        setSimpleValue("sw",showType);
    }

    public int getShowType(){
        return getSimpleIntegerValueDef("sw", Constant.ZERO);
    }
    
    public void setFsShowType(int fsShowType){
        setSimpleValue("fsw",fsShowType);
    }

    public int getFsShowType(){
        return getSimpleIntegerValueDef("fsw", Constant.ZERO);
    }
    
    public void setPmType(int pmType){
        setSimpleValue("pmType",pmType);
    }

    public int getPmType(){
        return getSimpleIntegerValueDef("pmType", Constant.ZERO);
    }
    
    public void setLrType(int lrType){
        setSimpleValue("lrType",lrType);
    }

    

    public int getLrType(){
        return getSimpleIntegerValueDef("lrType", Constant.TWO);
    }
    
    public int getIsNew(){
        return getSimpleIntegerValueDef("isNew", Constant.ZERO);
    }
    
    public void setIsNew(int isNew){
        setSimpleValue("isNew",isNew);
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

    public void setExamType(ObjectId examType){
        setSimpleValue("etp",examType);
    }

    public ObjectId getExamType(){
        return getSimpleObjecIDValue("etp");
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
