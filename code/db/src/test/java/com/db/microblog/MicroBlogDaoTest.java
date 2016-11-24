package com.db.microblog;

import com.db.school.SchoolDao;
import com.db.user.UserDao;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;
import org.junit.Test;

import com.pojo.app.Platform;
import com.pojo.microblog.MicroBlogEntry;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MicroBlogDaoTest {

	MicroBlogDao dao =new MicroBlogDao();
	
	@Test
	public void addMicroBlog()
	{
//		MicroBlogEntry e =new MicroBlogEntry(new ObjectId("54f6df81fe5b3549db690d98"),1, "我在测试1", Platform.Android,1,1,null,0,null,null);
//		System.out.println(dao.addMicroBlog(e));
	}
	
	@Test
	public void deleteMicroBlog()
	{
		dao.deleteMicroBlog(new ObjectId("54f6df9efe5b3dcd6a66050e"), new ObjectId("54f6df81fe5b3549db690d98"));
	}
	
	@Test
	public void addComment()
	{
		dao.addComment(new ObjectId("54f6df9efe5b3dcd6a66050e"), new ObjectId());;
	}
	
	
	@Test
	public void deleteComment()
	{
		dao.deleteComment(new ObjectId("54f6df9efe5b3dcd6a66050e"), new ObjectId("54f6e0a8fe5b3ed5c4b48934"));
	}
	
	@Test
	public void addZan()
	{
		dao.addZan(new ObjectId("54f6df9efe5b3dcd6a66050e"), new ObjectId());
	}
	
	@Test
	public void isHaveZan()
	{
		boolean a=dao.isHaveZan(new ObjectId("54f6df9efe5b3dcd6a66050e"), new ObjectId());
		System.out.println(a);
	}
	
	@Test
	public void getMicroBlogEntryList()
	{
//		//ObjectId ui,MicroBlogState state,Platform form,                                                                                    ObjectId classScope, ObjectId gradeScope,DBObject fields,int skip,int limit
//		try {
//			  List<MicroBlogEntry>  list=dao.getMicroBlogEntryList(new ObjectId("54f6df81fe5b3549db690d98"), MicroBlogState.NORMAL,Platform.Android, null, Constant.FIELDS, 0, 0);
//			  System.out.println(list.size());
//			  for(MicroBlogEntry e:list)
//			 {
//				 System.out.println(e);
//			 }
//		} catch (ResultTooManyException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	@Test
	public void test(){
		List<MicroBlogEntry> list =  dao.getMicroBlogEntryList("#中秋妙语#");
		UserDao userdao = new UserDao();
//		SchoolDao schoolDao = new SchoolDao();
		Set<ObjectId> userSet = new HashSet<ObjectId>();
		for(MicroBlogEntry microBlogEntry : list){
			UserEntry userEntry = userdao.getUserEntry(microBlogEntry.getUserId(), Constant.FIELDS);
//			SchoolEntry schoolEntry = schoolDao.getSchoolEntry(microBlogEntry.getSchoolID());
			System.out.println(userEntry.getUserName() + "\t" + userEntry.getExperiencevalue() + "\t" + userEntry.getID());
			if(!userEntry.getUserName().equals("梁紫嘉"))
				userSet.add(microBlogEntry.getUserId());
		}
//		for(ObjectId userId : userSet){
//			userdao.updateExperenceValue(userId);
//		}
//		System.out.println(list.size() + "     " + userSet.size());
	}
}
