package com.pojo.school;

import java.util.ArrayList;
import java.util.List;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;

/**
 * 导航条目
 * @author fourer
 *
 *<pre>
 * collectionName:navitems
 *</pre>
 *
 *<pre>
 * nm:名称
 * its:条目集合
 * [
	 * {
	 * r:拥有权限
	 * link:连接
	 * }
 * ]
 *</pre>
 */
public class NavigationItemEntry extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4778824479812261447L;
	
	public NavigationItemEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	public NavigationItemEntry(String name, List<NavigationLinkItem> items) {
		super();
		BasicDBObject dbo =new BasicDBObject()
		.append("nm", name)
		.append("its", MongoUtils.convert(MongoUtils.fetchDBObjectList(items)));
		setBaseEntry(dbo);
	}
	public String getName() {
		return getSimpleStringValue("nm");
	}

	public void setName(String name) {
		setSimpleValue("nm", name);
	}

	public List<NavigationLinkItem> getItems() {
		List<NavigationLinkItem> retList =new ArrayList<NavigationLinkItem>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("its");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add(new NavigationLinkItem((BasicDBObject)o));
			}
		}
		return retList;
	}

	public void setItems(List<NavigationLinkItem> items) {
		List<DBObject> list=MongoUtils.fetchDBObjectList(items);
		setSimpleValue("its", MongoUtils.convert(list));
	}


	/**
	 * @author fourer
	 * {
	 *   r:拥有权限
	 *   link:连接
	 * }
	 */
	public static class NavigationLinkItem extends BaseDBObject
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -7695812735485035292L;
		
		public NavigationLinkItem(BasicDBObject baseEntry) {
			super(baseEntry);
		}
		
		
		public NavigationLinkItem(int role, String link) {
			super();
			BasicDBObject dbo =new BasicDBObject()
			.append("r", role)
			.append("link", link);
			setBaseEntry(dbo);
		}
		public int getRole() {
			return getSimpleIntegerValue("r");
		}
		public void setRole(int role) {
			setSimpleValue("r", role);
		}
		public String getLink() {
			return getSimpleStringValue("link");
		}
		public void setLink(String link) {
			setSimpleValue("link", link);
		}
	}

}
