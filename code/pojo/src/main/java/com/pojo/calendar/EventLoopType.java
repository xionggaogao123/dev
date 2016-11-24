package com.pojo.calendar;


import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

/**
 * 循环事件类型；
 * 
  {
    lp:频率  1日    2周    3月  4年
    dv:当选择为日时， 0表示每个工作日重复    3：表示每3天重复
    edt:何时结束 0一直持续   1 循环几次后结束 2 具体日期结束
    edv:0,5,122222222552(long)
  }
 * @author fourer
 *
 */
public class EventLoopType extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -113036423725387389L;

	
	
	public EventLoopType(BasicDBObject baseEntry) {
		super(baseEntry);
		
	}
	
	
	public EventLoopType(int loopType, int dayValue, int endType,
			long endValue) {
		super();
		BasicDBObject dbo =new BasicDBObject()
        .append("lp", loopType)
        .append("dv", dayValue)
        .append("edt", endType)
        .append("edv", endValue)
        ;
        setBaseEntry(dbo);
	}


	public int getLoopType() {
		return getSimpleIntegerValue("lp");
	}
	public void setLoopType(int loopType) {
		setSimpleValue("lp", loopType);
	}
	public int getDayValue() {
		return getSimpleIntegerValue("dv");
	}
	public void setDayValue(int dayValue) {
		setSimpleValue("dv", dayValue);
	}
	public int getEndType() {
		return getSimpleIntegerValue("edt");
	}
	public void setEndType(int endType) {
		setSimpleValue("edt", endType);
	}
	public long getEndValue() {
		return getSimpleLongValue("edv");
	}
	public void setEndValue(long endValue) {
		setSimpleValue("edv", endValue);
	}
	
}
