package com.db.temp;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.db.user.UserDao;
import com.pojo.user.UserEntry;
import com.sys.exceptions.IllegalParamException;

public class SSOLinks {

	public static void main(String[] args) throws IOException {
		
		    UserDao userDao =new UserDao();
		
			List<String> list =FileUtils.readLines(new File("/home/links.txt") , "utf-8");
			
			for(String s:list)
			{
				String[] infos=s.split(",");
				if(null!=infos && infos.length==2)
				{
					UserEntry ue =userDao.getUserEntryByName(infos[1]);
					if(null!=ue && StringUtils.isNotBlank(infos[0]))
					{
						try {
							userDao.update(ue.getID(), "bnm", infos[0], false);
						} catch (IllegalParamException e) {
						}
					}
					
				}
			}
	}
}
