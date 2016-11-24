package com.db.temp;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.bson.types.ObjectId;

import com.db.letter.LetterDao;
import com.db.school.SchoolDao;
import com.db.user.UserDao;
import com.mongodb.BasicDBObject;
import com.pojo.letter.LetterEntry;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;

/**
 * 信件统计
 *
 * @author fourer
 */
public class LetterStat {

    public static void main(String[] args) throws IOException {

//		 File file =new File("/home/letter1221.txt");
//		 file.createNewFile();
//		 
//		 UserDao userDao=new UserDao();
//		 LetterDao letterDao =new LetterDao();
//		 SchoolDao schoolDao =new SchoolDao();
//		 
//		 List<UserEntry> list= userDao.getUserList("小助手",new BasicDBObject(Constant.ID,1));
//		 
//		 Set<ObjectId> uIdSet=new HashSet<ObjectId>(MongoUtils.getFieldObjectIDs(list, "_id"));
//		 
//		 List<LetterEntry> letters= letterDao.getLetters(uIdSet, Constant.FIELDS);
//		 
//		 //"yyyy-MM-dd HH:mm:ss
//		 //12.5~12.20
//		 long minTime=DateTimeUtils.getStrToLongTime("2015-12-05 00:00:00");
//		 long maxTime=DateTimeUtils.getStrToLongTime("2015-12-20 23:59:59");
//		 
//		 
//		 UserEntry ue;
//		 SchoolEntry se;
//		 long time;
//		 
//		 for(LetterEntry le:letters)
//		 {
//			 try
//			 {
//				 time=le.getID().getTime();
//				 if(time>=minTime && time<=maxTime)
//				 {
//					 ue= userDao.getUserEntry(le.getSenderId(), Constant.FIELDS);
//					 se=schoolDao.getSchoolEntry(ue.getSchoolID());
//					 FileUtils.write(file, "\r\n",true);
//					 FileUtils.write(file, "时间："+DateTimeUtils.convert(time, DateTimeUtils.DATE_YYYY_MM_DD_HH_MM)+"发送者："+ ue.getUserName()+";学校："+se.getName()+";内容："+le.getContent(),true);
//				 }
//			 }catch(Exception ex)
//			 {
//				 
//			 }
//		 }
    }
}
