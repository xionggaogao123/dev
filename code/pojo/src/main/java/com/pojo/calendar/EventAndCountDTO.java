package com.pojo.calendar;

import java.util.ArrayList;
import java.util.List;

/**
 * 某天的事件以及数目，用于月历
 * @author fourer
 *
 */
public class EventAndCountDTO {

	private String time;
	private List<EventDTO> list =new ArrayList<EventDTO>();
	
	private int count;
	
	public EventAndCountDTO(String time,EventDTO dto)
	{
		this.time=time;
		list.add(dto);
	}
	

	public List<EventDTO> getList() {
		return list;
	}

	public void setList(List<EventDTO> list) {
		this.list = list;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getTime() {
		return time;
	}


	public void setTime(String time) {
		this.time = time;
	}
	

	public void increaseCount()
	{
		this.count=this.count+1;
	}


	@Override
	public String toString() {
		return "EventAndCountDTO [time=" + time + ", list=" + list + ", count="
				+ count + "]";
	}
	
	
	
	
	
}
