package com.pojo.itempool;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;



import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.IdNameValuePair;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;


/**
 * 学生错题库
 * <pre>
 * collectionName:erroritems
 * </pre>
 * <pre>
 * {
 *  ui:
 *  con:总数
 *  subs: IdNameValuePair
 *  [
 *   {
 *    id:
 *    nm:科目名字
 *    v:个数
 *   }
 *  ]
 *  items:题目集合
 *  [
	 *  {
	 *   sub:科目
	 *   sc:知识面
	 *   ans:我的错误答案
	 *   ori:题库id
	 *   con:次数
	 *   maxt:最新时间
	 *   ts:时间
	 *   [
	 *    
	 *   ]
	 *  }
 *  ]
 * }
 * </pre>
 * @author fourer
 */

public class StudentErrorItemEntry extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1152884066705148355L;
	public StudentErrorItemEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	
	public StudentErrorItemEntry(ObjectId ui,IdNameValuePair pair,ObjectId itemId,ObjectId scope,String myAnswer)
	{
		BasicDBList pairList =new BasicDBList();
		pairList.add(pair.getBaseEntry());
		Item item =new Item(itemId,pair.getIntId(),scope,myAnswer);
		BasicDBList itemList =new BasicDBList();
		itemList.add(item.getBaseEntry());
		
		BasicDBObject dbo =new BasicDBObject()
		.append("ui", ui)
		.append("con", 1)
		.append("subs", pairList)
		.append("items",  itemList);
		setBaseEntry(dbo);
	}
	
	
	public StudentErrorItemEntry(ObjectId ui, int count,
			List<IdNameValuePair> subjects, List<Item> items) {
		super();
		
		List<DBObject> list =MongoUtils.fetchDBObjectList(subjects);
		List<DBObject> itemList =MongoUtils.fetchDBObjectList(items);
		BasicDBObject dbo =new BasicDBObject()
		.append("ui", ui)
		.append("con", count)
		.append("subs", MongoUtils.convert(list))
		.append("items",  MongoUtils.convert(itemList));
		setBaseEntry(dbo);
	}



	public ObjectId getUi() {
		return getSimpleObjecIDValue("ui");
	}

	public void setUi(ObjectId ui) {
		setSimpleValue("ui", ui);
	}

	public int getCount() {
		return getSimpleIntegerValue("con");
	}

	public void setCount(int count) {
		setSimpleValue("con", count);
	}

	public List<IdNameValuePair> getSubjects() {
		
		
		 List<IdNameValuePair> times =new ArrayList<IdNameValuePair>();
			BasicDBList list =(BasicDBList)getSimpleObjectValue("subs");
			if(null!=list && !list.isEmpty())
			{
				for(Object o:list)
				{
					times.add( new IdNameValuePair((BasicDBObject)o));
				}
			}
			return times;
	}

	public void setSubjects(List<IdNameValuePair> subjects) {
		List<DBObject> list =MongoUtils.fetchDBObjectList(subjects);
		setSimpleValue("subs", MongoUtils.convert(list));
	}

	public List<Item> getItems() {
		 List<Item> times =new ArrayList<Item>();
			BasicDBList list =(BasicDBList)getSimpleObjectValue("items");
			if(null!=list && !list.isEmpty())
			{
				for(Object o:list)
				{
					times.add( new Item((BasicDBObject)o));
				}
			}
			return times;
	}

	public void setItems(List<Item> items) {
		List<DBObject> list =MongoUtils.fetchDBObjectList(items);
		setSimpleValue("items", MongoUtils.convert(list));
	}

	/**
	 *  {
	 *   sub:科目
	 *   sc:知识面
	 *   ans:我的错误答案
	 *   ori:题库id
	 *   con:次数
	 *   maxt:最新时间
	 *   ts:时间
	 *   [
	 *    
	 *   ]
	 *  }
	 *  */
	public static class Item  extends BaseDBObject
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = -3600311437938530094L;
		
		
		public Item(BasicDBObject baseEntry) {
			super(baseEntry);
		}

		public Item(ObjectId oriId,int subject,ObjectId scope,String myAnswer)
		{
			BasicDBList list =new BasicDBList();
			list.add(System.currentTimeMillis());
		
			BasicDBObject dbo =new BasicDBObject()
			.append("sub", subject)
			.append("ans", myAnswer)
			.append("sc", scope)
			.append("ori", oriId)
			.append("con", 1)
			.append("maxt", System.currentTimeMillis())
			.append("ts", list);
			setBaseEntry(dbo);
		}
		
		public String getMyAnswer() {
			return getSimpleStringValue("ans");
		}


		public void setMyAnswer(String myAnswer) {
			setSimpleValue("ans", myAnswer);
		}


		public int getSubject() {
			return getSimpleIntegerValue("sub");
		}

		public void setSubject(int subject) {
		     setSimpleValue("sub", subject);
		}

		public ObjectId getScope() {
			return getSimpleObjecIDValue("sc");
		}

		public void setScope(ObjectId scope) {
			setSimpleValue("sc", scope);
		}

		public ObjectId getOriId() {
			return getSimpleObjecIDValue("ori");
		}
		public void setOriId(ObjectId oriId) {
			setSimpleValue("ori", oriId);
		}
		public int getCount() {
			return getSimpleIntegerValue("con");
		}
		public void setCount(int count) {
			setSimpleValue("con", count);
		}
		public long getMaxTime() {
			return getSimpleLongValue("maxt");
		}
		public void setMaxTime(long maxTime) {
			setSimpleValue("maxt", maxTime);
		}
		public List<Long> getTimes() {
	        List<Long> times =new ArrayList<Long>();
			BasicDBList list =(BasicDBList)getSimpleObjectValue("ts");
			if(null!=list && !list.isEmpty())
			{
				for(Object o:list)
				{
					times.add((Long)o);
				}
			}
			return times;
		}
		public void setTimes(List<Long> times) {
			setSimpleValue("ts", MongoUtils.convert(times));
		}
	}
	

}
