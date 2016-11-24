package com.pojo.examresult;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.school.ClassEntry;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**考试信息表
 * Created by fl on 2015/6/12.
 * collectionName:examresult
 * {
 *     id：考试编号
 *     name:考试名称
 *     type:考试类型(期中，期末，月考，其他)
 *     sId:考试的学校
 *     gId：考试年级
 *     cList：考试班级列表
 *     [
 *      {
 *
 *       }
 *     ]
 *     date：考试日期
 *     pList:学生成绩
 *     [
 *     {
 *         studentId:学生的id
 *         name：学生的姓名
 *         classId:班级的Id
 *         score：学生的成绩
 *         [
 *          {
 *              subjectId:考试科目
 *              subjectScore:考试成绩
 *              fullScore：满分
 *              failScore：及格分
 *              absence:缺考
 *              exemption:免考
 *           }
 *         ]
 *     }
 *     ]
 *     schY:学年
 *     isGra:考试来源（是否为年级统一考试）  0：老师自建 1：考务管理 2：考试 3：区域联考
 *     eid:对应来自exerciseEntry的id
 *     df:逻辑删除标志，0未删除、1删除
 *     sList:考试科目
 *     [
 *     {}
 *     ]
 *
 * }
 */
public class ExamResultEntry extends BaseDBObject{


    /**
	 * 
	 */
	private static final long serialVersionUID = -9108279201992379305L;
	/**
     * 构造函数
     * @param baseEntry
     */
    public ExamResultEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }
    public ExamResultEntry() {
        super();
    }

    /**
     * 构造函数
     * @param name
     * @param type
     * @param schoolId
     * @param gradeId
     * @param classList
     * @param date
     * @param performanceList
     * @param schoolYear
     * @param isGradeExam
     */
    public ExamResultEntry(String name,String type,ObjectId schoolId,ObjectId gradeId,List<ObjectId> classList,String date,
                           List<ObjectId> performanceList,String schoolYear,Integer isGradeExam, List<ObjectId> subjectList){
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("name", name)
                .append("type", type)
                .append("gId", gradeId)
                .append("sId", schoolId)
                .append("cList", MongoUtils.convert(classList))
                .append("date", date)
                .append("pList", MongoUtils.convert(performanceList))
                .append("schY", schoolYear)
                .append("isGra", isGradeExam)
                .append("sList",  MongoUtils.convert(subjectList));
        setBaseEntry(dbo);
    }

    /**
     * 构造函数
     * @param name
     * @param type
     * @param schoolId
     * @param gradeId
     * @param classList
     * @param date
     * @param schoolYear
     * @param isGradeExam
     * @param subjectList
     */
    public ExamResultEntry(String name,String type,ObjectId schoolId,ObjectId gradeId,List<ObjectId> classList,String date,
                          String schoolYear,Integer isGradeExam, List<ObjectId> subjectList){
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("name", name)
                .append("type", type)
                .append("gId", gradeId)
                .append("sId", schoolId)
                .append("cList", MongoUtils.convert(classList))
                .append("date", date)
                .append("schY", schoolYear)
                .append("isGra", isGradeExam)
                .append("sList",  MongoUtils.convert(subjectList))
                .append("df", 0);
        setBaseEntry(dbo);
    }


    public String getName() {
        return getSimpleStringValue("name");
    }

    public void setName(String type) {
        setSimpleValue("name", type);
    }

    public String getType() {
        try {
            return getSimpleStringValue("type");
        } catch(NullPointerException e) {
            return "其他";
        }

    }

    public void setType(String type) {
        setSimpleValue("type", type);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sId");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("sId", schoolId);
    }

    public ObjectId getGradeId() {
        return getSimpleObjecIDValue("gId");
    }

    public void setGradeId(ObjectId gradeId) {
        setSimpleValue("gId", gradeId);
    }

    public String getDate() {
        return getSimpleStringValue("date");
    }

    public void setDate(String date) {
        setSimpleValue("date", date);
    }

    public void setExerciseId(ObjectId eId) {
        setSimpleValue("eId", eId);
    }

    public String getSchoolYear() {
        return getSimpleStringValue("schY");
    }

    public void setSchoolYear(String schoolYear) {
        setSimpleValue("schY", schoolYear);
    }

    public Integer getIsGradeExam() {
        return getSimpleIntegerValue("isGra");
    }

    public void setIsGradeExam(Integer isGradeExam) {
        setSimpleValue("isGra", isGradeExam);
    }

    public Integer getDeleteFlag() {
        return getSimpleIntegerValue("df");
    }

    public void setDeleteFlag(Integer deleteFlag) {
        setSimpleValue("df", deleteFlag);
    }

    public List<ObjectId> getClassList() {
        List<ObjectId> retList =new ArrayList<ObjectId>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("cList");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add((ObjectId)o);
            }
        }
        return retList;
    }
    public void setClassList(List<ObjectId> classList) {
        setSimpleValue("cList", MongoUtils.convert(classList));
    }


    public List<ObjectId> getPerformanceList() {
        List<ObjectId> retList =new ArrayList<ObjectId>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("pList");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add((ObjectId)o);
            }
        }
        return retList;
    }
    public void setPerformanceList(List<ObjectId> performanceList) {
        setSimpleValue("pList", MongoUtils.convert(performanceList));
    }

    public List<ObjectId> getSubjectList() {
        List<ObjectId> retList =new ArrayList<ObjectId>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("sList");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add((ObjectId)o);
            }
        }
        return retList;
    }
    public void setSubjectList(List<ObjectId> subjectList) {
        setSimpleValue("sList", MongoUtils.convert(subjectList));
    }


}
