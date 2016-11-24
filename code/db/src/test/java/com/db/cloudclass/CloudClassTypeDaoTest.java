package com.db.cloudclass;

import org.bson.types.ObjectId;
import org.junit.Test;

import com.db.cloudlesson.CloudLessonTypeDao;
import com.pojo.cloudlesson.CloudLessonTypeEntry;

public class CloudClassTypeDaoTest {

	private CloudLessonTypeDao dao =new CloudLessonTypeDao();
	
	@Test
	public void addCloudClassTypeEntry()
	{
		CloudLessonTypeEntry e =new CloudLessonTypeEntry(1, 2, 1, "科学杂志", "测试", 1);
		dao.addCloudLessonTypeEntry(e);
	}
	
	@Test
	public void updateValue()
	{
		dao.updateValue(new ObjectId("54fd0dbffe5b6288c804517d"), "or", 5);
	}
	
	@Test
	public void getList()
	{
	   System.out.println(dao.getList(1, -1, 1));
	}
	
}
