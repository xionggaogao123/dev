package com.pojo.school;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;

/**
 * 学校导航
 * @author fourer
 *
 *<pre>
 * collectionName:navs
 *</pre>
 *
 *<pre>
 *{
 * index：序号
 * nm:名字  
 * blks:导航块; 参见NavigationBlock
 * [
 *  {
	 *  nm:名字    比如微校园
	 *  sort:排序   用于左面导航顺序
	 *  link:导航连接；如果此导航块没有子导航的时候，link有值;
	 *  its:
	 *  {
	 *    id:
	 *    sort:
	 *  }
 *  }
 * ]
 *}
 * nm:名称
   so:排序
 *</pre>
 */
public class NavigationEntry extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2901008626039421397L;
	
	
	public NavigationEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}

	public NavigationEntry(int index, String name,
			List<NavigationBlock> items) {
		super();
		List<DBObject> list=MongoUtils.fetchDBObjectList(items);
		BasicDBObject dbo =new BasicDBObject()
		.append("index", index)
		.append("nm", name)
		.append("blks", MongoUtils.convert(list))
		;
		setBaseEntry(dbo);
	}

	public int getIndex() {
		return getSimpleIntegerValue("index");
	}


	public void setIndex(int index) {
		setSimpleValue("index", index);
	}


	public String getName() {
		return getSimpleStringValue("nm");
	}


	public void setName(String name) {
		setSimpleValue("nm", name);
	}


	public List<NavigationBlock> getList() {
		List<NavigationBlock> retList =new ArrayList<NavigationBlock>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("blks");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add(new NavigationBlock((BasicDBObject)o));
			}
		}
		return retList;
	}


	public void setList(List<NavigationBlock> items) {
		List<DBObject> list=MongoUtils.fetchDBObjectList(items);
		setSimpleValue("blks", MongoUtils.convert(list));
	}


	/**
	 * 导航快
	 * @author fourer
	 *
	 */
	public static class NavigationBlock extends BaseDBObject
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 4423143217374904881L;
		
		public NavigationBlock(BasicDBObject baseEntry) {
			super(baseEntry);
		}
		
		public NavigationBlock(String name, int sort, String link,
				List<SortItem> items) {
			super();
			
			List<DBObject> list=MongoUtils.fetchDBObjectList(items);
			BasicDBObject dbo =new BasicDBObject()
			.append("nm", name)
			.append("sort", sort)
			.append("link", link)
			.append("its", MongoUtils.convert(list))
			;
			setBaseEntry(dbo);
		}
		
		public String getName() {
			return getSimpleStringValue("nm");
		}
		public void setName(String name) {
			setSimpleValue("nm", name);
		}
		public int getSort() {
			return getSimpleIntegerValue("sort");
		}
		public void setSort(int sort) {
			setSimpleValue("sort", sort);
		}
		public String getLink() {
			return getSimpleStringValue("link");
		}
		public void setLink(String link) {
			setSimpleValue("link", link);
		}
		
		
		
		public List<SortItem> getItems() {
			List<SortItem> retList =new ArrayList<SortItem>();
			BasicDBList list =(BasicDBList)getSimpleObjectValue("its");
			if(null!=list && !list.isEmpty())
			{
				for(Object o:list)
				{
					retList.add(new SortItem((BasicDBObject)o));
				}
			}
			return retList;
		}
		public void setItems(List<SortItem> items) {
			List<DBObject> list=MongoUtils.fetchDBObjectList(items);
			setSimpleValue("its", MongoUtils.convert(list));
		}
	}
	
	
	/**
	 * 
	 * @author fourer
	 * {
	 *  id:
	 *  sort:
	 * }
	 */
	public static class SortItem extends BaseDBObject
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -5471806638592186829L;
		public SortItem(BasicDBObject baseEntry) {
			super(baseEntry);
		}
		
		public SortItem(ObjectId id, int sort) {
			super();
			BasicDBObject dbo =new BasicDBObject()
			.append("id", id)
			.append("sort", sort);
			setBaseEntry(dbo);
		}

		public ObjectId getId() {
			return getSimpleObjecIDValue("id");
		}
		public void setId(ObjectId id) {
			setSimpleValue("id", id);
		}
		public int getSort() {
			return getSimpleIntegerValue("sort");
		}
		public void setSort(int sort) {
			setSimpleValue("sort", sort);
		}
		
	}

}
