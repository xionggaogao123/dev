package com.db.school;

import org.bson.types.ObjectId;
import org.junit.Test;

import com.pojo.app.IdValuePair;
import com.pojo.school.TeacherClassSubjectEntry;

public class TeacherClassLessonDaoTest {

	private TeacherClassSubjectDao dao =new TeacherClassSubjectDao();
	
	@Test
	public void addTeacherClassLessonEntry()
	{
		IdValuePair idv=	new IdValuePair(new ObjectId("550aa605fe5bb2c3438568b9"), "科学");
		
		
//		
//		TeacherClassLessonEntry e =new TeacherClassLessonEntry(
//				new ObjectId("550aa605fe5bb2c3438568ba"), 
//				idv,idv,idv
//								);
//		dao.addTeacherClassLessonEntry(e);
	}
	
	@Test
	public void getClassLessonSet()
	{
		dao.getClassLessonSet(new ObjectId("5523e4cdf6f28cb76f18df72"));
	}
}
