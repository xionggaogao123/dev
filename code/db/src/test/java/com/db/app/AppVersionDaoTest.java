package com.db.app;

import org.junit.Test;

import com.pojo.app.AppVersionEntry;

public class AppVersionDaoTest {

	private AppVersionDao dao =new AppVersionDao();
	
	@Test
	public void addAppVersion()
	{
		AppVersionEntry e=new AppVersionEntry(1,"1.3.1");
		dao.addAppVersion(e);
	}
	
	@Test
	public void getRecentlyVersion()
	{
		AppVersionEntry e=dao.getRecentlyVersion(1);
		System.out.println(e);
	}
	
	
	
}
