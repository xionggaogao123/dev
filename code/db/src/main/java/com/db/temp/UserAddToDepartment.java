package com.db.temp;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bson.types.ObjectId;

import com.db.school.DepartmentDao;
import com.db.user.UserDao;
import com.pojo.school.DepartmentEntry;
import com.pojo.user.UserEntry;

public class UserAddToDepartment {

	public static void main(String[] args) throws IOException {
		
		DepartmentDao departmentDao=new DepartmentDao();
		UserDao userDao =new UserDao();
		
		String path="/home/来凤县实验小学分组/";
		ObjectId schoolId =new ObjectId("56e636770cf2a5c70a1c406c");
		
		
		
		
		File logFile =new File("/home/useradddep0323.txt");
		logFile.createNewFile();
		
		File dir =new File(path);
		
		for(File file:dir.listFiles())
		{
			List<String> infos =FileUtils.readLines(file, "utf-8");
			
			String depName =file.getName().replaceAll(".txt", "").trim();
			DepartmentEntry dep=departmentDao.getDepartmentEntry(schoolId,depName);
			
			if(null==dep)
			{
				FileUtils.write(logFile, "没有找到部门："+depName, true);
				FileUtils.write(logFile, "\r\n", true);
				continue;
			}
			
			for(int i=0;i<infos.size();i++)
			{
				
				String name="";
				if(i==0)
					name=infos.get(i).trim().substring(1);
				else
					name=infos.get(i).trim();
				
				
				
				UserEntry ue =userDao.getUserEntryByName(name,schoolId);
				
				if(null==ue)
				{
					for(int a=1;a<10;a++)
					{
						ue =userDao.getUserEntryByName(name+a,schoolId);
						if(null!=ue)
						{
							break;
						}
					}
				}
				
				if(null==ue)
				{
					FileUtils.write(logFile, "没有找到用户："+infos.get(i), true);
					FileUtils.write(logFile, "\r\n", true);
					continue;
				}
				departmentDao.addMember(dep.getID(), ue.getID());
			}
			
		}
	}
}
