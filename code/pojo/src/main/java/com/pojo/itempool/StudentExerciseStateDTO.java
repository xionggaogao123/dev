package com.pojo.itempool;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.pojo.exercise.ExerciseItemType;



/**
 * 学生练习状态
 * @author fourer
 *
 */
public class StudentExerciseStateDTO {

	/**
	 * 下一个练习的题目类型
	 */
	private ExerciseItemType type;
	/**
	 * 该类型已经练习过的题目ID
	 */
	private List<ObjectId> alreadyList =new ArrayList<ObjectId>();
	
	
	public StudentExerciseStateDTO(ExerciseItemType type,
			List<ObjectId> alreadyList) {
		super();
		this.type = type;
		this.alreadyList = alreadyList;
	}
	
	
	public ExerciseItemType getType() {
		return type;
	}
	public void setType(ExerciseItemType type) {
		this.type = type;
	}
	public List<ObjectId> getAlreadyList() {
		return alreadyList;
	}
	public void setAlreadyList(List<ObjectId> alreadyList) {
		this.alreadyList = alreadyList;
	}
	
	
	
}
