package com.db.app;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Test;

import com.pojo.app.RegionEntry;

public class RegionDaoTest {

	RegionDao dao =new RegionDao();
	
	@Test
	public void addRegionEntry()
	{
		RegionEntry e =new RegionEntry(1,new ObjectId(),"中国");
		dao.addRegionEntry(e);
		System.out.println(e.getID().toString());
	}
	
	
	@Test
	public void addRegionEntrys()
	{
		RegionEntry e =new RegionEntry(2,new ObjectId(),"河北省");
		RegionEntry e1 =new RegionEntry(2,new ObjectId(),"上海市");
		RegionEntry e2 =new RegionEntry(2,new ObjectId(),"北京市");
		List<RegionEntry> list =new ArrayList<RegionEntry>();
		list.add(e);
		list.add(e1);
		list.add(e2);
		
		dao.addRegionEntrys(list);
	
	}
	
	
	
	
	
	
	@Test
	public void gerRegionEntryByName()
	{
			RegionEntry e =dao.getRegionEntryByName("河北省");
			System.out.println(e.getBaseEntry());
	}
	
}
