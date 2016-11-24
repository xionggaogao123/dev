package com.pojo.examregional;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

/**
 * 考试科目集合，依附于RegionalExamEntry
 * @author lujiang
 * 科目名称：nm
 * 科目id：id
 * 满分：fl
 * 优秀分：ex
 * 及格分：pa
 * 日期：date
 * 考试时间：time
 */
public class RegionalSubjectItem extends BaseDBObject{
	public RegionalSubjectItem(BasicDBObject baseEntry){
		super(baseEntry);
	}
	public RegionalSubjectItem(String name,ObjectId subjectId,float full,float excellent, float pass,long date,String time){
		super();
		BasicDBObject baseEntry = new BasicDBObject().append("nm", name)
				.append("id", subjectId)
				.append("fl", full)
				.append("ex", excellent)
				.append("pa", pass)
				.append("date", date)
				.append("time", time);
		setBaseEntry(baseEntry);
	}
	
	public String getName(){
		return getSimpleStringValue("nm");
	}
	public void setName(String name){
		setSimpleValue("nm",name);
	}
	public ObjectId getSubjectId(){
		return getSimpleObjecIDValue("id");
	}
	public void setSubjectId(ObjectId subjectId){
		setSimpleValue("id",subjectId);
	}
	public double getFull(){
		return getSimpleDoubleValue("fl");
	}
	public void setFull(String full){
		setSimpleValue("fl",full);
	}
	public double getExcellent(){
		return getSimpleDoubleValue("ex");
	}
	public void setExcellent(String excellent){
		setSimpleValue("ex",excellent);
	}
	public double getPass(){
		return getSimpleDoubleValue("pa");
	}
	public void setPass(String pass){
		setSimpleValue("pa",pass);
	}
	public long getDate(){
		return getSimpleLongValue("date");
	}
	public void setDate(long date){
		setSimpleValue("date",date);
	}
	public String getTime(){
		return getSimpleStringValue("time");
	}
	public void setTime(String time){
		setSimpleValue("time",time);
	}
}
