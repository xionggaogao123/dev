package com.pojo.calendar;


import java.text.MessageFormat;

import com.sys.utils.DateTimeUtils;

public class EventDTO {

	private static final String POS_STR="left:{0}px;top:{1}px;width:{2}px;height:{3}px";
	private static final String POS_STR1="top:{0}px;width:{1}px;height:{2}px";
	private String id;
	private String orgId;
	private int type;
	private String beginTime;
	private String endTime;
	private String title;
	private String content;
	
	
	
	private int left;
	private int top;
	
	private int width;
	private int height;
	
	
	
   private String pos;
   @SuppressWarnings("unused")
    private String pos1;
	
	
   //对应Event的e.getBeginTime()
   private long origBeginTime;
	
	public EventDTO(Event e )
	{
		this.id=String.valueOf(e.getType())+"_"+e.getID().toString();
		
		if(e.getBaseEntry().containsField("orgId"))
		{
		   this.orgId=e.getBaseEntry().getString("orgId");
		}else
		{
			this.orgId=e.getID().toString();
		}
		this.type=e.getType();
		
		this.beginTime=DateTimeUtils.convert(e.getBeginTime(), DateTimeUtils.DATE_HH_MM);
		this.endTime=DateTimeUtils.convert(e.getEndTime(), DateTimeUtils.DATE_HH_MM);
		this.title=e.getTitle();
		this.content=e.getContent();
		this.origBeginTime=e.getBeginTime();
	}
	
	
	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}


	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getLeft() {
		return left;
	}
	public void setLeft(int left) {
		this.left = left;
	}
	public int getTop() {
		return top;
	}
	public void setTop(int top) {
		this.top = top;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}


	public String getPos() {
		pos= MessageFormat.format(POS_STR, String.valueOf(this.left),String.valueOf(this.top),String.valueOf(this.width),String.valueOf(this.height));
		return pos;
	}


	public void setPos(String pos) {
		this.pos = pos;
	}

	
	

	public String getPos1() {
		pos= MessageFormat.format(POS_STR1,String.valueOf(this.top),String.valueOf(this.width),String.valueOf(this.height));
		return pos;
	}


	public void setPos1(String pos) {
		this.pos = pos;
	}

	public String getOrgId() {
		return orgId;
	}


	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}


	public long getOrigBeginTime() {
		return origBeginTime;
	}


	public void setOrigBeginTime(long origBeginTime) {
		this.origBeginTime = origBeginTime;
	}
	
	
	
	
	
	
}
