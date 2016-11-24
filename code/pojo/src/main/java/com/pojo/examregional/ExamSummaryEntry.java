package com.pojo.examregional;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

/**
 * Created by Huanxl on 2015/10/27.
 * 区域联考汇总表Entry
 * 对应数据库集合 Constant.COLLECTION_EXAM_SUMMARY="examsummary"
 * 区域联考:aid
 * 学校id:sid
 * 学校名:snm
 * 学生总数:stn
 * 综合分数:cs
 * 综合排名:cr
 * 科目详情:sci
 */
public class ExamSummaryEntry extends BaseDBObject implements Comparable{
	
   public ExamSummaryEntry(BasicDBObject baseEntry){
	   super(baseEntry);
   }
   
   public ExamSummaryEntry(ObjectId areaExamId, ObjectId schoolId, String schoolName, int studentNumber,
		   double compositeScores, int compositeRanking, List<SubjectDetails> subjectDetails,int isDelete){
	   super();
	   isDelete=Constant.ZERO;
	   BasicDBObject baseEntry=new BasicDBObject()
	   .append("aid", areaExamId)
	   .append("sid", schoolId)
	   .append("snm", schoolName)
	   .append("stn", studentNumber)
	   .append("cs", compositeScores)
	   .append("cr", compositeRanking)
	   .append("isd",isDelete)
	   .append("sci", MongoUtils.convert(MongoUtils.fetchDBObjectList(subjectDetails)));
	   setBaseEntry(baseEntry);
   }
   public int getIsDelete() {
       return getSimpleIntegerValue("isd");
   }

   public void setIsdelete(int isDelete) {
       setSimpleValue("isd", isDelete);
   }
   
   public ObjectId getAreaExamId() {
       return getSimpleObjecIDValue("aid");
   }

   public void setAreaExamId(ObjectId areaExamId) {
       setSimpleValue("aid", areaExamId);
   }
   
   public ObjectId getSchoolId() {
       return getSimpleObjecIDValue("sid");
   }

   public void setSchoolId(ObjectId schoolId) {
       setSimpleValue("sid", schoolId);
   }

   public String getSchoolName() {
       return getSimpleStringValue("snm");
   }

   public void setSchoolName(String schoolName) {
       setSimpleValue("snm", schoolName);
   }
   
   public int getStudentNumber() {
       return getSimpleIntegerValue("stn");
   }

   public void setStudentNumber(int studentNumber) {
       setSimpleValue("stn", studentNumber);
   }

   public double getCompositeScores(){
	   return getSimpleDoubleValue("cs");
   }
   
   public void setCompositeScores(double compositeScores){
	   setSimpleValue("cs",compositeScores);
   }
   
   public int getCompositeRanking(){
	   return getSimpleIntegerValue("cr");
   }
   
   public void setCompositeRanking(int compositeRanking){
	   setSimpleValue("cr",compositeRanking);
   }
   
   public List<SubjectDetails> getSubjectDetails() {
	   List<SubjectDetails> resultList = new ArrayList<SubjectDetails>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("sci");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				resultList.add(new SubjectDetails((BasicDBObject)o));
			}
		}
		return resultList;
   }

   public void setSubjectDetails(List<SubjectDetails> subjectDetails) {
	   List<DBObject> list = MongoUtils.fetchDBObjectList(subjectDetails);
       setSimpleValue("sci", MongoUtils.convert(list));
   }

    @Override
    public int compareTo(Object o) {
        ExamSummaryEntry other = (ExamSummaryEntry)o;
        Double thisScore = getCompositeScores();
        Double otherScore = other.getCompositeScores();
        return thisScore.compareTo(otherScore);
    }
}
