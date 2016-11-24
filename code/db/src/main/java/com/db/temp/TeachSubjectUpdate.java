package com.db.temp;

import java.util.List;

import org.bson.types.ObjectId;

import com.db.school.ClassDao;
import com.db.school.InterestClassDao;
import com.db.school.TeacherClassSubjectDao;
import com.mongodb.BasicDBObject;
import com.pojo.school.ClassEntry;
import com.pojo.school.InterestClassEntry;
import com.pojo.school.TeacherClassSubjectEntry;

public class TeachSubjectUpdate {

	public static void main(String[] args) {
		ClassDao classDao =new ClassDao();
		InterestClassDao interestClassDao=new InterestClassDao();
		TeacherClassSubjectDao teacherClassSubjectDao =new TeacherClassSubjectDao();
		
		int skip=0;
		int limit=200;
		
		while(true)
		{
			 System.out.println("skip="+skip);
			 List<TeacherClassSubjectEntry> schoolList=teacherClassSubjectDao.getClassSubjectEntry(skip, limit);
			
			 if(null==schoolList || schoolList.isEmpty())
			 {
				  break;
			 }
			 for(TeacherClassSubjectEntry se:schoolList)
			 {
				 ObjectId clasid=se.getClassInfo().getId();
				 ClassEntry ce= classDao.getClassEntryById(clasid, new BasicDBObject("_id",1));
				 
				 if(null==ce)
				 {
					 InterestClassEntry inc= interestClassDao.findEntryByClassId(clasid);
					 if(null==inc)
					 {
						 teacherClassSubjectDao.removeByClassId(clasid);
					 }
				 }
				
			 }
			 skip=skip+200;
		}
	}
}
