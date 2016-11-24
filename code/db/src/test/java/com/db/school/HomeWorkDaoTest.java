package com.db.school;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Test;

import com.db.school.HomeWorkDao;
import com.pojo.app.IdValuePair;
import com.pojo.school.HomeWorkEntry;
import com.pojo.school.HomeWorkSubmitType;
import com.pojo.school.StudentSubmitHomeWork;

public class HomeWorkDaoTest {

	private HomeWorkDao dao =new HomeWorkDao();
	
	@Test
	public void  addHomeWorkEntry()
	{
		List<IdValuePair> classes =new ArrayList<IdValuePair>();
		IdValuePair idValuePair =new IdValuePair(new ObjectId(),"1班");
		
		classes.add(idValuePair);
		//HomeWorkEntry e =new HomeWorkEntry(new ObjectId(), classes, "英语作业", "好好组也", "", "");
		//dao.addHomeWorkEntry(e);
	}
	
	
	@Test
	public void submitHomeWork()
	{
		for(int i=0;i<50;i++)
		{
//			StudentSubmitHomeWork sw =new StudentSubmitHomeWork(new ObjectId(), new ObjectId(), System.currentTimeMillis(), "dd"+i, "", "", HomeWorkSubmitType.ONCLASS);
//			dao.submitHomeWork(new ObjectId("550a86b8fe5b1a6205d99056"), sw);
//			try {
//				Thread.sleep(3);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
		}
	}
	
	
	@Test
	public void getHomeWorkEntry()
	{
		System.out.println(dao.findHomeWorkEntry(new ObjectId("550a86b8fe5b1a6205d99056")));
	}
	
	
	@Test
	public void getStudentSubmitHomeWorks()
	{
		//List<StudentSubmitHomeWork> list=dao.getStudentSubmitHomeWorks(new ObjectId("550a86b8fe5b1a6205d99056"), 0, 5);
		//System.out.println(list);
	}
	
}
