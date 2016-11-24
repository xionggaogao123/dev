package com.pojo.jointexam;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

/**
 * 
 * @author Administrator
 * 科目名称：nm
 * 科目id：id
 * 满分：fl
 * 优秀学分：ex
 * 及格分：pa
 * 日期:date
 * 考试时间：time
 */
public class JointSubjectEntry extends BaseDBObject {

	
	 public JointSubjectEntry(BasicDBObject baseEntry) {
	        super(baseEntry);
	    }
	 
	 public JointSubjectEntry(String name,ObjectId  subjectId,int fullMarks ,int excellent,int pass,String date,String time){
		 super();
	        BasicDBObject baseEntry = new BasicDBObject()
	        .append("nm", name)
	        .append("id", subjectId)
	        .append("fl", fullMarks)
	        .append("ex", excellent)
	        .append("pa", pass)
	        .append("date", date)
	        .append("time", time);
	        setBaseEntry(baseEntry);
	        
	 }
	   public String getName() {
	        return getSimpleStringValue("nm");
	    }

	    public void setName(String name) {
	        setSimpleValue("nm", name);
	    }
	    public ObjectId getSubjectId() {
	    	 return getSimpleObjecIDValue("id");
	    }

	    public void setSubjectId(ObjectId subjectId) {
	        setSimpleValue("id", subjectId);
	    }
	    
	    public int getFullMarks() {
	        return getSimpleIntegerValue("fl");
	    }

	    public void setFullMarks(int fullMarks) {
	        setSimpleValue("nm", fullMarks);
	    }
	    
	    public int getExcellent() {
	        return getSimpleIntegerValue("ex");
	    }

	    public void setExcellent(int excellent) {
	        setSimpleValue("ex", excellent);
	    }
	    public int getPass() {
	        return getSimpleIntegerValue("pa");
	    }

	    public void setPass(int pass) {
	        setSimpleValue("pa", pass);
	    }
	    public String getDate() {
	        return getSimpleStringValue("date");
	    }

	    public void setDate(String date) {
	        setSimpleValue("date", date);
	    }
	    public String getTime() {
	        return getSimpleStringValue("time");
	    }

	    public void setTime(String time) {
	        setSimpleValue("nm", time);
	    }
	    
	    
}
