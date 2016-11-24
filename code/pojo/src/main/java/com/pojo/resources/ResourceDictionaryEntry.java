package com.pojo.resources;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.IdValuePair;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;

/**
 * 资源字典
 * <pre>
 * collectionName:resdictionary
 * </pre>
 * <pre>
 *      章节： 1学段 ->2学科-> 3版本 ->4年级-> 5单元-> 6课
 *      知识点：1学段-> 2学科->  7知识面 -> 8知识点 ->9小知识点
 *      题型 : 1学段-> 2学科->10题型
 * </pre>
 * <pre>
	{
	  ty:类型        1学段 2学科 3版本 4年级 5单元 6课 7知识面  8知识点 9小知识点 10题型
	  nm:名字
	  pid:父节点ID
	  pinfos:父类集合；按照顺序一次是一级，二级，一次类推
	      [
	         {
	          id:
	          v:
	         }
	         {
	          id:
	          v:
	         }
	      ]
      st: sort排序字段，
      c:题目数量
	 }
 * </pre>
 * @author fourer
 */
public class ResourceDictionaryEntry extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3327761755946283652L;
	
	public ResourceDictionaryEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	public ResourceDictionaryEntry(int type,
			String name, ObjectId parentId,List<IdValuePair> parentList) {
		super();
		List<DBObject> list=MongoUtils.fetchDBObjectList(parentList);
		BasicDBObject dbo =new BasicDBObject()
		.append("ty", type)
		.append("nm", name)
		.append("pid", parentId)
		.append("pinfos", MongoUtils.convert(list))
                .append("st",0)
                .append("c", 0);
		;
		
		setBaseEntry(dbo);
	}
	
	
	public int getCount() {
		 return getSimpleIntegerValueDef("c",0);
	}

	public void setCount(int count) {
		setSimpleValue("c", 0);
	}

	public int getType() {
		return getSimpleIntegerValue("ty");
	}
	public void setType(int type) {
		setSimpleValue("ty", type);
	}
	
	public String getName() {
		return getSimpleStringValue("nm");
	}
	public void setName(String name) {
		setSimpleValue("nm", name);
	}
	public ObjectId getParentId() {
		return getSimpleObjecIDValue("pid");
	}
	public void setParentId(ObjectId parentId) {
		setSimpleValue("pid", parentId);
	}
	public List<IdValuePair> getParentInfos() {
		List<IdValuePair> retList =new ArrayList<IdValuePair>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("pinfos");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add(new IdValuePair((BasicDBObject)o));
			}
		}
		return retList;
	}
	public void setParentInfos(List<IdValuePair> parentInfos) {
		List<DBObject> list=MongoUtils.fetchDBObjectList(parentInfos);
		setSimpleValue("pinfos", MongoUtils.convert(list));
	}
    public long getSort(){return getSimpleLongValue("st");}
    public void setSort(long sort){setSimpleValue("st", sort);}
	
}
