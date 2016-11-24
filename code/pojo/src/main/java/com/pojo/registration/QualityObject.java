package com.pojo.registration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.repertory.PropertyObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
/**
 * 评价项目，依附于GrowthRecordEntry
 {
           nm(name) : 评价项目名称
           lv(level) : 评价等级
		   
		   subs[ 参见SubQualityObject
		   		{
		   			nm(name) : 评价项目名称
		   			rq(requirement) : 主要表现和要求
		   			slv(subLevel) : 自我评价等级
		   			tlv(subLevel) : 教师评价等级
		   		}
		   ]
 }
 * @author cxy
 * 2015-9-27 17:01:34
 */
public class QualityObject extends BaseDBObject  {
	public QualityObject(BasicDBObject baseEntry) {
		super(baseEntry);
	}

	public QualityObject(String name,List<SubQualityObject> subQualityList) {
		super();

		BasicDBObject baseEntry = new BasicDBObject().append("nm", name)
													 .append("lv",Constant.EMPTY)
													 .append("subs",MongoUtils.convert(MongoUtils.fetchDBObjectList(subQualityList)));

		setBaseEntry(baseEntry);
	}
	
	public String getName() {
		return getSimpleStringValue("nm");
	}
	public void setName(String name) {
		setSimpleValue("nm", name);
	}
	
	public String getLevel() {
		return getSimpleStringValue("lv");
	}
	public void setLevel(String level) {
		setSimpleValue("lv", level);
	}
	
	
	
	public List<SubQualityObject> getSubQualityList() {
		List<SubQualityObject> resultList =new ArrayList<SubQualityObject>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("subs");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				resultList.add(new SubQualityObject((BasicDBObject)o));
			}
		}
		return resultList;
	}
	public void setSubQualityList(Collection<SubQualityObject> levels) {
		List<DBObject> list= MongoUtils.fetchDBObjectList(levels);
		setSimpleValue("subs", MongoUtils.convert(list));
	}
}
