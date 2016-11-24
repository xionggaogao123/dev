package com.db.user;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Test;

import com.db.factory.MongoFacroty;
import com.db.notice.NoticeDao;
import com.pojo.notice.NoticeReadsEntry;
import com.sys.constants.Constant;

public class NoticeReadDaoTest {

	@Test
	public void addEntry()
	{
		long a=System.currentTimeMillis();
		List<ObjectId> list =new ArrayList<ObjectId>();
		
		for(int i=0;i<10000*20;i++)
		{
			list.add(new ObjectId());
		}
		
		NoticeReadsEntry e =new NoticeReadsEntry(new ObjectId(), list);
		
		MongoFacroty.getAppDB().getCollection(Constant.COLLECTION_NOTICE_READ_NAME).save(e.getBaseEntry());
		long b=System.currentTimeMillis();
		
		System.out.println("time="+(b-a));
	}
	
	
	@Test
	public void addReader()
	{
		NoticeDao dao =new NoticeDao();
		
		for(int i=0;i<30;i++)
		{
			long a=System.currentTimeMillis();
		    dao.userReadNotice(new ObjectId("572ab8f663e7f9f22f79d47e"),new ObjectId());
		    long b=System.currentTimeMillis();
			
			System.out.println(i+":time="+(b-a));
		}
	}
	
}
