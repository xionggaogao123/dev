package com.db.temp;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.db.user.UserDao;
import com.pojo.user.UserEntry;
import com.sys.utils.DateTimeUtils;

public class UserTimes {

	public static void main(String[] args) throws IOException {
		UserDao userDao =new UserDao();
		
		 File file =new File("/home/micro160606_user_time.txt");
		 file.createNewFile();
		 
		 
		
		 {
			
			 List<UserEntry> list= userDao.getAllEntrys(0, 50000);
			 
			 for(UserEntry u:list)
			 {
				 FileUtils.write(file, "\r\n",true);
				 FileUtils.write(file, u.getID().toString()+","+u.getUserName()+","+DateTimeUtils.convert(u.getID().getTime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A),true);
			 }
			
			 
		
		 }
	}
}
