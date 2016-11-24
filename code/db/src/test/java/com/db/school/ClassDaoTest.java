package com.db.school;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Test;

import com.pojo.school.ClassEntry;
import com.pojo.school.ClassType;
import com.sys.constants.Constant;

public class ClassDaoTest {

	ClassDao dao =new ClassDao();
	
	@Test
	public void addClassEntry()
	{
		ClassEntry  e = new ClassEntry(new ObjectId("54f16ba6fe5bfcc6c96b4ba3"), new ObjectId("54f16d1efe5b185d3fcbff98"), "销售", null,100);
		dao.addClassEntry(e);
		System.out.println(e.getID());
	}
	
	@Test
	public void updateIntroduce()
	{
		dao.updateIntroduce(new ObjectId("54f192b8fe5be49a15aa0961"), "this is test!!");
	}
	
	@Test
	public void addTeacher()
	{
		dao.addTeacher(new ObjectId("54f192b8fe5be49a15aa0961"), new ObjectId("54f192b8fe5be49a15aa0961"));
	}
	
	
	@Test
	public void addStudent()
	{
		dao.addStudent(new ObjectId("54f192b8fe5be49a15aa0961"), new ObjectId("54f192b8fe5be49a15aa0961"));
	}
	
	@Test
	public void getClassEntryMap()
	{
		List<ObjectId> list =new ArrayList<ObjectId>();
		list.add(new ObjectId("54f192b8fe5be49a15aa0961"));
		list.add(new ObjectId("54f193c4fe5b34eb0b5fde40"));
		System.out.println(dao.getClassEntryMap(list, Constant.FIELDS));
	}
	
	@Test
	public void getClassEntryListByStudentId()
	{
		//List<ClassEntry> list=dao.getClassEntryListByStudentId( new ObjectId("54f192b8fe5be49a15aa0961"), Constant.FIELDS);
		//System.out.println(list);
	}
}
