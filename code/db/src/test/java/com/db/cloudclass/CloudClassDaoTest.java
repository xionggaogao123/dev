package com.db.cloudclass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.junit.Test;

import com.db.cloudlesson.CloudLessonDao;
import com.pojo.cloudlesson.CloudLessonEntry;
import com.sys.constants.Constant;
import com.sys.exceptions.ResultTooManyException;

public class CloudClassDaoTest {

	private CloudLessonDao dao =new CloudLessonDao();
	
	
	@Test
	public void addCloudClassEntry()
	{
//		CloudLessonEntry e =new CloudLessonEntry("我在测试视频", "我在测试视频", new ObjectId(), 1, "", new ObjectId(), 1, null, null);
//		dao.addCloudLessonEntry(e);
	}

}
