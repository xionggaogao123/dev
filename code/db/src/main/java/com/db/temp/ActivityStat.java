package com.db.temp;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.db.activity.ActivityDao;
import com.db.school.SchoolDao;
import com.db.user.UserDao;
import com.pojo.activity.ActivityEntry;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;

/**
 * 好友圈活动统计
 * @author fourer
 *
 */
public class ActivityStat {

	public static void main(String[] args) throws IOException {

		File f=new File("D:\\var\\activit2.txt");
		f.createNewFile();
		ActivityDao activityDao =new ActivityDao();
		UserDao uDao =new UserDao();
		SchoolDao sDao =new SchoolDao();
		
		 List<ActivityEntry> list =activityDao.getAllList();
		 
		 for(ActivityEntry e:list)
		 {
			 try
			 {
				 StringBuffer b =new StringBuffer();
				 b.append("时间:");
				 b.append(DateTimeUtils.convert(e.getID().getTime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM));
				 b.append("发起人：");
			
				 UserEntry ue= uDao.getUserEntry(e.getOrganizerId(), Constant.FIELDS);
				 b.append(ue.getUserName());
				 b.append("学校：");
				 SchoolEntry se= sDao.getSchoolEntry(ue.getSchoolID(), Constant.FIELDS);
				 b.append(se.getName());
				 b.append("讨论数目：");
				 b.append(e.getDiscussCount());
				 b.append("照片数目：");
				 b.append(e.getImageCount());
				 b.append("标题：");
				 b.append(e.getActName());
				 b.append("说明：");
				 b.append(e.getDescription());
				 
				 System.out.println(b.toString());
				 FileUtils.write(f, b.toString(), true);
				 FileUtils.write(f, "\r\n", true);
			 }catch(Exception ex)
			 {
				 
			 }
		 }
		
	}
}
