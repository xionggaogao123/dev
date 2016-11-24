package com.db.itempool;

import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Test;

import com.pojo.app.IdNameValuePair;
import com.pojo.itempool.StudentErrorItemEntry;
import com.pojo.itempool.StudentErrorItemEntry.Item;


public class StudentErrorItemDaoTest {

	private StudentErrorItemDao dao =new StudentErrorItemDao();
	
	@Test
	public void addStudentErrorItem()
	{
		IdNameValuePair p =new IdNameValuePair(2, "数学", 1);
		StudentErrorItemEntry seie =new StudentErrorItemEntry(new ObjectId(), p,new ObjectId("5538497f63e729601ad09a05"),new ObjectId(),"D");
		//dao.addStudentErrorItem(seie);
	}
	

	
	@Test
	public void getSubject()
	{
		//IdNameValuePair dp=dao.getSubject(new ObjectId("5530b6b663e70148e63dffe8"), 1);
		//System.out.println(dp.getBaseEntry());
	}
	
	
	@Test
	public void getErrorItem()
	{
		//StudentErrorItemEntry.Item item=dao.getErrorItem(new ObjectId("5530b6b663e70148e63dffe8"), new ObjectId("5530b6b663e70148e63dffe9"));
		//System.out.println(item.getBaseEntry());
	}
	
	
	@Test
	public void addSubject()
	{
		IdNameValuePair p =new IdNameValuePair(2, "语文", 1);
		//dao.addSubject(new ObjectId("5530b6b663e70148e63dffe8"), p);
	}
	
	
	@Test
	public void addItem()
	{
		StudentErrorItemEntry.Item item =new Item(new ObjectId("553849dc63e7e46397519537"), 2, new ObjectId(), "D");
	//	dao.addItem(new ObjectId("55384cd663e722bf84809f86"), 2, item);
	}
	
	@Test
	public void updateItem()
	{
		//dao.updateItem(new ObjectId("55384cd663e722bf84809f86"), new ObjectId("553849dc63e7e46397519537"));
	}
	
	@Test
	public void getSubjects()
	{
		//List<IdNameValuePair> list=dao.getSubjects(new ObjectId("5530bf2463e743a0b82130c1"));
		//System.out.println(list);
	}
	
	@Test
	public void getItems()
	{
		//List<StudentErrorItemEntry.Item> list =dao.getItems(new ObjectId("552e05f9f6f27442d6b51590"), 1,4, 0, 10);
		//System.out.println(list);
	}
	
	
	
	
	
	
}
