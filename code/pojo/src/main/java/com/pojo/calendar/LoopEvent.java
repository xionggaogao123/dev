package com.pojo.calendar;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
  collectionName:loopEvent
 
 {
  uid:发布者
  ty:类型 1:课程 2：日程
  tit:标题
  con:内容
  bt:开始时间
  et:结束时间
  reb:操作时间 ,默认为“” 删除的时候是2255555555L值
  rt:
  {
    lp:频率 1日    2月    3年
    dv:当选择为日时， 0表示每个工作日重复    3：表示每3天重复
    edt:何时结束 0一直持续   1 循环几次后结束 2 具体日期结束
    edv:0,5,122222222552(long)
  }
  dld[]:已经删除的天数 2015-01-02，2015-04-12
 }
 

 * @author fourer
 *
 */
public class LoopEvent extends Event {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1137504566540238370L;
	
	
	
	public LoopEvent(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	
	public LoopEvent(ObjectId uid, int type, String title, String content,
			long beginTime, long endTime,EventLoopType loopType) {
		BasicDBObject dbo =new BasicDBObject()
        .append("uid", uid)
        .append("ty", type)
        .append("tit", title)
        .append("con", content)
        .append("bt", beginTime)
        .append("et", endTime)
        .append("reb", 0L)
        .append("dld", new BasicDBList())
        .append("rt", loopType.getBaseEntry());
        setBaseEntry(dbo);
	}



	public Set<String> getDeleteDates()
	{
		Set<String> deleteSet =new HashSet<String>();
		
		BasicDBList list =(BasicDBList)getSimpleObjectValue("dld");
		if(null!=list && list.size()>0)
		{
			for(Object o:list)
			{
				deleteSet.add(o.toString());
			}
		}
		return deleteSet;
	}
	


	public EventLoopType getLoopType() {
		
		DBObject dbo= (DBObject)getSimpleObjectValue("rt");
		if(null!=dbo)
		{
			return new EventLoopType((BasicDBObject)dbo);
		}
		return null;
	}
	
	public void setLoopType(EventLoopType loopType) {
		setSimpleValue("rt", loopType.getBaseEntry());
	}
	

	
	public Event toEvent(ObjectId orgId,long beginTime,long endTime)
	{
		ObjectId id =new ObjectId(new Date(beginTime));
		Event e= new Event(this.getUid(), this.getType(), this.getTitle(), this.getContent(), beginTime, endTime);
		e.setID(id);
		e.getBaseEntry().append("orgId", orgId);
		return e;
	}
	

	public long getRemoveBeginTime() {
		return getSimpleLongValue("reb");
	}



	public void setRemoveBeginTime(long removeBeginTime) {
		setSimpleValue("reb", removeBeginTime);
	}

}
