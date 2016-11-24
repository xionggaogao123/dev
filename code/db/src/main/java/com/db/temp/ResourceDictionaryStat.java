package com.db.temp;

import java.util.List;

import com.db.resources.ResourceDictionaryDao;
import com.pojo.resources.ResourceDictionaryEntry;

/**
 * 找到哪个单元下面有30个课程
 * @author fourer
 *
 */
public class ResourceDictionaryStat {

	public static void main(String[] args) {
		ResourceDictionaryDao dao =new ResourceDictionaryDao();
		
		List<ResourceDictionaryEntry> list=dao.getResourceDictionaryEntrys(5);
		
		for(ResourceDictionaryEntry e:list)
		{
			List<ResourceDictionaryEntry> sList=dao.getResourceDictionaryEntrys(e.getID(), 6);
			if(sList.size()>20)
			{
				System.out.println(sList.size()+":"+e.getID());
			}
		}
	}
}
