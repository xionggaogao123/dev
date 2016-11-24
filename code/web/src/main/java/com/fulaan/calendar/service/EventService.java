package com.fulaan.calendar.service;

import java.util.List;

import org.bson.types.ObjectId;

import com.db.calendar.EventDao;
import com.pojo.calendar.Event;

public class EventService {

	private EventDao dao= new EventDao();
	
	
	public Event getEvent(ObjectId userid, ObjectId id)
	{
		return dao.getEvent(userid, id);
	}
	
	
	public void removeEvent(ObjectId userid, ObjectId id)
	{
		dao.removeEvent(userid, id);
	}
	
	
	public ObjectId addEvent(Event e)
	{
		return dao.addEvent(e);
	}
	
	
	public List<Event> getEventList(ObjectId uid, long beginTime, long endTime)
	{
		return dao.getEventList(uid, beginTime, endTime);
	}
}
