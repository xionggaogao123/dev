package com.fulaan.property.service;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.db.factory.MongoFacroty;
import com.db.property.PropertyClassificationDao;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.property.PropertyClassificationEntry;
import com.sys.constants.Constant;
/**
 * 
 * @author cxy
 *
 */
@Service
public class PropertyClassificationService {

	private PropertyClassificationDao propertyClassificationDao = new PropertyClassificationDao();
	
	/**
	 * 添加一条校园资产分类信息
	 * @param e
	 * @return
	 */
	public ObjectId addPropertyClassificationEntry(PropertyClassificationEntry e)
	{
		return propertyClassificationDao.addPropertyClassificationEntry(e);
	}
	
	/**
	 * 根据Id查询一个特定的校园资产分类信息
	 * @param id
	 * @return
	 */
	public PropertyClassificationEntry getPropertyClassificationEntry(ObjectId id)
	{
		return propertyClassificationDao.getPropertyClassificationEntry(id);
	}
	
	/**
	 * 删除一条校园资产分类
	 * @param id
	 */
	public void deletePropertyClassification(String id){
		propertyClassificationDao.deletePropertyClassification(id);
	}
	
	/**
	 * 根据ID更新一条校园资产分类信息
	 */
	public void updatePropertyClassification(ObjectId id,String propertyClassificationName,String propertyClassificationPostscript,
													String propertyClassificationId,String propertyClassificationParentId){

		propertyClassificationDao.updatePropertyClassification(id, propertyClassificationName, propertyClassificationPostscript,
				propertyClassificationId, propertyClassificationParentId);
	}
	
	/**
	 * 根据ID更新一条校园资产分类信息的基本信息
	 */
	public void updatePropertyClassificationBaseInfo(String id,String propertyClassificationName,String propertyClassificationPostscript){

		propertyClassificationDao.updatePropertyClassificationBaseInfo(id, propertyClassificationName, propertyClassificationPostscript);
		
	}
	
	/**
	 * 根据学校ID查询校产信息
	 * @param id
	 * @return
	 */
	public List<PropertyClassificationEntry> queryPropertyClassificationBySchoolIdAndPropertyClassificationId(ObjectId schoolId)
	{
		return propertyClassificationDao.queryPropertyClassificationBySchoolIdAndPropertyClassificationId(schoolId);
	}
	
	/**
	 * 根据父ID查询所有孩子校产信息
	 * @param id
	 * @return
	 */
	public List<PropertyClassificationEntry> queryPropertyClassificationByParentId(String parentId)
	{
		return propertyClassificationDao.queryPropertyClassificationByParentId(parentId);
	}
	/**
	 * 删除一个学校所有校园资产分类
	 * @param id
	 */
	public void deleteAllPropertyClassifications(ObjectId id){
		propertyClassificationDao.deleteAllPropertyClassifications(id);
	}
}
