package com.db.app;

import com.db.lesson.DirDao;

import org.bson.types.ObjectId;
import org.junit.Test;

import com.pojo.lesson.DirEntry;
import com.pojo.lesson.DirType;

public class DirdaoTest {

	private DirDao dao =new DirDao();
	
	@Test
	public void addDirEntry()
	{
		DirEntry e =new DirEntry(new ObjectId(), "我的目录", null, 0,DirType.BACK_UP);
		dao.addDirEntry(e);
	}
	
	
	
	@Test
	public void getDirEntryList()
	{
		System.out.println(dao.getDirEntryList(new ObjectId("54fe8e96fe5bf187594aa048"),null));
		;
	}
	

	
}
