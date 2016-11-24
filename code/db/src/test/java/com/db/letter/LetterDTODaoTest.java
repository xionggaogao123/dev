package com.db.letter;

import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Test;

import com.pojo.letter.LetterEntry;
import com.pojo.letter.LetterState;
import com.sys.constants.Constant;
import com.sys.exceptions.ResultTooManyException;

public class LetterDTODaoTest {

	LetterDao dao =new LetterDao();
	
	@Test
	public void addLetterEntry()
	{
	
		LetterEntry e =new LetterEntry(new ObjectId("54f6a15cfe5bd6dd37a6b2a7"), "查询", new ObjectId("54f6a15cfe5bd6dd37a6b2a8"));
		ObjectId id=dao.addLetterEntry(e);
		System.out.println(id);
	}
	
	@Test
	public void senderDelete()
	{
		dao.senderDelete(new ObjectId("54f6a199fe5b07270c6cbbdf"), new ObjectId("54f6a199fe5b07270c6cbbe1"));
	}
	
	
	@Test
	public void receiverDelete()
	{
		dao.receiverDelete(new ObjectId("54f6a199fe5b07270c6cbbe0"), new ObjectId("54f6a199fe5b07270c6cbbe1"));
	}
	
	
	@Test
	public void writeReply()
	{
		dao.writeReply(new ObjectId("54f6a301fe5b5f5cebb38baa"), new ObjectId("54f6a301fe5b5f5cebb38ba9"), new ObjectId());
	}
	

	
	@Test
	public void getLetterEntryList () throws ResultTooManyException
	{
		List<LetterEntry> list =dao.getLetterEntryList(new ObjectId("54f6a15cfe5bd6dd37a6b2a7"), 1, 1, new ObjectId("54f6a15cfe5bd6dd37a6b2a8"), 1, 0, 10, Constant.FIELDS);
		System.out.println(list.size());
		for(LetterEntry e:list)
		{
			System.out.println(e);
		}
		
	}
	
}
