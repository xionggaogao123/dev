package com.db.temp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.bson.types.ObjectId;

import com.db.app.RegionDao;
import com.db.lesson.LessonDao;
import com.db.school.SchoolDao;
import com.db.user.UserDao;
import com.mongodb.BasicDBObject;
import com.pojo.app.RegionEntry;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;

/**
 * 安徽人数统计
 * @author fourer
 *
 */
public class AnhuiUserStat {

	public static void main(String[] args) throws IOException {
		
		File file =new File("/home/ah_micro0823.txt");
		file.createNewFile();
		ObjectId anhuiId=new ObjectId("55934c13f6f28b7261c19c3d");
		RegionDao rDao =new RegionDao();
		
		List<RegionEntry> list=rDao.getRegionEntryListByPid(Arrays.asList(anhuiId));
		
		Set<ObjectId> idSet =new HashSet<ObjectId>();
		
		
		
		for(RegionEntry re:list)
		{
			idSet.add(re.getID());
		}
		
		list=rDao.getRegionEntryListByPid(new ArrayList<ObjectId>(idSet));
		
		for(RegionEntry re:list)
		{
				idSet.add(re.getID());
		}
		 
		 idSet.add(anhuiId);
		
		SchoolDao sdao =new SchoolDao();
		UserDao uDao =new UserDao();
		LessonDao lDao =new LessonDao();
		
		List<SchoolEntry> schools=	sdao.getSchoolEntry(idSet, 0, 5000);
		
		 FileUtils.write(file, "\r\n",true);
		 FileUtils.write(file, "学校数目："+schools.size(),true);
		
		 
		 

		 int studentCount=0;
		 int teacherCount=0;
		 
		for(SchoolEntry s:schools)
		{
			 List<UserEntry> uList_stu=uDao.getUserBySchoolIdAndRole(s.getID(), UserRole.STUDENT.getRole(), new BasicDBObject("_id",1));
			 List<UserEntry> uList_tea=  uDao.getTeacherEntryBySchoolId(s.getID(),"",new BasicDBObject("_id",1));
			 FileUtils.write(file, "\r\n",true);
			 FileUtils.write(file, s.getName()+" 学生："+uList_stu.size()+" 老师："+uList_tea.size(),true);
			 studentCount+=uList_stu.size();
			 teacherCount+=uList_tea.size();
		}
		
		 FileUtils.write(file, "\r\n",true);
		 FileUtils.write(file, "\r\n",true);
		 FileUtils.write(file, "学生总数："+studentCount+",老师总数："+teacherCount,true);
		
	}
}
