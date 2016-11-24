package com.db.school;

import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Test;

import com.mongodb.DBObject;
import com.pojo.exercise.ExerciseMixItem;
import com.sys.constants.Constant;

public class ExerciseItemDaoTest {

	private ExerciseItemDao dao =new ExerciseItemDao();
	
	//550a7fad8cf404c144e3f2dc
	//555220cd8cf404c144e413ac
	@Test
	public void getExerciseItemEntry()
	{
		ExerciseMixItem e=dao.getExerciseItemEntry(new ObjectId("550a7fad8cf404c144e3f2dc"), new ObjectId("555220cd8cf404c144e413ac"));
		System.out.println(e);
	}
	
	@Test
	public void getExerciseMixItems()
	{
		List<ExerciseMixItem> list=dao.getExerciseMixItems(new ObjectId("550a7fad8cf404c144e3f2dc"),Constant.FIELDS);
		System.out.println(list);
	}
}
