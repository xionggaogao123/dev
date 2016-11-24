package com.db.user;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Test;

import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;

public class UserDaoTest {

	UserDao dao =new UserDao();
	
	@Test
	public void addUserEntry()
	{
//		UserEntry e =new UserEntry("王明","222",0,null);
//		e.setSchoolID(new ObjectId());
//		e.setAvatar("123.jpg");
//		dao.addUserEntry(e);
		
		List<String> ids =new ArrayList<String>();
		ids.add("");
		long a=System.currentTimeMillis();
		dao.getUserEntrysByChatids(ids, Constant.FIELDS);
		long b=System.currentTimeMillis();
		
		System.out.println(b-a);
//		
		
		//long a=System.currentTimeMillis();
		//MongoFacroty.getAppDB().getCollection("uuser2").update(new BasicDBObject(), new BasicDBObject("$set",new BasicDBObject("ttest","阿斯顿发是大法师大法是的发生；兰多夫金；拉萨地方就；拉上京东方；垃圾收到发；了解阿斯顿飞；理解啊是的；浪费就；阿斯兰多夫金；拉上京东方；理解啊是的 "),false,true);
		
		//MongoFacroty.getAppDB().getCollection("uuser2").update(new BasicDBObject(),new BasicDBObject("$set",new BasicDBObject("ttest","阿斯顿发是大法师大法是的发生；兰多夫金；拉萨地方就；拉上京东方；垃圾收到发；了解阿斯顿飞；理解啊是的；浪费就；阿斯兰多夫金；拉上京东方；理解啊是的 ")),false,true);
		//long b=System.currentTimeMillis();
		//System.out.println(b-a);
	}
	
	
	
	@Test
	public void update()
	{
		try {
			dao.update(new ObjectId("54f0328afe5b0f608a43329a"), "add", "ddd",true);
		} catch (IllegalParamException e) {
		}
	}
	
	
	@Test
	public void getUserEntry()
	{
		UserEntry e=dao.getUserEntry(new ObjectId("55934c16f6f28b7261c19d95"),Constant.FIELDS);
		System.out.println(e.getBaseEntry());
	}
	
	
	@Test
	public void getUserEntryList()
	{
		List<ObjectId> list =new ArrayList<ObjectId>();
		list.add(new ObjectId("54f033cdfe5b1df5359c9be5"));
		BasicDBObject fields =new BasicDBObject("nm",1);
		
		List<UserEntry> res=dao.getUserEntryList(list, fields);
		for(UserEntry e:res)
		{
			System.out.println(e.getBaseEntry());
		}
	}
	
	@Test
	public void getSynCount()
	{
		System.out.println(dao.getSynCount(Constant.COLLECTION_USER_NAME));
	}
	
	
	@Test
	public void getSynDBObjectList()
	{
		List<ObjectId> list =new ArrayList<ObjectId>();
		list.add(new ObjectId("54f033cdfe5b1df5359c9be5"));
		List<DBObject> res=dao.getSynDBObjectList(Constant.COLLECTION_USER_NAME, 0, 2);
		for(DBObject dbo:res)
		{
			System.out.println(dbo);
		}
	}

	@Test
	public void updateSyn()
	{
		List<ObjectId> list =new ArrayList<ObjectId>();
		list.add(new ObjectId("54f033cdfe5b1df5359c9be5"));
		dao.updateSyn(Constant.COLLECTION_USER_NAME, list);
	}
	
	
	@Test
	public void searchUsers()
	{
		System.out.println(dao.searchUserByUserName("王明test"));
	}
}
