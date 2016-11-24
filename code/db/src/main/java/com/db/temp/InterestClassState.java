package com.db.temp;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.bson.types.ObjectId;

import com.db.school.InterestClassDao;
import com.db.user.UserDao;
import com.mongodb.BasicDBObject;
import com.pojo.school.InterestClassEntry;
import com.pojo.school.InterestClassStudent;
import com.pojo.user.UserEntry;

public class InterestClassState {

	public static void main(String[] args) throws IOException {
		
		
		/**
		File file =new File("/home/interest0909.txt");
		file.createNewFile();
		
		InterestClassDao dao =new InterestClassDao();
		UserDao uDao =new UserDao();
		List<InterestClassEntry> list=dao.findClassBySchoolId(new ObjectId("55934c14f6f28b7261c19c5e"),-1);
		
		
		Set<ObjectId> userSet =new HashSet<ObjectId>();
		
		for(InterestClassEntry e:list)
		{
			userSet.add(e.getTeacherId());
			for(InterestClassStudent stus:e.getInterestClassStudents())
			{
				userSet.add(stus.getStudentId());
			}
		}
		
		Map<ObjectId, UserEntry> uMap=uDao.getUserEntryMap(userSet, new BasicDBObject("nm",1));
		
		
		for(InterestClassEntry e:list)
		{
			StringBuilder b =new StringBuilder();
			
			b.append("名字："+e.getClassName());
			b.append(" 老师姓名："+uMap.get(e.getTeacherId()).getUserName());
			int t=e.getIsLongCourse();
			if(t==1)
			{
				b.append(" 课程类型：长课");
			}
			if(t==0)
			{
				b.append(" 课程类型：短课");
			}
			if(t==-1)
			{
				b.append(" 课程类型：默认");
			}
			
			b.append(" 学生人数；"+e.getInterestClassStudents().size()+";");
			
			for(InterestClassStudent stus:e.getInterestClassStudents())
			{
				b.append(" "+uMap.get(stus.getStudentId()).getUserName());
			}
			FileUtils.write(file, b.toString(),true);
		    FileUtils.write(file, "\r\n",true);
		}
		
		**/
	}
}
