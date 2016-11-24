package com.pojo.itempool;

import org.bson.types.ObjectId;

import com.pojo.school.SubjectType;
import com.sys.utils.DateTimeUtils;

/**
 * 学生练习DTO
 * @author fourer
 *
 */
public class StudeneExerciseDTO {

	private ObjectId id;
	private String name;
	private String subjectName;
	private int totalItem; //总题目个数
	private int alreadyFinish; //已经完成的个数
	private int right;//正确个数
	private String time;
	private int state;
	
	public StudeneExerciseDTO(StudentExerciseEntry e,String subject)
	{
		this.id=e.getID();
		this.name=e.getName();
		this.subjectName=SubjectType.getSubjectType(e.getSubjectType()).getName();
		this.totalItem=e.getTotalCount();
		this.alreadyFinish=e.getFinishCount();
		this.right=e.getRightCount();
		this.time=DateTimeUtils.convert(e.getTime(),DateTimeUtils.DATE_YYYY_MM_DD_HH_MM);
		this.state=e.getState();
	}
	
	
	public String getIdStr() {
		if(null!=this.id)
			return this.id.toString();
		return "";
	}
	public void setIdStr(String idStr) {
		
	}
	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public int getTotalItem() {
		return totalItem;
	}
	public void setTotalItem(int totalItem) {
		this.totalItem = totalItem;
	}
	public int getAlreadyFinish() {
		return alreadyFinish;
	}
	public void setAlreadyFinish(int alreadyFinish) {
		this.alreadyFinish = alreadyFinish;
	}
	public int getRight() {
		return right;
	}
	public void setRight(int right) {
		this.right = right;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
}
