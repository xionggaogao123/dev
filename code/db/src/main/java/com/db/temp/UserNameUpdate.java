package com.db.temp;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bson.types.ObjectId;

import com.db.user.UserDao;
import com.pojo.user.UserEntry;

public class UserNameUpdate {

	public static void main(String[] args) throws Exception {
		
		
		ObjectId jinyuanId=new ObjectId("56937c650cf2dd4bb8da5e46");
		ObjectId fulanId =new ObjectId("56e68e7e0cf2a5c70a1cc136");
		
		List<String> names=FileUtils.readLines(new File("/home/jinyuanlist.txt"), "utf-8");
		
		UserDao udao =new UserDao();
		
		
		File f =new File("/home/stat_jinyuan.log");
		f.createNewFile();
		
		
		for(String s:names)
		{
			boolean isf=false;
			UserEntry u1=udao.getUserEntryByName(s); //复兰
			if(null!=u1 && u1.getSchoolID().equals(fulanId))
			{
				UserEntry u2=udao.getUserEntryByName(s+"1"); //晋元
				if(null!=u2 && u2.getSchoolID().equals(jinyuanId))
				{
					udao.update(u1.getID(), "ir", 1, false);
					udao.update(u2.getID(), "nm", s, false);
					isf=true;
				}
			}
			
			if(!isf)
			{
				  FileUtils.write(f, s, true);
				  FileUtils.write(f, "\r\n", true);
			}
		}
		
	}
}
