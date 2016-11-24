package com.pojo.questionnaire;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by qinbo on 15/3/6.
 *  调查问卷信息表
 * <pre>
 * collectionName:surveys
 * </pre>
 * <pre>
 * {
 *  nm:标题
 *  pb:发布人id
 *  pbt:发布时间,long
 *  edt:结束时间,long
 *  cls[]:班级id,空表示全校
 *  parr:家长是否回应0,1
 *  stur:学生是否回应0,1
 *  tear:教师是否回应0,1
 *  hear:校领导是否回应0,1
 *  docurl:问卷地址
 *  ans[]: answerSheet 答题卡设置，大于0表示选择题的选项个数，等于0为问答题，小于0表示多选题，增加了一个打分题，大于10000做为打分题，其中1后面跟的4位数为最小分，再4位数是最大分。分值范围在0-9999
 *  res[]:回应结果 , 单选题就是一个数字，多选题是数字列表，问答题为字符串，打分题是一个大于10000的整数，减10000即为打分分数
 *  ip：isPublic 是否公开 0：不公开， 1：公开， 默认0
 *  ipf: isPlatform 是否全平台 0：非， 1：全平台   默认0
 *  tn: totalNumber 应该参与问卷的总人数   新建问卷时本字段为0，/questionnaire/list.do查询时计算保存（2016.4.18）
 * }
 * </pre>
 */
public class QuestionnaireEntry extends BaseDBObject {

    private static final long serialVersionUID = -7845214572653496369L;

    public QuestionnaireEntry(DBObject dbo) {
        this((BasicDBObject)dbo);
    }


    public QuestionnaireEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    /**
     * 构造器
     *
     */
    public QuestionnaireEntry(String name, ObjectId publisherId, long publishTime, long endTime,
                              ObjectId schoolId, List<ObjectId> classIds, int parentRespondent,
                              int studentRespondent, int teacherRespondent, int headmasterRespondent, String docUrl,
                              List<Integer> answerSheet, Map<String, List<Object>> respondent, int isPublic, int isPlatform){
        super();

        BasicDBObject dbo = new BasicDBObject()
                .append("nm",name)
                .append("pb",publisherId)
                .append("pbt",publishTime)
                .append("edt",endTime)
                .append("sid",schoolId)
                .append("cls",classIds)
                .append("parr",parentRespondent)
                .append("stur",studentRespondent)
                .append("tear",teacherRespondent)
                .append("hear",headmasterRespondent)
                .append("docurl", docUrl)
                .append("ans",answerSheet)
                .append("res",respondent)
                .append("ip", isPublic)
                .append("ipf", isPlatform)
                .append("tn", 0)
                ;
        setBaseEntry(dbo);

    }

    public String getName() {
        return getSimpleStringValue("nm");
    }
    public void setName(String name) {
        setSimpleValue("nm", name);
    }

    public ObjectId getPublisherId() {return getSimpleObjecIDValue("pb");}
    public void setPublisherId(ObjectId publisherId) {setSimpleValue("pb", publisherId);}

    public long getPublishTime() {return getSimpleLongValue("pbt");}
    public void setPublishTime(long publishTime) {setSimpleValue("pbt",publishTime);}

    public long getEndTime() {return getSimpleLongValue("edt");}
    public void setEndTime(long endTime) {setSimpleValue("edt",endTime);}

    public ObjectId getSchoolId() {return getSimpleObjecIDValue("sid");}
    public void setSchoolId(ObjectId schoolId) {setSimpleValue("sid", schoolId);}

    public List<String> getClassIds() {
        List<String> classes = null;
        List list =(List)getSimpleObjectValue("cls");
        if(null!=list && !list.isEmpty())
        {
            classes =new ArrayList<String>();
            for(Object o:list)
            {
                classes.add(o.toString());
            }
        }
        return classes;
    }
    public void setClassIds(List<String> classIds) {
       

        setSimpleValue("cls", MongoUtils.convert(classIds));
    }

    public int getStudentRespondent() {return getSimpleIntegerValue("stur");}
    public void setStudentRespondent(int respondent) {setSimpleValue("stur", respondent);}

    public int getTeacherRespondent() {return getSimpleIntegerValue("tear");}
    public void setTeacherRespondent(int respondent) {setSimpleValue("tear", respondent);}

    public int getParentRespondent() {return getSimpleIntegerValue("parr");}
    public void setParentRespondent(int respondent) {setSimpleValue("parr", respondent);}

    public int getHeadmasterRespondent(){
        return getSimpleIntegerValueDef("hear", 0);
    }

    public void setHeadmasterRespondent(int respondent){
        setSimpleValue("hear", respondent);
    }

    public String getDocUrl() {
        return getSimpleStringValue("docurl");
    }
    public void setDocUrl(String docUrl) {
        setSimpleValue("docurl", docUrl);
    }


    public List<Integer> getAnswerSheet() {
        return (List<Integer>)getSimpleObjectValue("ans");
    }
    public void setAnswerSheet(List<Integer> answerSheet){
        setSimpleValue("ans", MongoUtils.convert(answerSheet));
    }

    public Map<String,List<Object>> getRespondent(){
        return (Map<String,List<Object>> )getSimpleObjectValue("res");
    }

    public void setRespondent(Map<String,List<Object>> respondent){
        setSimpleValue("res", respondent);
    }

    public int getIsPublic(){
        return getSimpleIntegerValueDef("ip", 0);
    }

    public void setIsPublic(int isPublic){
        setSimpleValue("ip", isPublic);
    }

    public int getIsPlatform(){
        return getSimpleIntegerValueDef("ipf", 0);
    }

    public void setIsPlatform(int isPlatform){
        setSimpleValue("ipf", isPlatform);
    }

    public int getTotalNumber(){
        return getSimpleIntegerValueDef("tn", 0);
    }

    public void setTotalNumber(int totalNumber){
        setSimpleValue("tn", totalNumber);
    }
}
