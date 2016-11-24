package com.pojo.cloudlesson;

import java.io.Serializable;
/**
 * 云课程类别DTO
 * @author fourer
 *
 */
public class CloudLessonTypeDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4733598214629463118L;

	private String id;
	private int schoolType; //学校种类
	private int subjectType;//科目
	private int gradeType; //年级
	private String name; //名字
	private String des; //描述
	
	public CloudLessonTypeDTO()
	{
		
	}
	
	
	public CloudLessonTypeDTO (CloudLessonTypeEntry e)
	{
		this.id=e.getID().toString();
		this.schoolType=e.getSchoolType();
		this.subjectType=e.getSubject();
		this.gradeType=e.getCloudClassGradeType();
		this.name=e.getName();
		this.des=e.getDescription();
	}
	
	
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getSchoolType() {
		return schoolType;
	}
	public void setSchoolType(int schoolType) {
		this.schoolType = schoolType;
	}
	public int getSubjectType() {
		return subjectType;
	}
	public void setSubjectType(int subjectType) {
		this.subjectType = subjectType;
	}
	public int getGradeType() {
		return gradeType;
	}
	public void setGradeType(int cloudClassGradeType) {
		this.gradeType = cloudClassGradeType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}

}
