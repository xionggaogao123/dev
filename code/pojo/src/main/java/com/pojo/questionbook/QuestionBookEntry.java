package com.pojo.questionbook;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 问题本(错题本2.0)
 * Created by James on 2017/9/30.
 * id
 description                   题目描述		      dec
 imageList                     图片list           img
 userId                         用户id            uid
 gradeId        				年级id            gid
 gradeName					    年级名称          gnm
 subjectId						学科id			  sid
 subjectName					学科名称	      snm
 questionTypeId					问题类型		  qid
 questionTypeName				问题类型名称	  qnm
 testId						    测试类型id		  tid
 testName						测试类型名称      tnm
 type 				1：未学会，2：已学会）        typ
 dateTime                      创建时间            dtm
 pageTime                      展示日期             ptm
 level                    阶段（1.转1   2转7  3转30） lev
 pointList						知识点列表        plt

 */
public class QuestionBookEntry  extends BaseDBObject {
    public QuestionBookEntry(){

    }
    public QuestionBookEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }
    //添加构造
    public QuestionBookEntry(
            String description,
            List<String> imageList,
            ObjectId userId,
            ObjectId gradeId,
            String gradeName,
            ObjectId subjectId,
            String subjectName,
            ObjectId questionTypeId,
            String questionTypeName,
            ObjectId testId,
            String testName,
            List<ObjectId> pointList,
            long dateTime,
            long pageTime,
            int level,
            int type
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("dec",description)
                .append("img", imageList)
                .append("uid",userId)
                .append("gid", gradeId)
                .append("gnm",gradeName)
                .append("sid",subjectId)
                .append("snm",subjectName)
                .append("qid",questionTypeId)
                .append("qnm",questionTypeName)
                .append("tid",testId)
                .append("tnm",testName)
                .append("plt",pointList)
                .append("dtm",dateTime)
                .append("ptm",pageTime)
                .append("lev",level)
                .append("typ",type)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public QuestionBookEntry(
            ObjectId id,
            String description,
            List<String> imageList,
            ObjectId userId,
            ObjectId gradeId,
            String gradeName,
            ObjectId subjectId,
            String subjectName,
            ObjectId questionTypeId,
            String questionTypeName,
            ObjectId testId,
            String testName,
            List<ObjectId> pointList,
            long dateTime,
            long pageTime,
            int level,
            int type
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID,id)
                .append("dec", description)
                .append("img", imageList)
                .append("uid",userId)
                .append("gid", gradeId)
                .append("gnm",gradeName)
                .append("sid",subjectId)
                .append("snm",subjectName)
                .append("qid",questionTypeId)
                .append("qnm", questionTypeName)
                .append("tid",testId)
                .append("tnm",testName)
                .append("plt",pointList)
                .append("dtm",dateTime)
                .append("ptm",pageTime)
                .append("lev", level)
                .append("typ", type)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }
    public String getDescription(){
        return getSimpleStringValue("dec");
    }

    public void setDescription(String description){
        setSimpleValue("dec",description);
    }

    public void setImageList(List<String> imageList){
        setSimpleValue("img",imageList);
    }

    public List<String> getImageList(){
        @SuppressWarnings("rawtypes")
        List imageList =(List)getSimpleObjectValue("img");
        return imageList;
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public ObjectId getGradeId(){
        return getSimpleObjecIDValue("gid");
    }

    public void setGradeId(ObjectId gradeId){
        setSimpleValue("gid",gradeId);
    }

    public String getGradeName(){
        return getSimpleStringValue("gnm");
    }

    public void setGradeName(String gradeName){
        setSimpleValue("gnm",gradeName);
    }

    public ObjectId getSubjectId(){
        return getSimpleObjecIDValue("sid");
    }

    public void setSubjectId(ObjectId subjectId){
        setSimpleValue("sid",subjectId);
    }

    public String getSubjectName(){
        return getSimpleStringValue("snm");
    }

    public void setSubjectName(String subjectName){
        setSimpleValue("snm",subjectName);
    }

    public ObjectId getQuestionTypeId(){
        return getSimpleObjecIDValue("qid");
    }

    public void setQuestionTypeId(ObjectId questionTypeId){
        setSimpleValue("qid",questionTypeId);
    }

    public String getQuestionTypeName(){
        return getSimpleStringValue("qnm");
    }

    public void setQuestionTypeName(String questionTypeName){
        setSimpleValue("qnm",questionTypeName);
    }

    public ObjectId getTestId(){
        return getSimpleObjecIDValue("tid");
    }

    public void setTestId(ObjectId testId){
        setSimpleValue("tid",testId);
    }

    public String getTestName(){
        return getSimpleStringValue("tnm");
    }

    public void setTestName(String testName){
        setSimpleValue("tnm",testName);
    }

    public List<ObjectId> getPointList() {

        List<ObjectId> objectIdList = new ArrayList<ObjectId>();

        BasicDBList dbList = (BasicDBList) getSimpleObjectValue("plt");
        if(dbList != null
                && !dbList.isEmpty()) {
            for(Object obj : dbList) {
                objectIdList.add((ObjectId) obj);
            }
        }

        return objectIdList;
    }

    public void setPointList(List<ObjectId> pointList) {
        setSimpleValue("plt", pointList);
    }



    public long getDateTime(){
        return getSimpleLongValue("dtm");
    }
    public void setDateTime(long dateTime){
        setSimpleValue("dtm",dateTime);
    }
    public long getPageTime(){
        return getSimpleLongValue("ptm");
    }
    public void setPageTime(long pageTime){
        setSimpleValue("ptm",pageTime);
    }

    public int getType(){
        return getSimpleIntegerValue("typ");
    }

    public void setType(int type){
        setSimpleValue("typ",type);
    }
    public int getLevel(){
        return getSimpleIntegerValue("lev");
    }

    public void setLevel(int level){
        setSimpleValue("lev",level);
    }
    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }
}
