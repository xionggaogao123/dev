package com.db.temp;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.bson.types.ObjectId;

import com.db.questionnaire.QuestionnaireDao;
import com.db.school.SchoolDao;
import com.db.user.UserDao;
import com.google.common.base.Joiner;
import com.pojo.questionnaire.QuestionnaireEntry;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;

public class QuestionnaireStat {

	public static void main(String[] args) throws IOException {
		
		/**
		QuestionnaireDao dao =new QuestionnaireDao();
		UserDao userDao =new UserDao();
		SchoolDao schoolDao =new SchoolDao();
		
		
		 File file =new File("/home/quare1102.txt");
		 file.createNewFile();
		 List<QuestionnaireEntry>  list=dao.getByName("问答活");
		 
		 System.out.println("++++++"+list.size());
		 for(QuestionnaireEntry e:list )
		 {
			 SchoolEntry se =schoolDao.getSchoolEntry(e.getSchoolId());
			 if(null!=se)
			 {
				 FileUtils.write(file, "学校："+se.getName(),true);
				 FileUtils.write(file, "\r\n",true);
				 Map<String,List<Object>>  maps= e.getRespondent();
				 
				 
				 if(null!=maps)
				 {
				 for(Map.Entry<String,List<Object>> entry:maps.entrySet())
				 {
					UserEntry ue= userDao.getUserEntry(new ObjectId(entry.getKey()), Constant.FIELDS);
					
					
					if(null!=ue)
					{
						 FileUtils.write(file, "   学生："+ue.getUserName()+";回答："+Joiner.on(",").join(entry.getValue()),true);
						 FileUtils.write(file, "\r\n",true);
					}
				 }
				 }
			 }
			 
			 FileUtils.write(file, "\r\n",true);
			 FileUtils.write(file, "\r\n",true);
			 FileUtils.write(file, "\r\n",true);
			 FileUtils.write(file, "\r\n",true);
			 FileUtils.write(file, "\r\n",true);
			 
		 }**/
	}
}
