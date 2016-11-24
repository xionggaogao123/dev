package com.db.itempool;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Test;

import com.pojo.app.IdValuePair;
import com.pojo.exercise.ExerciseItemType;
import com.pojo.itempool.StudentExerciseEntry;
import com.pojo.school.SubjectType;

public class StudentPapersDaoTest {

	private StudentExerciseDao dao =new StudentExerciseDao();
	
	@Test
	public void addStudentPapersEntry()
	{
		IdValuePair p =new IdValuePair(new ObjectId(), 0);
		List<IdValuePair> list =new ArrayList<IdValuePair>();
		list.add(p);
		
		//StudentExerciseEntry e=new StudentExerciseEntry(new ObjectId(), "测试", SubjectType.MATH, list, null, null, null);
		//dao.addStudentExerciseEntry(e);
	}
	
	
	
	@Test
	public void finishOneItem()
	{
		//dao.finishOneItem(new ObjectId("5539b79a63e71269cea4b78b"), new ObjectId("5539b79a63e71269cea4b789"), true, ExerciseItemType.SINGLECHOICE);
	}
}
