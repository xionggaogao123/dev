package com.db.temp;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bson.types.ObjectId;

import com.db.school.SchoolDao;
import com.sys.utils.DateTimeUtils;

public class SchoolNavs {

	public static void main(String[] args) throws IOException {
		
		//yyyy-MM-dd HH:mm:ss
		System.out.println(new ObjectId(DateTimeUtils.stringToDate("2016-10-09 00:00:00", DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H)));
		
		
		
		
		SchoolDao schoolDao =new SchoolDao();
		
		File logFile =new File("/home/schoolNavs.log");
		logFile.createNewFile();
		
		
		File nvaFile =new File("/home/学校导航.txt");
		List<String> list =FileUtils.readLines(nvaFile, "utf-8");
		
		for(String f:list)
		{
			String[] infos =f.split(",");
		    ObjectId id=	schoolDao.getSchoolIdByName(infos[0].trim());
		    
		    if(null==id)
		    {
		    	id=	schoolDao.getSchoolIdByName(infos[0].trim().substring(1));
		    }
		    
		    if(null==id)
		    {
		    	List<ObjectId> ls=	schoolDao.getSchoolIdByNames(infos[0].trim());
		    	if(null!=ls && ls.size()==1)
		    	{
		    		id=ls.get(0);
		    	}
		    }
		    
		    boolean isSuccess =false;
		    if(null!=id)
		    {
		    	try
		    	{
		    	  schoolDao.update(id, "nv", Integer.parseInt(infos[1]));
		    	  isSuccess=true;
		    	}catch(Exception ex)
		    	{
		    		
		    	}
		    }
		    
		    if(!isSuccess)
		    {
		    	FileUtils.write(logFile, f, true);
		    	FileUtils.write(logFile, "\r\n", true);
		    }
		}
		
	}
}
