package com.pojo.exercise;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.pojo.app.IdValuePairDTO;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;

/**
 * 测试，比如考试等
 * @author fourer
 *
 */
public class ExerciseDTO {

	
	private String id;
	/**
	 * 文档状态  //0没有答题 1已经答题 ，等待批改 2 老师已经给出分数
	 * 学生是否已经答题
	 */
	private int state;
	private String name;
	//已经推送的班级
	private List<IdValuePairDTO> alreadClasses= new ArrayList<IdValuePairDTO>();
	//没有推送的班级
	private List<IdValuePairDTO> classes= new ArrayList<IdValuePairDTO>();
	private String time;
	private int submitStudent;
	private int totalStudent;
	
	private int isFinish;
	private int isGoods=0; //默认不是优秀 
	
	public ExerciseDTO(ExerciseEntry e)
	{
		this.id=e.getID().toString();
		this.name=e.getName();
		this.time=DateTimeUtils.convert(e.getLastUpdateTime(),DateTimeUtils.DATE_YYYY_MM_DD);
		this.submitStudent=e.getSubmitStudents().size();
		//this.state=e.getState();
		for(ObjectId id:e.getClassIds())
		{
			alreadClasses.add(new IdValuePairDTO(id,Constant.EMPTY));
		}
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<IdValuePairDTO> getAlreadClasses() {
		return alreadClasses;
	}
	public void setAlreadClasses(List<IdValuePairDTO> alreadClasses) {
		this.alreadClasses = alreadClasses;
	}
	public List<IdValuePairDTO> getClasses() {
		return classes;
	}
	public void setClasses(List<IdValuePairDTO> classes) {
		this.classes = classes;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getSubmitStudent() {
		return submitStudent;
	}
	public void setSubmitStudent(int submitStudent) {
		this.submitStudent = submitStudent;
	}
	public int getTotalStudent() {
		return totalStudent;
	}
	public void setTotalStudent(int totalStudent) {
		this.totalStudent = totalStudent;
	}
	
	public void addIdValuePairDTO(IdValuePairDTO dto)
	{
		classes.add(dto);
	}
	public int getIsFinish() {
		return isFinish;
	}
	public void setIsFinish(int isFinish) {
		this.isFinish = isFinish;
	}
	public int getIsGoods() {
		return isGoods;
	}
	public void setIsGoods(int isGoods) {
		this.isGoods = isGoods;
	}
	
	
	
}
