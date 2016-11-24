package com.pojo.examregional;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

/**
 * Created by Huanxl on 2015/10/27.
 * 科目详情信息，依附于ExamSummaryEntry
 *	科目id:sjid
 *	科目名称:sjnm
 *	平均分:as
 *	最高分:max
 *	最低分:min
 *	及格人数:pn
 *	及格率:pr
 *	优秀人数:en
 *	优秀率:er
 *  综合分数:cs
 *	综合排名:cr
 */
public class SubjectDetails extends BaseDBObject  implements Comparable{
	
    public SubjectDetails(BasicDBObject baseEntry){
    	super(baseEntry);
    }
    
    public SubjectDetails(ObjectId subjectId, String subjectName, double averageScore, double maxScore,
    		double minScore, int passNumber, double passRate, int excellentNumber, double excellentRate, 
    		double compositeScores, int compositeRanking){
    	BasicDBObject baseEntry=new BasicDBObject()
    	.append("sjid", subjectId)
    	.append("sjnm", subjectName)
    	.append("as", averageScore)
    	.append("max", maxScore)
    	.append("min", minScore)
    	.append("pn", passNumber)
    	.append("pr", passRate)
    	.append("en", excellentNumber)
    	.append("er", excellentRate)
    	.append("cs", compositeScores)
    	.append("cr", compositeRanking);
    	setBaseEntry(baseEntry);
    }
    
    public SubjectDetails(ObjectId subjectId, String subjectName){
    	BasicDBObject baseEntry=new BasicDBObject()
    	.append("sjid", subjectId)
    	.append("sjnm", subjectName)
    	.append("as", 0.0)
    	.append("max", 0.0)
    	.append("min", 0.0)
    	.append("pn", 0)
    	.append("pr", 0.0)
    	.append("en", 0)
    	.append("er", 0.0)
    	.append("cs", 0.0)
    	.append("cr", 0);
    	setBaseEntry(baseEntry);
    }
    
    public ObjectId getSubjectId(){
       return getSimpleObjecIDValue("sjid");
    }
    
    public void setSubjectId(ObjectId subjectId){
       setSimpleValue("sjid",subjectId);
    }
    
    public String getSubjectName(){
       return getSimpleStringValue("sjnm");
    }
    
    public void setSubjectName(String subjectName){
       setSimpleValue("sjnm",subjectName);
    }
    
    public double getAverageScore(){
       return getSimpleDoubleValue("as");
    }
    
    public void setAverageScore(double averageScore){
       setSimpleValue("as",averageScore);
    }
    
    public double getMaxScore(){
       return getSimpleDoubleValue("max");
    }
    
    public void setMaxScore(double maxScore){
       setSimpleValue("max",maxScore);
    }
    
    public double getMinScore(){
       return getSimpleDoubleValue("min");
    }
     
    public void setMinScore(double minScore){
       setSimpleValue("min",minScore);
    }
     
    public int getPassNumber(){
  	   return getSimpleIntegerValue("pn");
    }
     
    public void setPassNumber(int passNumber){
  	   setSimpleValue("pn",passNumber);
    }
     
    public double getPassRate(){
  	   return getSimpleDoubleValue("pr");
    }
     
    public void setPassRate(double passRate){
  	   setSimpleValue("pr",passRate);
    }
    
    public int getExcellentNumber(){
   	   return getSimpleIntegerValue("en");
    }
      
    public void setExcellentNumber(int excellentNumber){
   	   setSimpleValue("en",excellentNumber);
    }
    
    public double getExcellentRate(){
  	   return getSimpleDoubleValue("er");
    }
     
    public void setExcellentRate(double excellentRate){
  	   setSimpleValue("er",excellentRate);
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

    @Override
    public int compareTo(Object o) {
        SubjectDetails other = (SubjectDetails)o;
        Double thisScore = getCompositeScores();
        Double otherScore = other.getCompositeScores();
        return thisScore.compareTo(otherScore);
    }
}
