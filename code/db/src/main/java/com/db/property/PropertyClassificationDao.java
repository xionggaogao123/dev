package com.db.property;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.property.PropertyClassificationEntry;
import com.sys.constants.Constant;

/**
 * 校产分类Dao
 * @author cxy
 * 2015-8-6 10:24:47
 * 
 */
public class PropertyClassificationDao extends BaseDao{
	/**
	 * 添加一条校园资产分类信息
	 * @param e
	 * @return
	 */
	public ObjectId addPropertyClassificationEntry(PropertyClassificationEntry e)
	{
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_PROPERTY_CLASSIFICATION, e.getBaseEntry());
		return e.getID();
	}
	
	/**
	 * 根据Id查询一个特定的校园资产分类信息
	 * @param id
	 * @return
	 */
	public PropertyClassificationEntry getPropertyClassificationEntry(ObjectId id)
	{
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_PROPERTY_CLASSIFICATION, query, Constant.FIELDS);
		if(null!=dbo)
		{
			return new PropertyClassificationEntry((BasicDBObject)dbo);
		}
		return null;
	}
	
	/**
	 * 删除一条校园资产分类
	 * @param id
	 */
	public void deletePropertyClassification(String id){
		DBObject query =new BasicDBObject("pcid",id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject().append("ir", 1));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_PROPERTY_CLASSIFICATION, query, updateValue);
	}
	
	/**
	 * 根据ID更新一条校园资产分类信息
	 */
	public void updatePropertyClassification(ObjectId id,String propertyClassificationName,String propertyClassificationPostscript,
												String propertyClassificationId,String propertyClassificationParentId){

		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,
													new BasicDBObject()
														.append("pcna", propertyClassificationName)
														.append("pcps", propertyClassificationPostscript)
														.append("pcid", propertyClassificationId)
														.append("pcpid", propertyClassificationParentId));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_PROPERTY_CLASSIFICATION, query, updateValue);
		
	}
	/**
	 * 根据ID更新一条校园资产分类信息的基本信息
	 */
	public void updatePropertyClassificationBaseInfo(String id,String propertyClassificationName,String propertyClassificationPostscript){

		DBObject query =new BasicDBObject("pcid",id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,
													new BasicDBObject()
														.append("pcna", propertyClassificationName)
														.append("pcps", propertyClassificationPostscript));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_PROPERTY_CLASSIFICATION, query, updateValue);
		
	}
	/**
	 * 根据学校ID查询校产信息
	 * @param id
	 * @return
	 */
	public List<PropertyClassificationEntry> queryPropertyClassificationBySchoolIdAndPropertyClassificationId(ObjectId schoolId)
	{
		BasicDBObject query = new BasicDBObject();
		query.append("scid",schoolId)
			 .append("ir",Constant.ZERO);
		DBObject orderBy = new BasicDBObject("_id",Constant.ASC); 
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(),Constant.COLLECTION_PROPERTY_CLASSIFICATION,query,Constant.FIELDS,orderBy);
        List<PropertyClassificationEntry> resultList = new ArrayList<PropertyClassificationEntry>();
        for(DBObject dbObject:dbObjects){
        	PropertyClassificationEntry propertyClassificationEntry = new PropertyClassificationEntry((BasicDBObject)dbObject);
        	resultList.add(propertyClassificationEntry);
        }
		return resultList;
	}
	
	/**
	 * 根据父ID查询所有孩子校产信息
	 * @param id
	 * @return
	 */
	public List<PropertyClassificationEntry> queryPropertyClassificationByParentId(String parentId)
	{
		BasicDBObject query = new BasicDBObject();
		query.append("pcpid",parentId)
			 .append("ir",Constant.ZERO);
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(),Constant.COLLECTION_PROPERTY_CLASSIFICATION,query,Constant.FIELDS);
        List<PropertyClassificationEntry> resultList = new ArrayList<PropertyClassificationEntry>();
        for(DBObject dbObject:dbObjects){
        	PropertyClassificationEntry propertyClassificationEntry = new PropertyClassificationEntry((BasicDBObject)dbObject);
        	resultList.add(propertyClassificationEntry);
        }
		return resultList;
	}
	
	/**
	 * 删除一个学校所有校园资产分类
	 * @param id
	 */
	public void deleteAllPropertyClassifications(ObjectId id){
		DBObject query = new BasicDBObject("scid",id);
		remove(MongoFacroty.getAppDB(), Constant.COLLECTION_PROPERTY_CLASSIFICATION, query);
	}
}
