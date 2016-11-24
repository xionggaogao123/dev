package com.pojo.exercise;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;

/**
 * 测试题目
 * <pre>
 * collectionName:exerciseitems
 * </pre>
 * <pre>
 * {
 *  di:文档ID  对应exerciseEntry的_id
 *  nm:名字
 *  ti:大题目号，1,2，3
 *  spt:应该所花时间;单位秒, -1为未配置
 *  its:大题题目列表
 *  [
 *   * {
 *   *  id:
	 *  ti:小题目号，1，2，3
	 *  ty:题目类型；ExerciseItemType
	 *  so:分值
	 *  opt:选项
	 *  [
	 *   A,
	 *   B,
	 *  ]
	 *  as:答案；选择：ABCD  判断：1表示该答案为“对”，0 表示该答案为“错”  填空：具体答案      主观：具体答案
	 * }
 *  ]
 * }
 * </pre>
 * @author fourer
 */
public class ExerciseItemEntry extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4708303572768234055L;
	
	public ExerciseItemEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	public ExerciseItemEntry(ObjectId documentId, String name,String titleId,int spendTime) {
		super();
		BasicDBObject baseEntry =new BasicDBObject()
		.append("di", documentId)
		.append("nm", name)
		.append("ti", titleId)
		.append("spt", spendTime)
		.append("its", new BasicDBList())
		;
		setBaseEntry(baseEntry);
	}
	
	
	public ExerciseItemEntry(ObjectId documentId,String name, String titleId,int spendTime,List<Item> items) {
		super();
		BasicDBObject baseEntry =new BasicDBObject()
		.append("di", documentId)
		.append("nm", name)
		.append("ti", titleId)
		.append("spt", spendTime)
		.append("its", MongoUtils.convert(MongoUtils.fetchDBObjectList(items)))
		;
		setBaseEntry(baseEntry);
	}
	
	public String getName() {
		return getSimpleStringValue("nm");
	}

	public void setName(String name) {
		setSimpleValue("nm", name);
	}

	public List<Item> getItemList() {
        List<Item> itemList =new ArrayList<Item>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("its");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				itemList.add(  new Item((BasicDBObject)o));
			}
		}
		return itemList;
	}
	
	public void setItemList(List<Item> itemList) {
		List<DBObject> list =MongoUtils.fetchDBObjectList(itemList);
		setSimpleValue("its", MongoUtils.convert(list));
	}
	
	public ObjectId getDocumentId() {
		return getSimpleObjecIDValue("di");
	}
	public void setDocumentId(ObjectId documentId) {
		setSimpleValue("di", documentId);
	}
	public String getTitleId() {
		return getSimpleStringValue("ti");
	}
	public void setTitleId(String titleId) {
		setSimpleValue("ti", titleId);
	}
	public int getSpendTime() {
		return getSimpleIntegerValue("spt");
	}
	public void setSpendTime(int spendTime) {
		setSimpleValue("spt", spendTime);
	}
	
	
	

	
	/**
	 * 小题目
	 * <pre>
	 * {
	 *  id:
	 *  ti:小题目号，1，2，3
	 *  ty:题目类型；ExerciseItemType
	 *  so:分值
	 *  opt:选项
	 *  [
	 *   A,
	 *   B,
	 *  ]
	 *  as:答案；选择：ABCD  判断：1表示该答案为“对”，0 表示该答案为“错”  填空：具体答案      主观：具体答案
	 * }
	 * </pre>
	 * @author fourer
	 */
	public static  class Item extends BaseDBObject
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = -6628710382995277852L;
		
		public Item(BasicDBObject baseEntry) {
			super(baseEntry);
		}
		public Item( String titleId, int type,
	                             double score, String answers,int selectCount,List<String> opt) {
			super();
			BasicDBObject baseEntry =new BasicDBObject()
			.append("id", new ObjectId())
			.append("ti", titleId)
			.append("ty", type)
			.append("so", score)
			.append("as", answers)
			.append("sc", selectCount)
			.append("opt", MongoUtils.convert(opt))
			
			;
			setBaseEntry(baseEntry);
		}
		
		public List<String> getOptions() {
			 List<String> itemList =new ArrayList<String>();
				BasicDBList list =(BasicDBList)getSimpleObjectValue("opt");
				if(null!=list && !list.isEmpty())
				{
					for(Object o:list)
					{
						itemList.add((String)o);
					}
				}
				return itemList;
		}
		public void setOptions(List<String> options) {
			setSimpleValue("opt", MongoUtils.convert(options));
		}
		public int getSelectCount() {
			return getSimpleIntegerValue("sc");
		}
		public void setSelectCount(int selectCount) {
			setSimpleValue("sc", selectCount);
		}
		public ObjectId getId() {
			return getSimpleObjecIDValue("id");
		}
		public void setId(ObjectId id) {
			setSimpleValue("id", id);
		}
		public String getTitleId() {
			return getSimpleStringValue("ti");
		}
		public void setTitleId(String titleId) {
			setSimpleValue("ti", titleId);
		}
		public int getType() {
			return getSimpleIntegerValue("ty");
		}
		public void setType(int type) {
			setSimpleValue("ty", type);
		}
		public double getScore() {
			return getSimpleDoubleValue("so");
		}
		public void setScore(double score) {
			setSimpleValue("so", score);
		}
		public String getAnswers() {
			return getSimpleStringValue("as");
		}
		public void setAnswers(String answers) {
			setSimpleValue("as", answers);
		}
	}
	
}
