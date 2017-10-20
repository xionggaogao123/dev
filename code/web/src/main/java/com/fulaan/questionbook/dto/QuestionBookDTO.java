package com.fulaan.questionbook.dto;

import com.pojo.questionbook.QuestionBookEntry;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/9/30.
 */
public class QuestionBookDTO {
    private String id;
    private String description;
    private String ename;
    private List<String> imageList = new ArrayList<String>();
    private String userId;
    private String gradeId;
    private String gradeName;
    private String subjectId;
    private String subjectName;
    private String questionTypeId;
    private String questionTypeName;
    private String testId;
    private String testName;
    private List<String> pointList;
    private String dateTime;
    private String pageTime;
    private int level;
    private int type;
    private List<QuestionAdditionDTO> jxList = new ArrayList<QuestionAdditionDTO>();
    private List<QuestionAdditionDTO> daList = new ArrayList<QuestionAdditionDTO>();
    private List<QuestionAdditionDTO> wdList = new ArrayList<QuestionAdditionDTO>();

    public QuestionBookDTO(){

    }
    public QuestionBookDTO(QuestionBookEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.description = e.getDescription();
            this.imageList = e.getImageList();
            this.userId = e.getUserId() == null ? "" : e.getUserId().toString();
            this.gradeId = e.getGradeId() == null ? "" : e.getGradeId().toString();
            this.gradeName = e.getGradeName();
            this.subjectId = e.getSubjectId() == null ? "" : e.getSubjectId().toString();
            this.subjectName = e.getSubjectName();
            this.questionTypeId = e.getQuestionTypeId() == null ? "" : e.getQuestionTypeId().toString();
            this.questionTypeName = e.getQuestionTypeName();
            this.testId = e.getTestId() == null ? "" : e.getTestId().toString();
            this.testName = e.getTestName();
            this.type = e.getType();
            this.level = e.getLevel();
            if(e.getDateTime()!=0l){
                this.dateTime = DateTimeUtils.getLongToStrTimeTwo(e.getDateTime());
            }else{
                this.dateTime = "";
            }
            List<ObjectId> pointIdList = e.getPointList();
            if(pointIdList != null
                    && !pointIdList.isEmpty()) {
                pointList = new ArrayList<String>();
                for(ObjectId objId : pointIdList) {
                    pointList.add(objId.toString());
                }
            }
            if(e.getPageTime()!=0l){
                this.pageTime = DateTimeUtils.getLongToStrTimeTwo(e.getPageTime());
            }else{
                this.pageTime = "";
            }
        }else{
            new QuestionBookDTO();
        }
    }

    public QuestionBookEntry buildAddEntry(){
        ObjectId uId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            uId=new ObjectId(this.getUserId());
        }
        ObjectId gId=null;
        if(this.getGradeId()!=null&&!"".equals(this.getGradeId())){
            gId=new ObjectId(this.getGradeId());
        }
        ObjectId sId=null;
        if(this.getSubjectId()!=null&&!"".equals(this.getSubjectId())){
            sId=new ObjectId(this.getSubjectId());
        }
        ObjectId qId=null;
        if(this.getQuestionTypeId()!=null&&!"".equals(this.getQuestionTypeId())){
            qId=new ObjectId(this.getQuestionTypeId());
        }
        ObjectId tId=null;
        if(this.getTestId()!=null&&!"".equals(this.getTestId())){
           tId=new ObjectId(this.getTestId());
        }

        List<ObjectId> pointIdList = new ArrayList<ObjectId>();
        if(pointList != null) {
            for(String str : pointList) {
                pointIdList.add(new ObjectId(str));
            }
        }
        long dTm = 0l;
        if(this.getDateTime() != null && this.getDateTime() != ""){
            dTm = DateTimeUtils.getStrToLongTime(this.getDateTime());
        }
        long pTm = 0l;
        if(this.getPageTime() != null && this.getPageTime() != ""){
            pTm = DateTimeUtils.getStrToLongTime(this.getPageTime());
        }
        QuestionBookEntry openEntry =
                new QuestionBookEntry(
                        this.description,
                        this.imageList,
                        uId,
                        gId,
                        this.gradeName,
                        sId,
                        this.subjectName,
                        qId,
                        this.questionTypeName,
                        tId,
                        this.testName,
                        pointIdList,
                        dTm,
                        pTm,
                        this.level,
                        this.type
                );
        return openEntry;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public List<QuestionAdditionDTO> getJxList() {
        return jxList;
    }

    public void setJxList(List<QuestionAdditionDTO> jxList) {
        this.jxList = jxList;
    }

    public List<QuestionAdditionDTO> getDaList() {
        return daList;
    }

    public void setDaList(List<QuestionAdditionDTO> daList) {
        this.daList = daList;
    }

    public List<QuestionAdditionDTO> getWdList() {
        return wdList;
    }

    public void setWdList(List<QuestionAdditionDTO> wdList) {
        this.wdList = wdList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getQuestionTypeId() {
        return questionTypeId;
    }

    public void setQuestionTypeId(String questionTypeId) {
        this.questionTypeId = questionTypeId;
    }

    public String getQuestionTypeName() {
        return questionTypeName;
    }

    public void setQuestionTypeName(String questionTypeName) {
        this.questionTypeName = questionTypeName;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public List<String> getPointList() {
        return pointList;
    }

    public void setPointList(List<String> pointList) {
        this.pointList = pointList;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getPageTime() {
        return pageTime;
    }

    public void setPageTime(String pageTime) {
        this.pageTime = pageTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
