package com.fulaan.calendar.service;

import java.util.List;

import org.bson.types.ObjectId;

import com.db.calendar.LoopEventDao;
import com.pojo.calendar.LoopEvent;

public class LoopEventService {

	private LoopEventDao dao =new LoopEventDao();
	
	public ObjectId addLoopEvent(LoopEvent e)
	{
		return dao.addLoopEvent(e);
	}
	public LoopEvent getLoopEvent(ObjectId userid, ObjectId id)
	{
		return dao.getLoopEvent(userid, id);
	}
	
	public void updateRemoves(ObjectId userid, ObjectId id, long begintime)
	{
		dao.updateRemoves(userid, id, begintime);
	}
	
	
	public void remove(ObjectId userid, ObjectId id)
	{
		dao.remove(userid, id);
	}
	
	
	
	public List<LoopEvent> getLoopEventList(ObjectId userid, long endTime)
	{
		return dao.getLoopEventList(userid, endTime);
	}
	
	
	public void addDeleteDate(String date, ObjectId userid, ObjectId id)
	{
		dao.addDeleteDate(date, userid, id);
	}
}
