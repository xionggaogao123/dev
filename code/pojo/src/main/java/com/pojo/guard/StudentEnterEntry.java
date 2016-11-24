package com.pojo.guard;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;

/**
 * 
 * @author chengwei@ycode.cn
 * 2015-12-07
 * 
 * 进校Entry类
 * 学生id: sid(studentId)
 * 姓名: sn(studentName)
 * 年级: gd(grade)
 * 班级: cr(classroom)
 * 进校时间: et(enterTime)
 * 进校事由: ers(enterReasons)
 * 是否删除: ir(isRemoved,0为未删除，1为已删除)

 */
public class StudentEnterEntry extends BaseDBObject{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2033839649544307116L;

	/**
	 * 构造函数
	 * @param baseEntry
	 */
	public StudentEnterEntry(BasicDBObject baseEntry){
		super(baseEntry);
	}
	
	public StudentEnterEntry(ObjectId studentId, String studentName, String grade, String classroom, String enterTime, String enterReasons){
		super();
		
		BasicDBObject baseEntry = new BasicDBObject()
		.append("sn", studentName)
		.append("gd", grade)
		.append("cr", classroom)
		.append("et", enterTime)
		.append("ers", enterReasons)
		.append("ir", Constant.ZERO);
		
		setBaseEntry(baseEntry);
	}
	
	/**
	 * set/get方法
	 * @return
	 */
	public String getStudentName(){
		return getSimpleStringValue("sn");
	}
	
	public void setStudentName(String studentName){
		setSimpleValue("sn", studentName);
	}
	
	public String getGrade(){
		return getSimpleStringValue("gd");
	}
	
	public void setGrade(String grade){
		setSimpleValue("gd", grade);
	}
	
	public String getClassroom(){
		return getSimpleStringValue("cr");
	}
	
	public void setClassroom(String classroom){
		setSimpleValue("cs", classroom);
	}
	
	public String getEntryTime(){
		return getSimpleStringValue("et");
	}
	
	public void setEntryTime(String enterTime){
		setSimpleValue("et", enterTime);
	}
	
	public String getEntryReasons(){
		return getSimpleStringValue("ers");
	}
	
	public void setEntryReasons(String enterReasons){
		setSimpleValue("ers", enterReasons);
	}
	
	//默认未删除
	public int getIsRemove(){
		return getSimpleIntegerValue("ir");
	}
	
	public void setIsRemove(Integer isRemove){
		setSimpleValue("ir", isRemove);
	}
	
	

}
























