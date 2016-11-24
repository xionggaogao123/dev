package com.pojo.calendar;

import com.sys.constants.Constant;

public class MiddleDTO {

	private Event event;
	private int count;
	private int left; //从0开始
	public MiddleDTO(Event event)
	{
		this.event=event;
		this.count =Constant.ONE;
		left=Constant.ZERO;
	}
	
	public MiddleDTO(Event event,int count)
	{
		this.event=event;
		this.count =count;
	}
	
	
	
	
	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	/**
	 * 增加影响数目
	 */
	public void increaseCount()
	{
		this.count++;
	}
	
	
	public void increaseleft()
	{
		this.left++;
	}

	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	@Override
	public String toString() {
		return "MiddleDTO [event=" + event + ", count=" + count + ", left="
				+ left + "]";
	}
	
	
	
	
}
