package com.db.temp;

import java.util.List;


import com.db.school.SchoolDao;
import com.pojo.school.Grade;
import com.pojo.school.SchoolEntry;

public class GradeUpdate2 {

	public static void main(String[] args) {
		
		SchoolDao schoolService =new SchoolDao();
		int skip=0;
		int limit=200;
		
		while(true)
		{
			 List<SchoolEntry> schoolList=schoolService.getSchoolEntry(skip, limit);
			
			 if(null==schoolList || schoolList.isEmpty())
			 {
				  break;
			 }
			 for(SchoolEntry se:schoolList)
			 {
				 if(se.getSchoolType()==2 || se.getSchoolType()==4 || se.getSchoolType()==8)
				 {
					 if(se.getSchoolType()==2)
					 {
						 for(Grade g:se.getGradeList())
						 {
							 if(g.getGradeType()==-1)
							 {
								// schoolService.updateGradTypeById(se.getID(), g.getGradeId(), 15);
								// break;
							 }
						 }
					 }
					 
					 
					 if(se.getSchoolType()==4)
					 {
						 for(Grade g:se.getGradeList())
						 {
							 if(g.getGradeType()==-1)
							 {
								// schoolService.updateGradTypeById(se.getID(), g.getGradeId(), 16);
								 //break;
							 }
						 }
					 }
					 
					 if(se.getSchoolType()==8)
					 {
						 for(Grade g:se.getGradeList())
						 {
							 if(g.getGradeType()==-1)
							 {
								// schoolService.updateGradTypeById(se.getID(), g.getGradeId(), 17);
								 //break;
							 }
						 }
					 }
					 
					 
				 }
			 }
			 skip=skip+200;
		}
	}
}
