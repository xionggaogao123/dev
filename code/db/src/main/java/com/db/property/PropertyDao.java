package com.db.property;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.property.PropertyEntry;
import com.sys.constants.Constant;
/**
 * 校园资产Dao
 * 2015-7-28 15:23:29
 * @author cxy
 *
 */
public class PropertyDao extends BaseDao{
	
	/**
	 * 添加一条校园资产信息
	 * @param e
	 * @return
	 */
	public ObjectId addPropertyEntry(PropertyEntry e)
	{
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_PROPERTY, e.getBaseEntry());
		return e.getID();
	}
	
	/**
	 * 根据Id查询一个特定的校园资产信息
	 * @param id
	 * @return
	 */
	public PropertyEntry getPropertyEntry(ObjectId id)
	{
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_PROPERTY, query, Constant.FIELDS);
		if(null!=dbo)
		{
			return new PropertyEntry((BasicDBObject)dbo);
		}
		return null;
	}
	
	/**
	 * 删除一条校园资产
	 * @param id
	 */
	public void deleteProperty(ObjectId id){
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject().append("ir", 1));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_PROPERTY, query, updateValue);
	}
	
	/**
	 * 根据ID更新一条校园资产信息
	 */
	public void updateProperty(ObjectId id,String propertyNumber,String propertyName,
							String propertySpecifications,String propertyOrgin,String propertyBrand){

		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,
													new BasicDBObject()
														.append("pnu", propertyNumber)
														.append("pna", propertyName)
														.append("psp", propertySpecifications)
														.append("po", propertyOrgin)
														.append("pb", propertyBrand));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_PROPERTY, query, updateValue);
		
	}
	/**
	 * 根据学校ID和分类ID查询校产信息
	 * @param id
	 * @return
	 */
	public List<PropertyEntry> queryPropertiesBySchoolIdAndPropertyClassificationId(ObjectId schoolId,ObjectId propertyClassificationId)
	{
		BasicDBObject query = new BasicDBObject();
		query.append("pcid", propertyClassificationId)
			 .append("scid",schoolId)
			 .append("ir",Constant.ZERO);
		DBObject orderBy = new BasicDBObject("pnu",Constant.ASC); 
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(),Constant.COLLECTION_PROPERTY,query,Constant.FIELDS,orderBy);
        List<PropertyEntry> resultList = new ArrayList<PropertyEntry>();
        for(DBObject dbObject:dbObjects){
        	PropertyEntry propertyEntry = new PropertyEntry((BasicDBObject)dbObject);
        	resultList.add(propertyEntry);
        }
		return resultList;
	}
	/**
	 * 根据分类，删除校园资产
	 * @param id
	 */
	public void deletePropertiesByPropertyClassificationId(ObjectId id){ 
		DBObject query =new BasicDBObject("pcid",id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject().append("ir", 1));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_PROPERTY, query, updateValue);
	}
}
