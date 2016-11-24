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
 * 进出校学生Entry类
 * 学生id:sid(studentId)
 * 姓名:sn(studentName)
 * 年级:gd(grade)
 * 班级:cr(classroom)
 * 出校时间:ot(outTime)
 * 出校事由:ors(outReasons)
 * 批准老师:apt(approveTeacher)
 * 是否删除:ir(isRemoved,0为未删除，1为已删除)

 */
public class StudentOutEntry extends BaseDBObject{
	
	/**
	 * 构造函数
	 * @param baseEntry
	 */
	public StudentOutEntry(BasicDBObject baseEntry){
		super(baseEntry);
	}
	public StudentOutEntry(){
		super();
	}
	
	public StudentOutEntry(ObjectId studentId, String studentName, String grade,String classroom, String outTime,
			String outReasons, String approveTeacher){
		super();
		
		BasicDBObject baseEntry = new BasicDBObject()
		.append("sn", studentName)
		.append("gd", grade)
		.append("cs", classroom)
		.append("ot", outTime)
		.append("ors", outReasons)
		.append("apt", approveTeacher)
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
		return getSimpleStringValue("cs");
	}
	
	public void setClassroom(String classroom){
		setSimpleValue("cs", classroom);
	}
	
	public String getOutTime(){
		return getSimpleStringValue("ot");
	}
	
	public void setOutTime(String outTime){
		setSimpleValue("ot", outTime);
	}
	
	public String getOutReasons(){
		return getSimpleStringValue("ors");
	}
	
	public void setOutReasons(String outReasons){
		setSimpleValue("ors", outReasons);
	}
	
	public String getApproveTeacher(){
		return getSimpleStringValue("atr");
	}
	
	public void setApproveTeacher(String approveTeacher){
		setSimpleValue("atr", approveTeacher);
	}
	
	//默认未删除
	public int getIsRemove(){
		return getSimpleIntegerValue("ir");
	}
	
	public void setIsRemove(Integer isRemove){
		setSimpleValue("ir", isRemove);
	}
	
	
}
























