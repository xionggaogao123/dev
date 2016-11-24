package com.db.lesson;


import org.bson.types.ObjectId;
import org.junit.Test;

import com.pojo.emarket.Comment;
import com.pojo.lesson.LessonEntry;
import com.pojo.lesson.LessonType;

public class LessonDaoTest {

	private LessonDao dao =new LessonDao();
	
	@Test
	public void addLessonEntry()
	{
		LessonEntry lessonEntry =new LessonEntry("你好", "jjj", LessonType.BACKUP_LESSON, new ObjectId(), new ObjectId(), new ObjectId(), 1);
		dao.addLessonEntry(lessonEntry);
	}
	
	
	@Test
	public void addComment()
	{
		for(int i=0;i<67;i++)
		{
		  Comment comment =new Comment(new ObjectId(), "评论"+i);
		  dao.addComment(new ObjectId("5524a20e63e71da138df8f00"),comment);
		  try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}
		  
		}
	}
	
	
	@Test
	public void deleteComment()
	{
		dao.deleteComment(new ObjectId("5524a20e63e71da138df8f00"));;
	}
	
	
}
