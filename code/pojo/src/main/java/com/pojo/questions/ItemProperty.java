package com.pojo.questions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
/**
 * 属性信息，依附于ItemStoreEntry
 * 		   tcv(teachingVersion):教材版本属性信息 
		   [
		   		{
		   			id : 
		   			nm :
		   		}
		   ]
		  
		  kpn(knowledgePoint) : 综合知识点属性信息 
		   [
		   		{
		   			id : 
		   			nm :
		   		}
		   ]
		   
		   
		  
 * @author zoukai
 * 2015-9-16
 */
public class ItemProperty extends BaseDBObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ItemProperty() {
		super();
	}
	
	public ItemProperty(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	public ItemProperty(List<PropertiesObj> teachingVersion,List<PropertiesObj> knowledgePoint) {
		super();
		BasicDBObject baseEntry =new BasicDBObject().append("tcv",MongoUtils.convert(MongoUtils.fetchDBObjectList(teachingVersion)))
													.append("kpn",MongoUtils.convert(MongoUtils.fetchDBObjectList(knowledgePoint)));
		
									setBaseEntry(baseEntry);
	}
	
	public List<PropertiesObj> getTeachingVersion() {
		List<PropertiesObj> resultList =new ArrayList<PropertiesObj>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("tcv");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				resultList.add(new PropertiesObj((BasicDBObject)o));
			}
		}
		return resultList;
	}
	public void setTeachingVersion(Collection<PropertiesObj> teachingVersion) {
		List<DBObject> list= MongoUtils.fetchDBObjectList(teachingVersion);
		setSimpleValue("tcv", MongoUtils.convert(list));
	}
	
	public List<PropertiesObj> getKnowledgePoint() {
		List<PropertiesObj> resultList =new ArrayList<PropertiesObj>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("kpn");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				resultList.add(new PropertiesObj((BasicDBObject)o));
			}
		}
		return resultList;
	}
	public void setKnowledgePoint(Collection<PropertiesObj> knowledgePoint) {
		List<DBObject> list= MongoUtils.fetchDBObjectList(knowledgePoint);
		setSimpleValue("kpn", MongoUtils.convert(list));
	}
}

