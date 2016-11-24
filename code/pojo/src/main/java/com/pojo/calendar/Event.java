package com.pojo.calendar;


import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

/**
 * 日历事件
 * <pre>
 * collectionName:events
 * </pre>
 * <pre>
 * {
 *  uid:发布者
 *  ty:类型 1:课程 2：日程  3通知
 *  tit:标题
 *  con:内容
 *  bt:开始时间
 *  et:结束时间
 * }
 * </pre>
 * @author fourer
 */
public class Event extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3808402127810209578L;
	
	
	public Event() {
	}
	
	public Event(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	public Event(ObjectId uid,int type, String title, String content,long beginTime,long endTime
			 ) {
		super();
		BasicDBObject dbo =new BasicDBObject()
		                      .append("uid", uid)
		                      .append("ty", type)
		                      .append("tit", title)
		                      .append("con", content)
				              .append("bt", beginTime)
				              .append("et", endTime)
				              ;
		setBaseEntry(dbo);
	}

	public ObjectId getUid() {
		return getSimpleObjecIDValue("uid");
	}
	public void setUid(ObjectId uid) {
		setSimpleValue("uid", uid);
	}
	
	public int getType() {
		return getSimpleIntegerValue("ty");
	}
	public void setType(int type) {
		setSimpleValue("ty", type);
	}
	
	
	public String getTitle() {
		return getSimpleStringValue("tit");
	}

	public void setTitle(String title) {
		setSimpleValue("tit", title);
	}
	
	public String getContent() {
		return getSimpleStringValue("con");
	}

	public void setContent(String con) {
		setSimpleValue("con", con);
	}
	
	public long getBeginTime() {
		return getSimpleLongValue("bt");
	}
	public void setBeginTime(long beginTime) {
		setSimpleValue("bt", beginTime);
	}

	public long getEndTime() {
		return getSimpleLongValue("et");
	}
	public void setEndTime(long endTime) {
		setSimpleValue("et", endTime);
	}
}
