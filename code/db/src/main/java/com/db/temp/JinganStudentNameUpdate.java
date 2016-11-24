package com.db.temp;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.db.user.UserDao;
import com.pojo.user.UserEntry;

public class JinganStudentNameUpdate {

	public static void main(String[] args) throws IOException {
		
		UserDao uDao =new UserDao();
	
		List<String> names=FileUtils.readLines(new File("D:\\新建文件夹\\total.txt"), "utf-8");
	

		
		for(int i=0;i<names.size();i++)
		{
			System.out.println(i);
			try
			{
				if(StringUtils.isNotBlank(names.get(i)))
				{
					
					String[] nameArr= names.get(i).split(",");
					
					String n1=nameArr[0];
					String n2=nameArr[0].substring(1);
					UserEntry sEntry1 =uDao.getUserEntryByName(n1);
					UserEntry sEntry2 =uDao.getUserEntryByName(n2);
					
					
					String realName="";
					
					if(null!=sEntry1)
					{
						realName=n1;
						uDao.update(sEntry1.getID(), "logn", nameArr[1],false);
					}
					
					if(null!=sEntry2)
					{
						realName=n2;
						uDao.update(sEntry2.getID(), "logn", nameArr[1],false);
					}
					
					
					if(StringUtils.isNotBlank(realName))
					{
						  UserEntry spEntry =uDao.getUserEntryByName(realName+"家长");
						  if(null!=spEntry)
						  {
						     uDao.update(spEntry.getID(), "logn", nameArr[2],false);
						  }
						  else
						  {
							  System.out.println("没有找到用户家长;"+n1);
						  }
					}
					else
					{
						System.out.println("没有找到用户;"+n1);
					}
					
					
					
					
					
					
					
				    
				}
			}catch(Exception ex)
			{
			}
		}
	}
}
