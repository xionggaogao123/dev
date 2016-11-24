package com.fulaan.property.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.db.factory.MongoFacroty;
import com.db.property.PropertyDao;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.property.PropertyEntry;
import com.sys.constants.Constant;
/**
 * 
 * @author cxy
 *
 */
@Service
public class PropertyService {
	
	PropertyDao propertyDao = new PropertyDao();
	
	/**
	 * 添加一条校园资产信息
	 * @param e
	 * @return
	 */
	public ObjectId addPropertyEntry(PropertyEntry e){
		return propertyDao.addPropertyEntry(e);
	}
	
	/**
	 * 根据Id查询一个特定的校园资产信息
	 * @param id
	 * @return
	 */
	public PropertyEntry getPropertyEntry(ObjectId id){
		return propertyDao.getPropertyEntry(id);
	}
	
	/**
	 * 删除一条校园资产
	 * @param id
	 */
	public void deleteProperty(ObjectId id){
		propertyDao.deleteProperty(id);;
	}
	
	/**
	 * 根据ID更新一条校园资产信息
	 */
	public void updateProperty(ObjectId id,String propertyNumber,String propertyName,
							String propertySpecifications,String propertyOrgin,String propertyBrand){
		propertyDao.updateProperty(id, propertyNumber, propertyName, propertySpecifications, propertyOrgin, propertyBrand);;
		
	}
	
	/**
	 * 根据学校ID和分类ID查询校产信息
	 * @param id
	 * @return
	 */
	public List<PropertyEntry> queryPropertiesBySchoolIdAndPropertyClassificationId(ObjectId schoolId,ObjectId propertyClassificationId)
	{
		return propertyDao.queryPropertiesBySchoolIdAndPropertyClassificationId(schoolId, propertyClassificationId);
	}
	
	/**
	 * 根据分类，删除校园资产
	 * @param id
	 */
	public void deletePropertiesByPropertyClassificationId(ObjectId id){
		propertyDao.deletePropertiesByPropertyClassificationId(id); 
	}
}
