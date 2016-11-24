package com.db.school;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Test;

import com.pojo.school.Grade;
import com.pojo.school.SchoolEntry;
import com.pojo.school.SchoolType;
import com.sys.constants.Constant;

public class SchoolDaoTest {

	SchoolDao dao =new SchoolDao();
	
	@Test
	public void addSchoolEntry()
	{
		SchoolEntry e =new SchoolEntry(SchoolType.UNIVERSITY.getType(),"复兰大学",new ObjectId(),"123");
		ObjectId id=dao.addSchoolEntry(e);
		System.out.println(id);
	}
	
	@Test
	public void getSchoolEntry()
	{
		SchoolEntry e =dao.getSchoolEntry(new ObjectId("54f16ba6fe5bfcc6c96b4ba3"),Constant.FIELDS);
		System.out.println(e);
	}
	
	
	@Test
	public void addGrade()
	{
//		Grade g =new Grade("一年级", SchoolType.UNIVERSITY.getType(),new ObjectId(),"小四");
//		dao.addGrade(new ObjectId("54f16ba6fe5bfcc6c96b4ba3"), g);
//		getSchoolEntry();
	}
	
	
	@Test
	public void update()
	{
		dao.update(new ObjectId("54f16ba6fe5bfcc6c96b4ba3"), "tp", "12345678");
		getSchoolEntry();
	}
	
	@Test
	public void updateGradeForSubject()
	{
		List<ObjectId> gradeids =new ArrayList<ObjectId>();
		gradeids.add(new ObjectId());
		gradeids.add(new ObjectId());
		gradeids.add(new ObjectId());
		gradeids.add(new ObjectId());
		
		
		dao.updateGradeForSubject(new ObjectId("558a9b45f6f285bf9fa082df"), new ObjectId("558aa02cf6f285bf9fa86a85"), gradeids);
	}
}
