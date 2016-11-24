package com.pojo.repertory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.school.Grade;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

/**
 * 属性信息，依附于CoursewareEntry
 * 		   ver(version) : 教材版本属性信息 PropertyVersion
		   [
		   		{
		   			id : 
		   			nm :
		   		}
		   ]
		   kno(knowledge) : 知识点属性信息 PropertyKnowledge
		   [
		   		{
		   			id : 
		   			nm :
		   		}
		   ]
 * @author cxy
 * 2015-9-15 21:01:51
 */
public class RepertoryProperty extends BaseDBObject{
	public RepertoryProperty(BasicDBObject baseEntry) {
		super(baseEntry);
	}


	public RepertoryProperty(List<PropertyObject> versionList, List<PropertyObject> knowledgeList) {
		super();
		BasicDBObject baseEntry =new BasicDBObject()
		.append("ver", MongoUtils.convert(MongoUtils.fetchDBObjectList(versionList)))
		.append("kno", MongoUtils.convert(MongoUtils.fetchDBObjectList(knowledgeList)));
		setBaseEntry(baseEntry);
	}
	
	public List<PropertyObject> getVersionList() {
		List<PropertyObject> resultList =new ArrayList<PropertyObject>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("ver");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				resultList.add(new PropertyObject((BasicDBObject)o));
			}
		}
		return resultList;
	}
	public void setVersionList(Collection<PropertyObject> versionList) {
		List<DBObject> list= MongoUtils.fetchDBObjectList(versionList);
		setSimpleValue("ver", MongoUtils.convert(list));
	}
	
	
	public List<PropertyObject> getKnowledgeList() {
		List<PropertyObject> resultList =new ArrayList<PropertyObject>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("kno");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				resultList.add(new PropertyObject((BasicDBObject)o));
			}
		}
		return resultList;
	}
	public void setKnowledgeListt(Collection<PropertyObject> knowledgeList) {
		List<DBObject> list= MongoUtils.fetchDBObjectList(knowledgeList);
		setSimpleValue("kno", MongoUtils.convert(list));
	}
	
	
}
