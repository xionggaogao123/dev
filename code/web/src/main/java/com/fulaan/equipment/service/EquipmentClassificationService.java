package com.fulaan.equipment.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.db.equipment.EquipmentClassificationDao;
import com.pojo.equipment.EquipmentClassificationEntry;
/**
 * 
 * @author cxy
 *
 */
@Service
public class EquipmentClassificationService {

	private EquipmentClassificationDao equipmentClassificationDao = new EquipmentClassificationDao();
	
	/**
	 * 添加一条校园设备分类信息
	 * @param e
	 * @return
	 */
	public ObjectId addEquipmentClassificationEntry(EquipmentClassificationEntry e)
	{
		return equipmentClassificationDao.addEquipmentClassificationEntry(e);
	}
	
	/**
	 * 根据Id查询一个特定的校园设备分类信息
	 * @param id
	 * @return
	 */
	public EquipmentClassificationEntry getEquipmentClassificationEntry(ObjectId id)
	{
		return equipmentClassificationDao.getEquipmentClassificationEntry(id);
	}
	
	/**
	 * 删除一条校园设备分类
	 * @param id
	 */
	public void deleteEquipmentClassification(String id){
		equipmentClassificationDao.deleteEquipmentClassification(id);
	}
	
	/**
	 * 根据ID更新一条校园设备分类信息
	 */
	public void updateEquipmentClassification(ObjectId id,String equipmentClassificationName,String equipmentClassificationPostscript,
													String equipmentClassificationId,String equipmentClassificationParentId){

		equipmentClassificationDao.updateEquipmentClassification(id, equipmentClassificationName, equipmentClassificationPostscript,
				equipmentClassificationId, equipmentClassificationParentId);
	}
	
	/**
	 * 根据ID更新一条校园设备分类信息的基本信息
	 */
	public void updateEquipmentClassificationBaseInfo(String id,String equipmentClassificationName,String equipmentClassificationPostscript){

		equipmentClassificationDao.updateEquipmentClassificationBaseInfo(id, equipmentClassificationName, equipmentClassificationPostscript);
		
	}
	
	/**
	 * 根据学校ID查询设备信息
	 * @param id
	 * @return
	 */
	public List<EquipmentClassificationEntry> queryEquipmentClassificationBySchoolIdAndEquipmentClassificationId(ObjectId schoolId)
	{
		return equipmentClassificationDao.queryEquipmentClassificationBySchoolIdAndEquipmentClassificationId(schoolId);
	}
	
	/**
	 * 根据父ID查询所有孩子设备信息
	 * @param id
	 * @return
	 */
	public List<EquipmentClassificationEntry> queryEquipmentClassificationByParentId(String parentId)
	{
		return equipmentClassificationDao.queryEquipmentClassificationByParentId(parentId);
	}
	/**
	 * 删除一个学校所有设备分类
	 * @param id
	 */
	public void deleteAllEquipmentClassifications(ObjectId id){
		equipmentClassificationDao.deleteAllEquipmentClassifications(id);
	}
}
