package com.db.temp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bson.types.ObjectId;

import com.db.school.SchoolDao;
import com.db.user.UserDao;
import com.mongodb.BasicDBObject;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.MD5Utils;

/**
 * 
 * @author fourer
 *
 */
public class ParentAccount {

	public static void main(String[] args) throws IOException, IllegalParamException {
		
		File logFile =new File("/home/parentAccount.log");
		File errorFile =new File("/home/errorAccount.log");
		
		logFile.createNewFile();
		errorFile.createNewFile();
		
		UserDao userDao =new UserDao();
		SchoolDao schoolDao =new SchoolDao();
		
		 int skip=0;
		 int limit=200;
		 
		 SchoolEntry schoolEntry=null;
		 
		 while(true)
		 {
			 System.out.println("skip="+skip);
			 List<UserEntry> list= userDao.getAllEntrys(skip, limit);
			 
			 if(null==list || list.size()==0)
			 {
				 break;
			 }
			 
			 for(UserEntry u:list)
			 {
				 if(UserRole.isStudent(u.getRole()))
				 {
					 try
					 {
							 List<ObjectId> studentConnectids =new ArrayList<ObjectId>();
							 schoolEntry =schoolDao.getSchoolEntry(u.getSchoolID(), new BasicDBObject("inp",1));
							 List<ObjectId> sunList =new ArrayList<ObjectId>();
							 sunList.add(u.getID());
							 
							 //爸爸
							 ObjectId fatherId=new ObjectId();
							 UserEntry father=new UserEntry(u.getUserName()+"爸爸", MD5Utils.getMD5String(schoolEntry.getInitialPassword()), Constant.ONE, null);
						 father.setLetter(PinYin2Abbreviation.cn2py(father.getUserName()));
						 father.setRole(UserRole.PARENT.getRole());
							 father.setSchoolID(schoolEntry.getID());
							 father.setID(fatherId);
							 father.setConnectIds(sunList);
							 father.setAvatar("head-" + "default-head.jpg");
							 father.setNickName(u.getUserName()+"爸爸");
							 userDao.addUserEntry(father);
							 
							 //妈妈
							 ObjectId matherId=new ObjectId();
							 UserEntry mather=new UserEntry(u.getUserName()+"妈妈", MD5Utils.getMD5String(schoolEntry.getInitialPassword()), Constant.ZERO, null);
						 mather.setLetter(PinYin2Abbreviation.cn2py(mather.getUserName()));
						 mather.setRole(UserRole.PARENT.getRole());
							 mather.setSchoolID(schoolEntry.getID());
							 mather.setID(matherId);
							 mather.setConnectIds(sunList);
							 mather.setAvatar("head-" + "default-head.jpg");
							 mather.setNickName(u.getUserName()+"妈妈");
							 userDao.addUserEntry(mather);
							 
							 
							 UserEntry parentEntry= userDao.getUserEntryByName(u.getUserName()+"家长"); 
							 
							 if(null==parentEntry)
							 {
								 FileUtils.write(logFile, u.getUserName()+"家长没有找到", true);;
								 FileUtils.write(logFile, "\r\n", true);
							 }
							 else
							 {
								 studentConnectids.add(parentEntry.getID());
							     userDao.update(parentEntry.getID(), "cid", MongoUtils.convert(sunList), false);
							 }
							 
							 studentConnectids.add(fatherId);
							 studentConnectids.add(matherId);
							 
							 userDao.update(u.getID(), "cid", MongoUtils.convert(studentConnectids), false);
							 studentConnectids=null;
							 sunList=null;
					 
					 }catch(Exception ex)
					 {
						 FileUtils.write(errorFile, u.getUserName(), true);;
						 FileUtils.write(errorFile, ex.getMessage(), true);
						 FileUtils.write(errorFile, "\r\n", true);
					 }
				 }
			 }
			 
			 skip=skip+200;
			 list=null;
		 }
	}
	
}
