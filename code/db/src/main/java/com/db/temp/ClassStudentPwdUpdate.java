
package com.db.temp;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.bson.types.ObjectId;

import com.db.school.ClassDao;
import com.db.user.UserDao;
import com.mongodb.BasicDBObject;
import com.pojo.school.ClassEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;

public class ClassStudentPwdUpdate {
 

	public static void main(String[] args) throws IllegalParamException, IOException {
		
		File f =new File("/home/fuyang_stus.txt");
		ClassDao cDao =new ClassDao();
		UserDao uDao =new UserDao();
		
		
		List<UserEntry> ulist=uDao.getUserEntryBySchoolId(new ObjectId("55f14f240cf2f06e97c87a18"), null, new BasicDBObject().append("nm", 1).append("r", 1), 0, 15000);
		
		List<ClassEntry> classes=	cDao.findClassInfoBySchoolId(new ObjectId("55f14f240cf2f06e97c87a18"),Constant.FIELDS);

		Map<ObjectId, ClassEntry> classMap =new HashMap<ObjectId, ClassEntry>();
		
		for(ClassEntry c:classes)
		{
			for(ObjectId stuId:c.getStudents())
			{
				classMap.put(stuId, c);
			}
			
			for(ObjectId stuId:c.getTeachers())
			{
				classMap.put(stuId, c);
			}
		}
		
		ClassEntry c=null;
		
		for(UserEntry ue:ulist)
		{
			c=classMap.get(ue.getID());
			if(null!=c)
			{
				if(ue.getRole()==1)
				{
				    FileUtils.write(f, "学生："+ue.getRealUserName()+","+c.getName(), true);
				   FileUtils.write(f, "\r\n", true);
				}else
				{
					FileUtils.write(f, "老师："+ue.getRealUserName()+","+c.getName(), true);
					FileUtils.write(f, "\r\n", true);
				}
			}
		}
	}
	
}
